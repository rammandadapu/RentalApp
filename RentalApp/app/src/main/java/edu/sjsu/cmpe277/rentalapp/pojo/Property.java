package edu.sjsu.cmpe277.rentalapp.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * Created by ram.mandadapu on 5/7/16.
 */
public class Property implements Serializable {



    private String name;
    @JsonProperty("desc")
    private String description;
    private String type;
    @JsonProperty("bedNo")
    private int noOfBedRooms;
    @JsonProperty("bathNo")
    private int noOfBathRooms;
    private Address address;
    private int size;
    private double price;
    @JsonProperty("viewCount")
    private int noOfViwes;
    private String status;
    private String phone;
    @JsonProperty("email")
    private String userEmail;
    private String imageUrl;
    @JsonProperty("createdBy")
    private String uniqueUserId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNoOfBedRooms() {
        return noOfBedRooms;
    }

    public void setNoOfBedRooms(int noOfBedRooms) {
        this.noOfBedRooms = noOfBedRooms;
    }

    public int getNoOfBathRooms() {
        return noOfBathRooms;
    }

    public void setNoOfBathRooms(int noOfBathRooms) {
        this.noOfBathRooms = noOfBathRooms;
    }

    public Address getAddress() {
        if(null==address)
            this.address=new Address();
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNoOfViwes() {
        return noOfViwes;
    }

    public void setNoOfViwes(int noOfViwes) {
        this.noOfViwes = noOfViwes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUniqueUserId() {
        return uniqueUserId;
    }

    public void setUniqueUserId(String uniqueUserId) {
        this.uniqueUserId = uniqueUserId;
    }

    public class Address{
        @JsonProperty("line1")
        private String line;
        private String city;
        private String state;
        private String zip;

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }
    }
}

