using BE.ConstanctValue;
using BE.Data.Database;
using BE.Data.Entities;
using BE.DTOs;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace BE.Controller.APIService
{
    [Route($"{ConstantValue.AppName}/[controller]")]
    [ApiController]
    public class CourseController : ControllerBase
    {
        private readonly MyAppDBContext DbContext;
        public CourseController(MyAppDBContext context)
        {
            DbContext = context;
        }

        [HttpGet("course/{account_id}")]
        public async Task<ActionResult<List<CourseDTO>>> GetCoursThatAccountJoin(string account_id)
        {
            return null;
        }
    }
}
