package com.lamtev.poker.core.states;

import com.lamtev.poker.core.model.Card;

import java.util.ArrayList;
import java.util.List;

final class FlopWageringState extends WageringState {

    FlopWageringState(ActionState state) {
        super(state);
    }

    @Override
    void makeDealerJob() {
        dealer.makeFlop();
        List<Card> addedCards = new ArrayList<>();
        for (Card communityCard : communityCards) {
            addedCards.add(communityCard);
        }
        poker.notifyCommunityCardsDealtListeners(addedCards);
    }

    @Override
    boolean attemptNextState() {
        if (timeToForcedShowdown()) {
            dealer.makeTurn();
            dealer.makeRiver();
            List<Card> addedCards = new ArrayList<>();
            addedCards.add(communityCards.cardAt(4));
            addedCards.add(communityCards.cardAt(5));
            poker.notifyCommunityCardsDealtListeners(addedCards);
            if (latestAggressor() == null) {
                players.nextActiveAfterDealer();
            } else {
                players.setLatestAggressor(latestAggressor());
            }
            poker.setState(new ShowdownState(this, latestAggressor()));
            return true;
        } else if (timeToNextState()) {
            poker.setState(new TurnWageringState(this));
            return true;
        }
        return false;
    }

}
