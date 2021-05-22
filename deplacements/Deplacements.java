package deplacements;

import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.NXTProtocol;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;



import java.io.IOException;

public class Deplacements {

    private RemoteMotor rmB;
    private RemoteMotor rmC;

    
    /////////////// les vitesses des differents moteurs ///////////////
    
    // vissese pendant virage
    private final int FastVirage = 38;
    private final int NormalVirage = 16;
    private final int LowVirage = 13;

    // vitesses pendant lees droites 
    private final int FastDroite = 35;
    private final int LowDroite = 25;
    private final int NormalDroite = 26 ;

    // vitesses pendant les intersections
    private final int FastSlipVirage = 28;
    private final int MediumSlipVirage = 20;
    private final int LowSlipVirage = 13;

    //bon pour droitDroit : 35 30 20 30
    private final int FastSlipDroit = 27 ;
    private final int MediumSlipDroit = 25;
    private final int LowSlipDroit = 20;

    private final int SpeedRecalibrage = 14;

    //Valeur de transition entre le blanc et le noir
    private final int blackWhiteThreshold = 625;

    
    
    
    /////////////// appel des fonction generiques ///////////////
    
    public void DepVirage(NXTComm nxtComm) throws IOException, InterruptedException {
        VirageDroite(nxtComm, FastVirage, LowVirage, NormalVirage);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }

    public void DepDroit(NXTComm nxtComm) throws IOException, InterruptedException {
        VirageDroite(nxtComm, FastDroite, LowDroite, NormalDroite);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }

    public void slipGauche(NXTComm nxtComm) throws IOException, InterruptedException {
        slipVirage(nxtComm,3,2, LowSlipVirage, MediumSlipVirage, FastSlipVirage);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }

    public void slipDroite(NXTComm nxtComm) throws IOException, InterruptedException {
        slipVirage(nxtComm,2,3, FastSlipVirage, MediumSlipVirage, LowSlipVirage);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }

    // aller tout droit dans les intersections 
    public void slipDroitDroite(NXTComm nxtComm) throws IOException, InterruptedException {
        // capteur droit compte le blanc
        slipToutDroit(nxtComm,3,2,LowSlipDroit, LowSlipDroit, MediumSlipDroit,FastSlipDroit);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }
    public void slipDroitGauche(NXTComm nxtComm) throws IOException, InterruptedException {
        // capteur gauche compte le blanc
        slipToutDroit(nxtComm,2,3,FastSlipDroit, MediumSlipDroit, LowSlipDroit,LowSlipDroit);
        DepTransiblanc();
        Recalibrage(nxtComm);
    }

    public Deplacements(RemoteMotor rmB, RemoteMotor rmC) {
        this.rmB = rmB;
        this.rmC = rmC;
    }

    public void DepDemiTour () throws InterruptedException {
        rmB.setPower(50);
        rmC.setPower(50);
        rmB.backward();
        rmC.forward();
        Thread.sleep(970);
        rmC.stop();
        rmB.stop();
    }
    
    // fonction de transition entre deux cases 
    public void DepTransiblanc () throws InterruptedException {
        rmB.setPower(30);
        rmC.setPower(30);
        rmB.forward();
        rmC.forward();
        Thread.sleep(500);
        rmC.stop();
        rmB.stop();
    }

    // recalibrage du robot pour commencer les cases plus droit 
    public void Recalibrage(NXTComm nxtComm) throws IOException {
        NXTCommand nxtCommand = new NXTCommand(nxtComm);
        nxtCommand.setInputMode(2,NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        nxtCommand.setInputMode(3, NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        rmC.setPower(SpeedRecalibrage);
        rmB.setPower(SpeedRecalibrage);
        int lower = SpeedRecalibrage;
        while (nxtCommand.getInputValues(2).rawADValue<blackWhiteThreshold){ // Si un capteur voit du blanc 
            rmB.backward();
            rmC.forward();
            lower -= 1;
            rmC.setPower(lower);
            rmB.setPower(lower);
        }
        rmC.stop();
        rmB.stop();
        lower  = SpeedRecalibrage;
        while (nxtCommand.getInputValues(3).rawADValue<blackWhiteThreshold){ // si le dexieme capteur vois du blanc 
            rmB.forward();
            rmC.backward();
            lower -= 1;
            rmC.setPower(lower);
            rmB.setPower(lower);
        }
        rmC.stop();
        rmB.stop();
    }

    
    // une seul fonction pour aller tout droit et prendre les virages simples
    // les vitesse change quand on appel la fontion en depend de ce que on veut faire
    private void VirageDroite(NXTComm nxtComm,int SpeedVirageFast ,int SpeedVirageLow, int SpeedNormal) throws InterruptedException, IOException {
        NXTCommand nxtCommand = new NXTCommand(nxtComm);
        nxtCommand.setInputMode(2,NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        nxtCommand.setInputMode(3, NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        int valLightRight;
        int valLightLeft;
        Thread.sleep(10);
        
        // Boucle tant que les deux capteurs ne voient pas du blanc en memem temps 
        while (nxtCommand.getInputValues(3).rawADValue >blackWhiteThreshold || nxtCommand.getInputValues(2).rawADValue  >blackWhiteThreshold){
            valLightRight = nxtCommand.getInputValues(2).rawADValue;
            valLightLeft = nxtCommand.getInputValues(3).rawADValue;
            if (valLightRight> blackWhiteThreshold && valLightLeft > blackWhiteThreshold) { 
                // les deux moteurs a la meme vitesse 
                rmB.setPower(SpeedNormal);
                rmC.setPower(SpeedNormal);
                rmB.forward();
                rmC.forward();
            }
            else {
                if (valLightRight<blackWhiteThreshold){
                    // les moteurs a des vitesses diferentes pour Tourner
                    rmB.setPower(SpeedVirageLow);
                    rmC.setPower(SpeedVirageFast);
                    rmB.forward();
                    rmC.forward();
                }
                else {
                    if(valLightLeft<blackWhiteThreshold){
                        // Tourner de lautre cote
                        rmB.setPower(SpeedVirageFast);
                        rmC.setPower(SpeedVirageLow);
                        rmB.forward();
                        rmC.forward();
                    }
                }
            }
        }
        rmC.stop();
        rmB.stop();
    }


    // aller a droite ou a gauche dans les cases intersections 
    private void slipVirage(NXTComm nxtComm,int port2, int port3, int SpeedVirageFast, int SpeedVirageMedium ,int SpeedVirageLow) throws InterruptedException, IOException {
        NXTCommand nxtCommand = new NXTCommand(nxtComm);
        nxtCommand.setInputMode(port2,NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        nxtCommand.setInputMode(port3, NXTProtocol.LIGHT_ACTIVE,NXTProtocol.RAWMODE);
        int valLightRight;
        int valLightLeft;
        int compteur =0;
        boolean estBlanc = false;
        Thread.sleep(10);
        if (nxtCommand.getInputValues(port3).rawADValue < blackWhiteThreshold) {
            compteur--;
        }
        while (compteur<3){ // un compteur car le capteur doit voir 3 fois une ligne blanche
            while (nxtCommand.getInputValues(port2).rawADValue < blackWhiteThreshold && compteur<3){ 
                rmB.setPower(SpeedVirageLow);
                rmC.setPower(SpeedVirageFast);
                rmB.forward();
                rmC.forward();
                if (nxtCommand.getInputValues(port3).rawADValue > blackWhiteThreshold){
                    estBlanc = false;
                }
                if (nxtCommand.getInputValues(port3).rawADValue < blackWhiteThreshold && !estBlanc){
                    estBlanc = true;
                    compteur++;
                }
                if (compteur >= 3){ // arret des moteurs car fin de la fonction 
                    rmC.stop();
                    rmB.stop();
                }
            }
            valLightRight = nxtCommand.getInputValues(port2).rawADValue;
            valLightLeft = nxtCommand.getInputValues(port3).rawADValue;
            if (valLightLeft>blackWhiteThreshold && valLightRight >blackWhiteThreshold && compteur<3){
                rmB.setPower(SpeedVirageMedium);
                rmC.setPower(SpeedVirageMedium);
                rmB.forward();
                rmC.forward();
            }
            else if (valLightRight<blackWhiteThreshold && compteur<3){
                rmB.setPower(SpeedVirageLow);
                rmC.setPower(SpeedVirageFast);
                rmB.forward();
                rmC.forward();
            }
            if (valLightLeft < blackWhiteThreshold && !estBlanc){
                estBlanc = true;
                compteur++;
            }
            if (valLightLeft > blackWhiteThreshold){
                estBlanc = false;
            }
            if (compteur >= 3 ){
                rmC.stop();
                rmB.stop();
            }
        }
        rmC.stop();
        rmB.stop();
    }

    
    
    // aller tout droit lors des passages des intersections 
    //fonction de principe similaire au virege dur  ant les intersections 
    private void slipToutDroit(NXTComm nxtComm,int port2, int port3, int SpeedVirageFast, int SpeedVirageMedium ,int SpeedVirageLow, int SpeedVirageLow2) throws InterruptedException, IOException {
        NXTCommand nxtCommand = new NXTCommand(nxtComm);
        nxtCommand.setInputMode(port2, NXTProtocol.LIGHT_ACTIVE, NXTProtocol.RAWMODE);
        nxtCommand.setInputMode(port3, NXTProtocol.LIGHT_ACTIVE, NXTProtocol.RAWMODE);
        int valLightRight;
        int valLightLeft;
        int compteur = 0;
        boolean estBlanc = false;
        Thread.sleep(10);
        if (nxtCommand.getInputValues(port3).rawADValue < blackWhiteThreshold) {
            compteur--;
        }
        while (compteur<3){
                while (nxtCommand.getInputValues(port2).rawADValue < blackWhiteThreshold && compteur<3){
                    rmB.setPower(SpeedVirageLow);
                    rmC.setPower(SpeedVirageMedium);
                    rmB.forward();
                    rmC.forward();
                    if (nxtCommand.getInputValues(port3).rawADValue > blackWhiteThreshold){
                        estBlanc = false;
                    }
                    if (nxtCommand.getInputValues(port3).rawADValue < blackWhiteThreshold && !estBlanc){
                        estBlanc = true;
                        compteur++;
                    }
                    if (compteur >= 3){
                        rmC.stop();
                        rmB.stop();
                    }
            }
            valLightRight = nxtCommand.getInputValues(port2).rawADValue;
            valLightLeft = nxtCommand.getInputValues(port3).rawADValue;
            if (valLightLeft>blackWhiteThreshold && valLightRight >blackWhiteThreshold && compteur<3){
                rmB.setPower(SpeedVirageFast);
                rmC.setPower(SpeedVirageLow2);
                rmB.forward();
                rmC.forward();
            }
            else if (valLightRight>blackWhiteThreshold && compteur<3){
                rmB.setPower(SpeedVirageFast);
                rmC.setPower(SpeedVirageLow2);
                rmB.forward();
                rmC.forward();
            }
            if (valLightLeft < blackWhiteThreshold && !estBlanc){
                estBlanc = true;
                compteur++;
            }
            if (valLightLeft > blackWhiteThreshold){
                estBlanc = false;
            }
            if (compteur >= 3 ){
                rmC.stop();
                rmB.stop();
            }
        }
        rmC.stop();
        rmB.stop();
    }

}
