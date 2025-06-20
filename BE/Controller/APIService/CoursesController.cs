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
        private readonly MyAppDBContext db;
        public CoursesController(MyAppDBContext context)
        {
            db = context;
        }


        // --- Get all courses that this user has joined ------------------------------------------------
        [HttpGet("myself/joined")]
        [Authorize(Roles = StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCourseUserJoined()
        {
            try
            {
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (string.IsNullOrEmpty(requester))
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                List<CourseDTO> courses = await db.Courses.Where(
                    (c) => db.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                ).Join(db.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.Id, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description, StaticClass.StateOfJoinCourse.Joined))
                .ToListAsync();

                return Ok(courses.OrderBy(c => c.Name).ToList());
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
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesHostedByUser()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            try
            {
                List<CourseDTO> courses = await db.Courses.Where(c => c.HostId == requester)
                    .Join(db.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.Id, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description, ""))
                    .ToListAsync();
                return Ok(courses.OrderBy(c => c.Name).ToList());
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
        [Authorize(Roles = StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesUserPending()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }
            try
            {
                List<CourseDTO> courses = await db.Courses.Where(c => db.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Pending))
                    .Join(db.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.Id, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description, StaticClass.StateOfJoinCourse.Requested))
                    .ToListAsync();
                return Ok(courses.OrderBy(c => c.Name).ToList());
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
        [Authorize(Roles = StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursesUserIsDenied()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }
            try
            {
                List<CourseDTO> courses = await db.Courses.Where(c => db.JoinCourses.Any(jc => jc.AccountID == requester && jc.CourseID == c.Id && jc.State == (int)JoinCourse.JoinCourseState.Denied))
                    .Join(db.Accounts, c => c.HostId, a => a.Id, (c, a) => new CourseDTO(c.Id, c.Name, a.Id, a.LastMiddleName + " " + a.FirstName, c.IsPrivate, c.Avatar, c.Description, ""))
                    .ToListAsync();
                return Ok(courses.OrderBy(c => c.Name).ToList());
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
        [Authorize(Roles = StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<CourseDTO>> GetCourseByCourseId([FromRoute] string course_id)
        {
            try
            {
                string? requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                string? role = User.FindFirstValue(ClaimTypes.Role);
                Course? course = await db.Courses.FindAsync(course_id);
                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }
                Account? host = await db.Accounts.FindAsync(course.HostId);

                string hostFullName = "";
                string hostId = "";

                if (host != null)
                {
                    hostFullName = host.LastMiddleName + " " + host.FirstName;
                    hostId = host.Id;
                }


                string stateOfJoinCourse = "";

                if (role == StaticClass.RoleId.Student)
                {

                    if (await db.JoinCourses.AnyAsync(jc => jc.AccountID == requester && jc.CourseID == course.Id && jc.State == (int)JoinCourse.JoinCourseState.Joined))
                    {
                        stateOfJoinCourse = StaticClass.StateOfJoinCourse.Joined;
                    }
                    else if (await db.JoinCourses.AnyAsync(jc => jc.AccountID == requester && jc.CourseID == course.Id && jc.State == (int)JoinCourse.JoinCourseState.Pending))
                    {
                        stateOfJoinCourse = StaticClass.StateOfJoinCourse.Requested;
                    }
                    else
                    {
                        stateOfJoinCourse = StaticClass.StateOfJoinCourse.NotJoined;
                    }
                }

                CourseDTO courseDTO = new CourseDTO(course.Id, course.Name, hostId, hostFullName, course.IsPrivate, course.Avatar, course.Description, stateOfJoinCourse);
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
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<CourseDTO>> CreateCourse([FromBody] RequestCreateCourseDTO request)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            string id = "";
            do
            {
                id = StaticClass.CreateId();
            } while (await db.Courses.AnyAsync(c => c.Id == id));

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
                await db.Courses.AddAsync(newCourse);
                await db.SaveChangesAsync();
                string hostName = await db.Accounts.Where(a => a.Id == requester)
                    .Select(a => a.LastMiddleName + " " + a.FirstName)
                    .FirstOrDefaultAsync() ?? "";

                return StatusCode(StatusCodes.Status201Created, new CourseDTO()
                {
                    Id = newCourse.Id,
                    Name = newCourse.Name,
                    HostName = hostName,
                    IsPrivate = newCourse.IsPrivate,
                    Avatar = newCourse.Avatar,
                    Description = newCourse.Description,
                    HostId = requester
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

        [HttpGet("{course_id}/members")]
        [Authorize(Roles = StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student + "," + StaticClass.RoleId.Admin)]
        public async Task<ActionResult<List<AccountInfoDTO>>> GetMembersInCourse([FromRoute] string course_id)
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (requester == null)
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }
            try
            {
                List<AccountInfoDTO> members = await db.JoinCourses.Where(jc => jc.CourseID == course_id && jc.State == (int)JoinCourse.JoinCourseState.Joined)
                    .Join(db.Accounts, jc => jc.AccountID, a => a.Id, (jc, a) => new AccountInfoDTO(a.Id, "", a.LastMiddleName, a.FirstName, a.Avatar, a.AccountTypeId, ""))
                    .ToListAsync();
                return Ok(members.OrderBy(m => m.FirstName).ToList());
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi lấy danh sách thành viên khóa học"
                }
                );
            }
        }
    }
}
