package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * This class allows the logic of MeasurementUnits which may be in the BD The
 * logic is to consult, edit or add MeasurementUnits
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MeasurementUnitsAction implements Serializable {
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;

	private MeasurementUnits measurementUnits;

	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	private List<MeasurementUnits> listaMeasurementUnits;

	/**
	 * @return measurementUnits: object that contains the data of measurement
	 *         Units
	 */
	public MeasurementUnits getMeasurementUnits() {
		return measurementUnits;
	}

	/**
	 * @param measurementUnits
	 *            : object that contains the data of measurement Units
	 */
	public void setMeasurementUnits(MeasurementUnits measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	/**
	 * @return paginador: Management paged list names measurementUnits
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list names measurementUnits
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Units of measurement name to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Units of measurement name to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return listaMeasurementUnits: list of objects of type Measurement Units
	 */
	public List<MeasurementUnits> getListaMeasurementUnits() {
		return listaMeasurementUnits;
	}

	/**
	 * @param listaMeasurementUnits
	 *            : list of objects of type Measurement Units
	 */
	public void setListaMeasurementUnits(
			List<MeasurementUnits> listaMeasurementUnits) {
		this.listaMeasurementUnits = listaMeasurementUnits;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of measurementUnits
	 * 
	 * @return consultarMeasurementUnits: method that allows consulting the
	 *         measurementUnits returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarMeasurementUnits();
	}

	/**
	 * Consult the list of the Measurement Units
	 * 
	 * @return "gesMeasurementUnits": redirects to the template to manage the
	 *         Measurement Units
	 */
	public String consultarMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMeasurementUnits = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMeasurementUnits = new ArrayList<MeasurementUnits>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = measurementUnitsDao.cantidadMeasurementUnits(
					consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaMeasurementUnits = measurementUnitsDao
					.consultarMeasurementUnits(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);
			if ((listaMeasurementUnits == null || listaMeasurementUnits.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMeasurementUnits == null
					|| listaMeasurementUnits.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMeasurementUnits
										.getString("measurement_units_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMeasurementUnits";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * 
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(mu.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');

		}

	}

	/**
	 * Method to edit or create a new MeasurementUnits.
	 * 
	 * @param measurementUnits
	 *            :Measurement Units to be add or edit
	 * 
	 * @return "regMeasurementUnits":redirected to the template record
	 *         Measurement Units.
	 */
	public String agregarEditarMeasurementUnits(
			MeasurementUnits measurementUnits) throws Exception {

		if (measurementUnits != null) {
			this.measurementUnits = measurementUnits;

		} else {
			this.measurementUnits = new MeasurementUnits();

		}
		return "regMeasurementUnits";
	}

	/**
	 * Method used to save or edit MeasurementUnits
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultarMeasurementUnits: Measurement Units redirects to manage
	 *         the list of updated Measurement Units
	 */
	public String guardarMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";

		try {

			if (measurementUnits.getIdMeasurementUnits() != 0) {
				measurementUnitsDao.editarMeasurementUnits(measurementUnits);
			} else {
				mensajeRegistro = "message_registro_guardar";
				measurementUnitsDao.guardarMeasurementUnits(measurementUnits);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					measurementUnits.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMeasurementUnits();
	}

	/**
	 * Measurement Units redirects to manage the list of updated Measurement
	 * Units
	 * 
	 * @return consultarMeasurementUnits(): Consult the list of measurement
	 *         Units and returns to manage measurement Units
	 */
	public String eliminarMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			measurementUnitsDao.eliminarMeasurementUnits(measurementUnits);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					measurementUnits.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					measurementUnits.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarMeasurementUnits();
	}
}
