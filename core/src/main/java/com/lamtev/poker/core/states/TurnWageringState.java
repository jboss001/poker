package com.lamtev.poker.core.states;

import com.lamtev.poker.core.model.Card;

import java.util.ArrayList;
import java.util.List;

class TurnWageringState extends WageringState {

    TurnWageringState(ActionState state) {
        super(state);
        dealer().makeTurn();
        List<Card> addedCards = new ArrayList<>();
        addedCards.add(communityCards().cardAt(4));
        poker().notifyCommunityCardsChangedListeners(addedCards);
    }

    @Override
    protected void attemptNextState() {
        if (timeToForcedShowdown()) {
            //TODO think about how to dispose of code duplicates
            dealer().makeRiver();
            List<Card> addedCards = new ArrayList<>();
            addedCards.add(communityCards().cardAt(5));
            poker().notifyCommunityCardsChangedListeners(addedCards);
            poker().setState(new ShowdownState(this, latestAggressor()));
        } else if (timeToNextState()) {
            poker().setState(new RiverWageringState(this));
        }
    }

    @Override
    void determineUnderTheGunPosition() {
        players().nextNonAllinnerAfterDealer();
    }

}