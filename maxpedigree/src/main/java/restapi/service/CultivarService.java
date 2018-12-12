package restapi.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.ws.Response;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.DisjunctionFragment;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPList;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.REngineStdOutput;
import org.rosuda.REngine.RList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import restapi.model.*;
import restapi.viewmodel.*;

@Service
public class CultivarService {

	private Connection conn;
	private REngine eng;
	private Session ss;
	private Transaction t;
	private SessionFactory sessionFactory;
	private RList alleles;
	private ArrayList<Markerinfo> markerInfos;
	private MarkerinfoMaps markerinfoMaps;
	private CultivarInfo cultivarInfo;
	private CultivarMetaDataService cultivarMetaDataService;
	private SnpMetaDataService snpMetaDataService;
	private ValidationService validationService;
	
	

	public CultivarService() {
          System.out.println("service bean");
		try {
			conn = DBConnection.getConnection();
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			eng = REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new String[] {}, new REngineStdOutput(),
					false);
		   this.validationService = new ValidationService();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public RList getSNP(List<String> cultivarIds) {
		RList rList = null;
		List<Snp> snps = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("From Snp s WHERE s.id IN (:ids)");
			query.setParameterList("ids", cultivarIds);
			snps = (ArrayList<Snp>) query.list();
			rList = new RList();
			for (int index = 0; index < snps.size(); index++) {
				Snp snp = snps.get(index);
				rList.put(snp.getId().toString(), new REXPString(snp.getSnp()));
			}
                
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return rList;
	}

	private double[][] pca(RList glist, RList alleles) throws REXPMismatchException, ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException, REngineException {
		// REngine eng =
		// REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new
		// String[] {},
		// new REngineStdOutput(), false);
		// RList l = new RList();
		// l.put("a", new REXPString("AB_D"));
		// l.put("b", new REXPString("CD-L"));
		eng.assign("data", new REXPList(glist));
		eng.assign("alleles", new REXPList(alleles));
		eng.parseAndEval("source('D:\\\\data-genetics\\\\clint\\\\PCA.R')");

		REXP pca2 = eng.parseAndEval("pca2(data,alleles)");
		// REXP pca = eng.parseAndEval("pca(data,alleles)");
		// System.out.println("pull all three back to Java");
		// REXP x = eng.parseAndEval("x");
		// System.out.println(" x = " + x);
		// REXP y = eng.parseAndEval("y");
		// System.out.println(" y = " + y);
		// REXP z = eng.parseAndEval("print(z)");
		// System.out.println("pca" + pca);
		System.out.println("pca2 " + pca2);
		double[][] k = pca2.asDoubleMatrix();
		System.out.println("PASSED");
		return k;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Cultivar> addPrincipleComponenttoCultivars(List<String> ids, Vector names, double[][] pc)
			throws SQLException {
		// ArrayList<Cultivar> cultivars = new ArrayList<Cultivar>();
		// Connection conn = DBConnection.getConnection();
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;
		// selectSQL = "SELECT * FROM public.cultivar WHERE cultivar_id in
		// ('PI548500','PI548651','PI548624','PI548693',
		// 'PI548519','PI557010','PI539867','PI539866','PI612932','PI597386','PI634827','PI548522','PI548653','PI548521','PI548972','PI592524','PI633049','PI633424','PI548656','PI548517')";
		// preparedStatement = conn.prepareStatement(selectSQL);
		// ResultSet rs = preparedStatement.executeQuery();
		//
		// while (rs.next()) {
		// int index = names.indexOf(rs.getString("cultivar_id"));
		// String id = rs.getString("cultivarid");
		// String name = names.get(index).toString();
		// String country = rs.getString("Country");
		// String state = rs.getString("State");
		// String mg = rs.getString("MG");
		// Double pc1 = pc[index][0];
		// Double pc2 = pc[index][1];
		// Cultivar c = new Cultivar(id, name, mg, country, state, pc1, pc2);
		// cultivars.add(c);
		// }
		//
		// return cultivars;
		RList rList = null;
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;
		List<Cultivar> cultivars = null;
		List<Cultivar> Cultivars = null;
		Session session = null;
		try {
			// selectSQL = "SELECT id ,snp FROM public.snp WHERE id in
			// ('PI548500','PI548651','PI548624','PI548693',
			// 'PI548519','PI557010','PI539867','PI539866','PI612932','PI597386','PI634827','PI548522','PI548653','PI548521','PI548972','PI592524','PI633049','PI633424','PI548656','PI548517')";
			// preparedStatement = conn.prepareStatement(selectSQL);
			// ResultSet rs = preparedStatement.executeQuery();
			// list = new RList();
			// while (rs.next()) {
			// list.put(rs.getString("id"), new
			// REXPString(rs.getString("snp")));
			// }
			session = sessionFactory.openSession();
			Query query = session.createQuery("From Cultivar c WHERE c.id IN (:ids)");
			query.setParameterList("ids", ids);
			cultivars = new ArrayList<Cultivar>(ids.size());
			cultivars = (ArrayList<Cultivar>) query.list();
			for (int index = 0; index < ids.size(); index++) {

				Cultivar cultivar = cultivars.get(index);

				int i = names.indexOf(cultivar.getCultivarId());
				if (i > -1) {
					cultivar.setPc1(pc[i][0]);
					cultivar.setPc2(pc[i][1]);
				}
				cultivars.add(cultivar);
				// viewCultivar.setCultivarName(cultivar.getCultivarName());
				// viewCultivar.setFlowerColor(cultivar.getFlowerColor());
				// viewCultivar.setCountry(cultivar.getCountry());
				// viewCultivar.setCultivarAlias(cultivar.getCultivarAlias());
				//
				// viewCultivar.setCultivarId(cultivar.getCultivarId());
				// viewCultivar.setFemaleId(femaleId);
				// viewCultivar.setFemaleName(femaleName);
				// viewCultivar.setFlowerColor(flowerColor);
				// viewCultivar.setHilumColor(hilumColor);
				// viewCultivar.setMaleId(maleId);
				// viewCultivar.setMaleName(maleName);
				// viewCultivar.setMaturityGroup(maturityGroup);
				// viewCultivar.setOtherLeaf(otherLeaf);
				// viewCultivar.setOtherPlant(otherPlant);
				// viewCultivar.setOtherseeds(otherseeds);
				// viewCultivar.setPedigreeOriginal(pedigreeOriginal);
				// viewCultivar.setPodColor(podColor);
				// viewCultivar.setPubescenceColor(pubescenceColor);
				// viewCultivar.setPubescenceColor(pubescenceColor);
				// viewCultivar.setPubescenceDensity(pubescenceDensity);
				// viewCultivar.setPubescenceForm(pubescenceForm);
				// viewCultivar.setSeedcoatColor(seedcoatColor);
				// viewCultivar.setHilumColor(HilumColor);
				// viewCultivar.setState(state);
				// viewCultivar.setState(cultivar.getState());
				// viewCultivar.setPubescenceColor(cultivar.getPubescenceColor());
				//
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return cultivars;

	}

	@SuppressWarnings("unchecked")
	public CultivarResponse getallCultivars(Pagination page) {
		CultivarResponse response = new CultivarResponse();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteriaforTotal = session.createCriteria(Cultivar.class);
			Criteria criteria = session.createCriteria(Cultivar.class);
			 
             
	         // Add restriction.
			if(page.getCultivar()!= null){
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.ilike("cultivarName", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("cultivarId", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("maleName", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("femaleName", page.getCultivar(),MatchMode.START));
			Conjunction and = Restrictions.conjunction();
			and.add(or);
			criteria.add(and);
	    	criteriaforTotal.add(and);
			}
		    if(page.getCountry()!= null){
		    	Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.ilike("country", page.getCountry(),MatchMode.START));
				or.add(Restrictions.ilike("stateName", page.getCountry(),MatchMode.START));
				Conjunction and = Restrictions.conjunction();
				and.add(or);
		    	criteria.add(and);
		    	criteriaforTotal.add(and);
			}
		    if(page.getMaturitygroup() != null){
		    	Conjunction and = Restrictions.conjunction();
				and.add(Restrictions.ilike("maturityGroup", page.getMaturitygroup(),MatchMode.EXACT));
		    	criteria.add(and);
		    	criteriaforTotal.add(and);	
		    }
		    if(page.getYear() != null){
		    	Conjunction and = Restrictions.conjunction();
				and.add(Restrictions.ilike("year", page.getYear(),MatchMode.EXACT));
		    	criteria.add(and);
		    	criteriaforTotal.add(and);
		    }
		    
			// getting total cultivar matching search bar
		    criteriaforTotal.setProjection(Projections.rowCount());	
			Object o = criteriaforTotal.uniqueResult();
			response.setTotal((long) o);
			
			// getting top (limit) cultivars
			criteria.setFirstResult(page.getStart());
			criteria.setMaxResults(page.getLimit());
			response.setCultivars((ArrayList<Cultivar>) criteria.list());
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Cultivar> getallCultivars(ArrayList<String> cultivarIds) {
		ArrayList<Cultivar> list = new ArrayList<Cultivar>();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteriaforTotal = session.createCriteria(Cultivar.class);
			Criteria criteria = session.createCriteria(Cultivar.class);
			 
			criteria.add( Restrictions.in("cultivarId", cultivarIds ) );
		    
			 
			
			 // just to make sure it is not fetching more than 100 UI will restrict if more than 100 so setting 105 	      
		    criteria.setMaxResults(105);
		    list = ((ArrayList<Cultivar>) criteria.list());
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Snp> getallSNP(ArrayList<String> cultivarIds) {
		ArrayList<Snp> response = new ArrayList<Snp>();
		
		Session session = null;
		try {
			session = sessionFactory.openSession();
			 
			Criteria criteria = session.createCriteria(Cultivar.class);
			 
             
			 
		    criteria.setProjection(Projections.property("cultivarId"));
		    criteria.add( Restrictions.in("cultivarId", cultivarIds  ) );
		   // just to make sure it is not fetching more than 100 UI will restrict if more than 100 so setting 105 	      
		    criteria.setMaxResults(105);
			 
			ArrayList<String> ids = (ArrayList<String>) criteria.list();
			
			criteria = session.createCriteria(Snp.class);
			criteria.add( Restrictions.in("id", ids ) );
			
			response = ((ArrayList<Snp>) criteria.list());
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return response;
	}
	
	
	@SuppressWarnings("unchecked")
	public CultivarResponse getallCultivarids(Pagination page) {
		CultivarResponse response = new CultivarResponse();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteriaforTotal = session.createCriteria(Cultivar.class);
			Criteria criteria = session.createCriteria(Cultivar.class);
			 
             
	         // Add restriction.
			if(page.getCultivar()!= null){
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.ilike("cultivarName", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("cultivarId", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("maleName", page.getCultivar(),MatchMode.START));
			or.add(Restrictions.ilike("femaleName", page.getCultivar(),MatchMode.START));
			Conjunction and = Restrictions.conjunction();
			and.add(or);
			criteria.add(and);
	    	criteriaforTotal.add(and);
			}
		    if(page.getCountry()!= null){
		    	Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.ilike("country", page.getCountry(),MatchMode.START));
				or.add(Restrictions.ilike("stateName", page.getCountry(),MatchMode.START));
				Conjunction and = Restrictions.conjunction();
				and.add(or);
		    	criteria.add(and);
		    	criteriaforTotal.add(and);
			}
		    if(page.getMaturitygroup() != null){
		    	Conjunction and = Restrictions.conjunction();
				and.add(Restrictions.ilike("maturityGroup", page.getMaturitygroup(),MatchMode.EXACT));
		    	criteria.add(and);
		    	criteriaforTotal.add(and);	
		    }
		    if(page.getYear() != null){
		    	Conjunction and = Restrictions.conjunction();
				and.add(Restrictions.ilike("year", page.getYear(),MatchMode.EXACT));
		    	criteria.add(and);
		    	criteriaforTotal.add(and);
		    }
		    
		 // getting total cultivar matching search bar
		    criteriaforTotal.setProjection(Projections.rowCount());	
			Object o = criteriaforTotal.uniqueResult();
			  response.setTotal((long) o);
			 
		    criteria.setProjection(Projections.property("cultivarId"));
		   // just to make sure it is not fetching more than 100 UI will restrict if more than 100 so setting 105 	      
		    criteria.setMaxResults(105);
			 
			response.setCultivarids((ArrayList<String>) criteria.list());
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return response;
	}

	public List<Cultivar> getPCA(List<String> ids) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, REXPMismatchException, REngineException, SQLException {
		RList glist = getSNP(ids);
		RList alleles = this.validationService.getSnpMetaDataService().getAlleles();
		double[][] pcs = pca(glist, alleles);

		List<Cultivar> list = addPrincipleComponenttoCultivars(ids, glist.names, pcs);
		System.out.println(list);
		return list;

	}

	public Cultivar getCultivar(String cultivarid) {
		Cultivar cultivar = null;
		Session session = null;
		try {
			if (cultivarid != null) {

				session = sessionFactory.openSession();
				cultivar = (Cultivar) session.get(Cultivar.class, cultivarid);
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return cultivar;

	}

	public Cultivar getpedigree(String cultivarId, String gender) {
		Cultivar root = getCultivar(cultivarId);
		if (root == null) {
			return null;
		}

		root = getCultivar(cultivarId);
		root.setActedGender(gender);
		ArrayList<Cultivar> parents = new ArrayList<Cultivar>();
		Cultivar femaleParent = getpedigree(root.getFemaleId(), "female");
		Cultivar maleParent = getpedigree(root.getMaleId(), "male");
		String un = "Unknown";
		boolean bothparentsUnknown = false;

		if (maleParent != null) {
			parents.add(maleParent);
			bothparentsUnknown = maleParent.getCultivarName().equals(un);
		}
		if (femaleParent != null) {
			parents.add(femaleParent);
			bothparentsUnknown = bothparentsUnknown && femaleParent.getCultivarName().equals(un);

		}

		if (bothparentsUnknown) {
			parents = new ArrayList<Cultivar>();
		}

		root.setChildof(parents);
		return root;

	}

	public HClusterNode getHCluster(List<String> ids) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, REXPMismatchException, REngineException, SQLException {
		RList glist = getSNP(ids);
		RList alleles = this.validationService.getSnpMetaDataService().getAlleles();
		return HCluster(glist, alleles);
	}

	private int[][] IntMatrix(double[][] mat) {
		int row = mat.length;
		int col = mat[0].length;
		int[][] intmat = new int[row][col];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				intmat[i][j] = (int) mat[i][j];
			}
		}
		return intmat;
	}

	private HClusterNode HCluster(RList glist, RList alleles) throws REXPMismatchException, ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException, REngineException {

		// RList l = new RList();
		// l.put("a", new REXPString("AB_D"));
		// l.put("b", new REXPString("CD-L"));
		eng.assign("data", new REXPList(glist));
		eng.assign("alleles", new REXPList(alleles));
		eng.parseAndEval("source('D:\\\\data-genetics\\\\clint\\\\PCA.R')");

		REXP hc = eng.parseAndEval("hc(data,alleles)");
		System.out.println("hc " + hc.asList());
		int[][] merge = IntMatrix(hc.asList().at(0).asDoubleMatrix());
		double[] height = hc.asList().at(1).asDoubles();
		int[] order = hc.asList().at(2).asIntegers();
		String[] labels = hc.asList().at(3).asStrings();
		ArrayList<HClusterNode> list = new ArrayList<HClusterNode>();
		for (int index = 0; index < merge.length; index++) {
			String nodeName = "node" + index;
			String parentName = "node" + index;
			if (merge[index][0] < 0 && merge[index][1] < 0) {
				list.add(
						new HClusterNode(nodeName,
								new HClusterNode[] {
										new HClusterNode(labels[-merge[index][0] - 1], parentName, null, 0),
										new HClusterNode(labels[-merge[index][1] - 1], parentName, null, 0) },
								height[index]));
			} else if (merge[index][0] < 0 && merge[index][1] > 0) {

				int child2index = merge[index][1] - 1;
				HClusterNode mergeNode2 = list.get(child2index);
				mergeNode2.setParent(parentName);
				list.add(new HClusterNode(
						"node" + index, new HClusterNode[] {
								new HClusterNode(labels[-merge[index][0] - 1], parentName, null, 0), mergeNode2 },
						height[index]));
			} else if (merge[index][0] > 0 && merge[index][1] < 0) {
				int child1index = merge[index][0] - 1;
				HClusterNode mergeNode1 = list.get(child1index);
				mergeNode1.setParent(parentName);

				list.add(
						new HClusterNode("node" + index,
								new HClusterNode[] { mergeNode1,
										new HClusterNode(labels[-merge[index][0] - 1], parentName, null, 0) },
								height[index]));
			} else {
				int child1index = merge[index][0] - 1;
				HClusterNode mergeNode1 = list.get(child1index);
				mergeNode1.setParent(parentName);

				int child2index = merge[index][1] - 1;
				HClusterNode mergeNode2 = list.get(child2index);
				mergeNode2.setParent(parentName);

				list.add(
						new HClusterNode("node" + index, new HClusterNode[] { mergeNode1, mergeNode2 }, height[index]));
			}
		}

		return list.get(list.size() - 1);

	}

	// // save file
	// public String saveUploadedFiles(List<MultipartFile> files) throws
	// IOException {
	// String UPLOADED_FOLDER = "D://temp//";
	//
	// for (MultipartFile file : files) {
	//
	// if (file.isEmpty()) {
	// continue; // next pls
	// }
	//
	// InputStreamReader r = new InputStreamReader(file.getInputStream());
	// HashMap<String, HashMap<String, String>> snpinfos = new HashMap<String,
	// HashMap<String, String>>();
	// try (BufferedReader br = new BufferedReader(r)) {
	// String[] headers = br.readLine().split("\\s+");
	// if (validheaders(file)) {
	// for (int cultivarIndex = 5; cultivarIndex < headers.length;
	// cultivarIndex++) {
	// HashMap<String, String> snp = new HashMap<String, String>();
	// String cultivarId = headers[cultivarIndex];
	// String line;
	// while ((line = br.readLine()) != null) {
	// String[] splited = line.split("\\s+");
	// String ss = splited[0];
	//
	// String val = splited[cultivarIndex];
	// snp.put(ss, val);
	// }
	// snpinfos.put(cultivarId, snp);
	//
	// }
	// } else {
	// return "Headers are Invalid/empty";
	// }
	// }
	// String k = "sss";
	// }
	// return UPLOADED_FOLDER;
	// }

	public UploadResponse UploadFile(MultipartFile uploadedfile, String fileType, boolean upload) {
		UploadResponse response = new UploadResponse(true);
		File temp = null;
		try {
			String fileName = (fileType != null) ? fileType : "data";
			temp = File.createTempFile(fileName, ".csv");
			java.nio.file.Files.copy(uploadedfile.getInputStream(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);

			if (fileType.equals("snp")) {
				if (validheaders(temp, fileType)) {
					response = this.validateSNP(temp, upload);
				} else {
					response.setSuccess(false);
					response.setMessage("Invalid Headers");
					return response;
				}

			} else if (fileType.equals("cultivar")) {
				if (validheaders(temp, fileType)) {
					response = this.validateCultivar(temp, upload);
				} else {
					response.setSuccess(false);
					response.setMessage("Invalid Headers");
					return response;
				}
			}

		} catch (Exception e) {

			System.out.println(e);

		} finally {
			if (temp != null) {
				temp.delete();
			}
		}

		return response;
	}

	public UploadResponse validateCultivar(File file, boolean upload) {
		SNPInfo info = null;
		UploadResponse uploadResponse = new UploadResponse(true);
		ArrayList<ValidationResponse> validationResponseList = new ArrayList<ValidationResponse>();
		Session session = null;
		Transaction tx = null;
		BufferedReader br = null;
		try {
			if (upload) {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
			}
			br = new BufferedReader(new FileReader(file));
			String[] headers = br.readLine().split("\\,", -1);
            int lineno = 1;
			String line;
			ValidationResponse response = null;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split("\\,", -1);
				response = new ValidationResponse(true);

				if (!this.validationService.CultivarIdorNameExist(headers, splited)) {
					response.setCultivarId("row number : " + lineno);
					response.setMessage("Cultivar Id and Name, both can not be blank.");
					response.setValid(false);
				}

				Cultivar cultivar = new Cultivar();
				if (response.isValid()) {
					response = validateCultivar(cultivar, response, splited, headers);
				}

				if (upload && response.isValid()) {
					response = InsertorUpdateCultivar(cultivar, response, session);
				}

				validationResponseList.add(response);

				if (!response.isValid()) {

					break;
				}

			}
			uploadResponse.setValidationresponse(validationResponseList);
			if (upload) {
				boolean commit = true;
				for (ValidationResponse validationResponse : validationResponseList) {
					if (!validationResponse.isValid()) {
						tx.rollback();
						commit = false;
					}

				}

				if (commit) {
					tx.commit();
					uploadResponse.setUploaded(true);

				}
			}
			br.close();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			if (session != null) {
				session.close();
			}

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return uploadResponse;
	}

	public ValidationResponse validateCultivar(Cultivar cultivar, ValidationResponse response, String[] splited,
			String[] headers) {
		boolean valid = false;
		boolean lastCultivarValid = true;
		StringBuilder valiationmsg = new StringBuilder("incorrect columns");
		for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
			Headers header = Headers.valueOf(headers[headerIndex]);
			switch (header) {
			case Cultivar_ID:
				if (!splited[headerIndex].trim().isEmpty()) {
					cultivar.setCultivarId(splited[headerIndex]);
				}
				response.setCultivarId(splited[headerIndex]);
				break;
			case Cultivar_Name:
				cultivar.setCultivarName(splited[headerIndex]);

				break;
			case Cultivar_Alias:
				cultivar.setCultivarAlias(splited[headerIndex]);

				break;
			case Female_ID:
				cultivar.setFemaleId(splited[headerIndex]);

				break;
			case Female_Name:
				cultivar.setFemaleName(splited[headerIndex]);
				break;
			case Male_ID:
				cultivar.setMaleId(splited[headerIndex]);

				break;
			case Male_Name:
				cultivar.setMaleName(splited[headerIndex]);

				break;
			case Subcollection: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {
					Subcollection validateSubcollection = validationService.validateSubcollection(uploadedValue);
					if (validateSubcollection != null) {
						cultivar.setSubcollection(validateSubcollection);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case State:
				cultivar.setStateName(splited[headerIndex]);

				break;
			case Country: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {
					Country validCountry = validationService.validateCountry(uploadedValue);
					if (validCountry != null) {
						cultivar.setCountry(validCountry);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Year:
				cultivar.setYear(splited[headerIndex]);

				break;
			case Pedigree_Original:
				// cultivar.setCultivarId(splited[headerIndex]);

				break;
			case MG: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					MaturityGroup maturityGroup = validationService.validateMaturityGroup(uploadedValue);
					if (maturityGroup != null) {
						cultivar.setMaturityGroup(maturityGroup);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Stem_Termination: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					StemTermination stemTermination = validationService.validateStemTermination(uploadedValue);
					if (stemTermination != null) {
						cultivar.setStemTermination(stemTermination);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Flower_Color: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					FlowerColor flowerColor = validationService.validateFlowerColor(uploadedValue);
					if (flowerColor != null) {
						cultivar.setFlowerColor(flowerColor);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Pubescence_Color: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					PubescenceColor pubescenceColor = validationService.validatePubescenceColor(uploadedValue);

					if (pubescenceColor != null) {
						cultivar.setPubescenceColor(pubescenceColor);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Pubescence_Density: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					PubescenceDensity pubescenceDensity = validationService.validatePubescenceDensity(uploadedValue);

					if (pubescenceDensity != null) {
						cultivar.setPubescenceDensity(pubescenceDensity);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Pubescence_Form: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					PubescenceForm pubescenceForm = validationService.validatePubescenceForm(uploadedValue);

					if (pubescenceForm != null) {
						cultivar.setPubescenceForm(pubescenceForm);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Pod_Color: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {
					PodColor PodColor = validationService.validatePodColor(uploadedValue);
					if (PodColor != null) {
						cultivar.setPodColor(PodColor);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Seedcoat_Color: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					SeedcoatColor seedcoatColor = validationService.validateSeedcoatColor(uploadedValue);
					if (seedcoatColor != null) {
						cultivar.setSeedcoatColor(seedcoatColor);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Seedcoat_Luster: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					SeedcoatLuster seedcoatLuster = validationService.validateSeedcoatLuster(uploadedValue);
					if (seedcoatLuster != null) {
						cultivar.setSeedcoatLuster(seedcoatLuster);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}
			case Hilum_Color: {
				String uploadedValue = splited[headerIndex];
				if (uploadedValue.trim().isEmpty()) {
					break;
				} else {

					HilumColor HilumColor = validationService.validateHilumColor(uploadedValue);
					if (HilumColor != null) {
						cultivar.setHilumColor(HilumColor);
					} else {
						lastCultivarValid = false;
						response.setValid(false);
						valiationmsg.append("\n").append(headers[headerIndex]);
					}
					break;
				}
			}

			case Other_Leaf:
				cultivar.setOtherLeaf(splited[headerIndex]);

				break;
			case Other_Plant:
				cultivar.setOtherPlant(splited[headerIndex]);

				break;
			case Other_Seed:
				cultivar.setOtherseeds(splited[headerIndex]);

				break;

			}
		}

		if (!response.isValid()) {

			response.setMessage(valiationmsg.toString());
		}
		return response;
	}

	public ValidationResponse InsertorUpdateCultivar(Cultivar cultivar, ValidationResponse response, Session session) {
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;
		boolean success = false;

		try {
			CultivarMetaDataService cultivarMetaDataService = this.validationService.getCultivarMetaDataService();
			// session = sessionFactory.getCurrentSession();

			if (cultivarMetaDataService.CultivarIdExist(cultivar.getCultivarId())) {
				session.update(cultivar);
				session.flush();
				response.setCultivarId(cultivar.getCultivarId());
				response.setValid(true);

			} else {
				if (cultivarMetaDataService.CultivarNameAlreadyExist(cultivar.getCultivarName())) {
					response.setCultivarId(cultivar.getCultivarName());
					response.setValid(false);
					response.setMessage("duplicate name " + cultivar.getCultivarId());
				} else {
					session.save(cultivar);
					session.flush();
					session.clear();
					response.setCultivarId(cultivar.getCultivarId());
					response.setValid(true);

				}
			}
		} catch (Exception e) {
			System.out.println(e);
			response.setValid(false);
		}

		return response;
	}

	public ValidationResponse validateSNP(HashMap<String, String[]> allelesSnpNameMap, SNPInfo info,
			int cultivarindex) {
		String cultivarid = info.cultivarNames.get(cultivarindex);
		StringBuilder validationmsg = new StringBuilder();
		ValidationResponse response = new ValidationResponse(true);
		response.setCultivarId(cultivarid);
		try{
			for (int snpindex = 0; snpindex < info.snpNamesIndex.size(); snpindex++) {
				if(snpindex == 42289 ){
					System.out.println("stop");
				}
			String snpName = info.IndexSnpNames.get(snpindex);
			String snpValue = info.snp[snpindex][cultivarindex];
			String[] alleles = allelesSnpNameMap.get(snpName);
			if (alleles != null) {
				boolean matched = false;
				for (String allele : alleles) {
					if (allele.equals(snpValue)) {
						matched = true;
						break;
					}
				}
				if (!matched) {
					response.setValid(false);
					validationmsg.append(snpName + ": " + snpValue + ". Correct Options:" + Arrays.toString(alleles))
							.append("\n");
				}
			}

		}}catch(Exception e){
			System.out.println(e);
		}
		if (!response.isValid()) {
			response.setMessage(validationmsg.toString());
		}
		return response;
	}

	public UploadResponse validateSNP(File file, boolean upload) {
		boolean commit = true;
		SNPInfo info = null;
		Session session = null;
		Transaction tx = null;
		BufferedReader br = null;
		UploadResponse uploadResponse = new UploadResponse(true);
		uploadResponse.setMessage("Validation Success!!");
		ArrayList<ValidationResponse> validationResponseList = new ArrayList<ValidationResponse>();
		HashMap<String, String[]> allelesSnpNameMap = this.validationService.getSnpMetaDataService()
				.getSnpNameAllelesMap();
		try {
			if (upload) {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
			}
			br = new BufferedReader(new FileReader(file));
			// MarkerinfoMaps markerinfoMaps = getalleles();
			info = getSnpInfo(file);
			for (int cultivarindex = 0; cultivarindex < info.cultivarNames.size(); cultivarindex++) {
				ValidationResponse validationResponse = validateSNP(allelesSnpNameMap, info, cultivarindex);

				if (upload && validationResponse.isValid()) {
					Snp snp = getCompleteSnp(info, cultivarindex);
					insertorUpdateSnp(session, snp, validationResponse);
				}
				validationResponseList.add(validationResponse);
				if (!validationResponse.isValid()) {
					commit = false;
					uploadResponse.setSuccess(false);
					uploadResponse.setMessage("Validation error!!!");
					break;
				}
			}
			
			uploadResponse.setValidationresponse(validationResponseList);
			
			if (upload) {
				if(commit){
					uploadResponse.setUploaded(true);
					uploadResponse.setMessage("Uploaded!!!");
					tx.commit();
				}else{
					uploadResponse.setUploaded(false);
					uploadResponse.setMessage("Upload failed!!!");
					tx.rollback();
				}
				
			}

			System.out.println("done");

		} catch (HibernateException e) {
			uploadResponse.setSuccess(false);
			uploadResponse.setMessage("Upload failed!!!");
			if (tx != null)
				tx.rollback();
			System.out.println(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
			}
		}
		return uploadResponse;
	}

	public Snp getCompleteSnp(SNPInfo info, int cultivarindex) {
		Snp snp = new Snp();
		snp.setId(info.cultivarNames.get(cultivarindex));
		HashMap<Integer, String> snpNameMap = this.validationService.getSnpMetaDataService().getSnpNameMap();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= snpNameMap.size(); i++) {
			String snpname = snpNameMap.get(i);
			Integer snpindex = info.snpNamesIndex.get(snpname);
			if (snpindex == null) {
				sb.append("-");
			} else {

				String snpValue = info.snp[snpindex][cultivarindex];
				sb.append(snpValue);
			}
		}
		snp.setSnp(sb.toString());
		return snp;
	}

	public ValidationResponse insertorUpdateSnp(Session session, Snp snp, ValidationResponse validationResponse) {
		// String selectSQL = null;
		// PreparedStatement preparedStatement = null;

		try {
			SnpMetaDataService snpMetaDataService = this.validationService.getSnpMetaDataService();
			// session = sessionFactory.getCurrentSession();

			if (snpMetaDataService.CultivarIdExist(snp.getId())) {
				session.update(snp);
				session.flush();
				session.clear();
				validationResponse.setValid(true);

			} else {

				session.save(snp);
				session.flush();
				session.clear();
				validationResponse.setValid(true);

			}
		} catch (Exception e) {
			System.out.println(e);
			validationResponse.setValid(false);
		}

		return validationResponse;
	}

	public static SNPInfo getSnpInfo(File file) {
		SNPInfo info = new SNPInfo();
		info.snpNamesIndex = new HashMap<String, Integer>();
		info.IndexSnpNames = new HashMap<Integer, String>();
		info.cultivarNames = new HashMap<Integer, String>();
		int row = countFileRows(file, true);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] headers = br.readLine().split("\\,", -1);

			String[] cultivars = Arrays.copyOfRange(headers, 3, headers.length);
			for (int i = 0; i < cultivars.length; i++) {
				info.cultivarNames.put(i, cultivars[i]);
			}

			String line;
			int lineno = 0;
			info.snp = new String[row][cultivars.length];

			while ((line = br.readLine()) != null) {
				String[] splited = line.split("\\,", -1);
				String ss = splited[2];
				info.snpNamesIndex.put(ss, lineno);
				info.IndexSnpNames.put(lineno, ss);
				int offset = 3;
				for (int cultivarIndex = 0; cultivarIndex < cultivars.length; cultivarIndex++) {
					info.snp[lineno][cultivarIndex] = splited[cultivarIndex + offset];
				}

				lineno++;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		
		}
		return info;
	}

	private static int countFileRows(File file, boolean containsheaders) {
		if (file == null) {
			return 0;
		}
		int lines = containsheaders ? 0 : 1;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while (br.readLine() != null)
				lines++;
		} catch (FileNotFoundException e) {
			System.out.println(e);

		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		return lines;
	}

	@SuppressWarnings("resource")
	private boolean validheaders(File file, String fileType) {
		Boolean isValid = true;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));

			String[] headers = br.readLine().split(",");

			for (int i = 0; i < headers.length; i++) {
				if (headers[i].trim().isEmpty()) {
					return false;
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isValid;

	}
}

class MarkerinfoMaps {
	public RList alleles;
	HashMap<Long, String> snpnameMap;
	HashMap<String, String[]> allelessnpNameMap;
}

class SNPInfo {
	public HashMap<Integer, String> IndexSnpNames;
	public HashMap<String, Integer> snpNamesIndex;
	public HashMap<Integer, String> cultivarNames;
	public String[][] snp;

}

class CultivarInfo {
	public HashMap<String, Integer> country;
	public HashMap<String, Integer> flowerColor;
	public HashMap<String, Integer> hilumColor;
	public HashMap<String, Integer> maturityGroup;
}

enum Headers {
	Cultivar_ID, Cultivar_Name, Cultivar_Alias, Female_ID, Female_Name, Male_ID, Male_Name, Subcollection, State, Country, Year, Pedigree_Original, MG, Stem_Termination, Flower_Color, Pubescence_Color, Pubescence_Form, Pubescence_Density, Pod_Color, Seedcoat_Luster, Seedcoat_Color, Hilum_Color, Other_Seed, Other_Leaf, Other_Plant;
}
