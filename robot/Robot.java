package robot;

import cases.Case;
import cases.Parcours;

public class Robot {
	
	private int iCurr;
	private int jCurr;
	private int lastI;
	private int lastJ;
	private Orientation ori;
	private int capacity;
	private String name;
	
	public Robot(int i, int j, Orientation ori, String name){
		iCurr = i;
		jCurr = j;
		lastI = 0;
		lastJ = 0;
		this.ori = ori;
		capacity = 2;
		this.name = name;
	}

	public void pickUp(Parcours p){
		Case curr = p.getCaseAt(iCurr,jCurr);
		if(curr.hasPatient()){
			if(capacity > 0) {
				curr.setPatient(false);
				capacity -= 1;
				System.out.println(" **** Le patient en " + iCurr + " " + jCurr + " a été récupéré par "+name+" **** ");
				p.setNbPatients(p.getNbPatients() - 1);
			}
			else
				System.out.println("Il n'y a pas de place.. On reviendra");
		}
		else
			System.out.println("Il n'y a pas de patients");
	}

	public void dropOut(Parcours p){
		Case curr = p.getCaseAt(iCurr,jCurr);
		if(curr.hasHopital()){
			while (capacity != 2){
				capacity++;
				System.out.println(" **** Un patient à été déposé par "+name+" **** ");
			}
		}
		else{
			System.out.println("Il n'y a pas d'hopital ici");
		}
	}

	public void updatePosition(int i, int j){
		lastI = iCurr;
		lastJ = jCurr;
		iCurr = i;
		jCurr = j;
	}

	public String getName(){ return name; }

	public Case getCurrCase(Parcours p){
		return p.getCaseAt(iCurr,jCurr);
	}

	public Case getLastCase(Parcours p) { return p.getCaseAt(lastI,lastJ); }

	public int getiCurr() {
		return iCurr;
	}

	public int getjCurr() {
		return jCurr;
	}

	public int getLastI() {
		return lastI;
	}

	public int getLastJ() {
		return lastJ;
	}

	public void setLastI(int i){
		this.lastI = i;
	}

	public void setLastJ(int j){
		this.lastJ = j;
	}

	public Orientation getOriRobot() {
		int iOri = iCurr - lastI;
		int jOri = jCurr - lastJ;
		if(iOri == 1 && jOri == 0)
			return Orientation.S;
		else if(iOri == -1 && jOri == 0)
			return Orientation.N;
		else if(iOri == 0 && jOri == 1)
			return Orientation.E;
		else if(iOri == 0 && jOri == -1)
			return Orientation.W;
		return ori;
		
	}

	public void setOri(Orientation ori){
		this.ori = ori;
	}

	public int getCapacity(){
		return capacity;
	}
}
