package co.informatix.erp.utz.action;

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
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * certifications and roles that may exist.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CertificationsAndRolesAction implements Serializable {
	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;

	private CertificationsAndRoles certificationsAndRoles;
	private String nombreBuscar;
	private List<CertificationsAndRoles> listaCertificacionesAndRoles;
	private Paginador paginador = new Paginador();

	/**
	 * @return certificationsAndRoles: object containing data type
	 *         certifications and roles
	 * 
	 */
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : object containing data type certifications and roles
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	/**
	 * @return nombreBuscar: name to search type certification and role
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : name to search type certification and role
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return listaCertificacionesAndRoles: list of certifications and role
	 */
	public List<CertificationsAndRoles> getListaCertificacionesAndRoles() {
		return listaCertificacionesAndRoles;
	}

	/**
	 * @param listaCertificacionesAndRoles
	 *            : list of certifications and role
	 */
	public void setListaCertificacionesAndRoles(
			List<CertificationsAndRoles> listaCertificacionesAndRoles) {
		this.listaCertificacionesAndRoles = listaCertificacionesAndRoles;
	}

	/**
	 * @return paginador: management paginated list of certifications and role.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : management paginated list of certifications and role.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of certifications and role
	 * 
	 * @return consultarCertificationsAndRoles: method to query the roles
	 *         certifications and returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarCertificationsAndRoles();
	}

	/**
	 * Consult the list of certifications and roles
	 * 
	 * @return gesCertifi: Template manage certifications and roles
	 */
	public String consultarCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaCertificacionesAndRoles = new ArrayList<CertificationsAndRoles>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = certificationsAndRolesDao
					.cantidadCertificationsAndRoles(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaCertificacionesAndRoles = certificationsAndRolesDao
					.consultarCertificationsAndRolesAction(
							paginador.getInicio(), paginador.getRango(),
							consulta, parametros);
			if ((listaCertificacionesAndRoles == null || listaCertificacionesAndRoles
					.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaCertificacionesAndRoles == null
					|| listaCertificacionesAndRoles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("certifications_and_roles_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCertifi";
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
			consult.append("WHERE UPPER(ct.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new CertificationsANdRoles
	 * 
	 * @param certificationsAndRoles
	 *            :certicicationsAndRoles types that are adding or editing
	 * 
	 * @return "regCertifi":redirects to register certicicationsAndRoles
	 *         template.
	 */
	public String agregarEditarCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		if (certificationsAndRoles != null) {
			this.certificationsAndRoles = certificationsAndRoles;
		} else {
			this.certificationsAndRoles = new CertificationsAndRoles();
		}
		return "regCertifi";
	}

	/**
	 * Allows validate the type of certification, that it is not repeated in the
	 * database (valid for the name of the certification)
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {

			CertificationsAndRoles certificationsAndRolesAux = new CertificationsAndRoles();
			certificationsAndRolesAux = certificationsAndRolesDao
					.nombreExiste(nombre);
			if (certificationsAndRolesAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
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
	 * Method used to save or edit the types of certifications and roles
	 * 
	 * @return consultarCertificationAndRoles: Redirects to manage types of
	 *         certificationsAndroles with the list of names updated
	 */
	public String guardarCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (certificationsAndRoles.getIdCertificactionsAndRoles() != 0) {
				certificationsAndRolesDao
						.editarCertificationsAndRoles(certificationsAndRoles);
			} else {
				mensajeRegistro = "message_registro_guardar";
				certificationsAndRolesDao
						.guardaCertificationsAndRoles(certificationsAndRoles);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					certificationsAndRoles.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarCertificationsAndRoles();
	}

	/**
	 * Method to delete a type certificate and role of the database
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarCertificationsAndRoles(): Consult the list of the types
	 *         of certifications and role and returns to manage certification
	 *         and role.
	 */
	public String eliminarCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			certificationsAndRolesDao
					.eliminarCertificationsAndRoles(certificationsAndRoles);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					certificationsAndRoles.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					certificationsAndRoles.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarCertificationsAndRoles();
	}

}
