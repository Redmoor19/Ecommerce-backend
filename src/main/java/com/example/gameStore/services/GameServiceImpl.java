package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import com.example.gameStore.services.interfaces.GameService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
//    private final GameRepository gameRepository;

    @Override
    public GameDto getAcademicPlan(String id) {
        return new GameDto(UUID.randomUUID(), "Adventure Quest", List.of(Genre.ADVENTURE), 85,
                "http://example.com/thumb1.jpg", List.of("http://example.com/image1.jpg"),
                "Quest Devs", new Date(), "4GB RAM, 2GB GPU", List.of(PlayerSupport.SINGLE_PLAYER),
                19.99f, "An epic adventure game", "SKU12345", true, 8);
    }

}
