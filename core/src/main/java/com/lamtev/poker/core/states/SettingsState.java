package com.lamtev.poker.core.states;

import com.lamtev.poker.core.api.MoveAbility;
import com.lamtev.poker.core.api.PlayerIdStack;
import com.lamtev.poker.core.api.Poker;
import com.lamtev.poker.core.model.*;
import com.lamtev.poker.core.states.exceptions.GameHaveNotBeenStartedException;

import java.util.ArrayList;
import java.util.List;

public class SettingsState extends AbstractPokerState {

    private final Poker poker;

    private final List<PlayerIdStack> playerIdStackList;
    private final String dealerId;
    private final int smallBlindWager;

    public SettingsState(Poker poker, List<PlayerIdStack> playerIdStackList, String dealerId, int smallBlindWager) {
        this.poker = poker;
        this.playerIdStackList = playerIdStackList;
        this.dealerId = dealerId;
        this.smallBlindWager = smallBlindWager;
    }

    @Override
    public void start() {
        List<Player> playerList = new ArrayList<>();
        playerIdStackList.forEach(playerIdStack -> {
            String id = playerIdStack.id();
            int stack = playerIdStack.stack();
            playerList.add(new Player(id, stack));
        });
        final Players players = new Players(playerList, dealerId);
        final Cards communityCards = new Cards();
        poker.setState(new BlindsState(
                poker,
                players,
                new Bank(players),
                new Dealer(players, communityCards),
                communityCards,
                new MoveAbility(),
                smallBlindWager
        ));
    }

    @Override
    public void call() throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

    @Override
    public void raise(int additionalWager) throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

    @Override
    public void allIn() throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

    @Override
    public void fold() throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

    @Override
    public void check() throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

    @Override
    public void showDown() throws GameHaveNotBeenStartedException {
        throw new GameHaveNotBeenStartedException();
    }

}