package bluetooth;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import javax.swing.*;


public class BluetoothConnection {

    private String nomRobot;

    public NXTComm Connexion() throws NXTCommException {
        NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
        String nomRobot = (String) JOptionPane.showInputDialog(null,"Entrez le nom du robot :","Robot", JOptionPane.QUESTION_MESSAGE,null,null,"");
        NXTInfo[] nxtInfo = nxtComm.search(nomRobot);
        if (nxtInfo.length == 0) {
            System.out.println("No nxt found");
            System.exit(1);
        }
        nxtComm.open(nxtInfo[0]);
        this.nomRobot = nxtInfo[0].name;
        System.out.println("Connecte a : "+ this.nomRobot + ", addresse : "  + nxtInfo[0].deviceAddress);
        return nxtComm;
    }

    public String getNomRobot() { return this.nomRobot; }
}
