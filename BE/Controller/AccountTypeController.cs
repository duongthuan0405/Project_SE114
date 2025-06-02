using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BE.Controller
{
    [ApiController]
    [Route("tqtquiz/[controller]")]
    public class AccountTypeController : ControllerBase
    {
        private readonly MyAppDBContext _context;

        public AccountTypeController(MyAppDBContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<List<AccountTypeDTO>>> GetAllAccountType()
        {
            var l = await _context.AccountTypes.ToListAsync();
            return Ok(l);
        }
    }
}