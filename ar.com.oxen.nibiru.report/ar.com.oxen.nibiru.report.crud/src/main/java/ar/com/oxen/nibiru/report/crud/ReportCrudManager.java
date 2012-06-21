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
	private final static String REPORT_NAME_FIELD = "reportName";
	private List<Report> reports = new LinkedList<Report>();

	public ReportCrudManager(ExtensionPointManager extensionPointManager) {
		super();
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

	@Override
	public String getEntityTypeName() {
		return Report.class.getName();
	}

	@Override
	public List<CrudField> getListFields() {
		List<CrudField> listFields = new ArrayList<CrudField>(1);
		listFields.add(new SimpleCrudField(REPORT_NAME_FIELD, String.class,
				new SimpleCrudField.SimpleListInfo(500), null));
		return listFields;
	}

	@Override
	public List<CrudField> getFormFields() {
		List<CrudField> formFields = new LinkedList<CrudField>();
		// TODO Generar los campso en funcion de los parametros del reporte
		return formFields;
	}

	@Override
	public List<CrudEntity<Report>> findAll() {
		return this.toEntity(this.reports);
	}

	@Override
	public CrudEntity<Report> findById(Object id) {
		return new ReportCrudEntity((Report) id);
	}

	@Override
	public List<CrudEntity<Report>> findByfield(String field, Object value) {
		// TODO Filtrar la lista por los campos
		return this.toEntity(this.reports);
	}

	private List<CrudEntity<Report>> toEntity(List<Report> reports) {
		List<CrudEntity<Report>> entities = new ArrayList<CrudEntity<Report>>(
				reports.size());
		for (Report report : reports) {
			entities.add(new ReportCrudEntity(report));
		}
		return entities;
	}

	private static class ReportCrudEntity implements CrudEntity<Report> {
		private Report report;

		public ReportCrudEntity(Report report) {
			super();
			this.report = report;
		}

		@Override
		public Object getId() {
			return report;
		}

		@Override
		public Object getValue(CrudField field) {
			return this.getValue(field.getName());
		}

		@Override
		public Object getValue(String fieldName) {
			if (REPORT_NAME_FIELD.equals(fieldName)) {
				return report.getName();
			} else {
				throw new IllegalArgumentException("Invalid field name: "
						+ fieldName);
			}
		}

		@Override
		public void setValue(CrudField field, Object value) {
			this.setValue(field.getName(), value);
		}

		@Override
		public void setValue(String fieldName, Object value) {
			if (REPORT_NAME_FIELD.equals(fieldName)) {
				throw new IllegalArgumentException(
						"Report name can't be modified " + fieldName);
			} else {
				throw new IllegalArgumentException("Invalid field name: "
						+ fieldName);
			}
		}

		@Override
		public Report getEntity() {
			return report;
		}

		@Override
		public String getEntityTypeName() {
			return Report.class.getName();
		}

		@Override
		public Iterable<Object> getAvailableValues(CrudField field) {
			return this.getAvailableValues(field.getName());
		}

		@Override
		public Iterable<Object> getAvailableValues(String fieldName) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
