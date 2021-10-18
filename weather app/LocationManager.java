//********************************************************************
//ForecastFetcher.java
//Author: Evan Conway
//Manages listed locations and their information.
//********************************************************************

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class LocationManager {
	Properties locations = new Properties();
	String currentLocation;
	
	public LocationManager() {
		try {
			InputStream in = new FileInputStream("locations.properties");
			locations.load(in);
		} catch (Exception e) {}
		
		currentLocation = locations.getProperty("currentLocation");
	}
	
	public void getLocations() {
		Iterator<Object> locationItr = locations.keySet().iterator();
		int option = 1;
		String locationName;
		
		while(locationItr.hasNext()) {
			locationName = (String) locationItr.next();
			if(locationName.equals("currentLocation")) {
				continue;
			} else if(locationName.equals(currentLocation)) {
				locationName += " (C)";
			}
			System.out.println("" + option + ". " + locationName);
			option++;
		}
	}
	
	public void setCurrentLocation(int option) throws Exception {
		Iterator<Object> locationItr = locations.keySet().iterator();
		int num = 1;
		String locationName;
		
		while(locationItr.hasNext()) {
			locationName = (String) locationItr.next();
			if(locationName.equals("currentLocation")) {
				continue;
			}
			if(num == option) {
				currentLocation = locationName;
				
				locations.setProperty("currentLocation", currentLocation);
				try { save(); } catch (Exception e) { throw e; }
				return;
			}
			num++;
		}
	}
	
	public void addLocation(String name, String coords) throws Exception {
		locations.setProperty(name, coords);
		try { save(); } catch (Exception e) { throw e; }
	}
	
	public void removeLocation(int option) throws Exception {
		Iterator<Object> locationItr = locations.keySet().iterator();
		int num = 1;
		String locationName;
		
		while(locationItr.hasNext()) {
			locationName = (String) locationItr.next();
			if(locationName.equals("currentLocation")) {
				continue;
			}
			if(num == option) {
				locations.remove(locationName);
				try { save(); } catch (Exception e) { throw e; }
				return;
			}
			num++;
		}
	}
	
	private void save() throws Exception {
		try {
			locations.store(new FileOutputStream("locations.properties"), null);
		} catch (Exception e) {
			throw e;
		}
	}
}