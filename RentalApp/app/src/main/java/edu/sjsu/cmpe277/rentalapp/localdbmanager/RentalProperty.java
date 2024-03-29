package edu.sjsu.cmpe277.rentalapp.localdbmanager;

/**
 * Created by divya.chittimalla on 4/26/16.
 */
public class RentalProperty {
    String _id;
    String address;
    String addressLine1;
    String addressCity;
    String addressState;
    String addressZip;
    String price;
    String bed;
    String bath;
    String bedBath;
    String image_url;
    String createdBy;
    public RentalProperty() {
    }

    public RentalProperty(String _id, String addressLine1, String addressCity, String addressState,
                          String addressZip, String price, String bed, String bath, String image_url,
                          String createdBy) {
        this._id = _id;
        this.addressLine1 = addressLine1;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.addressZip = addressZip;
        this.price = price;
        this.bed = bed;
        this.bath = bath;
        this.image_url = image_url;
        this.createdBy=createdBy;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressZip() {
        return addressZip;
    }

    public void setAddressZip(String addressZip) {
        this.addressZip = addressZip;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getBath() {
        return bath;
    }

    public void setBath(String bath) {
        this.bath = bath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBedBath() {
        return bedBath;
    }

    public void setBedBath(String bedBath) {
        this.bedBath = bedBath;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
