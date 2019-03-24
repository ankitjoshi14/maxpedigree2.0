package restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import restapi.model.*;

@Service
public class ValidationService {

	private CultivarMetaDataService cultivarMetaDataService;
	private SnpMetaDataService snpMetaDataService;

	@Autowired
	public ValidationService(CultivarMetaDataService cultivarMetaDataService , SnpMetaDataService snpMetaDataService) {
		super();
		this.cultivarMetaDataService = cultivarMetaDataService;
		this.snpMetaDataService = snpMetaDataService;
	}

	public CultivarMetaDataService getCultivarMetaDataService() {
		return cultivarMetaDataService;
	}

	public void setCultivarMetaDataService(CultivarMetaDataService cultivarMetaDataService) {
		this.cultivarMetaDataService = cultivarMetaDataService;
	}

	public SnpMetaDataService getSnpMetaDataService() {
		return snpMetaDataService;
	}

	public void setSnpMetaDataService(SnpMetaDataService snpMetaDataService) {
		this.snpMetaDataService = snpMetaDataService;
	}

	public Country validateCountry(String uploadedValue) {
		Country validCountry = null;
		if (cultivarMetaDataService.getCountryMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getCountryMap().get(uploadedValue);
			validCountry = new Country();
			validCountry.setId(id);

		}
		return validCountry;
	}

	public Subcollection validateSubcollection(String uploadedValue) {
		Subcollection validSubcollection = null;
		if (cultivarMetaDataService.getSubcollectionMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getSubcollectionMap().get(uploadedValue);
			validSubcollection = new Subcollection();
			validSubcollection.setId(id);

		}
		return validSubcollection;
	}

	public FlowerColor validateFlowerColor(String uploadedValue) {
		FlowerColor validFlowerColor = null;
		if (cultivarMetaDataService.getFlowerColorMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getFlowerColorMap().get(uploadedValue);
			validFlowerColor = new FlowerColor();
			validFlowerColor.setId(id);

		}
		return validFlowerColor;
	}

	public PodColor validatePodColor(String uploadedValue) {
		PodColor validPodColor = null;
		if (cultivarMetaDataService.getPodColorMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getPodColorMap().get(uploadedValue);
			validPodColor = new PodColor();
			validPodColor.setId(id);

		}
		return validPodColor;
	}

	public SeedcoatColor validateSeedcoatColor(String uploadedValue) {
		SeedcoatColor validSeedcoatColor = null;
		if (cultivarMetaDataService.getSeedcoatcolorMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getSeedcoatcolorMap().get(uploadedValue);
			validSeedcoatColor = new SeedcoatColor();
			validSeedcoatColor.setId(id);

		}
		return validSeedcoatColor;
	}

	public SeedcoatLuster validateSeedcoatLuster(String uploadedValue) {
		SeedcoatLuster validSeedcoatLuster = null;
		if (cultivarMetaDataService.getSeedcoatLusterMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getSeedcoatLusterMap().get(uploadedValue);
			validSeedcoatLuster = new SeedcoatLuster();
			validSeedcoatLuster.setId(id);

		}
		return validSeedcoatLuster;
	}

	public HilumColor validateHilumColor(String uploadedValue) {
		HilumColor validHilumColor = null;
		if (cultivarMetaDataService.getHilumcolorMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getHilumcolorMap().get(uploadedValue);
			validHilumColor = new HilumColor();
			validHilumColor.setId(id);

		}
		return validHilumColor;
	}

	public PubescenceColor validatePubescenceColor(String uploadedValue) {
		PubescenceColor validPubescenceColor = null;
		if (cultivarMetaDataService.getPubescenceColorMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getPubescenceColorMap().get(uploadedValue);
			validPubescenceColor = new PubescenceColor();
			validPubescenceColor.setId(id);

		}
		return validPubescenceColor;
	}

	public PubescenceDensity validatePubescenceDensity(String uploadedValue) {
		PubescenceDensity validPubescenceDensity = null;
		if (cultivarMetaDataService.getPubescenceDensityMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getPubescenceDensityMap().get(uploadedValue);
			validPubescenceDensity = new PubescenceDensity();
			validPubescenceDensity.setId(id);

		}
		return validPubescenceDensity;
	}

	public PubescenceForm validatePubescenceForm(String uploadedValue) {
		PubescenceForm validPubescenceForm = null;
		if (cultivarMetaDataService.getPubescenceFormMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getPubescenceFormMap().get(uploadedValue);
			validPubescenceForm = new PubescenceForm();
			validPubescenceForm.setId(id);

		}
		return validPubescenceForm;
	}

	public StemTermination validateStemTermination(String uploadedValue) {
		StemTermination validStemTermination = null;
		if (cultivarMetaDataService.getStemTerminationMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getStemTerminationMap().get(uploadedValue);
			validStemTermination = new StemTermination();
			validStemTermination.setId(id);

		}
		return validStemTermination;
	}

	public MaturityGroup validateMaturityGroup(String uploadedValue) {
		MaturityGroup validMaturityGroup = null;
		if (cultivarMetaDataService.getMaturityGroupMap().containsKey(uploadedValue)) {
			int id = cultivarMetaDataService.getMaturityGroupMap().get(uploadedValue);
			validMaturityGroup = new MaturityGroup();
			validMaturityGroup.setId(id);

		}
		return validMaturityGroup;
	}

	public boolean CultivarIdorNameExist(String[] headers, String[] splited) {
		boolean exist = false;
		for (int i = 0; i < headers.length; i++) {
			if (exist) {
				break;
			}
			if (headers[i].equals(Headers.Cultivar_ID.toString())) {
				exist = (!splited[i].trim().isEmpty() ? true : false);
			} else if (headers[i].equals(Headers.Cultivar_Name.toString())) {
				exist = (!splited[i].trim().isEmpty() ? true : false);
			}
		}

		return exist;
	}

}
