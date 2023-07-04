package com.example.book;

import javafx.scene.image.Image;

public class Book {
    public static int lastId;
    private int id;
    private String bookCover;

    public static Image defaultBookCover;
    static{
        try {
            defaultBookCover = new Image("book.png");
        } catch(IllegalArgumentException exception){
            System.out.println(exception);
            defaultBookCover = null;
        }
    }

    private String title;
    private String author;
    private String rating;
    private String[] tags;
    private String datePublished;

    public Book(String title, String author) {
        id = ++lastId;
        this.title = title;
        this.author = author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String[] getTags() {
        return tags;
    }

}
