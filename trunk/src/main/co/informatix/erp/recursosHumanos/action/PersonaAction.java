package co.informatix.erp.recursosHumanos.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.informacionBase.dao.CivilStatusDao;
import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.dao.TipoDocumentoDao;
import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.action.PerfilUsuarioAction;
import co.informatix.erp.seguridad.dao.UsuarioDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.Usuario;

/**
 * This class is used to create, update, and query of a user in the system
 * information.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PersonaAction implements Serializable {

	@EJB
	private PersonaDao personaDao;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;
	@EJB
	private TipoDocumentoDao tipoDocumentoDao;
	@EJB
	private UsuarioDao usuarioDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private CivilStatusDao civilStatusDao;

	@Resource
	private UserTransaction userTransaction;
	@Inject
	private IdentityAction identity;

	private List<SelectItem> itemsPaises;
	private List<SelectItem> itemDepartamentos;
	private List<SelectItem> itemsMunicipios;
	private List<SelectItem> itemsTiposDocumentos;
	private List<SelectItem> itemsEstadosCivil;
	private List<SelectItem> itemDepartamentosRes;
	private List<SelectItem> itemsMunicipiosRes;
	private List<Persona> personas;
	private Persona persona;
	private Paginador pagination = new Paginador();
	private Date fechaActual = new Date();
	private String carpetaArchivos;
	private String carpetaArchivosTemporal;
	private String mensajeMiga;
	private String labelRichPanel;
	private String filtroBusqueda;
	private String vigencia = Constantes.SI;
	private boolean esEdicion;
	private boolean cargarFotoTemporal;
	private boolean personasSinUsuario = false;

	/**
	 * @return vigencia: allows gets the selected value 'yes' of existing and
	 *         'no' for not applicable
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : allows sets the selected value 'yes' of existing and 'no'
	 *            for not applicable
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return fechaActual: variable that gets the current system date.
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual
	 *            : variable that sets the current system date.
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * @return carpetaArchivos: Variable that gets the path to the folder where
	 *         the pictures are saved person.
	 */
	public String getCarpetaArchivos() {
		this.carpetaArchivos = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_PERSONAS;
		return carpetaArchivos;
	}

	/**
	 * @param carpetaArchivos
	 *            : Variable that gets the path to the folder where the pictures
	 *            are saved person.
	 */
	public void setCarpetaArchivos(String carpetaArchivos) {
		this.carpetaArchivos = carpetaArchivos;
	}

	/**
	 * @return carpeta Archivos: path of the temporary folder where photos of
	 *         people loaded
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
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
	 * @return fileUploadBean: gets the object variable to load files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : sets the object variable to load files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return pagination: pagination object that allows listing of people.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : pagination object that allows listing of people.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return persona: Object of the person for registration or implementation
	 *         edition.
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona
	 *            : Object of the person for registration or implementation
	 *            edition.
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/**
	 * @return itemsPaises: List of items from the countries that are loaded
	 *         into the combo in the user interface.
	 */
	public List<SelectItem> getItemsPaises() {
		return itemsPaises;
	}

	/**
	 * @return itemDepartamentos: List of items from the departments that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemDepartamentos() {
		return itemDepartamentos;
	}

	/**
	 * @return itemsMunicipios :List of items of the municipalities that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsMunicipios() {
		return itemsMunicipios;
	}

	/**
	 * @return itemsTiposDocumentos: List of items for document types that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsTiposDocumentos() {
		return itemsTiposDocumentos;
	}

	/**
	 * @return itemsEstadosCivil: marital status items that are loaded into the
	 *         combo in the user interface.
	 */
	public List<SelectItem> getItemsEstadosCivil() {
		return itemsEstadosCivil;
	}

	/**
	 * @return itemDepartamentosRes: Items departments residence of the person.s
	 */
	public List<SelectItem> getItemDepartamentosRes() {
		return itemDepartamentosRes;
	}

	/**
	 * @return itemsMunicipiosRes: items in the municipalities of residence of
	 *         the person.
	 */
	public List<SelectItem> getItemsMunicipiosRes() {
		return itemsMunicipiosRes;
	}

	/**
	 * @return personas: List of people that are uploaded to the management.
	 */
	public List<Persona> getPersonas() {
		return personas;
	}

	/**
	 * @param personas
	 *            :List of people that are uploaded to the management.
	 */
	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on whether you create or modify a record
	 * 
	 * @return mensajeMiga: message crumb of bread in the register or person
	 *         managing staff.
	 */
	public String getMensajeMiga() {
		return mensajeMiga;
	}

	/**
	 * Variable that sets the value of the message to display in the
	 * breadcrumbs, depending on whether you create or modify a record
	 * 
	 * @param mensajeMiga
	 *            : message crumb of bread in the register or person managing
	 *            staff.
	 */
	public void setMensajeMiga(String mensajeMiga) {
		this.mensajeMiga = mensajeMiga;
	}

	/**
	 * You gets a Boolean variable to see if the page is loaded or not editing
	 * 
	 * @return esEdicion: true if the load in editing, page false otherwise
	 */
	public boolean isEsEdicion() {
		return esEdicion;
	}

	/**
	 * sets a boolean variable to see if the page is loaded or not editing
	 * 
	 * @param esEdicion
	 *            : true if the load in editing, page false otherwise
	 */
	public void setEsEdicion(boolean esEdicion) {
		this.esEdicion = esEdicion;
	}

	/**
	 * @return labelRichPanel: label shown in rich editing panel depending on
	 *         whether or not and if it comes from third.
	 */
	public String getLabelRichPanel() {
		return labelRichPanel;
	}

	/**
	 * @param labelRichPanel
	 *            : label shown in rich editing panel depending on whether or
	 *            not and if it comes from third.
	 */
	public void setLabelRichPanel(String labelRichPanel) {
		this.labelRichPanel = labelRichPanel;
	}

	/**
	 * @return filtroBusqueda: Search filter records of the person.
	 */
	public String getFiltroBusqueda() {
		return filtroBusqueda;
	}

	/**
	 * @param filtroBusqueda
	 *            : Search filter records of the person.
	 */
	public void setFiltroBusqueda(String filtroBusqueda) {
		this.filtroBusqueda = filtroBusqueda;
	}

	/**
	 * @return personasSinUsuario: allows to validate that the consultation is
	 *         made for those people who do not have user.
	 */
	public boolean isPersonasSinUsuario() {
		return personasSinUsuario;
	}

	/**
	 * @param personasSinUsuario
	 *            : allows to validate that the consultation is made for those
	 *            people who do not have user.
	 */
	public void setPersonasSinUsuario(boolean personasSinUsuario) {
		this.personasSinUsuario = personasSinUsuario;
	}

	/**
	 * Allows to load interface to record or edit a person.
	 * 
	 * @modify Adonay.Mantilla 25/01/2013
	 * 
	 * @param persona
	 *            : Person edited in edit mode.
	 * @return regPersona: Navigation rule that directs the person form.
	 */
	public String registrarPersona(Persona persona) {
		ResourceBundle bundleRecHum = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		this.personasSinUsuario = false;
		try {
			fileUploadBean = new FileUploadBean();
			if (persona == null) {
				labelRichPanel = bundleRecHum.getString("persona_label_crear");
				mensajeMiga = "mensajeRecursosHumanos.persona_label_crear";
				this.esEdicion = false;
				this.persona = new Persona();
				this.cargarFotoTemporal = true;
			} else {
				this.esEdicion = true;
				this.persona = persona;
				labelRichPanel = bundleRecHum
						.getString("persona_label_modificar");
				mensajeMiga = "mensajeRecursosHumanos.persona_label_modificar";
				fileUploadBean.setFileName(persona.getFoto());
				this.cargarFotoTemporal = false;
			}
			cargarCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPersona";
	}

	/**
	 * This method allows you to load combos country, department, municipality
	 * and type of document
	 * 
	 * @author marisol.calderon
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void cargarCombos() throws Exception {
		itemsPaises = new ArrayList<SelectItem>();
		List<Pais> paises = paisDao.consultarPaisesVigentes();
		if (paises != null) {
			for (Pais pais : paises) {
				itemsPaises.add(new SelectItem(pais.getId(), pais.getNombre()));
			}
		}
		cargarComboDepartamento();
		cargarComboMunicipio();
		cargarComboDepartamentoRes();
		cargarComboMunicipioRes();

		itemsTiposDocumentos = new ArrayList<SelectItem>();
		List<TipoDocumento> tiposDocumentos = tipoDocumentoDao
				.consultarTiposDocumentoVigentes();
		if (tiposDocumentos != null) {
			for (TipoDocumento td : tiposDocumentos) {
				itemsTiposDocumentos.add(new SelectItem(td.getId(), td
						.getNombre()));
			}
		}
		itemsEstadosCivil = new ArrayList<SelectItem>();
		List<CivilStatus> civilStatusList = civilStatusDao.consultCivilStatus();
		if (civilStatusList != null) {
			for (CivilStatus civilStatus : civilStatusList) {
				itemsEstadosCivil.add(new SelectItem(civilStatus.getId(),
						civilStatus.getName()));
			}
		}
	}

	/**
	 * This method makes the query associated departments selected a particular
	 * country from interface, makes the insertion of the indicator and the
	 * department name itemDepartamentos list for display in the user interface.
	 * 
	 */
	public void cargarComboDepartamento() {
		itemDepartamentos = new ArrayList<SelectItem>();
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Pais pais = persona.getPaisNac();
			if (pais != null && pais.getId() > 0) {
				llenarDepartamentos(pais, itemDepartamentos);
				cargarComboMunicipio();
			} else {
				persona.setDepartamentoNac(new Departamento());
				persona.setMunicipioNac(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the query associated municipalities given department,
	 * the insertion flag and the name of the municipalities in the list of
	 * itemsMunicipios is made to be shown in the user interface
	 * 
	 */
	public void cargarComboMunicipio() {
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Departamento departamento = persona.getDepartamentoNac();
			if (departamento != null && departamento.getId() > 0
					&& this.itemDepartamentos.size() > 0) {
				llenarMunicipios(departamento, itemsMunicipios);
			} else {
				persona.setDepartamentoNac(new Departamento());
				persona.setMunicipioNac(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to fill in the items of municipalities.
	 * 
	 * @param departamento
	 *            : selected by the department which municipalities are
	 *            filtered.
	 * @param itemsMunicipios
	 *            : list of items of municipalities to fill displayed in the
	 *            user interface.
	 * @throws Exception
	 */
	private void llenarMunicipios(Departamento departamento,
			List<SelectItem> itemsMunicipios) throws Exception {
		int idDepartamento = departamento.getId();
		List<Municipio> municipios = municipioDao
				.consultarMunicipiosVigentes(idDepartamento);
		if (municipios != null) {
			for (Municipio m : municipios) {
				itemsMunicipios.add(new SelectItem(m.getId(), m.getNombre()));
			}
		}
	}

	/**
	 * This method makes the query departments residence of the person
	 * associated with certain selected country from the interface, makes the
	 * insertion of the indicator and the department name itemDepartamentos list
	 * for display in the user interface.
	 * 
	 */
	public void cargarComboDepartamentoRes() {
		itemDepartamentosRes = new ArrayList<SelectItem>();
		itemsMunicipiosRes = new ArrayList<SelectItem>();
		try {
			Pais pais = persona.getPaisRes();
			if (pais != null && pais.getId() > 0) {
				llenarDepartamentos(pais, itemDepartamentosRes);
				cargarComboMunicipioRes();
			} else {
				persona.setDepartamentoRes(new Departamento());
				persona.setMunicipioRes(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to fill in the items of the departments.
	 * 
	 * @param pais
	 *            : country selected the which departments are filtered.
	 * @param itemDepartamentos
	 *            : list of items of departments to fill displayed in the user
	 *            interface.
	 * @throws Exception
	 */
	private void llenarDepartamentos(Pais pais,
			List<SelectItem> itemDepartamentos) throws Exception {
		short idPais = pais.getId();
		List<Departamento> departamentos = departamentoDao
				.consultarDepartamentosPaisVigentes(idPais);
		if (departamentos != null) {
			for (Departamento d : departamentos) {
				itemDepartamentos.add(new SelectItem(d.getId(), d.getNombre()));
			}
		}
	}

	/**
	 * This method makes the query associated municipalities of residence of a
	 * particular department, the insertion flag and the name of the
	 * municipalities in the list of itemsMunicipios is made to be shown in the
	 * user interface
	 * 
	 */
	public void cargarComboMunicipioRes() {
		itemsMunicipiosRes = new ArrayList<SelectItem>();
		try {
			Departamento departamento = persona.getDepartamentoRes();
			if (departamento != null && departamento.getId() > 0
					&& this.itemDepartamentosRes.size() > 0) {
				llenarMunicipios(departamento, itemsMunicipiosRes);
			} else {
				persona.setDepartamentoRes(new Departamento());
				persona.setMunicipioRes(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows you to save or edit a person in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @return salida: Navigation rule that addresses the management of people
	 *         if no validation errors, otherwise it returns to form person.
	 */
	public String agregarEditarPersona() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		PerfilUsuarioAction perfilUsuarioAction = ControladorContexto
				.getContextBean(PerfilUsuarioAction.class);
		String salida = "regPersona";
		String mensajeInfo = "message_registro_modificar";
		String nombreFotoBorrar = null;
		String nombreMostrar = "";
		boolean seCambioFoto = false;
		try {
			userTransaction.begin();
			validarObjetos();
			persona.setNombres(WordUtils.capitalizeFully(persona.getNombres()));
			persona.setApellidos(WordUtils.capitalizeFully(persona
					.getApellidos()));
			persona.setUserName(identity.getUserName());
			if (esEdicion) {
				if (persona.getFoto() != null
						&& !"".equals(persona.getFoto())
						&& !persona.getFoto().equals(
								fileUploadBean.getFileName())) {
					seCambioFoto = true;
					this.borrarArchivoReal(persona.getFoto());
				} else if (persona.getFoto() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					seCambioFoto = true;
				}
				persona.setFoto(fileUploadBean.getFileName());
				if (persona.getFoto() != null && seCambioFoto) {
					nombreFotoBorrar = fileUploadBean.getFileName();
					subirImagenUbicacionReal();
				}
				personaDao.editarPersona(persona);
				Usuario usuario = usuarioDao.searchPersonUser(persona
						.getId());
				if (usuario != null) {
					usuario.setNombre(persona.getNombres());
					usuario.setApellido(persona.getApellidos());
					usuario.setCorreoElectronico(persona.getCorreo());
					usuario.setUserName(identity.getUserName());
					usuarioDao.editUser(usuario);
				}
			} else {
				mensajeInfo = "message_registro_guardar";
				if (fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName().trim())) {
					nombreFotoBorrar = fileUploadBean.getFileName();
					subirImagenUbicacionReal();
				}
				persona.setFoto(fileUploadBean.getFileName());
				this.persona.setFechaCreacion(new Date());
				personaDao.crearPersona(persona);
			}
			nombreMostrar = persona.getDocumento();
			userTransaction.commit();
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
			if (perfilUsuarioAction.isGuardarPersonaDesdePerfil()) {
				nombreMostrar = persona.getNombres() + " "
						+ persona.getApellidos();
				salida = perfilUsuarioAction
						.cargarPerfilDeUsuario(Constantes.N_TAB);
			} else {
				salida = inicializarConsulta();
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeInfo), nombreMostrar));
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
		}
		return salida;
	}

	/**
	 * This method starts or ends the validity of a particular user, it is
	 * called the method editarPersona to save the update to the database and
	 * then query the current user is done with the method consultarPersonas
	 * 
	 * @param vigente
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * @return consultarPersonas. Page redirect to people query
	 */
	public String cambioVigenciaPersonas(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
		try {
			validarObjetos();
			if (vigente) {
				mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
				this.persona.setFechaFinVigencia(new Date());
				this.persona.setUserName(identity.getUserName());
				personaDao.editarPersona(this.persona);

			} else {
				this.persona.setFechaFinVigencia(null);
				this.persona.setUserName(identity.getUserName());
				personaDao.editarPersona(this.persona);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeCambioVigencia) + ": {0}",
					this.persona.getNombres() + " "
							+ this.persona.getApellidos()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarPersonas();
	}

	/**
	 * Method to validate objects before save person in the database, to which
	 * an instance of the class was created but there is no record.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 */
	private void validarObjetos() {
		if (this.persona != null) {
			CivilStatus civilStatus = this.persona.getCivilStatus();
			if (civilStatus != null && civilStatus.getId() == 0) {
				this.persona.setCivilStatus(null);
			}
		}
	}

	/**
	 * This method makes consulting registered user of the information system,
	 * taking into account the type of validity that is required by the
	 * interface determined by the tipoVigenteSelect this variable.
	 * 
	 * @modify 04/06/2013 Luz.Jaimes
	 * @modify 08/03/2016 Mabell.Boada
	 * 
	 * @return retorno: Redirects output page listarPersonas
	 */
	public String consultarPersonas() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String inModal = ControladorContexto.getParam("param2");
		this.personas = new ArrayList<Persona>();
		List<Persona> personasTemp = new ArrayList<Persona>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		boolean fromModal = (inModal != null && Constantes.SI.equals(inModal)) ? true
				: false;
		String retorno = fromModal ? "" : "gesPersonas";
		try {
			if (!fromModal)
				personasSinUsuario = false;
			busquedaAvanzada(consult, parameters, bundleRecursosHumanos,
					unionMessageSearch);
			Long cantidadRegistros = this.personaDao.cantidadPersonas(consult,
					parameters);
			if (cantidadRegistros != null) {
				if (fromModal)
					pagination.paginarRangoDefinido(cantidadRegistros, 5);
				else
					this.pagination.paginar(cantidadRegistros);
			}
			personasTemp = personaDao.consultarPersonas(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			cargarInformacionDetallePersona(personasTemp);
			if ((this.personas == null || this.personas.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (this.personas == null || this.personas.size() <= 0) {
				messageSearch = bundle
						.getString("message_no_existen_registros");
				if (!fromModal) {
					ControladorContexto.mensajeInformacion(null, messageSearch);
					messageSearch = "";
				}
			} else if (!"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("persona_label_s"),
								unionMessageSearch);
			}
			if (fromModal) {
				validations.setMensajeBusquedaPopUp(messageSearch);
			} else {
				validations.setMensajeBusqueda(messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
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
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		consult.append("WHERE p.fechaFinVigencia ");
		if (Constantes.NOT.equals(vigencia)) {
			consult.append(Constantes.IS_NOT_NULL + " ");
		} else {
			consult.append(Constantes.IS_NULL + " ");
		}
		if (this.filtroBusqueda != null && !"".equals(this.filtroBusqueda)) {
			consult.append("AND (UPPER(p.nombres) LIKE UPPER(:parametro) ");
			consult.append("OR UPPER(p.apellidos) LIKE UPPER(:parametro) ");
			consult.append("OR UPPER(p.documento) LIKE UPPER(:parametro)) ");
			SelectItem item = new SelectItem("%" + this.filtroBusqueda + "%",
					"parametro");
			parameters.add(item);
			unionMessagesSearch
					.append(bundle
							.getString("persona_message_consulta_nombre_apellido_identificacion")
							+ ": " + '"' + this.filtroBusqueda + '"');
		}
		if (personasSinUsuario) {
			consult.append("AND p.usuario IS NULL ");
		}
	}

	/**
	 * Method to load the detail information of the person.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param personasTemp
	 *            : initial list of people
	 * @throws Exception
	 */
	public void cargarInformacionDetallePersona(List<Persona> personasTemp)
			throws Exception {
		if (personasTemp != null) {
			for (Persona persona : personasTemp) {
				cargarDetallesUnaPersona(persona);
				this.personas.add(persona);
			}
		}
	}

	/**
	 * Allows load the details of the person sent as a parameter.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @param persona
	 *            : person in charge details
	 * @throws Exception
	 */
	public void cargarDetallesUnaPersona(Persona persona) throws Exception {
		int idPersona = persona.getId();
		TipoDocumento tipoDocumento = (TipoDocumento) this.personaDao
				.consultarObjetoPersona("tipoDocumento", idPersona);
		Pais paisNac = (Pais) this.personaDao.consultarObjetoPersona("paisNac",
				idPersona);
		Departamento departamentoNac = (Departamento) this.personaDao
				.consultarObjetoPersona("departamentoNac", idPersona);
		Municipio municipioNac = (Municipio) this.personaDao
				.consultarObjetoPersona("municipioNac", idPersona);
		Pais paisRes = (Pais) this.personaDao.consultarObjetoPersona("paisRes",
				idPersona);
		Departamento departamentoRes = (Departamento) this.personaDao
				.consultarObjetoPersona("departamentoRes", idPersona);
		Municipio municipioRes = (Municipio) this.personaDao
				.consultarObjetoPersona("municipioRes", idPersona);
		CivilStatus civilStatus = (CivilStatus) this.personaDao
				.consultarObjetoPersona("civilStatus", idPersona);

		persona.setTipoDocumento(tipoDocumento);
		persona.setPaisNac(paisNac);
		persona.setDepartamentoNac(departamentoNac);
		persona.setMunicipioNac(municipioNac);
		persona.setPaisRes(paisRes);
		persona.setDepartamentoRes(departamentoRes);
		persona.setMunicipioRes(municipioRes);
		persona.setCivilStatus(civilStatus != null ? civilStatus
				: new CivilStatus());
	}

	/**
	 * Allows erase the default filename.
	 * 
	 */
	public void borrarFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(persona.getFoto())
				&& this.cargarFotoTemporal) {
			borrarArchivo(fileUploadBean.getFileName());
		}
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
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
	 * Delete files from the actual location
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
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
	 * Allows to load the photo image of the person
	 * 
	 * @modify 03/02/2015 Marcela.Chaparro
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAceptadas[] = Constantes.EXT_IMG.split(", ");
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		String resultUpload = fileUploadBean.upload(extAceptadas, ubicaciones);
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			ControladorContexto.mensajeError("frmRegistrarPersona:uploadFile",
					bundle.getString("error_ext_invalida"));
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			ControladorContexto.mensajeError("frmRegistrarPersona:uploadFile",
					bundle.getString("error_carga_archivo"));
		}
		if (esEdicion) {
			cargarFotoTemporal = true;
		}
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless picture that are not filled out these fields
	 * 
	 * @author marisol.calderon
	 * @modify Gabriel.Moreno 08/Marzo/2012
	 * 
	 * @param nombreForm
	 *            :template id where is valid people
	 * @return salida: boolean to true if the required fields are filled out or
	 *         false otherwise
	 */
	public boolean requeridosOk(String nombreForm) {
		boolean salida = true;
		if (persona.getTipoDocumento() == null
				|| persona.getTipoDocumento().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":cmbTipoDocumento");
			salida = false;
		}
		if (persona.getDocumento() == null || "".equals(persona.getDocumento())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":txtId");
			salida = false;
		}
		if (persona.getNombres() == null || "".equals(persona.getNombres())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":txtNombre");
			salida = false;
		}
		if (persona.getGenero() == null || "".equals(persona.getGenero())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":radGenero");
			salida = false;
		}
		if (persona.getPaisNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":comboPais");
			salida = false;
		}
		if (persona.getDepartamentoNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboDepartamento");
			salida = false;
		}
		if (persona.getMunicipioNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboMunicipio");
			salida = false;
		}
		if (persona.getTelefono() == null || "".equals(persona.getTelefono())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":txtTelefono");
			salida = false;
		}
		if (persona.getPaisRes().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":comboPaisRes");
			salida = false;
		}
		if (persona.getDepartamentoRes().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboDepartamentoRes");
			salida = false;
		}
		if (persona.getMunicipioRes().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboMunicipioRes");
			salida = false;
		}
		return salida;
	}

	/**
	 * Allows to validate the document of the person, so it is not repeated in
	 * the database and validates against XSS.
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
	public void validarDocumentoXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String documento = (String) value;
		String clientId = toValidate.getClientId(context);
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbTipoDocumento");
		short idTipoDocumento = (Short) findComponent.getValue();
		try {
			Persona personaBD = personaDao.validarPersonaXDocumento(
					documento.toUpperCase(), idTipoDocumento);
			if (personaBD != null) {
				if (personaBD.getFechaFinVigencia() == null) {
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
			if (!EncodeFilter.validarXSS(documento, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method is used to initialize the consultation of the people
	 * registered in the information system.
	 * 
	 * @return consultarPersonas: method that queries the information of the
	 *         people and returns to the template management.
	 */
	public String inicializarConsulta() {
		this.filtroBusqueda = "";
		return consultarPersonas();
	}

}
