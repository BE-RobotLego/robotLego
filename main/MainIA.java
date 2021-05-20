package main;

import bluetooth.BluetoothConnection;
import cases.Graphe;
import cases.Parcours;
import ia.Ambulance;
import ia.Dijkstra;
import lecture.Lecture;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import robot.Robot;
import java.io.IOException;



public class MainIA {
    public static void main (String[] args) throws NXTCommException, InterruptedException, IOException {

        BluetoothConnection bC = new BluetoothConnection();
        NXTComm nxtComm = bC.Connexion();

        RemoteMotor rmB = new RemoteMotor(new NXTCommand(nxtComm), 1);
        RemoteMotor rmC = new RemoteMotor(new NXTCommand(nxtComm), 2);

        Lecture l = new Lecture(bC.getNomRobot());
        if(l.readFile() == 0) {
            Parcours parcours = l.initParcours();
            Robot robot = l.initRobot();
            Graphe graphe = new Graphe(parcours);


            Ambulance a = new Ambulance(robot, parcours, graphe, rmB, rmC, nxtComm, false, null);

            System.out.println("*bip* *bip*");
            System.out.println("Quoi ?");
            System.out.println("Le LEGOPHONE ! LEGOPOTES EN AVANT !!");

            a.comportementAmbulance();

            System.out.println("Encore une journée de terminée. Les LEGOPOTES ont sauvé la journée");


        }
    }
}
