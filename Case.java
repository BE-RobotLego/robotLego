package cases;

public class Case implements Comparable<Case>  {
	private int i;
	private int j;
	private String dirCase;
	private boolean patient;
	private boolean hopital;
	private int poids;
	
	public Case(int i, int j, String dirCase) {
		this.i = i;
		this.j = j;
		this.dirCase = dirCase;
		this.patient = contientDir('P');
		this.hopital = contientDir('H');
		initPoids();
	}

	public boolean contientDir(char c){
		for(int i=0; i<dirCase.length();i++){
			if(dirCase.charAt(i)==c)
				return true;
		}
		return false;
	}
	
	public boolean hasHopital(){
		return hopital;
	}
	
	public boolean hasPatient(){
		return patient;
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public boolean is3way(){
		return ((contientDir('U') && contientDir('D') && (contientDir('R') || contientDir('L')))
			|| (contientDir('L') && contientDir('R') && (contientDir('U') || contientDir('D'))));
	}

	public boolean isDroite(){
		return (contientDir('U') && contientDir('D')) || (contientDir('R') && contientDir('L'));
	}

	public boolean isVirage(){
		return !isDroite();
	}

	public int[] getCoords(){
		int[] coords = {i,j};
		return coords ;
	}

	public String getDirCase() {
		return dirCase;
	}


	public void setPatient(boolean patient) {
		this.patient = patient;
	}


	public void setPoids(int poids) { this.poids = poids; }

	public void initPoids(){
		if(is3way())
			poids = 2;
		else
			poids = 1;
	}

	public int getPoids(){ return poids; }

	public String toString(){
		return "Case "+dirCase+" en "+i+"/"+j;
	}
	
	public int compareTo(Case c) {
		if(i > c.getI())
			return 1;
		else if(i < c.getI())
			return -1;
		else if(j > c.getJ())
			return 1;
		else if(j < c.getJ())
			return -1;
		return 0;
	}
	
	
}
