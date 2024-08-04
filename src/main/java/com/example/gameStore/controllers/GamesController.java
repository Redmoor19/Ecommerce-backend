package com.example.gameStore.controllers;


import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.services.interfaces.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class GamesController {

    private final GameService gameService;

    private List<GameDto> games = new ArrayList<>();

    @GetMapping("game/{id}")
    public GameDto findGameById(@PathVariable(required = true, name = "id") String id) {
        return gameService.getAcademicPlan(id);
    }

    @GetMapping("all-games")
    public List<GameDto> findAll() {
        return games;
    }

    @PostMapping("create-game")
    public GameDto createGame(@RequestBody GameDto game) {
        System.out.println("==========================" + game + "==========================");
        games.add(game);
        return game;
    }

    @DeleteMapping("delete-game/{id}")
    public GameDto deleteGame(@PathVariable int id) {
        Optional<GameDto> deletingGame = games.stream().filter(game -> game.getId() == id).findFirst();
        if (deletingGame.isPresent()) {
            games.remove(deletingGame.get());
            return deletingGame.get();
        }
        return null;
    }
}
