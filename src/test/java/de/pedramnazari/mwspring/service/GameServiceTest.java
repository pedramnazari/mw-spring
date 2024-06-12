package de.pedramnazari.mwspring.service;

import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.service.GameService;
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
    public void testStartNewGame() {
        final int WIDTH = 10;
        final int HEIGHT = 9;
        Game game = gameService.startGame(WIDTH, HEIGHT, 25);
        assertNotNull(game);
        assertEquals(WIDTH, game.getWidth());
        assertEquals(HEIGHT, game.getHeight());
        assertEquals(25, game.getMineCount());
    }

}
