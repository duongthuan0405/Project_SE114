using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
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
    [Route(ConstantValue.AppName + "/[controller]")]
    public class AuthenController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        private readonly IConfiguration Configuration;
        public AuthenController(MyAppDBContext DbContext, IConfiguration Configuration)
        {
            this.DbContext = DbContext;
            this.Configuration = Configuration;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequestDTO req)
        {
            try
            {
                string reqEmail = req.Email;
                string reqPass = req.Password;

                AccountAuthen? authen = await DbContext.AccountAuthens.FirstOrDefaultAsync(authen => authen.Email == reqEmail && authen.Password == reqPass);
                if (authen == null)
                {
                    return Unauthorized(new LoginResponseDTO()
                    {
                        Message = "Tên đăng nhập hoặc mật khẩu không đúng"
                    });
                }

                var UserInfo = await DbContext.Accounts.Join(DbContext.AccountTypes, ac => ac.AccountTypeId, at => at.Id,
                    (ac, at) => new { ac, at }).FirstOrDefaultAsync(info => info.ac.Id == authen.Id);

                var claims = new Claim[]
                {
                new Claim(ClaimTypes.NameIdentifier, UserInfo.ac.Id),
                new Claim(ClaimTypes.Role, UserInfo.at.Name),
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

                return Ok(new LoginResponseDTO
                {
                    Message = "Đăng nhập thành công",
                    Token = new JwtSecurityTokenHandler().WriteToken(token),
                    Expires = expires,
                    UserId = UserInfo.ac.Id,
                    FullName = $"{UserInfo.ac.FirstName} {UserInfo.ac.LastMiddleName}",
                    Email = authen.Email,
                    Role = UserInfo.at.Name
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new LoginResponseDTO()
                {
                    Message = "Lỗi hệ thống: " + ex.Message
                });
            }
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequestDTO req)
        {
            try
            {
                if (string.IsNullOrEmpty(req.Email) || string.IsNullOrEmpty(req.Password) || string.IsNullOrEmpty(req.AccountTypeId))
                {
                    return BadRequest(new RegisterResponseDTO()
                    {
                        Message = "Email, mật khẩu và loại tài khoản là bắt buộc"
                    });
                }

                var existingAuthen = await DbContext.AccountAuthens.FirstOrDefaultAsync(a => a.Email == req.Email);

                if (existingAuthen != null)
                {
                    return Conflict(new RegisterResponseDTO()
                    {
                        Message = "Email đã được sử dụng"
                    });
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
                return Ok(new RegisterResponseDTO()
                {
                    Message = "Đăng ký thành công",
                    UserId = newAccount.Id
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new RegisterResponseDTO()
                {
                    Message = "Lỗi hệ thống: " + ex.Message
                });
            }
        }
    }
}