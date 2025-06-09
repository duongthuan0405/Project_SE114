using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BE.Controller
{
    [ApiController]
    [Route(ConstantValue.AppName + "/[controller]")]
    public class AccountTypeController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;

        public AccountTypeController(MyAppDBContext context)
        {
            DbContext = context;
        }

        [HttpGet("accounttypes")]
        [Authorize]
        public async Task<ActionResult<List<AccountTypeDTO>>> GetAllAccountType()
        {
            var l = await DbContext.AccountTypes.ToListAsync();
            var result = l.Select(at => new AccountTypeDTO
            {
                Name = at.Name
            }).ToList();

            return Ok(result);
        }
    }
}