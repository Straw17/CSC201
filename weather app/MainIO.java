//********************************************************************
//MainIO.java
//Author: Evan Conway
//Controls main input/output for weather app
//********************************************************************

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MainIO {
	Scanner scnr = new Scanner(System.in);
	LocationManager manager = new LocationManager();
	ForecastFetcher fetcher;
	
	File zipFile = new File("zips.txt");
	BufferedReader br;
	
	//-----------------------------------------------------------------
	//Manages program exiting and access of the location and forecast menus
	//-----------------------------------------------------------------
	public void masterMenu() {
		while(true) {
			System.out.println("Select action:");
			System.out.println("0. Exit");
			System.out.println("1. Manage location");
			System.out.println("2. Get forecast");
			
			switch(getMenuAnswer(3)) {
			case 0:
				System.exit(0);
				break;
			case 1:
				locationMenu();
				break;
			case 2:
				forecastMenu();
				break;
			}
		}
	}
	
	//-----------------------------------------------------------------
	//Manages location features: add (ZIP or coords), remove, set current location
	//-----------------------------------------------------------------
	private void locationMenu() {
		while(true) {
			System.out.println("Select action:");
			System.out.println("0. Return to main menu");
			System.out.println("1. Add tracked location (ZIP)");
			System.out.println("2. Add tracked location (coords)");
			System.out.println("3. Remove tracked location");
			System.out.println("4. Set current location");
			
			String name;
			String coords = "";
			int option, numLocations;
			
			try {
				switch(getMenuAnswer(5)) {
				case 0:
					return;
				case 1:
					System.out.print("Enter location name: ");
					name = scnr.nextLine();
					try {
						coords = getZIPLocationInput(); 
					} catch (Exception e) {
						System.out.println("Error. Please try again."); System.out.println();
						continue;
					}
					manager.addLocation(name, coords);
					System.out.println();
					break;
				case 2:
					System.out.print("Enter location name: ");
					name = scnr.nextLine();
					
					String latitude, longitude;
					while(true) {
						System.out.print("Enter location latitude: ");
						latitude = scnr.nextLine();
						try {
							double latNum = Double.parseDouble(latitude);
							if(latNum >= -90.0 && latNum <= 90.0) {
								break;
							}
						} catch (Exception e) {}
					}
					while(true) {
						System.out.print("Enter location longitude: ");
						longitude = scnr.nextLine();
						try {
							double longNum = Double.parseDouble(longitude);
							if(longNum >= -180.0 && longNum <= 180.0) {
								break;
							}
						} catch (Exception e) {}
					}
					
					coords = latitude + "," + longitude;
					manager.addLocation(name, coords);
					System.out.println();
					break;
				case 3:
					System.out.println("Select location to remove: ");
					System.out.println("0. Return to location menu");
					manager.getLocations();
					numLocations = manager.locations.size() - (manager.currentLocation == null ? 0 : 1);
					option = getMenuAnswer(numLocations+1);
					if(option == 0) {
						continue;
					} else {
						manager.removeLocation(option);
					}
					break;
				case 4:
					System.out.println("Select current location: ");
					System.out.println("0. Return to location menu");
					manager.getLocations();
					numLocations = manager.locations.size() - (manager.currentLocation == null ? 0 : 1);
					option = getMenuAnswer(numLocations+1);
					if(option == 0) {
						continue;
					} else {
						manager.setCurrentLocation(option);
					}
					break;
				}
			} catch (Exception e) {
				System.out.println("Error. Please attempt to fix any issues and then try again."); System.out.println();
			}
		}
	}
	
	//-----------------------------------------------------------------
	//Manages forecast features: daily, weekly, current rain chance
	//-----------------------------------------------------------------
	private void forecastMenu() {
		if(manager.currentLocation == null || manager.locations.getProperty(manager.currentLocation) == null) {
			System.out.println("No current location has been set");
			System.out.println();
			return;
		}
		fetcher = new ForecastFetcher(manager.locations.getProperty(manager.currentLocation));
		
		while(true) {
			System.out.println("Select action:");
			System.out.println("0. Return to main menu");
			System.out.println("1. Get daily forecast");
			System.out.println("2. Get weekly forecast");
			System.out.println("3. Get current chance of rain");
			
			int answer = getMenuAnswer(4);
			
			try {
				URL url = new URL("https://api.weather.gov/");
				URLConnection connection = url.openConnection();
				connection.connect();
			} catch (Exception e) {
				System.out.println("Error with internet connection. Please check your connection.");
				System.out.println();
				continue;
			}
			
			try {
				switch(answer) {
				case 0:
					return;
				case 1:
					fetcher.getDailyForecast();
					System.out.println();
					break;
				case 2:
					fetcher.getWeeklyForecast();
					System.out.println();
					break;
				case 3:
					fetcher.getRainChance();
					System.out.println();
					break;
				}
			} catch (Exception e) {
				System.out.println("Error. Please attempt to fix any issues and then try again."); System.out.println();
			}
		}
	}
	
	//-----------------------------------------------------------------
	//Manages location input via ZIP
	//-----------------------------------------------------------------
	private String getZIPLocationInput() throws IOException {
		while(true) {
			System.out.print("Enter ZIP code: ");
			String zipCode = scnr.nextLine();
			
			if(zipCode.length() != 5) { continue;}
			try {
				int zipInt = Integer.parseInt(zipCode);
			} catch (Exception e) { continue; }
			
			br = new BufferedReader(new FileReader(zipFile));
			String currentLine;
			
			int index1;
			String lineZip; String coords;
			
			while((currentLine = br.readLine()) != null) {
				index1 = currentLine.indexOf(',');
				lineZip = currentLine.substring(0, index1);
				if(!lineZip.equals(zipCode)) { 
					continue; 
				}

				coords = currentLine.substring(index1+1);
				coords = coords.replaceAll("\\s+","");
				return coords;
			}
		}
	}
	
	//-----------------------------------------------------------------
	//Prompts for a menu option out of a certain number of options
	//-----------------------------------------------------------------
	private int getMenuAnswer(int numOptions) {
		int option = -1;
		while(!(option >= 0 && option < numOptions)) {
			System.out.print("Enter option number: ");
			try {
				option = Integer.parseInt(scnr.nextLine());
			} catch(Exception e) {
				option = -1;
			}
		}
		
		System.out.println();
		return option;
	}

	//-----------------------------------------------------------------
	//Starts the master menu
	//-----------------------------------------------------------------
	public static void main(String[] args) {
		MainIO main = new MainIO();
		main.masterMenu();
	}
}