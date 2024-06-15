package com.src.challengetwentyone.model;

public class GameResponse {
    private String player;
    private int randomNumber;
    private int score;
    private boolean locked;

    public GameResponse(String player, int randomNumber, int score, boolean locked) {
        this.player = player;
        this.randomNumber = randomNumber;
        this.score = score;
        this.locked = locked;
    }

    public String getPlayer() {
        return player;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public int getScore() {
        return score;
    }

    public boolean isLocked() {
        return locked;
    }
}
