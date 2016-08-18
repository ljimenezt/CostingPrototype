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

import co.informatix.erp.humanResources.dao.ContractDao;
import co.informatix.erp.humanResources.dao.PaymentsDao;
import co.informatix.erp.humanResources.entities.Contract;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.Payments;
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
	private ContractDao contractDao;

	private List<Payments> listPayments;
	private Payments payments;
	private Paginador pagination = new Paginador();

	private String nameSearch = "";

	/**
	 * @return listPayments: Gets the list of payments.
	 */
	public List<Payments> getListPayments() {
		return listPayments;
	}

	/**
	 * @param listPayments
	 *            : Gets the list of payments.
	 */
	public void setListPayments(List<Payments> listPayments) {
		this.listPayments = listPayments;
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
	 * @return pagination: Paginated list of payments which may have in the
	 *         view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of payments which may have in the view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: gets the name by which you want to consult the
	 *         payments.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : gets the name by which you want to consult the payments.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of payments.
	 * 
	 * @return consultPayments: method that consults the payments, and takes the
	 *         user to the payments management template.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultPayments();
	}

	/**
	 * Consult the list of payments.
	 * 
	 * @return "gesPayments": redirects to the template to manage payments.
	 */
	public String consultPayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listPayments = new ArrayList<Payments>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = paymentsDao.quantityPayments(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listPayments = paymentsDao.consultPayments(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			if ((listPayments == null || listPayments.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listPayments == null || listPayments.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("payments_label_s"),
								unionMessagesSearch);
			}
			loadDetailsPayments();
			validations.setMensajeBusqueda(messageSearch);
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
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * 
	 * @param unionMessagesSearch
	 *            : Message search
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(p.hr.name) LIKE UPPER(:keyword) ");
			consult.append("OR UPPER(p.hr.familyName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method fills the various objects associated with a payment.
	 * 
	 * @modify 18/04/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	public void loadDetailsPayments() throws Exception {
		if (this.listPayments != null) {
			for (Payments payment : listPayments) {
				loadDetailsPayment(payment);
			}
		}
	}

	/**
	 * Method of uploading the details of payment.
	 * 
	 * @modify 28/04/2016 Mabell.Boada
	 * 
	 * @param payment
	 *            : payment which will carry the details.
	 * @throws Exception
	 */
	public void loadDetailsPayment(Payments payment) throws Exception {
		int idPayment = payment.getIdPayment();
		Contract contract = (Contract) this.paymentsDao.consultObjectPayments(
				"contract", idPayment);
		Hr hr = (Hr) this.paymentsDao.consultObjectPayments("hr", idPayment);
		if (contract != null) {
			int idContract = contract.getId();
			Hr hrCont = (Hr) this.contractDao.searchContract("hr", idContract);
			contract.setHr(hrCont);
			payment.setContract(contract);
			payment.setHr(hr);
		}

	}

	/**
	 * Method that allows to eliminate payment of the database.
	 * 
	 * @return consultPayments: Consulting payment method, return to the
	 *         template management.
	 */
	public String removePayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			paymentsDao.removePayments(this.payments);
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
		return consultPayments();
	}

	/**
	 * Method to edit or create a new payment.
	 * 
	 * @modify 28/04/2016 Mabell.Boada
	 * 
	 * @param payments
	 *            : payment is to add or edit.
	 * @return "regPayments": redirected to the template record payments.
	 */
	public String addEditPayments(Payments payments) {
		try {
			if (payments != null) {
				this.payments = payments;
				loadDetailsPayment(payments);
			} else {
				this.payments = new Payments();
				this.payments.setHr(new Hr());
				Contract contract = new Contract();
				contract.setHr(new Hr());
				this.payments.setContract(contract);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);

		}
		return "regPayments";
	}

	/**
	 * Method for cleaning the contract associated with the payment.
	 */
	public void cleanContract() {
		this.payments.setContract(new Contract());
	}

	/**
	 * Method to load the selected contract.
	 * 
	 * @param contract
	 *            : object selected contract.
	 */
	public void loadContract(Contract contract) {
		this.payments.setContract(contract);
	}

	/**
	 * Method to clean the HR associated with the payment.
	 */
	public void cleanHr() {
		this.payments.setHr(new Hr());
	}

	/**
	 * Method to load the selected HR.
	 * 
	 * @param hr
	 *            : object HR selected.
	 */
	public void loadHr(Hr hr) {
		this.payments.setHr(hr);
	}

	/**
	 * Method used to save or edit payments.
	 * 
	 * @return searchInitialization(): Method consulting payments and returns to
	 *         template management.
	 */
	public String savePayments() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = bundle.getString("message_registro_modificar");
		try {
			if (this.payments.getIdPayment() != 0) {
				paymentsDao.editPayments(this.payments);
			} else {
				key = bundle.getString("message_registro_guardar");
				paymentsDao.savePayments(this.payments);
			}
			this.nameSearch = "";
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(key, payments.getHr().getName() + " "
							+ payments.getHr().getFamilyName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
	}
}
