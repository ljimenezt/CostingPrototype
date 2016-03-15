package co.informatix.erp.utz.action;

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

import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.dao.HrCertificationsAndRolesDao;
import co.informatix.erp.utz.entities.CertificationsAndRoles;
import co.informatix.erp.utz.entities.HrCertificationsAndRoles;
import co.informatix.erp.utz.entities.HrCertificationsAndRolesPK;

/**
 * This class is all related logic with the creation and updating of human
 * resources and certificacionesRoles system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HrCertificationsAndRolesAction implements Serializable {
	private List<SelectItem> itemsHr;
	private List<SelectItem> itemsCertificationsAndRoles;
	private List<HrCertificationsAndRoles> listHrCertificationsAndRoles;

	private Hr hr;
	private CertificationsAndRoles certificationsAndRoles;
	private HrCertificationsAndRoles hrCertificationsAndRoles;
	private HrCertificationsAndRolesPK hrCertificationsAndRolesPK;

	private String nameSearch;
	private Paginador paginador = new Paginador();
	private boolean edit = false;

	@EJB
	private HrDao hrDao;

	@EJB
	private HrCertificationsAndRolesDao hrCertificationsAndRolesDao;

	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;

	/**
	 * @return itemsHr: List of human resources associated with certifications
	 *         and roles.
	 */
	public List<SelectItem> getItemsHr() {
		return itemsHr;
	}

	/**
	 * @param itemsHr
	 *            : List of human resources associated with certifications and
	 *            roles.
	 * 
	 */
	public void setItemsHr(List<SelectItem> itemsHr) {
		this.itemsHr = itemsHr;
	}

	/**
	 * @return itemsCertificationsAndRoles: List of certifications and roles
	 *         associated with human resources.
	 */
	public List<SelectItem> getItemsCertificationsAndRoles() {
		return itemsCertificationsAndRoles;
	}

	/**
	 * @param itemsCertificationsAndRoles
	 *            : List of certifications and roles associated with human
	 *            resources.
	 * 
	 */
	public void setItemsCertificationsAndRoles(
			List<SelectItem> itemsCertificationsAndRoles) {
		this.itemsCertificationsAndRoles = itemsCertificationsAndRoles;
	}

	/**
	 * @return listHrCertificationsAndRoles: List of human resources and
	 *         certifications.
	 */
	public List<HrCertificationsAndRoles> getListHrCertificationsAndRoles() {
		return listHrCertificationsAndRoles;
	}

	/**
	 * @param listHrCertificationsAndRoles
	 *            : List of human resources and certifications.
	 * 
	 */
	public void setListHrCertificationsAndRoles(
			List<HrCertificationsAndRoles> listHrCertificationsAndRoles) {
		this.listHrCertificationsAndRoles = listHrCertificationsAndRoles;
	}

	/**
	 * @return hr: Human resources object.
	 */
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Human resources object.
	 * 
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return certificationsAndRoles: Object certifications and roles.
	 */
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : Object certifications and roles.
	 * 
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	/**
	 * @return hrCertificationsAndRoles: Human resources object and
	 *         certifications and roles.
	 */
	public HrCertificationsAndRoles getHrCertificationsAndRoles() {
		return hrCertificationsAndRoles;
	}

	/**
	 * @param hrCertificationsAndRoles
	 *            : Human resources object and certifications and roles.
	 * 
	 */
	public void setHrCertificationsAndRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) {
		this.hrCertificationsAndRoles = hrCertificationsAndRoles;
		this.hr = this.hrCertificationsAndRoles.getHrCertificationsAndRolesPK()
				.getHr();
	}

	/**
	 * @return hrCertificationsAndRolesPK: Object of the primary key human
	 *         resources and certifications and roles.
	 */
	public HrCertificationsAndRolesPK getHrCertificationsAndRolesPK() {
		return hrCertificationsAndRolesPK;
	}

	/**
	 * @param hrCertificationsAndRolesPK
	 *            : Object of the primary key human resources and certifications
	 *            and roles.
	 * 
	 */
	public void setHrCertificationsAndRolesPK(
			HrCertificationsAndRolesPK hrCertificationsAndRolesPK) {
		this.hrCertificationsAndRolesPK = hrCertificationsAndRolesPK;
	}

	/**
	 * @return nameSearch: Human resource name for.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Human resource name for.
	 * 
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return paginador: Paginated list of human resources and certifications
	 *         may be in the view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of human resources and certifications may be
	 *            in the view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return edit: Boolean variable that allows to verify if the user is
	 *         editing or saving.
	 */
	public boolean isEdit() {
		return edit;
	}

	/**
	 * @param edit
	 *            : Boolean variable that allows to verify if the user is
	 *            editing or saving.
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * Method to initialize the search parameters and load the template to
	 * manage human resources and certificacionesRoles.
	 * 
	 * @return gesActivAndCert: Returns to the template of human resource
	 *         management and certifications.
	 */
	public String searchInitialization() {
		try {
			nameSearch = "";
			certificationsAndRoles = new CertificationsAndRoles();
			hr = new Hr();
			hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
			loadComboCertAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesHrCertRoles";

	}

	/**
	 * Method to edit or create human resources and certificacionesRoles.
	 * 
	 * @param hrCertificationsAndRoles
	 *            :Activity and certification are adding or editing.
	 * 
	 * @return regHrCertRoles: Template redirects to record activities and
	 *         certifications.
	 */
	public String addEditHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) {
		edit = false;
		try {
			loadComboCertAndRoles();
			loadComboHr();
			if (hrCertificationsAndRoles != null) {
				this.hrCertificationsAndRoles = hrCertificationsAndRoles;
				setCertificationsAndRoles(hrCertificationsAndRoles
						.getHrCertificationsAndRolesPK()
						.getCertificationsAndRoles());
				setHr(hrCertificationsAndRoles.getHrCertificationsAndRolesPK()
						.getHr());
				hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
				hrCertificationsAndRolesPK
						.setCertificationsAndRoles(certificationsAndRoles);
				hrCertificationsAndRolesPK.setHr(hr);
				edit = true;
			} else {
				this.hrCertificationsAndRoles = new HrCertificationsAndRoles();
				this.hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
				this.hrCertificationsAndRolesPK
						.setCertificationsAndRoles(new CertificationsAndRoles());
				this.hrCertificationsAndRolesPK.setHr(new Hr());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regHrCertRoles";
	}

	/**
	 * Method that allows load the human resources in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboHr() throws Exception {
		hr = new Hr();
		itemsHr = new ArrayList<SelectItem>();
		List<Hr> listHr = hrDao.consultarHr();
		if (listHr != null) {
			for (Hr hr : listHr) {
				itemsHr.add(new SelectItem(hr.getIdHr(), hr.getName()));
			}
		}

	}

	/**
	 * Method that allows load the certifications and roles in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboCertAndRoles() throws Exception {
		certificationsAndRoles = new CertificationsAndRoles();
		itemsCertificationsAndRoles = new ArrayList<SelectItem>();
		List<CertificationsAndRoles> listCertificationsAndRoles = certificationsAndRolesDao
				.consultCertificationsAndRoles();
		if (listCertificationsAndRoles != null) {
			for (CertificationsAndRoles certificationsAndRoles : listCertificationsAndRoles) {
				itemsCertificationsAndRoles.add(new SelectItem(
						certificationsAndRoles.getIdCertificactionsAndRoles(),
						certificationsAndRoles.getName()));
			}
		}

	}

	/**
	 * Consult the list of human resources and associated certifications
	 * certifications and roles.
	 * 
	 * @return gesHrCertRoles: Redirects to the template to manage human
	 *         resources and certifications.
	 */
	public String consultHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listHrCertificationsAndRoles = new ArrayList<HrCertificationsAndRoles>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		int idCertAndRoles = this.certificationsAndRoles
				.getIdCertificactionsAndRoles();
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = hrCertificationsAndRolesDao.quantXIdCertRol(
					idCertAndRoles, query, parameters);
			if (quantity != null) {
				paginador.paginar(quantity);
			}
			listHrCertificationsAndRoles = hrCertificationsAndRolesDao
					.consultXIdCertRol(idCertAndRoles, paginador.getInicio(),
							paginador.getRango(), query, parameters);

			if ((listHrCertificationsAndRoles == null || listHrCertificationsAndRoles
					.size() <= 0) && !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listHrCertificationsAndRoles == null
					|| listHrCertificationsAndRoles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("hr_certifications_roles_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesHrCertRoles";
	}

	/**
	 * Method used to save or edit human resources and certifications.
	 * 
	 * @return inicializarBusqueda: Redirects to initialize variables and load
	 *         the template HRM and certifications.
	 * 
	 */
	public String saveHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog;
		try {
			Object nameCert = ValidacionesAction.getLabel(
					itemsCertificationsAndRoles,
					this.certificationsAndRoles.getIdCertificactionsAndRoles());
			if (hrCertificationsAndRolesPK.getCertificationsAndRoles()
					.getIdCertificactionsAndRoles() != 0
					&& hrCertificationsAndRolesPK.getHr().getIdHr() != 0) {
				messageLog = "message_registro_modificar";
				hrCertificationsAndRolesDao
						.editHrCertRoles(hrCertificationsAndRoles);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(messageLog), nameCert));
			} else {
				int idCertAndRoles = this.certificationsAndRoles
						.getIdCertificactionsAndRoles();
				int idHr = this.hr.getIdHr();
				boolean existPK = hrCertificationsAndRolesDao.relationExist(
						idCertAndRoles, idHr);
				if (!existPK) {
					messageLog = "message_registro_guardar";
					hrCertificationsAndRolesPK
							.setCertificationsAndRoles(certificationsAndRoles);
					hrCertificationsAndRolesPK.setHr(hr);
					hrCertificationsAndRoles
							.setHrCertificationsAndRolesPK(hrCertificationsAndRolesPK);
					hrCertificationsAndRolesDao
							.saveHrCertRoles(hrCertificationsAndRoles);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(messageLog), nameCert));
				} else {
					messageLog = "message_ya_existe";
					ControladorContexto.mensajeInformacion(null,
							bundle.getString(messageLog));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
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
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(hr.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method that allows delete a record of Human Resources and certifications
	 * database.
	 * 
	 * 
	 * @return searchInitialization: Redirects to initialize variables and load
	 *         the template human resources management and certifications.
	 */
	public String removeHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrCertificationsAndRolesDao
					.removeHrCertRoles(hrCertificationsAndRoles);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					hrCertificationsAndRoles.getHrCertificationsAndRolesPK()
							.getHr().getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					hrCertificationsAndRoles.getHrCertificationsAndRolesPK()
							.getHr().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchInitialization();
	}

}
