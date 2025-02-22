package com.employeemanagement.dto;

public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;

    public PasswordUpdateRequest() {
    }

    public PasswordUpdateRequest(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

