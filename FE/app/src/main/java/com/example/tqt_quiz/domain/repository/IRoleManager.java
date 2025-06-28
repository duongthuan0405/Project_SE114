package com.example.tqt_quiz.domain.repository;

public interface IRoleManager {
    void SaveRole(String RoleID);
    String GetRole();
    void ClearRole();
}
