package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, update, and delete types
 * of human resources in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HrTypesAction implements Serializable {

	private List<HrTypes> listaHrTypes;
	private Paginador paginador = new Paginador();
	private HrTypes hrTypes;
	private String nombreBuscar;

	@EJB
	private HrTypesDao hrTypesDao;

	/**
	 * @return listaHrTypes: gets the list of types of human resources
	 */
	public List<HrTypes> getListaHrTypes() {
		return listaHrTypes;
	}

	/**
	 * @param listaHrTypes
	 *            : gets the list of types of human resources
	 */
	public void setListaHrTypes(List<HrTypes> listaHrTypes) {
		this.listaHrTypes = listaHrTypes;
	}

	/**
	 * @return paginador: paginated list of the types of human resources which
	 *         may have in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : paginated list of the types of human resources which may
	 *            have in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return hrTypes: object the types of human resources
	 */
	public HrTypes getHrTypes() {
		return hrTypes;
	}

	/**
	 * @param hrTypes
	 *            : object the types of human resources
	 */
	public void setHrTypes(HrTypes hrTypes) {
		this.hrTypes = hrTypes;
	}

	/**
	 * @return nombreBuscar: gets the name by which you want to consult types of
	 *         human resources
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : gets the name by which you want to consult types of human
	 *            resources
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of types of human resources
	 * 
	 * @return consultarHrTypes: method that queries resource types human,
	 *         returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarHrTypes();
	}

	/**
	 * Consult the list of hrTypes
	 * 
	 * @return gesHrTypes: Navigation rule that redirects to manage HR types
	 */
	public String consultarHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaHrTypes = new ArrayList<HrTypes>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = hrTypesDao.cantidadHrTypes(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaHrTypes = hrTypesDao.consultarHrTypes(paginador.getInicio(),
					paginador.getRango(), consulta, parametros);
			if ((listaHrTypes == null || listaHrTypes.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaHrTypes == null || listaHrTypes.size() <= 0) {
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
		return "gesHrTypes";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to display depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consulta
	 *            : query to concatenate
	 * @param parametros
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * 
	 * @param unionMensajesBusqueda
	 *            : Message search
	 * 
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consulta.append("WHERE UPPER(ht.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new type of human resources.
	 * 
	 * @param hrTypes
	 *            : name type of human resources to be added or edit
	 * 
	 * @return "regHrTypes": redirected to the template record type human
	 *         Resources.
	 */
	public String agregarEditarHrTypes(HrTypes hrTypes) {
		if (hrTypes != null) {
			this.hrTypes = hrTypes;
		} else {
			this.hrTypes = new HrTypes();
		}
		return "regHrTypes";
	}

	/**
	 * To validate the name of the kinds of human resources, so that no is
	 * repeated in the database and validates against XSS.
	 * 
	 * @param contexto
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value is validated
	 */
	public void validarNombreXSS(FacesContext contexto, UIComponent toValidate,
			Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		try {
			int id = hrTypes.getIdHrType();
			HrTypes hrTypesAux = new HrTypes();
			hrTypesAux = hrTypesDao.nombreExiste(nombre, id);
			if (hrTypesAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						mensajeExistencia, "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the types of human resources
	 * 
	 * @modify 30/07/2015 Gerardo.Herrera
	 * 
	 * @return consultarHrTypes: Redirects to manage resource types humans with
	 *         the list of names updated
	 */
	public String guardaHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (hrTypes.getIdHrType() != 0) {
				hrTypesDao.editarHrTypes(hrTypes);
			} else {
				mensajeRegistro = "message_registro_guardar";
				hrTypesDao.guardarHrTypes(hrTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), hrTypes.getName()));

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarHrTypes();
	}

	/**
	 * Method to delete a type of human resource database
	 * 
	 * @return consultarHrTypes: Consult the list of resource types human
	 *         template and returns to manage type of human resources
	 */
	public String eliminarHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrTypesDao.eliminarHrTypes(hrTypes);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					hrTypes.getIdHrType()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					hrTypes.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarHrTypes();
	}
}
