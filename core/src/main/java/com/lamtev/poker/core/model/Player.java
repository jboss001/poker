package com.lamtev.poker.core.model;

public final class Player {

    private int stack;
    private int wager = 0;
    private Cards cards = new Cards();
    private boolean isActive = true;
    private final String id;

    public Player(String id, int stack) {
        this.id = id;
        this.stack = stack;
    }

    public String id() {
        return id;
    }

    public int stack() {
        return stack;
    }

    public int wager() {
        return wager;
    }

    public Cards cards() {
        return cards;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAllinner() {
        return isActive && stack == 0;
    }

    public boolean isActiveNonAllinner() {
        return isActive && stack != 0;
    }

    public void addCard(Card card) {
        if (cards.size() == 2) {
            throw new RuntimeException("can't add more than 2 cards to player");
        }
        cards.add(card);
    }

    public void fold() {
        if (cards.isEmpty()) {
            throw new RuntimeException("can't fold");
        }
        while (!cards.isEmpty()) {
            cards.pickUpTop();
        }
        isActive = false;
    }

    public void addMoney(int money) {
        stack += money;
    }

    public int takeMoney(int money) {
        stack -= money;
        wager += money;
        return money;
    }

}
