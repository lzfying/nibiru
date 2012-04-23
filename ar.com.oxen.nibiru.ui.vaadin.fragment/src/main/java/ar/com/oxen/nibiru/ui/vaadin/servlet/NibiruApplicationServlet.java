package ar.com.oxen.nibiru.ui.vaadin.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ar.com.oxen.nibiru.ui.vaadin.api.ApplicationAccessor;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

public class NibiruApplicationServlet extends AbstractApplicationServlet {
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -4897206489141799154L;

	private String beanName = "applicationAccessor";

	@Override
	public void init() throws ServletException {
		super.init();
		String configuredBeanName = this.getServletConfig().getInitParameter(
				"beanName");
		if (configuredBeanName != null) {
			this.beanName = configuredBeanName;
		}
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request)
			throws ServletException {

		ServletContext servletContext = request.getSession()
				.getServletContext();
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);

		ApplicationAccessor accessor = (ApplicationAccessor) context
				.getBean(beanName);
		
		return accessor.getApplication();
	}

	@Override
	protected Class<? extends Application> getApplicationClass()
			throws ClassNotFoundException {
		return Application.class;
	}
}
