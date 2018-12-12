package restapi.viewmodel;

import java.util.ArrayList;

import restapi.model.Cultivar;

public class CultivarResponse {
	
	public CultivarResponse() {
	     this.cultivars = new ArrayList<Cultivar>();
	}

	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public ArrayList<Cultivar> getCultivars() {
		return cultivars;
	}
	
	
	public void setCultivars(ArrayList<Cultivar> cultivars) {
		this.cultivars = cultivars;
	}
	long total;
	ArrayList<Cultivar> cultivars;
	ArrayList<String> cultivarids;
	public ArrayList<String> getCultivarids() {
		return cultivarids;
	}

	public void setCultivarids(ArrayList<String> cultivarids) {
		this.cultivarids = cultivarids;
	}
	
}
