package com.example.gameStore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {
    private int id;
    private String name;
    private String key;
}
