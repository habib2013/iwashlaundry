package com.example.lalecon.iwashlaundry.model;

import java.util.List;

/**
 * Created by Lalecon on 6/1/2018.
 */

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<Order> subcategpry;
    private String comment;


    public Request() {
    }

    public Request(String phone, String name, String address, String total, String status, List<Order> subcategpry, String comment) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.subcategpry = subcategpry;
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getSubcategpry() {
        return subcategpry;
    }

    public void setSubcategpry(List<Order> subcategpry) {
        this.subcategpry = subcategpry;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
