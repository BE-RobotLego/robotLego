package main;

import bluetooth.BluetoothConnection;
import cases.Graphe;
import cases.Parcours;
import ia.Ambulance;
import lecture.LectureDeuxRobots;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import robot.Robot;

import java.io.IOException;

public class MainDualRobots {
    public static void main(String[] args) throws NXTCommException, InterruptedException, IOException {

        BluetoothConnection bC = new BluetoothConnection();
        NXTComm nxtComm = bC.Connexion();

        BluetoothConnection bC2 = new BluetoothConnection();
        NXTComm nxtComm2 = bC2.Connexion();

        RemoteMotor rmB = new RemoteMotor(new NXTCommand(nxtComm), 1);
        RemoteMotor rmC = new RemoteMotor(new NXTCommand(nxtComm), 2);

        RemoteMotor rmB2 = new RemoteMotor(new NXTCommand(nxtComm2), 1);
        RemoteMotor rmC2 = new RemoteMotor(new NXTCommand(nxtComm2), 2);

        LectureDeuxRobots lecteur = new LectureDeuxRobots(bC.getNomRobot(),bC2.getNomRobot());
        if(lecteur.readFile() == 0) {
            Parcours parcours = lecteur.initParcours();
            Robot robotIA = lecteur.getRobot_0();
            Robot robotH = lecteur.getRobot_1();
            telecommand(rmB2, rmC2, nxtComm2, robotH, parcours);
            IA(rmB, rmC, nxtComm, robotIA, parcours, robotH);
        }
    }

    private static void IA(RemoteMotor rmB,RemoteMotor rmC,NXTComm nxtComm, Robot robot,Parcours parcours,Robot robotH) throws IOException, InterruptedException {
        Graphe graphe = new Graphe(parcours);
        Ambulance ambulance = new Ambulance(robot,parcours,graphe,rmB,rmC,nxtComm,true,robotH);
        ambulance.comportementAmbulance();
    }

    private static void telecommand(RemoteMotor rmB,RemoteMotor rmC,NXTComm nxtComm,Robot bot,Parcours parcours){
        MainTelecommande.createTelecommand(nxtComm, rmB, rmC, parcours, bot);
    }

}
