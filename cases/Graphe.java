package cases;

import java.util.ArrayList;
import java.util.TreeMap;

public class Graphe {
	private int nb_lignes;
	private int nb_colonnes;
	private Case[][] parcours;
	private TreeMap<Case, ArrayList<Case>> graphe;
	
	public Graphe(Parcours parcours) {
		this.nb_lignes = parcours.getNb_lignes();
		this.nb_colonnes = parcours.getNb_colonnes();
		this.parcours = parcours.getParcours();
		graphe = new TreeMap<Case, ArrayList<Case>>();
		remplirGraphe();
	}
	
	private void remplirGraphe(){
		Case c;
		for(int i=0; i<nb_lignes; i++){
			for(int j=0; j<nb_colonnes; j++){
				c = parcours[i][j];
				graphe.put(c, trouverVoisins(i,j,c));
			}
		}
	}
	
	private ArrayList<Case> trouverVoisins(int i, int j, Case c){
		Case voisin;
		ArrayList<Case> voisins = new ArrayList<>();
		//On cherche un voisin à gauche, on vérifie donc qu'on est pas sur le bord gauche
		if(j>0){
			voisin = parcours[i][j-1];
			if(c.contientDir('L') && voisin.contientDir('R'))
				voisins.add(voisin);
		}
		//On cherche un voisin à droite, on vérifie donc qu'on est pas sur le bord droit
		if(j+1<nb_colonnes){
			voisin = parcours[i][j+1];
			if(c.contientDir('R') && voisin.contientDir('L'))
				voisins.add(voisin);
		}
		//On cherche un voisin en bas, on vérifie donc qu'on est pas sur le bord inferieur
		if(i+1<nb_lignes){
					voisin = parcours[i+1][j];
					if(c.contientDir('D') && voisin.contientDir('U'))
						voisins.add(voisin);
		}
		//On cherche un voisin en haut, on vérifie donc qu'on est pas sur le bord superieur
		if(i>0){
					voisin = parcours[i-1][j];
					if(c.contientDir('U') && voisin.contientDir('D'))
						voisins.add(voisin);
		}
		return voisins;
	}
	
	public int getNb_lignes() {
		return nb_lignes;
	}

	public int getNb_colonnes() {
		return nb_colonnes;
	}

	public Case[][] getParcours() {
		return parcours;
	}

	public TreeMap<Case, ArrayList<Case>> getGraphe() {
		return graphe;
	}
	
	
	
	
}	


