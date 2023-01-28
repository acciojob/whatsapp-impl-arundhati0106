package com.driver;

import java.util.HashSet;

public class User {
    //attributes
    private String name;
    private String mobile;

    //getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    //constructor
    public User() {
        //default constructor
    }
    public User(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }
}
