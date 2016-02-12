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
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.PaymentMethodsDao;
import co.informatix.erp.humanResources.entities.PaymentMethods;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, update, and delete types
 * that can PaymentMethods exist.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PaymentMethodsAction implements Serializable {
	@EJB
	private PaymentMethodsDao paymentMethodsDao;
	private List<PaymentMethods> listaPaymentMethods;
	private String nombreBuscar;
	private Paginador paginador = new Paginador();
	private PaymentMethods paymentMethods;

	/**
	 * @return listaPaymentMethods: PaymentMethods list shown User interface
	 */
	public List<PaymentMethods> getListaPaymentMethods() {
		return listaPaymentMethods;
	}

	/**
	 * @param listaPaymentMethods
	 *            : PaymentMethods list shown User interface
	 */
	public void setListaPaymentMethods(List<PaymentMethods> listaPaymentMethods) {
		this.listaPaymentMethods = listaPaymentMethods;
	}

	/**
	 * @return nombreBuscar: PaymentMethods to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : PaymentMethods to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return paginador: Management paged list types PaymentMethods.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list types PaymentMethods
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paymentMethods: data object containing a Payment methods
	 */
	public PaymentMethods getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods
	 *            : data object containing a Payment methods
	 */
	public void setPaymentMethods(PaymentMethods paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	/**
	 * Method to initialize the parameters of the search and load initial
	 * listing of the types of PaymentMethods
	 * 
	 * @return consultarPaymentMethods: consulting method types PaymentMethods
	 *         returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarPaymentMethods();
	}

	/**
	 * Consult the list of PaymentMethods who are in the BD
	 * 
	 * @return "gesPaymentMethods": redirects to the template to manage types of
	 *         PaymentMethods
	 */
	public String consultarPaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundlePaymentMethods = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaPaymentMethods = new ArrayList<PaymentMethods>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = paymentMethodsDao.cantidadPaymentMethods(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaPaymentMethods = paymentMethodsDao.consultarPaymentMethods(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaPaymentMethods == null || listaPaymentMethods.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaPaymentMethods == null
					|| listaPaymentMethods.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundlePaymentMethods
										.getString("paymentmethods_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPaymentMethods";
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
			consulta.append("WHERE UPPER(pm.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new PaymentMethods.
	 * 
	 * @param paymentMethods
	 *            : Payment methods types that are adding or editing
	 * 
	 * @return "regPaymentMethods": redirected to the template record Payment
	 *         methods.
	 */
	public String agregarEditarPaymentMethods(PaymentMethods paymentMethods) {
		if (paymentMethods != null) {
			this.paymentMethods = paymentMethods;
		} else {
			this.paymentMethods = new PaymentMethods();
		}
		return "regPaymentMethods";
	}

	/**
	 * Method used to save or edit the PaymentMethods
	 * 
	 * @return consultarPaymentMethods: To manage types redirects PaymentMethods
	 *         with the list of names updated
	 */
	public String guardaPaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (paymentMethods.getIdPaymentMethod() != 0) {
				paymentMethodsDao.editarPaymentMethods(paymentMethods);
			} else {
				mensajeRegistro = "message_registro_guardar";
				paymentMethodsDao.guardaPaymentMethods(paymentMethods);
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(mensajeRegistro),
							paymentMethods.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarPaymentMethods();
	}

	/**
	 * PaymentMethods delete method allowing the database
	 * 
	 * @return consultarPaymentMethods: Consult the list of PaymentMethods and
	 *         returns to manage PaymentMethods
	 */
	public String eliminarPaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			paymentMethodsDao.eliminarPaymentMethods(paymentMethods);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					paymentMethods.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					paymentMethods.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarPaymentMethods();
	}
}
