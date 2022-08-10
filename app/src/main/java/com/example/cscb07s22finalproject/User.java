package com.example.cscb07s22finalproject;

public class User
{
    // Note: Made variables private to ensure safety. Getters and Setters are implemented
    private String username;
    private String password;

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

    @Override
    public String toString()
    {
        return username;
    }

    @Override
    public String toString()
    {
        return username;
    }
}
