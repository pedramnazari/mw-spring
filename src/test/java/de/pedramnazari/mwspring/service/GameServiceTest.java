package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Cell;
import de.pedramnazari.mwspring.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
    }

    @Test
    public void testStartGame() {
        final int rows = 9;
        final int columns = 10;
        final int mines = 25;

        Game game = gameService.startGame(rows, columns, mines);

        assertNotNull(game);
        assertEquals(rows, game.getRows());
        assertEquals(columns, game.getColumns());
        assertEquals(mines, game.getMineCount());

        // Test that board is fully filled with cells
        // that are neither revealed nor flagged
        final Cell[][] board = game.getBoard();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Cell cell = board[i][j];
                assertNotNull(cell);
                assertFalse(cell.isFlagged());
                assertFalse(cell.isRevealed());
            }
        }
    }


    @Test
    public void testToggleFlag() {
        final int rows = 9;
        final int columns = 10;
        final int mines = 25;

        final Game game = gameService.startGame(rows, columns, mines);
        assertNotNull(game);

        final Cell cell = game.getCell(5, 5);

        assertFalse(cell.isFlagged());

        gameService.toggleFlag(5, 5);
        assertTrue(cell.isFlagged());

        gameService.toggleFlag(5, 5);
        assertFalse(cell.isFlagged());
    }





}
