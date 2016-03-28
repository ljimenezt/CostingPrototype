package co.informatix.erp.organizaciones.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.informacionBase.dao.TipoDocumentoDao;
import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.erp.organizaciones.dao.OrganizacionDao;
import co.informatix.erp.organizaciones.entities.Organizacion;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class allows business logic of the application organizations, as well as
 * people who are in the organization
 * 
 * The logic is to see, edit, add or change the validity of an organization and
 * add people to the organization, terminate the contract or term of the person
 * in the organization.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class OrganizacionAction implements Serializable {

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	@EJB
	private OrganizacionDao organizacionDao;

	@EJB
	private TipoDocumentoDao tipoDocumentoDao;
	@EJB
	private FileUploadBean fileUploadBean;

	private List<Organizacion> organizaciones;
	private List<Persona> personas;
	private HashMap<String, Short> itemsTiposDocumentos;
	private Paginador pagination = new Paginador();
	private Organizacion organizacionAModificarVigencia;
	private Organizacion organizacion;
	private Persona persona;
	private String vigencia = Constantes.SI;
	private String nombreBuscar;
	private String carpetaArchivos;
	private String carpetaArchivosTemporal;
	private String filtroBusqueda;
	private boolean cargarFotoTemporal;

	/**
	 * Gets the object reference where the organization is positioned to be the
	 * change the current
	 * 
	 * @return organizacionAModificarVigencia: object reference to the
	 *         organization that you are going to change the current
	 */
	public Organizacion getOrganizacionAModificarVigencia() {
		return organizacionAModificarVigencia;
	}

	/**
	 * Sets the object reference where the organization is positioned to be the
	 * change the current
	 * 
	 * @param organizacionAModificarVigencia
	 *            : object reference to the organization that you are going to
	 *            change the current
	 */
	public void setOrganizacionAModificarVigencia(
			Organizacion organizacionAModificarVigencia) {
		this.organizacionAModificarVigencia = organizacionAModificarVigencia;
	}

	/**
	 * @return carpetaArchivos: Variable that gets the path to the folder where
	 *         the logo of the organization are stored
	 */
	public String getCarpetaArchivos() {
		this.carpetaArchivos = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_LOGOS_ORGANIZACIONES;
		return carpetaArchivos;
	}

	/**
	 * 
	 * @param carpetaArchivos
	 *            :Variable that gets the path to the folder where the logo of
	 *            the organization are stored
	 */
	public void setCarpetaArchivos(String carpetaArchivos) {
		this.carpetaArchivos = carpetaArchivos;
	}

	/**
	 * @return carpetaArchivosTemporal: path of the temporary folder where logo
	 *         or photos of the organization are loaded.
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return carpetaArchivosTemporal;
	}

	/**
	 * @return cargarFotoTemporal: Flag indicating whether the picture is loaded
	 *         from the temporary location or not
	 */
	public boolean isCargarFotoTemporal() {
		return cargarFotoTemporal;
	}

	/**
	 * @param cargarFotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setCargarFotoTemporal(boolean cargarFotoTemporal) {
		this.cargarFotoTemporal = cargarFotoTemporal;
	}

	/**
	 * @return filtroBusqueda:Filter search of organizations
	 */
	public String getFiltroBusqueda() {
		return filtroBusqueda;
	}

	/**
	 * @param filtroBusqueda
	 *            Filter search of organizations
	 */
	public void setFiltroBusqueda(String filtroBusqueda) {
		this.filtroBusqueda = filtroBusqueda;
	}

	/**
	 * @return fileUploadBean: Variable that gets the object for uploading
	 *         files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : Variable that gets the object for uploading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return organizacion: Object representing the Organization
	 */
	public Organizacion getOrganizacion() {
		return organizacion;
	}

	/**
	 * 
	 * @param organizacion
	 *            : Object representing the Organization
	 */
	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	/**
	 * 
	 * @return organizaciones: Object list of organizations that are loaded into
	 *         the interface table.
	 */
	public List<Organizacion> getOrganizaciones() {
		return organizaciones;
	}

	/**
	 * @param organizaciones
	 *            : Object list of organizations that are loaded into the
	 *            interface table.
	 */
	public void setOrganizaciones(List<Organizacion> organizaciones) {
		this.organizaciones = organizaciones;
	}

	/**
	 * 
	 * @return pagination: Pager object list of Organizations
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            :Pager object list of Organizations
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * 
	 * @return vigencia: It is giving the selected value 'yes' of existing and
	 *         'no' for not applicable
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * 
	 * @param vigencia
	 *            : It is giving the selected value 'yes' of existing and 'no'
	 *            for not applicable
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nombreBuscar: Name to search for the person who wants to
	 *         associate with the organization
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name to search for the person who wants to associate with
	 *            the organization
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return personas: List of persons encountered by name
	 */
	public List<Persona> getPersonas() {
		return personas;
	}

	/**
	 * @param personas
	 *            : List of persons encountered by name
	 */
	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	/**
	 * @return persona: get the person associated with the organization.
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona
	 *            : set the person associated with the organization.
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/**
	 * List of items that get the document types in the combo of the user
	 * interface
	 * 
	 * @return itemsTiposDocumentos : Items of the types of documents that are
	 *         displayed in the user interface
	 */
	public HashMap<String, Short> getItemsTiposDocumentos() {
		return itemsTiposDocumentos;
	}

	/**
	 * List of items that establishes the types of documents in the combo of the
	 * user interface
	 * 
	 * @param itemsTiposDocumentos
	 *            : Items of the types of documents that are displayed in the
	 *            user interface
	 */
	public void setItemsTiposDocumentos(
			HashMap<String, Short> itemsTiposDocumentos) {
		this.itemsTiposDocumentos = itemsTiposDocumentos;
	}

	/**
	 * Initializes search parameters and load the initial list of organizations
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return consultarOrganizaciones(): method consulting organizations of the
	 *         system and returns to the template management.
	 */
	public String inicializarBusqueda() {
		this.filtroBusqueda = "";
		return this.consultarOrganizaciones();
	}

	/**
	 * Allows to consult existing organizations in the database according to
	 * user is asked: current or not current
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @return gesOrganizacion: redirects to the Manage organization
	 */
	public String consultarOrganizaciones() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String mensajeBusqueda = "";
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			String cadena = bundle.getString("label_razon_social") + " / "
					+ bundle.getString("label_identification");
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadOrganizaciones = organizacionDao
					.cantidadOrganizaciones(condicionVigencia,
							this.filtroBusqueda);
			if (cantidadOrganizaciones != null) {
				pagination.paginar(cantidadOrganizaciones);
			}
			organizaciones = organizacionDao.consultarOrganizaciones(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.filtroBusqueda);

			if ((this.organizaciones == null || this.organizaciones.size() <= 0)
					&& (this.filtroBusqueda != null && !""
							.equals(this.filtroBusqueda))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								cadena + ": " + '"' + this.filtroBusqueda + '"');
			} else if (this.organizaciones == null
					|| this.organizaciones.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.filtroBusqueda != null
					&& !"".equals(this.filtroBusqueda)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrg.getString("organizacion_label_s"),
								cadena + ": " + '"' + this.filtroBusqueda + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesOrganizacion";
	}

	/**
	 * Method that allow load the user interface for editing or registration of
	 * an organization with its affiliated persons.
	 * 
	 * @param organizacion
	 *            : object of the organization that is loaded into the template
	 *            if editing.
	 * @return regOrganizacion: rule navigation that redirects to register the
	 *         organization, which is loaded into editing or empty to add a new
	 *         organization.
	 */
	public String registrarOrganizacion(Organizacion organizacion) {
		fileUploadBean = new FileUploadBean();
		this.personas = new ArrayList<Persona>();
		this.nombreBuscar = "";
		try {
			cargarCombosTipoCargoTipoDoc();
			if (organizacion != null) {
				organizacion = organizacionDao
						.consultarOrganizacionConTipoDocumento(organizacion
								.getId());
				this.organizacion = organizacion;
				fileUploadBean.setFileName(organizacion.getLogo());
				this.cargarFotoTemporal = false;
			} else {
				this.cargarFotoTemporal = true;
				this.organizacion = new Organizacion();
				this.organizacion.setTipoDocumento(new TipoDocumento());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regOrganizacion";
	}

	/**
	 * Method that allows charging information of the types of charge current in
	 * a combo to the user interface.
	 * 
	 * @throws Exception
	 */
	private void cargarCombosTipoCargoTipoDoc() throws Exception {
		itemsTiposDocumentos = new HashMap<String, Short>();
		List<TipoDocumento> tiposDocumentos = tipoDocumentoDao
				.consultarTiposDocumentoVigentes();
		if (tiposDocumentos != null) {
			for (TipoDocumento td : tiposDocumentos) {
				itemsTiposDocumentos.put(td.getNombre(), td.getId());
			}
		}
	}

	/**
	 * Method that allows you to load detailed organizational information in the
	 * user interface
	 * 
	 * @param organizacion
	 *            : identification of the organization you want to see the
	 *            information
	 */
	public void verDetallesOrganizacion(Organizacion organizacion) {
		this.organizacion = new Organizacion();
		try {
			organizacion = organizacionDao
					.consultarOrganizacionConTipoDocumento(organizacion.getId());
			this.organizacion = organizacion;

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that allows you to save or edit an organization, validating the
	 * social reason not repeated in the database.
	 * 
	 * @return regOrganizacion: page redirects to register the organization.
	 */
	public String guardarOrganizacion() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombreFotoBorrar = null;
		String key = "message_registro_modificar";
		boolean seCambioFoto = false;
		boolean esEdicion = false;
		try {
			userTransaction.begin();
			organizacion.setRazonSocial(WordUtils.capitalizeFully(organizacion
					.getRazonSocial()));
			organizacion.setNit(organizacion.getNit().toUpperCase());
			organizacion.setUserName(identity.getUserName());
			if (organizacion.getId() != 0) {
				esEdicion = true;
				if (organizacion.getLogo() != null
						&& !"".equals(organizacion.getLogo())
						&& !organizacion.getLogo().equals(
								fileUploadBean.getFileName())) {
					seCambioFoto = true;
					this.borrarArchivoReal(organizacion.getLogo());
				} else if (organizacion.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					seCambioFoto = true;
				}
				organizacion.setLogo(fileUploadBean.getFileName());
				if (organizacion.getLogo() != null && seCambioFoto) {
					nombreFotoBorrar = fileUploadBean.getFileName();
					subirImagenUbicacionReal();
				}
				organizacionDao.editarOrganizacion(organizacion);

			} else {
				key = "message_registro_guardar";
				if (fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName().trim())) {
					nombreFotoBorrar = fileUploadBean.getFileName();
					subirImagenUbicacionReal();
				}
				organizacion.setLogo(fileUploadBean.getFileName());
				organizacion.setFechaCreacion(new Date());
				organizacionDao.guardarOrganizacion(organizacion);
			}
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							organizacion.getRazonSocial()));
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
		} catch (Exception e) {
			if (!esEdicion && fileUploadBean.getFileName() != null
					&& !"".equals(fileUploadBean.getFileName())) {
				this.borrarArchivoReal(fileUploadBean.getFileName());
			}
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
			return "regOrganizacion";
		}
		return inicializarBusqueda();
	}

	/**
	 * Method that validate the trade name of the organization, so it is not
	 * repeated in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarRazonSocialXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String razonSocial = (String) value;
		String clientId = toValidate.getClientId(context);
		int idOrg = organizacion.getId();
		String razonSocialCapitalize = WordUtils.capitalizeFully(razonSocial);
		Organizacion organizacionTemp = new Organizacion();
		try {
			if (idOrg != 0) {
				organizacionTemp = organizacionDao.consultarExisteActualizar(
						razonSocialCapitalize, Constantes.RAZON_SOCIAL, idOrg);
			} else {
				organizacionTemp = organizacionDao.consultarExiste(
						razonSocialCapitalize, Constantes.RAZON_SOCIAL);
			}
			if (organizacionTemp != null) {
				if (organizacionTemp.getFechaFinVigencia() == null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(razonSocial, clientId,
					"locate.regex.letras.numeros.caracteres")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that validates the NIT of the organization, so it is not repeated
	 * in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNitXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nit = (String) value;
		String clientId = toValidate.getClientId(context);
		int idOrg = organizacion.getId();
		Organizacion organizacionTemp = new Organizacion();
		try {
			if (idOrg != 0) {
				organizacionTemp = organizacionDao.consultarExisteActualizar(
						nit.toUpperCase(), Constantes.NIT, idOrg);
			} else {
				organizacionTemp = organizacionDao.consultarExiste(
						nit.toUpperCase(), Constantes.NIT);
			}
			if (organizacionTemp != null) {
				if (organizacionTemp.getFechaFinVigencia() == null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(nit, clientId,
					"locate.regex.numeros.guion")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that changes the effect of organizations that are not associated
	 * with another entity in the database
	 * 
	 * @modify Luis.Ruiz
	 * 
	 * @param vigente
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * @return consultarOrganizaciones: page redirects to manage organizations
	 */
	public String vigenciaOrganizaciones(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		StringBuilder regVigentesUsados = new StringBuilder();
		StringBuilder regExito = new StringBuilder();
		String mensajeCambioVigencia = "";
		try {
			if (vigente) {
				mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";

				organizacionAModificarVigencia.setFechaFinVigencia(new Date());
				organizacionAModificarVigencia.setUserName(identity
						.getUserName());
				organizacionDao
						.editarOrganizacion(organizacionAModificarVigencia);
				regExito.append(organizacionAModificarVigencia.getRazonSocial()
						+ ", ");

			} else {
				mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
				organizacionAModificarVigencia.setFechaFinVigencia(null);
				organizacionAModificarVigencia.setUserName(identity
						.getUserName());
				organizacionDao
						.editarOrganizacion(organizacionAModificarVigencia);
				regExito.append(organizacionAModificarVigencia.getRazonSocial()
						+ ", ");
			}
			if (regVigentesUsados.length() > 0) {
				String message = bundle
						.getString("message_registro_vigencia_con_relaciones")
						+ ": "
						+ regVigentesUsados.substring(0,
								regVigentesUsados.length() - 2);
				ControladorContexto.mensajeError(message);
			}
			if (regExito.length() > 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString(mensajeCambioVigencia) + ": "
								+ regExito.substring(0, regExito.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarOrganizaciones();
	}

	/**
	 * Method that allows add the person is created in the modal to the list of
	 * people to be involved in organizing
	 */
	public void registrarPersona() {
		if (ControladorContexto.getMaxSeverity() == null) {
			this.personas.add(this.persona);
		}
	}

	/**
	 * Method that allows delete the file name.
	 * 
	 */
	public void borrarFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(organizacion.getLogo())
				&& this.cargarFotoTemporal) {
			borrarArchivo(fileUploadBean.getFileName());
		}
		fileUploadBean.setFileName(null);
	}

	/**
	 * Method that allows delete files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivo(String fileName) {
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Method that allows you to load the logo image of the organization.
	 * 
	 * @modify 02/02/2015 Jonathan.Arias
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAceptadas[] = Constantes.EXT_IMG.split(", ");
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		long tamanyoMaxArchivos = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAceptadas,
				ubicaciones, tamanyoMaxArchivos);
		String mensaje = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			mensaje = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"),
					tamanyoMaxArchivos, "MB");
			ControladorContexto.mensajeError("organizacionForm:uploadFile",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			mensaje = "error_carga_archivo";
		}
		if (!"".equals(mensaje)) {
			ControladorContexto.mensajeError("organizacionForm:uploadFile",
					bundle.getString(mensaje));
		}
		if (organizacion.getId() != 0) {
			cargarFotoTemporal = true;
		}
	}

	/**
	 * method that allows delete files from the actual location
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            :Name of the file to delete.
	 * 
	 */
	public void borrarArchivoReal(String fileName) {
		String ubicaciones[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getCarpetaArchivos(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getCarpetaArchivos() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Add photo image to the actual folder
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void subirImagenUbicacionReal() throws Exception {
		String origen = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivosTemporal();
		String destino1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivos();
		String destino2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getCarpetaArchivos();
		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, fileUploadBean.getFileName());
		File fileDestino1 = new File(destino1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destino2, fileUploadBean.getFileName());

		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Method that allows assign the organization to change its validity
	 * 
	 * @param organizacion
	 *            : organization you want to change the effect.
	 */
	public void asignarOrganizacionModificar(Organizacion organizacion) {
		if (organizacion instanceof Organizacion) {
			this.organizacionAModificarVigencia = organizacion;
		}
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless logo that are not filled out these fields
	 * 
	 * @author marisol.calderon
	 * 
	 */
	public void requeridosOk() {
		try {
			if (organizacion.getTipoDocumento() == null
					|| organizacion.getTipoDocumento().getId() == 0) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:cmbtipodocumento");
			}
			if (organizacion.getRazonSocial() == null
					|| "".equals(organizacion.getRazonSocial())) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:txtRazonSocial");
			}
			if (organizacion.getNit() == null
					|| "".equals(organizacion.getNit())) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:txtNit");
			}
			/* Validation Scripting in the name of the file to upload */
			EncodeFilter.validarXSS(fileUploadBean.getFileName(),
					"organizacionForm:uploadFile", null);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
