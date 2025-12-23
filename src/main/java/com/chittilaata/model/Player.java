package com.chittilaata.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Player {
    private int id;
    private String name;
    private Role role;
    private int score;
    private boolean isRevealed;
}
