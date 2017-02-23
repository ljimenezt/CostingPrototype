package co.informatix.erp.diesel.action;

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

import co.informatix.erp.diesel.dao.ConsumableResourcesDao;
import co.informatix.erp.diesel.entities.ConsumableResources;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * This class is all related logic with creating, updating and removal of
 * ConsumableResources.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ConsumableResourcesAction implements Serializable {

	@EJB
	private ConsumableResourcesDao consumableResourcesDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;

	private Paginador pagination = new Paginador();

	private ConsumableResources consumableResources;

	private String nameSearch;

	private List<ConsumableResources> consumableResourcesList;
	private List<SelectItem> measurementUnitsItems;

	/**
	 * @return pagination: The paging controller object.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : The paging controller object.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return consumableResources: consumableResources stored in data base.
	 */
	public ConsumableResources getConsumableResources() {
		return consumableResources;
	}

	/**
	 * @param consumableResources
	 *            : consumableResources stored in data base.
	 */
	public void setConsumableResources(ConsumableResources consumableResources) {
		this.consumableResources = consumableResources;
	}

	/**
	 * @return consumableResourcesList: list of consumable resources stored in
	 *         data base.
	 */
	public List<ConsumableResources> getConsumableResourcesList() {
		return consumableResourcesList;
	}

	/**
	 * @param consumableResourcesList
	 *            : list of consumable resources stored in data base.
	 */
	public void setConsumableResourcesList(
			List<ConsumableResources> consumableResourcesList) {
		this.consumableResourcesList = consumableResourcesList;
	}

	/**
	 * @return nameSearch :Name by which you want to consult the consumable
	 *         resources.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to consult the consumable resources.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return measurementUnitsItems: Items of the measurement units that are
	 *         displayed in a combo in the user interface.
	 */
	public List<SelectItem> getMeasurementUnitsItems() {
		return measurementUnitsItems;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultConsumableResources: consumableResources consulting method
	 *         and redirects to the template to manage Zone.
	 */
	public String searchInitialization() {
		this.nameSearch = null;
		this.consumableResources = new ConsumableResources();
		return consultConsumableResources();
	}

	/**
	 * This method allows you to load combo measurement units
	 * 
	 * @throws Exception
	 */
	private void loadComboMeasurementUnits() throws Exception {
		measurementUnitsItems = new ArrayList<SelectItem>();
		List<MeasurementUnits> measurementUnits = measurementUnitsDao
				.consultMeasurementsUnits();
		if (measurementUnits != null) {
			for (MeasurementUnits measurement : measurementUnits) {
				measurementUnitsItems.add(new SelectItem(measurement
						.getIdMeasurementUnits(), measurement.getName()));
			}
		}
	}

	/**
	 * Consult the list of consumable resources
	 * 
	 * @return gesCon: Navigation rule that redirects to manage consumable
	 *         resources
	 */
	public String consultConsumableResources() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleConsumableResources = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		consumableResourcesList = new ArrayList<ConsumableResources>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = consumableResourcesDao.quantityConsumableResources(
					query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				consumableResourcesList = consumableResourcesDao
						.consultConsumableResources(pagination.getInicio(),
								pagination.getRango(), query, parameters);
			}
			if ((consumableResourcesList == null || consumableResourcesList
					.size() <= 0) && !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (consumableResourcesList == null
					|| consumableResourcesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleConsumableResources
										.getString("consumable_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCon";
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
			consult.append("WHERE UPPER(cr.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new consumable resource.
	 * 
	 * @param ConsumableResources
	 *            :consumableResource are adding or editing.
	 * @return "regCon":redirected to the template record consumable resources.
	 * @throws Exception
	 */
	public String addEditConsumableResources(
			ConsumableResources consumableResources) throws Exception {
		if (consumableResources != null) {
			this.consumableResources = consumableResources;
		} else {
			this.consumableResources = new ConsumableResources();
		}
		loadComboMeasurementUnits();
		return "regCon";
	}

	/**
	 * Method used to save or edit the consumable resource.
	 * 
	 * @return contulConsumableResources: Redirects to manage consumable
	 *         resource with a list of updated consumable resource.
	 */
	public String saveConsumableResources() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (consumableResources.getId() != 0) {
				consumableResourcesDao
						.editConsumableResources(consumableResources);
			} else {
				messageLog = "message_registro_guardar";
				consumableResourcesDao
						.saveConsumableResources(consumableResources);
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(messageLog),
							consumableResources.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultConsumableResources();
	}

	/**
	 * Method that allows consultConsumableResources to delete one database.
	 * 
	 * @return consultConsumableResources: Consult the list of consumable
	 *         resources and returns to manage Zone.
	 */
	public String deleteConsumableResources() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			consumableResourcesDao
					.deleteConsumableResources(consumableResources);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					consumableResources.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					consumableResources.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultConsumableResources();
	}

	/**
	 * This method allow validate the name of the consumable resource, so that
	 * it is not repeated in the database and validates against XSS.
	 * 
	 * @param context
	 *            : application context.
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
			int id = consumableResources.getId();
			ConsumableResources consumableAux = consumableResourcesDao
					.nameExists(name, id);
			if (consumableAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
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
