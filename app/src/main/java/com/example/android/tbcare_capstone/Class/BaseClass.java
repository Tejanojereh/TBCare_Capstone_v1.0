package com.example.android.tbcare_capstone.Class;

public class BaseClass extends Account {
    public int ID;
    public String Username;
    public String Password;
    public String Contact_No;
    public String Security_Question1;
    public String Security_Question2;
    public String Security_Answer1;
    public String Security_Answer2;

    public void SetUsername(String value)
    {
        this.Username = value;
    }

    public String GetUsername()
    {
        return this.Username;
    }

    public void SetPassword(String value)
    {
        try {
            this.Password = encrypt(this.Username, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetPassword()
    {
        return this.Password;
    }
}
