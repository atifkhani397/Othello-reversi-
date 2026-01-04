package com.othello.reversigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer {
    private Piece myPiece;

    public ComputerPlayer(Piece piece) {
        this.myPiece = piece;
    }

    public int[] getBestMove(Board board) {
        List<int[]> validMoves = new ArrayList<>();
        // 1. Gather all valid moves
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (board.isValidMove(i, j, myPiece)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }

        if (validMoves.isEmpty()) return null;

        // 2. Simple Heuristic: Prefer corners, avoid positions adjacent to corners
        int[] bestMove = validMoves.get(0);
        int maxScore = Integer.MIN_VALUE;

        for (int[] move : validMoves) {
            int score = evaluateMove(move);
            if (score > maxScore) {
                maxScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluateMove(int[] move) {
        int r = move[0];
        int c = move[1];
        int score = 0;

        // Corner is best
        if ((r == 0 || r == 7) && (c == 0 || c == 7)) score += 100;

            // Edges are good
        else if (r == 0 || r == 7 || c == 0 || c == 7) score += 10;

        // Inner Logic (Random variation to make it feel human-like)
        score += new Random().nextInt(10);

        return score;
    }
}