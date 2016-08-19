package co.informatix.erp.informacionBase.action;

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

import co.informatix.erp.informacionBase.dao.TypeUnitDao;
import co.informatix.erp.informacionBase.entities.TypeUnit;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of TypeUnit which may be in the database. The
 * logic is to consult, edit or add TypeUnit.
 * 
 * @author Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TypeUnitAction implements Serializable {
	@EJB
	private TypeUnitDao typeUnitDao;

	private TypeUnit typeUnit;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	private List<TypeUnit> listTypeUnits;

	/**
	 * @return typeUnit: object that contains the data of type Unit.
	 */
	public TypeUnit getTypeUnit() {
		return typeUnit;
	}

	/**
	 * @param typeUnit
	 *            : object that contains the data of type Unit.
	 */
	public void setTypeUnit(TypeUnit typeUnit) {
		this.typeUnit = typeUnit;
	}

	/**
	 * @return pagination: Management paged list names typeUnit.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list names typeUnit.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Type unit name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Type unit name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listTypeUnits: list of objects of Type Unit.
	 */
	public List<TypeUnit> getListTypeUnits() {
		return listTypeUnits;
	}

	/**
	 * @param listTypeUnits
	 *            : list of objects of Type Unit.
	 */
	public void setListTypeUnits(List<TypeUnit> listTypeUnits) {
		this.listTypeUnits = listTypeUnits;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of type Unit.
	 * 
	 * @return consultTypeUnit: method that allows consulting the Type Unit, it
	 *         redirects to the manage type unit template.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultTypeUnit();
	}

	/**
	 * Consult the list of the Type Unit.
	 * 
	 * @return "manTypeUnit": It redirects to the template to manage the Type
	 *         Unit.
	 */
	public String consultTypeUnit() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleTypeUnit = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listTypeUnits = new ArrayList<TypeUnit>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = typeUnitDao.quantityTypeUnit(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listTypeUnits = typeUnitDao.consultTypeUnit(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			if ((listTypeUnits == null || listTypeUnits.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listTypeUnits == null || listTypeUnits.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleTypeUnit.getString("type_unit_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manTypeUnit";
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
			consult.append("WHERE UPPER(tu.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}

	}

	/**
	 * Method to edit or create a new type unit.
	 * 
	 * @param typeUnit
	 *            : Type Unit to be add or edit.
	 * @return "regTypeUnit":redirected to the template to manage Type Unit.
	 */
	public String addEditTypeUnit(TypeUnit typeUnit) {
		if (typeUnit != null) {
			this.typeUnit = typeUnit;

		} else {
			this.typeUnit = new TypeUnit();

		}
		return "regTypeUnit";
	}

	/**
	 * Method used to save or edit TypeUnit.
	 * 
	 * @return consultTypeUnit: Type Units redirects to manage the list of
	 *         updated Type Units.
	 */
	public String saveTypeUnit() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (typeUnit.getId() != 0) {
				typeUnitDao.editTypeUnit(typeUnit);
			} else {
				messageLog = "message_registro_guardar";
				typeUnitDao.saveTypeUnit(typeUnit);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							typeUnit.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultTypeUnit();
	}

	/**
	 * Method to delete a type unit of the database.
	 * 
	 * @return consultTypeUnit(): Consult the list of type Unit and returns to
	 *         manage type Unit.
	 */
	public String removeTypeUnit() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			typeUnitDao.removeTypeUnit(typeUnit);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					typeUnit.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					typeUnit.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultTypeUnit();
	}

	/**
	 * To validate the name of the type unit, so it is not repeated in the
	 * database and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
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
			int id = typeUnit.getId();
			TypeUnit typeUnitAux = typeUnitDao.nameExists(name, id);
			if (typeUnitAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
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