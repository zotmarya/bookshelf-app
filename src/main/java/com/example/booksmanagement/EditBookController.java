package com.example.booksmanagement;

import com.example.book.Book;
import com.example.book.BooksManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class EditBookController extends BookController{

    @FXML
    private Button saveButton;

    private Book bookToEdit;

    public void setBookToEdit(Book book){
        bookToEdit = book;
    }

    @Override
    public void initialize(MainController mainController){
        super.initialize(mainController);
        initializeComponents();
    }

    private void initializeComponents(){
        titleTF.setText(bookToEdit.getTitle());
        authorTF.setText(bookToEdit.getAuthor());

        String date = bookToEdit.getDatePublished();
        if(date != null){
            String[] dateElements = date.split("-");
            dayComboBox.setValue(dateElements[0]);
            monthComboBox.setValue(Month.of(Integer.parseInt(dateElements[1])));
            yearTF.setText(dateElements[2]);
        }
        if(bookToEdit.getBookCover() != null) bookCover.setImage(new Image(bookToEdit.getBookCover()));
        if (bookToEdit.getTags() != null){

            String[] tags = bookToEdit.getTags();

            for (int i = 0; i < tags.length; i++) {
                if(tags[i].contains("\s")) tags[i] = '\"' + tags[i] + '\"';
            }

            tagsTF.setText(String.join(" ", tags));
        }

        if (bookToEdit.getRating() != null) ratingComboBox.setValue(bookToEdit.getRating());

    }

    @FXML
    private void onSaveButtonClicked(){
        String title = titleTF.getText().trim();
        String author = authorTF.getText().trim();
        String tagString = tagsTF.getText().trim();
        String ratings = null;
        Object ratingItem = ratingComboBox.getSelectionModel().getSelectedItem();
        if(ratingItem != null) ratings = ratingItem.toString();

        String date;
        Object day = null;
        Object dayObject = dayComboBox.getSelectionModel().getSelectedItem();
        if(!(dayObject instanceof String)) day = dayObject;
        Month month = null;
        Object monthObject = monthComboBox.getSelectionModel().getSelectedItem();
        if(monthObject instanceof Month) month = (Month) monthObject;
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
                alert.setContentText("Proceed saving edited book without date?\n" +
                        "You can modify it later or click cancel and write correct date");
                alert.setTitle("Warning");
                alert.setHeaderText("Date information is not fully filled");

                ButtonType buttonTypeOne = new ButtonType("Save book");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonTypeTwo || result.get() == null) return;
            }



            bookToEdit.setBookCover(pathToBookCover);
            if(day != null && month != null && year.length() != 0){
                if(day.toString().length() == 1) day = "0"+day;
                String monthNumber;
                if(month.getValue() < 10) monthNumber = "0"+month.getValue();
                else monthNumber = month.getValue() + "";

                date = day+"-"+monthNumber+"-"+year;

                bookToEdit.setDatePublished(date);
            } else{
                bookToEdit.setDatePublished(null);
            }
            bookToEdit.setRating(ratings);
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

                bookToEdit.setTags(tags.toArray(new String[0]));
            }else{
                bookToEdit.setTags(null);
            }

            BooksManager.getInstance().updateBooksJSON();
            mainController.getTable().refresh();
            mainController.displayBookInfo();
            ((Stage) saveButton.getScene().getWindow()).close();
        }
    }
}
