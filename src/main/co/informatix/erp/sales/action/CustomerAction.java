package co.informatix.erp.sales.action;

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

import co.informatix.erp.sales.dao.CustomerDao;
import co.informatix.erp.sales.entities.Customer;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of customers who can be in the database. The
 * logic is to see, edit, add or remove clients
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CustomerAction implements Serializable {
	@EJB
	private CustomerDao customerDao;

	private List<Customer> listaCustomers;

	private Customer customer;
	private Paginador paginador = new Paginador();

	private String nombreBuscar;

	/**
	 * @return listaCustomers: customer list.
	 */
	public List<Customer> getListaCustomers() {
		return listaCustomers;
	}

	/**
	 * @param listaCustomers
	 *            :customer list.
	 */
	public void setListaCustomers(List<Customer> listaCustomers) {
		this.listaCustomers = listaCustomers;
	}

	/**
	 * @return customer: get the customer record.
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            :set the customer record.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return paginador: Management paged list of customers.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paged list of customers.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Customer name search.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Customer name search.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of customers
	 * 
	 * @return consultarCustomers: query method that customers return to the
	 *         template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarCustomers();
	}

	/**
	 * Consult the list of customers
	 * 
	 * @return "gesCustomer": redirects to the template to manage clients.
	 */
	public String consultarCustomers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle mensajeSales = ControladorContexto
				.getBundle("mensajeSales");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaCustomers = new ArrayList<Customer>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = customerDao.cantidadCustomers(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaCustomers = customerDao.consultarCustomers(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);

			if ((listaCustomers == null || listaCustomers.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaCustomers == null || listaCustomers.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								mensajeSales.getString("customer_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCustomer";
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
			consult.append("WHERE UPPER(c.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new customer.
	 * 
	 * @param customer
	 *            :customer that you are adding or editing
	 * 
	 * @return "regCustomer":redirects the customer record template.
	 */
	public String agregarEditarCustomer(Customer customer) {
		if (customer != null) {
			this.customer = customer;
		} else {
			this.customer = new Customer();
		}
		return "regCustomer";
	}

	/**
	 * Allows validate the customer's name so that it does not recur in the
	 * database and validates against XSS.
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
			int id = customer.getIdCustomer();
			Customer customerAux = new Customer();
			customerAux = customerDao.nombreExiste(nombre, id);
			if (customerAux != null) {
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
	 * Method used to save or edit customers
	 * 
	 * @return consultarCustomers: Redirects customers to manage the list of
	 *         clients updated
	 */
	public String guardarCustomer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (customer.getIdCustomer() != 0) {
				customerDao.editarCustomer(customer);
			} else {
				mensajeRegistro = "message_registro_guardar";
				customerDao.guardarCustomer(customer);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), customer.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarCustomers();
	}

	/**
	 * Method for removing a client from the database.
	 * 
	 * @return consultarCustomers: Consult the list of customers and manage
	 *         customer returns.
	 */
	public String eliminarCustomer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			customerDao.eliminarCustomer(customer);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(bundle
							.getString("message_registro_eliminar")), customer
							.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					customer.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarCustomers();
	}

}
