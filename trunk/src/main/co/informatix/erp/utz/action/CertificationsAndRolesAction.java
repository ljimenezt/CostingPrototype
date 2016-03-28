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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * certifications and roles that may exist.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CertificationsAndRolesAction implements Serializable {

	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;

	private CertificationsAndRoles certificationsAndRoles;
	private String nameSearch;
	private List<CertificationsAndRoles> listCertificationsAndRoles;
	private Paginador pagination = new Paginador();

	/**
	 * @return certificationsAndRoles: object containing data type
	 *         certifications and roles.
	 * 
	 */
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : object containing data type certifications and roles.
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	/**
	 * @return nameSearch: name to search type certification and role.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : name to search type certification and role.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listCertificationsAndRoles: list of certifications and role.
	 */
	public List<CertificationsAndRoles> getListCertificationsAndRoles() {
		return listCertificationsAndRoles;
	}

	/**
	 * @param listCertificationsAndRoles
	 *            : list of certifications and role.
	 */
	public void setListCertificationsAndRoles(
			List<CertificationsAndRoles> listCertificationsAndRoles) {
		this.listCertificationsAndRoles = listCertificationsAndRoles;
	}

	/**
	 * @return pagination: management paginated list of certifications and role.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : management paginated list of certifications and role.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of certifications and role.
	 * 
	 * @return consultCertificationsAndRoles: method to query the roles
	 *         certifications and returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultCertificationsAndRoles();
	}

	/**
	 * Consult the list of certifications and roles.
	 * 
	 * @return gesCertifi: Template manage certifications and roles.
	 */
	public String consultCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listCertificationsAndRoles = new ArrayList<CertificationsAndRoles>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = certificationsAndRolesDao
					.quantityCertificationsAndRoles(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listCertificationsAndRoles = certificationsAndRolesDao
					.consultCertificationsAndRolesAction(
							pagination.getInicio(), pagination.getRango(),
							query, parameters);
			if ((listCertificationsAndRoles == null || listCertificationsAndRoles
					.size() <= 0) && !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listCertificationsAndRoles == null
					|| listCertificationsAndRoles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("certifications_and_roles_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCertifi";
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
			consult.append("WHERE UPPER(ct.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new CertificationsANdRoles.
	 * 
	 * @param certificationsAndRoles
	 *            :certicicationsAndRoles types that are adding or editing.
	 * 
	 * @return "regCertifi":redirects to register certicicationsAndRoles
	 *         template.
	 */
	public String addEditCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		if (certificationsAndRoles != null) {
			this.certificationsAndRoles = certificationsAndRoles;
		} else {
			this.certificationsAndRoles = new CertificationsAndRoles();
		}
		return "regCertifi";
	}

	/**
	 * To validate the name of the certification and roles, to not repeat in the
	 * database and validates against XSS.
	 * 
	 * @modify 10/03/2016 Jhair.Leal
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {

		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = certificationsAndRoles.getIdCertificactionsAndRoles();
			CertificationsAndRoles certificationsAndRolesAux = new CertificationsAndRoles();
			certificationsAndRolesAux = certificationsAndRolesDao.nameExists(
					name, id);
			if (certificationsAndRolesAux != null) {
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
	 * Method used to save or edit the types of certifications and roles.
	 * 
	 * @return consultCertificationAndRoles: Redirects to manage types of
	 *         certificationsAndroles with the list of names updated.
	 */
	public String saveCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (certificationsAndRoles.getIdCertificactionsAndRoles() != 0) {
				certificationsAndRolesDao
						.editCertificationsAndRoles(certificationsAndRoles);
			} else {
				messageLog = "message_registro_guardar";
				certificationsAndRolesDao
						.saveCertificationsAndRoles(certificationsAndRoles);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog),
					certificationsAndRoles.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCertificationsAndRoles();
	}

	/**
	 * Method to delete a type certificate and role of the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarCertificationsAndRoles(): Consult the list of the types
	 *         of certifications and role and returns to manage certification
	 *         and role.
	 */
	public String removeCertificationsAndRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			certificationsAndRolesDao
					.removeCertificationsAndRoles(certificationsAndRoles);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					certificationsAndRoles.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					certificationsAndRoles.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultCertificationsAndRoles();
	}

}
