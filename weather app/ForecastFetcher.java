//********************************************************************
//ForecastFetcher.java
//Author: Evan Conway
//Fetches and generates forecasts
//********************************************************************

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ForecastFetcher {
	String baseLink;
	
	public ForecastFetcher(String coords) {
		this.baseLink = "https://api.weather.gov/points/" + coords;
	}
	
	public void getDailyForecast() throws Exception {
		JSONObject forecastJSON = getForecastObject("forecastHourly");
		JSONObject properties = (JSONObject) forecastJSON.get("properties");
		JSONArray periods = (JSONArray) properties.get("periods");
		
		Integer[] rainChances = getRainChanceArray(24);
		
		JSONObject timeJSON;
		String time, temp, tempUnit, windSpeed, forecast;
		LocalDateTime parsedTime; int hour; String partOfDay;
		String toPrint, rain;
		for (int i = 0; i < 24; i++) {
			timeJSON = (JSONObject) periods.get(i);
			
			time = (String) timeJSON.get("startTime");
			parsedTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			hour = parsedTime.getHour() ;
			if(hour == 0) {
				break;
			}
			
			temp = Long.toString((long) timeJSON.get("temperature"));
			tempUnit = (String) timeJSON.get("temperatureUnit");
			windSpeed = (String) timeJSON.get("windSpeed");
			forecast = (String) timeJSON.get("shortForecast");
			rain = Integer.toString(rainChances[i]);
			
			partOfDay = (hour/12 == 0 ? "AM" : "PM");
			hour = hour % 12;
			hour = (hour == 0 ? 12 : hour);
			
			toPrint = Integer.toString(hour) + partOfDay + ": ";
			toPrint += forecast + ", ";
			toPrint += temp + tempUnit + ", ";
			toPrint += rain + "% chance of precipitation, ";
			toPrint += windSpeed + " wind speed";
			System.out.println(toPrint);
		}
	}
	
	public void getWeeklyForecast() throws Exception {
		JSONObject forecastJSON = getForecastObject("forecast");
		JSONObject properties = (JSONObject) forecastJSON.get("properties");
		JSONArray periods = (JSONArray) properties.get("periods");
		
		JSONObject timeJSON;
		String name, forecast;
		for (int i = 0; i < periods.size(); i++) {
			timeJSON = (JSONObject) periods.get(i);
			name = (String) timeJSON.get("name");
			forecast = (String) timeJSON.get("detailedForecast");
			System.out.println(name + ": " + forecast);
			if(name.contains("ight") && i != periods.size()-1) {
				System.out.println();
			}
		}
	}
	
	public void getRainChance() throws Exception {
		Integer[] rainChances = getRainChanceArray(5);
		
		String rainString;
		for(int hour = 0; hour < 5; hour++) {
			if(hour == 0) {
				rainString = "Current rain chance:    ";
				rainString += Integer.toString(rainChances[hour]).length() == 1 ? " " : "";
			} else {
				rainString = "Rain chance in " + hour;
				rainString += hour == 1 ? " hour:  " : " hours: ";
				rainString += Integer.toString(rainChances[hour]).length() == 1 ? " " : "";
			}
			
			rainString += rainChances[hour] + "%";
			System.out.println(rainString);
		}
	}
	
	private Integer[] getRainChanceArray(int size) throws Exception {
		Integer[] rainChanceArray = new Integer[size];
		
		JSONObject forecastJSON = getForecastObject("forecastGridData");
		JSONObject properties = (JSONObject) forecastJSON.get("properties");
		JSONObject precipitation = (JSONObject) properties.get("probabilityOfPrecipitation");
		JSONArray values = (JSONArray) precipitation.get("values");
		
		LocalDateTime current = LocalDateTime.now(ZoneId.of("UTC+00:00"));
		current = current.truncatedTo(ChronoUnit.HOURS);
		
		JSONObject section;
		String time;
		int chance;
		String[] timeSplit;
		ArrayList<LocalDateTime> times = new ArrayList<LocalDateTime>();
		ArrayList<Integer> chances = new ArrayList<Integer>();
		String period;
		int periodLength;
		for(int i = 0; i < values.size(); i++) {
			section = (JSONObject) values.get(i);
			time = (String) section.get("validTime");
			chance = Math.toIntExact((long) section.get("value"));
			
			timeSplit = time.split("/");
			
			LocalDateTime parsedDate = LocalDateTime.parse(timeSplit[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			period = timeSplit[1].substring(timeSplit[1].length() - 2);
			periodLength = Integer.parseInt(period.substring(0,period.length()-1));
			periodLength = period.charAt(period.length()-1) == 'D' ? periodLength * 24 : periodLength;
			
			for(int hour = 0; hour < periodLength; hour++) {
				times.add(parsedDate.plusHours(hour));
				chances.add(chance);
			}
		}
		
		boolean proceed = false; int futureHour = 0;
		for(int hour = 0; hour < times.size(); hour++) {
			if(times.get(hour).isEqual(current) || proceed) {
				proceed = true;
				if(futureHour >= size) {
					break;
				}
				rainChanceArray[futureHour] = chances.get(hour);
				futureHour++;
			}
		}
		
		return rainChanceArray;
	}
	
	private String getTextFromLink(String url) throws Exception {
		URL link;
		BufferedReader br;
		String text = null;
		
		try {
			link = new URL(url);
			br = new BufferedReader(new InputStreamReader(link.openStream()));
			text = br.lines().collect(Collectors.joining("\n"));
			br.close();
		} catch (Exception e) {
			throw e;
		}
		
		return text;
	}
	
	private JSONObject getForecastObject(String key) throws Exception {
		JSONObject forecastJSON = null;
		
		try {
			String text = getTextFromLink(baseLink);
			JSONParser parser = new JSONParser();
			JSONObject baseJSON = (JSONObject) parser.parse(text);
			JSONObject properties = (JSONObject) baseJSON.get("properties");
			
			text = getTextFromLink((String) properties.get(key));
			forecastJSON = (JSONObject) parser.parse(text);
		} catch (Exception e) {
			throw e;
		}
		
		return forecastJSON;
	}
}