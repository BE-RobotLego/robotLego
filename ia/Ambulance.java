package ia;

import cases.Case;
import cases.Graphe;
import cases.Parcours;
import deplacements.Deplacements;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import robot.Robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


//Comportement du robot
public class Ambulance {
	private final Deplacements vroum;
	private final Robot robot;
	private final Parcours p;
	private final Graphe g;
	private int poidsChemin;
	private final NXTComm nxtComm;
	private final boolean is2player;
	private final Robot robotH;

	public Ambulance (Robot robot, Parcours p, Graphe g, RemoteMotor rmB, RemoteMotor rmC, NXTComm nxtComm, boolean is2player, Robot robotH){
		this.robot = robot;
		this.p = p;
		this.g = g;
		vroum = new Deplacements(rmB,rmC);
		this.nxtComm = nxtComm;
		this.is2player = is2player;
		this.robotH = robotH;
	}


	public void comportementAmbulance() throws IOException, InterruptedException {
		Case curr;
		while(p.getNbPatients() > 0 || robot.getCapacity() < 2){
			curr = robot.getCurrCase(p);
			//Si l'ambulance est vide
			if(robot.getCapacity() == 2){
				goToPatient(curr);
			}

			/*Si l'ambulance a de la place et qu'il y a d'autres patients a sauver
			On va alors chercher si il est plus rapide d'ammener le patient à l'hopital
			et d'aller chercher le deuxième après ou alors
			aller chercher le deuxième puis aller à l'hopital*/
			else if(robot.getCapacity() == 1 && p.getNbPatients()>0){
				PHorHP(curr);
			}

			//Si il n'y a plus de patiens ou que le robot n'a plus de places
			else if(p.getNbPatients() == 0 || robot.getCapacity() == 0){
				goToHopital(curr);
			}
		}
	}


	private void PHorHP(Case curr) throws IOException, InterruptedException {
		ArrayList<ArrayList<Case>> ph = new ArrayList<>();
		ArrayList<ArrayList<Case>> hp = new ArrayList<>();
		ArrayList<Case> way;
		int poidsPH = 0;
		int poidsHP = 0;
		Case arrivee;

		//PH
		way = getShortestWayToPatient(curr);
		ph.add(way);
		arrivee = way.get(way.size()-1);
		poidsPH += poidsChemin;
		way = getShortestWayToHopital(arrivee);
		ph.add(way);
		poidsPH += poidsChemin;

		//HP
		way = getShortestWayToHopital(curr);
		hp.add(way);
		arrivee = way.get(way.size()-1);
		poidsHP += poidsChemin;
		way = getShortestWayToPatient(arrivee);
		hp.add(way);
		poidsHP += poidsChemin;


		if(poidsPH<poidsHP)
			execPH(ph,curr);
		else
			execHP(hp,curr);
	}

	private void execPH(ArrayList<ArrayList<Case>> ph, Case curr) throws IOException, InterruptedException {
		Chemin c;
		for(int i = 0;i<ph.size();i++){
			c = new Chemin(ph.get(i),robot,curr);
			if(execInstructions(c.getOrdre(),c.getInstructions()) == 0){
				curr = robot.getCurrCase(p);
				if(i==0) robot.pickUp(p);
				if(i==1) robot.dropOut(p);
			}
			//Si les instructions ne marchent pas, on break pour ne pas essayer d'exec les suivantes
			else
				break;
		}
	}

	private void execHP(ArrayList<ArrayList<Case>> hp,Case curr) throws IOException, InterruptedException {
		Chemin c;
		for(int i = 0;i<hp.size();i++){
			c = new Chemin(hp.get(i),robot,curr);
			if(execInstructions(c.getOrdre(),c.getInstructions()) == 0) {
				curr = robot.getCurrCase(p);
				if (i == 0) robot.dropOut(p);
				if (i == 1) robot.pickUp(p);
			}
			//Si les instructions ne marchent pas, on break pour ne pas essayer d'exec les suivantes
			else
				break;
		}
	}

	private void goToPatient(Case curr) throws IOException, InterruptedException {
		Chemin c = new Chemin(getShortestWayToPatient(curr), robot, curr);
		if(execInstructions(c.getOrdre(),c.getInstructions()) == 0)
			robot.pickUp(p);
	}

	private void goToHopital(Case curr) throws IOException, InterruptedException {
		Chemin c = new Chemin(getShortestWayToHopital(curr), robot, curr);
		if(execInstructions(c.getOrdre(),c.getInstructions()) == 0)
			robot.dropOut(p);
	}




	private int execInstructions(ArrayList<Case> ordre, ArrayList<String> instructions) throws InterruptedException, IOException {
		int pointer = 0;
		int attente;
		Case dest;
		for(String instruction : instructions){
			dest = ordre.get(pointer);
			if(is2player) {
				attente = 0;
				//Si le joueur est sur la case où l'on veut se rendre, on attend
				while (dest.equals(robotH.getCurrCase(p)) || dest.equals(robotH.getLastCase(p))) {
					if(attente >= 5){
						//Ici, on ne met pas Integer.MAX_VALUE sinon Dijkstra ne pourra plus être calculé
						//On met donc une valeur élevée (ici 50000)
						p.getCaseAt(dest.getI(),dest.getJ()).setPoids(50000);
						return 1;//on quitte la fonction pour recalculer un itinéraire
					}
					System.out.println(robot.getName()+" : Chemin bloqué par "+robotH.getName()+"... J'attends");
					TimeUnit.SECONDS.sleep(1);
					attente++;
				}
			}
			//Si le patient/l'hopital est toujours présent sur la case destination
			//Si il ne l'est plus on va supprimer le chemin et le robot va calculer un nouveau chemin
			if(ordre.get(ordre.size()-1).hasPatient() || ordre.get(ordre.size()-1).hasHopital()) {
				System.out.println(instruction);
				//Si on fait un demi tour, on va prendre la derniere case visitée comme coordonnée
				//On va néanmoins s'avancer un peu pour pouvoir mieux se placer dans la case
				if (instruction.contains("Demi-Tour")) {
					vroum.DepTransiblanc();
					vroum.DepDemiTour();
					vroum.Recalibrage(nxtComm);
					robot.updatePosition(robot.getLastI(), robot.getLastJ());
				}
				//Sinon on va executer la fonction correspondante et update la position
				else {
					switch (instruction) {
						case "Ligne Droite":
							vroum.DepDroit(nxtComm);
							break;
						case "Virage":
							vroum.DepVirage(nxtComm);
							break;
						case "Slip Droit":
							vroum.slipDroite(nxtComm);
							break;
						case "Slip Gauche":
							vroum.slipGauche(nxtComm);
							break;
						case "Slip Droit Gauche":
							vroum.slipDroitGauche(nxtComm);
							break;
						case "Slip Droit Droite":
							vroum.slipDroitDroite(nxtComm);
							break;
					}

					robot.updatePosition(dest.getI(), dest.getJ());
					System.out.println(robot.getName() + " est en : " + robot.getiCurr() + " / " + robot.getjCurr());
					pointer++;
				}
				TimeUnit.SECONDS.sleep(1);
			}
			else{
				instructions.remove(instruction);
				return 1;
			}
		}
		return 0;
	}

	public ArrayList<Case> getShortestWayToPatient(Case curr){
		ArrayList<Case> shortestOrdre = null;
		int poidsMin = 50000;
		ArrayList<Case> patients = p.getPatientList();
		Dijkstra d;
		for(Case c : patients){
			d = new Dijkstra(g.getGraphe(),curr,c,robot.getLastCase(p));
			if(d.getPoidsChemin() < poidsMin){
				shortestOrdre = d.getOrdre();
				poidsMin = d.getPoidsChemin();
			}
		}
		poidsChemin = poidsMin;
		return shortestOrdre;
	}

	public ArrayList<Case> getShortestWayToHopital(Case curr){
		ArrayList<Case> shortestOrdre = null;
		int poidsMin = 50000;
		ArrayList<Case> hopitaux = p.getHopitalList();
		Dijkstra d;
		for(Case c : hopitaux){
			d = new Dijkstra(g.getGraphe(),curr,c,robot.getLastCase(p));
			if(d.getPoidsChemin() < poidsMin){
				shortestOrdre = d.getOrdre();
				poidsMin = d.getPoidsChemin();
			}
		}
		poidsChemin = poidsMin;
		return shortestOrdre;
	}
}
