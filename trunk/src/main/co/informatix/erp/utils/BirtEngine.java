package co.informatix.erp.utils;

import java.io.Serializable;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

/**
 * This class contain the birt engine and its methods let to run the engine to
 * generate reports.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
public class BirtEngine implements Serializable {

	private static IReportEngine birtEngine = null;

	/**
	 * This method allows to start the engine birt, needed to build the birt
	 * reports.
	 * 
	 * @return birtEngine: IReportEngine object with the configuration to start
	 *         to work with the birt reports.
	 */
	public static synchronized IReportEngine getBirtEngine() {
		if (birtEngine == null) {
			EngineConfig config = new EngineConfig();
			try {
				Platform.startup(config);
			} catch (BirtException e) {
				ControladorContexto.mensajeError(e);
			}
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			birtEngine = factory.createReportEngine(config);
		}
		return birtEngine;
	}

}