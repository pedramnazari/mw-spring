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

    public Game startGame(int rows, int columns, int mineCount) {
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
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                game.setCell(row, column, new Cell(row, column));
            }
        }
    }

    void placeMines(final Game game, int mineCount) {
        final Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int row = random.nextInt(game.getRows());
            int column = random.nextInt(game.getColumns());
            if (!game.getCell(row, column).isMine()) {
                game.getCell(row, column).setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines(final Game game) {
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                if (!game.getCell(row, column).isMine()) {
                    int adjacentMines = countAdjacentMines(game, row, column);
                    game.getCell(row, column).setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    private int countAdjacentMines(final Game game, int row, int column) {
        int count = 0;

        for (int[] direction : DIRECTIONS) {
            int nr = row + direction[1];
            int nc = column + direction[0];
            if (game.isWithinBounds(nr, nc) && game.getCell(nr, nc).isMine()) {
                count++;
            }
        }
        return count;
    }


    public Game revealCell(int row, int column) {
        if (game.isGameOver()
                || !game.isWithinBounds(row, column)
                || game.getCell(row, column).isRevealed()
                || game.getCell(row, column).isFlagged()) {
            return game;
        }

        Cell cell = game.getCell(row, column);
        cell.setRevealed(true);

        if (cell.isMine()) {
            handleMineRevealed();
        } else if (allNonMineCellsRevealed()) {
            handleWin();
        } else if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(row, column);
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

    private void revealAdjacentCells(int row, int column) {
        for (int[] direction : DIRECTIONS) {
            int nr = row + direction[1];
            int nc = column + direction[0];
            revealCell(nr, nc);
        }
    }

    public Game toggleFlag(int row, int column) {
        if (game.isWithinBounds(row, column) && !game.getCell(row, column).isRevealed()) {
            game.getCell(row, column).setFlagged(!game.getCell(row, column).isFlagged());
        }

        return game;
    }

    private boolean allNonMineCellsRevealed() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int column = 0; column < game.getColumns(); column++) {
                if (!game.getCell(row, column).isMine() && !game.getCell(row, column).isRevealed()) {
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
