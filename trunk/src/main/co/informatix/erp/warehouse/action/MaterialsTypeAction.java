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
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.entities.MaterialsType;

/**
 * This class allows the logic of MaterialsType which may be in the BD The logic
 * is to consult, edit or add MaterialsType
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MaterialsTypeAction implements Serializable {

	@EJB
	private MaterialsTypeDao materialsTypeDao;

	private MaterialsType materialsType;

	private Paginador pagination = new Paginador();
	private String nameSearch;

	private List<MaterialsType> listMaterialsType;
	private ArrayList<SelectItem> options;

	/**
	 * @return materialsType: object that contains the material data Type
	 */
	public MaterialsType getMaterialsType() {
		return materialsType;
	}

	/**
	 * @param materialsType
	 *            : object that contains the material data Type
	 */
	public void setMaterialsType(MaterialsType materialsType) {
		this.materialsType = materialsType;
	}

	/**
	 * @return pagination: Management paged list names materialsType
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list names materialsType
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: name of the material type to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : name of the material type to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listMaterialsType: list of objects of type material Type
	 */
	public List<MaterialsType> getListMaterialsType() {
		return listMaterialsType;
	}

	/**
	 * @param listMaterialsType
	 *            : list of objects of type material Type
	 */
	public void setListMaterialsType(List<MaterialsType> listMaterialsType) {
		this.listMaterialsType = listMaterialsType;
	}

	/**
	 * @return options: list of the types materialsType
	 */
	public ArrayList<SelectItem> getOptions() {
		return options;
	}

	/**
	 * @param options
	 *            : list of the types materialsType
	 */
	public void setOptions(ArrayList<SelectItem> options) {
		this.options = options;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of materialsType
	 * 
	 * @return consultMaterialsType: Consult also the materialsType method that
	 *         returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultMaterialsType();
	}

	/**
	 * Consult the list of the machines
	 * 
	 * @return "gesMaterialsType": redirects to the template to manage Materials
	 *         Type
	 */
	public String consultMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMaterialsType = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listMaterialsType = new ArrayList<MaterialsType>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = materialsTypeDao.quantityMaterialsType(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listMaterialsType = materialsTypeDao.consultMaterialsType(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listMaterialsType == null || listMaterialsType.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listMaterialsType == null
					|| listMaterialsType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMaterialsType
										.getString("materials_type_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaterialsType";
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(mt.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');

		}

	}

	/**
	 * Method to edit or create a new MaterialsType.
	 * 
	 * @param materialsType
	 *            :Type materials that are adding or editing
	 * 
	 * @return "regMaterialsType":redirected to the template record Material
	 *         Type.
	 */
	public String addEditMaterialsType(MaterialsType materialsType) {

		if (materialsType != null) {
			this.materialsType = materialsType;

		} else {
			this.materialsType = new MaterialsType();

		}
		return "regMaterialsType";
	}

	/**
	 * Method used to save or edit materialsType
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultMaterialsType: Type redirects to manage materials with a
	 *         list of updated materials Type
	 */
	public String saveMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";

		try {
			if (materialsType.getIdMaterialsType() != 0) {
				materialsTypeDao.editMaterialsType(materialsType);
			} else {
				messageLog = "message_registro_guardar";
				materialsTypeDao.saveMaterialsType(materialsType);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), materialsType.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMaterialsType();
	}

	/**
	 * Method that allows one materialsType delete the database
	 * 
	 * @return consultMaterialsType(): Consult the list of Type material returns
	 *         to manage materials Type
	 */
	public String removeMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			materialsTypeDao.removeMaterialsType(materialsType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					materialsType.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					materialsType.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMaterialsType();
	}

	/**
	 * To validate the name of the materials type, so it is not repeated in the
	 * database and valid against XSS.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = materialsType.getIdMaterialsType();
			MaterialsType materialTypeAux = materialsTypeDao
					.nameExist(name, id);
			if (materialTypeAux != null) {
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
