package com.yb.schoolmarket.bean;

public class Order {
    String id;
    String workerid;
    HomeItem homeItem;
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    public HomeItem getHomeItem() {
        return homeItem;
    }

    public void setHomeItem(HomeItem homeItem) {
        this.homeItem = homeItem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(String workerid, HomeItem homeItem) {
        this.workerid = workerid;
        this.homeItem = homeItem;
    }

    public Order(String id, String workerid, HomeItem homeItem) {
        this.id = id;
        this.workerid = workerid;
        this.homeItem = homeItem;
    }

    public Order(String workerid, HomeItem homeItem, String status) {
        this.workerid = workerid;
        this.homeItem = homeItem;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "workerid='" + workerid + '\'' +
                ", homeItem=" + homeItem +
                ", status='" + status + '\'' +
                '}';
    }
}
