package restapi.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.annotations.ResultsetMappingSecondPass;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.tree.BooleanLiteralNode;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.REngineStdOutput;
import org.rosuda.REngine.RList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import restapi.viewmodel.*;
import restapi.model.*;

public class CultivarMetaDataService {
	private Connection conn;
	private REngine eng;
	private Session ss;
	private Transaction t;
	private SessionFactory sessionFactory;
	private HashMap<String, Integer> subcollectionMap;
	private HashMap<String, Integer> countryMap;
	private HashMap<String, Integer> maturityGroupMap;
	private HashMap<String, Integer> stemTerminationMap;
	private HashMap<String, Integer> flowerColorMap;
	private HashMap<String, Integer> pubescenceColorMap;
	private HashMap<String, Integer> pubescenceFormMap;
	private HashMap<String, Integer> pubescenceDensityMap;
	private HashMap<String, Integer> podColorMap;
	private HashMap<String, Integer> seedcoatLusterMap;
	private HashMap<String, Integer> seedcoatcolorMap;
	private HashMap<String, Integer> hilumcolorMap;
	private RList alleles;
	private HashMap<Long, String> snpNameMap;
	private HashMap<String, String[]> snpNameAllelesMap;

	public CultivarMetaDataService() {
		this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		this.subcollectionMap = getSubcollection();
		this.countryMap = getCountries();
		this.maturityGroupMap = getMaturityGroup();
		this.stemTerminationMap = getStemTermination();
		this.flowerColorMap = getFlowerColor();
		this.pubescenceColorMap = getPubesenceColor();
		this.pubescenceFormMap = getPubescenceForm();
		this.pubescenceDensityMap = getPubescenceDensity();
		this.podColorMap = getPodColor();
		this.seedcoatLusterMap = getSeedcoatLuster();
		this.seedcoatcolorMap = getSeedcoatColor();
		this.hilumcolorMap = geHilumColor();
		System.out.println("done metadata");
	}

	public Session getSs() {
		return ss;
	}

	public void setSs(Session ss) {
		this.ss = ss;
	}

	public HashMap<String, Integer> getSubcollectionMap() {
		return subcollectionMap;
	}

	public void setSubcollectionMap(HashMap<String, Integer> subcollectionMap) {
		this.subcollectionMap = subcollectionMap;
	}

	public HashMap<String, Integer> getStemTerminationMap() {
		return stemTerminationMap;
	}

	public void setStemTerminationMap(HashMap<String, Integer> stemTerminationMap) {
		this.stemTerminationMap = stemTerminationMap;
	}

	public Connection getConn() {
		return conn;
	}

	public REngine getEng() {
		return eng;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public HashMap<String, Integer> getCountryMap() {
		return countryMap;
	}

	public HashMap<String, Integer> getMaturityGroupMap() {
		return maturityGroupMap;
	}

	public HashMap<String, Integer> getFlowerColorMap() {
		return flowerColorMap;
	}

	public HashMap<String, Integer> getPubescenceColorMap() {
		return pubescenceColorMap;
	}

	public HashMap<String, Integer> getPubescenceFormMap() {
		return pubescenceFormMap;
	}

	public HashMap<String, Integer> getPubescenceDensityMap() {
		return pubescenceDensityMap;
	}

	public HashMap<String, Integer> getPodColorMap() {
		return podColorMap;
	}

	public HashMap<String, Integer> getSeedcoatLusterMap() {
		return seedcoatLusterMap;
	}

	public HashMap<String, Integer> getSeedcoatcolorMap() {
		return seedcoatcolorMap;
	}

	public HashMap<String, Integer> getHilumcolorMap() {
		return hilumcolorMap;
	}

	public RList getAlleles() {
		return alleles;
	}

	public HashMap<Long, String> getSnpNameMap() {
		return snpNameMap;
	}

	public HashMap<String, String[]> getSnpNameAllelesMap() {
		return snpNameAllelesMap;
	}

	public boolean CultivarNameAlreadyExist(String cultivarName) {
		boolean Exist = false;
		if (cultivarName != null) {
			Session session = null;
			try {
				session = this.sessionFactory.openSession();
				Criteria cr = session.createCriteria(Cultivar.class);
		         // Add restriction.
		         cr.add(Restrictions.ilike("cultivarName", cultivarName));
				//Query query = session.createQuery("SELECT c.cultivarName From Cultivar c where c.cultivarName ILIKE ':name'").setString("name", cultivarName);
				//String result = (String) query.uniqueResult();
		         Cultivar result = (Cultivar) cr.uniqueResult();
				if(result != null){
					Exist = true;
				}

			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		return Exist;
	}

	public boolean CultivarIdExist(String cultivarId) {
		boolean Exist = false;
		if (cultivarId != null) {
			Session session = null;
			try {
				session = this.sessionFactory.openSession();
				Query query = session.createQuery("SELECT c.cultivarId From Cultivar c where c.cultivarId = :id")
						.setString("id", cultivarId);
				String result = (String) query.uniqueResult();

				if(result != null){
					Exist = true;
				}

			} catch (Exception e) {
				System.out.println(e);
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}

		return Exist;
	}

	private HashMap<String, Integer> geHilumColor() {
		HashMap<String, Integer> hilumColorMap = null;
		List<Snp> snps = null;
		Session session = null;
		try {
			hilumColorMap = new HashMap<String, Integer>();
			List<HilumColor> hilumColors = null;
			session = this.sessionFactory.openSession();
			Query query = session.createQuery("From HilumColor");
			hilumColors = (ArrayList<HilumColor>) query.list();

			for (HilumColor hilumColor : hilumColors) {
				hilumColorMap.put(hilumColor.getValue(), (int) hilumColor.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return hilumColorMap;
	}

	private HashMap<String, Integer> getCountries() {
		HashMap<String, Integer> countriesMap = null;

		Session session = null;
		try {
			countriesMap = new HashMap<String, Integer>();
			List<Country> countries = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From Country");
			countries = (ArrayList<Country>) query.list();

			for (Country country : countries) {
				countriesMap.put(country.getValue(), (int) country.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return countriesMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getMaturityGroup() {
		HashMap<String, Integer> maturityGroupMap = null;

		Session session = null;
		try {
			maturityGroupMap = new HashMap<String, Integer>();
			List<MaturityGroup> maturityGroups = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From MaturityGroup");

			maturityGroups = (ArrayList<MaturityGroup>) query.list();

			for (MaturityGroup maturityGroup : maturityGroups) {
				maturityGroupMap.put(maturityGroup.getValue(), (int) maturityGroup.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return maturityGroupMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getPodColor() {
		HashMap<String, Integer> podColorMap = null;

		Session session = null;
		try {
			podColorMap = new HashMap<String, Integer>();
			List<PodColor> podColors = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From PodColor");

			podColors = (ArrayList<PodColor>) query.list();

			for (PodColor podColor : podColors) {
				podColorMap.put(podColor.getValue(), (int) podColor.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return podColorMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getPubesenceColor() {
		HashMap<String, Integer> pubescenceColorMap = null;

		Session session = null;
		try {
			pubescenceColorMap = new HashMap<String, Integer>();
			List<PubescenceColor> pubesenceColors = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From PubescenceColor");

			pubesenceColors = (ArrayList<PubescenceColor>) query.list();

			for (PubescenceColor pubesenceColor : pubesenceColors) {
				pubescenceColorMap.put(pubesenceColor.getValue(), (int) pubesenceColor.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return pubescenceColorMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getPubescenceForm() {
		HashMap<String, Integer> pubescenceFormMap = null;

		Session session = null;
		try {
			pubescenceFormMap = new HashMap<String, Integer>();
			List<PubescenceForm> pubescenceForms = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From PubescenceForm");

			pubescenceForms = (ArrayList<PubescenceForm>) query.list();

			for (PubescenceForm pubesenceForm : pubescenceForms) {
				pubescenceFormMap.put(pubesenceForm.getValue(), (int) pubesenceForm.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return pubescenceFormMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getPubescenceDensity() {
		HashMap<String, Integer> pubescenceDensityMap = null;

		Session session = null;
		try {
			pubescenceDensityMap = new HashMap<String, Integer>();
			List<PubescenceDensity> pubescenceDensities = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From PubescenceDensity");

			pubescenceDensities = (ArrayList<PubescenceDensity>) query.list();

			for (PubescenceDensity pubesenceDensity : pubescenceDensities) {
				pubescenceDensityMap.put(pubesenceDensity.getValue(), (int) pubesenceDensity.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return pubescenceDensityMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getFlowerColor() {
		HashMap<String, Integer> FlowerColorMap = null;

		Session session = null;
		try {
			FlowerColorMap = new HashMap<String, Integer>();
			List<FlowerColor> FlowerColors = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From FlowerColor");

			FlowerColors = (ArrayList<FlowerColor>) query.list();

			for (FlowerColor flowerColor : FlowerColors) {
				FlowerColorMap.put(flowerColor.getValue(), (int) flowerColor.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return FlowerColorMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getSeedcoatColor() {
		HashMap<String, Integer> seedCoatColorMap = null;

		Session session = null;
		try {
			seedCoatColorMap = new HashMap<String, Integer>();
			List<SeedcoatColor> seedcoatColors = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From SeedcoatColor");

			seedcoatColors = (ArrayList<SeedcoatColor>) query.list();

			for (SeedcoatColor seedcoatColor : seedcoatColors) {
				seedCoatColorMap.put(seedcoatColor.getValue(), (int) seedcoatColor.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return seedCoatColorMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getSeedcoatLuster() {
		HashMap<String, Integer> seedcoatLusterMap = null;

		Session session = null;
		try {
			seedcoatLusterMap = new HashMap<String, Integer>();
			List<SeedcoatLuster> seedcoatLusters = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From SeedcoatLuster");

			seedcoatLusters = (ArrayList<SeedcoatLuster>) query.list();

			for (SeedcoatLuster seedcoatLuster : seedcoatLusters) {
				seedcoatLusterMap.put(seedcoatLuster.getValue(), (int) seedcoatLuster.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return seedcoatLusterMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getStemTermination() {
		HashMap<String, Integer> stemTerminationMap = null;
		Session session = null;
		try {
			stemTerminationMap = new HashMap<String, Integer>();
			List<StemTermination> stemTerminations = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From StemTermination");

			stemTerminations = (ArrayList<StemTermination>) query.list();

			for (StemTermination stemTermination : stemTerminations) {
				stemTerminationMap.put(stemTermination.getValue(), (int) stemTermination.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return stemTerminationMap;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Integer> getSubcollection() {
		HashMap<String, Integer> subcollectionMap = null;
		Session session = null;
		try {
			subcollectionMap = new HashMap<String, Integer>();
			List<Subcollection> subcollections = null;
			session = sessionFactory.openSession();
			Query query = session.createQuery("From Subcollection");

			subcollections = (ArrayList<Subcollection>) query.list();

			for (Subcollection subcollection : subcollections) {
				subcollectionMap.put(subcollection.getValue(), (int) subcollection.getId());
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return subcollectionMap;
	}

}
