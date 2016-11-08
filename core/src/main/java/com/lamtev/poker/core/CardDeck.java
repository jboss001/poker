package com.lamtev.poker.core;

import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class CardDeck extends Cards {

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
        Collections.shuffle(cards, new Random(new Date().getTime()));
    }

}
