package com.example.chris.conference_manage.Class;

/**
 * Created by asus on 2019/2/26.
 */

public class Room {

    private String id;
    private String people;
    private String projector;
    private String computer;
    private String microphone;
    private String conditions;

    public Room(String id,String people,String projector,String computer,String microphone,String conditions) {
        this.id = id;
        this.people = people;
        this.projector = projector;
        this.computer = computer;
        this.microphone = microphone;
        this.conditions = conditions;
    }

    public String getId() {
        return id;
    }

    public String getPeople() {
        return people;
    }

    public String getProjector() {
        return projector;
    }

    public String getComputer() {
        return computer;
    }

    public String getMicrophone() {
        return microphone;
    }

    public String getConditions() {
        return conditions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public void setProjector(String projector) {
        this.projector = projector;
    }

    public void setComputer(String computer) {
        this.computer = computer;
    }

    public void setMicrophone(String microphone) {
        this.microphone = microphone;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
