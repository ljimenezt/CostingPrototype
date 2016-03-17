package co.informatix.erp.informacionBase.action;

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

import co.informatix.erp.informacionBase.dao.CivilStatusDao;
import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating or removal of
 * civil States in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CivilStatusAction implements Serializable {
	private List<CivilStatus> listCivilStatus;
	private Paginador paginador = new Paginador();
	private CivilStatus civilStatus;
	private String nameSearch;

	@EJB
	private CivilStatusDao civilStatusDao;

	/**
	 * @return listCivilStatus: List of civil status.
	 */
	public List<CivilStatus> getListCivilStatus() {
		return listCivilStatus;
	}

	/**
	 * @param listCivilStatus
	 *            : List of civil status.
	 */
	public void setListCivilStatus(List<CivilStatus> listCivilStatus) {
		this.listCivilStatus = listCivilStatus;
	}

	/**
	 * @return paginador: Paginated list of civil states can be in view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of civil states can be in view.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return civilStatus: Object civil status.
	 */
	public CivilStatus getCivilStatus() {
		return civilStatus;
	}

	/**
	 * @param civilStatus
	 *            : Object civil status.
	 */
	public void setCivilStatus(CivilStatus civilStatus) {
		this.civilStatus = civilStatus;
	}

	/**
	 * @return nameSearch: Name by which you want to view the status civil.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to view the status civil.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of civil states.
	 * 
	 * @return consultCivilStatus: Method consulting civil states, returns to
	 *         the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultCivilStatus();
	}

	/**
	 * Consult the list of existing civil status.
	 * 
	 * @return gesCivilEst: Navigation rule that redirects manage civil status.
	 */
	public String consultCivilStatus() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleGeneral = ControladorContexto
				.getBundle("mensajeInformacionBase");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listCivilStatus = new ArrayList<CivilStatus>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = civilStatusDao.quantityCivilStatus(query,
					parameters);
			if (quantity != null) {
				paginador.paginar(quantity);
			}
			listCivilStatus = civilStatusDao.consultCivilStatus(
					paginador.getInicio(), paginador.getRango(), query,
					parameters);
			if ((listCivilStatus == null || listCivilStatus.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listCivilStatus == null || listCivilStatus.size() <= 0) {
				ControladorContexto
						.mensajeInformacion(null, MessageFormat.format(bundle
								.getString("message_no_existen_registros"), ""));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleGeneral.getString("civil_status_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCivilEst";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to display depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : Consult concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * 
	 * @param unionMessagesSearch
	 *            : Message Word Search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(cs.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parametros.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new civil status.
	 * 
	 * @param civilStatus
	 *            : Marital status to be add or edit.
	 * 
	 * @return regCivilEst: Redirects the state record template civil.
	 */
	public String addEditCivilStatus(CivilStatus civilStatus) {
		if (civilStatus != null) {
			this.civilStatus = civilStatus;
		} else {
			this.civilStatus = new CivilStatus();
		}
		return "regCivilEst";
	}

	/**
	 * To validate the name of civil states, so it is not repeated in the
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
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = civilStatus.getId();
			CivilStatus civilStatusAux = new CivilStatus();
			civilStatusAux = civilStatusDao.nameExist(name, id);
			if (civilStatusAux != null) {
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
	 * Method used to save or edit the civil status.
	 * 
	 * @return consultCivilStatus: Redirects to manage civil states with the
	 *         list of names updated.
	 */
	public String saveCivilStatus() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (civilStatus.getId() != 0) {
				civilStatusDao.editCivilStatus(civilStatus);
			} else {
				messageLog = "message_registro_guardar";
				civilStatusDao.saveCivilStatus(civilStatus);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), civilStatus.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCivilStatus();
	}

	/**
	 * Method to delete a civil state of the database.
	 * 
	 * @return consultCivilStatus: Redirects to manage the states civilians with
	 *         the list of names updated.
	 */
	public String removeCivilStatus() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			civilStatusDao.removeCivilStatus(civilStatus);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					civilStatus.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					civilStatus.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCivilStatus();
	}
}
