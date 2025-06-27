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
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Student + "," + StaticClass.RoleId.Teacher)]
        public async Task<ActionResult<AccountInfoDTO>> GetUserInfo()
        {
            var requester = User.FindFirstValue(ClaimTypes.NameIdentifier);
            if (string.IsNullOrEmpty(requester))
            {
                return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
            }

            try
            {
                var account = await DbContext.Accounts.AsNoTracking()
                    .Where(a => a.Id == requester)
                    .Join(DbContext.AccountAuthens, a => a.Id, aa => aa.Id, (a, aa) => new { a, aa })
                    .Select(x => new AccountInfoDTO(
                        x.a.Id,
                        x.aa.Email,
                        x.a.LastMiddleName,
                        x.a.FirstName,
                        x.a.Avatar,
                        x.a.AccountTypeId,
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
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult<AccountInfoDTO>> GetUserInfoById([FromRoute] string account_id)
        {
            try
            {
                var account = await DbContext.Accounts.AsNoTracking()
                    .Where(a => a.Id == account_id)
                    .Join(DbContext.AccountAuthens, a => a.Id, aa => aa.Id, (a, aa) => new { a, aa })
                    .Select(x => new AccountInfoDTO(
                        x.a.Id,
                        x.aa.Email,
                        x.a.LastMiddleName,
                        x.a.FirstName,
                        x.a.Avatar,
                        x.a.AccountTypeId,
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

        [HttpPut]
        [Authorize(Roles = StaticClass.RoleId.Admin + "," + StaticClass.RoleId.Teacher + "," + StaticClass.RoleId.Student)]
        public async Task<ActionResult> UpdateUserInfo([FromBody] UpdateAccountDTO accountInfo)
        {
        
            var account_id = User.FindFirstValue(ClaimTypes.NameIdentifier);

            try
            {
                var account = await DbContext.Accounts.FindAsync(account_id);
                if (account == null)
                {
                    return StatusCode(StatusCodes.Status404NotFound, new { Message = "Tài khoản không tồn tại" });
                }

                account.LastMiddleName = string.IsNullOrEmpty(accountInfo.LastMiddleName) || string.IsNullOrWhiteSpace(accountInfo.LastMiddleName) ? account.LastMiddleName : accountInfo.LastMiddleName;
                account.FirstName = string.IsNullOrEmpty(accountInfo.FirstName) || string.IsNullOrWhiteSpace(accountInfo.FirstName) ? account.FirstName : accountInfo.FirstName;

                if (string.IsNullOrWhiteSpace(accountInfo.NewPassword))
                {
                    var accountAth = await DbContext.AccountAuthens.FindAsync(account_id);
                    if (accountAth == null)
                    {
                        return StatusCode(StatusCodes.Status404NotFound, new { Message = "Thông tin xác thực tài khoản không tồn tại" });
                    }
                    accountAth.Password = accountInfo.NewPassword;
                }

                DbContext.Accounts.Update(account);
                await DbContext.SaveChangesAsync();

                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new { Message = "Lỗi server khi cập nhật thông tin tài khoản" });
            }
        }

    }
}
