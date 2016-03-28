package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
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

	private Paginador pagination = new Paginador();
	private String nameSearch;

	private List<MeasurementUnits> listMeasurementUnits;

	/**
	 * @return measurementUnits: object that contains the data of measurement
	 *         Units.
	 */
	public MeasurementUnits getMeasurementUnits() {
		return measurementUnits;
	}

	/**
	 * @param measurementUnits
	 *            : object that contains the data of measurement Units.
	 */
	public void setMeasurementUnits(MeasurementUnits measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	/**
	 * @return pagination: Management paged list names measurementUnits.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list names measurementUnits.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Units of measurement name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Units of measurement name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listMeasurementUnits: list of objects of type Measurement Units.
	 */
	public List<MeasurementUnits> getListMeasurementUnits() {
		return listMeasurementUnits;
	}

	/**
	 * @param listMeasurementUnits
	 *            : list of objects of type Measurement Units.
	 */
	public void setListMeasurementUnits(
			List<MeasurementUnits> listMeasurementUnits) {
		this.listMeasurementUnits = listMeasurementUnits;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of measurementUnits.
	 * 
	 * @return consultMeasurementUnits: method that allows consulting the
	 *         measurementUnits returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultMeasurementUnits();
	}

	/**
	 * Consult the list of the Measurement Units.
	 * 
	 * @return "gesMeasurementUnits": redirects to the template to manage the
	 *         Measurement Units.
	 */
	public String consultMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMeasurementUnits = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listMeasurementUnits = new ArrayList<MeasurementUnits>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = measurementUnitsDao.quantityMeasurementUnits(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listMeasurementUnits = measurementUnitsDao.consultMeasurementUnits(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listMeasurementUnits == null || listMeasurementUnits.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listMeasurementUnits == null
					|| listMeasurementUnits.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMeasurementUnits
										.getString("measurement_units_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
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
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(mu.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');

		}

	}

	/**
	 * Method to edit or create a new MeasurementUnits.
	 * 
	 * @param measurementUnits
	 *            :Measurement Units to be add or edit.
	 * 
	 * @return "regMeasurementUnits":redirected to the template record
	 *         Measurement Units.
	 */
	public String addEditMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {

		if (measurementUnits != null) {
			this.measurementUnits = measurementUnits;

		} else {
			this.measurementUnits = new MeasurementUnits();

		}
		return "regMeasurementUnits";
	}

	/**
	 * Method used to save or edit MeasurementUnits.
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultMeasurementUnits: Measurement Units redirects to manage
	 *         the list of updated Measurement Units.
	 */
	public String saveMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";

		try {

			if (measurementUnits.getIdMeasurementUnits() != 0) {
				measurementUnitsDao.editMeasurementUnits(measurementUnits);
			} else {
				messageLog = "message_registro_guardar";
				measurementUnitsDao.saveMeasurementUnits(measurementUnits);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), measurementUnits.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMeasurementUnits();
	}

	/**
	 * Measurement Units redirects to manage the list of updated Measurement
	 * Units.
	 * 
	 * @return consultMeasurementUnits(): Consult the list of measurement Units
	 *         and returns to manage measurement Units.
	 */
	public String removeMeasurementUnits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			measurementUnitsDao.removeMeasurementUnits(measurementUnits);
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

		return consultMeasurementUnits();
	}

	/**
	 * To validate the name of the measurement units, so it is not repeated in
	 * the database and validates against XSS.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = measurementUnits.getIdMeasurementUnits();
			MeasurementUnits measurementUnitsAux = new MeasurementUnits();
			measurementUnitsAux = measurementUnitsDao.nameExists(name, id);
			if (measurementUnitsAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}
