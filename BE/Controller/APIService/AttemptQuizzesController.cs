using BE.ConstanctValue;
using BE.Data.Database;
using BE.DTOs;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Text;


namespace BE.Controller.APIService
{
    [Route($"{StaticClass.AppName}/[controller]")]
    [ApiController]
    public class AttemptQuizzesController : ControllerBase
    {
        MyAppDBContext db;
        IConfiguration Configuration;
        public AttemptQuizzesController(MyAppDBContext dbContext, IConfiguration configuration)
        {
            db = dbContext;
            Configuration = configuration;
        }

        [HttpPost("{quiz_id}/attempt")]
        [Authorize(Roles = StaticClass.RoleId.Student)]
        public async Task<ActionResult<AttemptQuizDTO>> AttempQuiz([FromRoute] string quiz_id)
        {
            string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
            if (!await db.Quizzes.AnyAsync(q => q.Id.Equals(quiz_id) && q.IsPublished == true))
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Quiz không tồn tại hoặc chưa được phát hành" });
            }

            if (await db.Quizzes.AnyAsync(q => q.Id == quiz_id && q.DueTime < DateTime.Now))
            {
                return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Quiz đã hết thời gian làm bài" });
            }

            if (await db.Quizzes.AnyAsync(q => q.Id == quiz_id && q.StartTime > DateTime.Now))
            {
                return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Quiz chưa bắt đầu" });
            }

            var aq = await db.AttemptQuizzes.Where(aq => aq.AccountId == requester && aq.QuizId == quiz_id).FirstOrDefaultAsync();

            if (aq != null && aq.IsSubmitted)
            {
                return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn đã nộp bài làm cho quiz này" });
            }

            var claims = new Claim[]
            {
                new Claim(ClaimTypes.NameIdentifier, requester),
                new Claim(ClaimTypes.Role, StaticClass.RoleId.Student),
            };

            var jwt = Configuration.GetSection("Jwt");
            var keyBytes = Encoding.UTF8.GetBytes(jwt["Key"]);
            var key = new SymmetricSecurityKey(keyBytes);
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
            var expires = await db.Quizzes
                .Where(q => q.Id == quiz_id)
                .Select(q => q.DueTime)
                .FirstOrDefaultAsync(); 

            var token = new JwtSecurityToken(
                issuer: jwt["Issuer"],
                audience: jwt["Audience"],
                claims: claims,
                expires: DateTime.SpecifyKind(expires, DateTimeKind.Local).ToUniversalTime(),
                signingCredentials: creds
            );

            var tokenForQuiz = new JwtSecurityTokenHandler().WriteToken(token);

            if (aq != null)
            {
                AttemptQuizDTO atq = new AttemptQuizDTO
                {
                    Id = aq.Id,
                    QuizId = aq.QuizId,
                    AccountId = aq.AccountId,
                    AttemptTime = aq.AttemptTime,
                    FinishTime = aq.FinishTime,
                    IsSubmitted = aq.IsSubmitted,
                    TokenForQuiz = tokenForQuiz
                };

                atq.QuizName = await db.Quizzes.Where(q => q.Id == quiz_id)
                    .Select(q => q.Name)
                    .FirstOrDefaultAsync() ?? "";

                atq.CourseName = await db.Quizzes.Join(db.Courses, q => q.CourseID, c => c.Id, (q, c) => new { q, c })
                    .Where(qc => qc.q.Id == quiz_id)
                    .Select(qc => qc.c.Name)
                    .FirstOrDefaultAsync() ?? "";

                return Ok(atq);
            }

            string id = StaticClass.CreateId();
            while (db.AttemptQuizzes.Any(aq => aq.Id == id))
            {
                id = StaticClass.CreateId();
            }

            var attemptQuiz = new Data.Entities.AttemptQuiz
            {
                Id = id,
                AccountId = requester,
                QuizId = quiz_id,
                AttemptTime = DateTime.Now,
                FinishTime = db.Quizzes.Where(q => q.Id == quiz_id)
                    .Select(q => q.DueTime)
                    .FirstOrDefault(),
                IsSubmitted = false
            };

            db.AttemptQuizzes.Add(attemptQuiz);
            await db.SaveChangesAsync();

            return Ok(new AttemptQuizDTO
            {
                Id = id,
                QuizId = quiz_id,
                AccountId = requester,
                AttemptTime = attemptQuiz.AttemptTime,
                FinishTime = attemptQuiz.FinishTime,
                IsSubmitted = attemptQuiz.IsSubmitted,
                TokenForQuiz = tokenForQuiz
            });
        }

        [HttpPatch("{quiz_id}/submit")]
        [Authorize(Roles = StaticClass.RoleId.Student)]
        public async Task<ActionResult> SubmitQuiz([FromRoute] string quiz_id)
        {
            string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
            var deadline = await db.Quizzes.Where(q => q.Id == quiz_id)
                .Select(q => q.DueTime)
                .FirstOrDefaultAsync();

            var attemptQuiz = await db.AttemptQuizzes.Where(aq => aq.AccountId == requester && aq.QuizId == quiz_id && !aq.IsSubmitted)
            .FirstOrDefaultAsync();
            if (attemptQuiz == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Không tìm thấy bài làm" });
            }
            if (DateTime.Now > deadline)
            {
                return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Quiz đã hết thời gian làm bài, bài làm của bạn đã tự động nộp" });
            }

            attemptQuiz.IsSubmitted = true;
            attemptQuiz.FinishTime = DateTime.Now;
            await db.SaveChangesAsync();
            return Ok();
        }
    

    }
}
