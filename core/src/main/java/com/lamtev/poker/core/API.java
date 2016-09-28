package com.lamtev.poker.core;

public interface API {
    void setUpNumberOfPlayers(int numberOfPlayers);
    void setUpSmallBlind(int smallBlind);
    void setUpBigBlind(int bigBlind);
    void placeAWager(int wager);
    void raise(int additionalWager);
    void check();
}
