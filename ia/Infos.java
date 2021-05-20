package ia;

import cases.Case;

public class Infos {
	
	private Case pere;
	private int poids;
	
	public Infos(Case c, int poids) {
		pere = c;
		this.poids = poids;
	}

	public Case getPere() {
		return pere;
	}

	public int getPoids() {
		return poids;
	}

	@Override
	public String toString() {
		return ""+poids;
	}

	

	
}
