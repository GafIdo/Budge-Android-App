package ido.apps.finance.budge.data;

import java.util.Date;

import ido.apps.finance.budge.util.Ided;

public class Transaction extends Ided {

    private long id;
    private String payee;
    private double payment;
    private int expanseSign;
    private Date date;

    public Transaction(String payee, double payment, Date date, int expanseSign){

        this.payee = payee;
        this.payment = payment;
        this.date = date;
        this.id = createUniqueId();
        this.expanseSign = expanseSign;

    }


    public int getExpanseSign() {
        return expanseSign;
    }

    public void setExpanseSign(int expanseSign) {
        this.expanseSign = expanseSign;
    }

    public long getId() {
        return id;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getPayment() {
        return payment;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setId(long transactionId) {
        id = transactionId;
    }
}
