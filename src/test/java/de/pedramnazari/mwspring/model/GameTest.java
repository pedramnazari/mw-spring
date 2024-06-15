package de.pedramnazari.mwspring.model;

import de.pedramnazari.mwspring.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private static final int ROWS = 10;
    private static final int COLUMNS = 8;
    private static final int MINES = 5;

    private GameService gameService;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
    }


    @Test
    public void testIsWithinBounds() {
        final Game game = gameService.startGame(COLUMNS, ROWS, MINES);
        assertNotNull(game);

        // within bounds
        assertTrue(game.isWithinBounds(0, 0));
        assertTrue(game.isWithinBounds(0, 1));
        assertTrue(game.isWithinBounds(1, 0));
        assertTrue(game.isWithinBounds(game.getColumns()-1, 0));
        assertTrue(game.isWithinBounds(0, game.getRows()-1));
        assertTrue(game.isWithinBounds(game.getColumns()-1, game.getRows()-1));

        // outside bounds
        assertFalse(game.isWithinBounds(-1, 0));
        assertFalse(game.isWithinBounds(0, -1));
        assertFalse(game.isWithinBounds(game.getColumns(), 0));
        assertFalse(game.isWithinBounds(0, game.getRows()));
    }

    @Test
    public void testGetMineCount() {
        final Game game = gameService.startGame(COLUMNS, ROWS, MINES);
        assertNotNull(game);

        assertEquals(5, game.getMines().size());
    }

    @Test
    public void testGetMines() {
        final Game game = gameService.startGame(COLUMNS, ROWS, MINES);
        assertNotNull(game);

        List<Cell> mineCells = game.getMines();

        assertEquals(MINES, mineCells.size());

        for(Cell mineCell : mineCells) {
            assertTrue(mineCell.isMine());
        }
    }

}
