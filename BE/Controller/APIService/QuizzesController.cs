using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using BE.Email;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Internal;
using System.Security.Claims;

namespace BE.Controller.APIService
{
    [Route($"{StaticClass.AppName}/[controller]")]
    [ApiController]
    public class QuizController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        private readonly IEmailService emailService;
        public QuizController(MyAppDBContext dbContext, IEmailService emailService)
        {
            DbContext = dbContext;
            this.emailService = emailService;
        }

        [HttpGet("{quiz_id}")]
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<QuizDTO>> GetQuizById([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var role = User.FindFirstValue(ClaimTypes.Role);
            try
            {
                var quiz = await DbContext.Quizzes
                    .Where(q => q.Id == quiz_id)
                    .Select(q => new QuizDTO
                    {
                        Id = q.Id,
                        Name = q.Name,
                        Description = q.Description,
                        StartTime = q.StartTime,
                        DueTime = q.DueTime,
                        CourseId = q.CourseID,
                        IsPublished = q.IsPublished
                    }).FirstOrDefaultAsync();

                if (quiz == null || (role == StaticClass.RoleId.Student && !quiz.IsPublished))
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại" });
                }

                if (role == StaticClass.RoleId.Student)
                {
                    // Kiểm tra xem người dùng có tham gia khóa học chứa quiz này không
                    var isEnrolled = await DbContext.JoinCourses
                        .AnyAsync(jc => jc.AccountID == requester && jc.CourseID == DbContext.Quizzes
                            .Where(q => q.Id == quiz_id)
                            .Select(q => q.CourseID)
                            .FirstOrDefault() && jc.State == (int)JoinCourse.JoinCourseState.Joined);
                    if (!isEnrolled)
                    {
                        return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập Quiz này" });
                    }
                }
                else if (role == StaticClass.RoleId.Teacher)
                {
                    // Kiểm tra xem người dùng có quyền truy cập Quiz này thông qua khóa học
                    var isTeacherOfCourse = await DbContext.Courses
                        .AnyAsync(c => c.HostId == requester && c.Id == DbContext.Quizzes.Where(q => q.Id == quiz_id).Select(q => q.CourseID).FirstOrDefault());
                    if (!isTeacherOfCourse)
                    {
                        return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập Quiz này" });
                    }
                }


                if (role == StaticClass.RoleId.Student)
                {
                    AttemptQuiz? atquiz = await DbContext.AttemptQuizzes
                        .Where(aq => aq.AccountId == requester && aq.QuizId == quiz_id)
                        .FirstOrDefaultAsync();
                    // Lấy trạng thái của người dùng đối với Quiz này
                    if (atquiz == null)
                    {
                        quiz.StatusOfAttempt = StaticClass.StateOfAttemptQuiz.NotFinish;
                    }
                    else
                    {
                        if (atquiz.IsSubmitted)
                            quiz.StatusOfAttempt = StaticClass.StateOfAttemptQuiz.Finish;
                        else
                            quiz.StatusOfAttempt = StaticClass.StateOfAttemptQuiz.NotFinish;
                    }
                }
                else
                {
                    quiz.StatusOfAttempt = "";
                }


                quiz.CourseName = await DbContext.Courses
                .Where(c => c.Id == quiz.CourseId)
                .Select(c => c.Name)
                .FirstOrDefaultAsync() ?? "";

                return Ok(quiz);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy thông tin Quiz" });
            }
        }

        [HttpGet("course/{course_id}")]
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<List<QuizDTO>>> GetQuizzesInCourse([FromRoute] string course_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var role = User.FindFirstValue(ClaimTypes.Role);

            try
            {
                List<QuizDTO> quizzes = new List<QuizDTO>();

                if (role == StaticClass.RoleId.Student)
                {
                    // Kiểm tra xem người dùng có tham gia khóa học này không
                    var isEnrolled = await DbContext.JoinCourses
                        .AnyAsync(jc => jc.AccountID == requester && jc.CourseID == course_id && jc.State == (int)JoinCourse.JoinCourseState.Joined);
                    if (!isEnrolled)
                    {
                        return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập Quiz trong khóa học này" });
                    }
                    // Chỉ lấy các quiz đã được publish
                    quizzes = await DbContext.Quizzes
                    .Where(q => q.CourseID == course_id && q.IsPublished)
                    .GroupJoin(
                        DbContext.AttemptQuizzes.Where(aq => aq.AccountId == requester),
                        q => q.Id,
                        aq => aq.QuizId,
                        (q, i_at) => new { q, i_at }
                    )
                    .SelectMany(
                        x => x.i_at.DefaultIfEmpty(),
                        (x, aq) => new QuizDTO
                        {
                            Id = x.q.Id,
                            Name = x.q.Name,
                            Description = x.q.Description,
                            StartTime = x.q.StartTime,
                            DueTime = x.q.DueTime,
                            CourseId = x.q.CourseID,
                            IsPublished = x.q.IsPublished,
                            CourseName = DbContext.Courses
                                .Where(c => c.Id == x.q.CourseID)
                                .Select(c => c.Name)
                                .FirstOrDefault() ?? "",
                            StatusOfAttempt = aq == null ? StaticClass.StateOfAttemptQuiz.NotFinish : (aq.IsSubmitted ? StaticClass.StateOfAttemptQuiz.Finish : StaticClass.StateOfAttemptQuiz.NotFinish)
                        }
                    ).ToListAsync();

                }
                else
                {
                    // Lấy tất cả quiz nếu là giáo viên hoặc quản trị viên
                    if (role == StaticClass.RoleId.Teacher)
                    {
                        // Kiểm tra xem người dùng có quyền truy cập Quiz này thông qua khóa học
                        var isTeacherOfCourse = await DbContext.Courses
                            .AnyAsync(c => c.HostId == requester && c.Id == course_id);
                        if (!isTeacherOfCourse)
                        {
                            return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập Quiz trong khóa học này" });
                        }
                    }
                    quizzes = await DbContext.Quizzes
                        .Where(q => q.CourseID == course_id)
                        .Select(q => new QuizDTO
                        {
                            Id = q.Id,
                            Name = q.Name,
                            Description = q.Description,
                            StartTime = q.StartTime,
                            DueTime = q.DueTime,
                            CourseId = q.CourseID,
                            IsPublished = q.IsPublished,
                            CourseName = DbContext.Courses
                                .Where(c => c.Id == q.CourseID)
                                .Select(c => c.Name)
                                .FirstOrDefault() ?? "",
                            StatusOfAttempt = ""
                        }).ToListAsync();
                }
                return Ok(quizzes);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy danh sách Quiz" });
            }
        }

        [HttpPost("create")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<QuizDTO>> CreateQuiz([FromBody] QuizCreateRequestDTO quizCreateRequest)
        {
            try
            {
                if (DateTime.Now >= quizCreateRequest.StartTime)
                {
                    return BadRequest(new { Message = "Thời gian bắt đầu phải sau thời điểm hiện tại" });
                }
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (quizCreateRequest == null || string.IsNullOrEmpty(quizCreateRequest.Name) || string.IsNullOrEmpty(quizCreateRequest.CourseId))
                {
                    return BadRequest(new { Message = "Thông tin Quiz không hợp lệ" });
                }
                if (quizCreateRequest.StartTime >= quizCreateRequest.DueTime)
                {
                    return BadRequest(new { Message = "Thời gian bắt đầu phải trước thời gian kết thúc" });
                }
                Course? course = await DbContext.Courses
                    .Where(c => c.Id == quizCreateRequest.CourseId && c.HostId == requester)
                    .FirstOrDefaultAsync();

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền tạo Quiz cho khóa học này" });
                }

                var newQuiz = new Data.Entities.Quiz
                {
                    Name = quizCreateRequest.Name,
                    Description = quizCreateRequest.Description,
                    StartTime = quizCreateRequest.StartTime,
                    DueTime = quizCreateRequest.DueTime,
                    CourseID = quizCreateRequest.CourseId,
                    IsPublished = quizCreateRequest.IsPublished
                };

                string id = "";

                do
                {
                    id = StaticClass.CreateId();
                }
                while (await DbContext.Quizzes.AnyAsync(q => q.Id == id));

                newQuiz.Id = id;

                DbContext.Quizzes.Add(newQuiz);
                await DbContext.SaveChangesAsync();

                var quizDTO = new QuizDTO
                {
                    Id = newQuiz.Id,
                    Name = newQuiz.Name,
                    Description = newQuiz.Description,
                    StartTime = newQuiz.StartTime,
                    DueTime = newQuiz.DueTime,
                    CourseId = newQuiz.CourseID,
                    IsPublished = newQuiz.IsPublished,
                    CourseName = await DbContext.Courses
                        .Where(c => c.Id == newQuiz.CourseID)
                        .Select(c => c.Name)
                        .FirstOrDefaultAsync() ?? "",
                    StatusOfAttempt = ""
                };

                if(quizCreateRequest.IsPublished)
                {
                    var emailMembers = await DbContext.JoinCourses
                    .Where(jc => jc.CourseID == quizCreateRequest.CourseId && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                    .Join(DbContext.AccountAuthens, jc => jc.AccountID, aa => aa.Id, (jc, aa) => new { jc, aa })
                    .Select(x => x.aa.Email)
                    .ToListAsync();
                    
                    string subject = $"[Quiz mới] {newQuiz.Name} đã được tạo";
                    string htmlContent = $@"
                        <p>Chào bạn,</p>
                        <p>Một quiz mới đã được tạo trong khóa học <b>{quizDTO.CourseName}</b>:</p>
                        <ul>
                            <li><b>Tên:</b> {newQuiz.Name}</li>
                            <li><b>Mô tả:</b> {newQuiz.Description}</li>
                            <li><b>Thời gian bắt đầu:</b> {newQuiz.StartTime:HH:mm dd/MM/yyyy}</li>
                            <li><b>Thời gian kết thúc:</b> {newQuiz.DueTime:HH:mm dd/MM/yyyy}</li>
                        </ul>
                        <p>Vui lòng đăng nhập để làm bài đúng hạn.</p>";

                    foreach (var email in emailMembers)
                    {
                        try
                        {
                            emailService.SendAsync(email, subject, htmlContent);
                        }
                        catch (Exception e)
                        {
                            
                        }
                    }
                }

                return StatusCode(StatusCodes.Status201Created, quizDTO);
            }

            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi tạo Quiz" });
            }
        }

        [HttpPatch("{quiz_id}/publish")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> PublishQuiz([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

            try
            {
                var quiz = await DbContext.Quizzes.FindAsync(quiz_id);
                if (quiz == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại" });
                }
                var c = await DbContext.Courses.Where(c => quiz.CourseID == c.Id && c.HostId == requester).FirstOrDefaultAsync();
                if (c == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền công bố Quiz này" });
                }

                if (quiz.StartTime <= DateTime.Now)
                {
                    return BadRequest(new { Message = "Thời gian bắt đầu phải sau thời điểm hiện tại" });
                }

                quiz.IsPublished = true;
                await DbContext.SaveChangesAsync();

                var emailMembers = await DbContext.JoinCourses
                    .Where(jc => jc.CourseID == quiz.CourseID && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                    .Join(DbContext.AccountAuthens, jc => jc.AccountID, aa => aa.Id, (jc, aa) => new { jc, aa })
                    .Select(x => x.aa.Email)
                    .ToListAsync();
                    
                    string subject = $"[Quiz mới] {quiz.Name} đã được tạo";
                    string htmlContent = $@"
                        <p>Chào bạn,</p>
                        <p>Một quiz mới đã được tạo trong khóa học <b>{ await DbContext.Courses.Where(c => c.Id == quiz.CourseID).Select(c => c.Name).FirstOrDefaultAsync()}</b>:</p>
                        <ul>
                            <li><b>Tên:</b> {quiz.Name}</li>
                            <li><b>Mô tả:</b> {quiz.Description}</li>
                            <li><b>Thời gian bắt đầu:</b> {quiz.StartTime:HH:mm dd/MM/yyyy}</li>
                            <li><b>Thời gian kết thúc:</b> {quiz.DueTime:HH:mm dd/MM/yyyy}</li>
                        </ul>
                        <p>Vui lòng đăng nhập để làm bài đúng hạn.</p>";

                    foreach (var email in emailMembers)
                    {
                        try
                        {
                            emailService.SendAsync(email, subject, htmlContent);
                        }
                        catch (Exception e)
                        {
                            
                        }
                    }
                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi công bố Quiz" });
            }
        }

        [HttpPut("{quiz_id}/update")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<QuizDTO>> UpdateQuiz([FromRoute] string quiz_id, [FromBody] QuizCreateRequestDTO quizUpdateRequest)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

            try
            {
                if (DateTime.Now >= quizUpdateRequest.StartTime)
                {
                    return BadRequest(new { Message = "Thời gian bắt đầu phải sau thời điểm hiện tại" });
                }
                if (quizUpdateRequest == null || string.IsNullOrEmpty(quizUpdateRequest.Name) || string.IsNullOrEmpty(quizUpdateRequest.CourseId))
                {
                    return BadRequest(new { Message = "Thông tin Quiz không hợp lệ" });
                }
                if (quizUpdateRequest.StartTime >= quizUpdateRequest.DueTime)
                {
                    return BadRequest(new { Message = "Thời gian bắt đầu phải trước thời gian kết thúc" });
                }

                var quiz = await DbContext.Quizzes.FindAsync(quiz_id);
                if (quiz == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại" });
                }

                var course = await DbContext.Courses
                    .Where(c => c.Id == quiz.CourseID && c.HostId == requester)
                    .FirstOrDefaultAsync();

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền sửa Quiz này" });
                }

                quiz.Name = quizUpdateRequest.Name;
                quiz.Description = quizUpdateRequest.Description;
                quiz.StartTime = quizUpdateRequest.StartTime;
                quiz.DueTime = quizUpdateRequest.DueTime;
                quiz.IsPublished = quizUpdateRequest.IsPublished;

                await DbContext.SaveChangesAsync();

                var updatedQuizDTO = new QuizDTO
                {
                    Id = quiz.Id,
                    Name = quiz.Name,
                    Description = quiz.Description,
                    StartTime = quiz.StartTime,
                    DueTime = quiz.DueTime,
                    CourseId = quiz.CourseID,
                    IsPublished = quiz.IsPublished,
                    CourseName = await DbContext.Courses
                        .Where(c => c.Id == quiz.CourseID)
                        .Select(c => c.Name)
                        .FirstOrDefaultAsync() ?? "",
                    StatusOfAttempt = ""
                };

                if(quizUpdateRequest.IsPublished)
                {
                    var emailMembers = await DbContext.JoinCourses
                    .Where(jc => jc.CourseID == quiz.CourseID && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                    .Join(DbContext.AccountAuthens, jc => jc.AccountID, aa => aa.Id, (jc, aa) => new { jc, aa })
                    .Select(x => x.aa.Email)
                    .ToListAsync();
                    
                    string subject = $"[Quiz cập nhật] {quiz.Name} đã được cập nhật";
                    string htmlContent = $@"
                        <p>Chào bạn,</p>
                        <p>Quiz trong khóa học <b>{updatedQuizDTO.CourseName}</b> đã được cập nhật:</p>
                        <ul>
                            <li><b>Tên:</b> {quiz.Name}</li>
                            <li><b>Mô tả:</b> {quiz.Description}</li>
                            <li><b>Thời gian bắt đầu:</b> {quiz.StartTime:HH:mm dd/MM/yyyy}</li>
                            <li><b>Thời gian kết thúc:</b> {quiz.DueTime:HH:mm dd/MM/yyyy}</li>
                        </ul>
                        <p>Vui lòng đăng nhập để làm bài đúng hạn.</p>";

                    foreach (var email in emailMembers)
                    {
                        try
                        {
                            emailService.SendAsync(email, subject, htmlContent);
                        }
                        catch (Exception e)
                        {
                            
                        }
                    }
                }

                return Ok(updatedQuizDTO);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi cập nhật Quiz" });
            }
        }

        [HttpGet("all")]
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<List<QuizDTO>>> GetAllQuizzes()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var role = User.FindFirstValue(ClaimTypes.Role);

            try
            {
                List<QuizDTO> quizzes = new List<QuizDTO>();

                if (role == StaticClass.RoleId.Student)
                {
                    quizzes = await DbContext.Quizzes
                        .Where(q => q.IsPublished && DbContext.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == q.CourseID && jc.State == (int)JoinCourse.JoinCourseState.Joined))
                        .GroupJoin(
                            DbContext.AttemptQuizzes.Where(aq => aq.AccountId == requester),
                            q => q.Id,
                            aq => aq.QuizId,
                            (q, i_at) => new { q, i_at }
                        )
                        .SelectMany(
                            x => x.i_at.DefaultIfEmpty(),
                            (x, aq) => new QuizDTO
                            {
                                Id = x.q.Id,
                                Name = x.q.Name,
                                Description = x.q.Description,
                                StartTime = x.q.StartTime,
                                DueTime = x.q.DueTime,
                                CourseId = x.q.CourseID,
                                IsPublished = x.q.IsPublished,
                                CourseName = DbContext.Courses
                                    .Where(c => c.Id == x.q.CourseID)
                                    .Select(c => c.Name)
                                    .FirstOrDefault() ?? "",
                                StatusOfAttempt = aq == null ? StaticClass.StateOfAttemptQuiz.NotAttempted : (aq.IsSubmitted ? StaticClass.StateOfAttemptQuiz.Finish : StaticClass.StateOfAttemptQuiz.NotFinish)
                            }
                        ).ToListAsync();
                }
                else if (role == StaticClass.RoleId.Teacher)
                {
                    quizzes = await DbContext.Quizzes
                        .Where(q => DbContext.Courses.Any(c => c.HostId == requester && c.Id == q.CourseID))
                        .Select(q => new QuizDTO
                        {
                            Id = q.Id,
                            Name = q.Name,
                            Description = q.Description,
                            StartTime = q.StartTime,
                            DueTime = q.DueTime,
                            CourseId = q.CourseID,
                            IsPublished = q.IsPublished,
                            CourseName = DbContext.Courses
                                .Where(c => c.Id == q.CourseID)
                                .Select(c => c.Name)
                                .FirstOrDefault() ?? "",
                            StatusOfAttempt = ""
                        }).ToListAsync();
                }
                else // Admin
                {
                    quizzes = await DbContext.Quizzes
                        .Select(q => new QuizDTO
                        {
                            Id = q.Id,
                            Name = q.Name,
                            Description = q.Description,
                            StartTime = q.StartTime,
                            DueTime = q.DueTime,
                            CourseId = q.CourseID,
                            IsPublished = q.IsPublished,
                            CourseName = DbContext.Courses
                                .Where(c => c.Id == q.CourseID)
                                .Select(c => c.Name)
                                .FirstOrDefault() ?? "",
                            StatusOfAttempt = ""
                        }).ToListAsync();
                }

                return Ok(quizzes);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy danh sách Quiz" });
            }
        }


        [HttpDelete("{quiz_id}/delete")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> DeleteQuiz([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

            var tx = await DbContext.Database.BeginTransactionAsync();

            try
            {
                var quiz = await DbContext.Quizzes.FindAsync(quiz_id);
                if (quiz == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại" });
                }

                var course = await DbContext.Courses
                    .Where(c => c.Id == quiz.CourseID && c.HostId == requester)
                    .FirstOrDefaultAsync();

                if (DateTime.Now >= quiz.StartTime && quiz.IsPublished)
                {
                    return BadRequest(new { Message = "Không thể xóa Quiz đã bắt đầu" });
                }

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền xóa Quiz này" });
                }

                var questions = await DbContext.Questions
                    .Where(q => q.QuizId == quiz_id)
                    .ToListAsync();
                DbContext.Questions.RemoveRange(questions);
                await DbContext.SaveChangesAsync();

                DbContext.Quizzes.Remove(quiz);
                await DbContext.SaveChangesAsync();

                await tx.CommitAsync();

                return Ok(new { Message = "Quiz đã được xóa thành công" });
            }
            catch (Exception ex)
            {
                await tx.RollbackAsync();
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi xóa Quiz" });
            }
        }

        [HttpGet("{quiz_id}/quiz_with_result")]
        [Authorize(Roles = StaticClass.RoleId.Student)]
        public async Task<ActionResult<QuizWithScoreDTO>> GetQuizWithResult([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var role = User.FindFirstValue(ClaimTypes.Role);

            try
            {
                var quiz = await DbContext.Quizzes.AsNoTracking().Where(q => q.Id == quiz_id)
                    .Select(q => new QuizDTO
                    {
                        Id = q.Id,
                        Name = q.Name,
                        Description = q.Description,
                        StartTime = q.StartTime,
                        DueTime = q.DueTime,
                        CourseId = q.CourseID,
                        IsPublished = q.IsPublished
                    }).FirstOrDefaultAsync();

                if (quiz == null || !quiz.IsPublished)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại hoặc chưa được công bố" });
                }
                // Kiểm tra xem người dùng có tham gia khóa học chứa quiz này không
                var isEnrolled = await DbContext.JoinCourses
                    .AnyAsync(jc => jc.AccountID == requester && jc.CourseID == quiz.CourseId && jc.State == (int)JoinCourse.JoinCourseState.Joined);
                if (!isEnrolled)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập Quiz này" });
                }
                AttemptQuiz? atquiz = await DbContext.AttemptQuizzes
                    .Where(aq => aq.AccountId == requester && aq.QuizId == quiz_id)
                    .FirstOrDefaultAsync();

                var TotalQuestions = await DbContext.Questions
                .Where(q => q.QuizId == quiz_id)
                .CountAsync();
                if (atquiz == null)
                {
                    return Ok(new QuizWithScoreDTO
                    {
                        Quiz = quiz,
                        TotalCorrectAnswer = 0,
                        IsSubmitted = false,
                        TotalQuestions = TotalQuestions
                    });
                }
                if (!atquiz.IsSubmitted)
                {
                    return Ok(new QuizWithScoreDTO
                    {
                        Quiz = quiz,
                        TotalCorrectAnswer = 0,
                        IsSubmitted = false,
                        TotalQuestions = TotalQuestions
                    });
                }

                int TotalCorrectAnswer = 0;

                QuizWithScoreDTO quizWithScore = new QuizWithScoreDTO
                {
                    Quiz = quiz,
                    TotalCorrectAnswer = TotalCorrectAnswer,
                    IsSubmitted = atquiz.IsSubmitted,
                    TotalQuestions = TotalQuestions
                };

                if (DateTime.Now < quiz.DueTime)
                {
                    TotalCorrectAnswer = 0;
                }
                else
                {
                    quizWithScore.IsSubmitted = true;
                    TotalCorrectAnswer = await DbContext.DetailResults.Where(dr => dr.AttemptQuizId == atquiz.Id)
                    .Join(DbContext.Answers, dr => dr.AnswerId, a => a.Id, (dr, a) => new { dr, a })
                    .CountAsync(x => x.a.IsTrue);
                    quizWithScore.TotalCorrectAnswer = TotalCorrectAnswer;
                }                

                return Ok(quizWithScore);


            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy thông tin Quiz" });
            }
        }

        [HttpGet("{quiz_id}/all_results")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<AccountWithScore>>> GetAllResultsOfQuiz([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            try
            {
                var quiz = await DbContext.Quizzes.FindAsync(quiz_id);
                if (quiz == null || !quiz.IsPublished)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại hoặc chưa được công bố" });
                }

                var course = await DbContext.Courses
                    .Where(c => c.Id == quiz.CourseID && c.HostId == requester)
                    .FirstOrDefaultAsync();

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền truy cập kết quả Quiz này" });
                }

                int TotalQuestions = await DbContext.Questions
                    .Where(q => q.QuizId == quiz_id)
                    .CountAsync();

                var results = await DbContext.AccountAuthens.Join(DbContext.Accounts, at => at.Id, a => a.Id, (at, a) => new { at, a })
                        .Join(DbContext.JoinCourses, x => x.a.Id, jc => jc.AccountID, (x, jc) => new { at = x.at, a = x.a, jc = jc })
                        .Join(DbContext.Quizzes.Where(q => q.Id == quiz_id), x => x.jc.CourseID, q => q.CourseID, (x, q) => new { a = x.a, at = x.at, q = q })
                        .GroupJoin(DbContext.AttemptQuizzes, x => x.q.Id, aq => aq.QuizId, (x, ls_at) => new { x = x, ls_at = ls_at.DefaultIfEmpty() })
                        .SelectMany(x => x.ls_at, (x, at) => new { x = x.x, atq = at }).ToListAsync();

                List<AccountWithScore> accountWithScores = new List<AccountWithScore>();
                foreach (var r in results)
                {
                    if (r.atq == null)
                    {
                        accountWithScores.Add(new AccountWithScore
                        {
                            Account = new AccountInfoDTO(r.x.a.Id, r.x.at.Email, r.x.a.LastMiddleName, r.x.a.FirstName, r.x.a.Avatar, r.x.a.AccountTypeId, ""),
                            TotalCorrectAnswer = 0,
                            TotalQuestions = TotalQuestions,
                            IsSubmitted = false,
                            StartedAt = null,
                            FinishedAt = null
                        });
                    }
                    else
                    {
                        var totalCorrectAnswer = await DbContext.DetailResults.Where(dr => dr.AttemptQuizId == r.atq.Id)
                            .Join(DbContext.Answers, dr => dr.AnswerId, a => a.Id, (dr, a) => new { dr, a })
                            .CountAsync(x => x.a.IsTrue);

                        accountWithScores.Add(new AccountWithScore
                        {
                            Account = new AccountInfoDTO(r.x.a.Id, r.x.at.Email, r.x.a.LastMiddleName, r.x.a.FirstName, r.x.a.Avatar, r.x.a.AccountTypeId, ""),
                            TotalCorrectAnswer = totalCorrectAnswer,
                            TotalQuestions = TotalQuestions,
                            IsSubmitted = r.atq.IsSubmitted,
                            StartedAt = r.atq.AttemptTime,
                            FinishedAt = r.atq.IsSubmitted ? r.atq.FinishTime : null
                        });
                    }
                }

                accountWithScores.Sort((x, y) =>
                {
                    if (x.TotalCorrectAnswer != y.TotalCorrectAnswer)
                        return x.TotalCorrectAnswer.CompareTo(y.TotalCorrectAnswer);
                    return (x.FinishedAt < y.FinishedAt) ? 1 : x.Account.FirstName.CompareTo(y.Account.FirstName);
                });

                return Ok(accountWithScores);
                                     
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy kết quả Quiz" });
            }
        }
    }
}
