package com.othello.reversigame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CellView extends StackPane {
    private int row, col;
    private Circle disc;
    private Circle highlight;

    public CellView(int row, int col) {
        this.row = row;
        this.col = col;

        Rectangle bg = new Rectangle(50, 50);
        bg.getStyleClass().add("cell-bg");

        disc = new Circle(20);
        disc.setVisible(false);

        highlight = new Circle(8, Color.rgb(100, 255, 100, 0.5));
        highlight.setVisible(false);
        highlight.setMouseTransparent(true);

        getChildren().addAll(bg, highlight, disc);
    }

    public void update(Piece piece) {
        if (piece == Piece.EMPTY) {
            disc.setVisible(false);
        } else {
            disc.setVisible(true);
            disc.getStyleClass().clear();
            disc.getStyleClass().add(piece == Piece.BLACK ? "piece-black" : "piece-white");
        }
    }

    public void setHighlight(boolean active) {
        highlight.setVisible(active);
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}