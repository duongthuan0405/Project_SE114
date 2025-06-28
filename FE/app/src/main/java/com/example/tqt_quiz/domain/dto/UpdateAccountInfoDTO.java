package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateAccountInfoDTO {
    @SerializedName("lastMiddleName")
    String lastMiddleName;
    @SerializedName("firstName")
    String firstName;
    public String getLastMiddleName() {
        return lastMiddleName;
    }

    public void setLastMiddleName(String lastMiddleName) {
        this.lastMiddleName = lastMiddleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
