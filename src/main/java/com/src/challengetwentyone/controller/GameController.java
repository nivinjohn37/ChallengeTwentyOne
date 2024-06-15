package com.src.challengetwentyone.controller;

import com.src.challengetwentyone.model.GameRequest;
import com.src.challengetwentyone.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/join")
    public void join(GameRequest request) {
        gameService.joinGame(request.getPlayer());
    }

    @MessageMapping("/generate")
    public void generateNumber(GameRequest request) {
        gameService.generateNumber(request.getPlayer());
    }

    @MessageMapping("/lock")
    public void lockScore(GameRequest request) {
        gameService.lockScore(request.getPlayer());
    }
}
