package com.yb.schoolmarket.bean;



public class Legwork {
    private Long id;
    private String openid;
    private String type;
    private String info;
    private String name;
    private String phone;
    private String address;
    private double money;
    private int status;
    private String time;


    public Legwork(Long id, String openid, String type, String info, String name, String phone, String address, double money, int status, String time) {
        this.id = id;
        this.openid = openid;
        this.type = type;
        this.info = info;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.money = money;
        this.status = status;
        this.time = time;
    }

    public Legwork() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Legwork{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", type='" + type + '\'' +
                ", info='" + info + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", money=" + money +
                ", status=" + status +
                ", time='" + time + '\'' +
                '}';
    }
}
