package ar.com.oxen.nibiru.dynamicbundle.manager.spring;

public class Initializer {
	private Initializable initializable;

	public void init() {
		this.initializable.init();
	}

	public void setInitializable(Initializable initializable) {
		this.initializable = initializable;
	}

}
