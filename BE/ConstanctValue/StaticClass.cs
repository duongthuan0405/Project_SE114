namespace BE.ConstanctValue
{
    public static class StaticClass
    {
        public const string AppName = "tqtquiz";
        public static class Role
        {
            public const string Teacher = "Giáo viên";
            public const string Student = "Học sinh";
        }


        public static string CreateId()
        {
            return Guid.NewGuid().ToString("N").Substring(0, 10);
        }
    }

}
