package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;
public class RegisterRequest {
    @SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;

    @SerializedName("firstName")
    private String FirstName;

    @SerializedName("lastMiddleName")
    private String LastMiddleName;
    public RegisterRequest(String email, String password, String Firstname, String LastMiddleName)
    {
        this.Email=email;
        this.Password=password;
        this.FirstName=Firstname;
        this.LastMiddleName=LastMiddleName;
    }

    // Getters and Setters
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastMiddleName() {
        return LastMiddleName;
    }

    public void setLastMiddleName(String lastMiddleName) {
        this.LastMiddleName = lastMiddleName;
    }

}