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
	private Paginador pagination = new Paginador();

	private String nameSearch;

	private boolean edit = false;
	private int idCertificationsAndRoles;

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
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return pagination: Paginated list of human resources and certifications
	 *         may be in the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of human resources and certifications may be
	 *            in the view
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
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
	 * @return idCertificationsAndRoles: certificationsAndRoles identifier.
	 */
	public int getIdCertificationsAndRoles() {
		return idCertificationsAndRoles;
	}

	/**
	 * @param idCertificationsAndRoles
	 *            :certificationsAndRoles identifier.
	 */
	public void setIdCertificationsAndRoles(int idCertificationsAndRoles) {
		this.idCertificationsAndRoles = idCertificationsAndRoles;
	}

	/**
	 * Method to initialize the search parameters and load the template to
	 * manage human resources and certificacionesRoles.
	 * 
	 * @modify 16/03/2016 Wilhelm.Boada
	 * 
	 * @return consultHrCertRoles: Call the method consultHrCertRoles and
	 *         redirects to the template to manage human resources and
	 *         certifications
	 */
	public String searchInitialization() {
		try {
			this.idCertificationsAndRoles = 0;
			nameSearch = "";
			certificationsAndRoles = new CertificationsAndRoles();
			hr = new Hr();
			hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
			loadComboCertAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultHrCertRoles();

	}

	/**
	 * Method to edit or create human resources and certificacionesRoles.
	 * 
	 * @modify 16/03/2016 Wilhelm.Boada
	 * 
	 * @param hrCertificationsAndRoles
	 *            :Activity and certification are adding or editing.
	 * @return regHrCertRoles: Template redirects to record activities and
	 *         certifications.
	 */
	public String addEditHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) {
		edit = false;
		try {
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
				this.idCertificationsAndRoles = this.certificationsAndRoles
						.getIdCertificactionsAndRoles();
			} else {
				this.hrCertificationsAndRoles = new HrCertificationsAndRoles();
				this.hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
				this.hrCertificationsAndRolesPK
						.setCertificationsAndRoles(new CertificationsAndRoles());
				this.hrCertificationsAndRolesPK.setHr(new Hr());
			}
			loadComboCertAndRoles();
			loadComboHr();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regHrCertRoles";
	}

	/**
	 * Method that allows load the human resources in a list.
	 * 
	 * @modify 16/03/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void loadComboHr() throws Exception {
		hr = new Hr();
		itemsHr = new ArrayList<SelectItem>();
		List<Hr> listHr = hrDao.queryHr();
		if (listHr != null) {
			for (Hr hr : listHr) {
				itemsHr.add(new SelectItem(hr.getIdHr(), hr.getName() + " "
						+ hr.getFamilyName()));
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
	 * @modify 16/03/2016 Wilhelm.Boada
	 * @modify 30/05/2017 Fabian.Diaz
	 * 
	 * @return gesHrCertRoles: Redirects to the template to manage human
	 *         resources and certifications.
	 */
	public String consultHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listHrCertificationsAndRoles = new ArrayList<HrCertificationsAndRoles>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = hrCertificationsAndRolesDao.quantXIdCertRol(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
				pagination.setOpcion('f');
			}
			listHrCertificationsAndRoles = hrCertificationsAndRolesDao
					.consultXIdCertRol(pagination.getInicio(),
							pagination.getRango(), query, parameters);

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
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @return searchInitialization: Redirects to initialize variables and load
	 *         the template HRM and certifications.
	 */
	public String saveHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "";
		boolean flag = false;
		try {
			if (hrCertificationsAndRolesPK.getCertificationsAndRoles()
					.getIdCertificactionsAndRoles() != 0
					&& hrCertificationsAndRolesPK.getHr().getIdHr() != 0) {
				messageLog = "message_registro_modificar";
				hrCertificationsAndRolesDao
						.editHrCertRoles(hrCertificationsAndRoles);
				flag = true;
			} else {
				this.idCertificationsAndRoles = this.certificationsAndRoles
						.getIdCertificactionsAndRoles();
				int idHr = this.hr.getIdHr();
				boolean existPK = hrCertificationsAndRolesDao.relationExist(
						this.idCertificationsAndRoles, idHr);
				if (!existPK) {
					messageLog = "message_registro_guardar";
					hrCertificationsAndRolesPK
							.setCertificationsAndRoles(certificationsAndRoles);
					hrCertificationsAndRolesPK.setHr(hr);
					hrCertificationsAndRoles
							.setHrCertificationsAndRolesPK(hrCertificationsAndRolesPK);
					hrCertificationsAndRolesDao
							.saveHrCertRoles(hrCertificationsAndRoles);
					flag = true;
				}
			}
			Object nameCert = ValidacionesAction.getLabel(
					itemsCertificationsAndRoles, this.idCertificationsAndRoles);
			if (flag) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(messageLog), nameCert));
			} else {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_ya_existe"));
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
	 * @modify 16/03/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : query to concatenate
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
			consult.append("WHERE UPPER(hr.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');

			if (this.idCertificationsAndRoles != 0) {
				consult.append("AND cr.idCertificactionsAndRoles = :keyword3 ");
				item = new SelectItem(this.idCertificationsAndRoles, "keyword3");
				parameters.add(item);
			}
		} else {
			if (this.idCertificationsAndRoles != 0) {
				consult.append("WHERE cr.idCertificactionsAndRoles = :keyword ");
				SelectItem item = new SelectItem(this.idCertificationsAndRoles,
						"keyword");
				parameters.add(item);
			}

		}
	}

	/**
	 * Method that allows delete a record of Human Resources and certifications
	 * database.
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