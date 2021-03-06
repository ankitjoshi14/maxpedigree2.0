package restapi.model;
// Generated Nov 11, 2018 7:08:40 PM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Snp2 generated by hbm2java
 */
@Entity
@Table(name = "snp2", schema = "public")
public class Snp2 implements java.io.Serializable {

	private String idd;
	private String snp;

	public Snp2() {
	}

	public Snp2(String idd) {
		this.idd = idd;
	}

	public Snp2(String idd, String snp) {
		this.idd = idd;
		this.snp = snp;
	}

	@Id

	@Column(name = "idd", unique = true, nullable = false, length = 25)
	public String getIdd() {
		return this.idd;
	}

	public void setIdd(String idd) {
		this.idd = idd;
	}

	@Column(name = "snp", length = 43000)
	public String getSnp() {
		return this.snp;
	}

	public void setSnp(String snp) {
		this.snp = snp;
	}

}
