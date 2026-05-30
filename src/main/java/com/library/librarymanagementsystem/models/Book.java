package com.library.librarymanagementsystem.models;

public class Book {
    private int    id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int    quantity;
    private int    available;
    private double price;
    private int    publishedYear;

    public Book() {}

    public Book(int id, String title, String author, String isbn,
                String category, int quantity, int available,
                double price, int publishedYear) {
        this.id            = id;
        this.title         = title;
        this.author        = author;
        this.isbn          = isbn;
        this.category      = category;
        this.quantity      = quantity;
        this.available     = available;
        this.price         = price;
        this.publishedYear = publishedYear;
    }

    // Getters & Setters
    public int    getId()                       { return id; }
    public void   setId(int id)                 { this.id = id; }

    public String getTitle()                    { return title; }
    public void   setTitle(String title)        { this.title = title; }

    public String getAuthor()                   { return author; }
    public void   setAuthor(String author)      { this.author = author; }

    public String getIsbn()                     { return isbn; }
    public void   setIsbn(String isbn)          { this.isbn = isbn; }

    public String getCategory()                 { return category; }
    public void   setCategory(String category)  { this.category = category; }

    public int    getQuantity()                         { return quantity; }
    public void   setQuantity(int quantity)             { this.quantity = quantity; }

    public int    getAvailable()                        { return available; }
    public void   setAvailable(int available)           { this.available = available; }

    public double getPrice()                            { return price; }
    public void   setPrice(double price)                { this.price = price; }

    public int    getPublishedYear()                    { return publishedYear; }
    public void   setPublishedYear(int publishedYear)   { this.publishedYear = publishedYear; }
}
