package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Board;
import de.pedramnazari.mwspring.model.Cell;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {
    private static final int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};

    private Board board;

    public static void printBoard(Cell[][] cells) {
        for (int y = 0; y < cells[0].length; y++) {
            for (int x = 0; x < cells.length; x++) {
                printCell(cells[x][y]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void printCell(Cell cell) {
        if (cell.isMine()) {
                System.out.print("M");
        } else {
            System.out.print(cell.isFlagged() ? "F" : ".");
        }
    }

    public Board startGame(Board board, int mineCount) {
        this.board = board;

        initializeBoard();
        placeMines(mineCount);
        calculateAdjacentMines();

        return board;
    }

    void initializeBoard() {
        for (int x = 0; x < board.getWidth(); x++) {
            initializeRow(x);
        }
    }

    void initializeRow(int x) {
        for (int y = 0; y < board.getHeight(); y++) {
            board.setCell(x, y, new Cell());
        }
    }

    void placeMines(int mineCount) {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int x = random.nextInt(board.getWidth());
            int y = random.nextInt(board.getHeight());
            if (!board.getCell(x,y).isMine()) {
                board.getCell(x,y).setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (!board.getCell(x, y).isMine()) {
                    int adjacentMines = countAdjacentMines(x, y);
                    board.getCell(x, y).setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = 0; i < DX.length; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            if (isWithinBounds(nx, ny) && board.getCell(nx, ny).isMine()) {
                count++;
            }
        }
        return count;
    }

    private boolean isWithinBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < board.getWidth()) && (y < board.getHeight()));
    }

    public Board revealCell(int x, int y) {
        if (!isWithinBounds(x, y) || board.getCell(x, y).isRevealed() || board.getCell(x, y).isFlagged()) {
            return board;
        }

        Cell cell = board.getCell(x, y);
        cell.setRevealed(true);

        if (cell.isMine()) {
            handleMineRevealed();
        } else if (allNonMineCellsRevealed()) {
            handleWin();
        } else if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(x, y);
        }
        return board;
    }

     private void handleMineRevealed() {
        board.setGameOver(true);
    }

    private void handleWin() {
        board.setGameWon(true);
    }

    private void revealAdjacentCells(int x, int y) {
        for (int i = 0; i < DX.length; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            revealCell(nx, ny);
        }
    }

    public Board toggleFlag(int x, int y) {
        if (isWithinBounds(x, y) && !board.getCell(x, y).isRevealed()) {
            board.getCell(x, y).setFlagged(!board.getCell(x, y).isFlagged());
        }

        return board;
    }

    boolean allNonMineCellsRevealed() {
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (!board.getCell(x, y).isMine() && !board.getCell(x, y).isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    void placeMine(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return;
        }
        board.getCell(x, y).setMine(true);
    }

    public int countMines() {
        return board.countMines();
    }

}
