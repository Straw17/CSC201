//********************************************************************
//DeckOfCards.java
//Author: Evan Conway
//Represents a deck of cards
//********************************************************************

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DeckOfCards {
	public ArrayList<Card> cardsLeft = new ArrayList<Card>();
	public Random rand = new Random();
	
	int nextCardPos;
	Card nextCard;
	
	public DeckOfCards() {
		initDeck();
	}

	//-----------------------------------------------------------------
	//Fills and shuffles deck
	//-----------------------------------------------------------------
	public void shuffle() {
		initDeck();
		Collections.shuffle(cardsLeft);
	}
	
	//-----------------------------------------------------------------
	//Deals the first card and removes it from the deck
	//-----------------------------------------------------------------
	public void deal() {
		//if there are no cards left in the deck, inform the user
		if(cardsLeft.size() == 0) {
			getNumCards();
		} else {
			nextCard = cardsLeft.get(0);
			cardsLeft.remove(0);
			System.out.println(nextCard.toString());
		}
	}
	
	//-----------------------------------------------------------------
	//Prints the number of remaining cards
	//-----------------------------------------------------------------
	public void getNumCards () {
		System.out.println("There are " + cardsLeft.size() + " cards left");
	}
	
	//-----------------------------------------------------------------
	//Fills the deck with cards
	//-----------------------------------------------------------------
	public void initDeck() {
		cardsLeft = new ArrayList<Card>();
		
		for(int suit = 1; suit <= 4; suit++) {
			for(int face = 1; face <= 13; face++) {
				cardsLeft.add(new Card(face, suit));
			}
		}
	}
}