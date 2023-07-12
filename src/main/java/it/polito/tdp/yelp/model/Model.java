package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private List<Business> nodes;
	private Map<String,Business> mapIdBusiness;
	
	
	//Per ricorsione
	private List<Business> bestPercorso;
	private Map<Business,Double> mapBusinessReview;
	private double kmTot;
	
	public Model() {
		
		dao = new YelpDao();
		
	}
	
	public double getkmTot() {
		return this.kmTot;
	}
	
	public List<Business> migliorPercorso(double x, Business source, Business target){
		

		this.calcoloMediaRecensioni(x);
		
		List<Business> parziale = new ArrayList<>();
		
		parziale.add(source);
		
		this.bestPercorso = new ArrayList<>(parziale);
		
		ricorsione(parziale,1,target,x,0);
		
		return bestPercorso;
		
	}
	
	private void ricorsione(List<Business> parziale, int livello, Business target, double x,double kmParziali) {
		
		if(parziale.size()>this.bestPercorso.size() && parziale.get(parziale.size()-1).equals(target)) {
			bestPercorso = new ArrayList<>(parziale);
			kmTot = kmParziali;
		}
		
		for(Business b : nodes) {
				if(this.mapBusinessReview.containsKey(b)) {
					if(!parziale.contains(b) && this.mapBusinessReview.get(b)>x) {
						DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(parziale.size()-1),b);
						parziale.add(b);
						ricorsione(parziale,livello++,target,x,kmParziali+this.grafo.getEdgeWeight(e));
						parziale.remove(parziale.size()-1);
					}}
		}
	}
	
	
	public void calcoloMediaRecensioni(double x) {
		this.loadMapReviews(x);
		
	}
	
	
	
	
	
	public void createGraph(String city) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		loadNodes(city);
		Graphs.addAllVertices(this.grafo, nodes);
		for(Business b1:nodes) {
			for(Business b2 :nodes) {
				double peso=0;
				if(!b1.equals(b2)) {
					peso= LatLngTool.distance(b1.getLatLong(), b2.getLatLong(), LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, b1, b2, peso);
					}
			}
		}
	}
	
	public String localeDistante(Business business) {
		
		List<Business> listNeighbors = new ArrayList<>(Graphs.neighborListOf(this.grafo, business));
		double pesoMax=0;
		Business best = null;
		for(Business b : listNeighbors) {
			DefaultWeightedEdge e = this.grafo.getEdge(business, b);
			if(this.grafo.getEdgeWeight(e)>pesoMax) {
				pesoMax = this.grafo.getEdgeWeight(e);
				best = b;
				
			}
			
		}
		
		return best.toString() + " distanza totale di: "+ pesoMax;
	}
	
	public int nNodes() {
		return this.grafo.vertexSet().size();
	}
	
	public int nEdges() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Business> getBusiness(){
		return nodes;
	}
	private void loadMapReviews(double x) {
		this.mapBusinessReview = new HashMap<>(this.dao.getAllReviews(mapIdBusiness,x));
	}
	private void loadNodes(String city) {
		nodes= new ArrayList<>(this.dao.getAllBusiness(city));
		this.mapIdBusiness = new HashMap<>();
		
		for(Business b :nodes) {
			this.mapIdBusiness.put(b.getBusinessId(), b);
		}

	}

	public List<String> getCities(){
		return this.dao.getAllCities();
	}
	
}
