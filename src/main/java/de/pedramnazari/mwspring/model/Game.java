package de.pedramnazari.mwspring.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private final Cell[][] board;
    private boolean gameOver;
    private boolean gameWon;

    public Game(int rows, int columns) {
        this.board = new Cell[rows][columns];
    }

    public void setCell(int row, int column, Cell cell) {
        this.board[row][column] = cell;
    }

    public Cell getCell(int row, int column) {
        if (!isWithinBounds(row, column)) {
            return null;
        }

        return board[row][column];
    }

    public int getColumns() {
        return board[0].length;
    }

    public int getRows() {
        return board.length;
    }

    public boolean isWithinBounds(int row, int column) {
        return ((row >= 0) && (row < getRows()) && (column >= 0) && (column < getColumns()));
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

    public List<Cell> getMines() {
        final List<Cell> mineCells = new ArrayList<>();

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.isMine()) {
                    mineCells.add(cell);
                }
            }
        }

        return mineCells;
    }
}


