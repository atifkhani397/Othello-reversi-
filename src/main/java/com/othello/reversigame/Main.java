package com.othello.reversigame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private Stage primaryStage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.UNDECORATED);
        showHomeScreen();
    }

    public void showHomeScreen() {
        VBox root = new VBox();
        root.setId("root-pane");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(Insets.EMPTY);

        HBox titleBar = createTitleBar("Main Menu");

        VBox content = new VBox(25);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        VBox.setVgrow(content, Priority.ALWAYS);

        Label title = new Label("OTHELLO");
        title.setStyle("-fx-font-family: 'Segoe UI Black'; -fx-font-size: 48px; -fx-text-fill: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 5, 0, 0, 5);");

        Button pvpBtn = new Button("Player vs Player");
        pvpBtn.setGraphic(createIcon(false));
        pvpBtn.getStyleClass().add("capsule-button");
        pvpBtn.setMinWidth(220);
        pvpBtn.setOnAction(e -> startGame(false));

        Button aiBtn = new Button("Player vs Computer");
        aiBtn.setGraphic(createIcon(true));
        aiBtn.getStyleClass().add("capsule-button");
        aiBtn.setMinWidth(220);
        aiBtn.setOnAction(e -> startGame(true));

        Button exitBtn = new Button("Exit Game");
        exitBtn.getStyleClass().add("capsule-button");
        exitBtn.setMinWidth(220);
        exitBtn.setOnAction(e -> primaryStage.close());

        content.getChildren().addAll(title, pvpBtn, aiBtn, exitBtn);
        root.getChildren().addAll(titleBar, content);

        Scene homeScene = new Scene(root, 480, 700);
        homeScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Othello - Main Menu");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    public void startGame(boolean vsAI) {
        VBox root = new VBox();
        root.setId("root-pane");
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(Insets.EMPTY);

        HBox titleBar = createTitleBar("Othello Match");

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));
        VBox.setVgrow(content, Priority.ALWAYS);

        String p2Name = vsAI ? "Computer (White)" : "Player (White)";

        HBox p1Card = createPlayerCard("Player (Black)", "2", true);
        HBox p2Card = createPlayerCard(p2Name, "2", false);

        HBox scoreboard = new HBox(20, p1Card, p2Card);
        scoreboard.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        StackPane boardFrame = new StackPane(gridPane);
        boardFrame.getStyleClass().add("board-frame");

        Button btnMenu = new Button("Main Menu");
        btnMenu.getStyleClass().add("capsule-button");
        btnMenu.setMinWidth(100);

        Button btnRestart = new Button("Restart");
        btnRestart.getStyleClass().add("capsule-button");
        btnRestart.setMinWidth(100);

        Button btnUndo = new Button("Undo");
        btnUndo.getStyleClass().add("capsule-button");
        btnUndo.setMinWidth(100);

        HBox controls = new HBox(10, btnMenu, btnRestart, btnUndo);
        controls.setAlignment(Pos.CENTER);

        content.getChildren().addAll(scoreboard, boardFrame, controls);
        root.getChildren().addAll(titleBar, content);

        Label p1ScoreLabel = (Label)((VBox)p1Card.getChildren().get(0)).getChildren().get(1);
        Label p2ScoreLabel = (Label)((VBox)p2Card.getChildren().get(0)).getChildren().get(1);

        GameController controller = new GameController(gridPane, p1Card, p2Card,
                p1ScoreLabel,
                p2ScoreLabel,
                vsAI);

        btnRestart.setOnAction(e -> controller.restart());
        btnUndo.setOnAction(e -> controller.undo());
        btnMenu.setOnAction(e -> showHomeScreen());

        Scene scene = new Scene(root, 480, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTitleBar(String titleText) {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_RIGHT);
        bar.setMinHeight(40);
        bar.setPadding(new Insets(0, 0, 0, 15));
        bar.setStyle("-fx-background-color: rgba(0,0,0,0.4);");

        Label lblTitle = new Label(titleText);
        lblTitle.setStyle("-fx-text-fill: #dddddd; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnMin = new Button();
        Line minShape = new Line(0, 0, 10, 0);
        minShape.setStroke(Color.WHITE);
        minShape.setStrokeWidth(2);
        btnMin.setGraphic(minShape);
        styleTitleButton(btnMin, false);
        btnMin.setOnAction(e -> primaryStage.setIconified(true));

        Button btnMax = new Button();
        Rectangle maxShape = new Rectangle(10, 10);
        maxShape.setFill(Color.TRANSPARENT);
        maxShape.setStroke(Color.WHITE);
        maxShape.setStrokeWidth(2);
        btnMax.setGraphic(maxShape);
        styleTitleButton(btnMax, false);
        btnMax.setOnAction(e -> primaryStage.setMaximized(!primaryStage.isMaximized()));

        Button btnClose = new Button();
        Group closeShape = new Group();
        Line l1 = new Line(0, 0, 10, 10); l1.setStroke(Color.WHITE); l1.setStrokeWidth(2);
        Line l2 = new Line(10, 0, 0, 10); l2.setStroke(Color.WHITE); l2.setStrokeWidth(2);
        closeShape.getChildren().addAll(l1, l2);
        btnClose.setGraphic(closeShape);
        styleTitleButton(btnClose, true);
        btnClose.setOnAction(e -> primaryStage.close());

        bar.getChildren().addAll(lblTitle, spacer, btnMin, btnMax, btnClose);

        bar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        bar.setOnMouseDragged(event -> {
            if (!primaryStage.isMaximized()) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        return bar;
    }

    private void styleTitleButton(Button btn, boolean isClose) {
        btn.setMinWidth(45);
        btn.setMinHeight(40);

        String baseStyle = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-background-radius: 0;";
        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> {
            if (isClose) {
                btn.setStyle("-fx-background-color: #E81123; -fx-background-radius: 0;");
            } else {
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 0;");
            }
        });

        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private HBox createPlayerCard(String name, String score, boolean isActive) {
        HBox card = new HBox(10);
        card.getStyleClass().add("player-card");
        if(isActive) card.getStyleClass().add("active-card");
        card.setMinWidth(140);

        VBox textBox = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label scoreLbl = new Label(score);
        scoreLbl.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold; -fx-font-size: 20px;");

        textBox.getChildren().addAll(nameLbl, scoreLbl);
        card.getChildren().add(textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        return card;
    }

    private Node createIcon(boolean isComputer) {
        Group g = new Group();
        Shape s = isComputer ? createComputerIcon() : createUserIcon();
        s.setScaleX(0.5);
        s.setScaleY(0.5);
        s.setFill(Color.WHITE);
        g.getChildren().add(s);
        return g;
    }

    private Shape createUserIcon() {
        Circle head = new Circle(0, -10, 8);
        Arc body = new Arc(0, 10, 15, 12, 0, 180);
        body.setType(ArcType.CHORD);
        return Shape.union(head, body);
    }

    private Shape createComputerIcon() {
        Rectangle screen = new Rectangle(-15, -15, 30, 20);
        screen.setArcWidth(5); screen.setArcHeight(5);
        Rectangle stand = new Rectangle(-5, 5, 10, 8);
        Rectangle base = new Rectangle(-10, 13, 20, 3);
        Shape union = Shape.union(screen, stand);
        union = Shape.union(union, base);
        return union;
    }

    public static void main(String[] args) {
        launch(args);
    }
}