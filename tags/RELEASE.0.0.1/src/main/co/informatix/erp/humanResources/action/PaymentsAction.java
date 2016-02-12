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

import co.informatix.erp.humanResources.dao.PaymentsDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.Payments;
import co.informatix.erp.recursosHumanos.dao.ContratoDao;
import co.informatix.erp.recursosHumanos.entities.Contrato;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, update and delete
 * payments in the system.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PaymentsAction implements Serializable {

	@EJB
	private PaymentsDao paymentsDao;
	@EJB
	private ContratoDao contratoDao;

	private List<Payments> listaPayments;
	private Payments payments;
	private Paginador paginador = new Paginador();

	private String nombreBuscar = "";

	/**
	 * @return listaPayments: Gets the list of payments.
	 */
	public List<Payments> getListaPayments() {
		return listaPayments;
	}

	/**
	 * @param listaPayments
	 *            : Gets the list of payments.
	 */
	public void setListaPayments(List<Payments> listaPayments) {
		this.listaPayments = listaPayments;
	}

	/**
	 * @return payments: Gets the object payments.
	 */
	public Payments getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            : Gets the object payments.
	 */
	public void setPayments(Payments payments) {
		this.payments = payments;
	}

	/**
	 * @return paginador: Paginated list of payments which may have in the view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of payments which may have in the view.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: gets the name by which you want to consult the
	 *         payments.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : gets the name by which you want to consult the payments.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of contracts.
	 * 
	 * @return consultarPayments: method that consults the payments, and takes
	 *         the user to the payments management template.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarPayments();
	}

	/**
	 * Consult the list of contracts
	 * 
	 * @return "gesPayments": redirects to the template to manage contracts.
	 */
	public String consultarPayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaPayments = new ArrayList<Payments>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = paymentsDao.cantidadPayments(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaPayments = paymentsDao.consultarPayments(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaPayments == null || listaPayments.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaPayments == null || listaPayments.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("payments_label_s"),
								unionMensajesBusqueda);
			}
			cargarDetallesPayments();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPayments";
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
			consulta.append("WHERE UPPER(p.hr.name) LIKE UPPER(:keyword) ");
			consulta.append("OR UPPER(p.hr.familyName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * This method fills the various objects associated with a payment.
	 * 
	 * @throws Exception
	 */
	public void cargarDetallesPayments() throws Exception {
		List<Payments> payments = new ArrayList<Payments>();
		if (this.listaPayments != null) {
			payments.addAll(this.listaPayments);
			this.listaPayments = new ArrayList<Payments>();
			for (Payments payment : payments) {
				cargarDetallesPayment(payment);
				this.listaPayments.add(payment);
			}
		}
	}

	/**
	 * Method of uploading the details of payment.
	 * 
	 * @param payment
	 *            : payment which will carry the details.
	 * @throws Exception
	 */
	public void cargarDetallesPayment(Payments payment) throws Exception {
		int idPayment = payment.getIdPayment();
		Contrato contrato = (Contrato) this.paymentsDao
				.consultarObjetoPayments("contrato", idPayment);
		Hr hr = (Hr) this.paymentsDao.consultarObjetoPayments("hr", idPayment);
		int idContrato = contrato.getId();
		Persona persona = (Persona) this.contratoDao.consultarObjetoContrato(
				"persona", idContrato);
		contrato.setPersona(persona);
		payment.setContrato(contrato);
		payment.setHr(hr);
	}

	/**
	 * Method that allows to eliminate payment of the database.
	 * 
	 * @return consultarPayments: Consulting payment method, return to the
	 *         template management.
	 */
	public String eliminarPayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			paymentsDao.eliminarPayments(this.payments);
			String format = MessageFormat.format(bundle
					.getString("message_registro_eliminar"), payments.getHr()
					.getName() + " " + payments.getHr().getFamilyName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					payments.getHr().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarPayments();
	}

	/**
	 * Method to edit or create a new payment.
	 * 
	 * @param payments
	 *            : payment is to add or edit
	 * 
	 * @return "regPayments": redirected to the template record payments.
	 * @throws Exception
	 */
	public String agregarEditarPayments(Payments payments) throws Exception {
		if (payments != null) {
			this.payments = payments;
			cargarDetallesPayment(payments);
		} else {
			this.payments = new Payments();
			this.payments.setHr(new Hr());
			Contrato contrato = new Contrato();
			contrato.setPersona(new Persona());
			this.payments.setContrato(contrato);
		}
		return "regPayments";
	}

	/**
	 * Method for cleaning the contract associated with the payment.
	 */
	public void limpiarContrato() {
		this.payments.setContrato(new Contrato());
	}

	/**
	 * Method to load the selected contract.
	 * 
	 * @param contrato
	 *            : object selected contract.
	 */
	public void cargarContrato(Contrato contrato) {
		this.payments.setContrato(contrato);
	}

	/**
	 * Method to clean the HR associated with the payment.
	 */
	public void limpiarHr() {
		this.payments.setHr(new Hr());
	}

	/**
	 * Method to load the selected HR.
	 * 
	 * @param hr
	 *            : object HR selected.
	 */
	public void cargarHr(Hr hr) {
		this.payments.setHr(hr);
	}

	/**
	 * Method used to save or edit payments.
	 * 
	 * @return inicializarBusqueda(): Method consulting payments and returns to
	 *         template management.
	 */
	public String guardarPayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = bundle.getString("message_registro_modificar");
		try {
			if (this.payments.getIdPayment() != 0) {
				paymentsDao.editarPayments(this.payments);
			} else {
				key = bundle.getString("message_registro_guardar");
				paymentsDao.guardarPayments(this.payments);
			}
			this.nombreBuscar = "";
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(key, payments.getHr().getName() + " "
							+ payments.getHr().getFamilyName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}
}
