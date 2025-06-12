package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;
public class RegisterInfo {
    @SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;

    @SerializedName("firstName")
    private String FirstName;

    @SerializedName("lastMiddleName")
    private String LastMiddleName;

    @SerializedName("accountTypeId")
    private String AccountTypeID;
    public RegisterInfo(String email,String password,String Firstname,String LastMiddleName,String AccountTypeID)
    {
        this.Email=email;
        this.Password=password;
        this.FirstName=Firstname;
        this.LastMiddleName=LastMiddleName;
        this.AccountTypeID=AccountTypeID;
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

    public String getAccountTypeID() {
        return AccountTypeID;
    }

    public void setAccountTypeID(String accountTypeID) {
        this.AccountTypeID = accountTypeID;
    }
}