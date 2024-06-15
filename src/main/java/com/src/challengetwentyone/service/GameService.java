package com.src.challengetwentyone.service;

import com.src.challengetwentyone.model.GameResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private final Map<String, Boolean> playerLocked = new ConcurrentHashMap<>();
    private final Map<String, Boolean> playerJoined = new ConcurrentHashMap<>();

    public GameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void joinGame(String player) {
        playerJoined.put(player, true);
        playerScores.put(player, 0);
        playerLocked.put(player, false);
        messagingTemplate.convertAndSend("/topic/status", "Player " + player + " joined");
    }

    public void generateNumber(String player) {
        if (!playerJoined.containsKey(player)) {
            messagingTemplate.convertAndSendToUser(player, "/topic/numbers", new GameResponse(player, -1, -1, false));
            return;
        }

        int randomNumber = ThreadLocalRandom.current().nextInt(1, 11);
        int currentScore = playerScores.getOrDefault(player, 0) + randomNumber;
        playerScores.put(player, currentScore);

        GameResponse response = new GameResponse(player, randomNumber, currentScore, playerLocked.get(player));
        messagingTemplate.convertAndSend("/topic/numbers", response);
    }


    public void lockScore(String player) {
        if (!playerJoined.containsKey(player)) {
            return;
        }

        playerLocked.put(player, true);
        messagingTemplate.convertAndSend("/topic/scores", getPlayerScores());
    }


    public Map<String, Integer> getPlayerScores() {
        return playerScores;
    }

    public void resetGame() {
        playerScores.clear();
        playerLocked.clear();
        playerJoined.clear();
    }
}

