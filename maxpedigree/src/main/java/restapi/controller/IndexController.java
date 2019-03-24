package restapi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import restapi.model.Cultivar;
import restapi.service.CultivarService;
import restapi.viewmodel.CultivarForm;
import restapi.viewmodel.CultivarResponse;
import restapi.viewmodel.HClusterNode;
import restapi.viewmodel.Pagination;
import restapi.viewmodel.UploadResponse;

@CrossOrigin
@RestController
public class IndexController {
	private CultivarService service;

	@Autowired
	public IndexController(CultivarService cultivarService) {
		this.service = cultivarService;

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
	public String createfile(@RequestParam(name = "ids") ArrayList<String> ids,
			@RequestParam(name = "filetype") String fileType) {
		return this.service.CreateFile(ids, fileType);
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
