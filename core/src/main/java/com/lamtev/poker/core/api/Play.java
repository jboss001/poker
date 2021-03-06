package com.lamtev.poker.core.api;

import com.lamtev.poker.core.event_listeners.*;
import com.lamtev.poker.core.hands.PokerHand;
import com.lamtev.poker.core.model.Card;

import java.util.List;

public interface Play extends
        BankMoneyUpdatedListener,
        BlindWagersPlacedListener,
        CommunityCardsDealtListener,
        CurrentPlayerChangedListener,
        HoleCardsDealtListener,
        MoveAbilityListener,
        PlayerAllinnedListener,
        PlayerCalledListener,
        PlayerCheckedListener,
        PlayerFoldListener,
        PlayerMoneyUpdatedListener,
        PlayerRaisedListener,
        PlayerShowedDownListener,
        StateChangedListener {

    @Override
    @Deprecated
    default String id() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default void holeCardsDealt(List<Card> holeCards) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default void playerShowedDown(String playerId, List<Card> holeCards, PokerHand hand) {
        throw new UnsupportedOperationException();
    }

}
