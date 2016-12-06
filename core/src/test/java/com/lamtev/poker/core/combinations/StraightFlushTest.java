package com.lamtev.poker.core.combinations;

import com.lamtev.poker.core.model.Card;
import com.lamtev.poker.core.model.Rank;
import com.lamtev.poker.core.model.Suit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.lamtev.poker.core.combinations.Straight.isStraight;
import static com.lamtev.poker.core.combinations.StraightFlush.isStraightFlush;
import static org.junit.Assert.*;

public class StraightFlushTest {
    @Test
    public void testIsStraightFlush() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Rank.ACE, Suit.CLOVERS));
        cards.add(new Card(Rank.QUEEN, Suit.CLOVERS));
        cards.add(new Card(Rank.FIVE, Suit.HEARTS));
        cards.add(new Card(Rank.THREE, Suit.CLOVERS));
        cards.add(new Card(Rank.FOUR, Suit.CLOVERS));
        cards.add(new Card(Rank.TWO, Suit.CLOVERS));
        assertTrue(isStraightFlush(cards));
    }

}