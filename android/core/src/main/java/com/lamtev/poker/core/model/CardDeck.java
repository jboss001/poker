package com.lamtev.poker.core.model;

import java.util.Collections;


final class CardDeck extends Cards {

    public CardDeck() {
        super();
        initCards();
    }

    public void initCards() {
        cards.clear();
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public void add(Card card) {
        throw new UnsupportedOperationException();
    }

}
