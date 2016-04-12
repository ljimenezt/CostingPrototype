package co.informatix.erp.lifeCycle.action;

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

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
/**
 * This class allows the logical names that may be crop in the database
 * 
 * The logic is to consult, edit or add names crop
 * 
 * @author Johnatan.Naranjo
 * 
 */
public class CropNamesAction implements Serializable {
	@EJB
	private CropNamesDao cropNamesDao;

	private List<CropNames> listCropNames;

	private CropNames cropNames;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	/**
	 * @return List<CropNames>: list of names shown in crop user interface
	 */
	public List<CropNames> getListCropNames() {
		return listCropNames;
	}

	/**
	 * @param listCropNames
	 *            : list of names shown in crop user interface
	 */
	public void setListCropNames(List<CropNames> listCropNames) {
		this.listCropNames = listCropNames;
	}

	/**
	 * Get data from a name crop
	 * 
	 * @return cropNames: data object containing a name Harvest
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * Set data from a name crop
	 * 
	 * @param cropNames
	 *            : data object containing a name Harvest
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return Paginador: Management paginated list of the names of crops.
	 * 
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the names of crops.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name of the crop to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of the crop to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the names of crops
	 * 
	 * @return consultarCropNames: query method names crop returns to the
	 *         template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultCropNames();
	}

	/**
	 * Consult the list of the names of the crops
	 * 
	 * @return "gesCropNames": redirects to the template to manage the names of
	 *         crops
	 */
	public String consultCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listCropNames = new ArrayList<CropNames>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = cropNamesDao.quantityCropNames(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listCropNames = cropNamesDao.consultCropNames(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listCropNames == null || listCropNames.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listCropNames == null || listCropNames.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("crop_names_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCropNames";
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
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
			consult.append("WHERE UPPER(cn.cropName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new crop name.
	 * 
	 * @param cropNames
	 *            :Crop name to be add or edit
	 * 
	 * @return "regCropNames":redirects to the record template crop.
	 */
	public String addEditCropNames(CropNames cropNames) {
		if (cropNames != null) {
			this.cropNames = cropNames;
		} else {
			this.cropNames = new CropNames();
		}
		return "regCropNames";
	}

	/**
	 * To validate the name of the crop, so it is not repeated in the database
	 * and validates against XSS.
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
			int id = cropNames.getIdCropName();
			CropNames cropNamesAux = new CropNames();
			cropNamesAux = cropNamesDao.nameExists(name, id);
			if (cropNamesAux != null) {
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
	 * Method used to save or edit the names of crops
	 * 
	 * @return consultCropNames: Redirects to manage harvests names with the
	 *         list of names updated
	 */
	public String saveCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (cropNames.getIdCropName() != 0) {
				cropNamesDao.editCropNames(cropNames);
			} else {
				messageLog = "message_registro_guardar";
				cropNamesDao.saveCropNames(cropNames);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), cropNames.getCropName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCropNames();
	}

	/**
	 * Method to remove a name from the crop of the database
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultCropNames: Consult the list of names of crops and returns
	 *         to manage the crop name.
	 */
	public String removeCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cropNamesDao.removeCropNames(cropNames);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					cropNames.getCropName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					cropNames.getCropName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCropNames();
	}

}
