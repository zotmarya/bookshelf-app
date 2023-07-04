module com.example.booksmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires com.google.gson;


    opens com.example.booksmanagement to javafx.fxml;
    opens com.example.book to javafx.base, com.google.gson;
    exports com.example.booksmanagement;
}