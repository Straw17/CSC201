//********************************************************************
//Driver.java
//Author: Evan Conway
//Contains main method
//********************************************************************

public class Driver {
	//-----------------------------------------------------------------
	//Main method. Creates a deck, shuffles it, and deals every card.
	//-----------------------------------------------------------------
	public static void main(String[] args) {
		DeckOfCards deck = new DeckOfCards();
		
		deck.shuffle();
		for(int i = 0; i < 52; i++) {
			deck.deal();
		}
	}
}