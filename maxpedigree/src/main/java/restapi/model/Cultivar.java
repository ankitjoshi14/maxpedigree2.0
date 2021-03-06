package restapi.model;
// Generated Nov 11, 2018 7:08:40 PM by Hibernate Tools 4.3.1.Final

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * Cultivar generated by hbm2java
 */
@Entity
@Table(name = "cultivar", schema = "public")
public class Cultivar implements java.io.Serializable {

	private String cultivarId;
	private Country country;
	private FlowerColor flowerColor;
	private HilumColor hilumColor;
	private MaturityGroup maturityGroup;
	private PodColor podColor;
	private PubescenceColor pubescenceColor;
	private PubescenceDensity pubescenceDensity;
	private PubescenceForm pubescenceForm;
	private SeedcoatColor seedcoatColor;
	private SeedcoatLuster seedcoatLuster;
	private StemTermination stemTermination;
	private Subcollection subcollection;
	private String cultivarName;
	private String cultivarAlias;
	private String femaleId;
	private String femaleName;
	private String maleId;
	private String maleName;
	private String stateName;
	private String pedigreeOriginal;
	private String otherseeds;
	private String otherLeaf;
	private String otherPlant;
	private String year;
private double pc1;
	
	private double pc2;
	
	private String actedGender; 
	
	
	private List<Cultivar> childof;
	
	
	@Transient
	public List<Cultivar> getChildof() {
		return childof;
	}

	public void setChildof(List<Cultivar> childof) {
		this.childof = childof;
	}
	@Transient
	public String getActedGender() {
		return actedGender;
	}

	public void setActedGender(String actedGender) {
		this.actedGender = actedGender;
	}

	@Transient
	public double getPc1() {
		return pc1;
	}

	public void setPc1(double pc1) {
		this.pc1 = pc1;
	}

	@Transient
	public double getPc2() {
		return pc2;
	}

	public void setPc2(double pc2) {
		this.pc2 = pc2;
	}

	public Cultivar() {
	}

	public Cultivar(String cultivarId) {
		this.cultivarId = cultivarId;
	}

	public Cultivar(String cultivarId, Country country, FlowerColor flowerColor, HilumColor hilumColor,
			MaturityGroup maturityGroup, PodColor podColor, PubescenceColor pubescenceColor,
			PubescenceDensity pubescenceDensity, PubescenceForm pubescenceForm, SeedcoatColor seedcoatColor,
			SeedcoatLuster seedcoatLuster, StemTermination stemTermination, Subcollection subcollection,
			String cultivarName, String cultivarAlias, String femaleId, String femaleName, String maleId,
			String maleName, String stateName, String pedigreeOriginal, String otherseeds, String otherLeaf,
			String otherPlant, String year) {
		this.cultivarId = cultivarId;
		this.country = country;
		this.flowerColor = flowerColor;
		this.hilumColor = hilumColor;
		this.maturityGroup = maturityGroup;
		this.podColor = podColor;
		this.pubescenceColor = pubescenceColor;
		this.pubescenceDensity = pubescenceDensity;
		this.pubescenceForm = pubescenceForm;
		this.seedcoatColor = seedcoatColor;
		this.seedcoatLuster = seedcoatLuster;
		this.stemTermination = stemTermination;
		this.subcollection = subcollection;
		this.cultivarName = cultivarName;
		this.cultivarAlias = cultivarAlias;
		this.femaleId = femaleId;
		this.femaleName = femaleName;
		this.maleId = maleId;
		this.maleName = maleName;
		this.stateName = stateName;
		this.pedigreeOriginal = pedigreeOriginal;
		this.otherseeds = otherseeds;
		this.otherLeaf = otherLeaf;
		this.otherPlant = otherPlant;
		this.year = year;
	}
	
	
	

	@Id
	@GenericGenerator(name = "cultivar_id", strategy = "restapi.service.CultivarIdGenerator")
	@GeneratedValue(generator = "cultivar_id")  
	@Column(name = "cultivar_id", unique = true, nullable = false, length = 25)
	public String getCultivarId() {
		return this.cultivarId;
	}

	public void setCultivarId(String cultivarId) {
		this.cultivarId = cultivarId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flower_color_id")
	public FlowerColor getFlowerColor() {
		return this.flowerColor;
	}

	public void setFlowerColor(FlowerColor flowerColor) {
		this.flowerColor = flowerColor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hilum_color_id")
	public HilumColor getHilumColor() {
		return this.hilumColor;
	}

	public void setHilumColor(HilumColor hilumColor) {
		this.hilumColor = hilumColor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maturity_group_id")
	public MaturityGroup getMaturityGroup() {
		return this.maturityGroup;
	}

	public void setMaturityGroup(MaturityGroup maturityGroup) {
		this.maturityGroup = maturityGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pod_color_id")
	public PodColor getPodColor() {
		return this.podColor;
	}

	public void setPodColor(PodColor podColor) {
		this.podColor = podColor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pubescence_color_id")
	public PubescenceColor getPubescenceColor() {
		return this.pubescenceColor;
	}

	public void setPubescenceColor(PubescenceColor pubescenceColor) {
		this.pubescenceColor = pubescenceColor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pubescence_density_id")
	public PubescenceDensity getPubescenceDensity() {
		return this.pubescenceDensity;
	}

	public void setPubescenceDensity(PubescenceDensity pubescenceDensity) {
		this.pubescenceDensity = pubescenceDensity;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pubescence_form_id")
	public PubescenceForm getPubescenceForm() {
		return this.pubescenceForm;
	}

	public void setPubescenceForm(PubescenceForm pubescenceForm) {
		this.pubescenceForm = pubescenceForm;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "seedcoat_color_id")
	public SeedcoatColor getSeedcoatColor() {
		return this.seedcoatColor;
	}

	public void setSeedcoatColor(SeedcoatColor seedcoatColor) {
		this.seedcoatColor = seedcoatColor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "seedcoat_luster_id")
	public SeedcoatLuster getSeedcoatLuster() {
		return this.seedcoatLuster;
	}

	public void setSeedcoatLuster(SeedcoatLuster seedcoatLuster) {
		this.seedcoatLuster = seedcoatLuster;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stem_termination_id")
	public StemTermination getStemTermination() {
		return this.stemTermination;
	}

	public void setStemTermination(StemTermination stemTermination) {
		this.stemTermination = stemTermination;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subcollection_id")
	public Subcollection getSubcollection() {
		return this.subcollection;
	}

	public void setSubcollection(Subcollection subcollection) {
		this.subcollection = subcollection;
	}

	@Column(name = "cultivar_name",unique = true)
	public String getCultivarName() {
		return this.cultivarName;
	}

	public void setCultivarName(String cultivarName) {
		this.cultivarName = cultivarName;
	}

	@Column(name = "cultivar_alias")
	public String getCultivarAlias() {
		return this.cultivarAlias;
	}

	public void setCultivarAlias(String cultivarAlias) {
		this.cultivarAlias = cultivarAlias;
	}

	@Column(name = "female_id")
	public String getFemaleId() {
		return this.femaleId;
	}

	public void setFemaleId(String femaleId) {
		this.femaleId = femaleId;
	}

	@Column(name = "female_name")
	public String getFemaleName() {
		return this.femaleName;
	}

	public void setFemaleName(String femaleName) {
		this.femaleName = femaleName;
	}

	@Column(name = "male_id")
	public String getMaleId() {
		return this.maleId;
	}

	public void setMaleId(String maleId) {
		this.maleId = maleId;
	}

	@Column(name = "male_name")
	public String getMaleName() {
		return this.maleName;
	}

	public void setMaleName(String maleName) {
		this.maleName = maleName;
	}

	@Column(name = "state_name")
	public String getStateName() {
		return this.stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Column(name = "pedigree_original")
	public String getPedigreeOriginal() {
		return this.pedigreeOriginal;
	}

	public void setPedigreeOriginal(String pedigreeOriginal) {
		this.pedigreeOriginal = pedigreeOriginal;
	}

	@Column(name = "otherseeds")
	public String getOtherseeds() {
		return this.otherseeds;
	}

	public void setOtherseeds(String otherseeds) {
		this.otherseeds = otherseeds;
	}

	@Column(name = "other_leaf")
	public String getOtherLeaf() {
		return this.otherLeaf;
	}

	public void setOtherLeaf(String otherLeaf) {
		this.otherLeaf = otherLeaf;
	}

	@Column(name = "other_plant")
	public String getOtherPlant() {
		return this.otherPlant;
	}

	public void setOtherPlant(String otherPlant) {
		this.otherPlant = otherPlant;
	}

	@Column(name = "year")
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
