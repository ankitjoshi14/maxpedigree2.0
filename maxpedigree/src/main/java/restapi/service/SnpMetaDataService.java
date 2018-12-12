package restapi.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;
import org.springframework.stereotype.Service;

import restapi.model.Markerinfo;
@Service
public class SnpMetaDataService {
	private Transaction t;
	private SessionFactory sessionFactory;
	private RList alleles;
	private HashMap<Integer, String> snpNameMap;
	private HashMap<String, String[]> snpNameAllelesMap;
	private HashMap<String, String> snpNamePosMap;
	private HashMap<String,String> snpNameChromMap;

	public SnpMetaDataService() {
		 
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		getMetaData();
	}

	@SuppressWarnings("unchecked")
	private void getMetaData() {
		this.snpNameMap = new HashMap<Integer, String>();
		this.snpNameChromMap = new HashMap<String , String>();
		this.snpNamePosMap = new HashMap<String,String>();
		this.snpNameAllelesMap = new HashMap<String, String[]>();
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;
		this.alleles= new RList();
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;
		List<Markerinfo> markerInfos = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("From Markerinfo m order by m.id asc");
			markerInfos = (ArrayList<Markerinfo>) query.list();

			for (int index = 0; index < markerInfos.size(); index++) {
				if(index == 580){
					System.out.println(index);
				}
				Markerinfo markerInfo = markerInfos.get(index);
				alleles.put(Long.toString(markerInfo.getId()), new REXPString(markerInfo.getAlleles()));
				
				
				snpNameMap.put((int) markerInfo.getId(), markerInfo.getRs());
				snpNameChromMap.put(markerInfo.getRs(), markerInfo.getChrom());
				snpNamePosMap.put(markerInfo.getRs(),markerInfo.getPos());
				String[] values = markerInfo.getAlleles().split("/");
				String[] possibleallelesvalues = new String[]{"-","-","-","H"};
				
				// this condtion is for pos having one allele
				if(values.length == 2){
					possibleallelesvalues[1] = values[1];
				}
				
				possibleallelesvalues[0] = values[0];
			
				snpNameAllelesMap.put(markerInfo.getRs(), possibleallelesvalues);
			}

			System.out.println("got the alleles");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public HashMap<String, String> getSnpNamePosMap() {
		return snpNamePosMap;
	}

	public void setSnpNamePosMap(HashMap<String, String> snpNamePosMap) {
		this.snpNamePosMap = snpNamePosMap;
	}

	public HashMap<String, String> getSnpNameChromMap() {
		return snpNameChromMap;
	}

	public void setSnpNameChromMap(HashMap<String, String> snpNameChromMap) {
		this.snpNameChromMap = snpNameChromMap;
	}

	public RList getAlleles() {
		return alleles;
	}

	public void setAlleles(RList alleles) {
		this.alleles = alleles;
	}

	public HashMap<Integer, String> getSnpNameMap() {
		return snpNameMap;
	}

	public void setSnpNameMap(HashMap<Integer, String> snpNameMap) {
		this.snpNameMap = snpNameMap;
	}

	public HashMap<String, String[]> getSnpNameAllelesMap() {
		return snpNameAllelesMap;
	}

	public void setSnpNameAllelesMap(HashMap<String, String[]> snpNameAllelesMap) {
		this.snpNameAllelesMap = snpNameAllelesMap;
	}
	
	public boolean CultivarIdExist(String cultivarId) {
		boolean Exist = false;
		if (cultivarId != null) {
			Session session = null;
			try {
				session = this.sessionFactory.openSession();
				Query query = session.createQuery("SELECT c.id From Snp c where c.id = :snpId")
						.setString("snpId", cultivarId);
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
}
