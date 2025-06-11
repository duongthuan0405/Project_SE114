using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BE.Controller.APIService
{
    [Route($"{ConstantValue.AppName}/[controller]")]
    [ApiController]
    public class CourseController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        public CourseController(MyAppDBContext context)
        {
            DbContext = context;
        }

        [HttpGet("course/{account_id}")]
        [Authorize(Roles = ConstantValue.Role.Student + "," + ConstantValue.Role.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursThatAccountJoin(string account_id)
        {
            try
            {
                List<CourseDTO> courses = await DbContext.Courses.Where(
                    (c) => DbContext.JoinCourses.Any(jc => jc.AccountID == account_id && jc.CourseID == c.Id)
                ).Join(DbContext.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.LastMiddleName + " " + a.FirstName, c.Avatar))
                .ToListAsync();

                return Ok(courses);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new 
                    {
                        Message = "Lỗi server khi lấy danh sách khóa học" 
                    }
                );
            }
        }
    }
}
