package com.lamtev.poker.core.states;

import com.lamtev.poker.core.hands.PokerHand;
import com.lamtev.poker.core.hands.PokerHandFactory;
import com.lamtev.poker.core.model.Card;
import com.lamtev.poker.core.model.Player;
import com.lamtev.poker.core.states.exceptions.ForbiddenMoveException;
import com.lamtev.poker.core.states.exceptions.UnallowableMoveException;

import java.util.*;

class ShowdownState extends ActionState {

    private final Map<Player, PokerHand> showedDownPlayers = new HashMap<>();
    private final PokerHandFactory handFactory;

    ShowdownState(ActionState state, Player latestAggressor) {
        super(state);
        handFactory = new PokerHandFactory(communityCards());
        if (latestAggressor == null) {
            players().nextActiveAfterDealer();
        } else {
            players().setLatestAggressor(latestAggressor);
        }
        String currentPlayerId = players().current().id();
        poker().notifyCurrentPlayerChangedListeners(currentPlayerId);
        moveAbility().setAllInIsAble(false);
        moveAbility().setCallIsAble(false);
        moveAbility().setCheckIsAble(false);
        moveAbility().setRaiseIsAble(false);
        moveAbility().setFoldIsAble(false);
        moveAbility().setShowdownIsAble(true);
        poker().notifyMoveAbilityListeners(currentPlayerId, moveAbility());
    }

    @Override
    public void placeBlindWagers() throws ForbiddenMoveException {
        throw new ForbiddenMoveException("Placing the blind wagers", toString());
    }

    @Override
    public void call() throws ForbiddenMoveException {
        throw new ForbiddenMoveException("Call", toString());
    }

    @Override
    public void raise(int additionalWager) throws ForbiddenMoveException {
        throw new ForbiddenMoveException("Raise", toString());
    }

    @Override
    public void allIn() throws ForbiddenMoveException {
        throw new ForbiddenMoveException("All in", toString());
    }

    @Override
    public void fold() throws UnallowableMoveException {
        if (currentPlayerCantFold()) {
            throw new UnallowableMoveException("Fold");
        }
        Player currentPlayer = players().current();
        currentPlayer.fold();
        poker().notifyPlayerFoldListeners(currentPlayer.id());
        changePlayerIndex();
        attemptDetermineWinners();
    }

    private boolean currentPlayerCantFold() {
        return showedDownPlayers.isEmpty() || players().current().isAllinner();
    }

    @Override
    public void check() throws ForbiddenMoveException {
        throw new ForbiddenMoveException("Check", toString());
    }

    @Override
    public void showDown() {
        Player currentPlayer = players().current();
        PokerHand pokerHand = handFactory.createCombination(currentPlayer.cards());
        showedDownPlayers.put(currentPlayer, pokerHand);
        List<Card> holeCards = new ArrayList<>();
        currentPlayer.cards().forEach(holeCards::add);
        poker().notifyPlayerShowedDownListeners(currentPlayer.id(), holeCards, pokerHand);
        changePlayerIndex();
        attemptDetermineWinners();
    }

    @Override
    void changePlayerIndex() {
        players().nextActive();
        String currentPlayerId = players().current().id();
        poker().notifyCurrentPlayerChangedListeners(currentPlayerId);
        moveAbility().setFoldIsAble(!currentPlayerCantFold());
        poker().notifyMoveAbilityListeners(currentPlayerId, moveAbility());
    }

    //TODO     add feature for action: not showDown and not fold
    //TODO     When it would be added, if only one action player then state = ShowDown
    //TODO     and player will have 2 variants: do this action or showDown

    private void attemptDetermineWinners() {
        if (timeToDetermineWinners()) {

            Set<Player> winners = bank().giveMoneyToWinners(showedDownPlayers);

            //TODO think about rename PlayerMoneyUpdatedListener
            winners.forEach(winner -> poker().notifyPlayerMoneyUpdatedListeners(
                    winner.id(),
                    winner.stack(),
                    winner.wager()
            ));
            poker().setState(new RoundOfPlayIsOverState(this));
        }
    }

    private boolean timeToDetermineWinners() {
        return showedDownPlayers.size() == players().activePlayersNumber();
    }

}
