package com.example.gameStore.dtos.GameDtos;

import com.example.gameStore.enums.Genre;
import com.example.gameStore.enums.PlayerSupport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRequestDto {
    private String name;
    private List<Genre> genreList;
    private String thumbnail;
    private List<String> images;
    private String developer;
    private Date releaseDate;
    private String systemRequirements;
    private List<PlayerSupport> playerSupport;
    private float price;
    private String description;
}
