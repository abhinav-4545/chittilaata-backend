package com.chittilaata.controller;

import com.chittilaata.model.GameState;
import com.chittilaata.model.Player;
import com.chittilaata.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    // 1️⃣ Create a new game
    @PostMapping("/create")
    public GameState createGame(@RequestParam int totalRounds) {
        return gameService.createGame(totalRounds);
    }
    // 2️⃣ Get current game state
    @GetMapping("/{gameId}")
    public GameState getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId);
    }
    // 3️⃣ Join an existing game
    @PostMapping("/{gameId}/join")
    public GameState joinGame(
            @PathVariable String gameId,
            @RequestBody Player player
    ) {
        return gameService.addPlayer(gameId, player);
    }
    @PostMapping("/{gameId}/start")
    public GameState startGame(@PathVariable String gameId) {
        return gameService.startGame(gameId);
    }
    @PostMapping("/{gameId}/guess")
    public GameState makeGuess(
            @PathVariable String gameId,
            @RequestParam int guesserId,
            @RequestParam int guessedId
    ) {
        return gameService.makeGuess(gameId, guesserId, guessedId);
    }
}
