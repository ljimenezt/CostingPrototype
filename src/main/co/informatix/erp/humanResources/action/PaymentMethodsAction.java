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

import co.informatix.erp.humanResources.dao.PaymentMethodsDao;
import co.informatix.erp.humanResources.entities.PaymentMethods;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the business logic related to creating, updating, and
 * deleting PaymentMethods.
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
	private List<PaymentMethods> paymentMethodsList;
	private String nameSearch;
	private Paginador paginador = new Paginador();
	private PaymentMethods paymentMethods;

	/**
	 * @return paymentMethodsList: PaymentMethods list shown in the user
	 *         interface.
	 */
	public List<PaymentMethods> getPaymentMethodsList() {
		return paymentMethodsList;
	}

	/**
	 * @param paymentMethodsList
	 *            : PaymentMethods list shown in the user interface.
	 */
	public void setPaymentMethodsList(List<PaymentMethods> paymentMethodsList) {
		this.paymentMethodsList = paymentMethodsList;
	}

	/**
	 * @return nameSearch: PaymentMethods name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : PaymentMethods name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
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
	 * @return paymentMethods: Data object containing a Payment methods.
	 */
	public PaymentMethods getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods
	 *            : Data object containing a Payment methods.
	 */
	public void setPaymentMethods(PaymentMethods paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	/**
	 * Method to initialize the parameters of the search and load initial
	 * listing of the types of PaymentMethods
	 * 
	 * @return searchPaymentMethods: consulting method types PaymentMethods
	 *         returns to the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchPaymentMethods();
	}

	/**
	 * Search a list of PaymentMethods that are in the database.
	 * 
	 * @return "gesPaymentMethods": Redirects to a template to manage
	 *         PaymentMethods.
	 */
	public String searchPaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle paymentMethodsmBundle = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		paymentMethodsList = new ArrayList<PaymentMethods>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(query, parameters, bundle, jointSearchMessages);
			Long cantidad = paymentMethodsDao.amountPaymentMethods(query,
					parameters);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			paymentMethodsList = paymentMethodsDao.searchPaymentMethods(
					paginador.getInicio(), paginador.getRango(), query,
					parameters);
			if ((paymentMethodsList == null || paymentMethodsList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (paymentMethodsList == null
					|| paymentMethodsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								paymentMethodsmBundle
										.getString("paymentmethods_label_s"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPaymentMethods";
	}

	/**
	 * This method builds the query to the advanced search and it also builds
	 * messages to display depending on the search criteria selected by the
	 * user.
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameter
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * 
	 * @param jointSearchMessages
	 *            : Message search.
	 * 
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(pm.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameter.add(item);
			jointSearchMessages.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new PaymentMethods.
	 * 
	 * @param paymentMethods
	 *            : Payment methods that is going to be added or edited.
	 * 
	 * @return "regPaymentMethods": Redirects to the template to manage Payment
	 *         methods.
	 */
	public String addEditPaymentMethods(PaymentMethods paymentMethods) {
		if (paymentMethods != null) {
			this.paymentMethods = paymentMethods;
		} else {
			this.paymentMethods = new PaymentMethods();
		}
		return "regPaymentMethods";
	}

	/**
	 * Method to save or edit the PaymentMethods
	 * 
	 * @return searchPaymentMethods: Redirects to PaymentMethods managing with
	 *         the updated list of names.
	 */
	public String savePaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {
			if (paymentMethods.getIdPaymentMethod() != 0) {
				paymentMethodsDao.editPaymentMethods(paymentMethods);
			} else {
				registerMessage = "message_registro_guardar";
				paymentMethodsDao.savePaymentMethods(paymentMethods);
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(registerMessage),
							paymentMethods.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchPaymentMethods();
	}

	/**
	 * Delete PaymentMethods in the database.
	 * 
	 * @return searchPaymentMethods: Search the list of PaymentMethods and
	 *         redirects to manage PaymentMethods.
	 */
	public String deletePaymentMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			paymentMethodsDao.deletePaymentMethods(paymentMethods);
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

		return searchPaymentMethods();
	}

	/**
	 * To validate the name of the kinds of human resources, so that it is not
	 * repeated in the database and it is valid compared with XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = paymentMethods.getIdPaymentMethod();
			PaymentMethods auxPaymentMethods = new PaymentMethods();
			auxPaymentMethods = paymentMethodsDao.nameExists(name, id);
			if (auxPaymentMethods != null) {
				String existenceMessage = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						existenceMessage, "mensaje");
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

}
