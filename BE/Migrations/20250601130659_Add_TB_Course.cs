using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace BE.Migrations
{
    /// <inheritdoc />
    public partial class Add_TB_Course : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Course",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(6)", maxLength: 6, nullable: false),
                    Name = table.Column<string>(type: "NVarChar(20)", maxLength: 20, nullable: false),
                    Description = table.Column<string>(type: "NVarChar(100)", maxLength: 100, nullable: true),
                    IsPrivate = table.Column<bool>(type: "Bit", nullable: false),
                    Avatar = table.Column<string>(type: "VarChar(3000)", maxLength: 3000, nullable: false),
                    HostId = table.Column<string>(type: "Char(6)", nullable: true)
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

            migrationBuilder.CreateIndex(
                name: "IX_Course_HostId",
                table: "Course",
                column: "HostId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Course");
        }
    }
}
