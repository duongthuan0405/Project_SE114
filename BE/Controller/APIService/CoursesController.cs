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
    // tqt/courses
    public class CoursesController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        public CoursesController(MyAppDBContext context)
        {
            DbContext = context;
        }
        

        // --- Get all courses that this user has joined ------------------------------------------------
        [HttpGet("myself/joined")]
        [Authorize(Roles = StaticClass.Role.Student + "," + StaticClass.Role.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCourseUserJoined()
        {
            try
            {
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(requester))
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                List<CourseDTO> courses = await DbContext.Courses.Where(
                    (c) => DbContext.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                ).Join(DbContext.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description))
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


        // --- Get all courses that this user is the host ------------------------------------------------
        [HttpGet("myself/hosted")]
        [Authorize(Roles = StaticClass.Role.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesHostedByUser()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            try
            {
                List<CourseDTO> courses = await DbContext.Courses.Where(c => c.HostId == requester)
                    .Join(DbContext.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description))
                    .ToListAsync();
                return Ok(courses);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi lấy danh sách khóa học"
                }
                );
            }
        }


        // --- Get all courses that this user is pending ------------------------------------------------
        [HttpGet("myself/pending")]
        [Authorize(Roles = StaticClass.Role.Student + "," + StaticClass.Role.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesUserPending()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }
            try
            {
                List<CourseDTO> courses = await DbContext.Courses.Where(c => DbContext.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Pending))
                    .Join(DbContext.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description))
                    .ToListAsync();
                return Ok(courses);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi lấy danh sách khóa học"
                }
                );
            }
        }


        // --- Get all courses that this user has been denied by host ------------------------------------------------
        [HttpGet("myself/denied")]
        [Authorize(Roles = StaticClass.Role.Student + "," + StaticClass.Role.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesUserIsDenied()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }
            try
            {
                List<CourseDTO> courses = await DbContext.Courses.Where(c => DbContext.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Denied))
                    .Join(DbContext.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description))
                    .ToListAsync();
                return Ok(courses);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi lấy danh sách khóa học"
                }
                );
            }
        }


        // --- Get course by its ID ------------------------------------------------
        [HttpGet("{course_id}")]
        [Authorize(Roles = StaticClass.Role.Teacher + "," + StaticClass.Role.Student)]
        public async Task<ActionResult<CourseDTO>> GetCourseByCourseId([FromRoute] string course_id)
        {
            try
            {
                Course? course = await DbContext.Courses.FindAsync(course_id);
                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
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

                CourseDTO courseDTO = new CourseDTO(course.Id, course.Name, hostFullName, course.IsPrivate, course.Avatar, course.Description);
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


        // --- Create the code ------------------------------------------------
        [HttpPost]
        [Authorize(Roles = StaticClass.Role.Teacher)]
        public async Task<ActionResult<CourseDTO>> CreateCourse([FromBody] RequestCreateCourseDTO request)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if(requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            string id = "";
            do
            {
                id = StaticClass.CreateId();
            } while (await DbContext.Courses.AnyAsync(c => c.Id == id));

            Course newCourse = new Course()
            {
                Id = id,
                Name = request.Name,
                HostId = requester,
                Avatar = request.Avatar,
                Description = request.Description,
                IsPrivate = request.IsPrivate
            };

            try
            {
                await DbContext.Courses.AddAsync(newCourse);
                await DbContext.SaveChangesAsync();
                string hostName = await DbContext.Accounts.Where(a => a.Id == requester)
                    .Select(a => a.LastMiddleName + " " + a.FirstName)
                    .FirstOrDefaultAsync() ?? "";

                return StatusCode(StatusCodes.Status201Created, new CourseDTO()
                {
                    Id = newCourse.Id,
                    Name = newCourse.Name,
                    HostName= hostName,
                    IsPrivate = newCourse.IsPrivate,
                    Avatar = newCourse.Avatar,
                    Description = newCourse.Description
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

        
    }
}
