namespace BE.ConstanctValue
{
    public static class StaticClass
    {
        public const string AppName = "tqtquiz";
        public static class RoleId
        {
            public const string Teacher = "0000000001";
            public const string Student = "0000000002";
            public const string Admin = "0000000000";
        }


        public static string CreateId()
        {
            return Guid.NewGuid().ToString("N").Substring(0, 10);
        }
    }

}
