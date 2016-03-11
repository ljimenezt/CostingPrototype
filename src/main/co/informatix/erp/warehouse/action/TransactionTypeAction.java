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
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * This class is all related logic with creating, updating and removal of
 * TransactionType.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TransactionTypeAction implements Serializable {
	@EJB
	private TransactionTypeDao transactionTypeDao;

	private Paginador paginador = new Paginador();

	private TransactionType transactionType;

	private List<TransactionType> transactionTypeList;

	private String nameSearch;

	/**
	 * @return paginador: The paging controller object.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : The paging controller object.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return transactionType: transactionType stored in data base.
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            : transactionType stored in data base.
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return nameSearch :Name by which you want to consult the
	 *         transactionType.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to consult the transactionType.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return transactionTypeList: list of transaction type stored in data
	 *         base.
	 */
	public List<TransactionType> getTransactionTypeList() {
		return transactionTypeList;
	}

	/**
	 * @param transactionTypeList
	 *            : list of transaction type stored in data base.
	 */
	public void setTransactionTypeList(List<TransactionType> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return consultTransactionType: TransactionType consulting method and
	 *         redirects to the template to manage TransactionType.
	 */
	public String searchInitialization() {

		this.nameSearch = null;
		this.transactionType = new TransactionType();
		return consultTransactionType();
	}

	/**
	 * Consult the list of transactionType
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return gesTrans: Navigation rule that redirects to manage
	 *         transactionType
	 */
	public String consultTransactionType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		transactionTypeList = new ArrayList<TransactionType>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = transactionTypeDao.quantityTransactionType(query,
					parameters);
			if (quantity != null) {
				paginador.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				transactionTypeList = transactionTypeDao
						.consultTransactionType(paginador.getInicio(),
								paginador.getRango(), query, parameters);
			}
			if ((transactionTypeList == null || transactionTypeList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (transactionTypeList == null
					|| transactionTypeList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("transactionType_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTrans";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(t.transactionType) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new transactionType.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param transactionType
	 *            :transactionType are adding or editing.
	 * 
	 * @return "regTrans":redirected to the template record transactionType.
	 */
	public String addEditTransactionType(TransactionType transactionType) {
		try {
			if (transactionType != null) {
				this.transactionType = transactionType;
			} else {
				this.transactionType = new TransactionType();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regTrans";
	}

	/**
	 * Method used to save or edit the transactionType.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return contulTransactionType: Redirects to manage transactionType with a
	 *         list of updated transactionType.
	 */
	public String saveTransactionType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (transactionType.getIdTransactionType() != 0) {
				transactionTypeDao.editTransactionType(transactionType);
			} else {
				messageLog = "message_registro_guardar";
				transactionTypeDao.saveTransactionType(transactionType);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog),
					transactionType.getTransactionType()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultTransactionType();
	}

	/**
	 * Method that allows contulTransactionType to delete one database.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return consultTransactionType: Consult the list of transactionType and
	 *         returns to manage TransactionType.
	 */
	public String deleteTransactionType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			transactionTypeDao.deleteTransactionType(transactionType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					transactionType.getTransactionType()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					transactionType.getTransactionType());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultTransactionType();
	}

	/**
	 * This method allow validate the name of the transaction type, so that it
	 * is not repeated in the database and validates against XSS.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = transactionType.getIdTransactionType();
			TransactionType transactionTypeAux = new TransactionType();
			transactionTypeAux = transactionTypeDao.nameExists(name, id);
			if (transactionTypeAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
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
