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
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class is all related logic with the creation and updating of the system
 * suppliers
 * 
 * @author Mabell.Boada
 * 
 */

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class SuppliersAction implements Serializable {

	private List<Suppliers> listaSuppliers;
	private Paginador paginador = new Paginador();
	private Suppliers suppliers;
	private String nombreBuscar;

	@EJB
	private SuppliersDao suppliersDao;

	/**
	 * @return listaSuppliers: List of providers
	 */
	public List<Suppliers> getListaSuppliers() {
		return listaSuppliers;
	}

	/**
	 * @param listaSuppliers
	 *            : List of providers
	 */
	public void setListaSuppliers(List<Suppliers> listaSuppliers) {
		this.listaSuppliers = listaSuppliers;
	}

	/**
	 * @return paginador: Paging from the list of providers who may be in the
	 *         view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paging from the list of providers who may be in the view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return suppliers: Object provider
	 */
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            : Object provider
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return nombreBuscar: Name by which you want to consult suppliers
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to consult suppliers
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of suppliers
	 * 
	 * @return consultarSuppliers: method consulting suppliers, returns to the
	 *         template management
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarSuppliers();
	}

	/**
	 * Consult the list of providers
	 * 
	 * @return gesSuppliers: Navigation rule that redirects manage suppliers
	 */
	public String consultarSuppliers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaSuppliers = new ArrayList<Suppliers>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = suppliersDao
					.cantidadSuppliers(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaSuppliers = suppliersDao.consultarSuppliers(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaSuppliers == null || listaSuppliers.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaSuppliers == null || listaSuppliers.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("suppliers_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesSuppliers";
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
			consult.append("WHERE UPPER(s.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new provider
	 * 
	 * @param suppliers
	 *            : Provider object that you are adding or editing
	 * 
	 * @return regSuppliers: Template redirects to register suppliers
	 * 
	 */
	public String agregarEditarSuppliers(Suppliers suppliers) {
		if (suppliers != null) {
			this.suppliers = suppliers;
		} else {
			this.suppliers = new Suppliers();
		}
		return "regSuppliers";
	}

	/**
	 * To validate the names of the suppliers, so it is not repeated in the
	 * database and validates against XSS.
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
			int id = suppliers.getIdSupplier();
			Suppliers suppliersAux = new Suppliers();
			suppliersAux = suppliersDao.nombreExiste(nombre, id);
			if (suppliersAux != null) {
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
	 * Method used to save or edit providers
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultarSuppliers: Redirects to manage suppliers with the list
	 *         of names updated
	 */
	public String guardarSuppliers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (suppliers.getIdSupplier() != 0) {
				suppliersDao.editarSuppliers(suppliers);
			} else {
				mensajeRegistro = "message_registro_guardar";
				suppliersDao.guardarSuppliers(suppliers);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), suppliers.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarSuppliers();
	}

	/**
	 * Method to delete a provider of database
	 * 
	 * 
	 * @return consultarSuppliers: Consult the list of providers and returns to
	 *         manage template suppliers
	 */
	public String eliminarSuppliers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			suppliersDao.eliminarSuppliers(suppliers);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					suppliers.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					suppliers.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarSuppliers();
	}

}
