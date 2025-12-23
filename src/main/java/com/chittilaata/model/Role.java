package com.chittilaata.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {
    private String curRole;
    private int points;
    private String nextRole;
}
