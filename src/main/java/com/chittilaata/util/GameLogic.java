package com.chittilaata.util;

import com.chittilaata.model.GameState;
import com.chittilaata.model.Player;
import com.chittilaata.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GameLogic {
    //  LOCKED ROLE ORDER
    private static final List<Role> ROLE_ORDER = List.of(
            new Role("RAJU", 1000, "RANI"),
            new Role("RANI", 800, "MANTHRI"),
            new Role("MANTHRI", 600, "SENAPATHI"),
            new Role("SENAPATHI", 400, "POLICE"),
            new Role("POLICE", 200, "DONGA"),
            new Role("DONGA", 0, null)
    );
    public static void assignRoles(GameState gameState) {
        int playerCount = gameState.getPlayers().size();

        // pick last N roles based on players
        List<Role> selectedRoles =
                new ArrayList<>(ROLE_ORDER.subList(ROLE_ORDER.size() - playerCount, ROLE_ORDER.size()));

        Collections.shuffle(selectedRoles);

        for (int i = 0; i < playerCount; i++) {
            Player p = gameState.getPlayers().get(i);
            p.setRole(selectedRoles.get(i));
            p.setRevealed(false);
        }

        //  first turn = highest points (excluding DONGA)
        Player firstGuesser = null;

        for (Player p : gameState.getPlayers()) {
            if ("DONGA".equals(p.getRole().getCurRole())) {
                continue; // DONGA never guesses
            }

            if (firstGuesser == null ||
                    p.getRole().getPoints() > firstGuesser.getRole().getPoints()) {
                firstGuesser = p;
            }
        }

        if (firstGuesser != null) {
            gameState.setCurrentTurnRole(firstGuesser.getRole().getCurRole());
        }

        gameState.setCurrentPhase("IN_PROGRESS");
    }

    public static void handleGuess(GameState gameState, int guesserId , int guessedId){
        Player guesser = getPlayerById(gameState, guesserId);
        Player guessed = getPlayerById(gameState, guessedId);

        if (guesser == null || guessed == null) return;

        Role guesserRole = guesser.getRole();
        String expectedRole = guesserRole.getNextRole();
        // CORRECT GUESS
        if (guessed.getRole().getCurRole().equals(expectedRole)) {

            // award points
            guesser.setScore(guesser.getScore() + guesserRole.getPoints());
            guesser.setRevealed(true);

            // move to next role
            gameState.setCurrentTurnRole(expectedRole);

            // check if DONGA found → round ends
            if ("DONGA".equals(expectedRole)) {
                endRound(gameState);
            }

        }
        else {
            // swap roles
            Role temp = guesser.getRole();
            guesser.setRole(guessed.getRole());
            guessed.setRole(temp);
        }

    }
    private static void endRound(GameState gameState) {
        gameState.setCurrentRound(gameState.getCurrentRound() + 1);

        if (gameState.getCurrentRound() > gameState.getTotalRounds()) {
            gameState.setCurrentPhase("GAME_END");
        } else {
            assignRoles(gameState);
        }
    }
    private static Player getPlayerById(GameState gameState, int id) {
        for (Player p : gameState.getPlayers()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

}
