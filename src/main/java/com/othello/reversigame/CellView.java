package com.othello.reversigame;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CellView extends StackPane {
    private Circle disc;
    private Circle highlight;
    private Piece currentPiece = Piece.EMPTY;

    public CellView(int row, int col) {
        // 1. The Background Cell (Green)
        Rectangle bg = new Rectangle(50, 50);
        bg.getStyleClass().add("cell-bg");

        // 2. The Highlight (Possible Move) - Fixed to be a Ring
        highlight = new Circle(12); // Smaller radius
        highlight.setFill(Color.TRANSPARENT); // Important: Make inside transparent
        highlight.setStroke(Color.rgb(0, 0, 0, 0.3)); // Faint dark ring
        highlight.setStrokeWidth(2);
        highlight.getStyleClass().add("valid-move");
        highlight.setVisible(false);

        // 3. The Disc (Game Piece)
        disc = new Circle(20); // Larger radius
        disc.setVisible(false);

        getChildren().addAll(bg, highlight, disc);
    }

    public void update(Piece newPiece) {
        if (newPiece == currentPiece) return;

        if (currentPiece == Piece.EMPTY && newPiece != Piece.EMPTY) {
            // Placing a new piece (No animation, just appear)
            currentPiece = newPiece;
            disc.setVisible(true);
            setDiscStyle(newPiece);
        }
        else if (newPiece == Piece.EMPTY) {
            // Clearing a piece
            currentPiece = Piece.EMPTY;
            disc.setVisible(false);
        }
        else {
            // Flipping a piece (Play Animation)
            playFlipAnimation(newPiece);
        }
    }

    private void playFlipAnimation(Piece targetPiece) {
        currentPiece = targetPiece;

        // Step 1: Shrink width to 0 (Disappear)
        ScaleTransition shrink = new ScaleTransition(Duration.millis(150), disc);
        shrink.setFromX(1.0);
        shrink.setToX(0.0);

        // When shrunk, switch color
        shrink.setOnFinished(e -> setDiscStyle(targetPiece));

        // Step 2: Expand width back to 1 (Reappear as new color)
        ScaleTransition expand = new ScaleTransition(Duration.millis(150), disc);
        expand.setFromX(0.0);
        expand.setToX(1.0);

        // Run them in order
        SequentialTransition flip = new SequentialTransition(shrink, expand);
        flip.play();
    }

    private void setDiscStyle(Piece p) {
        disc.getStyleClass().clear();
        disc.getStyleClass().add(p == Piece.BLACK ? "piece-black" : "piece-white");
    }

    public void setHighlight(boolean on) {
        highlight.setVisible(on);
    }
}