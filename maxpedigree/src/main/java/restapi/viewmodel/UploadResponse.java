package restapi.viewmodel;

import java.util.ArrayList;

public class UploadResponse {
	String message;
	boolean success;
	boolean uploaded;
	public boolean isUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	ArrayList<ValidationResponse> validationResponses;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ArrayList<ValidationResponse> getValidtionresponses() {
		return validationResponses;
	}

	public void setValidationresponse(ArrayList<ValidationResponse> validationResponses) {
		this.validationResponses = validationResponses;
	}

	public UploadResponse(boolean success) {
		this.success = success;
	}

}
