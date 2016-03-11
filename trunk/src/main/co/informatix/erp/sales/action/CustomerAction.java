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
 * This class implements the logic of customers who would be in the database.
 * The logic is to see, edit, add or remove clients.
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

	private List<Customer> customersList;

	private Customer customer;
	private Paginador paginador = new Paginador();

	private String nameSearch;

	/**
	 * @return customersList: Customer list.
	 */
	public List<Customer> getCustomersList() {
		return customersList;
	}

	/**
	 * @param customersList
	 *            : Customer list.
	 */
	public void setCustomersList(List<Customer> customersList) {
		this.customersList = customersList;
	}

	/**
	 * @return customer: Get the customer record.
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            : Set the customer record.
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
	 * @return nameSearch: Customer name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Customer name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the search parameters and load the initial customers
	 * list.
	 * 
	 * @return searchCustomers: query method that customers return to the
	 *         template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchCustomers();
	}

	/**
	 * Consult the list of customers
	 * 
	 * @return "gesCustomer": redirects to the template to manage clients.
	 */
	public String searchCustomers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle salesMessages = ControladorContexto
				.getBundle("mensajeSales");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		customersList = new ArrayList<Customer>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointQueryMessages = new StringBuilder();
		String queryMessage = "";
		try {
			advancedQuery(queryBuilder, parameters, bundle, jointQueryMessages);
			Long amount = customerDao.customersAmount(queryBuilder, parameters);
			if (amount != null) {
				paginador.paginar(amount);
			}
			customersList = customerDao.searchCustomers(paginador.getInicio(),
					paginador.getRango(), queryBuilder, parameters);

			if ((customersList == null || customersList.size() <= 0)
					&& !"".equals(jointQueryMessages.toString())) {
				queryMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointQueryMessages);
			} else if (customersList == null || customersList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointQueryMessages.toString())) {
				queryMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								salesMessages.getString("customer_label_s"),
								jointQueryMessages);
			}
			validation.setMensajeBusqueda(queryMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCustomer";
	}

	/**
	 * This method builds the query for an advanced search and get display
	 * messages depending on the search criteria selected by the user.
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags
	 * @param jointSearchMessages
	 *            : Message search.
	 * 
	 */
	private void advancedQuery(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(c.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new customer.
	 * 
	 * @param customer
	 *            : Customer that you are adding or editing.
	 * 
	 * @return "regCustomer": Redirects to the register customer template.
	 */
	public String addEditCustomer(Customer customer) {
		if (customer != null) {
			this.customer = customer;
		} else {
			this.customer = new Customer();
		}
		return "regCustomer";
	}

	/**
	 * Validates the customer's name; so that, it does not query the database
	 * and it validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = customer.getIdCustomer();
			Customer auxCustomer = new Customer();
			auxCustomer = customerDao.cutomerExists(name, id);
			if (auxCustomer != null) {
				String existenceMessage = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(existenceMessage), null));
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

	/**
	 * Method used to save or edit customers
	 * 
	 * @return searchCustomers: Redirects customers to manage the list of
	 *         clients updated
	 */
	public String saveCustomer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {

			if (customer.getIdCustomer() != 0) {
				customerDao.editCustomer(customer);
			} else {
				registerMessage = "message_registro_guardar";
				customerDao.saveCustomer(customer);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), customer.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchCustomers();
	}

	/**
	 * Method for removing a client from the database.
	 * 
	 * @modify 10/03/2016 Sergio.Gelves
	 * 
	 * @return searchCustomers: Query the list of customers and redirects to the
	 *         manage customer view.
	 */
	public String deleteCustomer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			customerDao.deleteCustomer(customer);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					customer.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					customer.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchCustomers();
	}

}
