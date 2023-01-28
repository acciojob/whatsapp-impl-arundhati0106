package com.driver;

public class Group {
    //attributes
    private String name;
    private int numberOfParticipants;

    //constructor
    public Group() {
        //default constructor
    }
    public Group(String name, int numberOfParticipants) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
    }

    //getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getNumberOfParticipants() { return numberOfParticipants; }
    public void setNumberOfParticipants(int numberOfParticipants) { this.numberOfParticipants = numberOfParticipants; }
}
