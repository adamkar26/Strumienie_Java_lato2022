package com.SKP;

import java.math.BigDecimal;

public class BoardGame {

    public String name;
    public double rating;
    public BigDecimal price;
    public int minPlayers;
    public int maxPlayers;

    public BoardGame(String name, double rating, BigDecimal price, int minPlayers, int maxPlayers) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }
}
