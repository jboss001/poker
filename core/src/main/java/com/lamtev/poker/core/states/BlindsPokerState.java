package com.lamtev.poker.core.states;

import com.lamtev.poker.core.api.PlayerMoney;
import com.lamtev.poker.core.api.Poker;
import com.lamtev.poker.core.model.*;
import com.lamtev.poker.core.states.exceptions.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

class BlindsPokerState extends ActionPokerState {

    private final int smallBlindSize;

    BlindsPokerState(Poker poker, Players players, Bank bank, Dealer dealer, Cards communityCards, int smallBlindSize) {
        super(poker, players, bank, dealer, communityCards);
        this.smallBlindSize = smallBlindSize;
    }

    @Override
    public void placeBlindWagers() {
        bank().acceptBlindWagers(smallBlindSize);
        notifyWagerPlacedListeners(players().smallBlind(), players().bigBlind());
        nextState();
    }

    private void notifyWagerPlacedListeners(Player smallBlind, Player bigBlind) {
        String smallBlindId = smallBlind.id();
        PlayerMoney smallBLindMoney = new PlayerMoney(smallBlind.stack(), smallBlind.wager());
        poker().notifyWagerPlacedListeners(smallBlindId, smallBLindMoney, bank().money());
        PlayerMoney bigBlindMoney = new PlayerMoney(bigBlind.stack(), bigBlind.wager());
        poker().notifyWagerPlacedListeners(bigBlind.id(), bigBlindMoney, bank().money());
    }

    private void nextState() {
        if (timeToForcedShowdown()) {
            dealer().makePreflop();
            poker().notifyPreflopMadeListeners(new LinkedHashMap<String, Cards>() {{
                players().forEach(player -> put(player.id(), player.cards()));
            }});
            dealer().makeFlop();
            dealer().makeTurn();
            dealer().makeRiver();
            poker().notifyCommunityCardsChangedListeners(new ArrayList<Card>() {{
                communityCards().forEach(this::add);
            }});
            Player latestAggressor = players().bigBlind().isAllinner() ?
                    players().bigBlind() : players().smallBlind();
            poker().setState(new ShowdownPokerState(this, latestAggressor));
        } else {
            poker().setState(new PreflopWageringPokerState(this));
        }
    }

    @Override
    public void call() throws
            GameHaveNotBeenStartedException,
            ForbiddenMoveException,
            UnallowableMoveException,
            IsNotEnoughMoneyException,
            GameOverException {
    }

    @Override
    public void raise(int additionalWager) throws
            GameHaveNotBeenStartedException,
            ForbiddenMoveException,
            UnallowableMoveException,
            IsNotEnoughMoneyException,
            NotPositiveWagerException,
            GameOverException {
    }

    @Override
    public void allIn() throws
            GameHaveNotBeenStartedException,
            ForbiddenMoveException,
            UnallowableMoveException,
            GameOverException,
            IsNotEnoughMoneyException,
            NotPositiveWagerException {
    }

    @Override
    public void fold() throws
            UnallowableMoveException,
            GameOverException,
            GameHaveNotBeenStartedException {
    }

    @Override
    public void check() throws
            GameHaveNotBeenStartedException,
            ForbiddenMoveException,
            UnallowableMoveException,
            GameOverException {
    }

    @Override
    public void showDown() throws
            GameHaveNotBeenStartedException,
            ForbiddenMoveException,
            GameOverException {
    }
}