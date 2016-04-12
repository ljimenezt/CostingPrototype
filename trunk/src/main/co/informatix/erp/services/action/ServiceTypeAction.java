package co.informatix.erp.services.action;

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

import co.informatix.erp.services.dao.ServiceTypeDao;
import co.informatix.erp.services.entities.ServiceType;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the types of services that can be in the
 * database. The logic is; see, edit, add or delete types of service
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ServiceTypeAction implements Serializable {

	@EJB
	private ServiceTypeDao serviceTypeDao;

	private List<ServiceType> listServiceType;
	private ServiceType serviceType;
	private Paginador pagination = new Paginador();
	private String nameSearch;

	/**
	 * @return listServiceType: Service Type List.
	 */
	public List<ServiceType> getListServiceType() {
		return listServiceType;
	}

	/**
	 * @param listServiceType
	 *            : Service Type List.
	 */
	public void setListServiceType(List<ServiceType> listServiceType) {
		this.listServiceType = listServiceType;
	}

	/**
	 * @return serviceType: service type record.
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            : service type record.
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return pagination: Management paginated list of the types of service.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the types of service.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name the type of service to find.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name the type of service to find.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of service type.
	 * 
	 * @return consultServiceType: method consulting service types, returns to
	 *         the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultServiceType();
	}

	/**
	 * Consult the list of types of service.
	 * 
	 * @return "gesServiceType": navigation rule that directs the managed
	 *         service type.
	 */
	public String consultServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleServices = ControladorContexto
				.getBundle("messageServices");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listServiceType = new ArrayList<ServiceType>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = serviceTypeDao.quantityServiceType(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listServiceType = serviceTypeDao.consultServiceType(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listServiceType == null || listServiceType.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listServiceType == null || listServiceType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleServices.getString("service_type_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesServiceType";
	}

	/**
	 * This method allows to build the query to the advanced search also allows
	 * messages to build the show depending on the search criteria selected by
	 * the user.
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
			consult.append("WHERE UPPER(st.description) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new type of service.
	 * 
	 * @param serviceType
	 *            : type of service you are adding or editing.
	 * @return regServiceType: redirected to the template type of service
	 *         record.
	 */
	public String addEditServiceType(ServiceType serviceType) {
		if (serviceType != null) {
			this.serviceType = serviceType;
		} else {
			this.serviceType = new ServiceType();
		}
		return "regServiceType";
	}

	/**
	 * Method used to save or edit the types of service.
	 * 
	 * @return consultServiceType: Redirects to manage the types of service with
	 *         the list of upgraded service types.
	 */
	public String saveServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (serviceType.getIdServiceType() != 0) {
				serviceTypeDao.editServiceType(serviceType);
			} else {
				messageLog = "message_registro_guardar";
				serviceTypeDao.saveServiceType(serviceType);
			}
			ControladorContexto
					.mensajeInformacion(null, MessageFormat.format(
							bundle.getString(messageLog),
							serviceType.getDescription()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultServiceType();
	}

	/**
	 * Method for removing a service type of database.
	 * 
	 * @return consultServiceType: See the list of service types and returns to
	 *         manage type of service.
	 */
	public String removeServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			serviceTypeDao.removeServiceType(serviceType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					serviceType.getDescription()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					serviceType.getDescription());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultServiceType();
	}
}
