package ar.com.oxen.nibiru.report.crud;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.report.api.Report;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionTracker;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudField;

public class ReportCrudManager implements CrudManager<Report> {
	private List<Report> reports = new LinkedList<Report>();

	@Override
	public String getEntityTypeName() {
		return Report.class.getName();
	}

	@Override
	public List<CrudField> getListFields() {
		List<CrudField> listFields = new ArrayList<CrudField>(1);
		listFields.add(new SimpleCrudField("reportName", String.class, new SimpleCrudField.SimpleListInfo(500), null));
		return listFields;
	}

	@Override
	public List<CrudField> getFormFields() {
		List<CrudField> formFields = new LinkedList<CrudField>();
		// TODO Auto-generated method stub
		return formFields;
	}

	@Override
	public List<CrudEntity<Report>> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CrudEntity<Report> findById(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CrudEntity<Report>> findByfield(String field, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setExtensionPointManager(
			ExtensionPointManager extensionPointManager) {
		extensionPointManager.registerTracker(new ExtensionTracker<Report>() {
			@Override
			public void onRegister(Report extension) {
				reports.add(extension);
			}

			@Override
			public void onUnregister(Report extension) {
				reports.remove(extension);
			}
		}, Report.EXTENSION_POINT_NAME, Report.class);
	}
}
