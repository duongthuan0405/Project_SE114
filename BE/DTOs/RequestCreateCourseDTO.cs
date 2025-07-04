﻿using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BE.DTOs
{
    public class RequestCreateCourseDTO
    {
        [Required]
        public string Name { get; set; } = "";

        public string? Description { get; set; } = "";

        [Required]
        public bool IsPrivate { get; set; } = true;

        public string? Avatar { get; set; } = "";

        public RequestCreateCourseDTO(string name, string? description, bool isPrivate, string? avatar)
        {
            Name = name;
            Description = description;
            IsPrivate = isPrivate;
            Avatar = avatar;
        }

        public RequestCreateCourseDTO()
        {
        }
    }
}