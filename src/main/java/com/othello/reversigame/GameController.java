


package com.othello.reversigame;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class GameController {
    private GameModel model;
    private GridPane gridPane;
    private Pane p1Card, p2Card;
    private Label p1Score, p2Score;
    private CellView[][] cells;
    private boolean vsAI;

    public GameController(GridPane grid, Pane p1, Pane p2, Label s1, Label s2, boolean vsAI) {
        this.gridPane = grid;
        this.p1Card = p1;
        this.p2Card = p2;
        this.p1Score = s1;
        this.p2Score = s2;
        this.vsAI = vsAI;

        this.model = new GameModel(vsAI);
        this.cells = new CellView[8][8];

        initializeGrid();
        updateView();
    }

    private void initializeGrid() {
        gridPane.getChildren().clear();

        String[] cols = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 0; i < 8; i++) {
            Label lbl = new Label(cols[i]);
            lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            lbl.setMinSize(50, 25);
            lbl.setAlignment(Pos.CENTER);
            gridPane.add(lbl, i + 1, 0);
        }

        for (int i = 0; i < 8; i++) {
            Label lbl = new Label(String.valueOf(i + 1));
            lbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
            lbl.setMinSize(25, 50);
            lbl.setAlignment(Pos.CENTER);
            gridPane.add(lbl, 0, i + 1);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellView cell = new CellView(i, j);
                final int r = i;
                final int c = j;
                cell.setOnMouseClicked(e -> handleHumanMove(r, c));
                gridPane.add(cell, j + 1, i + 1);
                cells[i][j] = cell;
            }
        }
    }

    private void handleHumanMove(int row, int col) {
        if (model.isGameOver() || model.isAiTurn()) return;

        if (model.playMove(row, col)) {
            updateView();

            if (checkGameOver()) return;
            checkTurnSkipped();

            if (model.isAiTurn()) {
                PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
                pause.setOnFinished(e -> performAiMove());
                pause.play();
            }
        }
    }

    private void performAiMove() {
        if (model.isGameOver()) return;

        model.playAiMove();
        updateView();

        if (checkGameOver()) return;
        checkTurnSkipped();

        if (model.isAiTurn() && !model.isGameOver()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
            pause.setOnFinished(e -> performAiMove());
            pause.play();
        }
    }

    private boolean checkGameOver() {
        Board b = model.getBoard();
        int black = b.getCount(Piece.BLACK);
        int white = b.getCount(Piece.WHITE);

        if (model.isGameOver() || black == 0 || white == 0 || (black + white == 64)) {
            showGameOverDialog();
            return true;
        }
        return false;
    }

    private void checkTurnSkipped() {
        if (model.wasTurnSkipped() && !model.isGameOver()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("turn_passed.fxml"));
                Parent root = loader.load();

                Label messageLabel = (Label) root.lookup("#messageLabel");
                Button okButton = (Button) root.lookup("#okButton");

                String nextPlayer = model.getCurrentPlayer() == Piece.BLACK ? "BLACK" : "WHITE";
                messageLabel.setText("No valid moves available. Turn passes to " + nextPlayer + ".");

                Stage dialogStage = new Stage();
                dialogStage.initStyle(StageStyle.TRANSPARENT);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initOwner(gridPane.getScene().getWindow());

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                dialogStage.setScene(scene);

                okButton.setOnAction(e -> dialogStage.close());

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }

            model.resetSkippedFlag();
        }
    }

    private void updateView() {
        Board board = model.getBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j].update(board.getPiece(i, j));
                cells[i][j].setHighlight(false);
            }
        }

        if (!model.isAiTurn() && !model.isGameOver()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board.isValidMove(i, j, model.getCurrentPlayer())) {
                        cells[i][j].setHighlight(true);
                    }
                }
            }
        }

        p1Score.setText(String.valueOf(board.getCount(Piece.BLACK)));
        p2Score.setText(String.valueOf(board.getCount(Piece.WHITE)));

        p1Card.getStyleClass().remove("active-card");
        p2Card.getStyleClass().remove("active-card");

        if (!model.isGameOver()) {
            if (model.getCurrentPlayer() == Piece.BLACK) {
                p1Card.getStyleClass().add("active-card");
            } else {
                p2Card.getStyleClass().add("active-card");
            }
        }
    }

    private void showGameOverDialog() {
        Board b = model.getBoard();
        int blackScore = b.getCount(Piece.BLACK);
        int whiteScore = b.getCount(Piece.WHITE);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game_result.fxml"));
            Parent root = loader.load();

            javafx.scene.text.Text resultTitle = (javafx.scene.text.Text) root.lookup("#resultTitleText");
            Label subMessage = (Label) root.lookup("#subMessageLabel");
            Label sBlack = (Label) root.lookup("#scoreBlack");
            Label sWhite = (Label) root.lookup("#scoreWhite");
            Button btnPlay = (Button) root.lookup("#btnNewGame");
            Button btnClose = (Button) root.lookup("#btnMenu");

            sBlack.setText(String.valueOf(blackScore));
            sWhite.setText(String.valueOf(whiteScore));

            if (blackScore > whiteScore) {
                resultTitle.setText("YOU WIN!");
                subMessage.setText("Black dominates the board!");
            } else if (whiteScore > blackScore) {
                resultTitle.setText("YOU LOST!");
                subMessage.setText("Better luck next time.");
            } else {
                resultTitle.setText("DRAW!");
                subMessage.setText("Perfectly balanced.");
            }

            if (blackScore == 0) {
                resultTitle.setText("YOU LOST!");
                subMessage.setText("Black was wiped out!");
            }
            if (whiteScore == 0) {
                resultTitle.setText("YOU WIN!");
                subMessage.setText("White was wiped out!");
            }

            Stage dialogStage = new Stage();
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(gridPane.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            dialogStage.setScene(scene);

            btnPlay.setOnAction(e -> {
                dialogStage.close();
                restart();
            });

            btnClose.setOnAction(e -> dialogStage.close());

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        model.restart();
        updateView();
    }

    public void undo() {
        model.undo();
        updateView();
    }
}