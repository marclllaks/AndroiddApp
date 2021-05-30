package com.example.application;

import java.util.Date;

public class PractitionerItem {
    private String name;
    private Integer identifier;
    private boolean active;
    private String telecom;
    private String address;
    private String gender;
    private Date birthDate;
    private String photo; // todo
    private String communication;
    private String qualification;

    public PractitionerItem(String name, Integer identifier, boolean active, String telecom, String address, String gender, Date birthDate, String photo, String communication, String qualification) {
        this.name = name;
        this.identifier = identifier;
        this.active = active;
        this.telecom = telecom;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.photo = photo;
        this.communication = communication;
        this.qualification = qualification;
    }

    public PractitionerItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
