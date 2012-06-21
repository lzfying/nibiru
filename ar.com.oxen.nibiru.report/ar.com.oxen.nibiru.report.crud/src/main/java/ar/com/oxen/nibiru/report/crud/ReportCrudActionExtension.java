package ar.com.oxen.nibiru.report.crud;

import java.util.ArrayList;
import java.util.List;

import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudAction;
import ar.com.oxen.nibiru.report.api.Report;

public class ReportCrudActionExtension implements CrudActionExtension<Report> {
	private final static String OPEN_REPORT = "openReport";
	private final static String RUN_REPORT = "runReport";

	@Override
	public List<CrudAction> getActions() {
		List<CrudAction> actions = new ArrayList<CrudAction>(2);
		actions.add(new SimpleCrudAction(OPEN_REPORT, true, false, true, false,
				null));
		actions.add(new SimpleCrudAction(RUN_REPORT, true, true, false, true,
				null));
		return actions;
	}

	@Override
	public CrudEntity<Report> performAction(CrudAction action,
			CrudEntity<Report> entity) {
		if (action.getName().equals(OPEN_REPORT)) {
			return entity;
		} else if (action.getName().equals(RUN_REPORT)) {
			// TODO ejecutar el reporte y disparar un evento
			return null;
		} else {
			throw new IllegalArgumentException("Invlaid action: " + action);
		}
	}

	@Override
	public String[] getAllowedRoles() {
		return null;
	}
}
