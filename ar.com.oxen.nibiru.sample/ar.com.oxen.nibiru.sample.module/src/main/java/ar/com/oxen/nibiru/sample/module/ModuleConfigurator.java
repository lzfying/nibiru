package ar.com.oxen.nibiru.sample.module;

import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.utils.AbstractCrudModuleConfigurator;
import ar.com.oxen.nibiru.report.api.ReportExtension;
import ar.com.oxen.nibiru.report.birt.BirtReportExtension;
import ar.com.oxen.nibiru.sample.domain.Course;
import ar.com.oxen.nibiru.sample.domain.Student;
import ar.com.oxen.nibiru.sample.domain.Subject;
import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleSubMenuExtension;
import ar.com.oxen.nibiru.validation.api.Validator;
import ar.com.oxen.nibiru.validation.generic.NotEmptyValidator;
import ar.com.oxen.nibiru.validation.generic.RegexpValidator;
//import org.eclipse.birt.report.engine.api.EngineException;


public class ModuleConfigurator extends AbstractCrudModuleConfigurator {
	private static final String MENU_EXTENSION = "ar.com.oxen.nibiru.menu.sample.crud";

	private CrudManager<Subject> subjectCrudManager;
	private CrudActionExtension<Subject> subjectCrudActionExtension;

	private CrudManager<Course> courseCrudManager;
	private CrudActionExtension<Course> courseCrudActionExtension;

	private CrudManager<Student> studentCrudManager;
	private CrudActionExtension<Student> studentCrudActionExtension;

	@Override
	protected void configure() {
		this.registerExtension(new SimpleSubMenuExtension("sample.crud",
				MENU_EXTENSION, 1), "ar.com.oxen.nibiru.menu",
				SubMenuExtension.class);

		this.addCrudWithMenu("sample.crud.subject", MENU_EXTENSION,
				this.subjectCrudManager, this.subjectCrudActionExtension);

		this.addChildCrudWithMenu("editCourses", this.subjectCrudManager,
				"subject", this.courseCrudManager,
				this.courseCrudActionExtension);

		this.addCrudWithMenu("sample.crud.student", MENU_EXTENSION,
				this.studentCrudManager, this.studentCrudActionExtension);
		
		this.registerExtension(new RegexpValidator("^Mat.*", "subjectBeginning"),
				Subject.class.getName() + ".name", Validator.class);
		
		this.registerExtension(new NotEmptyValidator(),
				Subject.class.getName() + ".description", Validator.class);
		
		// Prueba con reportes
		ReportExtension report = new BirtReportExtension("myReport",
				"/ar/com/oxen/nibiru/sample/report/myReport.rptdesign");
		System.out.println(new String(report.render("html")));
	}

	public void setSubjectCrudManager(CrudManager<Subject> subjectCrudManager) {
		this.subjectCrudManager = subjectCrudManager;
	}

	public void setSubjectCrudActionExtension(
			CrudActionExtension<Subject> subjectCrudActionExtension) {
		this.subjectCrudActionExtension = subjectCrudActionExtension;
	}

	public void setCourseCrudManager(CrudManager<Course> courseCrudManager) {
		this.courseCrudManager = courseCrudManager;
	}

	public void setCourseCrudActionExtension(
			CrudActionExtension<Course> courseCrudActionExtension) {
		this.courseCrudActionExtension = courseCrudActionExtension;
	}

	public void setStudentCrudManager(CrudManager<Student> studentCrudManager) {
		this.studentCrudManager = studentCrudManager;
	}

	public void setStudentCrudActionExtension(
			CrudActionExtension<Student> studentCrudActionExtension) {
		this.studentCrudActionExtension = studentCrudActionExtension;
	}
}
