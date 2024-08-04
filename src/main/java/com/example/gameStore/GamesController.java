package com.example.gameStore;


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class GamesController {

    private List<Game> games = new ArrayList<>();

    @GetMapping("all-games")
    public List<Game> findAll() {
        return games;
    }

    @PostMapping("create-game")
    public Game createGame(@RequestBody Game game) {
        System.out.println("==========================" + game + "==========================");
        games.add(game);
        return game;
    }

    @DeleteMapping("delete-game/{id}")
    public Game deleteGame(@PathVariable int id) {
        Optional<Game> deletingGame = games.stream().filter(game -> game.getId() == id).findFirst();
        if (deletingGame.isPresent()) {
            games.remove(deletingGame.get());
            return deletingGame.get();
        }
        return null;
    }
}
