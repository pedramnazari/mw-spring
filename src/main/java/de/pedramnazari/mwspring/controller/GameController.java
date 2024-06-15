package de.pedramnazari.mwspring.controller;

import de.pedramnazari.mwspring.model.Game;
import de.pedramnazari.mwspring.model.Cell;
import de.pedramnazari.mwspring.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/game")
public class GameController {

   final Logger logger = Logger.getLogger(GameController.class.getName());

    @Autowired
    private GameService gameService;

    @GetMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam int columns, @RequestParam int rows, @RequestParam int mines) {
        logger.log(Level.INFO, "Start the Game: " + rows + " x " + columns + " mines: " + mines);
        Game game = gameService.startGame(rows, columns, mines);
        printBoard(game.getBoard());
        return ResponseEntity.ok(game);
    }

    public static void printBoard(Cell[][] cells) {
        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                printCell(value);
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

    @GetMapping("/reveal")
    public ResponseEntity<Game> revealCell(@RequestParam int column, @RequestParam int row) {
        return ResponseEntity.ok(gameService.revealCell(row, column));
    }

    @PostMapping("/flag")
    public Game toggleFlag(@RequestParam int column, @RequestParam int row) {
        return gameService.toggleFlag(row, column);
    }
}
