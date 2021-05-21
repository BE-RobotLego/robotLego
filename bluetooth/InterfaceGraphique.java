package bluetooth;

import cases.Case;
import cases.Parcours;
import deplacements.Deplacements;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import robot.*;
import robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InterfaceGraphique extends JFrame {
    JPanel mainPanel;
    JPanel panelBoutonsDep;

    JPanel panelActions;
    JPanel panelBoutonsActions;
    JPanel panelAffichageErreur;


    /////// Bouttons Deplacements////////
    JButton haut ;
    JButton gauche ;
    JButton droite;


    //////////Boutons Spéciaux //////////
    JButton recalibrage = new JButton("recalibrage");
    JButton demiTour;
    JButton transiBlanc = new JButton("transiBlanc");
    JButton deposer ;
    JButton ramasser;

    JPanel panelvide1 = new JPanel();
    JPanel panelvide2 = new JPanel();
    JPanel panelvide3 = new JPanel();
    JLabel posiBot = new JLabel();
    JLabel imBot = new JLabel("BOT");
    JLabel erreurs = new JLabel();

    Deplacements dIA;

    private RemoteMotor rmB;
    private RemoteMotor rmC;
    private NXTComm nxtComm;
    private Robot bot;
    private Parcours p;

    public InterfaceGraphique(RemoteMotor rmB, RemoteMotor rmC, NXTComm nxtComm,Robot bot, Parcours p) {
        this.rmB = rmB;
        this.rmC = rmC;
        this.nxtComm = nxtComm;
        this.bot = bot;
        this.p = p;
        dIA = new Deplacements(rmB,rmC);

        mettresIcons();

        mainPanel = new JPanel(new GridLayout(2,1));
        panelBoutonsDep= new JPanel(new GridLayout(3,3));

        DeplacementPanel();
        EnabledDisabled();

        mainPanel.add(panelBoutonsDep);
        ActionPanel();

        this.add(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void mettresIcons(){

        ImageIcon icon1 = new ImageIcon("../Icon/Haut.png");
        Image scaleImage1 = icon1.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT);
        icon1 = new ImageIcon(scaleImage1);
        haut=new JButton(icon1);

        ImageIcon icon2 = new ImageIcon("../Icon/Gauche.png");
        Image scaleImage2 = icon2.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT);
        icon2 = new ImageIcon(scaleImage2);
        gauche=new JButton(icon2);

        ImageIcon icon3 = new ImageIcon("../Icon/Droite.png");
        Image scaleImage3 = icon3.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT);
        icon3 = new ImageIcon(scaleImage3);
        droite=new JButton(icon3);

        ImageIcon icon4 = new ImageIcon("../Icon/Demi.png");
        Image scaleImage4 = icon4.getImage().getScaledInstance(40, 40,Image.SCALE_DEFAULT);
        icon4 = new ImageIcon(scaleImage4);
        demiTour=new JButton(icon4);

    }

    public void ActionPanel ( ){
        panelActions =new JPanel(new GridLayout(2,1));
        panelBoutonsActions= new JPanel(new GridLayout(1,2));
        panelAffichageErreur =new JPanel();


        ramasser= new JButton("ramasser");
        deposer =new JButton("deposer");

        ramasser.setPreferredSize(new Dimension(125,100));
        deposer.setPreferredSize(new Dimension(125,100));


        // Ajout des boutons

        ramasser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bot.pickUp(p);
            }
        });
        deposer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bot.dropOut(p);
            }
        });

        JPanel intermediaireRamasser =new JPanel();
        intermediaireRamasser.add(ramasser);

        JPanel intermediaireDeposer =new JPanel();
        intermediaireDeposer.add(deposer);

        panelBoutonsActions.add(intermediaireRamasser);
        panelBoutonsActions.add(intermediaireDeposer);

        //todo ajout du texte


        erreurs.setFont(new Font("Serif",Font.CENTER_BASELINE,18));
        String posiBotText = "Je suis dans les starting-blocks";
        erreurs.setText(posiBotText);
        erreurs.setForeground(Color.red);
        erreurs.setHorizontalAlignment(SwingConstants.CENTER);
        panelAffichageErreur.setLayout(new BorderLayout());
        panelAffichageErreur.add(erreurs,BorderLayout.CENTER);



        panelActions.add(panelBoutonsActions);
        panelActions.add(panelAffichageErreur);

        mainPanel.add(panelActions);
    }

    public void DeplacementPanel(){
        haut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    toutDroitListenner();
                    EnabledDisabled();
                    System.out.println(bot.getName() + " est en : " + bot.getiCurr() + " / " + bot.getjCurr());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        gauche.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viragesListener("Gauche");
                    EnabledDisabled();
                    System.out.println(bot.getName() + " est en : " + bot.getiCurr() + " / " + bot.getjCurr());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        droite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viragesListener("Droite");
                    EnabledDisabled();
                    System.out.println(bot.getName() + " est en : " + bot.getiCurr() + " / " + bot.getjCurr());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        recalibrage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dIA.Recalibrage(nxtComm);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        transiBlanc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dIA.DepTransiblanc();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        demiTour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dIA.DepDemiTour();
                    updatePosition("Demi-tour");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });


        Color couleursfond = new Color(238,238,238);
        panelvide1.setBackground(couleursfond);
        panelvide2.setBackground(couleursfond);
        panelvide3.setBackground(couleursfond);

        posiBot.setFont(new Font("Serif",Font.CENTER_BASELINE,18));
        imBot.setFont(new Font("Serif",Font.CENTER_BASELINE,18));
        String posiBotText = "Position: "+bot.getiCurr()+","+bot.getjCurr();
        posiBot.setText(posiBotText);
        posiBot.setForeground(Color.red);
        imBot.setForeground(Color.red);
        imBot.setHorizontalAlignment(SwingConstants.CENTER);
        posiBot.setHorizontalAlignment(SwingConstants.CENTER);
        panelvide1.setLayout(new BorderLayout());
        panelvide1.add(imBot, BorderLayout.NORTH);
        panelvide1.add(posiBot,BorderLayout.CENTER);


        JPanel intermediaire1 = new JPanel();
        JPanel intermediaire2 = new JPanel();
        JPanel intermediaire3 = new JPanel();
        JPanel intermediaire4 = new JPanel();
        JPanel intermediaire5 = new JPanel();
        JPanel intermediaire6 = new JPanel();
        intermediaire1.add(recalibrage);
        intermediaire2.add(haut);
        intermediaire3.add(transiBlanc);
        intermediaire4.add(gauche);
        intermediaire5.add(droite);
        intermediaire6.add(demiTour);

        panelBoutonsDep.add(intermediaire1);
        panelBoutonsDep.add(intermediaire2);
        panelBoutonsDep.add(intermediaire3);

        panelBoutonsDep.add(intermediaire4);
        panelBoutonsDep.add(panelvide1);
        panelBoutonsDep.add(intermediaire5);

        panelBoutonsDep.add(panelvide2);
        panelBoutonsDep.add(intermediaire6);
        panelBoutonsDep.add(panelvide3);


    }

    //////les listrenner

    private void viragesListener(String directionEntree) throws IOException, InterruptedException {
        Orientation oriRobot = bot.getOriRobot();
        Case curr = bot.getCurrCase(p);
        bot.getCurrCase(p).getDirCase();
        if (curr.is3way()){
            switch(oriRobot){
                case E:
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        erreurs.setText("J'ai tourné à gauche");
                        dIA.slipGauche(nxtComm);
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        if (directionEntree.equals("Droite")){
                            erreurs.setText("J'ai tourné à droite");
                            dIA.slipDroite(nxtComm);
                            updatePosition("SlipDroit");
                        }
                        else {
                            erreurs.setText("J'ai tourné à gauche");
                            dIA.slipGauche(nxtComm);
                            updatePosition("SlipGauche");
                        }
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à droite");
                        dIA.slipDroite(nxtComm);
                        updatePosition("SlipDroit");
                    }
                    break;
                case N:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à droite");
                        dIA.slipDroite(nxtComm);
                        updatePosition("SlipDroit");
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à gauche");
                        dIA.slipGauche(nxtComm);
                        updatePosition("SlipGauche");

                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        if (directionEntree.equals("Droite")){
                            erreurs.setText("J'ai tourné à droite");
                            dIA.slipDroite(nxtComm);
                            updatePosition("SlipDroit");
                        }
                        else {
                            erreurs.setText("J'ai tourné à gauche");
                            dIA.slipGauche(nxtComm);
                            updatePosition("SlipGauche");
                        }
                    }
                    break;
                case W:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        if (directionEntree.equals("Droite")){
                            erreurs.setText("J'ai tourné à droite");
                            dIA.slipDroite(nxtComm);
                            updatePosition("SlipDroit");
                        }
                        else {
                            erreurs.setText("J'ai tourné à gauche");
                            dIA.slipGauche(nxtComm);
                            updatePosition("SlipGauche");
                        }
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        erreurs.setText("J'ai tourné à droite");
                        dIA.slipDroite(nxtComm);
                        updatePosition("SlipDroit");
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à gauche");
                        dIA.slipGauche(nxtComm);
                        updatePosition("SlipGauche");
                    }
                    break;
                case S:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à gauche");
                        dIA.slipGauche(nxtComm);
                        updatePosition("SlipGauche");
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        if (directionEntree.equals("Droite")){
                            erreurs.setText("J'ai tourné à droite");
                            dIA.slipDroite(nxtComm);
                            updatePosition("SlipDroit");
                        }
                        else {
                            erreurs.setText("J'ai tourné à gauche");
                            dIA.slipGauche(nxtComm);
                            updatePosition("SlipGauche");
                        }
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        erreurs.setText("J'ai tourné à droite");
                        dIA.slipDroite(nxtComm);
                        updatePosition("SlipDroit");
                    }
                    break;
            }

        }
        else {
            if (curr.isVirage()){
                erreurs.setText("J'ai tourné dans le sens du virage");
                dIA.DepVirage(nxtComm);
                updatePosition("Virage");

            }
            else {
                System.out.println("Il n'y as pas de virage ici");
                erreurs.setText("Vous ne pouvez pas faire cette action");
            }

        }

    }


    private void toutDroitListenner () throws IOException, InterruptedException {
        Orientation oriRobot = bot.getOriRobot();
        Case curr = bot.getCurrCase(p);
        bot.getCurrCase(p).getDirCase();
        if (curr.is3way()){
            switch (oriRobot){
                case E:
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitGauche(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitDroite(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    break;
                case N:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitDroite(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitGauche(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    break;
                case W:
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitDroite(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitGauche(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    break;
                case S:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitGauche(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        erreurs.setText("Je suis allé tout droit");
                        dIA.slipDroitDroite(nxtComm);
                        updatePosition("ToutDroit");
                    }
                    break;
            }

        }
        else{
            if (curr.isDroite()){
                erreurs.setText("Je suis allé tout droit");
                dIA.DepDroit(nxtComm);
                updatePosition("ToutDroit");

            }
            else {
                System.out.println("Il n'y as pas de droite ici");
                erreurs.setText("Vous ne pouvez pas effectuer cette action");
            }
        }
    }


   // permet de mettre à jour la position robot en fonction de l'orientation et des fonctions de déplacement
    public void updatePosition(String action) {
        Case castwa = bot.getCurrCase(p);
        Orientation orient = bot.getOriRobot();
        switch (orient) {
            case S:
                if (action == "Demi-tour") {
                    bot.updatePosition(bot.getiCurr()-1, bot.getjCurr());
                    break;
                }else if (action == "SlipDroit") {
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr() - 1);
                    break;
                }else if (action == "SlipGauche") {
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()+1);
                    break;
                }else if (action == "SlipDroitDroite" || action == "SlipDroitGauche" || castwa.isDroite() ){
                    bot.updatePosition(bot.getiCurr() + 1, bot.getjCurr());
                    break;
                }
                //virage droit
                if ((castwa.contientDir('U') && castwa.contientDir('L'))) {
                    System.out.println("Je passe par S droit");
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()-1);
                    break;
                    //virage gauche
                }else if (castwa.contientDir('R') && castwa.contientDir('U')){
                    System.out.println("Je passe par S gauche");
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()+1);
                    break;
                }
                break;

            case N:
                if (action == "Demi-tour") {
                    bot.updatePosition(bot.getiCurr()+1, bot.getjCurr());
                    break;
                }else if (action == "SlipDroit") {
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr() + 1);
                    break;
                }else if (action == "SlipGauche") {
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()-1);
                    break;
                }else if (action == "SlipDroitDroite" || action == "SlipDroitGauche" || castwa.isDroite() ){
                    bot.updatePosition(bot.getiCurr() - 1, bot.getjCurr());
                    break;
                }
                //virage droit
                if ((castwa.contientDir('D') && castwa.contientDir('R'))) {
                    System.out.println("Je passe par N droit");
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()+1);
                    break;
                    //virage gauche
                }else if (castwa.contientDir('L') && castwa.contientDir('D')){
                    System.out.println("Je passe par N gauche");
                    bot.updatePosition(bot.getiCurr(),bot.getjCurr()-1);
                    break;
                }
                break;

            case W:
                if (action == "Demi-tour") {
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr()+1);
                    break;
                }else if (action == "SlipDroit") {
                    bot.updatePosition(bot.getiCurr()-1, bot.getjCurr());
                    break;
                }else if (action == "SlipGauche") {
                    bot.updatePosition(bot.getiCurr()+1,bot.getjCurr());
                    break;
                }else if (action == "SlipDroitDroite" || action == "SlipDroitGauche" || castwa.isDroite() ){
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr()-1);
                    break;
                }
                //virage droit
                if ((castwa.contientDir('U') && castwa.contientDir('R'))) {
                    System.out.println("Je passe par W droit");
                    bot.updatePosition(bot.getiCurr()-1,bot.getjCurr());
                    break;
                    //virage gauche
                }else if (castwa.contientDir('R') && castwa.contientDir('D')){
                    System.out.println("Je passe par W gauche");
                    bot.updatePosition(bot.getiCurr()+1,bot.getjCurr());
                    break;
                }
                break;

            case E:
                if (action == "Demi-tour") {
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr()-1);
                    break;
                }else if (action == "SlipDroit") {
                    bot.updatePosition(bot.getiCurr()+1, bot.getjCurr());
                    break;
                }else if (action == "SlipGauche") {
                    bot.updatePosition(bot.getiCurr()-1,bot.getjCurr());
                    break;
                }else if (action == "SlipDroitDroite" || action == "SlipDroitGauche" || castwa.isDroite() ){
                    bot.updatePosition(bot.getiCurr(), bot.getjCurr()+1);
                    break;
                }
                //virage gauche
                else if ((castwa.contientDir('U') && castwa.contientDir('L'))) {
                    System.out.println("Je passe par E droit");
                    bot.updatePosition(bot.getiCurr()-1,bot.getjCurr());
                    break;
                    //virage droite
                }else if (castwa.contientDir('D') && castwa.contientDir('L')){
                    System.out.println("Je passe par E gauche");
                    bot.updatePosition(bot.getiCurr()+1,bot.getjCurr());
                    break;
                }
                break;
        }
        String posiBotText = "Position : "+bot.getiCurr()+","+bot.getjCurr();
        posiBot.setText(posiBotText);
    }

    //Enabled/Disabled les boutons en fonctions du type de case
    private void EnabledDisabled() {
        Orientation oriRobot = bot.getOriRobot();
        Case curr = bot.getCurrCase(p);
        bot.getCurrCase(p).getDirCase();
        haut.setEnabled(true);
        gauche.setEnabled(true);
        droite.setEnabled(true);
        if (curr.is3way()){
            switch(oriRobot){
                case E:
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        droite.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        haut.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        gauche.setEnabled(false);
                        break;
                    }
                    break;
                case N:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        gauche.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        droite.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        haut.setEnabled(false);
                        break;
                    }
                    break;
                case W:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        haut.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        gauche.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('L') && curr.contientDir('R') && curr.contientDir('D')){
                        droite.setEnabled(false);
                        break;
                    }
                    break;
                case S:
                    if (curr.contientDir('U') && curr.contientDir('R') && curr.contientDir('D')){
                        droite.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('R')){
                        haut.setEnabled(false);
                        break;
                    }
                    if (curr.contientDir('U') && curr.contientDir('L') && curr.contientDir('D')){
                        gauche.setEnabled(false);
                        break;
                    }
                    break;
            }

        }
        else if (curr.isVirage()){
                haut.setEnabled(false);
                switch(oriRobot){
                    case E:
                        if (curr.contientDir('U') && curr.contientDir('L')){
                            droite.setEnabled(false);
                            break;
                        }else if (curr.contientDir('D') && curr.contientDir('L')) {
                            gauche.setEnabled(false);
                            break;
                        }
                    case N:
                        if (curr.contientDir('D') && curr.contientDir('L')){
                            droite.setEnabled(false);
                            break;
                        }else if (curr.contientDir('D') && curr.contientDir('R')) {
                            gauche.setEnabled(false);
                            break;
                        }
                    case W:
                        if (curr.contientDir('U') && curr.contientDir('R')){
                            gauche.setEnabled(false);
                            break;
                        }else if (curr.contientDir('D') && curr.contientDir('R')) {
                            droite.setEnabled(false);
                            break;
                        }
                    case S:
                        if (curr.contientDir('U') && curr.contientDir('R')){
                            gauche.setEnabled(false);
                            break;
                        }else if (curr.contientDir('U') && curr.contientDir('L')) {
                            droite.setEnabled(false);
                            break;
                        }
                }
        }else if (curr.isDroite()){
                gauche.setEnabled(false);
                droite.setEnabled(false);
            }
    }

}
