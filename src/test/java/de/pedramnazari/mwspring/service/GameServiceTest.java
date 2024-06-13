package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Cell;
import de.pedramnazari.mwspring.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    public static final int ROWS = 9;
    public static final int COLUMNS = 10;
    public static final int MINES = 25;

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
        assertEquals(MINES, game.getMineCount());

        // Test that board is fully filled with cells
        // that are neither revealed nor flagged
        final Cell[][] board = game.getBoard();
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                Cell cell = board[i][j];
                assertNotNull(cell);
                assertFalse(cell.isFlagged());
                assertFalse(cell.isRevealed());
            }
        }
    }


    @Test
    public void testToggleFlag() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        final Cell cell = game.getCell(4, 5);

        assertFalse(cell.isFlagged());

        gameService.toggleFlag(4, 5);
        assertTrue(cell.isFlagged());

        gameService.toggleFlag(4, 5);
        assertFalse(cell.isFlagged());
    }

    @Test
    public void testRevealCell() {
        final Game game = gameService.startGame(ROWS, COLUMNS, MINES);
        assertNotNull(game);

        final Cell cell = game.getCell(4, 5);
        assertFalse(cell.isRevealed());

        gameService.revealCell(4, 5);
        assertTrue(cell.isRevealed());

        // Repeating the action does not have an impact
        gameService.revealCell(4, 5);
        assertTrue(cell.isRevealed());
    }





}
