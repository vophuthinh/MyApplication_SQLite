package com.example.myapplication;

public class Department {
    private String code;
    private String name;
    private String phone;

    public Department(String code, String name, String phone) {
        this.code = code;
        this.name = name;
        this.phone = phone;
    }

    public Department() {

    }
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "DepartmentClass{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
