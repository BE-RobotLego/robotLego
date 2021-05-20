package lecture;

import cases.Parcours;
import robot.Orientation;
import robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LectureDeuxRobots extends Component {

    private int iRobot_0;
    private int iRobot_1;
    private int jRobot_0;
    private int jRobot_1;
    private Orientation oriRobot_0;
    private Orientation oriRobot_1;
    private final String name0;
    private final String name1;
    private String[][] dir;
    private int lignes;
    private int colonnes;
    private int nb_patients;

    public LectureDeuxRobots(String name0, String name1) {
        this.name0 = name0;
        this.name1 = name1;
    }

    public int readFile() throws FileNotFoundException, InterruptedException {
        Scanner scanner;

        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();

            scanner = new Scanner(fichier);

            iRobot_0 = Integer.parseInt(scanner.nextLine());
            jRobot_0 = Integer.parseInt(scanner.nextLine());
            String ori = scanner.nextLine();
            switch (ori) {
                case "Sud":
                    oriRobot_0 = Orientation.S;
                    break;
                case "Nord":
                    oriRobot_0 = Orientation.N;
                    break;
                case "Est":
                    oriRobot_0 = Orientation.E;
                    break;
                case "Ouest":
                    oriRobot_0 = Orientation.W;
                    break;
                default:
                    System.out.println("Erreur d'entrée");
                    oriRobot_0 = null;
            }

            iRobot_1 = Integer.parseInt(scanner.nextLine());
            jRobot_1 = Integer.parseInt(scanner.nextLine());
            ori = scanner.nextLine();
            switch (ori) {
                case "Sud":
                    oriRobot_1 = Orientation.S;
                    break;
                case "Nord":
                    oriRobot_1 = Orientation.N;
                    break;
                case "Est":
                    oriRobot_1 = Orientation.E;
                    break;
                case "Ouest":
                    oriRobot_1 = Orientation.W;
                    break;
                default:
                    System.out.println("Erreur d'entrée");
                    oriRobot_1 = null;
            }

            lignes = Integer.parseInt(scanner.nextLine());
            colonnes = Integer.parseInt(scanner.nextLine());
            nb_patients = Integer.parseInt(scanner.nextLine());

            String[] line;
            String fullLine;
            dir = new String[lignes][colonnes];

            for (int i = 0; i < lignes; i++) {
                fullLine = scanner.nextLine();
                line = fullLine.split(" ");
                dir[i] = line;
            }

            return 0;
        }
        return 1;
    }





    public Parcours initParcours() {
            return new Parcours(lignes,colonnes,dir,nb_patients);
    }


    public Robot getRobot_0(){
        return new Robot(iRobot_0,jRobot_0,oriRobot_0,name0);
    }

    public Robot getRobot_1(){
        return new Robot(iRobot_1,jRobot_1,oriRobot_1,name1);
    }

}
