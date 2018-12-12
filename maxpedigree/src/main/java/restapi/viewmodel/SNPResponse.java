package restapi.viewmodel;

import java.util.ArrayList;

import restapi.model.Cultivar;
import restapi.model.Snp;

public class SNPResponse {
	
	
	private ArrayList<Snp> snps;
	
	public SNPResponse() {
	      this.snps = new ArrayList<Snp>();
	}

	public ArrayList<Snp> getSnps() {
		return snps;
	}

	public void setSnps(ArrayList<Snp> snps) {
		this.snps = snps;
	}
	 
}
