//----------------------------------------------------------------------------------------------------
//CashCounter.java
//Author: Evan Conway
//Converts money value in dollars to component coins/bills
//----------------------------------------------------------------------------------------------------

import java.text.DecimalFormat;
import java.util.Scanner;

public class CashCounter {
	double moneyAmount;
	int tens, fives, ones, quarters, dimes, nickles, pennies;
	
	Scanner scnr = new Scanner(System.in);
	DecimalFormat df = new DecimalFormat("##.00");
	
	//----------------------------------------------------------------------------------------------------
	//prompts the user to enter their money amount and sets moneyAmount
	//checks to make sure input is valid
	//----------------------------------------------------------------------------------------------------
	public void moneyPrompt() {
		String moneyAmountStr;
		
		while(true) {
			System.out.println("Money values should be entered in dollars, without currency signs.");
			System.out.print("Input money value: ");
			try {
				moneyAmountStr = scnr.nextLine();
				//checks that input can be used as a double
				moneyAmount = Double.parseDouble(moneyAmountStr);
				//checks to make sure there are a valid number of digits
				if((moneyAmountStr.length() - moneyAmountStr.indexOf(".") - 1) < 3 || (moneyAmountStr.indexOf(".") == -1)) {
					break;
				} else {
					System.out.println("Your input has too many decimals.");
					System.out.println();
				}
			} catch(Exception e) {
				System.out.println("An error has occured with your input. Please check to make sure it is valid input.");
				System.out.println();
			}
		}
	}
	
	//----------------------------------------------------------------------------------------------------
	//assigns the number of bills/coins for each value of bill/coin
	//----------------------------------------------------------------------------------------------------
	public void assignCash() {
		tens = getCashNum(10.00);
		fives = getCashNum(5.00);
		ones = getCashNum(1.00);
		quarters = getCashNum(0.25);
		dimes = getCashNum(0.10);
		nickles = getCashNum(0.05);
		pennies = getCashNum(0.01);
	}
	
	//----------------------------------------------------------------------------------------------------
	//returns the number of bills/coins of a certain value that can be taken out of the money
	//updates the amount of remaining money to reflect the removal
	//----------------------------------------------------------------------------------------------------
	private int getCashNum(double cashVal) {
		int cashAmount = (int) (moneyAmount / cashVal);
		
		//sets MoneyAmount to match remaining money and fixes any rounding errors
		moneyAmount %= cashVal;
		moneyAmount = Double.parseDouble(df.format(moneyAmount));
		
		return cashAmount;
	}
	
	//----------------------------------------------------------------------------------------------------
	//prints the number of bills/coins for each value
	//----------------------------------------------------------------------------------------------------
	public void printCash() {
		System.out.println(tens + " ten dollar bills");
		System.out.println(fives + " five dollar bills");
		System.out.println(ones + " one dollar bills");
		System.out.println(quarters + " quarters");
		System.out.println(dimes + " dimes");
		System.out.println(nickles + " nickles");
		System.out.println(pennies + " pennies");
	}
	
	//----------------------------------------------------------------------------------------------------
	//creates CashCounter object and runs necessary methods
	//----------------------------------------------------------------------------------------------------
	public static void main(String args[]) {
		CashCounter mainCounter = new CashCounter();
		mainCounter.moneyPrompt();
		mainCounter.assignCash();
		mainCounter.printCash();
	}
}