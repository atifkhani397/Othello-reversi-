module com.othello.reversigame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.othello.reversigame to javafx.fxml;
    exports com.othello.reversigame;
}