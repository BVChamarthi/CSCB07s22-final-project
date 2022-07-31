package com.example.cscb07s22finalproject;

public class User
{
    // Note: Made variables private to ensure safety. Getters and Setters are implemented
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin)
    {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
