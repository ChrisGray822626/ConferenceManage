package com.example.chris.conference_manage.Class;

/**
 * Created by asus on 2019/2/25.
 */

public class Staff {

    private String id;
    private String password;
    private String name;
    private String telephone;
    private String rank;

    public Staff(String id, String password, String name, String telephone, String rank){
        this.id = id;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.rank = rank;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getRank() {
        return rank;
    }
}
