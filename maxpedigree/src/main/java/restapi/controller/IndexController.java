package restapi.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.SSLEngineResult.Status;
import javax.print.DocFlavor.URL;
import javax.xml.ws.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.springframework.web.bind.annotation.RestController;
import restapi.service.*;
import restapi.viewmodel.*;
import restapi.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class IndexController {
	private CultivarService service;
	private SnpMetaDataService snpMetaDataService;

	@Autowired
	public IndexController(CultivarService cultivarService, SnpMetaDataService snpMetaDataService) {
		this.service = cultivarService;
		this.snpMetaDataService = snpMetaDataService;
		System.out.println("parameterized index controller");
	}

	@PostMapping("/cultivars")
	public ResponseEntity<CultivarResponse> welcome(@RequestBody Pagination page) {// Welcome
																					// page,

		List<Cultivar> cultivars = null;
		CultivarResponse cultivarResponse = null;

		// List<String> ids = new ArrayList<String>();
		// ids.add("AC-2001");
		// ids.add("PI5485046");

		//
		try {

			cultivarResponse = service.getallCultivars(page);
		} catch (RuntimeException re) {

			throw re;
		}
		return new ResponseEntity<CultivarResponse>(cultivarResponse, HttpStatus.OK);// non-rest

	}

	@PostMapping("/cultivarids")
	public ResponseEntity<CultivarResponse> welcome2(@RequestBody Pagination page) {// Welcome
		// page,
		CultivarResponse cultivarResponse = null;

		// List<String> ids = new ArrayList<String>();
		// ids.add("AC-2001");
		// ids.add("PI5485046");

		//
		try {

			cultivarResponse = service.getallCultivarids(page);
		} catch (RuntimeException re) {

			throw re;
		}
		return new ResponseEntity<CultivarResponse>(cultivarResponse, HttpStatus.OK);// non-rest

	}

	@GetMapping("/pedigree")
	public Cultivar getPedigree(@RequestParam String id) {// Welcome
															// page,

		Cultivar cultivar = null;

		// List<String> ids = new ArrayList<String>();
		// ids.add("AC-2001");
		// ids.add("PI5485046");

		//
		try {

			cultivar = service.getpedigree(id, null);
		} catch (RuntimeException re) {

			throw re;
		}
		return cultivar;// non-rest

	}

	@PostMapping("/pca")
	public List<Cultivar> pca(@RequestBody List<String> ids) {

		try {
			// ArrayList<String> ids = new ArrayList<String>();
			// ids.add("PI103079");
			// ids.add("PI103080");
			return service.getPCA(ids);
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
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/hcluster")
	public HClusterNode hcluster(@RequestBody List<String> ids) {

		try {
			// ArrayList<String> ids = new ArrayList<String>();
			// ids.add("PI103079");
			// ids.add("PI103080");
			return service.getHCluster(ids);
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
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/createfile")
	public String CreateFile(@RequestParam(name = "ids") ArrayList<String> ids,
			@RequestParam(name = "filetype") String fileType) {

		String fileName = null;
		System.out.println("id recived" + ids.get(0) + fileType);

		FileInputStream st = null;
		FileWriter wr = null;
		File file = null;
		FileSystemResource fileres = null;
		HttpHeaders responseHeaders = null;
		ArrayList<Cultivar> cultivarlist = null;
		ArrayList<Snp> snplist = null;
		// Or should I use outputstream here?
		try {
			file = File.createTempFile("data", ".csv");
			wr = new FileWriter(file);
			StringBuilder sb = new StringBuilder();
            sb.append("Cultivar_ID,Cultivar_Name,Subcollection,Country,Year,MG,Stem_Termination").append("\n");
            
			if (fileType.equals("cultivar")) {
				wr.write(sb.toString());
	            wr.flush();
				cultivarlist = service.getallCultivars(ids);
				for (Cultivar cultivar : cultivarlist) {
					sb = new StringBuilder();
					sb.append(cultivar.getCultivarId()  
					+ "," + (cultivar.getCultivarName() !=null ? cultivar.getCultivarName() : "")
					+ "," + (cultivar.getSubcollection() != null? cultivar.getSubcollection().getValue(): "")
					+ "," + (cultivar.getCountry() !=null ? cultivar.getCountry().getValue() : "")
					+ "," + cultivar.getYear()
					+ "," + (cultivar.getMaturityGroup() !=null ? cultivar.getMaturityGroup().getValue() : "")
					+ "," + (cultivar.getStemTermination() !=null ? cultivar.getStemTermination().getValue() : "")).append("\n");
					wr.write(sb.toString());
					wr.flush();
				}

			} else {
				snplist = service.getallSNP(ids);
				HashMap<Integer, String> snpmap = this.snpMetaDataService.getSnpNameMap();
				HashMap<String, String> snpNameChrom = this.snpMetaDataService.getSnpNameChromMap();
				HashMap<String, String> snpNamePos = this.snpMetaDataService.getSnpNamePosMap();
				int rowsCount = snpmap.size() + 1;
				int colsCount = snplist.size() + 3 ;
				String[][] matrix = new String[rowsCount][colsCount];

				matrix[0][0] = "CHROM";
				matrix[0][1] = "POS";
				matrix [0][2] = "ID";

				for (int col = 0; col < colsCount; col++) {
					Snp snp = null;
					String cultivarid= null;
			
					String[] snps = null;
					if(col > 2){
						 snp = snplist.get(col - 3);
						cultivarid = snp.getId();
						snps = snp.getSnp().split("");	
					}
					
					for (int row = 0; row < rowsCount; row++) {
                       
						if (col == 0) {
							
							String chrom = snpNameChrom.get(snpmap.get(row));
							matrix[row][col] = (row ==0 )? "CHROM" :  snpNameChrom.get(snpmap.get(row-1));
							
						} else if (col == 1) {
							String pos = snpNamePos.get(snpmap.get(row));
							matrix[row][col] = (row == 0) ?  "POS" : snpNamePos.get(snpmap.get(row-1));
						} else if(col == 2){
							String snpName = snpmap.get(row);
							matrix[row][col] = (row == 0) ? "ID" : snpmap.get(row-1) ;
						}else{
							matrix[row][col] = (row == 0) ? cultivarid :  snps[row - 1]; 
						}

					}
				}
				
				StringBuilder sb2 = null;
				for(int i=0; i< matrix.length ; i++){
					sb2 = new StringBuilder(matrix[0].length);
					for (int j =0 ; j < matrix[0].length ; j++){
						sb2.append(matrix[i][j]).append(",");
					}
					sb2.append("\n");
					wr.write(sb2.toString());
					wr.flush();
				}
				
				
			}

			

			wr.close();

			// fileres = new
			// FileSystemResource("D:\\\\data-genetics\\\\soysnp50K_wm82.a1_41417.txt");
			fileres = new FileSystemResource(file);
			fileName = FilenameUtils.removeExtension(fileres.getFilename());

			st = new FileInputStream(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);

		}
		return fileName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/uploadfilesuspeld")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadedfile,
			@RequestParam("name") String name) {

		CultivarForm cultivarForm = new CultivarForm();
		cultivarForm.setName(name);
		cultivarForm.setSnpFile(uploadedfile);
		if (cultivarForm.getSnpFile().isEmpty()) {
			return new ResponseEntity("please select a file!", HttpStatus.OK);
		}

		return new ResponseEntity("Successfully uploaded - " + cultivarForm.getSnpFile().getOriginalFilename(),
				new HttpHeaders(), HttpStatus.OK);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/uploadfile")
	public ResponseEntity uploadDeleteFile(@RequestParam("file") MultipartFile uploadedfile,
			@RequestParam("name") String name, @RequestParam("upload") boolean upload) {

		UploadResponse response = service.UploadFile(uploadedfile, name, upload);
		System.out.println("respose sent");
		if (response.isSuccess()) {
			return new ResponseEntity(response, HttpStatus.OK);
		} else {
			return new ResponseEntity(response, HttpStatus.OK);

		}
	}

	@GetMapping(value = "/downloadfile/{filename}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<FileSystemResource> getFile(@PathVariable("filename") String fileName) throws IOException {
		FileInputStream st = null;
		FileWriter wr = null;
		File file = null;
		FileSystemResource fileres = null;
		HttpHeaders responseHeaders = null;
		ArrayList<Cultivar> list = null;
		// Or should I use outputstream here?
		try {

			// fileres = new FileSystemResource(file);
			String tempPath = System.getProperty("java.io.tmpdir");
			String path = tempPath + File.separator + fileName + ".csv";

			FileSystemResource file2 = new FileSystemResource(path);
			if (file2.exists()) {
				fileres = file2;
			}
			// todo read/write file
			// st = fileres.getInputStream();
			responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Disposition", "attachment; filename=" + "data.csv");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

		return new ResponseEntity<FileSystemResource>(fileres, responseHeaders, HttpStatus.OK);
	}

}
