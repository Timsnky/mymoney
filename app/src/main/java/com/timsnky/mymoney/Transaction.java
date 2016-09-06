package com.timsnky.mymoney;

/**
 * Created by Timsnky on 3/2/2015.
 */
public class Transaction {
    private int tranCategory, tranAccount;
    private long tranDate;
    private float tranAmount;
    private String tranDescription;

    public Transaction(){
    }

    public Transaction(int tranCategory, long tranDate, float tranAmount, String tranDescription, int tranAccount){
        this.tranCategory = tranCategory;
        this.tranDate = tranDate;
        this.tranAmount = tranAmount;
        this.tranDescription = tranDescription;
        this.tranAccount = tranAccount;
    }

    public int getTranCategory() {
        return tranCategory;
    }

    public void setTranCategory(int tranCategory) {
        this.tranCategory = tranCategory;
    }

    public long getTranDate() {
        return tranDate;
    }

    public void setTranDate(long tranDate) {
        this.tranDate = tranDate;
    }

    public float getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(float tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getTranDescription() {
        return tranDescription;
    }

    public void setTranDescription(String tranDescription) {
        this.tranDescription = tranDescription;
    }

    public int getTranAccount() {
        return tranAccount;
    }

    public void setTranAccount(int tranAccount) {
        this.tranAccount = tranAccount;
    }

    public String showTransaction(){
        String display = "";
        display += "Date: " + this.tranDate;
        display += " Description: " +  this.tranDescription;
        display += " Account: " + this.tranAccount;
        display += " Category: " + this.tranCategory;
        display += " Amount: " + this.tranAmount;
        return  display;
    }
}
