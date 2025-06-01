using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace BE.Migrations
{
    /// <inheritdoc />
    public partial class Add_TB_AttemptQuiz : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "AttemptQuiz",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(6)", maxLength: 6, nullable: false),
                    QuizId = table.Column<string>(type: "Char(6)", nullable: false),
                    AccountId = table.Column<string>(type: "Char(6)", nullable: false),
                    AttemptTime = table.Column<DateTime>(type: "datetime2", nullable: false),
                    FinishTime = table.Column<DateTime>(type: "datetime2", nullable: false)
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

            migrationBuilder.CreateIndex(
                name: "IX_AttemptQuiz_AccountId",
                table: "AttemptQuiz",
                column: "AccountId");

            migrationBuilder.CreateIndex(
                name: "IX_AttemptQuiz_QuizId",
                table: "AttemptQuiz",
                column: "QuizId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AttemptQuiz");
        }
    }
}
