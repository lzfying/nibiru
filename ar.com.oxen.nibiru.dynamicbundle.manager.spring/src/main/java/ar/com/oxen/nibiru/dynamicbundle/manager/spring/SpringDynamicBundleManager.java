package ar.com.oxen.nibiru.dynamicbundle.manager.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.springframework.osgi.context.BundleContextAware;

import ar.com.oxen.commons.exception.api.ExceptionWrapper;
import ar.com.oxen.commons.exception.api.IOExceptionWrapper;
import ar.com.oxen.nibiru.dynamicbundle.dao.api.DynamicBundleDao;
import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;
import ar.com.oxen.nibiru.dynamicbundle.domain.BundleReference;
import ar.com.oxen.nibiru.dynamicbundle.manager.api.DynamicBundleManager;

public class SpringDynamicBundleManager implements DynamicBundleManager,
		BundleContextAware, Initializable {
	private DynamicBundleDao dynamicBundleDao;
	private BundleContext bundleContext;

	public void init() {
		//this.createMockData();
		for (DynamicBundle dynamicBundle : this.dynamicBundleDao.findAll()) {
			this.start(dynamicBundle);
		}
	}

	@Override
	public void start(DynamicBundle dynamicBundle) {
		try {
			Bundle bundle = this.bundleContext.installBundle(
					"inputstream:dummy", this.createBundle(dynamicBundle));
			bundle.start();
		} catch (BundleException e) {
			throw new ExceptionWrapper(e);
		}
	}

	@Override
	public void stop(DynamicBundle dynamicBundle) {
		try {
			for (Bundle bundle : this.bundleContext.getBundles()) {
				if (bundle.getSymbolicName().equals(
						dynamicBundle.getSymbolicName())) {
					bundle.stop();
					bundle.uninstall();
					break;
				}
			}
		} catch (BundleException e) {
			throw new ExceptionWrapper(e);
		}
	}

	private InputStream createBundle(final DynamicBundle dynamicBundle) {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(bos);
			PrintWriter zipWriter = new PrintWriter(zip);
			/* Write manifest */
			zip.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
			zipWriter.println("Manifest-Version: 1.0");
			zipWriter.println("Bundle-ManifestVersion: 2");
			zipWriter.println("Bundle-Name: " + dynamicBundle.getName());
			zipWriter.println("Bundle-SymbolicName: "
					+ dynamicBundle.getSymbolicName());
			zipWriter.println("Bundle-Version: 1.0.0");
			if (dynamicBundle.getRequiredBundles().size() > 0) {
				zipWriter.print("Require-Bundle: ");
				boolean isFirst = true;
				for (BundleReference requiredBundle : dynamicBundle
						.getRequiredBundles()) {
					if (!isFirst) {
						zipWriter.print(",");
					}
					isFirst = false;
					zipWriter.print(requiredBundle.getSymbolicName());
				}
				zipWriter.println();
			}
			zipWriter.flush();
			zip.closeEntry();
			/* Write context */
			zip.putNextEntry(new ZipEntry("META-INF/spring/context.xml"));
			zipWriter.print(dynamicBundle.getDeclaration());
			zipWriter.flush();
			zip.closeEntry();
			/* Flush and close */
			zip.close();
			return new ByteArrayInputStream(bos.toByteArray());
		} catch (IOException e) {
			throw new IOExceptionWrapper(e);
		}
	}

	public void createMockData() {
		DynamicBundle dm = new DynamicBundle();

		dm.setName("My first service");

		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n");
		sb.append("	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append("	xmlns:lang=\"http://www.springframework.org/schema/lang\"\n");
		sb.append("	xmlns:osgi=\"http://www.springframework.org/schema/osgi\"\n");
		sb.append("	xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n");
		sb.append("	http://www.springframework.org/schema/beans/spring-beans.xsd\n");
		sb.append("	http://www.springframework.org/schema/osgi http://www.springframework.org/schema/osgi/spring-osgi.xsd\n");
		sb.append("    http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang-2.0.xsd\">\n");

		sb.append("	<osgi:service ref=\"sub1\" interface=\"ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension\">\n");
		sb.append("		<osgi:service-properties>\n");
		sb.append("			<entry key=\"extensionPoint\" value=\"ar.com.oxen.nibiru.menu\"/>\n");
		sb.append("		</osgi:service-properties>\n");
		sb.append("	</osgi:service>\n");
		sb.append("	<osgi:service ref=\"item1\" interface=\"ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension\">\n");
		sb.append("		<osgi:service-properties>\n");
		sb.append("			<entry key=\"extensionPoint\" value=\"ar.com.oxen.nibiru.menu\"/>\n");
		sb.append("		</osgi:service-properties>\n");
		sb.append("	</osgi:service>\n");
		sb.append("	<osgi:service ref=\"item2\" interface=\"ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension\">\n");
		sb.append("		<osgi:service-properties>\n");
		sb.append("			<entry key=\"extensionPoint\" value=\"menuFacturacion\"/>\n");
		sb.append("		</osgi:service-properties>\n");
		sb.append("	</osgi:service>\n");

		sb.append("    <osgi:reference id=\"eventBus\" interface=\"ar.com.oxen.commons.eventbus.api.EventBus\"/>\n");

		sb.append("<lang:groovy id=\"sub1\">\n");
		sb.append("    <lang:inline-script>\n");
		sb.append("package com.kckkc;\n");
		sb.append("import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;\n");
		sb.append("class UnaPrueba implements SubMenuExtension {\n");
		sb.append("  	public String getName() {\n");
		sb.append("        return \"Sub-menu\";\n");
		sb.append("    }\n");
		sb.append("    public String getExtensionPoint() {\n");
		sb.append("    	return \"menuFacturacion\";\n");
		sb.append("    }\n");
		sb.append("    public int getPosition() {\n");
		sb.append("    	return 0;\n");
		sb.append("    }\n");
		sb.append("}\n");
		sb.append("    </lang:inline-script>\n");
		sb.append("</lang:groovy>\n");

		sb.append("<lang:groovy id=\"item1\">\n");
		sb.append("    <lang:inline-script>\n");
		sb.append("package com.kckkc;\n");
		sb.append("import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;\n");
		sb.append("import ar.com.oxen.commons.eventbus.api.EventBus;\n");
		sb.append("class UnaPrueba implements MenuItemExtension {\n");
		sb.append("	EventBus eventBus;\n");
		sb.append("  	public String getName() {\n");
		sb.append("        return \"Item uan\";\n");
		sb.append("    }\n");
		sb.append("    public int getPosition() {\n");
		sb.append("    	return 1;\n");
		sb.append("    }\n");
		sb.append("    public void onClick() {\n");
		sb.append("    	System.out.println(\"Matanga\");\n");
		sb.append("    	eventBus.fireEvent(\"jelou\");\n");
		sb.append("    }\n");
		sb.append("}\n");
		sb.append("    </lang:inline-script>\n");
		sb.append("    <lang:property name=\"eventBus\" ref=\"eventBus\" />\n");
		sb.append("</lang:groovy>\n");

		sb.append("<lang:groovy id=\"item2\">\n");
		sb.append("    <lang:inline-script>\n");
		sb.append("package com.kckkc;\n");
		sb.append("import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;\n");
		sb.append("class UnaPrueba implements MenuItemExtension {\n");
		sb.append("  	public String getName() {\n");
		sb.append("        return \"Item dos\";\n");
		sb.append("    }\n");
		sb.append("    public int getPosition() {\n");
		sb.append("    	return 0;\n");
		sb.append("    }\n");
		sb.append("    public void onClick() {\n");
		sb.append("    	System.out.println(\"Cliqueo dos\");\n");
		sb.append("    }\n");
		sb.append("}\n");
		sb.append("    </lang:inline-script>\n");
		sb.append("</lang:groovy>\n");

		sb.append("</beans>\n");

		dm.setDeclaration(sb.toString());

		dm.setRequiredBundles(new LinkedList<BundleReference>());
		this.addRequiredBundle("ar.com.oxen.commons", dm);
		this.addRequiredBundle("groovy", dm);
		this.addRequiredBundle("org.springframework.context", dm);
		this.addRequiredBundle("ar.com.oxen.nibiru.ui.api", dm);

		this.dynamicBundleDao.update(dm);
	}

	private void addRequiredBundle(String name, DynamicBundle dm) {
		BundleReference br = new BundleReference();
		br.setSymbolicName(name);
		dm.getRequiredBundles().add(br);
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void setDynamicBundleDao(DynamicBundleDao dynamicBundleDao) {
		this.dynamicBundleDao = dynamicBundleDao;
	}
}
