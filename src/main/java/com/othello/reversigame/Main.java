//package com.othello.reversigame;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        BorderPane root = new BorderPane();
//        root.setPadding(new Insets(10));
//
//        // Top Control Bar
//        Label title = new Label("Othello / Reversi");
//        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
//
//        Label status = new Label("Turn: BLACK");
//        Label score = new Label("Black: 2 | White: 2");
//
//        VBox topBox = new VBox(10, title, status, score);
//        topBox.setAlignment(Pos.CENTER);
//        root.setTop(topBox);
//
//        // Center Grid
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setHgap(2);
//        grid.setVgap(2);
//        grid.setStyle("-fx-background-color: #006600; -fx-padding: 5;");
//        root.setCenter(grid);
//
//        // Controller logic
//        GameController controller = new GameController(grid, status, score);
//
//        // Bottom Controls
//        Button restartBtn = new Button("Restart Game");
//        restartBtn.setOnAction(e -> controller.restart());
//
//        Button undoBtn = new Button("Undo");
//        undoBtn.setOnAction(e -> controller.undo());
//
//        CheckBox themeToggle = new CheckBox("Dark Mode");
//        themeToggle.setOnAction(e -> controller.toggleTheme(themeToggle.isSelected()));
//
//        HBox bottomBox = new HBox(15, restartBtn, undoBtn, themeToggle);
//        bottomBox.setAlignment(Pos.CENTER);
//        bottomBox.setPadding(new Insets(10));
//        root.setBottom(bottomBox);
//
//        Scene scene = new Scene(root, 500, 600);
//        // Load CSS (Make sure style.css is in src/com/othello/)
//        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
//
//        primaryStage.setTitle("Othello - JavaFX Academic Project");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}




package com.othello.reversigame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showHomeScreen();
    }

    // --- SCREEN 1: HOME ---
    public void showHomeScreen() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("home-bg");

        // Title
        Label title = new Label("OTHELLO GAME");
        title.getStyleClass().add("title-label");

        // Button 1: Player vs Player
        Button pvpBtn = new Button("Player vs Player");
        pvpBtn.setGraphic(createIcon(false)); // Human Icon
        pvpBtn.getStyleClass().add("menu-button");
        pvpBtn.setOnAction(e -> startGame(false));

        // Button 2: Player vs AI
        Button aiBtn = new Button("Player vs Computer");
        aiBtn.setGraphic(createIcon(true)); // Computer Icon
        aiBtn.getStyleClass().add("menu-button");
        aiBtn.setOnAction(e -> startGame(true));

        root.getChildren().addAll(title, pvpBtn, aiBtn);

        Scene homeScene = new Scene(root, 600, 700);
        homeScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Othello - Main Menu");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    // --- SCREEN 2: GAME ---
    public void startGame(boolean vsAI) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // 1. Top Bar (Player Cards)
        VBox p1Card = createPlayerCard("Player (Black)", false);
        VBox p2Card = createPlayerCard(vsAI ? "Computer (White)" : "Player (White)", vsAI);

        HBox topBar = new HBox(40, p1Card, p2Card);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 15, 0));
        root.setTop(topBar);

        // 2. Center Grid
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setStyle("-fx-background-color: #005500; -fx-padding: 8; -fx-background-radius: 8;");
        root.setCenter(gridPane);

        // Labels inside cards to update score
        Label p1Score = (Label) p1Card.getChildren().get(2);
        Label p2Score = (Label) p2Card.getChildren().get(2);

        // 3. Controller
        GameController controller = new GameController(gridPane, p1Card, p2Card, p1Score, p2Score, vsAI);

        // 4. Bottom Controls
        Button backBtn = new Button("Main Menu");
        backBtn.getStyleClass().add("control-button");
        backBtn.setOnAction(e -> showHomeScreen());

        Button restartBtn = new Button("Restart");
        restartBtn.getStyleClass().add("control-button");
        restartBtn.setOnAction(e -> controller.restart());

        Button undoBtn = new Button("Undo");
        undoBtn.getStyleClass().add("control-button");
        undoBtn.setOnAction(e -> controller.undo());

        CheckBox themeToggle = new CheckBox("Dark Mode");
        themeToggle.setOnAction(e -> controller.toggleTheme(themeToggle.isSelected()));

        HBox bottomBox = new HBox(15, backBtn, restartBtn, undoBtn, themeToggle);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 0, 0));
        root.setBottom(bottomBox);

        Scene gameScene = new Scene(root, 650, 750);
        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle(vsAI ? "Othello: PvE" : "Othello: PvP");
        primaryStage.setScene(gameScene);
    }

    // --- HELPER: Draw Icons via Code (No images needed) ---
    private VBox createPlayerCard(String name, boolean isComputer) {
        VBox card = new VBox(5);
        card.getStyleClass().add("player-card");

        // Icon
        Shape icon = isComputer ? createComputerIcon() : createUserIcon();

        Label nameLbl = new Label(name);
        nameLbl.getStyleClass().add("player-name");

        Label scoreLbl = new Label("2");
        scoreLbl.getStyleClass().add("score-text");

        card.getChildren().addAll(icon, nameLbl, scoreLbl);
        return card;
    }

    private Node createIcon(boolean isComputer) {
        Group g = new Group();
        Shape s = isComputer ? createComputerIcon() : createUserIcon();
        // Scale down for buttons
        s.setScaleX(0.6);
        s.setScaleY(0.6);
        g.getChildren().add(s);
        return g;
    }

    private Shape createUserIcon() {
        Circle head = new Circle(0, -10, 8);
        Arc body = new Arc(0, 10, 15, 12, 0, 180);
        body.setType(ArcType.CHORD);
        body.setStroke(Color.BLACK);
        body.setFill(Color.DARKGRAY);
        head.setStroke(Color.BLACK);
        head.setFill(Color.DARKGRAY);
        return Shape.union(head, body);
    }

    private Shape createComputerIcon() {
        Rectangle screen = new Rectangle(-15, -15, 30, 20);
        screen.setArcWidth(5); screen.setArcHeight(5);
        Rectangle stand = new Rectangle(-5, 5, 10, 8);
        Rectangle base = new Rectangle(-10, 13, 20, 3);
        Shape union = Shape.union(screen, stand);
        union = Shape.union(union, base);
        union.setFill(Color.rgb(50, 50, 50));
        return union;
    }

    public static void main(String[] args) {
        launch(args);
    }
}