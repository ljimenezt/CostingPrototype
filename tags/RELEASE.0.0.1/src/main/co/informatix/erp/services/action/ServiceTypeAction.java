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

	private List<ServiceType> listaServiceType;
	private ServiceType serviceType;
	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	/**
	 * @return listaServiceType: Service Type List
	 */
	public List<ServiceType> getListaServiceType() {
		return listaServiceType;
	}

	/**
	 * @param listaServiceType
	 *            : Service Type List
	 */
	public void setListaServiceType(List<ServiceType> listaServiceType) {
		this.listaServiceType = listaServiceType;
	}

	/**
	 * @return serviceType: service type record
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            : service type record
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return paginador: Management paginated list of the types of service.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paginated list of the types of service.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name the type of service to find.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name the type of service to find.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of service type.
	 * 
	 * @return consultarServiceType: method consulting service types, returns to
	 *         the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarServiceType();
	}

	/**
	 * Consult the list of types of service
	 * 
	 * @return "gesServiceType": navigation rule that directs the managed
	 *         service type
	 */
	public String consultarServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaServiceType = new ArrayList<ServiceType>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = serviceTypeDao.cantidadServiceType(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaServiceType = serviceTypeDao.consultarServiceType(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaServiceType == null || listaServiceType.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaServiceType == null || listaServiceType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("tipo_recurso_humano_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
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
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(ec.descripcion) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
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
	public String agregarEditarServiceType(ServiceType serviceType) {
		if (serviceType != null) {
			this.serviceType = serviceType;
		} else {
			this.serviceType = new ServiceType();
		}
		return "regServiceType";
	}

	/**
	 * Method used to save or edit the types of service
	 * 
	 * @return consultarServiceType: Redirects to manage the types of service
	 *         with the list of upgraded service types
	 */
	public String guardarServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (serviceType.getIdServiceType() != 0) {
				serviceTypeDao.editarServiceType(serviceType);
			} else {
				mensajeRegistro = "message_registro_guardar";
				serviceTypeDao.guardarServiceType(serviceType);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					serviceType.getDescripcion()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarServiceType();
	}

	/**
	 * Method for removing a service type of database
	 * 
	 * @return consultarServiceType: See the list of service types and returns
	 *         to manage type of service
	 */
	public String eliminarServiceType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			serviceTypeDao.eliminarServiceType(serviceType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					serviceType.getDescripcion()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					serviceType.getDescripcion());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarServiceType();
	}
}
