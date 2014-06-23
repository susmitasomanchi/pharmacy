package controllers;


import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.DiagnosticCenter;
import models.DiagnosticReport;
import models.DiagnosticTest;


import org.apache.commons.io.FileUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

public class DiagnosticTestController extends Controller{
	
	public static Form<DiagnosticTest> diagnosticTestForm = Form
			.form(DiagnosticTest.class);
	public static Form<DiagnosticCenter> diagnosticForm = Form
			.form(DiagnosticCenter.class);
	public static Form<DiagnosticReport> diagReport = Form
			.form(DiagnosticReport.class);

	
	public static Result addTest(){
		return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));
		
	}
	
	public static Result addTestProcess() {
		final Form<DiagnosticTest> filledForm = diagnosticTestForm
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnostic.addDiagnosticTest.render(filledForm));
		} else {
			
			final DiagnosticTest diagTestForm = filledForm.get();
			Logger.info("*** user object ");
			Long id=LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
			DiagnosticCenter dc=DiagnosticCenter.find.byId(id);
			dc.diagnosticTestList.add(diagTestForm);
			dc.update();
			
			return ok(String.format("Saved product %s", diagTestForm));
		}

	}
	
	public static Result diagnosticTestList(Long id) {
		DiagnosticCenter diagnosticCenter =  DiagnosticCenter.find.byId(id);
		List<DiagnosticTest> diagnosticTestList=diagnosticCenter.diagnosticTestList;
		

		return ok(views.html.diagnostic.diagnosticTestList.render(diagnosticTestList));

	}
	public static Result uploadFile() {
		return ok(views.html.diagnostic.uploadPatientReort.render(diagReport));
	}
	public static Result uploadFileProcess() {
		DiagnosticReport upload=new DiagnosticReport();
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
		DiagnosticReport diagReport = DiagnosticReport.find.byId(id);

		response().setContentType("application/x-download"); 

		response().setHeader("Content-disposition","attachment; filename="+diagReport.fileName); 

		File file = new File(diagReport.fileName);

		try {

		FileUtils.writeByteArrayToFile(file, diagReport.fileContent);

		} catch (IOException e) {

		// TODO Auto-generated catch block

		e.printStackTrace();

		}

		return ok(file);
	}


}
