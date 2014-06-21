package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import models.DiagnosticRepresentative;
import models.Doctor;
import models.UploadFile;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class DRController extends Controller {
	static DiagnosticRepresentative loggedInDR = LoginController
			.getLoggedInUser().getDiagnosticRepresentative();
	public static Form<DiagnosticRepresentative> diagnosticRepresentative = Form
			.form(DiagnosticRepresentative.class);
	
	public static Form<UploadFile> fileUpload = Form
	.form(UploadFile.class);

	public static Result addDiagRep() {
		return ok(views.html.diagnostic.diagnosticRep
				.render(diagnosticRepresentative));
	}

	public static Result addDiagRepProcess() {
		final Form<DiagnosticRepresentative> filledForm = diagnosticRepresentative
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			
			return badRequest(views.html.diagnostic.diagnosticRep
					.render(filledForm));
		} else {

			final DiagnosticRepresentative diagForm = filledForm.get();
			
			diagForm.save();
			final String message = flash("success");
			return ok("Saved");
		}
	}

	public static Result diagnosticReplist() {
		List<DiagnosticRepresentative> allDiagRepList = DiagnosticRepresentative.find
				.all();
		return ok(views.html.diagnostic.diagnosticList.render(allDiagRepList));

	}

	public static Result doctorList() {
		List<Doctor> doctorList = Doctor.find.all();
		// Logger.error(doctorList.get(0).appUser.name);
		return ok(views.html.diagnostic.doctorsList.render(doctorList));
	}

	public static Result addDoctor(Long id) {
		Logger.info("id.............." + id);

		if (loggedInDR.doctorList.contains(Doctor.find.byId(id)) != true) {
			
			loggedInDR.doctorList.add(Doctor.find.byId(id));
			Logger.info(loggedInDR.doctorList.get(0).appUser.name+" NAME OF THE DOCTOR");
		}

		return ok(views.html.diagnostic.addDoctor.render(loggedInDR.doctorList));

	}

	// delete doctor from DR list
	public static Result removeDoctor(final Long id) {
		System.out.println("id........." + id);
		int indexOfDoctorList = 0;
		/*Doctor doctor = Doctor.find.byId(id);*/
		Logger.info("size.."+loggedInDR.doctorList.size());
		
		for (Doctor doc : loggedInDR.doctorList) {
			if (id==doc.id) {
				Logger.info("doctor name : " + doc.appUser.name);

				// indexOfDoctorList=loggedInMR.doctorList.indexOf(doctor.appUser.name);

				Logger.info("index is : " + indexOfDoctorList);
				break;
			}
			indexOfDoctorList++;
			
		}

		// return TODO;
		loggedInDR.doctorList.remove(indexOfDoctorList);

		return ok(views.html.diagnostic.addDoctor.render(loggedInDR.doctorList));

	}

	// for searching doctor
	public static Result search() {
		return ok(views.html.diagnostic.searchDoctor.render());
	}

	public static Result searchProcess() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		Logger.info("hello..........................." + searchStr);
		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {
			Logger.info("hello...........................");
			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {
				Logger.info("inside...........................");

				final List<Doctor> doctorList = Doctor.find.where()
						.like("appUser.name", searchStr + "%").findList();

				return ok(views.html.diagnostic.doctorsList.render(doctorList));
			}

		}
		return ok("no doctor present with this name");

	}

	public static Result addPatient() {
		return TODO;
		
	}
	public static Result uploadFile() {
		return ok(views.html.diagnostic.uploadPatientReort.render(fileUpload));
	}
	public static Result uploadFileProcess() {
		UploadFile upload=new UploadFile();
		play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
		  FilePart file = body.getFile("upload");
		  if (file != null) {
		    String fileName = file.getFilename();
		    String contentType = file.getContentType(); 
		    
		    File file1=file.getFile();

	         byte[] bytes = new byte[(int) file1.length()];
	         try {
	               FileInputStream fileInputStream = new FileInputStream(file1);
	               fileInputStream.read(bytes);
	               for (int i = 0; i < bytes.length; i++) {
	                           System.out.print((char)bytes[i]);
	                }
	          } catch (FileNotFoundException e) {
	                      System.out.println("File Not Found.");
	                      e.printStackTrace();
	          }
	          catch (IOException e1) {
	                   System.out.println("Error Reading The File.");
	                    e1.printStackTrace();
	          }
		    upload.fileName=file.getFilename();
		    upload.fileContent =bytes;
		    upload.save();
		    
		    return ok(views.html.diagnostic.download.render(upload));
		  } else {
		    flash("error", "Missing file");
		    return ok("got error while uploading");    
		  }
			
	    }	
	
	public static Result downloadFile() {
		Long id=(long) 2;
	/*UploadFile c1 = UploadFile.find.where().eq("id", id).findUnique();
	byte[] bytes=c1.file;
	//	 Logger.error(doctorList.get(0).appUser.name);
		return ok(views.html.diagnostic.displayReport.render(s));*/
		UploadFile uf = UploadFile.find.byId(id);

		response().setContentType("application/x-download"); 

		response().setHeader("Content-disposition","attachment; filename="+uf.fileName); 

		File file = new File(uf.fileName);

		try {

		FileUtils.writeByteArrayToFile(file, uf.fileContent);

		} catch (IOException e) {

		// TODO Auto-generated catch block

		e.printStackTrace();

		}

		return ok(file);
	}

    }

