package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Member implements Serializable {
    private int avatar;
    private String firstName;
    private String lastMiddleName;
    private String email;

    public Member(int avatarResId, String firstName, String lastMiddleName, String email) {
        this.avatar = avatarResId;
        this.firstName = firstName;
        this.lastMiddleName = lastMiddleName;
        this.email = email;
    }

    public int getAvatar() { return avatar; }

    public void setAvatar(int avatar) { this.avatar = avatar; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastMiddleName() { return lastMiddleName; }

    public void setLastMiddleName(String lastMiddleName) { this.lastMiddleName = lastMiddleName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
