package ia;

import java.util.ArrayList;

import robot.Orientation;
import robot.Robot;

import cases.Case;

public class Chemin {

	private ArrayList<String> instructions;
	private ArrayList<Case> ordre;
	private Robot robot;
	private Robot phantom;
	private Case depart;

	//Ici phantom va simuler notre robot afin de pouvoir changer les valeurs de positions sans alterer celles du vrai robot
	public Chemin(ArrayList<Case> ordre, Robot robot, Case depart) {
		this.instructions = new ArrayList<String>();
		this.ordre = ordre;
		this.robot = robot;
		this.depart = depart;
		phantom = new Robot(robot.getiCurr(), robot.getjCurr(), robot.getOriRobot(),"Phantom");
		phantom.setLastI(robot.getLastI());
		phantom.setLastJ(robot.getLastJ());
		constructPath(depart);
	}

	private void constructPath(Case depart){
		Case curr = depart;
		for(Case dest : ordre){
			//Verification orientation
			Orientation ori = phantom.getOriRobot();
			//System.out.println("Robot data : "+phantom.getLastI()+" "+phantom.getLastJ()+" / "+phantom.getiCurr()+" "+phantom.getjCurr());
			//System.out.println("Case data : "+dest.getI()+" "+dest.getJ());
			if(dest.getI() == phantom.getLastI() && dest.getJ() == phantom.getLastJ()){
				instructions.add("Demi-Tour");
			}
			else{
				if(curr.is3way()){
					instructionSlip(ori,dest,curr);

				}
				else{
					//Si c'est un virage
					if((curr.contientDir('U') || curr.contientDir('D')) && (curr.contientDir('R') || curr.contientDir('L'))) {
						instructions.add("Virage");
					}
					else {
						instructions.add("Ligne Droite");
					}
				}
			}
			phantom.updatePosition(dest.getI(),dest.getJ());
			curr = dest;
		}
	}

	private void instructionSlip(Orientation oriRobot, Case dest, Case curr){
		int mouvI = dest.getI() - phantom.getiCurr();
		int mouvJ = dest.getJ() - phantom.getjCurr();
		switch(oriRobot){
			case N:
				if(mouvJ == 1)//on va à droite
					instructions.add("Slip Droit");
				else if(mouvJ == -1)//on va à gauche
					instructions.add("Slip Gauche");
				else {//on va tout droit
					if(curr.contientDir('R'))//Si on peut tourner à droite
						instructions.add("Slip Droit Droit");
					else
						instructions.add("Slip Droit Gauche");
				}
				break;
			case S:
				if(mouvJ == -1)//on va à droite
					instructions.add("Slip Droit");
				else if(mouvJ == 1)//on va à gauche
					instructions.add("Slip Gauche");
				else {//on va tout droit
					if(curr.contientDir('L'))//Si on peut tourner à droite
						instructions.add("Slip Droit Droit");
					else
						instructions.add("Slip Droit Gauche");
				}
				break;
			case W:
				if(mouvI == -1)//on va à droite
					instructions.add("Slip Droit");
				else if(mouvI == 1)//on va à gauche
					instructions.add("Slip Gauche");
				else {//on va tout droit
					if(curr.contientDir('U'))//Si on peut tourner à droite (en remontant)
						instructions.add("Slip Droit Droit");
					else
						instructions.add("Slip Droit Gauche");
				}
				break;

			case E:
				if(mouvI == 1)//on va à droite
					instructions.add("Slip Droit");
				else if(mouvI == -1)//on va à gauche
					instructions.add("Slip Gauche");
				else {//on va tout droit
					if(curr.contientDir('D'))//Si on peut tourner à droite (en descendant)
						instructions.add("Slip Droit Droite");
					else
						instructions.add("Slip Droit Gauche");
				}
				break;
		}
	}



	public void getInstructionsList() {
		for(String inst : instructions){
			System.out.println(inst);
		}
	}

	public ArrayList<String> getInstructions() {
		return instructions;
	}

	public ArrayList<Case> getOrdre() {
		return ordre;
	}

	public Case getLastCase(){
		return ordre.get(ordre.size()-1);
	}


}
