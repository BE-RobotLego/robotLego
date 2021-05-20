package cases;

import robot.Orientation;

import java.util.ArrayList;

public class Parcours {
	
	private Case[][] cases;
	private int nb_lignes;
	private int nb_colonnes;
	private int nb_patients;
	
	public Parcours(int nb_lignes, int nb_colonnes, String[][] dir, int nb_patients) {
		cases = new Case[nb_lignes][nb_colonnes];
		this.nb_lignes = nb_lignes;
		this.nb_colonnes = nb_colonnes;
		this.nb_patients = nb_patients;
		remplirMatrice(dir);
	}
	
	
	private void remplirMatrice(String[][] dir){
		for(int i=0; i<nb_lignes;i++){
			for(int j=0; j<nb_colonnes;j++){
				cases[i][j] = new Case(i,j,dir[i][j]);
			}
		}
	}
	
	public Case[][] getParcours(){
		return cases;
	}

	public Case getNextCase(Case c, Orientation o){
		int i = c.getI();
		int j = c.getJ();

		Case next = null;

		switch(o){
			case E:
				next = cases[i][j+1];
				break;
			case W:
				next = cases[i][j-1];
				break;
			case N:
				next = cases[i-1][j];
				break;
			case S:
				next = cases[i+1][j];
				break;
		}
		return next;
	}

	public Case getCaseAt(int i, int j){
		return cases[i][j];
	}

	public ArrayList<Case> getPatientList(){
		ArrayList<Case> patients = new ArrayList<>();

		for(Case[] caseList: cases) {
			for(Case c: caseList) {
				if(c.hasPatient())
					patients.add(c);
			}
		}
		return patients;
	}

	public ArrayList<Case> getHopitalList(){
		ArrayList<Case> hopitaux = new ArrayList<>();

		for(Case[] caseList: cases) {
			for(Case c: caseList) {
				if(c.hasHopital())
					hopitaux.add(c);
			}
		}
		return hopitaux;
	}


	public int getNb_lignes() {
		return nb_lignes;
	}

	public int getNb_colonnes(){
		return nb_colonnes;
	}

	public int getNbPatients(){
		return nb_patients;
	}

	public void setNbPatients(int nb_patients){
		this.nb_patients = nb_patients;
	}

	public void resetPoidsCases(){
		for(int i = 0; i<nb_lignes;i++){
			for(int j = 0; j<nb_colonnes; j++){
				getCaseAt(i,j).initPoids();
			}
		}
	}
}
