package com.example.tqt_quiz.presentation.classes;

import com.example.tqt_quiz.domain.dto.AccountInfo;

import java.io.Serializable;

public class Member implements Serializable {
    private String id;
    private String avatar;
    private String firstName;
    private String lastMiddleName;
    private String fullName;
    private String type;
    private String email;

    public Member(String id, String avatarResId, String firstName, String lastMiddleName, String type,  String email) {
        this.id = id;
        this.avatar = avatarResId;
        this.firstName = firstName;
        this.lastMiddleName = lastMiddleName;
        this.fullName = lastMiddleName + " " + firstName;
        this.type = type;
        this.email = email;
    }

    public Member(AccountInfo accountInfo) {
        this.id = accountInfo.getUserID();
        this.avatar = accountInfo.getAvatar();
        this.firstName = accountInfo.getFirstName();
        this.lastMiddleName = accountInfo.getLastMiddleName();
        this.fullName = accountInfo.getFullName();
        this.type = accountInfo.getAccountType();
        this.email = accountInfo.getEmail();
    }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastMiddleName() { return lastMiddleName; }

    public void setLastMiddleName(String lastMiddleName) { this.lastMiddleName = lastMiddleName; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getName(){
        return lastMiddleName + " " + firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastMiddleName='" + lastMiddleName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", type='" + type + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
