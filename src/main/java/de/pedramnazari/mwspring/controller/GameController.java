package de.pedramnazari.mwspring.controller;

import de.pedramnazari.mwspring.model.Board;
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

    Logger logger = Logger.getLogger(GameController.class.getName());

    @Autowired
    private GameService gameService;

    @GetMapping("/start")
    public ResponseEntity<Board> startGame(@RequestParam int rows, @RequestParam int columns, @RequestParam int mines) {
        Board board = new Board(rows, columns);
        logger.log(Level.INFO, "Gut");
        board = gameService.startGame(board, mines);
        printBoard(board.getCells());
        return ResponseEntity.ok(board);
    }

    public static void printBoard(Cell[][] cells) {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                printCell(cells[x][y]);
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
    public ResponseEntity<Board> revealCell(@RequestParam int row, @RequestParam int column) {
        return ResponseEntity.ok(gameService.revealCell(row, column));
    }

    @PostMapping("/flag")
    public Board toggleCell(@RequestParam int row, @RequestParam int column) {
        return gameService.toggleFlag(row, column);
    }
}
