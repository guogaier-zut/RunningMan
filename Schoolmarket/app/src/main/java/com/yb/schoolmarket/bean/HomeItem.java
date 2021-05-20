package com.yb.schoolmarket.bean;


import java.io.Serializable;

public class HomeItem implements Serializable {

    private long id;
    private int type;
    private String info;
    private String address;
    private String name;
    private String phone;
    private double money;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public HomeItem(long id, int type, String info, String address, String name, String phone, double money, String time) {
        this.id = id;
        this.type = type;
        this.info = info;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.money = money;
        this.time = time;
    }

    public HomeItem() {
    }

    @Override
    public String toString() {
        return "HomeItem{" +
                "id=" + id +
                ", type=" + type +
                ", info='" + info + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", money=" + money +
                ", time='" + time + '\'' +
                '}';
    }
}
