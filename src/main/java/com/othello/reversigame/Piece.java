package com.othello.reversigame;

public enum Piece {
    BLACK, WHITE, EMPTY;

    public Piece opposite() {
        if (this == BLACK) return WHITE;
        if (this == WHITE) return BLACK;
        return EMPTY;
    }
}