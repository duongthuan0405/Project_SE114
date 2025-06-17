package com.example.tqt_quiz.domain.repository.token;

public interface ITokenManager {
    public void SaveToken(String Token);
    public String GetToken();
    public void ClearToken();
}
