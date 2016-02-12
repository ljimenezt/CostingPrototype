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
import co.informatix.erp.warehouse.dao.TypeOfManagementDao;
import co.informatix.erp.warehouse.entities.TypeOfManagement;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the names of the types of management in the system
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TypeOfManagementAction implements Serializable {
	private List<TypeOfManagement> listaTypeOfManagement;
	private Paginador paginador = new Paginador();
	private TypeOfManagement typeOfManagement;
	private String nombreBuscar;

	@EJB
	private TypeOfManagementDao typeOfManagementDao;

	/**
	 * @return listaTypeOfManagement: List of types of management
	 */
	public List<TypeOfManagement> getListaTypeOfManagement() {
		return listaTypeOfManagement;
	}

	/**
	 * @param listaTypeOfManagement
	 *            : List of types of management
	 */
	public void setListaTypeOfManagement(
			List<TypeOfManagement> listaTypeOfManagement) {
		this.listaTypeOfManagement = listaTypeOfManagement;
	}

	/**
	 * @return paginador: Paginated list of the types of management that may
	 *         have in the view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of the types of management that may have in
	 *            the view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return typeOfManagement: Object management types
	 */
	public TypeOfManagement getTypeOfManagement() {
		return typeOfManagement;
	}

	/**
	 * @param typeOfManagement
	 *            : Object management types
	 * 
	 */
	public void setTypeOfManagement(TypeOfManagement typeOfManagement) {
		this.typeOfManagement = typeOfManagement;
	}

	/**
	 * @return nombreBuscar: Name by which you want to consult the types of
	 *         management
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to consult the types of management
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of types of management
	 * 
	 * @return consultarTypeOfManagement: Method consulting management types,
	 *         returns to the template management
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarTypeOfManagement();
	}

	/**
	 * Consult the list of the types of management
	 * 
	 * @return gesTypeManag: Navigation rule that redirects manage management
	 *         types
	 */
	public String consultarTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaTypeOfManagement = new ArrayList<TypeOfManagement>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = typeOfManagementDao.cantidadTypeOfManagement(
					consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaTypeOfManagement = typeOfManagementDao
					.consultarTypeOfManagement(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);
			if ((listaTypeOfManagement == null || listaTypeOfManagement.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaTypeOfManagement == null
					|| listaTypeOfManagement.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse
										.getString("type_management_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTypeManag";
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
			consult.append("WHERE UPPER(tm.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new type of management
	 * 
	 * @param typeOfManagement
	 *            : Name the type of management that will add or edit
	 * 
	 * @return regTypeManag: Redirected to the template management record type
	 */
	public String agregarEditarTypeOfManagement(
			TypeOfManagement typeOfManagement) {
		if (typeOfManagement != null) {
			this.typeOfManagement = typeOfManagement;
		} else {
			this.typeOfManagement = new TypeOfManagement();
		}
		return "regTypeManag";
	}

	/**
	 * To validate the name of the types of management, so it is not repeated in
	 * the database and validates against XSS.
	 * 
	 * @param context
	 *            : Application context
	 * 
	 * @param toValidate
	 *            : Validate component
	 * @param value
	 *            : Field value is validated
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = typeOfManagement.getIdTypeOfManagement();
			TypeOfManagement typeOfManagementAux = new TypeOfManagement();
			typeOfManagementAux = typeOfManagementDao.nombreExiste(nombre, id);
			if (typeOfManagementAux != null) {
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
	 * Method used to save or edit the types of management
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultarTypeOfManagement: Redirects to manage types of
	 *         management with the list of names updated
	 */
	public String guardarTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (typeOfManagement.getIdTypeOfManagement() != 0) {
				typeOfManagementDao.editarTypeOfManagement(typeOfManagement);
			} else {
				mensajeRegistro = "message_registro_guardar";
				typeOfManagementDao.guardarTypeOfManagement(typeOfManagement);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					typeOfManagement.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarTypeOfManagement();
	}

	/**
	 * Method to delete a type of management of the database
	 * 
	 * 
	 * @return consultarTypeOfManagement: Consult the list of the types of
	 *         management and template returns to manage type of management
	 */
	public String eliminarTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			typeOfManagementDao.eliminarTypeOfManagement(typeOfManagement);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					typeOfManagement.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					typeOfManagement.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarTypeOfManagement();
	}

}
