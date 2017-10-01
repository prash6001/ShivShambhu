package com.pjadhav.myapplication.model;

/**
 * Created by pjadhav on 7/15/17.
 */

public class Quotes {
    public int id;

    public Quotes(){}

    public Quotes(int id, String quoteText, int isRead) {
        this.id = id;
        this.quoteText = quoteText;
        this.isReadAlready = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public int getIsReadAlready() {
        return isReadAlready;
    }

    public void setIsReadAlready(int isReadAlready) {
        this.isReadAlready = isReadAlready;
    }

    private String quoteText;
    private int isReadAlready;
}
