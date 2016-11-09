package com.lamtev.poker.core;

import org.junit.Test;

import static com.lamtev.poker.core.Rank.*;
import static com.lamtev.poker.core.Suit.*;
import static org.junit.Assert.assertEquals;

public class PlayerTest {

    private Player generatePlayer() {
        return new Player("Anton", 1000);
    }

    @Test
    public void testTakeCard() {
        Player player = generatePlayer();
        Card card1 = new Card(ACE, PIKES);
        player.addCard(card1);
        assertEquals(card1, player.getCards().cardAt(1));

        Card card2 = new Card(FIVE, HEARTS);
        player.addCard(card2);
        assertEquals(card2, player.getCards().cardAt(2));
    }

    @Test(expected = RuntimeException.class)
    public void testTakeCardExceptionThrowing() {
        Player player = generatePlayer();

        player.addCard(new Card(ACE, PIKES));

        player.addCard(new Card(FIVE, HEARTS));

        player.addCard(new Card(ACE, TILES));
    }

    @Test
    public void testGiveMoney() {
        Player player = generatePlayer();
        player.takeMoney(900);
        assertEquals(100, player.getStack());
    }

    @Test
    public void testTakeMoney() {
        Player player = generatePlayer();
        player.addMoney(500);
        assertEquals(1500, player.getStack());
    }

    @Test
    public void testFold() {
        Player player = generatePlayer();

        player.addCard(new Card(ACE, TILES));
        assertEquals(1, player.getCards().size());

        player.fold();
        assertEquals(0, player.getCards().size());

        player.addCard(new Card(ACE, TILES));
        assertEquals(1, player.getCards().size());

        player.addCard(new Card(FIVE, TILES));
        assertEquals(2, player.getCards().size());

        player.fold();
        assertEquals(0, player.getCards().size());
    }

    @Test(expected = RuntimeException.class)
    public void testFoldExceptionThrowing1() {
        Player player = generatePlayer();
        player.fold();
    }

    @Test(expected = RuntimeException.class)
    public void testFoldExceptionThrowing2() {
        Player player = generatePlayer();
        player.addCard(new Card(ACE, TILES));
        player.addCard(new Card(FIVE, TILES));
        player.fold();
        player.fold();
    }

}
