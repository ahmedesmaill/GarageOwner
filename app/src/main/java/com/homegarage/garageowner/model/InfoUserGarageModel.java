package com.homegarage.garageowner.model;

public class InfoUserGarageModel {
    private String id;
    private int NumOfRatings;
    private float  Rate;
    private float priceForHour;
    private String nameEn , nameAr;
    private String phone;
    private float balance;
    private String email;
    private String governoateEn , governoateAR;
    private String cityEn, cityAr;
    private String restOfAddressEN , restOfAddressAr;
    private String location;
    private String imageGarage;
    private float rate;
    private int numOfRatings;
    private String nationalIDImg1,nationalIDImg2;
    private String bankAcountName, bankAcountNum;



    public String getBankAcountName() {
        return bankAcountName;
    }

    public void setBankAcountName(String bankAcountName) {
        this.bankAcountName = bankAcountName;
    }

    public String getBankAcountNum() {
        return bankAcountNum;
    }

    public void setBankAcountNum(String bankAcountNum) {
        this.bankAcountNum = bankAcountNum;
    }

    public int getNumOfRatings() {
        return NumOfRatings;
    }

    public String getNationalIDImg1() {
        return nationalIDImg1;
    }

    public void setNationalIDImg1(String nationalIDImg1) {
        this.nationalIDImg1 = nationalIDImg1;
    }

    public String getNationalIDImg2() {
        return nationalIDImg2;
    }

    public void setNationalIDImg2(String nationalIDImg2) {
        this.nationalIDImg2 = nationalIDImg2;
    }

    public void setNumOfRatings(int numOfRatings) {
        NumOfRatings = numOfRatings;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getPriceForHour() { return priceForHour; }

    public void setPriceForHour(float priceForHour) { this.priceForHour = priceForHour;}

    public String getImageGarage() { return imageGarage; }

    public void setImageGarage(String imageGarage) { this.imageGarage = imageGarage; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNameEn() { return nameEn; }

    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getNameAr() { return nameAr; }

    public void setNameAr(String nameAr) { this.nameAr = nameAr; }

    public String getGovernoateEn() { return governoateEn; }

    public void setGovernoateEn(String governoateEn) { this.governoateEn = governoateEn; }

    public String getGovernoateAR() { return governoateAR; }

    public void setGovernoateAR(String governoateAR) { this.governoateAR = governoateAR; }

    public String getCityEn() { return cityEn; }

    public void setCityEn(String cityEn) { this.cityEn = cityEn; }

    public String getCityAr() { return cityAr; }

    public void setCityAr(String cityAr) { this.cityAr = cityAr; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getRestOfAddressEN() { return restOfAddressEN; }

    public void setRestOfAddressEN(String restOfAddressEN) { this.restOfAddressEN = restOfAddressEN; }

    public String getRestOfAddressAr() { return restOfAddressAr; }

    public void setRestOfAddressAr(String restOfAddressAr) { this.restOfAddressAr = restOfAddressAr; }

    public String getPhone() { return phone;}

    public void setPhone(String phone) { this.phone = phone;}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

}
