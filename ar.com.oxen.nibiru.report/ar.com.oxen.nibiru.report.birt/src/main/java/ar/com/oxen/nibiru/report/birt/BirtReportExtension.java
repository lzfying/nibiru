package ar.com.oxen.nibiru.report.birt;

import java.io.ByteArrayOutputStream;

import ar.com.oxen.nibiru.report.api.ReportExtension;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class BirtReportExtension implements ReportExtension {
	private String name;
	private String file;

	public BirtReportExtension(String name, String file) {
		super();
		this.name = name;
		this.file = file;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] render(String format) {
		try {
			IReportEngine engine = new ReportEngine(new EngineConfig());

			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();

			// Open the report design
			final IReportRunnable design = engine.openReportDesign(classLoader
					.getResourceAsStream(this.file));

			// Create task to run and render the report,
			final IRunAndRenderTask task = engine
					.createRunAndRenderTask(design);

			// Set parent classloader for engine
			task.getAppContext().put(
					EngineConstants.APPCONTEXT_CLASSLOADER_KEY, classLoader);

			final IRenderOption options = new RenderOption();
			options.setOutputFormat(format);

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			options.setOutputStream(output);

			if (options.getOutputFormat().equalsIgnoreCase("html")) {
				final HTMLRenderOption htmlOptions = new HTMLRenderOption(
						options);
				htmlOptions.setImageDirectory("img");
				htmlOptions.setHtmlPagination(false);
				htmlOptions.setHtmlRtLFlag(false);
				htmlOptions.setEmbeddable(false);
				htmlOptions.setSupportedImageFormats("PNG");

				// set this if you want your image source url to be altered
				// If using the setBaseImageURL, make sure to set image handler
				// to HTMLServerImageHandler
				// htmlOptions.setBaseImageURL("http://myhost/prependme?image=");
			} else if (options.getOutputFormat().equalsIgnoreCase("pdf")) {
				final PDFRenderOption pdfOptions = new PDFRenderOption(options);
				pdfOptions.setOption(IPDFRenderOption.PAGE_OVERFLOW,
						IPDFRenderOption.FIT_TO_PAGE_SIZE);
				pdfOptions.setOption(IPDFRenderOption.PAGE_OVERFLOW,
						IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES);
			}

			task.setRenderOption(options);

			// run and render report
			task.run();

			task.close();

			return output.toByteArray();

		} catch (EngineException e) {
			throw new RuntimeException(e);
		}
	}
}
