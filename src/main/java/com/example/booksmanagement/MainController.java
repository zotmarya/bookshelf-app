package com.example.booksmanagement;

import com.example.book.Book;
import com.example.book.BooksManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class MainController {
    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;
    @FXML
    private Label datePublishedLabel;
    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private Label datePublished;

    @FXML
    private ImageView coverImage;
    @FXML
    private TableView<Book> table;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> ratingColumn;

    @FXML
    private TableColumn<Book, String> tagsColumn;

    @FXML
    private TableColumn<Book, String> datePublishedColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button searchButton;
    @FXML
    private TextField searchTF;

    @FXML
    private Label tagsLabel;

    @FXML
    private Label tags;

    private BooksManager booksManager;

    @FXML
    private void onAddButtonClicked() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddBookFrame.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 670, 380);
        AddBookController addBookController = fxmlLoader.getController();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Add book");
        stage.setScene(scene);
        addBookController.initialize(this);
        stage.showAndWait();
    }




    public void initialize(){
        booksManager = BooksManager.getInstance();
        initializeTable();
        initializeImage();
    }

    private void initializeImage(){
        coverImage.setImage(Book.defaultBookCover);
    }

    public void displayBookInfo(){
        deleteButton.setDisable(false);
        editButton.setDisable(false);
        Book book = table.getSelectionModel().getSelectedItem();

        titleLabel.setVisible(true);
        title.setText(book.getTitle());

        authorLabel.setVisible(true);
        author.setText(book.getAuthor());

        if(book.getTags() != null){
            tagsLabel.setVisible(true);
            tags.setText(Arrays.toString(book.getTags()).replaceAll("\\[|\\]", ""));
        } else{
            tags.setText("");
            tagsLabel.setVisible(false);
        }

        if(book.getBookCover() != null){
            coverImage.setImage(new Image(book.getBookCover()));
        } else coverImage.setImage(Book.defaultBookCover);

        if(book.getDatePublished() != null){
            datePublishedLabel.setVisible(true);
            datePublished.setText(book.getDatePublished());
        } else {
            datePublished.setText("");
            datePublishedLabel.setVisible(false);
        }
    }

    private void initializeTable(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tagsColumn.setCellValueFactory(new PropertyValueFactory<>("tags"));
        tagsColumn.setCellValueFactory(cellData->{
            String[] tags = cellData.getValue().getTags();
            String tagsStr = Arrays.toString(tags).replaceAll("\\[|\\]","");
            if(tags == null) tagsStr = "";
            return new SimpleStringProperty(tagsStr);
        });

        datePublishedColumn.setCellValueFactory(new PropertyValueFactory<>("datePublished"));

        table.setFixedCellSize(27);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldselection, newSelection)->{
            if(newSelection != null){
               displayBookInfo();
            } else{
                deleteButton.setDisable(true);
                editButton.setDisable(true);

                title.setText("");
                titleLabel.setVisible(false);

                author.setText("");
                authorLabel.setVisible(false);

                datePublished.setText("");
                datePublishedLabel.setVisible(false);

                tags.setText("");
                tagsLabel.setVisible(false);

                coverImage.setImage(Book.defaultBookCover);
            }
        });

        updateTable();
    }

    @FXML
    private void onEditButtonClicked() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditBookFrame.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 670, 380);
        EditBookController editBookController = fxmlLoader.getController();
        editBookController.setBookToEdit(table.getSelectionModel().getSelectedItem());

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit book");
        stage.setScene(scene);
        editBookController.initialize(this);
        stage.showAndWait();
    }


    @FXML
    private void onDeleteButtonClicked(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm action");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("If you click delete button, information about the book will be deleted");

        ButtonType buttonTypeOne = new ButtonType("Delete");
        ButtonType buttonTypeTwo = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == buttonTypeOne) {
            Book book = table.getSelectionModel().getSelectedItem();

            booksManager.getBooks().remove(book);
            booksManager.updateBooksJSON();

            Alert alertDeleted = new Alert(Alert.AlertType.INFORMATION);
            alertDeleted.setHeaderText("");
            alertDeleted.setTitle("");
            alertDeleted.setContentText("Book was successfully deleted");

            updateTable();
        }
    }

    @FXML
    private void onSearchButtonClicked(){
        String text = searchTF.getText().trim().toLowerCase();
        if(text.length() == 0){
            updateTable();
            return;
        }

        String[] searchWords = text.split(" ");

        ObservableList<Book> books = FXCollections.observableArrayList();

        for(Book book: booksManager.getBooks()){
            for (int i = 0; i < searchWords.length; i++) {
                int ind = i;

                if(!book.getAuthor().toLowerCase().contains(searchWords[i])
                        && !book.getTitle().toLowerCase().contains(searchWords[i])
                        && !(book.getTags() != null && Arrays.stream(book.getTags()).anyMatch(tag -> tag.toLowerCase().equals(searchWords[ind]))))
                    break;


                if(i == searchWords.length-1) books.add(book);
            }
        }

        table.setItems(books);
    }

    public TableView<Book> getTable() {
        return table;
    }

    public void updateTable(){
        ObservableList<Book> booksItems = FXCollections.observableArrayList(booksManager.getBooks());

        table.setItems(booksItems);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().
                multiply(Bindings.size(table.getItems()).add(1)));
    }

}