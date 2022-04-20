package model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class Card {
	public static final int Ace = 0;
	public static final int Two = 1;
	public static final int Three = 2;
	public static final int Four = 3;
	public static final int Five = 4;
	public static final int Six = 5;
	public static final int Seven = 6;
	public static final int Eight = 7;
	public static final int Nine = 8;
	public static final int Ten = 9;
	public static final int Jack = 10;
	public static final int Queen = 11;
	public static final int King = 12;
	public static final int Unknown = 13;

	public static final int Colour = 16;
	public static final int Type = 32;

	public static final int SuitMask = 32 | 16;
	public static final int RankMask = ~SuitMask;

	public static Integer[] fullDeck() {
		int numSuits = 4;
		Integer[] deck = new Integer[(King - Ace + 1) * numSuits];

		for (int i = Ace; i < Unknown; i++) {
			deck[i * 4 + 0] = i;
			deck[i * 4 + 1] = i | Type;
			deck[i * 4 + 3] = i | Colour;
			deck[i * 4 + 2] = i | Colour | Type;
		}
		return deck;
	}

	public static String asString(int card) {
		if (card == Unknown) return "??";
		final String[] rankAsString = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K"};
		final String[] suitAsString = {"♠", "♥", "♣", "♦"};
		return suitAsString[(card & SuitMask) >> 4] + rankAsString[card & RankMask];
	}

	public static String[] asString(int @NotNull ... cards) {
		return Arrays.stream(cards).mapToObj(Card::asString).toArray(String[]::new);
	}

	public static boolean isValidCard(int card) {
		return (card & RankMask) >= Ace && (card & RankMask) <= Unknown; // This also ensure that no bit is set after SuitMask
	}

	public static void validateCard(int card) throws InvalidCardException {
		if (!isValidCard(card)) throw new InvalidCardException("Error: Invalid card: " + card + ".");
	}
}
