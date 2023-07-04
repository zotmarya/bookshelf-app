package com.example.booksmanagement;

import com.example.book.Book;
import com.example.book.BooksManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;


import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

public class AddBookController extends BookController{
    @FXML
    private Button addButton;

    @FXML
    private void onAddButtonClicked(){
        String title = titleTF.getText().trim();
        String author = authorTF.getText().trim();
        String tagString = tagsTF.getText().trim();
        String ratings = null;
        Object ratingItem = ratingComboBox.getSelectionModel().getSelectedItem();
        if(ratingItem != null) ratings = ratingItem.toString();

        String date;
        Object day = dayComboBox.getSelectionModel().getSelectedItem();
        Month month = (Month) monthComboBox.getSelectionModel().getSelectedItem();
        String year = yearTF.getText();

        if(title.length() == 0 || author.length() == 0){
            StringBuilder infoToFill = new StringBuilder();
            if(title.length() == 0){
                infoToFill.append(" title");
            }
            if(author.length() == 0){
                if(title.length() == 0) infoToFill.append(",");
                infoToFill.append(" author");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("");
            alert.setContentText("Not all necessary information was written. Fill in: " + infoToFill);
            alert.showAndWait();
        } else{
            if((day == null || month == null || year.length() == 0) && (day != null || month != null || year.length() != 0)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Proceed adding book without date?\n" +
                        "You can modify it later or click cancel and write correct date");
                alert.setTitle("Warning");
                alert.setHeaderText("Not all date information was filled");

                ButtonType buttonTypeOne = new ButtonType("Add book");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeTwo || result.get() == null) return;
            }



            Book book = new Book(title, author);
            if(pathToBookCover != null) book.setBookCover(pathToBookCover);
            if(day != null && month != null && year.length() != 0){
                if(day.toString().length() == 1) day = "0"+day;
                String monthNumber;
                if(month.getValue() < 10) monthNumber = "0"+month.getValue();
                else monthNumber = month.getValue() + "";

                date = day+"-"+monthNumber+"-"+year;

                book.setDatePublished(date);
            }
            if(ratings != null) book.setRating(ratings);
            if(tagString.length() != 0){

                char[] charTagString = tagString.toCharArray();
                char[] tag = new char[charTagString.length+1];
                ArrayList<String> tags = new ArrayList<>();
                boolean isQuotePresent = false;

                for (int i = 0, j = 0; i < charTagString.length; i++) {
                    if(charTagString[i] == '\"'){
                        isQuotePresent = true;
                    } else if(isQuotePresent){
                        if(charTagString[i] == '"') isQuotePresent = false;
                        else tag[j++] = charTagString[i];
                    } else if(charTagString[i] != ' '){
                        tag[j++] = charTagString[i];
                    } else if (j != 0){
                        tags.add(new String(tag, 0, j));
                        j = 0;
                    }

                    if(i == charTagString.length-1){
                        tags.add(new String(tag,0, j));
                    }
                }

                book.setTags(tags.toArray(new String[0]));
            }

            BooksManager.getInstance().addBook(book);
            mainController.updateTable();
            ((Stage) addButton.getScene().getWindow()).close();
        }

    }

    @Test
    public void checkTagsSplitter(){
        String tagString = "\"Hello, world\"";

        char[] charTagString = tagString.toCharArray();
        char[] tag = new char[charTagString.length+1];
        ArrayList<String> tags = new ArrayList<>();
        boolean isQuotePresent = false;

        for (int i = 0, j = 0; i < charTagString.length; i++) {
            if(charTagString[i] == '\"'){
                isQuotePresent = true;
            } else if(isQuotePresent){
                if(charTagString[i] == '"') isQuotePresent = false;
                else tag[j++] = charTagString[i];
            } else if(charTagString[i] != ' '){
                tag[j++] = charTagString[i];
            } else if (j != 0){
                tags.add(new String(tag, 0, j));
                j = 0;
            }

            if(i == charTagString.length-1){
                tags.add(new String(tag,0, j));
            }
        }

        for(String str: tags){
            System.out.println(str);
        }

        Assert.assertTrue(tags.get(0).equals("Hello, world"));
    }
}
