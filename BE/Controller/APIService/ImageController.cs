using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using BE.ConstanctValue;
using BE.Data.Database;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BE.Controller.APIService
{
    [ApiController]
    [Route($"{StaticClass.AppName}/[controller]")]
    public class ImageController : ControllerBase
    {
        private readonly IWebHostEnvironment _env;
        private readonly MyAppDBContext _db;
        public ImageController(IWebHostEnvironment env, MyAppDBContext db)
        {
            _env = env;
            _db = db;
        }

        [HttpPost("avatar/account")]
        [RequestFormLimits(MultipartBodyLengthLimit = 5 * 1024 * 1024)]
        [Authorize]
        public async Task<IActionResult> UploadAvatar(IFormFile file)
        {
            try
            {
                if (file == null || file.Length == 0)
                {
                    return BadRequest("File rỗng");
                }
                string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
                var user = await _db.Accounts.FirstOrDefaultAsync(ac => ac.Id == requester);
                if (user == null)
                {
                    return NotFound("Không tìm thấy người dùng");
                }

                var uploads = Path.Combine(_env.WebRootPath, "uploads", "avatar");
                if (!Directory.Exists(uploads))
                {
                    Directory.CreateDirectory(uploads);
                }

                if (!string.IsNullOrWhiteSpace(user.Avatar))
                {
                    string oldPhysical = Path.Combine(_env.WebRootPath, user.Avatar.TrimStart('/'));
                    if (System.IO.File.Exists(oldPhysical)) System.IO.File.Delete(oldPhysical);
                }

                var ext = Path.GetExtension(file.FileName).ToLowerInvariant();
                var filePath = Path.Combine(uploads, $"{user.Id}{ext}");
                using (var stream = new FileStream(filePath, FileMode.Create, FileAccess.Write))
                {
                    await file.CopyToAsync(stream);
                }

                user.Avatar = $"/uploads/avatar/{user.Id}{ext}";
                await _db.SaveChangesAsync();

                return Ok(new { Url = user.Avatar });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { Message = "Lỗi server khi tải lên avatar" });
            }
        }


        [HttpPost("logo/course/{courseId}")]
        [RequestFormLimits(MultipartBodyLengthLimit = 5 * 1024 * 1024)]
        [Authorize]
        public async Task<IActionResult> UploadCourseLogo([FromRoute] string courseId, IFormFile file)
        {
            try
            {
                if (file == null || file.Length == 0)
                {
                    return BadRequest("File rỗng");
                }
                string requester = User.FindFirstValue(ClaimTypes.NameIdentifier) ?? "";
                var course = await _db.Courses.FirstOrDefaultAsync(c => c.Id == courseId);
                if (course == null)
                {
                    return NotFound("Không tìm thấy khóa học");
                }
                if (course.HostId != requester)
                {
                    return Forbid("Bạn không có quyền cập nhật logo khóa học này");
                }

                var uploads = Path.Combine(_env.WebRootPath, "uploads", "logo");
                if (!Directory.Exists(uploads))
                {
                    Directory.CreateDirectory(uploads);
                }

                if (!string.IsNullOrWhiteSpace(course.Avatar))
                {
                    string oldPhysical = Path.Combine(_env.WebRootPath, course.Avatar.TrimStart('/'));
                    if (System.IO.File.Exists(oldPhysical)) System.IO.File.Delete(oldPhysical);
                }

                var ext = Path.GetExtension(file.FileName).ToLowerInvariant();
                var filePath = Path.Combine(uploads, $"{course.Id}{ext}");
                using (var stream = new FileStream(filePath, FileMode.Create, FileAccess.Write))
                {
                    await file.CopyToAsync(stream);
                }

                course.Avatar = $"/uploads/logo/{course.Id}{ext}";
                await _db.SaveChangesAsync();

                return Ok(new { Url = course.Avatar });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { Message = "Lỗi server khi tải lên logo khóa học" });
            }
        }
    }
}