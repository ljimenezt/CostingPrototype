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

import co.informatix.erp.humanResources.dao.ContractTypeDao;
import co.informatix.erp.humanResources.entities.ContractType;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, update, and delete types
 * of contracts in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ContractTypeAction implements Serializable {

	private List<ContractType> listContractType;
	private Paginador pagination = new Paginador();
	private ContractType contractType;

	private String nameSearch;

	@EJB
	private ContractTypeDao contractTypeDao;

	/**
	 * @return listContractType: List of types of contract.
	 */
	public List<ContractType> getListContractType() {
		return listContractType;
	}

	/**
	 * @param listContractType
	 *            : List of types of contract.
	 */
	public void setListContractType(List<ContractType> listContractType) {
		this.listContractType = listContractType;
	}

	/**
	 * @return pagination: Paginated list of the types of contract may be in
	 *         view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of the types of contract may be in view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return contractType: Object of the contract types.
	 */
	public ContractType getContractType() {
		return contractType;
	}

	/**
	 * @param contractType
	 *            : Object of the contract types.
	 */
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return nameSearch: Name by which you want to query the type of contract.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to query the type of contract.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load initial
	 * listing of the types of contract.
	 * 
	 * @return consultContractType: Method consulting contract types, returns to
	 *         the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultContractType();
	}

	/**
	 * Consult the list of the types of contract.
	 * 
	 * @return gesContrType: Navigation rule that redirects to manage contract
	 *         types.
	 */
	public String consultContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listContractType = new ArrayList<ContractType>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = contractTypeDao.quantityContractType(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listContractType = contractTypeDao.consultContractType(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listContractType == null || listContractType.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listContractType == null || listContractType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				messageSearch = MessageFormat.format(message,
						bundleHumanResources.getString("contract_type_label"),
						unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesContrType";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : Consult concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(ct.nombre) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new type of contract.
	 * 
	 * @param contractType
	 *            : Type of contract that will add or edit.
	 * @return regContrType: Redirected to the template record type contract.
	 */
	public String addEditContractType(ContractType contractType) {
		if (contractType != null) {
			this.contractType = contractType;
		} else {
			this.contractType = new ContractType();
		}
		return "regContrType";
	}

	/**
	 * To validate the name of the contract types, to not repeat in the database
	 * and validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = contractType.getId();
			ContractType contractTypeAux = contractTypeDao.nameExists(name, id);
			if (contractTypeAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						messageExistence, "mensaje");
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
	 * Method used to save or edit the types of contract.
	 * 
	 * @return consultContractType: Redirects to manage the types of contracts
	 *         with the list of names updated.
	 */
	public String saveContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (contractType.getId() != 0) {
				contractTypeDao.editContractType(contractType);
			} else {
				messageLog = "message_registro_guardar";
				contractTypeDao.saveContractType(contractType);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), contractType.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultContractType();
	}

	/**
	 * Method to delete a type of contract database.
	 * 
	 * @return consultContractType: Redirects to manage the types of contracts
	 *         with the list of names updated.
	 */
	public String removeContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			contractTypeDao.removeContractType(contractType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					contractType.getNombre()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					contractType.getNombre());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultContractType();
	}
}