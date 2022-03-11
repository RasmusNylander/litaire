package model;

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
}