package com.othello.reversigame;

public class Board {
    public static final int SIZE = 8;
    private Piece[][] grid;
    private int blackCount;
    private int whiteCount;

    public Board() {
        grid = new Piece[SIZE][SIZE];
        resetBoard();
    }

    // Copy Constructor for AI and Undo
    public Board(Board other) {
        grid = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(other.grid[i], 0, grid[i], 0, SIZE);
        }
        this.blackCount = other.blackCount;
        this.whiteCount = other.whiteCount;
    }

    public void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = Piece.EMPTY;
            }
        }
        // Standard Othello setup
        grid[3][3] = Piece.WHITE;
        grid[3][4] = Piece.BLACK;
        grid[4][3] = Piece.BLACK;
        grid[4][4] = Piece.WHITE;
        updateCounts();
    }

    public Piece getPiece(int row, int col) {
        if (isValidBounds(row, col)) return grid[row][col];
        return Piece.EMPTY;
    }

    public boolean isValidBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public boolean isValidMove(int row, int col, Piece player) {
        if (grid[row][col] != Piece.EMPTY) return false;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                if (checkDirection(row, col, dr, dc, player)) return true;
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dr, int dc, Piece player) {
        int r = row + dr;
        int c = col + dc;
        boolean foundOpponent = false;

        while (isValidBounds(r, c)) {
            if (grid[r][c] == player.opposite()) {
                foundOpponent = true;
            } else if (grid[r][c] == player) {
                return foundOpponent;
            } else {
                return false; // Found empty
            }
            r += dr;
            c += dc;
        }
        return false;
    }

    public void makeMove(int row, int col, Piece player) {
        grid[row][col] = player;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                if (checkDirection(row, col, dr, dc, player)) {
                    flipDirection(row, col, dr, dc, player);
                }
            }
        }
        updateCounts();
    }

    private void flipDirection(int row, int col, int dr, int dc, Piece player) {
        int r = row + dr;
        int c = col + dc;
        while (grid[r][c] == player.opposite()) {
            grid[r][c] = player;
            r += dr;
            c += dc;
        }
    }

    public boolean hasValidMoves(Piece player) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, player)) return true;
            }
        }
        return false;
    }

    private void updateCounts() {
        blackCount = 0;
        whiteCount = 0;
        for (Piece[] row : grid) {
            for (Piece p : row) {
                if (p == Piece.BLACK) blackCount++;
                else if (p == Piece.WHITE) whiteCount++;
            }
        }
    }

    public int getCount(Piece p) {
        return p == Piece.BLACK ? blackCount : whiteCount;
    }
}