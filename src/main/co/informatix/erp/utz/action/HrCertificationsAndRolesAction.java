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

	private String nombreBuscar;
	private Paginador paginador = new Paginador();
	private boolean editar = false;

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
	 * @return nombreBuscar: Human resource name for.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Human resource name for.
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
	 *            in the view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return editar: Boolean variable that allows to verify if the user is
	 *         editing or saving.
	 */
	public boolean isEditar() {
		return editar;
	}

	/**
	 * @param editar
	 *            : Boolean variable that allows to verify if the user is
	 *            editing or saving.
	 */
	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	/**
	 * Method to initialize the search parameters and load the template to
	 * manage human resources and certificacionesRoles.
	 * 
	 * @return gesActivAndCert: Returns to the template of human resource
	 *         management and certifications.
	 */
	public String inicializarBusqueda() {
		try {
			nombreBuscar = "";
			certificationsAndRoles = new CertificationsAndRoles();
			hr = new Hr();
			hrCertificationsAndRolesPK = new HrCertificationsAndRolesPK();
			cargarComboCertAndRoles();
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
	public String agregarEditarHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) {
		editar = false;
		try {
			cargarComboCertAndRoles();
			cargarComboHr();
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
				editar = true;
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
	private void cargarComboHr() throws Exception {
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
	private void cargarComboCertAndRoles() throws Exception {
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
	public String consultarHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listHrCertificationsAndRoles = new ArrayList<HrCertificationsAndRoles>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		int idCertAndRoles = this.certificationsAndRoles
				.getIdCertificactionsAndRoles();
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = hrCertificationsAndRolesDao.cantXIdCertRol(
					idCertAndRoles, consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listHrCertificationsAndRoles = hrCertificationsAndRolesDao
					.consultarXIdCertRol(idCertAndRoles, paginador.getInicio(),
							paginador.getRango(), consulta, parametros);

			if ((listHrCertificationsAndRoles == null || listHrCertificationsAndRoles
					.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listHrCertificationsAndRoles == null
					|| listHrCertificationsAndRoles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("hr_certifications_roles_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
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
	public String guardarHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro;
		try {
			Object nameCert = ValidacionesAction.getLabel(
					itemsCertificationsAndRoles,
					this.certificationsAndRoles.getIdCertificactionsAndRoles());
			if (hrCertificationsAndRolesPK.getCertificationsAndRoles()
					.getIdCertificactionsAndRoles() != 0
					&& hrCertificationsAndRolesPK.getHr().getIdHr() != 0) {
				mensajeRegistro = "message_registro_modificar";
				hrCertificationsAndRolesDao
						.editarHrCertRoles(hrCertificationsAndRoles);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(mensajeRegistro), nameCert));
			} else {
				int idCertAndRoles = this.certificationsAndRoles
						.getIdCertificactionsAndRoles();
				int idHr = this.hr.getIdHr();
				boolean existePK = hrCertificationsAndRolesDao.relacionExiste(
						idCertAndRoles, idHr);
				if (!existePK) {
					mensajeRegistro = "message_registro_guardar";
					hrCertificationsAndRolesPK
							.setCertificationsAndRoles(certificationsAndRoles);
					hrCertificationsAndRolesPK.setHr(hr);
					hrCertificationsAndRoles
							.setHrCertificationsAndRolesPK(hrCertificationsAndRolesPK);
					hrCertificationsAndRolesDao
							.guardarHrCertRoles(hrCertificationsAndRoles);
					ControladorContexto
							.mensajeInformacion(null,
									MessageFormat.format(
											bundle.getString(mensajeRegistro),
											nameCert));
				} else {
					mensajeRegistro = "message_ya_existe";
					ControladorContexto.mensajeInformacion(null,
							bundle.getString(mensajeRegistro));
				}
			}
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
			consult.append("AND UPPER(hr.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method that allows delete a record of Human Resources and certifications
	 * database.
	 * 
	 * 
	 * @return inicializarBusqueda: Redirects to initialize variables and load
	 *         the template human resources management and certifications.
	 */
	public String eliminarHrCertRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrCertificationsAndRolesDao
					.eliminarHrCertRoles(hrCertificationsAndRoles);
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

		return inicializarBusqueda();
	}

}
