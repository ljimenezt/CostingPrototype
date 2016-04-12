package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.informacionBase.dao.UnitConversionDao;
import co.informatix.erp.informacionBase.entities.UnitConversion;
import co.informatix.erp.informacionBase.entities.UnitConversionPK;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * This class implements the logic related to UnitConversion which may be in the
 * database. The logic is to consult, edit or add UnitConversion.
 * 
 * @author Sergio.Gelves
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class UnitConversionAction implements Serializable {
	@EJB
	private UnitConversionDao unitConversionDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;

	private UnitConversion unitConversion;
	private Paginador pagination = new Paginador();

	private List<SelectItem> unitOptions;
	private List<UnitConversion> unitConversionsList;

	private boolean edited;

	private String originalUnitName;
	private String finalUnitName;

	private String originalUnitIdSearch;
	private String finalUnitIdSearch;

	/**
	 * @return unitConversion: Object that contains the data of an unit
	 *         conversion.
	 */
	public UnitConversion getUnitConversion() {
		return unitConversion;
	}

	/**
	 * @param unitConversion
	 *            : Object that contains the data of an unit conversion.
	 */
	public void setUnitConversion(UnitConversion unitConversion) {
		this.unitConversion = unitConversion;
	}

	/**
	 * @return pagination: Paged list of unitConversion.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paged list of unitConversion.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return unitConversionsList: List of objects of Unit Conversion.
	 */
	public List<UnitConversion> getUnitConversionsList() {
		return unitConversionsList;
	}

	/**
	 * @param unitConversionsList
	 *            : List of objects of Unit Conversion.
	 */
	public void setUnitConversionsList(List<UnitConversion> unitConversionsList) {
		this.unitConversionsList = unitConversionsList;
	}

	/**
	 * Get the original unit name of the conversion.
	 * 
	 * @return The unit name.
	 */
	public String getOriginalUnitName() {
		return this.originalUnitName;
	}

	/**
	 * Set the original unit name for the conversion.
	 * 
	 * @param originalUnitName
	 *            : The unit name.
	 */
	public void setOriginalUnitName(String originalUnitName) {
		this.originalUnitName = originalUnitName;
	}

	/**
	 * Get the final unit name of the conversion.
	 * 
	 * @return The unit name.
	 */
	public String getFinalUnitName() {
		return this.finalUnitName;
	}

	/**
	 * Set the final unit name for the conversion.
	 * 
	 * @param finalUnitName
	 *            : The unit name.
	 */
	public void setFinalUnitName(String finalUnitName) {
		this.finalUnitName = finalUnitName;
	}

	/**
	 * Get the unit options to fill a combobox.
	 * 
	 * @return List of SelectItem objects with the names of measurement units
	 *         and their identifiers.
	 */
	public List<SelectItem> getUnitOptions() {
		return this.unitOptions;
	}

	/**
	 * Get the identifier of the original unit for the conversion.
	 * 
	 * @return Measurement unit id.
	 */
	public String getOriginalUnitIdSearch() {
		return this.originalUnitIdSearch;
	}

	/**
	 * Set the identifier of the original unit for the conversion.
	 * 
	 * @param id
	 *            : Measurement unit id.
	 */
	public void setOriginalUnitIdSearch(String id) {
		this.originalUnitIdSearch = id;
	}

	/**
	 * Get the identifier of the final unit for the conversion.
	 * 
	 * @return Measurement unit id.
	 */
	public String getFinalUnitIdSearch() {
		return this.finalUnitIdSearch;
	}

	/**
	 * Set the identifier of the final unit for the conversion.
	 * 
	 * @param id
	 *            : Measurement unit id.
	 */
	public void setFinalUnitIdSearch(String id) {
		this.finalUnitIdSearch = id;
	}

	/**
	 * Know whether a record in the registration process is in the database or
	 * not, to distinguish between modifying or creating a record.
	 * 
	 * @return It returns whether a record is being created or edited.
	 */
	public boolean isEdited() {
		return this.edited;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of Unit Conversion.
	 * 
	 * @return searchUnitConversion: Method that looks for the UnitConversion
	 *         objects, it redirects to the manage Unit Conversion template.
	 */
	public String searchInitialization() {
		this.originalUnitIdSearch = "";
		this.finalUnitIdSearch = "";
		return searchUnitConversion();
	}

	/**
	 * Search for a list of Unit Conversion from database which may be filtered.
	 * 
	 * @return "manUnitConversion": It redirects to the template to manage the
	 *         Unit Conversion.
	 */
	public String searchUnitConversion() {
		this.originalUnitName = "";
		this.finalUnitName = "";
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleUnitConversion = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		unitConversionsList = new ArrayList<UnitConversion>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(query, parameters, bundleUnitConversion,
					jointSearchMessages);
			Long quantity = unitConversionDao.unitConversionAmount(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			unitConversionsList = unitConversionDao.queryUnitConversion(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((unitConversionsList == null || unitConversionsList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (unitConversionsList == null
					|| unitConversionsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())
					&& !"null".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleUnitConversion
										.getString("unit_conversion_label"),
								jointSearchMessages);
			}
			validations.setMensajeBusqueda(searchMessage);
			loadUnitOptions();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manUnitConversion";
	}

	/**
	 * This method builds the query for an advanced search and it also builds
	 * messages displayed depending on the search criteria selected by the user.
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundleUnitConversion
	 *            : Context to access language tags.
	 * @param jointSearchMessages
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundleUnitConversion,
			StringBuilder jointSearchMessages) {
		String identifier = null;
		boolean notOriginalUnit = true;
		try {
			if (this.originalUnitIdSearch != null
					&& !"".equals(this.originalUnitIdSearch)) {
				notOriginalUnit = false;
				query.append("WHERE uc.unitConversionPk.originalUnit.idMeasurementUnits =:originalUnitId ");
				SelectItem item = new SelectItem(this.originalUnitIdSearch,
						"originalUnitId");
				parameters.add(item);

				int idOriginal = Integer.parseInt(this.originalUnitIdSearch);
				MeasurementUnits originalUnit = measurementUnitsDao
						.measurementUnitByID(idOriginal);
				identifier = bundleUnitConversion
						.getString("unit_conversion_label")
						+ ": "
						+ originalUnit.getName() + " - ";
			}
			if (this.finalUnitIdSearch != null
					&& !"".equals(this.finalUnitIdSearch)) {
				if (notOriginalUnit) {
					query.append("WHERE uc.unitConversionPk.finalUnit.idMeasurementUnits =:finalUnitId ");
					identifier = bundleUnitConversion
							.getString("unit_conversion_final_unit") + ": ";
				} else {
					query.append("AND uc.unitConversionPk.finalUnit.idMeasurementUnits =:finalUnitId ");
				}
				SelectItem item = new SelectItem(this.finalUnitIdSearch,
						"finalUnitId");
				parameters.add(item);

				int idFinal = Integer.parseInt(this.finalUnitIdSearch);
				MeasurementUnits finalUnit = measurementUnitsDao
						.measurementUnitByID(idFinal);
				identifier += finalUnit.getName();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		jointSearchMessages.append(identifier);
	}

	/**
	 * Method to edit or create a new Unit Conversion.
	 * 
	 * @param unitConversion
	 *            : Unit Conversion to be added or edited.
	 * 
	 * @return "regUnitConversion": It redirects to the template to manage unit
	 *         conversions.
	 */
	public String addEditUnitConversion(UnitConversion unitConversion)
			throws Exception {
		if (unitConversion != null) {
			this.unitConversion = unitConversion;
			this.originalUnitName = this.unitConversion.getUnitConversionPk()
					.getOriginalUnit().getName();
			this.finalUnitName = this.unitConversion.getUnitConversionPk()
					.getFinalUnit().getName();
			this.edited = true;
		} else {
			this.unitConversion = new UnitConversion();
			this.unitConversion.setUnitConversionPk(new UnitConversionPK());
			this.edited = false;
		}
		loadUnitOptions();
		return "regUnitConversion";
	}

	/**
	 * Method that loads the different units available in the database to do
	 * conversions, they are going to be shown in the unitOptions combobox.
	 */
	public void loadUnitOptions() {
		try {
			List<MeasurementUnits> mus = measurementUnitsDao
					.consultMeasurementsUnits();
			this.unitOptions = new ArrayList<SelectItem>();
			if (mus != null) {
				for (MeasurementUnits mu : mus) {
					this.unitOptions.add(new SelectItem(mu
							.getIdMeasurementUnits(), mu.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to save or edit an UnitConversion.
	 * 
	 * @return searchUnitConversion: It redirects to manage the list of updated
	 *         Unit Conversions.
	 */
	public String saveUnitConversion() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleGeneral = ControladorContexto
				.getBundle("messageBaseInformation");

		try {
			String messageLog = "message_registro_guardar";
			String originalUnit;
			String finalUnit;

			if (this.edited == true) {
				messageLog = "message_registro_modificar";
				unitConversionDao.editUnitConversion(this.unitConversion);
				originalUnit = this.unitConversion.getUnitConversionPk()
						.getOriginalUnit().getName();
				finalUnit = this.unitConversion.getUnitConversionPk()
						.getFinalUnit().getName();
			} else {
				unitConversionDao.saveUnitConversion(this.unitConversion);
				int idOriginal = this.unitConversion.getUnitConversionPk()
						.getOriginalUnit().getIdMeasurementUnits();
				int idFinal = this.unitConversion.getUnitConversionPk()
						.getFinalUnit().getIdMeasurementUnits();
				originalUnit = measurementUnitsDao.measurementUnitByID(
						idOriginal).getName();
				finalUnit = measurementUnitsDao.measurementUnitByID(idFinal)
						.getName();
			}
			String name = bundleGeneral.getString("unit_conversion_label")
					+ ": " + originalUnit + "-" + finalUnit;
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(messageLog), name));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchUnitConversion();
	}

	/**
	 * ITUnit Conversion redirects to manage Unit Conversion with the updated
	 * conversions list after the removal of one unit conversion.
	 * 
	 * @return searchUnitConversion(): It Looks for the list of Unit Conversion
	 *         and redirects to manage Unit Conversion.
	 */
	public String removeUnitConversion() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleGeneral = ControladorContexto
				.getBundle("messageBaseInformation");
		try {
			String originalUnit = this.unitConversion.getUnitConversionPk()
					.getOriginalUnit().getName();
			String finalUnit = this.unitConversion.getUnitConversionPk()
					.getFinalUnit().getName();
			String name = bundleGeneral.getString("unit_conversion_label")
					+ ": " + originalUnit + "-" + finalUnit;
			unitConversionDao.removeUnitConversion(unitConversion);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), name));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchUnitConversion();
	}

	/**
	 * Validate that both units submitted by the view are not repeated in the
	 * database, because they two builds a compound primary key. It also stores
	 * their names in the search parameters: originalUnitSearch and
	 * finalUnitSearch.
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validatePrimaryKey(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundleGeneral = ControladorContexto
				.getBundle("messageBaseInformation");
		int originalUnitId = (int) value;
		String clientId = toValidate.getClientId(context);
		/* It is sent the JSF component ID */
		UIComponent component = toValidate
				.findComponent("formUnitConversion:finalUnit");
		String stringNumber = ((UIInput) component).getSubmittedValue()
				.toString();

		int finalUnitId = stringNumber.equals("") ? 0 : Integer
				.parseInt(stringNumber);
		try {
			this.originalUnitName = measurementUnitsDao.measurementUnitByID(
					originalUnitId).getName();
			this.finalUnitName = measurementUnitsDao.measurementUnitByID(
					finalUnitId).getName();

			UnitConversion auxUnitConversion = unitConversionDao
					.queryPrimaryKey(originalUnitId, finalUnitId);
			if (auxUnitConversion != null && this.edited == false) {
				context.addMessage(
						clientId,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								bundleGeneral
										.getString("unit_conversion_repeated_pk"),
								null));
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
