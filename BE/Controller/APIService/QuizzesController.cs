using BE.ConstanctValue;
using BE.Data.Database;
using BE.DTOs;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
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
                
                if(role == StaticClass.RoleId.Student)
                {
                    // Chỉ lấy các quiz đã được publish
                    quizzes = await DbContext.Quizzes
                        .Where(q => q.CourseID == course_id && q.IsPublished)
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
                                .FirstOrDefault() ?? ""
                        }).ToListAsync();
                }
                else
                {
                    // Lấy tất cả quiz nếu là giáo viên hoặc quản trị viên
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
                                .FirstOrDefault() ?? ""
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
        [Authorize(StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<QuizDTO>> CreateQuiz([FromBody] QuizCreateRequestDTO quizCreateRequest)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (quizCreateRequest == null || string.IsNullOrEmpty(quizCreateRequest.Name) || string.IsNullOrEmpty(quizCreateRequest.CourseId))
            {
                return BadRequest(new { Message = "Thông tin Quiz không hợp lệ" });
            }
            try
            {
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

                {
                    id = StaticClass.CreateId();
                }
                while (await DbContext.Quizzes.AnyAsync(q => q.Id == id)) ;

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
                        .FirstOrDefaultAsync() ?? ""
                };

                return StatusCode(StatusCodes.Status201Created, quizDTO);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi tạo Quiz" });
            }
        }
    }
}
