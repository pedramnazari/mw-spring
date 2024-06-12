package de.pedramnazari.mwspring.model;

import java.util.Arrays;

public class Game {
    private final Cell[][] board;
    private boolean gameOver;
    private boolean gameWon;

    public Game(int rows, int columns) {
        this.board = new Cell[columns][rows];
    }

    public void setCell(int x, int y, Cell cell) {
        this.board[x][y] = cell;
    }

    public Cell getCell(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return null;
        }

        return board[x][y];
    }

    public int getColumns() {
        return board.length;
    }

    public int getRows() {
        return board[0].length;
    }

    public int getMineCount() {
        return countMines();
    }

    public int countMines() {
        int mineCount = 0;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y].isMine()) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    private boolean isWithinBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < getColumns()) && (y < getRows()));
    }

    public Cell[][] getBoard() {
        return Arrays.copyOf(board, board.length);
    }

    public void setGameOver(boolean b) {
        this.gameOver = b;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void setGameWon(boolean b) {
        this.gameWon = b;
    }

    public boolean isGameWon() {
        return this.gameWon;
    }
}
