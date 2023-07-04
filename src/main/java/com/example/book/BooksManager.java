package com.example.book;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class BooksManager {
    private static final File file = new File("books.json");
    private static BooksManager booksManager;
    private ArrayList<Book> books;

    private BooksManager() {
        if(file.length() != 0){
            try(FileReader reader = new FileReader(file)){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                books = gson.fromJson(reader, new TypeToken<ArrayList<Book>>(){}.getType());
                Book.lastId = books.get(books.size()-1).getId();
            }catch (IOException exception){
                exception.printStackTrace();
            }
        }
        if(books == null) books = new ArrayList<>();
    }

    public void addBook(Book book){
        books.add(book);
        updateBooksJSON();
    }


    public void updateBooksJSON(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(books);

        try(FileWriter writer = new FileWriter(file)){
            writer.write(json);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    public ArrayList<Book> getBooks() {
        return books;
    }

    public static BooksManager getInstance() {
        if(booksManager == null) booksManager = new BooksManager();

        return booksManager;
    }
}
