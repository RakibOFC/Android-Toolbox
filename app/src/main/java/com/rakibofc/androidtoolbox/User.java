package com.rakibofc.androidtoolbox;

public class User {

    public String fullName;
    public String email;

    public User() { }

    public User(String fullName, String email) {

        this.fullName = fullName;
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
