using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace BE.Migrations
{
    /// <inheritdoc />
    public partial class Add_TB_DetailResult : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "DetailResults",
                columns: table => new
                {
                    AttemptQuizId = table.Column<string>(type: "Char(6)", nullable: false),
                    AnswerId = table.Column<string>(type: "Char(6)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DetailResults", x => new { x.AnswerId, x.AttemptQuizId });
                    table.ForeignKey(
                        name: "FK_DetailResults_Answer_AnswerId",
                        column: x => x.AnswerId,
                        principalTable: "Answer",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_DetailResults_AttemptQuiz_AttemptQuizId",
                        column: x => x.AttemptQuizId,
                        principalTable: "AttemptQuiz",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_DetailResults_AttemptQuizId",
                table: "DetailResults",
                column: "AttemptQuizId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "DetailResults");
        }
    }
}
