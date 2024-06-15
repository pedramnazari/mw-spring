package de.pedramnazari.mwspring.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerAndGameServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;


    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testGetGame() throws Exception {
        mockMvc.perform(get("/api/game/start")
                        .param("rows", "2")
                        .param("columns", "3")
                        .param("mines", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.gameOver").value(false))
                .andExpect(jsonPath("$.gameWon").value(false))
                .andExpect(jsonPath("$.rows").value(2))
                .andExpect(jsonPath("$.columns").value(3));

        final Game game = gameService.getGame();

        assertFalse(game.isGameWon());
        assertFalse(game.isGameOver());
        assertEquals(2, game.getRows());
        assertEquals(3, game.getColumns());


        assertFalse(game.getCell(0, 1).isRevealed());

        mockMvc.perform(get("/api/game/reveal")
                        .param("row", "0")
                        .param("column", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(game.getCell(1, 0).isRevealed());
    }

}


