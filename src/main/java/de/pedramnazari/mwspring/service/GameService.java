package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.model.Cell;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {

    // Movements in all 8 directions
    private static final int[][] DIRECTIONS = {
            {-1, -1},   { 0, -1},   { 1, -1},
            {-1, 0},                { 1, 0},
            {-1, 1},    { 0, 1},    { 1, 1}
    };

    private Game game;

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

    public Game startGame(Game game, int mineCount) {
        this.game = game;

        initializeBoard();
        placeMines(mineCount);
        calculateAdjacentMines();

        return game;
    }

    void initializeBoard() {
        for (int x = 0; x < game.getWidth(); x++) {
            initializeRow(x);
        }
    }

    void initializeRow(int x) {
        for (int y = 0; y < game.getHeight(); y++) {
            game.setCell(x, y, new Cell());
        }
    }

    void placeMines(int mineCount) {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int x = random.nextInt(game.getWidth());
            int y = random.nextInt(game.getHeight());
            if (!game.getCell(x,y).isMine()) {
                game.getCell(x,y).setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {
                if (!game.getCell(x, y).isMine()) {
                    int adjacentMines = countAdjacentMines(x, y);
                    game.getCell(x, y).setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;

        for (int[] direction : DIRECTIONS) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            if (isWithinBounds(nx, ny) && game.getCell(nx, ny).isMine()) {
                count++;
            }
        }
        return count;
    }

    private boolean isWithinBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < game.getWidth()) && (y < game.getHeight()));
    }

    public Game revealCell(int x, int y) {
        if (!isWithinBounds(x, y) || game.getCell(x, y).isRevealed() || game.getCell(x, y).isFlagged()) {
            return game;
        }

        Cell cell = game.getCell(x, y);
        cell.setRevealed(true);

        if (cell.isMine()) {
            handleMineRevealed();
        } else if (allNonMineCellsRevealed()) {
            handleWin();
        } else if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(x, y);
        }
        return game;
    }

     private void handleMineRevealed() {
        game.setGameOver(true);
    }

    private void handleWin() {
        game.setGameWon(true);
    }

    private void revealAdjacentCells(int x, int y) {
        for (int direction[] : DIRECTIONS) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            revealCell(nx, ny);
        }
    }

    public Game toggleFlag(int x, int y) {
        if (isWithinBounds(x, y) && !game.getCell(x, y).isRevealed()) {
            game.getCell(x, y).setFlagged(!game.getCell(x, y).isFlagged());
        }

        return game;
    }

    boolean allNonMineCellsRevealed() {
        for (int x = 0; x < game.getWidth(); x++) {
            for (int y = 0; y < game.getHeight(); y++) {
                if (!game.getCell(x, y).isMine() && !game.getCell(x, y).isRevealed()) {
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
        game.getCell(x, y).setMine(true);

        // TODO adjacent mines of cell must be updated as well (see Cell.getAdjacentMines())
    }

    public int countMines() {
        return game.countMines();
    }

}
