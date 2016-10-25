package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.humanResources.dao.ChargeTypeDao;
import co.informatix.erp.humanResources.entities.ChargeType;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class lets you handle the business logic of the type of charge that
 * exist in the system, which allows you to insert, modify, and query.
 * 
 * @author Oscar.Amaya
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ChargeTypeAction implements Serializable {

	@EJB
	private ChargeTypeDao chargeTypeDao;
	@Inject
	private IdentityAction identity;

	private List<ChargeType> listChargeType;
	private Paginador pagination = new Paginador();
	private ChargeType chargeTypeValidity;
	private ChargeType chargeType;
	private String validity = Constantes.SI;
	private String nameSearch = "";

	/**
	 * @return validity: gets the value for the management of currency records
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            : sets the value for the management of currency records
	 */
	public void setValidity(String validity) {
		this.validity = validity;
	}

	/**
	 * @return pagination: management paginated list of types of jobs that may
	 *         be in view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : management paginated list of types of jobs that may be in
	 *            view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return listChargeType: list of types of jobs as shown in the table the
	 *         user interface.
	 */
	public List<ChargeType> getListChargeType() {
		return listChargeType;
	}

	/**
	 * @param listChargeType
	 *            : list of types of jobs as shown in the table the user
	 *            interface.
	 */
	public void setListChargeType(List<ChargeType> listChargeType) {
		this.listChargeType = listChargeType;
	}

	/**
	 * @return chargeTypeValidity: type job object to manage of the currency
	 */
	public ChargeType getChargeTypeValidity() {
		return chargeTypeValidity;
	}

	/**
	 * @param chargeTypeValidity
	 *            : type job object to manage of the currency
	 */
	public void setChargeTypeValidity(ChargeType chargeTypeValidity) {
		this.chargeTypeValidity = chargeTypeValidity;
	}

	/**
	 * @return chargeType: type job object to which the implementation of
	 *         registration or editing is done.
	 */
	public ChargeType getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            : type job object to which the implementation of registration
	 *            or editing is done.
	 */
	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return nameSearch: Name to find the type of charge
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Name to find the type of charge
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * This method prepares the view to add a new type of charge
	 * 
	 * @return navigation rule that loads register a template of charge.
	 */
	public String newChargeType() {
		chargeType = new ChargeType();
		return "regChargeType";
	}

	/**
	 * This method prepares the view to edit one type of charge
	 * 
	 * @param chargeTypeEdit
	 *            : the type of object you want to edit position.
	 * @return regChargeType: navigation rule that loads register a template of
	 *         charge.
	 */
	public String editChargeType(ChargeType chargeTypeEdit) {
		chargeType = chargeTypeEdit;
		return "regChargeType";
	}

	/**
	 * Cleans the variable used in the search for types of cargo
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return consultChargeTypes: Navigation rule shows the view
	 *         manageChargeType
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return consultChargeTypes();
	}

	/**
	 * Consult the list of types of cargo according to the term submitted
	 * 
	 * @modify Luis.Ruiz
	 * @modify Liseth.Jimenez 21/03/2012
	 * 
	 * @return gesChargeType: load navigation rule template management fee
	 *         rates.
	 */
	public String consultChargeTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRH = ControladorContexto
				.getBundle("messageHumanResources");
		String messageSearch = "";
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			String validityCondition = Constantes.IS_NULL;
			if (Constantes.NOT.equals(validity)) {
				validityCondition = Constantes.IS_NOT_NULL;
			}
			chargeTypeValidity = new ChargeType();
			this.listChargeType = new ArrayList<ChargeType>();
			long amount = this.chargeTypeDao.countChargeType(validityCondition,
					this.nameSearch);
			pagination.paginar(amount);
			this.listChargeType = chargeTypeDao.searchChargeTypes(
					pagination.getInicio(), pagination.getRango(),
					validityCondition, this.nameSearch);
			if ((this.listChargeType == null || this.listChargeType.size() <= 0)
					&& (this.nameSearch != null && !"".equals(this.nameSearch))) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			} else if (this.listChargeType == null
					|| this.listChargeType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRH.getString("tipo_cargo_label_s"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesChargeType";
	}

	/**
	 * Modifies the validity of one type of charge
	 * 
	 * @param valid
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            start with 'false', the selected record in the user interface.
	 * @return consultChargeTypes: consulting method types of cargo and freight
	 *         management template.
	 */
	public String ChargeTypeValidity(boolean valid) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			chargeTypeValidity.setUserName(identity.getUserName());
			String message = "";
			if (valid) {
				chargeTypeValidity.setDateEndValidity(new Date());
				chargeTypeDao.editChargeType(chargeTypeValidity);
				message = bundle
						.getString("message_fin_vigencia_satisfactorio") + ": ";
			} else {
				chargeTypeValidity.setDateEndValidity(null);
				chargeTypeDao.editChargeType(chargeTypeValidity);
				message = bundle
						.getString("message_inicio_vigencia_satisfactorio")
						+ ": ";
			}
			ControladorContexto.mensajeInformacion(null, message
					+ chargeTypeValidity.getName());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultChargeTypes();
	}

	/**
	 * This method allows you to edit register a new type of charge, you gets
	 * the data stored in the form and stored in the database
	 * 
	 * @return navigation rule for querying the types of charges and load
	 *         management template.
	 */
	public String addEditChargeType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "";
		try {
			chargeType.setName(WordUtils.capitalizeFully(chargeType.getName()));
			chargeType.setUserName(identity.getUserName());
			if (chargeType.getId() != null
					&& chargeType.getId().intValue() != 0) {
				chargeTypeDao.editChargeType(chargeType);
				message = MessageFormat.format(
						bundle.getString("message_registro_modificar"),
						this.chargeType.getName());
			} else {
				chargeType.setDateCreation(new Date());
				chargeTypeDao.saveChargeType(chargeType);
				message = MessageFormat.format(
						bundle.getString("message_registro_guardar"),
						this.chargeType.getName());
			}
			ControladorContexto.mensajeInformacion(null, message);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeSearch();
	}

	/**
	 * This method applies the current existence of the name of a type of charge
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateNameChargeXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String nameCapitalize = WordUtils.capitalizeFully(name);
		String clientId = toValidate.getClientId(context);
		try {
			Integer id = this.chargeType.getId();
			ChargeType chargeTypeAux = new ChargeType();
			if (id == null) {
				chargeTypeAux = chargeTypeDao.nameExists(nameCapitalize);
			} else {
				chargeTypeAux = chargeTypeDao.nameExists(nameCapitalize, id);
			}
			if (chargeTypeAux != null) {
				if (chargeTypeAux.getDateEndValidity() != null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				}
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