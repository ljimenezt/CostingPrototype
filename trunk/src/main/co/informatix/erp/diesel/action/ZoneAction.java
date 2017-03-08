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

import co.informatix.erp.diesel.dao.ZoneDao;
import co.informatix.erp.diesel.entities.Zone;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all related logic with creating, updating and removal of Zone.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ZoneAction implements Serializable {

	@EJB
	private ZoneDao zoneDao;

	private Paginador pagination = new Paginador();

	private Zone zone;

	private List<Zone> zoneList;

	private String nameSearch;

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
	 * @return zone: zone stored in data base.
	 */
	public Zone getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            : zone stored in data base.
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	/**
	 * @return nameSearch :Name by which you want to consult the zone.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to consult the zone.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return zoneList: list of zone stored in data base.
	 */
	public List<Zone> getZoneList() {
		return zoneList;
	}

	/**
	 * @param zoneList
	 *            : list of zone stored in data base.
	 */
	public void setZoneList(List<Zone> zoneList) {
		this.zoneList = zoneList;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultZone: Zone consulting method and redirects to the template
	 *         to manage Zone.
	 */
	public String searchInitialization() {
		this.nameSearch = null;
		this.zone = new Zone();
		return consultZone();
	}

	/**
	 * Consult the list of zone
	 * 
	 * @return gesZone: Navigation rule that redirects to manage zone
	 */
	public String consultZone() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleZones = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		zoneList = new ArrayList<Zone>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = zoneDao.quantityZone(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				zoneList = zoneDao.consultZone(pagination.getInicio(),
						pagination.getRango(), query, parameters);
			}
			if ((zoneList == null || zoneList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (zoneList == null || zoneList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleZones.getString("zone_label_name"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesZone";
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
			consult.append("WHERE UPPER(z.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new zone.
	 * 
	 * @param Zone
	 *            :zone are adding or editing.
	 * @return "regZone":redirected to the template record zone.
	 */
	public String addEditZone(Zone zone) {
		if (zone != null) {
			this.zone = zone;
		} else {
			this.zone = new Zone();
		}
		return "regZone";
	}

	/**
	 * Method used to save or edit the zone.
	 * 
	 * @return contulZone: Redirects to manage zone with a list of updated zone.
	 */
	public String saveZone() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (zone.getId() != 0) {
				zoneDao.editZone(zone);
			} else {
				messageLog = "message_registro_guardar";
				zoneDao.saveZone(zone);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							zone.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultZone();
	}

	/**
	 * Method that allows consultZone to delete one database.
	 * 
	 * @return consultZone: Consult the list of zone and returns to manage Zone.
	 */
	public String deleteZone() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			zoneDao.deleteZone(zone);
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(
							bundle.getString("message_registro_eliminar"),
							zone.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					zone.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultZone();
	}

	/**
	 * This method allow validate the name of the zone, so that it is not
	 * repeated in the database and validates against XSS.
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
			int id = zone.getId();
			Zone zoneAux = zoneDao.nameExists(name, id);
			if (zoneAux != null) {
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
