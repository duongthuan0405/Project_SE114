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
    public class AccountInfoController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        public AccountInfoController(MyAppDBContext context)
        {
            DbContext = context;
        }

        [HttpGet("myself")]
        [Authorize(Roles = StaticClass.Role.Admin + "," + StaticClass.Role.Student + "," + StaticClass.Role.Teacher)]
        public async Task<ActionResult<AccountInfoDTO>> GetUserInfo()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (string.IsNullOrEmpty(requester))
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            try
            {
                var account = await DbContext.Accounts
                    .Where(a => a.Id == requester)
                    .Join(DbContext.AccountAuthens, a => a.Id, aa => aa.Id, (a, aa) => new { a, aa })
                    .Select(x => new AccountInfoDTO(
                        x.a.Id,
                        x.aa.Email,
                        x.a.LastMiddleName + " " + x.a.FirstName,
                        x.a.Avatar,
                        DbContext.AccountTypes.Where(at => at.Id == x.a.AccountTypeId)
                            .Select(at => at.Name).FirstOrDefault() ?? "Unknown Account Type"
                    ))
                    .FirstOrDefaultAsync();
                if (account == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }
                return Ok(account);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy thông tin tài khoản" });
            }
        }

        [HttpGet("{account_id}")]
        [Authorize(Roles = StaticClass.Role.Admin + "," + StaticClass.Role.Teacher + "," + StaticClass.Role.Student)]
        public async Task<ActionResult<AccountInfoDTO>> GetUserInfoById([FromRoute] string account_id)
        {
            try
            {
                var account = await DbContext.Accounts
                    .Where(a => a.Id == account_id)
                    .Join(DbContext.AccountAuthens, a => a.Id, aa => aa.Id, (a, aa) => new { a, aa })
                    .Select(x => new AccountInfoDTO(
                        x.a.Id,
                        x.aa.Email,
                        x.a.LastMiddleName + " " + x.a.FirstName,
                        x.a.Avatar,
                        DbContext.AccountTypes.Where(at => at.Id == x.a.AccountTypeId)
                            .Select(at => at.Name).FirstOrDefault() ?? "Unknown Account Type"
                    ))
                    .FirstOrDefaultAsync();
                if (account == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }
                return Ok(account);
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi lấy thông tin tài khoản" });
            }
        }

    }
}
