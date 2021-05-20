package main;

import bluetooth.BluetoothConnection;
import bluetooth.*;
import cases.Parcours;
import lecture.Lecture;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import robot.Robot;

import javax.swing.*;
import java.io.FileNotFoundException;

public class MainTelecommande {
    public static void main (String[] args) throws NXTCommException, FileNotFoundException, InterruptedException {

        BluetoothConnection bC = new BluetoothConnection();
        NXTComm nxtComm = bC.Connexion();

        RemoteMotor rmB = new RemoteMotor(new NXTCommand(nxtComm), 1);
        RemoteMotor rmC = new RemoteMotor(new NXTCommand(nxtComm), 2);

        Lecture l = new Lecture(bC.getNomRobot());
        if(l.readFile() == 0) {
            Parcours parcours = l.initParcours();
            Robot bot = l.initRobot();

            System.out.println("Position de "+bot.getName()+" au départ : i = " + bot.getiCurr() + " j= " + bot.getjCurr());
            createTelecommand(nxtComm, rmB, rmC, parcours, bot);
        }
    }

    public static void createTelecommand(NXTComm nxtComm, RemoteMotor rmB, RemoteMotor rmC, Parcours parcours, Robot bot) {
        JFrame f = new InterfaceGraphique(rmB, rmC, nxtComm, bot, parcours);
        f.setSize(500, 250);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent wE) {
                System.exit(1);
            }
        });
    }
}
