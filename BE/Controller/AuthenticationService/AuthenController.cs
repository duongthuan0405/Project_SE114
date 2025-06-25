using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using BE.Email;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace BE.Controller.AuthenticationService
{
    [ApiController]
    [Route(StaticClass.AppName + "/[controller]")]
    public class AuthenController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        private readonly IConfiguration Configuration;
        private readonly IEmailService _emailService;
        public AuthenController(MyAppDBContext DbContext, IEmailService emailService, IConfiguration Configuration)
        {
            this.DbContext = DbContext;
            this.Configuration = Configuration;
            _emailService = emailService;
        }

        [HttpPost("login")]
        public async Task<ActionResult<LoginResponseDTO>> Login([FromBody] LoginRequestDTO req)
        {
            try
            {
                string reqEmail = req.Email;
                string reqPass = req.Password;

                AccountAuthen? authen = await DbContext.AccountAuthens.FirstOrDefaultAsync(authen => authen.Email == reqEmail && authen.Password == reqPass);
                if (authen == null)
                {

                    return StatusCode(StatusCodes.Status401Unauthorized, new
                    {
                        Message = "Tên đăng nhập hoặc mật khẩu không đúng"
                    }
                    );
                }

                var UserInfo = await DbContext.Accounts.Join(DbContext.AccountTypes, ac => ac.AccountTypeId, at => at.Id,
                    (ac, at) => new { ac, at }).FirstOrDefaultAsync(info => info.ac.Id == authen.Id);

                var claims = new Claim[]
                {
                new Claim(ClaimTypes.NameIdentifier, UserInfo.ac.Id),
                new Claim(ClaimTypes.Role, UserInfo.at.Id),
                };

                var jwt = Configuration.GetSection("Jwt");
                var keyBytes = Encoding.UTF8.GetBytes(jwt["Key"]);
                var key = new SymmetricSecurityKey(keyBytes);
                var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
                var expires = DateTime.Now.AddHours(1);

                var token = new JwtSecurityToken(
                    issuer: jwt["Issuer"],
                    audience: jwt["Audience"],
                    claims: claims,
                    expires: expires,
                    signingCredentials: creds
                );


                return StatusCode(StatusCodes.Status200OK,
                    new LoginResponseDTO
                    {
                        Token = new JwtSecurityTokenHandler().WriteToken(token),
                        UserId = UserInfo.ac.Id,
                        FullName = $"{UserInfo.ac.LastMiddleName} {UserInfo.ac.FirstName}",
                        Role = UserInfo.at.Name,
                        RoleId = UserInfo.at.Id
                    }
                );
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi đăng nhập"
                }
                );
            }
        }

        [HttpPost("register")]
        public async Task<ActionResult<AccountInfoDTO>> Register([FromBody] RegisterRequestDTO req)
        {
            try
            {
                if (string.IsNullOrEmpty(req.Email) || string.IsNullOrEmpty(req.Password))
                {
                    return StatusCode(StatusCodes.Status400BadRequest, new
                    {
                        Message = "Email, mật khẩu và loại tài khoản là bắt buộc"
                    }
                    );
                }

                var existingAuthen = await DbContext.AccountAuthens.FirstOrDefaultAsync(a => a.Email == req.Email);

                if (existingAuthen != null)
                {
                    return StatusCode(StatusCodes.Status409Conflict, new
                    {
                        Message = "Email đã được sử dụng"
                    }
                    );
                }

                string id = "";
                do
                {
                    id = Guid.NewGuid().ToString("N").Substring(0, 10); // Tạo ID ngẫu nhiên
                }
                while (await DbContext.AccountAuthens.AnyAsync(a => a.Id == id)); // Kiểm tra trùng lặp ID

                var newAuthen = new AccountAuthen
                {
                    Id = id,
                    Email = req.Email,
                    Password = req.Password // Nên mã hóa mật khẩu trước khi lưu
                };
                DbContext.AccountAuthens.Add(newAuthen);
                var newAccount = new Account
                {
                    Id = id,
                    FirstName = req.FirstName,
                    LastMiddleName = req.LastMiddleName,
                    AccountTypeId = req.AccountTypeId
                };
                DbContext.Accounts.Add(newAccount);

                await DbContext.SaveChangesAsync();
                return Ok(new AccountInfoDTO()
                {
                    Id = newAccount.Id,
                    FullName = newAccount.LastMiddleName + " " + newAccount.FirstName,
                    FirstName = newAccount.FirstName,
                    LastMiddleName = newAccount.LastMiddleName,
                    Email = newAuthen.Email,
                    Avatar = newAccount.Avatar,
                    AccountTypeId = newAccount.AccountTypeId,
                    AccountType = newAccount.AccountTypeId
                });
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi đăng ký"
                }
                );
            }
        }

        [HttpPost("forgot-password")]
        public async Task<IActionResult> ForgotPassword([FromBody] ForgotPwDTO dto)
        {
            try
            {
                var user = await DbContext.AccountAuthens.FirstOrDefaultAsync(a => a.Email == dto.Email);
                if (user == null) return Ok();

                var existingToken = await DbContext.PasswordResetTokens.FirstOrDefaultAsync(t => t.Email == dto.Email && t.ExpiredAt > DateTime.Now);

                if (existingToken != null)
                {
                    return Ok();
                }

                var token = Guid.NewGuid().ToString("N").Substring(0, 6);
                var expiredAt = DateTime.Now.AddMinutes(5);
                string id = "";
                do
                {
                    id = StaticClass.CreateId();
                }
                while (await DbContext.PasswordResetTokens.AnyAsync(t => t.Id == id));

                var resetToken = new PasswordResetToken
                {
                    Id = id,
                    Email = dto.Email,
                    Token = token,
                    ExpiredAt = expiredAt
                };

                DbContext.PasswordResetTokens.Add(resetToken);
                await DbContext.SaveChangesAsync();

                var html = $@"
                <p>Bạn đã yêu cầu đặt lại mật khẩu.</p>
                <p>Mã xác thực của bạn (hết hạn sau 5 phút):</p>
                <h2>{token}</h2>
                ";

                await _emailService.SendAsync(dto.Email, "Đặt lại mật khẩu", html);
                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi gửi email đặt lại mật khẩu"
                });
            }
        }

        [HttpPost("reset-password")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPwDTO dto)
        {
            try
            {
                var tokenEntity = await DbContext.PasswordResetTokens.FirstOrDefaultAsync(t => t.Email == dto.Email && t.Token == dto.Token);

                if (tokenEntity == null || tokenEntity.ExpiredAt < DateTime.UtcNow)
                    return BadRequest(new { Message = "Token không hợp lệ hoặc đã hết hạn." });

                var account = await DbContext.AccountAuthens.FirstOrDefaultAsync(a => a.Email == dto.Email);
                if (account == null)
                    return BadRequest(new { Message = "Tài khoản không tồn tại" });

                account.Password = dto.NewPassword; 
                DbContext.PasswordResetTokens.Remove(tokenEntity);
                await DbContext.SaveChangesAsync();

                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, new
                {
                    Message = "Lỗi hệ thống khi đặt lại mật khẩu"
                });
            }

        }
    }
}