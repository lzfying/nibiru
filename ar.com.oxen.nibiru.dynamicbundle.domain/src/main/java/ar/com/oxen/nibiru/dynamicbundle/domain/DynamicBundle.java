package ar.com.oxen.nibiru.dynamicbundle.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import ar.com.oxen.nibiru.crud.bean.annotation.Action;
import ar.com.oxen.nibiru.crud.bean.annotation.Actions;
import ar.com.oxen.nibiru.crud.bean.annotation.Show;
import ar.com.oxen.nibiru.crud.bean.annotation.Widget;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.WidgetType;

@Entity
@Actions({
		@Action(name = CrudAction.NEW, requiresEntity = false),
		@Action(name = CrudAction.EDIT, requiresEntity = true, showInForm = false),
		@Action(name = CrudAction.UPDATE, requiresEntity = true, showInList = false),
		@Action(name = CrudAction.DELETE, requiresEntity = true, showInForm = false, requiresConfirmation = true) })
public class DynamicBundle {
	@Id
	@GeneratedValue
	@Show(order = 0)
	@Widget(type=WidgetType.TEXT_FIELD, readonly = true)
	private Long id;
	@Column
	@Show(order = 1)
	private String name;
	@OneToMany(cascade = CascadeType.ALL)
	private List<BundleReference> requiredBundles;
	@Column
	@Lob
	@Show(order = 2, inList = false)
	private String declaration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbolicName() {
		return "ar.com.oxen.dynamicbundle." + this.getId();
	}

	public List<BundleReference> getRequiredBundles() {
		return requiredBundles;
	}

	public void setRequiredBundles(List<BundleReference> requiredBundles) {
		this.requiredBundles = requiredBundles;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

}
