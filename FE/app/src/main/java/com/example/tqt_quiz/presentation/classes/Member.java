package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Member implements Serializable {
    private String avatar;
    private String firstName;
    private String lastMiddleName;
    private String type;
    private String email;

    public Member(String avatarResId, String firstName, String lastMiddleName, String type,  String email) {
        this.avatar = avatarResId;
        this.firstName = firstName;
        this.lastMiddleName = lastMiddleName;
        this.type = type;
        this.email = email;
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
}
