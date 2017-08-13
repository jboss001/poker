package com.lamtev.poker.core.states;

import com.lamtev.poker.core.model.MoveValidator;
import com.lamtev.poker.core.model.Player;
import com.lamtev.poker.core.states.exceptions.ForbiddenMoveException;
import com.lamtev.poker.core.states.exceptions.IsNotEnoughMoneyException;
import com.lamtev.poker.core.states.exceptions.NotPositiveWagerException;
import com.lamtev.poker.core.states.exceptions.UnallowableMoveException;

abstract class WageringState extends ActionState {

    private int raises;
    private Player latestAggressor;
    private final MoveValidator moveValidator;
    private int checks = 0;

    WageringState(ActionState state) {
        super(state);
        moveValidator = new MoveValidator(players, bank);
    }

    @Override
    public void start() {
        determineUnderTheGunPosition();
        moveAbility.setAllInIsAble(true);
        moveAbility.setFoldIsAble(true);
        updateMoveAbility();
        makeDealerJob();
        poker.notifyCurrentPlayerChangedListeners(players.current().id());
    }

    @Override
    public void call() throws UnallowableMoveException, IsNotEnoughMoneyException {
        Player currentPlayer = players.current();
        moveValidator.validateCall();
        bank.acceptCall(currentPlayer);
        notifyMoneyUpdatedListeners();
        poker.notifyPlayerCalledListeners(currentPlayer.id());
        boolean stateChanged = attemptNextState();
        if (!stateChanged) {
            changePlayerIndex();
        }
    }

    @Override
    public void raise(int additionalWager) throws UnallowableMoveException,
            IsNotEnoughMoneyException, NotPositiveWagerException {
        checks = 0;
        moveValidator.validateRaise(raises);
        Player currentPlayer = players.current();
        bank.acceptRaise(additionalWager, currentPlayer);
        ++raises;
        latestAggressor = currentPlayer;
        notifyMoneyUpdatedListeners();
        poker.notifyPlayerRaisedListeners(currentPlayer.id());
        changePlayerIndex();
    }

    @Override
    public void allIn() throws UnallowableMoveException,
            IsNotEnoughMoneyException, NotPositiveWagerException {
        Player currentPlayer = players.current();
        int additionalWager = currentPlayer.stack() -
                (bank.wager() - currentPlayer.wager());
        if (additionalWager == 0) {
            call();
        } else if (additionalWager > 0) {
            raise(additionalWager);
        } else {
            bank.acceptAllIn(players.current());
            notifyMoneyUpdatedListeners();
            poker.notifyPlayerAllinnedListeners(currentPlayer.id());
            boolean stateChanged = attemptNextState();
            if (!stateChanged) {
                changePlayerIndex();
            }
        }
    }

    @Override
    public void fold() throws UnallowableMoveException {
        Player currentPlayer = players.current();
        currentPlayer.fold();
        poker.notifyPlayerFoldListeners(currentPlayer.id());
        if (onlyOneActivePlayer()) {
            Player winner = players.nextActive();
            bank.giveMoneyToSingleWinner(winner);
            poker.notifyPlayerMoneyUpdatedListeners(
                    winner.id(),
                    winner.stack(), winner.wager()
            );
            poker.setState(new RoundOfPlayIsOverState());
            return;
        }
        boolean stateChanged = attemptNextState();
        if (!stateChanged) {
            changePlayerIndex();
        }
    }

    @Override
    public void check() throws ForbiddenMoveException, UnallowableMoveException {
        moveValidator.validateCheck(raises);
        poker.notifyPlayerCheckedListeners(players.current().id());
        ++checks;
        boolean stateChanged = attemptNextState();
        if (!stateChanged) {
            changePlayerIndex();
        }
    }

    @Override
    public void showDown() throws ForbiddenMoveException {
        throw new ForbiddenMoveException("Show down", toString());
    }

    @Override
    void updateMoveAbility() {
        moveAbility.setRaiseIsAble(raiseIsAble());
        moveAbility.setCallIsAble(callIsAble());
        moveAbility.setCheckIsAble(moveValidator.checkIsAble(raises));
        poker.notifyMoveAbilityListeners(players.current().id(), moveAbility);
    }

    abstract void makeDealerJob();

    abstract boolean attemptNextState();

    boolean callIsAble() {
        return moveValidator.callIsAble();
    }

    boolean raiseIsAble() {
        return moveValidator.raiseIsAble(raises);
    }

    void determineUnderTheGunPosition() {
        players.nextNonAllinnerAfterDealer();
    }

    boolean timeToNextState() {
        return allActiveNonAllinnersChecked() ||
                thereWereRaisesAndAllActivePlayersAreAllinnersOrHaveSameWagers();
    }

    Player latestAggressor() {
        return latestAggressor;
    }

    int checks() {
        return checks;
    }

    int raises() {
        return raises;
    }

    private boolean onlyOneActivePlayer() {
        return players.activePlayersNumber() == 1;
    }

    private boolean allActiveNonAllinnersChecked() {
        return checks == players.activeNonAllinnersNumber();
    }

    private boolean thereWereRaisesAndAllActivePlayersAreAllinnersOrHaveSameWagers() {
        return raises != 0 && players.activeNonAllinnersWithSameWagerNumber(bank.wager())
                + players.allinnersNumber() == players.activePlayersNumber();
    }

    private void notifyMoneyUpdatedListeners() {
        Player currentPlayer = players.current();
        poker.notifyPlayerMoneyUpdatedListeners(currentPlayer.id(), currentPlayer.stack(), currentPlayer.wager());
        poker.notifyBankMoneyUpdatedListeners(bank.money(), bank.wager());
    }

}
