package ido.apps.finance.budge.data;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import ido.apps.finance.budge.util.Ided;

@Entity(tableName = "budgets")
public class Budget extends Ided {

    @PrimaryKey
    private long id;
    private double budgetSize, leftBudget;
    private String subject;
    //private int image;
    private List<Transaction> transactions;


    public Budget(long id, double budgetSize, double leftBudget, String subject, List<Transaction> transactions) {
        this.id = id;
        this.budgetSize = budgetSize;
        this.leftBudget = leftBudget;
        this.subject = subject;
        //this.image = image;
        this.transactions = transactions;
    }

    public static Budget createBudget(String subject, double budgetSize) {
        return new Budget(
                createUniqueId(),
                budgetSize,
                budgetSize,
                subject,
                new ArrayList<Transaction>()
        );
        //TODO: add an image this.image = ???
    }


    public double getBudgetSize() {
        return budgetSize;
    }

    public double getLeftBudget() {
        return leftBudget;
    }

    public String getSubject() {
        return subject;
    }

//    //public int getImage() {
//        return image;
//    }

    public long getId() {
        return id;
    }

    public List<Transaction> getTransactions() {return transactions;}

    public void setBudgetSize(double budgetSize) {
        if (budgetSize <= 0) {
            //TODO: raise an error
        }
        this.budgetSize = budgetSize;
    }

    public void setLeftBudget(double leftBudget) {
        this.leftBudget = leftBudget;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

//    public void setImage(int image) {
//        this.image = image;
//    }
//}
