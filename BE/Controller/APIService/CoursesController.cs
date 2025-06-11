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
    [Route($"{StaticClass.AppName}/[controller]")]
    [ApiController]
    // tqt/courses
    public class CoursesController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        public CoursesController(MyAppDBContext context)
        {
            DbContext = context;
        }

        [HttpGet("{account_id}/enrolled")]
        [Authorize(Roles = StaticClass.Role.Student + "," + StaticClass.Role.Teacher)]
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

        [HttpPut("create/withhost/{host_id}")]
        [Authorize(Roles = StaticClass.Role.Teacher)]
        public async Task<ActionResult<ResponseCreateCourseDTO>> CreateCourse([FromRoute] string host_id, RequestCreateCourseDTO request)
        {
            string id = "";
            do
            {
                id = StaticClass.CreateId();
            } while (await DbContext.Courses.AnyAsync(c => c.Id == id));

            Course newCourse = new Course()
            {
                Id = id,
                Name = request.Name,
                HostId = host_id,
                Avatar = request.Avatar,
                Description = request.Description
            };

            try
            {
                await DbContext.Courses.AddAsync(newCourse);
                await DbContext.SaveChangesAsync();
                return StatusCode(StatusCodes.Status201Created, new ResponseCreateCourseDTO()
                {
                    Id = newCourse.Id
                }
                );
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi tạo khóa học"
                }
                );
            }
        }

        [HttpGet("get/{course_id}")]
        [Authorize(Roles = StaticClass.Role.Teacher + "," + StaticClass.Role.Student)]
        public async Task<ActionResult<CourseDTO>> GetCourseById([FromRoute] string course_id)
        {
            try
            {
                Course? course = await DbContext.Courses.FindAsync(course_id);
                if (course == null)
                {
                    return NotFound(new { Message = "Khóa học không tồn tại" });
                }
                Account? host = await DbContext.Accounts.FindAsync(course.HostId);

                string hostFullName = "";

                if (host != null)
                {
                    hostFullName = host.LastMiddleName + " " + host.FirstName;
                }
                else
                {
                    hostFullName = "";
                }

                CourseDTO courseDTO = new CourseDTO(course.Id, course.Name, hostFullName, course.Avatar);
                return Ok(courseDTO);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                    {
                        Message = "Lỗi hệ thống khi lấy thông tin khóa học"
                    }
                );
            }
        }
    }
}
