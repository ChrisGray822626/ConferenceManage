package com.example.chris.conference_manage.Class;

public class Order {

    private String id;
    private String borrower;
    private String room;
    private String topic;
    private String air_conditioner;
    private String start_time;
    private String end_time;

    public Order(String id,String borrower,String room,String topic,String air_conditioner,String start_time,String end_time){
        this.id = id;
        this.borrower = borrower;
        this.room = room;
        this.topic = topic;
        this.air_conditioner = air_conditioner;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public String getBorrower() {
        return borrower;
    }

    public String getRoom() {
        return room;
    }

    public String getTopic() {
        return topic;
    }

    public String getAir_conditioner() {
        return air_conditioner;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setAir_conditioner(String air_conditioner) {
        this.air_conditioner = air_conditioner;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
