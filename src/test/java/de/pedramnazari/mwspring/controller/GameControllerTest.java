package de.pedramnazari.mwspring.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.pedramnazari.mwspring.model.Cell;
import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(10, 10);

        for (int x = 0; x < game.getColumns(); x++) {
            for (int y = 0; y < game.getRows(); y++) {
                game.setCell(x, y, new Cell(x, y));
            }
        }
    }

    @Test
    void testStartGame() throws Exception {
        when(gameService.startGame(anyInt(), anyInt(), anyInt())).thenReturn(game);

        final MvcResult result = mockMvc.perform(get("/api/game/start")
                        .param("columns", "10")
                        .param("rows", "10")
                        .param("mines", "15")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(gameService).startGame(10, 10, 15);
    }

    @Test
    void testRevealCell() throws Exception {
        when(gameService.revealCell(anyInt(), anyInt())).thenReturn(game);

        final MvcResult result = mockMvc.perform(get("/api/game/reveal")
                        .param("column", "1")
                        .param("row", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(gameService).revealCell(1, 0);
    }

    @Test
    void testToggleFlag() throws Exception {
        when(gameService.toggleFlag(anyInt(), anyInt())).thenReturn(game);

        final MvcResult result = mockMvc.perform(post("/api/game/flag")
                        .param("column", "0")
                        .param("row", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(gameService).toggleFlag(0, 1);
    }
}

