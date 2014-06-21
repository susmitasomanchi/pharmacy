package controllers;


import java.util.List;

import models.DiagnosticCenter;
import models.DiagnosticRepresentative;
import models.DiagnosticTest;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticTestController extends Controller{
	
	public static Form<DiagnosticTest> diagnosticTestForm = Form
			.form(DiagnosticTest.class);
	public static Form<DiagnosticCenter> diagnosticForm = Form
			.form(DiagnosticCenter.class);

	
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


}
