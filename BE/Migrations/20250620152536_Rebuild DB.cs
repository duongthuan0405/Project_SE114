using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace BE.Migrations
{
    /// <inheritdoc />
    public partial class RebuildDB : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "AccountAuthen",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Email = table.Column<string>(type: "VarChar(60)", maxLength: 60, nullable: false),
                    Password = table.Column<string>(type: "VarChar(25)", maxLength: 25, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AccountAuthen", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "AccountType",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Name = table.Column<string>(type: "NVarChar(20)", maxLength: 20, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AccountType", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Account",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    LastMiddleName = table.Column<string>(type: "NVarChar(40)", maxLength: 40, nullable: true),
                    FirstName = table.Column<string>(type: "NVarChar(20)", maxLength: 20, nullable: true),
                    Avatar = table.Column<string>(type: "VarChar(3000)", maxLength: 3000, nullable: true),
                    AccountTypeId = table.Column<string>(type: "Char(10)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Account", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Account_AccountAuthen_Id",
                        column: x => x.Id,
                        principalTable: "AccountAuthen",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Account_AccountType_AccountTypeId",
                        column: x => x.AccountTypeId,
                        principalTable: "AccountType",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Course",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Name = table.Column<string>(type: "NVarChar(20)", maxLength: 20, nullable: false),
                    Description = table.Column<string>(type: "NVarChar(100)", maxLength: 100, nullable: true),
                    IsPrivate = table.Column<bool>(type: "Bit", nullable: false),
                    Avatar = table.Column<string>(type: "VarChar(3000)", maxLength: 3000, nullable: true),
                    HostId = table.Column<string>(type: "Char(10)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Course", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Course_Account_HostId",
                        column: x => x.HostId,
                        principalTable: "Account",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateTable(
                name: "JoinCourse",
                columns: table => new
                {
                    CourseID = table.Column<string>(type: "Char(10)", nullable: false),
                    AccountID = table.Column<string>(type: "Char(10)", nullable: false),
                    TimeJoin = table.Column<DateTime>(type: "datetime2", nullable: false),
                    State = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_JoinCourse", x => new { x.AccountID, x.CourseID, x.TimeJoin });
                    table.ForeignKey(
                        name: "FK_JoinCourse_Account_AccountID",
                        column: x => x.AccountID,
                        principalTable: "Account",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_JoinCourse_Course_CourseID",
                        column: x => x.CourseID,
                        principalTable: "Course",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Quiz",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Name = table.Column<string>(type: "NVarChar(30)", maxLength: 30, nullable: false),
                    Description = table.Column<string>(type: "NVarChar(100)", maxLength: 100, nullable: true),
                    StartTime = table.Column<DateTime>(type: "datetime2", nullable: false),
                    DueTime = table.Column<DateTime>(type: "datetime2", nullable: false),
                    CourseID = table.Column<string>(type: "Char(10)", nullable: false),
                    IsPublished = table.Column<bool>(type: "bit", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Quiz", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Quiz_Course_CourseID",
                        column: x => x.CourseID,
                        principalTable: "Course",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "AttemptQuiz",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    QuizId = table.Column<string>(type: "Char(10)", nullable: false),
                    AccountId = table.Column<string>(type: "Char(10)", nullable: false),
                    AttemptTime = table.Column<DateTime>(type: "datetime2", nullable: false),
                    FinishTime = table.Column<DateTime>(type: "datetime2", nullable: false),
                    IsSubmitted = table.Column<bool>(type: "bit", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AttemptQuiz", x => x.Id);
                    table.ForeignKey(
                        name: "FK_AttemptQuiz_Account_AccountId",
                        column: x => x.AccountId,
                        principalTable: "Account",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AttemptQuiz_Quiz_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quiz",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "QuestionDTO",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Content = table.Column<string>(type: "NVarChar(1000)", maxLength: 1000, nullable: false),
                    QuizId = table.Column<string>(type: "Char(10)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_QuestionDTO", x => x.Id);
                    table.ForeignKey(
                        name: "FK_QuestionDTO_Quiz_QuizId",
                        column: x => x.QuizId,
                        principalTable: "Quiz",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateTable(
                name: "AnswerDTO",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(10)", maxLength: 10, nullable: false),
                    Content = table.Column<string>(type: "NVarChar(1000)", maxLength: 1000, nullable: false),
                    IsTrue = table.Column<bool>(type: "Bit", nullable: false),
                    QuestionID = table.Column<string>(type: "Char(10)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AnswerDTO", x => x.Id);
                    table.ForeignKey(
                        name: "FK_AnswerDTO_QuestionDTO_QuestionID",
                        column: x => x.QuestionID,
                        principalTable: "QuestionDTO",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "DetailResults",
                columns: table => new
                {
                    AttemptQuizId = table.Column<string>(type: "Char(10)", nullable: false),
                    AnswerId = table.Column<string>(type: "Char(10)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DetailResults", x => new { x.AnswerId, x.AttemptQuizId });
                    table.ForeignKey(
                        name: "FK_DetailResults_AnswerDTO_AnswerId",
                        column: x => x.AnswerId,
                        principalTable: "AnswerDTO",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_DetailResults_AttemptQuiz_AttemptQuizId",
                        column: x => x.AttemptQuizId,
                        principalTable: "AttemptQuiz",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.InsertData(
                table: "AccountType",
                columns: new[] { "Id", "Name" },
                values: new object[,]
                {
                    { "0000000000", "Quản trị" },
                    { "0000000001", "Giáo viên" },
                    { "0000000002", "Học sinh" }
                });

            migrationBuilder.CreateIndex(
                name: "IX_Account_AccountTypeId",
                table: "Account",
                column: "AccountTypeId");

            migrationBuilder.CreateIndex(
                name: "IX_AnswerDTO_QuestionID",
                table: "AnswerDTO",
                column: "QuestionID");

            migrationBuilder.CreateIndex(
                name: "IX_AttemptQuiz_AccountId",
                table: "AttemptQuiz",
                column: "AccountId");

            migrationBuilder.CreateIndex(
                name: "IX_AttemptQuiz_QuizId",
                table: "AttemptQuiz",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_Course_HostId",
                table: "Course",
                column: "HostId");

            migrationBuilder.CreateIndex(
                name: "IX_DetailResults_AttemptQuizId",
                table: "DetailResults",
                column: "AttemptQuizId");

            migrationBuilder.CreateIndex(
                name: "IX_JoinCourse_CourseID",
                table: "JoinCourse",
                column: "CourseID");

            migrationBuilder.CreateIndex(
                name: "IX_QuestionDTO_QuizId",
                table: "QuestionDTO",
                column: "QuizId");

            migrationBuilder.CreateIndex(
                name: "IX_Quiz_CourseID",
                table: "Quiz",
                column: "CourseID");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "DetailResults");

            migrationBuilder.DropTable(
                name: "JoinCourse");

            migrationBuilder.DropTable(
                name: "AnswerDTO");

            migrationBuilder.DropTable(
                name: "AttemptQuiz");

            migrationBuilder.DropTable(
                name: "QuestionDTO");

            migrationBuilder.DropTable(
                name: "Quiz");

            migrationBuilder.DropTable(
                name: "Course");

            migrationBuilder.DropTable(
                name: "Account");

            migrationBuilder.DropTable(
                name: "AccountAuthen");

            migrationBuilder.DropTable(
                name: "AccountType");
        }
    }
}
