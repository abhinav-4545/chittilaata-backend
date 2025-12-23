package com.chittilaata.service;

import com.chittilaata.model.GameState;
import com.chittilaata.model.Player;
import com.chittilaata.util.GameLogic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    public final Map<String, GameState> games = new ConcurrentHashMap<>();

    public GameState createGame(int totalRounds){
        String gameId = UUID.randomUUID().toString();

        GameState gameState = new GameState();
        gameState.setGameId(gameId);
        gameState.setPlayers(new ArrayList<>());
        gameState.setCurrentRound(1);
        gameState.setTotalRounds(totalRounds);
        gameState.setCurrentPhase("SETUP");
        gameState.setCurrentTurnRole(null);
        games.put(gameId, gameState);
        return gameState;

    }
    public GameState getGame(String gameId) {
        return games.get(gameId);
    }
    public GameState addPlayer(String gameId, Player player) {
        GameState gameState = games.get(gameId);

        // 1️⃣ invalid gameId
        if (gameState == null) {
            throw new IllegalStateException("Invalid gameId");
        }

        // 2️⃣ cannot join after game started
        if (!"SETUP".equals(gameState.getCurrentPhase())) {
            throw new IllegalStateException("Game already started");
        }

        // 3️⃣ max players = 6
        if (gameState.getPlayers().size() >= 6) {
            throw new IllegalStateException("Maximum 6 players allowed");
        }

        // 4️⃣ duplicate player ID
        for (Player p : gameState.getPlayers()) {
            if (p.getId() == player.getId()) {
                throw new IllegalStateException("Player ID already exists");
            }
        }

        gameState.getPlayers().add(player);
        return gameState;
    }

    public GameState startGame(String gameId) {
        GameState gameState = games.get(gameId);

        if (gameState == null) {
            throw new IllegalStateException("Invalid gameId");
        }

        // minimum 3 players
        if (gameState.getPlayers().size() < 3) {
            throw new IllegalStateException("At least 3 players required to start");
        }

        GameLogic.assignRoles(gameState);
        return gameState;
    }

    public GameState makeGuess(String gameId, int guesserId, int guessedId) {
        GameState gameState = games.get(gameId);

        // 1️⃣ invalid gameId
        if (gameState == null) {
            throw new IllegalStateException("Invalid gameId");
        }

        // 2️⃣ game already ended
        if ("GAME_END".equals(gameState.getCurrentPhase())) {
            throw new IllegalStateException("Game already ended");
        }

        // 3️⃣ guessing self
        if (guesserId == guessedId) {
            throw new IllegalStateException("Player cannot guess himself");
        }

        Player guesser = null;
        Player guessed = null;

        for (Player p : gameState.getPlayers()) {
            if (p.getId() == guesserId) guesser = p;
            if (p.getId() == guessedId) guessed = p;
        }

        // 4️⃣ invalid player ids
        if (guesser == null || guessed == null) {
            throw new IllegalStateException("Invalid player id");
        }

        // 5️⃣ wrong player trying to guess
        if (!guesser.getRole().getCurRole().equals(gameState.getCurrentTurnRole())) {
            throw new IllegalStateException("Not this player's turn");
        }

        // 6️⃣ guessing revealed player
        if (guessed.isRevealed()) {
            throw new IllegalStateException("Cannot guess already revealed player");
        }

        GameLogic.handleGuess(gameState, guesserId, guessedId);
        return gameState;
    }


}
