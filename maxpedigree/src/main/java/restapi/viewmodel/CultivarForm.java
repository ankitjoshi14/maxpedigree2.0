package restapi.viewmodel;

import org.springframework.web.multipart.MultipartFile;

public class CultivarForm implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private MultipartFile snpFile;
    
    public CultivarForm() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getSnpFile() {
		return snpFile;
	}
	public void setSnpFile(MultipartFile snpFile) {
		this.snpFile = snpFile;
	}
	
    
    
}
