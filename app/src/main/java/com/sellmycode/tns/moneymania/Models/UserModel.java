package com.sellmycode.tns.moneymania.Models;

public class UserModel {

   private String name,email,password,profile,referCode;
   private int coins,spins,scratch;
   private String uId;

    public UserModel(String name, String email, String password, String profile, String referCode, int coins, int spins, int scratch) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.referCode = referCode;
        this.coins = coins;
        this.spins = spins;
        this.scratch = scratch;
    }

    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSpins() {
        return spins;
    }

    public void setSpins(int spins) {
        this.spins = spins;
    }

    public int getScratch() {
        return scratch;
    }

    public void setScratch(int scratch) {
        this.scratch = scratch;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
