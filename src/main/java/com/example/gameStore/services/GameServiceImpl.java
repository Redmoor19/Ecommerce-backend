package com.example.gameStore.services;

import com.example.gameStore.dtos.GameDto;
import com.example.gameStore.services.interfaces.GameService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
//    private final GameRepository gameRepository;

    @Override
    public GameDto getAcademicPlan(String id) {
        return new GameDto(1, "name", "key");
    }

}
