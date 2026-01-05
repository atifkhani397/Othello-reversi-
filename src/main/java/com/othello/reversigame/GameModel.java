//package com.othello.reversigame;
//
//import java.util.Stack;
//
//public class GameModel {
//    private Board board;
//    private Piece currentPlayer;
//    private ComputerPlayer ai;
//    private boolean isAiEnabled;
//    private Stack<Board> history; // For Undo
//
//    public GameModel() {
//        this.board = new Board();
//        this.history = new Stack<>();
//        this.currentPlayer = Piece.BLACK; // Black moves first
//        this.isAiEnabled = true;
//        this.ai = new ComputerPlayer(Piece.WHITE);
//    }
//
//    public void restart() {
//        board.resetBoard();
//        history.clear();
//        currentPlayer = Piece.BLACK;
//    }
//
//    public boolean playMove(int row, int col) {
//        if (board.isValidMove(row, col, currentPlayer)) {
//            saveState(); // Save for undo
//            board.makeMove(row, col, currentPlayer);
//            switchTurn();
//            return true;
//        }
//        return false;
//    }
//
//    public void playAiMove() {
//        if (!isAiEnabled || currentPlayer != Piece.WHITE) return;
//
//        int[] move = ai.getBestMove(board);
//        if (move != null) {
//            playMove(move[0], move[1]);
//        } else {
//            // AI passes
//            switchTurn();
//        }
//    }
//
//    private void switchTurn() {
//        currentPlayer = currentPlayer.opposite();
//
//        // If next player has no moves, switch back or end game
//        if (!board.hasValidMoves(currentPlayer)) {
//            currentPlayer = currentPlayer.opposite();
//            if (!board.hasValidMoves(currentPlayer)) {
//                // Game Over handled by controller checking status
//            }
//        }
//    }
//
//    public void undo() {
//        if (!history.isEmpty()) {
//            // Undo twice if playing against AI (to go back to human turn)
//            board = history.pop();
//            if(isAiEnabled && !history.isEmpty()) {
//                board = history.pop();
//            }
//            currentPlayer = Piece.BLACK; // Assuming Human is always Black for simplicity
//        }
//    }
//
//    private void saveState() {
//        history.push(new Board(board));
//    }
//
//    public Board getBoard() { return board; }
//    public Piece getCurrentPlayer() { return currentPlayer; }
//    public boolean isGameOver() {
//        return !board.hasValidMoves(Piece.BLACK) && !board.hasValidMoves(Piece.WHITE);
//    }
//    public void setAiEnabled(boolean enable) { this.isAiEnabled = enable; }
//    public boolean isAiTurn() { return isAiEnabled && currentPlayer == Piece.WHITE; }
//}


package com.othello.reversigame;

import java.util.Stack;

public class GameModel {
    private Board board;
    private Piece currentPlayer;
    private ComputerPlayer ai;
    private boolean isAiGame; // New Flag: Is this PvAI or PvP?
    private Stack<Board> history;
    private boolean lastTurnSkipped; // To track if a turn was passed

    // Updated Constructor
    public GameModel(boolean isAiGame) {
        this.board = new Board();
        this.history = new Stack<>();
        this.currentPlayer = Piece.BLACK;
        this.isAiGame = isAiGame; // Set based on user choice
        this.ai = new ComputerPlayer(Piece.WHITE);
        this.lastTurnSkipped = false;
    }

    public void restart() {
        board.resetBoard();
        history.clear();
        currentPlayer = Piece.BLACK;
        lastTurnSkipped = false;
    }

    public boolean playMove(int row, int col) {
        if (board.isValidMove(row, col, currentPlayer)) {
            saveState();
            board.makeMove(row, col, currentPlayer);
            lastTurnSkipped = false;
            switchTurn();
            return true;
        }
        return false;
    }

    public void playAiMove() {
        if (!isAiGame || currentPlayer != Piece.WHITE) return;

        int[] move = ai.getBestMove(board);
        if (move != null) {
            playMove(move[0], move[1]);
        } else {
            // AI must pass
            switchTurn();
        }
    }

    // Handles the "Pass Turn" logic
    private void switchTurn() {
        Piece nextPlayer = currentPlayer.opposite();

        // Check if next player has moves
        if (board.hasValidMoves(nextPlayer)) {
            currentPlayer = nextPlayer;
            lastTurnSkipped = false;
        } else {
            // Next player has no moves!
            lastTurnSkipped = true;
            // Check if ORIGINAL player has moves (if not, Game Over)
            if (!board.hasValidMoves(currentPlayer)) {
                // Both stuck = Game Over (Handled by isGameOver check)
            } else {
                // Original player goes again
                // We keep currentPlayer as is, but we flag 'lastTurnSkipped'
                // so the Controller can alert the user.
            }
        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            board = history.pop();
            // In PvAI, we usually undo 2 steps (AI + Human)
            if (isAiGame && !history.isEmpty()) {
                board = history.pop();
            } else if (!isAiGame) {
                // In PvP, just undo 1 step
                currentPlayer = currentPlayer.opposite();
            }
        }
    }

    private void saveState() {
        history.push(new Board(board));
    }

    public Board getBoard() { return board; }
    public Piece getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() {
        return !board.hasValidMoves(Piece.BLACK) && !board.hasValidMoves(Piece.WHITE);
    }
    public boolean isAiTurn() { return isAiGame && currentPlayer == Piece.WHITE; }
    public boolean wasTurnSkipped() { return lastTurnSkipped; }
    public void resetSkippedFlag() { lastTurnSkipped = false; }
}