package lab9;

import lab9.Flight;
import lab9.GMTtime;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ShortestFlight {

    public static void main(String[] args) {
	if (args.length != 4) {
	    System.out.println("Usage: AirportOrigin AirportDestination Time A/P");
	    return;
	}
	Map<String, Integer> airportTimeZoneMap = readAirportData();
	List<Flight> flights = readFlightData(airportTimeZoneMap);

	String airportOrigin = args[0];
	String airportDestination = args[1];
	GMTtime leaveTime = new GMTtime(Integer.parseInt(args[2]),
					airportTimeZoneMap.get(airportOrigin),
					args[3].equals('A'));
	return;
    }

    private static Map<String, Integer> readAirportData() {
	Map<String, Integer> airportTimeZoneMap = new HashMap<String, Integer>();
	BufferedReader r;
	try {
	    InputStream is = new FileInputStream("input/airport-data.txt");
	    r = new BufferedReader(new InputStreamReader(is));
	} catch (IOException e) {
	    System.out.println("IOException while opening airport-data.txt\n" + e);
	    return null;
	}
	try {
	    String nextline = r.readLine();
	    StringTokenizer st = new StringTokenizer(nextline);
	    int numAirports = Integer.parseInt(st.nextToken());
	    for (int i = 0; i < numAirports; i++){
		nextline = r.readLine();
		st = new StringTokenizer(nextline);
		String airportCode = st.nextToken();
		int gmtConv = Integer.parseInt(st.nextToken());
		airportTimeZoneMap.put(airportCode, gmtConv);
	    }
	} catch (IOException e) {
	    System.out.println("IOException while reading sequence from " +
			       "airport-data.txt\n" + e);
	    return null;
	}
	return airportTimeZoneMap;
    }

    private static List<Flight> readFlightData(Map<String, Integer> airportTimeZoneMap) {
	List<Flight> flights = new ArrayList<Flight>();
	BufferedReader r;
	try {
	    InputStream is = new FileInputStream("input/flight-data.txt");
	    r = new BufferedReader(new InputStreamReader(is));
	} catch (IOException e) {
	    System.out.println("IOException while opening flight-data.txt\n" + e);
	    return null;
	}
	try {
	    String nextline = r.readLine();
	    while (nextline != null && !nextline.trim().equals("")) { // not end of file or an empty line
		StringTokenizer st = new StringTokenizer(nextline);
		Flight flight = new Flight();
		flight.airline = st.nextToken();
		flight.flightNum = Integer.parseInt(st.nextToken());
		flight.departureAirportCode = st.nextToken();
		int localDepartureTime = Integer.parseInt(st.nextToken()); 
		boolean amDeparture = st.nextToken().equals("A");
		flight.departureTime = new GMTtime(localDepartureTime, airportTimeZoneMap.get(flight.departureAirportCode), amDeparture);
		flight.arrivalAirportCode = st.nextToken();
		int localArrivalTime = Integer.parseInt(st.nextToken());
		boolean amArrival = st.nextToken().equals("A");
		flight.arrivalTime = new GMTtime(localArrivalTime, airportTimeZoneMap.get(flight.arrivalAirportCode), amArrival);
		nextline = r.readLine();
		flights.add(flight);
	    }
	} catch (IOException e) {
	    System.out.println("IOException while reading sequence from " +
			       "flight-data.txt\n" + e);
	    return null;
	}
	return flights;
    }
}
