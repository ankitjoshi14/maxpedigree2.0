package restapi.viewmodel;

public class ValidationResponse {
	boolean valid;
	   String message;
	   String cultivarId;
   public boolean isValid() {
		return valid;
	}
   public ValidationResponse(boolean valid){
	   this.valid = valid;
   }
   
   public ValidationResponse(){}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCultivarId() {
		return cultivarId;
	}

	public void setCultivarId(String cultivarId) {
		this.cultivarId = cultivarId;
	}


   
   



public void appendmessage(String snpname, String incorrectvalues, String[] possiblevalue){
	
}
   
   
}
