package com.example.cscb07s22finalproject;

public class User
{
    // Note: Made variables private to ensure safety. Getters and Setters are implemented
    public String username;
    public String password;
    public boolean adminFlag;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
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
}
