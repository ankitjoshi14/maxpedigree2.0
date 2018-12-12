package restapi.viewmodel;

public class Pagination implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int start;
	private int limit;
	private String cultivar;
	private String maturityGroup;
	private String country;
	private String year;
	
	public String getCultivar() {
		return cultivar;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setCultivar(String cultivar) {
		this.cultivar = cultivar;
	}

	public String getMaturitygroup() {
		return maturityGroup;
	}

	public void setMaturitygroup(String maturitygroup) {
		this.maturityGroup = maturitygroup;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Pagination(){
		
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	

}
