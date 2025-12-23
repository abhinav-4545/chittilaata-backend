package com.chittilaata.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class GameState {
    private String gameId;
    private List<Player> players;
    private int currentRound;
    private int totalRounds;
    private String currentPhase;
    private String currentTurnRole;
}
