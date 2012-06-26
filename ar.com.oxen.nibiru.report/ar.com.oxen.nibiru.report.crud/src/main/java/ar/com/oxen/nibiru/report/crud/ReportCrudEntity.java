package ar.com.oxen.nibiru.report.crud;

import java.util.LinkedList;
import java.util.List;

import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import static ar.com.oxen.nibiru.crud.manager.api.CrudField.FormInfo.GENERAL_TAB;
import ar.com.oxen.nibiru.crud.manager.api.WidgetType;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudField;
import ar.com.oxen.nibiru.report.api.Report;

class ReportCrudEntity implements CrudEntity<Report> {
	final static String REPORT_NAME_FIELD = "reportName";
	final static String REPORT_FORMAT_FIELD = "reportFormat";
	private Report report;
	private String format;

	public ReportCrudEntity(Report report) {
		super();
		this.report = report;
	}

	@Override
	public Object getId() {
		return report;
	}

	@Override
	public List<CrudField> getFormFields() {
		List<CrudField> formFields = new LinkedList<CrudField>();

		/* Format field */
		formFields.add(new SimpleCrudField(REPORT_FORMAT_FIELD, String.class,
				null, new SimpleCrudField.SimpleFormInfo(WidgetType.COMBO_BOX,
						false, 0, GENERAL_TAB)));

		// TODO Generar los campso en funcion de los parametros del reporte
		return formFields;
	}

	@Override
	public Object getValue(CrudField field) {
		return this.getValue(field.getName());
	}

	@Override
	public Object getValue(String fieldName) {
		if (REPORT_NAME_FIELD.equals(fieldName)) {
			return this.report.getName();
		} else if (REPORT_FORMAT_FIELD.equals(fieldName)) {
			return this.format;
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
			throw new IllegalArgumentException("Report name can't be modified "
					+ fieldName);
		} else if (REPORT_FORMAT_FIELD.equals(fieldName)) {
			this.format = (String) value;
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
	public Iterable<?> getAvailableValues(CrudField field) {
		return this.getAvailableValues(field.getName());
	}

	@Override
	public Iterable<?> getAvailableValues(String fieldName) {
		if (REPORT_FORMAT_FIELD.equals(fieldName)) {
			return this.report.getFormats();
		} else {
			throw new IllegalArgumentException(
					"Field with no available values: " + fieldName);
		}
	}

}