
using BE.Adapter;
using BE.AutoService;
using BE.Data.Database;
using BE.Email;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using System.Security.Claims;
using System.Text;
using System.Text.Encodings.Web;
using System.Text.Unicode;

namespace BE;

public class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);
        string? TQT_Quiz_Connection = builder.Configuration.GetConnectionString("TQT_Quiz_Connection");

        // Đọc cấu hình từ appsetting.json
        var jwtConfig = builder.Configuration.GetSection("Jwt");
        var keyBytes  = Encoding.UTF8.GetBytes(jwtConfig["Key"]);

        // Make the connection
        builder.Services.AddDbContext<MyAppDBContext>(
            opts => opts.UseSqlServer(TQT_Quiz_Connection)
        );

        builder.Services.AddHostedService<AutoSubmitService>();

        // CamelCase
        builder.Services.AddControllers().AddJsonOptions(
            opt =>
            {
                opt.JsonSerializerOptions.PropertyNamingPolicy = System.Text.Json.JsonNamingPolicy.CamelCase;
                opt.JsonSerializerOptions.Encoder = JavaScriptEncoder.Create(UnicodeRanges.All);
                opt.JsonSerializerOptions.Converters.Add(new DateTimeConverterUsingFormat("yyyy-MM-dd HH:mm:ss"));
            }
        );

        // Đăng ký và cấu hình 
        builder.Services
                .AddAuthentication(options =>
                {
                    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
                })
                .AddJwtBearer(options =>
                {
                    options.RequireHttpsMetadata = true;  
                    options.SaveToken = true;  
                    options.TokenValidationParameters = new TokenValidationParameters
                    {
                    ValidateIssuer = true,
                    ValidIssuer = jwtConfig["Issuer"],

                    ValidateAudience = true,
                    ValidAudience = jwtConfig["Audience"],

                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(keyBytes),

                    ValidateLifetime = true,
                    ClockSkew = TimeSpan.Zero,

                    NameClaimType = ClaimTypes.NameIdentifier,
                    RoleClaimType = ClaimTypes.Role
                    };

                  
                });




        Console.WriteLine(TQT_Quiz_Connection ?? "null");  

        // Add services to the container.
        builder.Services.AddAuthorization();

        // Learn more about configuring OpenAPI at https://aka.ms/aspnet/openapi
        builder.Services.AddEndpointsApiExplorer();
        builder.Services.AddSwaggerGen(options =>
        {
            options.SwaggerDoc("v1", new OpenApiInfo
            {
                Title = "Project_SE114 BE API",
                Version = "v1",
                Description = "Tài liệu Swagger cho dự án BE (.NET 8)"
            });

            options.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
            {
                Name = "Authorization",
                In = ParameterLocation.Header,
                Type = SecuritySchemeType.ApiKey,
                Scheme = "Bearer",
                BearerFormat = "JWT",
                Description = "Nhập ‘Bearer {token}’"
            });

            options.AddSecurityRequirement(new OpenApiSecurityRequirement {
            {
                new OpenApiSecurityScheme {
                Reference = new OpenApiReference {
                    Type = ReferenceType.SecurityScheme,
                    Id   = "Bearer"
                }
                },
                Array.Empty<string>()
            }
            });
           
        });

        builder.Services.Configure<EmailSetting>(builder.Configuration.GetSection("Email"));
        builder.Services.AddScoped<IEmailService, EmailService>();


        var app = builder.Build();
        app.UseStaticFiles();

        // Configure the HTTP request pipeline.
        if (app.Environment.IsDevelopment())
        {
            app.UseSwagger();

            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", "Project_SE114 BE API V1");

            });


        }

        //app.UseHttpsRedirection();

        app.UseAuthentication(); 
        app.UseAuthorization();

        app.MapControllers();
        app.Run();
    }
}
