package com.example.booksmanagement;

import com.example.book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookController {
    @FXML
    protected Button cancelButton;

    @FXML
    protected TextField titleTF;

    @FXML
    protected TextField authorTF;

    @FXML
    protected TextField tagsTF;
    @FXML
    protected Button loadImageButton;

    @FXML
    protected Button defaultImageButton;

    @FXML
    protected ComboBox ratingComboBox;

    @FXML
    protected ComboBox dayComboBox;

    @FXML
    protected ComboBox monthComboBox;

    @FXML
    protected TextField yearTF;

    @FXML
    protected ImageView bookCover;

    protected MainController mainController;

    public void initialize(MainController mainController){
        setMainController(mainController);
        fillRatingComboBox();
        fillDayComboBox();
        fillMonthComboBox();
        initializeYearTextField();
        initializeBookCover();
    }

    protected void initializeBookCover(){
        bookCover.setImage(Book.defaultBookCover);
    }

    protected void initializeYearTextField(){
        yearTF.setTextFormatter(new TextFormatter<>(change->{
            if(change.getControlNewText().matches("^[1-9]?|[1-9]\\d{0,3}$")){
                return change;
            } else return null;
        }));
    }

    protected void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    protected void fillMonthComboBox(){
        List<Object> list = new ArrayList<>();
        list.add("Month");
        list.addAll(Arrays.asList(Month.values()));
        monthComboBox.setItems(FXCollections.observableList(list));
    }

    protected void fillDayComboBox(){
        dayComboBox.setItems(FXCollections.observableList(Arrays.asList(
                "Day", 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)));
    }

    protected void fillRatingComboBox(){
        ObservableList<String> ratings = FXCollections.observableArrayList();
        ratings.add("");
        ratings.add("\u2740");
        ratings.add("\u2740 \u2740");
        ratings.add("\u2740 \u2740 \u2740");
        ratings.add("\u2740 \u2740 \u2740 \u2740");
        ratings.add("\u2740 \u2740 \u2740 \u2740 \u2740");

        ratingComboBox.setItems(ratings);
    }

    @FXML
    protected void onCancelButtonClicked(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    protected String pathToBookCover;
    @FXML
    protected void onLoadImageButtonClicked(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

        File selectedFile = fileChooser.showOpenDialog(bookCover.getScene().getWindow());
        if(selectedFile != null){
            pathToBookCover = selectedFile.getAbsolutePath();

            bookCover.setImage(new Image(pathToBookCover));
        }
    }

    @FXML
    protected void onDefaultImageButtonClicked(){
        bookCover.setImage(Book.defaultBookCover);
    }
}
