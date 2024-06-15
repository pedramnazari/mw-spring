package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.model.Cell;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GameService {

    // TODO: Remove dependency on logger to maintain clean architecture
    final Logger logger = Logger.getLogger(GameService.class.getName());

    // Movements in all 8 directions
    private static final int[][] DIRECTIONS = {
            {-1, -1},   { 0, -1},   { 1, -1},
            {-1, 0},                { 1, 0},
            {-1, 1},    { 0, 1},    { 1, 1}
    };

    private Game game;

    public Game startGame(int columns, int rows, int mineCount) {
        // TODO: add checks

        // TODO: use factory method pattern and dependency injection
        final Game g = new Game(rows, columns);

        // TODO: refactor code to delete field "game"
        this.game = g;

        initializeBoard(game);
        placeMines(game, mineCount);
        calculateAdjacentMines(game);

        return game;
    }

    void initializeBoard(final Game game) {
        for (int column = 0; column < game.getColumns(); column++) {
            for (int row = 0; row < game.getRows(); row++) {
                game.setCell(row, column, new Cell(row, column));
            }
        }
    }

    void placeMines(final Game game, int mineCount) {
        final Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int x = random.nextInt(game.getColumns());
            int y = random.nextInt(game.getRows());
            if (!game.getCell(y, x).isMine()) {
                game.getCell(y, x).setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines(final Game game) {
        for (int x = 0; x < game.getColumns(); x++) {
            for (int y = 0; y < game.getRows(); y++) {
                if (!game.getCell(y, x).isMine()) {
                    int adjacentMines = countAdjacentMines(game, x, y);
                    game.getCell(y, x).setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    private int countAdjacentMines(final Game game, int x, int y) {
        int count = 0;

        for (int[] direction : DIRECTIONS) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            if (game.isWithinBounds(nx, ny) && game.getCell(ny, nx).isMine()) {
                count++;
            }
        }
        return count;
    }


    public Game revealCell(int x, int y) {
        if (game.isGameOver()
                || !game.isWithinBounds(x, y)
                || game.getCell(y, x).isRevealed()
                || game.getCell(y, x).isFlagged()) {
            return game;
        }

        Cell cell = game.getCell(y, x);
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
        game.setGameWon(false);
         logger.log(Level.INFO, "Game Over. Game Lost!");
    }

    private void handleWin() {
        game.setGameOver(true);
        game.setGameWon(true);
        logger.log(Level.INFO, "Game Over. Game Won!");
    }

    private void revealAdjacentCells(int x, int y) {
        for (int[] direction : DIRECTIONS) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            revealCell(nx, ny);
        }
    }

    public Game toggleFlag(int x, int y) {
        if (game.isWithinBounds(x, y) && !game.getCell(y, x).isRevealed()) {
            game.getCell(y, x).setFlagged(!game.getCell(y, x).isFlagged());
        }

        return game;
    }

    boolean allNonMineCellsRevealed() {
        for (int x = 0; x < game.getColumns(); x++) {
            for (int y = 0; y < game.getRows(); y++) {
                if (!game.getCell(y, x).isMine() && !game.getCell(y, x).isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Game getGame() {
        return game;
    }
}
