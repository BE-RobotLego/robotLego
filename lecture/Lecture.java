package lecture;

import cases.Parcours;
import robot.Orientation;
import robot.Robot;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lecture extends Component {


    private int iRobot;
    private int jRobot;
    private Orientation oriRobot;
    private String name;
    private String[][] dir;
    private int lignes;
    private int colonnes;
    private int nb_patients;

    public Lecture(String s) {
        name = s;
    }

    public int readFile() throws FileNotFoundException {
        Scanner scanner;
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Map Files","map");
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();
            scanner = new Scanner(fichier);
            iRobot = Integer.parseInt(scanner.nextLine());
            jRobot = Integer.parseInt(scanner.nextLine());
            String ori = scanner.nextLine();
            switch (ori) {
                case "Sud":
                    oriRobot = Orientation.S;
                    break;
                case "Nord":
                    oriRobot = Orientation.N;
                    break;
                case "Est":
                    oriRobot = Orientation.E;
                    break;
                case "Ouest":
                    oriRobot = Orientation.W;
                    break;
                default:
                    System.out.println("Erreur d'entrée");
                    oriRobot = null;
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
        return new Parcours(lignes, colonnes, dir, nb_patients);
    }

    public Robot initRobot() {
        return new Robot(iRobot,jRobot,oriRobot,name);
    }


}
