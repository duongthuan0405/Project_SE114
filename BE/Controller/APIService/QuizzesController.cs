using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
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
        public QuizController(MyAppDBContext dbContext)
        {
            DbContext = dbContext;
        }

        [HttpGet("{quiz_id}")]
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<QuizDTO>> GetQuizById([FromRoute] string quiz_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var role = User.FindFirstValue(ClaimTypes.Role);
            try
            {
                if(role == StaticClass.RoleId.Student)
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
                    IsPublished = false
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

                return StatusCode(StatusCodes.Status201Created, quizDTO);
            }

            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi tạo Quiz" });
            }
        }

        [HttpPatch("{quiz_id}/publish")]
        [Authorize(StaticClass.RoleId.Teacher)]
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

                quiz.IsPublished = true;
                await DbContext.SaveChangesAsync();
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
                                StatusOfAttempt = aq == null ? StaticClass.StateOfAttemptQuiz.NotFinish : (aq.IsSubmitted ? StaticClass.StateOfAttemptQuiz.Finish : StaticClass.StateOfAttemptQuiz.NotFinish)
                            }
                        ).ToListAsync();
                }
                else if(role == StaticClass.RoleId.Teacher)
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

    }
}
