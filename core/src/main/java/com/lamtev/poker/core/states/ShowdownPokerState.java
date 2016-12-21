package com.lamtev.poker.core.states;

import com.lamtev.poker.core.api.Poker;
import com.lamtev.poker.core.hands.PokerHand;
import com.lamtev.poker.core.hands.PokerHandFactory;

import java.util.*;

//TODO
class ShowdownPokerState extends ActionPokerState {

    private int showDowns = 0;
    private Map<String, PokerHand> map = new TreeMap<>();

    ShowdownPokerState(ActionPokerState state, int latestAggressorIndex) {
        super(state);
        playerIndex = latestAggressorIndex;
    }

    @Override
    public void call() throws Exception {
        throw new Exception();
    }

    @Override
    public void raise(int additionalWager) throws Exception {
        throw new Exception();
    }

    @Override
    public void allIn() throws Exception {
        throw new Exception();
    }

    @Override
    public void fold() throws Exception {
        if (showDowns == 0) {
            throw new Exception("Can't fold when nobody did showDown");
        }
        currentPlayer().fold();
        changePlayerIndex();
        if (timeToDetermineWinners()) {
            poker.setState(new GameIsOverPokerState(this));
        }
    }

    @Override
    public void check() throws Exception {
        throw new Exception();
    }

    //TODO think about actuality of wrappers for collections
    @Override
    public PokerHand.Name showDown() throws Exception {
        ++showDowns;
        PokerHandFactory phf = new PokerHandFactory(commonCards);
        PokerHand pokerHand = phf.createCombination(currentPlayer().getCards());
        map.put(currentPlayer().getId(), pokerHand);
        if (timeToDetermineWinners()) {
            PokerHand maxPokerHand = Collections.max(map.values());
            List<String> winners = new ArrayList<>();
            map.entrySet().stream()
                    .filter(e -> e.getValue().compareTo(maxPokerHand) == 0)
                    .forEach(e -> winners.add(e.getKey()));
            bank.giveMoneyToWinners(winners);

            poker.setState(new GameIsOverPokerState(this));
        }
        changePlayerIndex();
        return pokerHand.getName();
    }



    private boolean timeToDetermineWinners() {
        return showDowns == players.activePlayersNumber();
    }

}
