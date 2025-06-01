using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace BE.Migrations
{
    /// <inheritdoc />
    public partial class Add_TB_Account : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Account",
                columns: table => new
                {
                    Id = table.Column<string>(type: "Char(6)", maxLength: 6, nullable: false),
                    Email = table.Column<string>(type: "VarChar(60)", maxLength: 60, nullable: false),
                    Password = table.Column<string>(type: "VarChar(25)", maxLength: 25, nullable: false),
                    LastMiddleName = table.Column<string>(type: "NVarChar(40)", maxLength: 40, nullable: true),
                    FirstName = table.Column<string>(type: "NVarChar(20)", maxLength: 20, nullable: true),
                    Avatar = table.Column<string>(type: "VarChar(3000)", maxLength: 3000, nullable: true),
                    AccountTypeId = table.Column<string>(type: "Char(6)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Account", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Account_AccountType_AccountTypeId",
                        column: x => x.AccountTypeId,
                        principalTable: "AccountType",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Account_AccountTypeId",
                table: "Account",
                column: "AccountTypeId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Account");
        }
    }
}
