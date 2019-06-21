package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Arco;
import it.polito.tdp.extflightdelays.model.Flight;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports() {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				result.add(airport);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Airport> loadAllVertex(Map<Integer, Airport>mappaIdAir,int max,Airport a) {
		String sql ="SELECT f.DESTINATION_AIRPORT_ID as id ,COUNT(DISTINCT(f.AIRLINE_ID)) AS n  " + 
				"FROM flights AS f  " + 
				"GROUP BY f.DESTINATION_AIRPORT_ID " + 
				"";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//st.setInt(1,a.getId() );
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(rs.getInt("n")>max) {
					result.add(mappaIdAir.get(rs.getInt("id")));
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Arco> loadAllLink(Map<Integer, Airport>mappaIdAir) {
		String sql ="SELECT f.ORIGIN_AIRPORT_ID as oid,f.DESTINATION_AIRPORT_ID as did " + 
				"FROM flights AS f " + 
				"GROUP BY f.ORIGIN_AIRPORT_ID,f.DESTINATION_AIRPORT_ID  ";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			//st.setInt(1,a.getId() );
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport a1 = mappaIdAir.get(rs.getInt("oid"));
				Airport a2 = mappaIdAir.get(rs.getInt("did"));
			result.add(new Arco(a1, a2, 0));
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	public double GetPeso(Map<Integer, Airport>mappaIdAir,Airport a1 ,Airport a2 ) {
		String sql ="SELECT f.ORIGIN_AIRPORT_ID,f.DESTINATION_AIRPORT_ID,COUNT(f.DESTINATION_AIRPORT_ID) as n " + 
				"FROM flights AS f " + 
				"WHERE f.DESTINATION_AIRPORT_ID= ?  AND  f.ORIGIN_AIRPORT_ID= ?  || f.DESTINATION_AIRPORT_ID= ? AND f.ORIGIN_AIRPORT_ID= ?   " + 
				"GROUP BY f.ORIGIN_AIRPORT_ID,f.DESTINATION_AIRPORT_ID ";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,a1.getId() );
			st.setInt(2, a2.getId());
			st.setInt(3,a2.getId() );
			st.setInt(4, a1.getId());
			
			ResultSet rs = st.executeQuery();
			double somma = 0;

			while (rs.next()) {
				somma+=rs.getDouble("n");
			}

			conn.close();
			return somma;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public int IsArco(Map<Integer, Airport>mappaIdAir,Airport a1 ,Airport a2) {
		String sql ="SELECT f.ORIGIN_AIRPORT_ID,f.DESTINATION_AIRPORT_ID " + 
				"FROM flights AS f   " + 
				"WHERE f.ORIGIN_AIRPORT_ID= ?  AND f.DESTINATION_AIRPORT_ID= ?    " + 
				"GROUP BY f.ORIGIN_AIRPORT_ID,f.ORIGIN_AIRPORT_ID  ";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,a1.getId() );
			st.setInt(2,a2.getId() );
			ResultSet rs = st.executeQuery();
			int is =0;
			while (rs.next()) {
			is++;
				
			}

			conn.close();
			return is;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
