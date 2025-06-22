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

        [HttpPut("create")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> CreateQuestion([FromBody] List<CreateQuestionRequest> questionsDTO)
        {
            string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
            if (questionsDTO == null || questionsDTO.Count == 0)
            {
                return StatusCode(StatusCodes.Status400BadRequest, "Danh sách câu hỏi không được để trống");
            }

            var c = await db.Quizzes.Join(db.Courses, q => q.CourseID, c => c.Id, (q, c) => new { q, c })
                .Where(x => x.q.Id == questionsDTO[0].QuizId && x.c.HostId == requester)
                .Select(x => x.c)
                .FirstOrDefaultAsync();
            if (c == null)
            {
                return StatusCode(StatusCodes.Status403Forbidden, "Bạn không có quyền tạo câu hỏi cho khóa học này");
            }

            foreach (CreateQuestionRequest questionDTO in questionsDTO)
            {
                if (questionDTO.LAnswers.Count < 2)
                {
                    return StatusCode(StatusCodes.Status400BadRequest, "Câu hỏi phải có ít nhất 2 đáp án");
                }
                if (questionDTO.LAnswers.Count(a => a.IsCorrect) != 1)
                {
                    return StatusCode(StatusCodes.Status400BadRequest, "Câu hỏi chỉ 1 đáp án đúng");
                }
                   
            }

            await using var tx = await db.Database.BeginTransactionAsync();
            try
            {
                string quiz_id = questionsDTO[0]?.QuizId ?? "";
                Quiz? q = await db.Quizzes.Where(q => q.Id == quiz_id).FirstOrDefaultAsync();
                if(q == null)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bài quiz không tồn tại" });
                }    

                if(DateTime.Now > q.StartTime)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Không thể sửa câu hỏi khi quiz đã bắt đầu" });
                }    

                db.Questions.RemoveRange(await db.Questions.Where(q => q.QuizId == quiz_id).ToListAsync());
                await db.SaveChangesAsync();

                foreach (var questionDTO in questionsDTO)
                {
                    string id = "";
                    do
                    {
                        id = StaticClass.CreateId();
                    } while (await db.Questions.AnyAsync(q => q.Id == id));

                    var question = new Data.Entities.Question
                    {
                        Id = id,
                        Content = questionDTO.Content,
                        QuizId = questionDTO.QuizId
                    };

                    db.Questions.Add(question);

                    List<Answer> answers = new List<Answer>();

                    foreach (CreateAnswerRequest ans in questionDTO.LAnswers)
                    {
                        string idans = "";
                        do
                        {
                            idans = StaticClass.CreateId();
                        } while (db.Answers.Any(a => a.Id == idans));

                        answers.Add
                            (new Answer
                                {
                                    Id = idans,
                                    Content = ans.Content,
                                    IsTrue = ans.IsCorrect,
                                    QuestionID = question.Id
                                }
                            );
                    }

                    db.Answers.AddRange(answers);

                }
                await db.SaveChangesAsync();
                await tx.CommitAsync();

                return Ok();
            }
            catch (Exception ex)
            {
                await tx.RollbackAsync();
                return StatusCode(StatusCodes.Status500InternalServerError, "Lỗi server khi tạo câu hỏi");
            }
        }

        [HttpGet("{quizId}/get_for_teacher")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<QuestionDTO>>> GetAllQuestionForTeacher([FromRoute] string quizId)
        {
            try
            {
                string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
                var isHosted = await db.Quizzes.Join(db.Courses, q => q.CourseID, c => c.Id, (q, c) => new { q, c })
                    .AnyAsync(x => x.q.Id == quizId && x.c.HostId == requester);
                  

                if (!isHosted)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, "Bạn không có quyền truy cập vào khóa học này");
                }

                var questions = await db.Questions.AsNoTracking()
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
                if (DateTime.Now < startTime)
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Quiz chưa bắt đầu" });
                }    
                if(DateTime.Now > deadline)
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
