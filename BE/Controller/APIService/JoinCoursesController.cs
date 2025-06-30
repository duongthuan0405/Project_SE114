using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using BE.Email;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace BE.Controller.APIService
{
    [Route($"{StaticClass.AppName}/[controller]")]
    [ApiController]
    public class JoinCoursesController : ControllerBase
    {
        public readonly MyAppDBContext DbContext;
        public readonly IEmailService emailService;

        public JoinCoursesController(MyAppDBContext context, IEmailService emailService)
        {
            DbContext = context;
            this.emailService = emailService;
        }

        [HttpPost("{course_id}")]
        [Authorize(Roles = StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<JoinCourseResponseDTO>> JoinCourseWithIds([FromRoute] string course_id)
        {
            try
            {
                var course = await DbContext.Courses.FindAsync(course_id);
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }
                if (requester == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                if (course.HostId == requester)
                {
                    return StatusCode(StatusCodes.Status200OK, new JoinCourseResponseDTO(course_id, requester, "Bạn không cần phải tham gia khóa học của chính mình"));
                }


                var existingJoin = DbContext.JoinCourses.Where(jc => jc.CourseID == course_id && jc.AccountID == requester);
                if (await existingJoin.AnyAsync(jc => jc.State == (int)JoinCourse.JoinCourseState.Joined))
                {
                    return StatusCode(StatusCodes.Status200OK, new JoinCourseResponseDTO(course_id, requester, "Bạn đã tham gia khóa học này rồi"));
                }
                if (await existingJoin.AnyAsync(jc => jc.State == (int)JoinCourse.JoinCourseState.Pending))
                {
                    return StatusCode(StatusCodes.Status200OK, new JoinCourseResponseDTO(course_id, requester, "Bạn đã gửi yêu cầu tham gia khóa học này, vui lòng chờ phê duyệt"));

                }

                var joinCourse = new JoinCourse
                {
                    CourseID = course_id,
                    AccountID = requester,
                    TimeJoin = DateTime.Now
                };

                if (course.IsPrivate == true)
                {
                    joinCourse.State = (int)JoinCourse.JoinCourseState.Pending;
                    await DbContext.JoinCourses.AddAsync(joinCourse);
                    await DbContext.SaveChangesAsync();
                    return Ok(new JoinCourseResponseDTO(course_id, requester, "Vui lòng chờ giáo viên duyệt yêu cầu tham gia"));
                }

                joinCourse.State = (int)JoinCourse.JoinCourseState.Joined;
                await DbContext.JoinCourses.AddAsync(joinCourse);
                await DbContext.SaveChangesAsync();
                return Ok(new JoinCourseResponseDTO(course_id, requester, "Tham gia khóa học thành công"));

            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi tham gia khóa học" });
            }
        }

        [HttpPatch("{account_id}/join/{course_id}/approve")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> AcceptJoinCoursePermission([FromRoute] string account_id, [FromRoute] string course_id)
        {
            try
            {
                var course = await DbContext.Courses.FindAsync(course_id);
                var account = await DbContext.Accounts.FindAsync(account_id);
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }
                if (account == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }
                if (requester == null || !requester.Equals(course.HostId))
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền phê duyệt yêu cầu tham gia khóa học này" });
                }

                var joinCourse = await DbContext.JoinCourses
                    .FirstOrDefaultAsync(jc => jc.CourseID == course_id && jc.AccountID == account_id && jc.State == (int)JoinCourse.JoinCourseState.Pending);

                if (joinCourse == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Yêu cầu tham gia khóa học không tồn tại" });
                }

                joinCourse.State = (int)JoinCourse.JoinCourseState.Joined;
                await DbContext.SaveChangesAsync();
                // Gửi email thông báo cho người dùng đã được phê duyệt
                var email = await DbContext.AccountAuthens
                    .Where(aa => aa.Id == account_id)
                    .Select(aa => aa.Email)
                    .FirstOrDefaultAsync();
                if (!string.IsNullOrEmpty(email))
                {
                    string subject = "Phê duyệt tham gia khóa học";
                    string htmlContent = $@"
                        <p>Xin chào,</p>
                        <p>Bạn đã được <b>phê duyệt</b> tham gia vào khóa học <b>{course.Name}</b>.</p>
                        <p>Hãy đăng nhập hệ thống để bắt đầu học và làm bài.</p>
                        <p>Chúc bạn học tốt!</p>";

                    try
                    {
                        await emailService.SendAsync(email, subject, htmlContent);
                    }
                    catch (Exception e)
                    {
                       
                    }
                }
                    
                return Ok(new { Message = "Phê duyệt tham gia khóa học thành công" });
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi phê duyệt yêu cầu tham gia khóa học" });
            }
        }

        [HttpPatch("{account_id}/join/{course_id}/deny")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> DenyJoinCoursePermission([FromRoute] string account_id, [FromRoute] string course_id)
        {
            try
            {
                var course = await DbContext.Courses.FindAsync(course_id);
                var account = await DbContext.Accounts.FindAsync(account_id);
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }
                if (account == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }
                if (requester == null || !requester.Equals(course.HostId))
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền từ chối yêu cầu tham gia khóa học này" });
                }

                var joinCourse = await DbContext.JoinCourses
                        .FirstOrDefaultAsync(jc => jc.CourseID == course_id && jc.AccountID == account_id && jc.State == (int)JoinCourse.JoinCourseState.Pending);

                if (joinCourse == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Yêu cầu tham gia khóa học không tồn tại" });
                }

                joinCourse.State = (int)JoinCourse.JoinCourseState.Denied;
                await DbContext.SaveChangesAsync();

                var email = await DbContext.AccountAuthens
                    .Where(aa => aa.Id == account_id)
                    .Select(aa => aa.Email)
                    .FirstOrDefaultAsync();
                if (!string.IsNullOrEmpty(email))
                {
                    string subject = "Phê duyệt tham gia khóa học";
                    string htmlContent = $@"
                        <p>Xin chào,</p>
                        <p>Bạn đã bị <b>từ chối</b> tham gia vào khóa học <b>{course.Name}</b>.</p>";
                    try
                    {
                        await emailService.SendAsync(email, subject, htmlContent);
                    }
                    catch (Exception e)
                    {
                       
                    }
                }

                return Ok(new { Message = "Từ chối tham gia nhóm thành công" });
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi từ chối yêu cầu tham gia khóa học" });
            }
        }

        [HttpGet("{course_id}/permissions")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<List<AccountInfoDTO>>> GetAllPermissionByCourseId(string course_id)
        {
            try
            {
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (requester == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                if (!await DbContext.Courses.AnyAsync(c => c.Id == course_id))
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }

                if (!await DbContext.Courses.AnyAsync(c => c.Id == course_id && c.HostId == requester))
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền xem danh sách yêu cầu tham gia khóa học này" });
                }

                var permissions = await DbContext.JoinCourses.AsNoTracking()
                    .Where(jc => jc.CourseID == course_id && jc.State == (int)JoinCourse.JoinCourseState.Pending)
                    .OrderBy(jc => jc.TimeJoin)
                    .Join(DbContext.Accounts, jc => jc.AccountID, a => a.Id, (jc, a) => new { jc, a })
                    .Join(DbContext.AccountAuthens, x => x.a.Id, aa => aa.Id, (x, aa) => new AccountInfoDTO()
                    {
                        Id = x.a.Id,
                        FullName = x.a.LastMiddleName + " " + x.a.FirstName,
                        FirstName = x.a.FirstName,
                        LastMiddleName = x.a.LastMiddleName,
                        Email = aa.Email,
                        Avatar = x.a.Avatar,
                        AccountTypeId = x.a.AccountTypeId,
                        AccountType = DbContext.AccountTypes.Where(at => at.Id == x.a.AccountTypeId)
                                                                    .Select(at => at.Name)
                                                                    .FirstOrDefault() ?? "Chưa xác định"
                    }
                    ).ToListAsync();
                return Ok(permissions);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy danh sách yêu cầu tham gia khóa học" });
            }
        }

        [HttpPatch("{course_id}/leave")]
        [Authorize(Roles = StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> LeaveCourse([FromRoute] string course_id)
        {
            try
            {
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
                if (requester == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                var joinCourse = await DbContext.JoinCourses
                    .FirstOrDefaultAsync(jc => jc.CourseID == course_id && jc.AccountID == requester && jc.State == (int)JoinCourse.JoinCourseState.Joined);

                if (joinCourse == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Bạn chưa tham gia khóa học này" });
                }

            

                joinCourse.State = (int)JoinCourse.JoinCourseState.Left;
                await DbContext.SaveChangesAsync();
                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi rời khỏi khóa học" });
            }
        }

        [HttpPatch("{course_id}/ban/{account_id}")]
        [Authorize(Roles = StaticClass.RoleId.Teacher)]
        public async Task<ActionResult> BanAccountFromCourse([FromRoute] string course_id, [FromRoute] string account_id)
        {
            try
            {
                var course = await DbContext.Courses.FindAsync(course_id);
                var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);

                if (course == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Khóa học không tồn tại" });
                }
                if (requester == null || !requester.Equals(course.HostId))
                {
                    return StatusCode(StatusCodes.Status403Forbidden, new { Message = "Bạn không có quyền cấm người dùng khỏi khóa học này" });
                }

                var joinCourse = await DbContext.JoinCourses
                    .FirstOrDefaultAsync(jc => jc.CourseID == course_id && jc.AccountID == account_id);

                if (joinCourse == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Người dùng không tham gia khóa học này" });
                }

                joinCourse.State = (int)JoinCourse.JoinCourseState.Banned;
                await DbContext.SaveChangesAsync();

                string subject = "Phê duyệt tham gia khóa học";
                string htmlContent = $@"
                    <p>Xin chào,</p>
                    <p>Bạn đã bị <b>cấm</b> khỏi khóa học <b>{course.Name}</b>.</p>";
                var email = await DbContext.AccountAuthens
                    .Where(aa => aa.Id == account_id)
                    .Select(aa => aa.Email)
                    .FirstOrDefaultAsync();
                if (!string.IsNullOrEmpty(email))
                {

                    try
                    {
                        await emailService.SendAsync(email, subject, htmlContent);
                    }
                    catch (Exception e)
                    {

                    }
                }
                return Ok(new { Message = "Cấm người dùng khỏi khóa học thành công" });
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi cấm người dùng khỏi khóa học" });
            }
        }

    }
}
