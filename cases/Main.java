package cases;

import bluetooth.BluetoothConnection;
import ia.Ambulance;
import ia.Dijkstra;
import lecture.Lecture;
import lecture.LectureDeuxRobots;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import main.MainTelecommande;
import robot.Robot;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws NXTCommException, InterruptedException, IOException {
		LectureDeuxRobots lecteur = new LectureDeuxRobots("IA","Tele");
		if(lecteur.readFile() == 0) {
			Parcours parcours = lecteur.initParcours();
			Robot robotIA = lecteur.getRobot_0();
			Robot robotH = lecteur.getRobot_1();
			telecommand(null, null, null, robotH, parcours);
			IA(null, null, null, robotIA, parcours, robotH);
		}
	}

	private static void IA(RemoteMotor rmB,RemoteMotor rmC,NXTComm nxtComm, Robot robot,Parcours parcours,Robot robotH) throws IOException, InterruptedException {
		Graphe graphe = new Graphe(parcours);
		Ambulance ambulance = new Ambulance(robot,parcours,graphe,rmB,rmC,nxtComm,true,robotH);
		ambulance.comportementAmbulance();
	}

	private static void telecommand(RemoteMotor rmB,RemoteMotor rmC,NXTComm nxtComm,Robot bot,Parcours parcours){
		MainTelecommande.createTelecommand(nxtComm, rmC, rmC, parcours, bot);
	}
}
