using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
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
    public class Questions_AnswersController : ControllerBase
    {
        private MyAppDBContext db;
        public Questions_AnswersController(MyAppDBContext dbContext)
        {
            db = dbContext;
        }

        [HttpPost("create")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> CreateQuestion([FromBody] CreateQuestionRequest questionDTO)
        {
            try
            {
                if (questionDTO.LAnswers.Count < 2)
                {
                    return StatusCode(StatusCodes.Status400BadRequest, "Câu hỏi phải có ít nhất 2 đáp án");
                }
                if (questionDTO.LAnswers.Count(a => a.IsCorrect) != 1)
                {
                    return StatusCode(StatusCodes.Status400BadRequest, "Câu hỏi chỉ 1 đáp án đúng");
                }
                if (!await db.Quizzes.AnyAsync(q => q.Id == questionDTO.QuizId && q.IsPublished == true))
                {
                    return StatusCode(StatusCodes.Status404NotFound, "Quiz không tồn tại hoặc chưa được phát hành");
                }

                string id = "";
                do
                {
                    id = StaticClass.CreateId();
                } while (db.Questions.Any(q => q.Id == id));

                var question = new Data.Entities.Question
                {
                    Id = id,
                    Content = questionDTO.Content,
                    QuizId = questionDTO.QuizId
                };

                db.Questions.Add(question);            

                Answer answer = null;

                foreach (CreateAnswerRequest ans in questionDTO.LAnswers)
                {
                    string idans = "";
                    do
                    {
                        idans = StaticClass.CreateId();
                    } while (db.Answers.Any(a => a.Id == idans));

                    answer = new Answer
                    {
                        Id = idans,
                        Content = ans.Content,
                        IsTrue = ans.IsCorrect,
                        QuestionID = question.Id
                    };

                    db.Answers.Add(answer);
                }

                await db.SaveChangesAsync();


                return Created();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Lỗi server khi tạo câu hỏi");
            }
        }

        [HttpGet("{quizId}/get_for_teacher")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<QuestionDTO>>> GetAllQuestionForTeacher([FromRoute] string quizId)
        {
            try
            {
                var questions = await db.Questions
                    .Where(q => q.QuizId == quizId)
                    .Select(q => new QuestionDTO
                    {
                        Id = q.Id,
                        Content = q.Content,
                        LAnswers = q.LAnswers.Select(a => new AnswerDTO
                        {
                            Id = a.Id,
                            Content = a.Content,
                            IsCorrect = a.IsTrue
                        }).ToList()
                    }).ToListAsync();
                return Ok(questions);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Lỗi server khi lấy danh sách câu hỏi");
            }
        }

        [HttpGet("{quizId}/get_for_student")]
        [Authorize(Roles = StaticClass.RoleId.Student)]
        public async Task<ActionResult<List<QuestionDTO>>> GetAllQuestionForStudent([FromRoute] string quizId)
        {
            string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
            try
            {
                var deadline = await db.Quizzes.Where(q => q.Id == quizId).Select(q => q.DueTime).FirstOrDefaultAsync();
                var startTime = await db.Quizzes.Where(q => q.Id == quizId).Select(q => q.StartTime).FirstOrDefaultAsync();
                if (DateTime.UtcNow < startTime)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Quiz chưa bắt đầu" });
                }    
                if(DateTime.UtcNow > deadline)
                {
                    var atqId = await db.AttemptQuizzes
                        .Where(at => at.AccountId == requester && at.QuizId == quizId)
                        .Select(at => at.Id)
                        .FirstOrDefaultAsync() ?? "";

                    var questions = await db.Questions.Where(q => q.QuizId == quizId).Select(
                        qs => new QuestionDTO
                        {
                            Id = qs.Id,
                            Content = qs.Content,
                            LAnswers = qs.LAnswers.Select(a => new AnswerDTO
                            {
                                Id = a.Id,
                                Content = a.Content,
                                IsCorrect = a.IsTrue,
                                IsSelected = db.DetailResults
                                    .Where(dr => dr.AnswerId == a.Id && dr.AttemptQuizId == atqId)
                                    .Any()
                            }).ToList()
                        }
                    ).ToListAsync();

                    return Ok(questions);
                }
                else
                {
                    var atqId = await db.AttemptQuizzes
                        .Where(at => at.AccountId == requester && at.QuizId == quizId)
                        .Select(at => at.Id)
                        .FirstOrDefaultAsync() ?? "";

                    var questions = await db.Questions.Where(q => q.QuizId == quizId).Select(
                        qs => new QuestionDTO
                        {
                            Id = qs.Id,
                            Content = qs.Content,
                            LAnswers = qs.LAnswers.Select(a => new AnswerDTO
                            {
                                Id = a.Id,
                                Content = a.Content,
                                IsSelected = db.DetailResults
                                    .Where(dr => dr.AnswerId == a.Id && dr.AttemptQuizId == atqId)
                                    .Any(),

                                IsCorrect = false
                            }).ToList()
                        }
                    ).ToListAsync();

                    return Ok(questions);
                }    
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Lỗi server khi lấy danh sách câu hỏi");
            }
        }
    }
}
