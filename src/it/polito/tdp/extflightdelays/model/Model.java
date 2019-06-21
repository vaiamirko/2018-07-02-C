package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	Graph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport>mappaIdAir;
	
	
	public void CreaGrafo(int max) {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		mappaIdAir = new HashMap<Integer, Airport>();
		
		for(Airport a : dao.loadAllAirports()) {
			mappaIdAir.put(a.getId(), a);
		}
		
		List<Arco> listarchi = new ArrayList<>();
		
		
		Graphs.addAllVertices(grafo, dao.loadAllVertex(mappaIdAir, max, null));
		
		System.out.println("creato grafo con vertici :"+grafo.vertexSet().size());
		
		for(Airport ap : grafo.vertexSet()) {
			for(Airport ad : grafo.vertexSet()) {
				if(dao.IsArco(mappaIdAir, ap, ad)>0) {
					if(!ap.equals(ad))
					grafo.addEdge(ap, ad);
				}
				
				
				
			}
		}
		
		/*for(Arco arc : dao.loadAllLink(mappaIdAir)) {
			
			grafo.addEdge(arc.getAorigine(), arc.getAdest());
			
		}*/
		System.out.format("creato grafo con %d vertici e %d archi ", grafo.vertexSet().size(),grafo.edgeSet().size());
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			Airport a1 = grafo.getEdgeSource(e);
			Airport a2 = grafo.getEdgeTarget(e);
			
			double peso = dao.GetPeso(mappaIdAir, a1, a2);
			
			grafo.setEdgeWeight(e, peso);
					
				
					
		}
		
		
		
	}


	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Arco> getAirConn(Airport a){
		
		List<Arco> connessi = new ArrayList<>();
		
		for(Airport vicino : Graphs.neighborListOf(grafo, a)) {
			
			DefaultWeightedEdge e =grafo.getEdge(a, vicino);
			
			connessi.add(new Arco(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e)));
					
			
		}
		
		Collections.sort(connessi);
		
		return connessi;
	}
	
}
