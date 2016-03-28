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
import co.informatix.erp.warehouse.dao.DiseasesDao;
import co.informatix.erp.warehouse.entities.Diseases;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * diseases in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DiseasesAction implements Serializable {
	private List<Diseases> listDiseases;
	private Paginador pagination = new Paginador();
	private Diseases diseases;
	private String nameSearch;

	@EJB
	private DiseasesDao diseasesDao;

	/**
	 * @return listDiseases: List of diseases.
	 */
	public List<Diseases> getListDiseases() {
		return listDiseases;
	}

	/**
	 * @param listDiseases
	 *            : List of diseases.
	 */
	public void setListDiseases(List<Diseases> listDiseases) {
		this.listDiseases = listDiseases;
	}

	/**
	 * @return pagination: Paging from the list of diseases that can be in view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paging from the list of diseases that can be in view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return diseases: Object of diseases.
	 */
	public Diseases getDiseases() {
		return diseases;
	}

	/**
	 * @param diseases
	 *            : Object of diseases.
	 */
	public void setDiseases(Diseases diseases) {
		this.diseases = diseases;
	}

	/**
	 * @return nameSearch: Name by which you want to consult disease.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to consult disease.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of diseases.
	 * 
	 * @return consultDiseases: Method consulting diseases, returns to the
	 *         template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultDiseases();
	}

	/**
	 * Consult the list of existing diseases.
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return gesDiseases: Navigation rule that redirects manage diseases.
	 */
	public String consultDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listDiseases = new ArrayList<Diseases>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			Long quantity = diseasesDao.quantityDiseases(consult, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listDiseases = diseasesDao.consultDiseases(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			if ((listDiseases == null || listDiseases.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listDiseases == null || listDiseases.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("diseases_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesDiseases";
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
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(d.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new disease.
	 * 
	 * @param diseases
	 *            : Disease are adding or editing.
	 * 
	 * @return regDiseases: Template redirects to register disease.
	 */
	public String addEditDiseases(Diseases diseases) {
		if (diseases != null) {
			this.diseases = diseases;
		} else {
			this.diseases = new Diseases();
		}
		return "regDiseases";
	}

	/**
	 * To validate the name of the disease, so it is not repeated in the
	 * database and validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = diseases.getIdDisease();
			Diseases diseasesAux = new Diseases();
			diseasesAux = diseasesDao.nameExist(name, id);
			if (diseasesAux != null) {
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

	/**
	 * Method used to save or edit diseases.
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultDiseases: Redirects to manage the disease with the list of
	 *         names updated.
	 */
	public String saveDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (diseases.getIdDisease() != 0) {
				diseasesDao.editDiseases(diseases);
			} else {
				messageLog = "message_registro_guardar";
				diseasesDao.saveDiseases(diseases);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							diseases.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultDiseases();
	}

	/**
	 * Method for eliminating a disease of the database.
	 * 
	 * 
	 * @return consultDiseases: Redirects to manage the disease with the list of
	 *         names updated.
	 */
	public String removeDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			diseasesDao.removeDiseases(diseases);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					diseases.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					diseases.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultDiseases();
	}

}
