package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Cell;
import de.pedramnazari.mwspring.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private static final int ROWS = 9;
    private static final int COLUMNS = 10;
    private static final int MINES = 25;

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
    }

    @Test
    public void testStartGame() {
        Game game = gameService.startGame(ROWS, COLUMNS, MINES);

        assertNotNull(game);
        assertEquals(ROWS, game.getRows());
        assertEquals(COLUMNS, game.getColumns());
        assertEquals(MINES, game.getMines().size());

        // Test that board is fully filled with cells
        // that are neither revealed nor flagged
        final Cell[][] board = game.getBoard();
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                Cell cell = board[i][j];
                assertNotNull(cell);
                assertEquals(i, cell.getColumn());
                assertEquals(j, cell.getRow());
                assertFalse(cell.isFlagged());
                assertFalse(cell.isRevealed());
            }
        }
    }


    @Test
    public void testToggleFlag() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        final Cell cell = game.getCell(5, 4);

        assertFalse(cell.isFlagged());

        gameService.toggleFlag(5, 4);
        assertTrue(cell.isFlagged());

        gameService.toggleFlag(5, 4);
        assertFalse(cell.isFlagged());
    }

    @Test
    public void testRevealCell() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        final Cell cell = game.getCell(5, 4);
        assertFalse(cell.isRevealed());

        gameService.revealCell(5, 4);
        assertTrue(cell.isRevealed());

        // Repeating the action does not have an impact
        gameService.revealCell(5, 4);
        assertTrue(cell.isRevealed());
    }

    @Test
    public void testGameLost() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());

        final List<Cell> mineCells = game.getMines();
        assertEquals(MINES, mineCells.size());

        Cell firstMineCell = mineCells.get(0);

        gameService.revealCell(firstMineCell.getRow(), firstMineCell.getColumn());
        assertTrue(game.isGameOver());
        assertFalse(game.isGameWon());
    }


    @Test
    public void testGameWon() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());

        for (Cell[] cells : game.getBoard()) {
            for (Cell cell : cells) {
                if (!cell.isMine()) {
                    gameService.revealCell(cell.getRow(), cell.getColumn());
                }
            }
        }

        assertTrue(game.isGameOver());
        assertTrue(game.isGameWon());
    }
}
