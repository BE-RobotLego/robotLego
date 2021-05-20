package ia;

import java.util.*;
import java.util.TreeMap;

import cases.Case;

public class Dijkstra {
	
	private TreeMap<Case, ArrayList<Case>> graphe;
	private Case depart;
	private Case arrivee;
	private Map<Case,Infos> dijkstra;
	private ArrayList<Case> sommets;
	private ArrayList<Case> dejaVu;
	private ArrayList<Case> ordre;
	private Case previous;
	
	public Dijkstra(TreeMap<Case, ArrayList<Case>> graphe, Case depart, Case arrivee, Case previous) {
		this.graphe = graphe;
		this.depart = depart;
		this.arrivee = arrivee;
		dijkstra = new HashMap<>();
		sommets = getSommets();
		ordre = new ArrayList<>();
		this.previous = previous;
		initDijkstra();
		applyDijkstra(this.depart, previous);
		plusCourtChemin();
	}
	
	private void initDijkstra(){
		for(Case c : sommets){
			if(c.equals(depart))
				dijkstra.put(c, new Infos(c, 0));
			else
				dijkstra.put(c, new Infos(null,Integer.MAX_VALUE));
				
		}
	}
	
	private ArrayList<Case> getSommets(){
		Set<Case> keys = graphe.keySet();
		return new ArrayList<>(keys);
	}
	
	private ArrayList<Case> getVoisins(Case c){
		return graphe.get(c);
	}

	private void applyDijkstra(Case curr, Case previous){
		//poidsCurr le poids pour aller à la case
		//getPoids() le poids de la case en elle même
		int poidsCurr = dijkstra.get(curr).getPoids();
		//System.out.println("Case en cours : "+curr+" a un poid de : "+curr.getPoids());
		//System.out.println("Le poids pour y acceder est de : "+poidsCurr);
		ArrayList<Case> voisins = getVoisins(curr);
		if(curr.equals(arrivee)){
			return;
		}
		for(Case voisin : voisins){
			//Si la case où l'on veut aller est celle d'où l'on vient
			if(curr.equals(depart) && voisin.equals(previous)){
				if(poidsCurr + curr.getPoids() < dijkstra.get(previous).getPoids()){
					dijkstra.put(voisin,new Infos(curr, poidsCurr +2));
					applyDijkstra(voisin, curr);
				}
			}
			//Si c'est une intersection
			else if(curr.is3way()){
				if(poidsCurr + curr.getPoids() < dijkstra.get(voisin).getPoids()){
					dijkstra.put(voisin,new Infos(curr, poidsCurr + curr.getPoids()));
					applyDijkstra(voisin, curr);
				}
			}
			//Virage ou ligne droite
			else{
				if(poidsCurr + curr.getPoids() < dijkstra.get(voisin).getPoids()){
					dijkstra.put(voisin,new Infos(curr, poidsCurr + curr.getPoids()));
					applyDijkstra(voisin, curr);
				}
			}
		}
	}


	private void plusCourtChemin(){
		Case pointer = arrivee;
		System.out.println(dijkstra);
		while(pointer!=depart){
			ordre.add(0,pointer);
			pointer = dijkstra.get(pointer).getPere();
		}
	}
	
	public ArrayList<Case> getOrdre() {
		return ordre;
	}


	public int getPoidsChemin(){
		return dijkstra.get(arrivee).getPoids();
	}
	
	public Infos getLastCaseInfos(){
		return dijkstra.get(arrivee);
	}

	public Map<Case, Infos> getDijkstra() {
		return dijkstra;
	}
}
