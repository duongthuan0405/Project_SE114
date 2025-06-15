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
                    }).FirstOrDefaultAsync();
                if (quiz == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại" });
                }
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

            try
            {
                var quizzes = await DbContext.Quizzes
                    .Where(q => q.CourseID == course_id)
                    .Select(q => new QuizDTO
                    {
                        Id = q.Id,
                        Name = q.Name,
                        Description = q.Description,
                        StartTime = q.StartTime,
                        DueTime = q.DueTime,
                    }).ToListAsync();

 
                return Ok(quizzes);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy danh sách Quiz" });
            }
        }


    }
}
