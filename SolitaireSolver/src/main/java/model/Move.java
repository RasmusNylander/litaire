package model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public record Move(int movedCard, @NotNull Optional<Integer> destination) {

	public Move(int movedCard, @NotNull Optional<Integer> destination) {
		if (!Card.isValidCard(movedCard) | (destination.isPresent() && !Card.isValidCard(destination.get())))
			throw new IllegalArgumentException("Error: Invalid card.");
		this.movedCard = movedCard;
		this.destination = destination;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Move move = (Move) o;
		return movedCard == move.movedCard && destination.equals(move.destination);
	}

	@Override
	public int hashCode() {
		return Objects.hash(movedCard, destination);
	}


	@Override
	public String toString() {
		return "Move[" +
				"movedCard=" + movedCard + ", " +
				"destination=" + destination + ']';
	}

}
