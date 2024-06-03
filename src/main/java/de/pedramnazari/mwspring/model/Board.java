package de.pedramnazari.mwspring.model;

import java.util.Arrays;

public class Board {
    private final Cell[][] cells;
    private final int width;
    private final int height;
    private boolean gameOver;
    private boolean gameWon;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
    }

    public void setCell(int x, int y, Cell cell) {
        this.cells[x][y] = cell;
    }

    public Cell getCell(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return null;
        }

        return cells[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineCount() {
        return countMines();
    }

    public int countMines() {
        int mineCount = 0;
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y].isMine()) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    private boolean isWithinBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < width) && (y < height));
    }

    public Cell[][] getCells() {
        return Arrays.copyOf(cells, cells.length);
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
