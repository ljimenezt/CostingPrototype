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

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.ActivitiesAndCertificationsDao;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.entities.ActivitiesAndCertifications;
import co.informatix.erp.utz.entities.ActivitiesAndCertificationsPK;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class is all related logic with creating activities and certifications
 * in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndCertificationsAction implements Serializable {
	private List<Activities> listActivities;
	private List<SelectItem> itemsCertificationsAndRoles;
	private List<SelectItem> itemsActivities;

	private CertificationsAndRoles certificationsAndRoles;
	private Activities activities;
	private ActivityNames activityNames;
	private ActivitiesAndCertifications activitiesAndCertifications;
	private ActivitiesAndCertificationsPK activitiesAndCertificationsPK;

	private int idCertAndRoles;

	private String nombreBuscar;

	private Paginador paginador = new Paginador();

	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;

	@EJB
	private ActivitiesAndCertificationsDao activitiesAndCertificationsDao;

	@EJB
	private ActivitiesDao activitiesDao;

	/**
	 * @return listActivities: List of activities.
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : List of activities.
	 * 
	 */
	public void setListActivities(List<Activities> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * @return itemsCertificationsAndRoles: List items certifications and roles.
	 */
	public List<SelectItem> getItemsCertificationsAndRoles() {
		return itemsCertificationsAndRoles;
	}

	/**
	 * @param itemsCertificationsAndRoles
	 *            : List items certifications and roles.
	 * 
	 */
	public void setItemsCertificationsAndRoles(
			List<SelectItem> itemsCertificationsAndRoles) {
		this.itemsCertificationsAndRoles = itemsCertificationsAndRoles;
	}

	/**
	 * @return itemsActivities: List of items of activities.
	 */
	public List<SelectItem> getItemsActivities() {
		return itemsActivities;
	}

	/**
	 * @param itemsActivities
	 *            : List of items of activities.
	 * 
	 */
	public void setItemsActivities(List<SelectItem> itemsActivities) {
		this.itemsActivities = itemsActivities;
	}

	/**
	 * @return certificationsAndRoles: Object to certification and associated
	 *         roles.
	 */
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : Object to certification and associated roles.
	 * 
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	/**
	 * @return activities: Object associated activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Object associated activities.
	 * 
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return activityNames: Name Object associated activity.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Name Object associated activity.
	 * 
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return activitiesAndCertifications: Object and certification activities.
	 */
	public ActivitiesAndCertifications getActivitiesAndCertifications() {
		return activitiesAndCertifications;
	}

	/**
	 * @param activitiesAndCertifications
	 *            : Object and certification activities.
	 * 
	 */
	public void setActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications) {
		this.activitiesAndCertifications = activitiesAndCertifications;
	}

	/**
	 * @return activitiesAndCertificationsPK: Object the composite key
	 *         activities and certifications.
	 */
	public ActivitiesAndCertificationsPK getActivitiesAndCertificationsPK() {
		return activitiesAndCertificationsPK;
	}

	/**
	 * @param activitiesAndCertificationsPK
	 *            : Object the composite key activities and certifications.
	 * 
	 */
	public void setActivitiesAndCertificationsPK(
			ActivitiesAndCertificationsPK activitiesAndCertificationsPK) {
		this.activitiesAndCertificationsPK = activitiesAndCertificationsPK;
	}

	/**
	 * @return idCertAndRoles: Identifier of certifications and roles.
	 */
	public int getIdCertAndRoles() {
		return idCertAndRoles;
	}

	/**
	 * @param idCertAndRoles
	 *            : Identifier of certifications and roles.
	 * 
	 */
	public void setIdCertAndRoles(int idCertAndRoles) {
		this.idCertAndRoles = idCertAndRoles;
	}

	/**
	 * @return nombreBuscar: Activity name to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Activity name to search
	 * 
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
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
	 *            in the view.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * Method to initialize the search parameters and load the template to
	 * manage activities and certifications.
	 * 
	 * @return gesActivAndCert: Returns to the template management and
	 *         certification activities.
	 */
	public String inicializarBusqueda() {
		try {
			nombreBuscar = "";
			certificationsAndRoles = new CertificationsAndRoles();
			activityNames = new ActivityNames();
			cargarComboCertAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesActivAndCert";

	}

	/**
	 * Method to edit or create a certification activity.
	 * 
	 * @param activitiesAndCertifications
	 *            :Activity and certification are adding or editing.
	 * 
	 * @return regActivAndCert: Template redirects to record activities and
	 *         certifications.
	 */
	public String agregarEditarActiAndCert(
			ActivitiesAndCertifications activitiesAndCertifications) {
		try {
			cargarComboCertAndRoles();
			cargarComboActivityNames();
			if (activitiesAndCertifications != null) {
				this.activitiesAndCertifications = activitiesAndCertifications;

			} else {
				this.certificationsAndRoles = new CertificationsAndRoles();
				this.activityNames = new ActivityNames();
				this.activitiesAndCertifications = new ActivitiesAndCertifications();
				this.activitiesAndCertificationsPK = new ActivitiesAndCertificationsPK();
				this.activitiesAndCertificationsPK
						.setActivities(new Activities());
				this.activitiesAndCertificationsPK
						.setCertificationsAndRoles(new CertificationsAndRoles());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regActivAndCert";
	}

	/**
	 * Method to load certifications and roles in a list.
	 * 
	 * @throws Exception
	 */
	private void cargarComboCertAndRoles() throws Exception {
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
	 * Method that allows load the certificates and roles in a list.
	 * 
	 * @throws Exception
	 */
	private void cargarComboActivityNames() throws Exception {
		itemsActivities = new ArrayList<SelectItem>();
		List<Activities> listActivities = activitiesDao.consultarActivities();
		if (listActivities != null) {
			for (Activities activities : listActivities) {
				itemsActivities.add(new SelectItem(activities.getActivityName()
						.getIdActivityName(), activities.getActivityName()
						.getActivityName()));
			}
		}
	}

	/**
	 * Consult the list of activities and associated certifications
	 * certifications.
	 * 
	 * @return "gesActivAndCert": Redirects to the template to manage activities
	 *         and certifications.
	 */
	public String consultarActividades() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listActivities = new ArrayList<Activities>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		this.idCertAndRoles = this.certificationsAndRoles
				.getIdCertificactionsAndRoles();
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = activitiesDao.cantidadActivitiesXIdCert(
					idCertAndRoles, consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listActivities = activitiesDao.consultarActivityNamesXIdCert(
					idCertAndRoles, paginador.getInicio(),
					paginador.getRango(), consulta, parametros);

			if ((listActivities == null || listActivities.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activities_certifications_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesActivAndCert";
	}

	/**
	 * Method used to save or edit activities and certifications.
	 * 
	 * @return inicializarBusqueda: Method to initialize the search parameters
	 *         and load the template to manage activities and certifications.
	 * 
	 */
	public String guardarActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		int idActName = this.activityNames.getIdActivityName();
		try {
			Object nameCert = ValidacionesAction.getLabel(
					itemsCertificationsAndRoles,
					this.certificationsAndRoles.getIdCertificactionsAndRoles());
			activities = activitiesDao.activityXIdActNames(idActName);
			String mensajeRegistro = "message_registro_guardar";
			activitiesAndCertificationsPK.setActivities(activities);
			activitiesAndCertificationsPK
					.setCertificationsAndRoles(certificationsAndRoles);
			activitiesAndCertifications
					.setActivitiesAndCertificationsPK(activitiesAndCertificationsPK);
			activitiesAndCertificationsDao
					.guardarActivitiesAndCertifications(activitiesAndCertifications);

			String format = MessageFormat.format(
					bundle.getString(mensajeRegistro), nameCert);
			ControladorContexto.mensajeInformacion(null, format);
			activityNames = new ActivityNames();
			certificationsAndRoles = new CertificationsAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
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
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to delete an activity, certification of the database.
	 * 
	 * @return inicializarBusqueda: Method to initialize the search parameters
	 *         and load the template to manage activities and certifications.
	 */
	public String eliminarActivitiesAndCert() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			int idActivities = this.activities.getIdActivity();
			ActivitiesAndCertifications activitiesAndCertifications = activitiesAndCertificationsDao
					.activAndCertifXIdActiviAndIdCertAndRol(idActivities,
							this.idCertAndRoles);
			activitiesAndCertificationsDao
					.eliminarActivitiesAndCertifications(activitiesAndCertifications);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.activities.getActivityName().getActivityName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.activities.getActivityName().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}
}
