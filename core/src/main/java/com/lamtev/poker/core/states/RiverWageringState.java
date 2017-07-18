package com.lamtev.poker.core.states;

import com.lamtev.poker.core.model.Card;

import java.util.ArrayList;
import java.util.List;

class RiverWageringState extends WageringState {

    RiverWageringState(ActionState state) {
        super(state);
        dealer().makeRiver();
        List<Card> addedCards = new ArrayList<>();
        addedCards.add(communityCards().cardAt(5));
        poker().notifyCommunityCardsChangedListeners(addedCards);
    }

    @Override
    protected void attemptNextState() {
        if (timeToNextState()) {
            poker().setState(new ShowdownState(this, latestAggressor()));
        }
    }

    @Override
    void determineUnderTheGunPosition() {
        players().nextNonAllinnerAfterDealer();
    }

}