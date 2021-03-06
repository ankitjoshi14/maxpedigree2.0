package restapi.model;
// Generated Nov 11, 2018 7:08:40 PM by Hibernate Tools 4.3.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * PubescenceForm generated by hbm2java
 */
@Entity
@Table(name = "pubescence_form", schema = "public")
public class PubescenceForm implements java.io.Serializable {

	private long id;
	private String value;
	private String description;
	private Set<Cultivar> cultivars = new HashSet<Cultivar>(0);

	public PubescenceForm() {
	}

	public PubescenceForm(long id) {
		this.id = id;
	}

	public PubescenceForm(long id, String value, String description, Set<Cultivar> cultivars) {
		this.id = id;
		this.value = value;
		this.description = description;
		this.cultivars = cultivars;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pubescenceForm")
	@JsonIgnore
	public Set<Cultivar> getCultivars() {
		return this.cultivars;
	}

	public void setCultivars(Set<Cultivar> cultivars) {
		this.cultivars = cultivars;
	}

}
