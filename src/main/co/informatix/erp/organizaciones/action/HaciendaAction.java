package co.informatix.erp.organizaciones.action;

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

import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.dao.UnidadMedidaDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.UnidadMedida;
import co.informatix.erp.organizaciones.dao.EmpresaDao;
import co.informatix.erp.organizaciones.dao.HaciendaDao;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Hacienda;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * 
 * This class allows business logic to manage the records of estates
 * 
 * The logic is to consult, edit and add a farm
 * 
 * @author Oscar.Amaya
 * @modify 09/02/2012 marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HaciendaAction implements Serializable {

	private Paginador pagination = new Paginador();
	private Hacienda hacienda;

	private List<Hacienda> listaHaciendas;
	private List<SelectItem> itemsEmpresas;
	private List<SelectItem> itemsUnidadesMedida;
	private List<SelectItem> itemsPaises;
	private List<SelectItem> itemsDepartamentos;
	private List<SelectItem> itemsMunicipios;

	private String carpetaArchivosReal;
	private String carpetaArchivosTemporal;
	private String nombreEmpresaBuscar;
	private String fileNameFoto;
	private String fileNameMapa;

	private boolean cargarFotoTemporal;
	private boolean cargarMapa;

	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private HaciendaDao haciendaDao;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;
	@EJB
	private EmpresaDao empresaDao;
	@EJB
	private UnidadMedidaDao unidadMedidaDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	/**
	 * @return listaHaciendas: list of farms that are loaded into the user
	 *         interface.
	 */
	public List<Hacienda> getListaHaciendas() {
		return listaHaciendas;
	}

	/**
	 * @param listaHaciendas
	 *            : list of farms that are loaded into the user interface.
	 */
	public void setListaHaciendas(List<Hacienda> listaHaciendas) {
		this.listaHaciendas = listaHaciendas;
	}

	/**
	 * @return hacienda: object of the farm to which the implementation of
	 *         registration or editing is done.
	 */
	public Hacienda getHacienda() {
		return hacienda;
	}

	/**
	 * @param hacienda
	 *            : object of the farm to which the implementation of
	 *            registration or editing is done.
	 */
	public void setHacienda(Hacienda hacienda) {
		this.hacienda = hacienda;
	}

	/**
	 * @return itemsEmpresas: Items of companies that are displayed in a combo
	 *         in the user interface
	 */
	public List<SelectItem> getItemsEmpresas() {
		return itemsEmpresas;
	}

	/**
	 * @return itemsPaises: Items of the countries that are displayed in a combo
	 *         in the user interface
	 */
	public List<SelectItem> getItemsPaises() {
		return itemsPaises;
	}

	/**
	 * @param itemsPaises
	 *            : Items of the countries that are displayed in a combo in the
	 *            user interface
	 */
	public void setItemsPaises(List<SelectItem> itemsPaises) {
		this.itemsPaises = itemsPaises;
	}

	/**
	 * @return carpetaArchivosReal: the actual folder path where the photos are
	 *         loaded farm
	 */
	public String getCarpetaArchivosReal() {
		this.carpetaArchivosReal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_FOTOS_FINCAS;
		return carpetaArchivosReal;
	}

	/**
	 * @return carpetaArchivosReal: the actual folder path where the photos are
	 *         loaded farm
	 */
	public String getCarpetaArchivosRealMapas() {
		String folderHacienda = carpetaMapaHacienda();
		this.carpetaArchivosReal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_MAPAS_FINCAS + "/"
				+ folderHacienda;
		return carpetaArchivosReal;
	}

	/**
	 * @return carpetaArchivosTemporal: path of the temporary folder where the
	 *         photos are loaded farm
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return carpetaArchivosTemporal;
	}

	/**
	 * @return itemsUnidadesMedida: Items of the units of measurement are
	 *         displayed in a combo in the user interface
	 */
	public List<SelectItem> getItemsUnidadesMedida() {
		return itemsUnidadesMedida;
	}

	/**
	 * @return itemsDepartamentos: Items of the departments that are displayed
	 *         in a combo in the user interface
	 */
	public List<SelectItem> getItemsDepartamentos() {
		return itemsDepartamentos;
	}

	/**
	 * @param itemsDepartamentos
	 *            : Items of the departments that are displayed in a combo in
	 *            the user interface
	 */
	public void setItemsDepartamentos(List<SelectItem> itemsDepartamentos) {
		this.itemsDepartamentos = itemsDepartamentos;
	}

	/**
	 * @return itemsMunicipios: Items of the municipalities that are displayed
	 *         in a combo in the user interface
	 */
	public List<SelectItem> getItemsMunicipios() {
		return itemsMunicipios;
	}

	/**
	 * @param itemsMunicipios
	 *            : Items of the municipalities that are displayed in a combo in
	 *            the user interface
	 */
	public void setItemsMunicipios(List<SelectItem> itemsMunicipios) {
		this.itemsMunicipios = itemsMunicipios;
	}

	/**
	 * 
	 * @return pagination: management paged list estates in sight
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            : management paged list estates in sights
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return fileUploadBean: object that allows the upload of photos
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : object that allows the upload of photos
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return nombreEmpresaBuscar: gets the name of the company you want to
	 *         search for filtering the list of estates.
	 */
	public String getNombreEmpresaBuscar() {
		return nombreEmpresaBuscar;
	}

	/**
	 * @param nombreEmpresaBuscar
	 *            : sets the name of the company you want to search for
	 *            filtering the list of estates.
	 */
	public void setNombreEmpresaBuscar(String nombreEmpresaBuscar) {
		this.nombreEmpresaBuscar = nombreEmpresaBuscar;
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
	 * @return cargarMapa: variable that allows to know true if the map is
	 *         loaded or when loaded false picture of the estate.
	 */
	public boolean isCargarMapa() {
		return cargarMapa;
	}

	/**
	 * @param cargarMapa
	 *            : variable that allows to know true if the map is loaded or
	 *            when loaded false picture of the estate.
	 */
	public void setCargarMapa(boolean cargarMapa) {
		this.cargarMapa = cargarMapa;
	}

	/**
	 * @return fileNameFoto: variable that allow to store the filename of the
	 *         picture of the property.
	 */
	public String getFileNameFoto() {
		return fileNameFoto;
	}

	/**
	 * @param fileNameFoto
	 *            : variable allow that to store the filename of the picture of
	 *            the property.
	 */
	public void setFileNameFoto(String fileNameFoto) {
		this.fileNameFoto = fileNameFoto;
	}

	/**
	 * @return fileNameMapa: allow variable to store the filename of the map of
	 *         the estate.
	 */
	public String getFileNameMapa() {
		return fileNameMapa;
	}

	/**
	 * @param fileNameMapa
	 *            : allow variable to store the filename of the map of the
	 *            estate.
	 */
	public void setFileNameMapa(String fileNameMapa) {
		this.fileNameMapa = fileNameMapa;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultarhaciendas: method to query the properties and load the
	 *         template with the information found.
	 * 
	 */
	public String inicializarBusqueda() {
		this.hacienda = new Hacienda();
		this.nombreEmpresaBuscar = "";
		return consultarHaciendas();
	}

	/**
	 * Method to load the registration form a ranch in issue or to add a new
	 * farm
	 * 
	 * @param hacienda
	 *            : farm to be edited
	 * @return reghacienda: redirected to the registration form a farm
	 */
	public String agregarEditarHacienda(Hacienda hacienda) {
		this.cargarMapa = false;
		try {
			fileUploadBean = new FileUploadBean();
			cargarCombos();
			if (hacienda != null) {
				cargarDetallesHacienda(hacienda);
				if (this.hacienda.getUnidadMedida() == null) {
					this.hacienda.setUnidadMedida(new UnidadMedida());
				}
				cargarDepartamentos();
				if (this.hacienda.getDepartamento() == null) {
					this.hacienda.setDepartamento(new Departamento());
				}
				cargarMunicipios();
				if (this.hacienda.getMunicipio() == null) {
					this.hacienda.setMunicipio(new Municipio());
				}
				this.cargarFotoTemporal = false;
			} else {
				this.fileNameFoto = null;
				this.fileNameMapa = null;
				this.hacienda = new Hacienda();
				this.cargarFotoTemporal = true;
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regHacienda";
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless picture that are not filled out these fields
	 * 
	 * @modify 08/02/2012 marisol.calderon
	 * 
	 */
	public void requeridosOk() {
		try {
			if (hacienda.getEmpresa().getId() == 0) {
				ControladorContexto
						.mensajeRequeridos("haciendaForm:cmbEmpresa");
			}
			if (hacienda.getNombre() == null || "".equals(hacienda.getNombre())) {
				ControladorContexto.mensajeRequeridos("haciendaForm:nombre");
			}
			if (hacienda.getContacto().getNombres() == null
					|| "".equals(hacienda.getContacto().getNombres())) {
				ControladorContexto
						.mensajeRequeridos("haciendaForm:txtContactoReadonly");
			}
			if (hacienda.getUbicacion() == null
					|| "".equals(hacienda.getUbicacion())) {
				ControladorContexto
						.mensajeRequeridos("haciendaForm:txtUbicacion");
			}
			if (hacienda.getPais().getId() == 0) {
				ControladorContexto.mensajeRequeridos("haciendaForm:comboPais");
			}
			/* Validation Scripting in the name of the file to upload */
			EncodeFilter.validarXSS(fileNameFoto,
					"haciendaForm:uploadFileFoto", null);
			EncodeFilter.validarXSS(fileNameMapa,
					"haciendaForm:uploadFileMapa", null);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Consult the list of the properties that exist in the system also allows
	 * you to load the list by company name you type the user
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * 
	 * @return geshacienda: redirects manage estates
	 */
	public String consultarHaciendas() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String mensajeBusqueda = "";
		listaHaciendas = new ArrayList<Hacienda>();
		try {
			if (nombreEmpresaBuscar != null && !"".equals(nombreEmpresaBuscar)) {
				nombreEmpresaBuscar = nombreEmpresaBuscar.toUpperCase();
				Long cantidadHaciendasPorNombreEmpresa = haciendaDao
						.cantidadHaciendasPorNombreEmpresa(nombreEmpresaBuscar);
				if (cantidadHaciendasPorNombreEmpresa != null) {
					pagination.paginar(cantidadHaciendasPorNombreEmpresa);
				}
				listaHaciendas = haciendaDao.buscarHaciendasPorNombreEmpresa(
						nombreEmpresaBuscar, pagination.getInicio(),
						pagination.getRango());
			} else {
				pagination.paginar(haciendaDao.cantidadHaciendas());
				listaHaciendas = haciendaDao.consultarHaciendas(
						pagination.getInicio(), pagination.getRango());
			}
			/* builds the messages Search */
			if ((listaHaciendas == null || listaHaciendas.size() <= 0)
					&& (this.nombreEmpresaBuscar != null && !""
							.equals(this.nombreEmpresaBuscar))) {

				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("empresa_label")
										+ ": " + '"' + this.nombreEmpresaBuscar
										+ '"');
			} else if (listaHaciendas == null || listaHaciendas.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreEmpresaBuscar != null
					&& !"".equals(this.nombreEmpresaBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad
										.getString("hacienda_label_listado"),
								bundleSeguridad.getString("empresa_label")
										+ ": " + '"' + this.nombreEmpresaBuscar
										+ '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesHacienda";
	}

	/**
	 * Method that allows you to upload detailed information of the property.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param farm
	 *            :object to load details
	 */
	public void cargarDetallesHacienda(Hacienda farm) {
		try {
			if (farm != null) {
				farm = haciendaDao.consultarHacienda(farm.getId());
				fileNameFoto = farm.getFoto();
				fileNameMapa = farm.getMapa();
				this.hacienda = farm;
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the properties
	 * 
	 * @return consultarHaciendas: consulting method farms and redirected to the
	 *         template management
	 */
	public String guardarHacienda() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		boolean seCambioFoto = false;
		boolean seCambioMapa = false;
		boolean edicion = false;
		try {
			this.userTransaction.begin();
			hacienda.setNombre(WordUtils.capitalizeFully(hacienda.getNombre()));
			hacienda.setUserName(identity.getUserName());

			if (hacienda.getDepartamento().getId() == 0) {
				hacienda.setDepartamento(null);
			}
			if (hacienda.getMunicipio().getId() == 0) {
				hacienda.setMunicipio(null);
			}
			if (hacienda.getUnidadMedida().getId() == 0) {
				hacienda.setUnidadMedida(null);
			}
			seCambioFoto = validarCargaArchivo(this.fileNameFoto,
					hacienda.getFoto(), true);
			seCambioMapa = validarCargaArchivo(this.fileNameMapa,
					hacienda.getMapa(), false);
			hacienda.setFoto(this.fileNameFoto);
			if (hacienda.getId() != 0) {
				edicion = true;
				haciendaDao.editarHacienda(hacienda);
			} else {
				key = "message_registro_guardar";
				hacienda.setFechaCreacion(new Date());
				haciendaDao.guardarHacienda(hacienda);
			}
			this.userTransaction.commit();
			if (seCambioFoto && this.fileNameFoto != null) {
				subirImagenUbicacionReal(this.fileNameFoto, true);
				this.borrarArchivo(this.fileNameFoto);
			}
			if (seCambioMapa && this.fileNameMapa != null) {
				subirImagenUbicacionReal(this.fileNameMapa, false);
				this.borrarArchivo(this.fileNameMapa);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							hacienda.getNombre()));
		} catch (Exception e) {
			if (this.fileNameFoto != null && !"".equals(this.fileNameFoto)
					&& !edicion) {
				this.borrarArchivoReal(this.fileNameFoto, true);
			}
			if (this.fileNameMapa != null && !"".equals(this.fileNameMapa)
					&& !edicion) {
				this.borrarArchivoReal(this.fileNameMapa, false);
			}
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return consultarHaciendas();
	}

	/**
	 * Method to validate if I change the map file or photo of the estate.
	 * 
	 * @param archivoActual
	 *            : Current file farm on the issue.
	 * @param archivoAnterior
	 *            : farm previous file before editing.
	 * @param esFoto
	 *            : variable that shows whether uploading photos or maps is
	 *            validated.
	 * @return seCambioArchivo: boolean to true if the file or false otherwise
	 *         be change.
	 */
	private boolean validarCargaArchivo(String archivoActual,
			String archivoAnterior, boolean esFoto) {
		boolean seCambioArchivo = false;
		if (archivoActual != null && !"".equals(archivoActual)) {
			if (archivoAnterior != null && !"".equals(archivoAnterior)
					&& !archivoAnterior.equals(archivoActual)) {
				seCambioArchivo = true;
				borrarArchivoReal(archivoAnterior, esFoto);
			} else if (archivoAnterior == null) {
				seCambioArchivo = true;
			}
		} else if (archivoAnterior != null && !"".equals(archivoAnterior)) {
			seCambioArchivo = true;
			borrarArchivoReal(archivoAnterior, esFoto);
		}
		return seCambioArchivo;
	}

	/**
	 * Method used to load the combo of countries, companies and measurement
	 * unit
	 * 
	 * @throws Exception
	 * 
	 */
	private void cargarCombos() throws Exception {
		itemsPaises = new ArrayList<SelectItem>();
		List<Pais> listaPaisesVigentes = paisDao.consultarPaisesVigentes();
		if (listaPaisesVigentes != null) {
			for (Pais pais : listaPaisesVigentes) {
				itemsPaises.add(new SelectItem(pais.getId(), pais.getNombre()));
			}
		}
		itemsEmpresas = new ArrayList<SelectItem>();
		List<Empresa> listaEmpresasVigentes = empresaDao
				.consultarEmpresasVigentes();
		if (listaEmpresasVigentes != null) {
			for (Empresa empresa : listaEmpresasVigentes) {
				itemsEmpresas.add(new SelectItem(empresa.getId(), empresa
						.getNombre()));
			}
		}
		itemsUnidadesMedida = new ArrayList<SelectItem>();
		List<UnidadMedida> listaUnidadesMedidaVigentes = unidadMedidaDao
				.consultarUnidadesMedidaVigentes(Constantes.TIPO_UNIDAD_LONGITUD);
		if (listaUnidadesMedidaVigentes != null) {
			for (UnidadMedida unidadMedida : listaUnidadesMedidaVigentes) {
				itemsUnidadesMedida.add(new SelectItem(unidadMedida.getId(),
						unidadMedida.getNombre()));
			}
		}
		itemsDepartamentos = new ArrayList<SelectItem>();
		itemsMunicipios = new ArrayList<SelectItem>();
	}

	/**
	 * Method allows complete the list of departments according to the selected
	 * country.
	 */
	public void cargarDepartamentos() {
		itemsDepartamentos = new ArrayList<SelectItem>();
		try {
			List<Departamento> listaDepartamentosPaisVigentes = departamentoDao
					.consultarDepartamentosPaisVigentes(hacienda.getPais()
							.getId());
			if (listaDepartamentosPaisVigentes != null) {
				for (Departamento departamento : listaDepartamentosPaisVigentes) {
					itemsDepartamentos.add(new SelectItem(departamento.getId(),
							departamento.getNombre()));
				}
			}
			cargarMunicipios();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allow fill the list of municipalities according to the selected
	 * department.
	 */
	public void cargarMunicipios() {
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Departamento departamento = hacienda.getDepartamento();
			if (departamento != null) {
				List<Municipio> listaMunicipiosVigentes = municipioDao
						.consultarMunicipiosVigentes(departamento.getId());
				if (listaMunicipiosVigentes != null) {
					for (Municipio municipio : listaMunicipiosVigentes) {
						itemsMunicipios.add(new SelectItem(municipio.getId(),
								municipio.getNombre()));
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows you to load the image of a farm to the originally temporary
	 * folder.
	 * 
	 * @modify 02/02/2015 Marcela.Chaparro
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAceptadas[] = Constantes.EXT_IMG.split(", ");
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		String categoria = (String) e.getComponent().getAttributes()
				.get("param");
		boolean cargarM = Boolean.parseBoolean(categoria);
		cargarMapa = cargarM;
		String idUpload = cargarMapa ? "uploadFileMapa" : "uploadFileFoto";
		String resultUpload = Constantes.UPLOAD_NULL;
		fileUploadBean.setUploadedFile(e.getFile());
		if (cargarMapa) {
			resultUpload = fileUploadBean.uploadValTamanyo(extAceptadas,
					ubicaciones, Constantes.TAMANYO_MAPA_FINCA);
		} else {
			resultUpload = fileUploadBean.upload(extAceptadas, ubicaciones);
		}
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			ControladorContexto.mensajeError("haciendaForm:" + idUpload,
					bundle.getString("error_ext_invalida"));
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"),
					Constantes.TAMANYO_MAPA_FINCA, "MB");
			ControladorContexto
					.mensajeError("haciendaForm:" + idUpload, format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			ControladorContexto.mensajeError("haciendaForm:" + idUpload,
					bundle.getString("error_carga_archivo"));
		} else {
			if (!cargarMapa) {
				this.fileNameFoto = fileUploadBean.getFileName();
			} else {
				this.fileNameMapa = fileUploadBean.getFileName();
			}
		}
		if (hacienda.getId() != 0) {
			cargarFotoTemporal = true;
		}
	}

	/**
	 * Method that allows you to upload the image file to the actual folder.
	 * 
	 * @param nombreArchivo
	 *            : name of the file you want to upload.
	 * @param esFoto
	 *            : variable that lets you know if you load a photo or map in
	 *            the system.
	 * 
	 * @throws Exception
	 */
	private void subirImagenUbicacionReal(String nombreArchivo, boolean esFoto)
			throws Exception {
		String carpetaReal = esFoto ? this.getCarpetaArchivosReal() : this
				.getCarpetaArchivosRealMapas();
		String origen = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivosTemporal();
		String destino1 = Constantes.RUTA_UPLOADFILE_GLASFISH + carpetaReal;
		String destino2 = Constantes.RUTA_UPLOADFILE_WORKSPACE + carpetaReal;
		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, nombreArchivo);
		File fileDestino1 = new File(destino1, nombreArchivo);
		File fileDestino2 = new File(destino2, nombreArchivo);
		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Allows to delete the file name.
	 * 
	 */
	public void borrarFilename(boolean esFoto) {
		String nombreArchivo = esFoto ? fileNameFoto : fileNameMapa;
		if (esFoto) {
			if (nombreArchivo != null && !"".equals(nombreArchivo)
					&& !nombreArchivo.equals(hacienda.getFoto())
					&& this.cargarFotoTemporal) {
				borrarArchivo(nombreArchivo);
			}
			fileNameFoto = null;
		} else {
			if (nombreArchivo != null && !"".equals(nombreArchivo)
					&& !nombreArchivo.equals(hacienda.getMapa())
					&& this.cargarFotoTemporal) {
				borrarArchivo(nombreArchivo);
			}
			fileNameMapa = null;
		}
	}

	/**
	 * Allows to delete the file.
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
	 * Allows to delete files from the actual location
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * @param esFoto
	 *            : variable that shows whether a photo or map is removed.
	 * 
	 */
	private void borrarArchivoReal(String fileName, boolean esFoto) {
		if (esFoto) {
			String carpetaReal = esFoto ? this.getCarpetaArchivosReal() : this
					.getCarpetaArchivosRealMapas();
			String ubicaciones[] = {
					Constantes.RUTA_UPLOADFILE_GLASFISH + carpetaReal,
					Constantes.RUTA_UPLOADFILE_WORKSPACE + carpetaReal };
			fileUploadBean.delete(ubicaciones, fileName);
		}
	}

	/**
	 * Method to validate the creation of more than a farm with the same number
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
	public void validarNumeroHacienda(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		List<Hacienda> haciendas = new ArrayList<Hacienda>();
		String idCampoText = toValidate.getClientId(context);
		String codigoHacienda = (String) value;
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbEmpresa");
		Integer idEmpresa = (Integer) findComponent.getValue();
		String mensaje = "hacienda_message_numero_asignado";
		try {
			if (idEmpresa != null && codigoHacienda != null
					&& !"".equals(codigoHacienda)) {
				if (hacienda.getId() != 0) {
					haciendas = haciendaDao
							.buscarHaciendasPorNumeroYEmpresaEdicion(
									codigoHacienda, hacienda.getId(), idEmpresa);
				} else {
					haciendas = haciendaDao.buscarHaciendasPorNumeroYEmpresa(
							codigoHacienda, idEmpresa);
				}
				if (haciendas != null && haciendas.size() > 0) {
					context.addMessage(idCampoText,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundleOrg.getString(mensaje), null));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to validate that the name of the estate is not repeated for the
	 * company
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreHaciendaXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String idCampoText = toValidate.getClientId(context);
		String nombreHacienda = (String) value;
		String mensaje = "hacienda_message_nombre_existe";
		String nombreCapitalize = WordUtils.capitalizeFully(nombreHacienda);
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbEmpresa");
		Integer idEmpresa = (Integer) findComponent.getValue();
		boolean result = false;
		try {
			if (idEmpresa != null) {
				if (hacienda.getId() != 0) {
					result = haciendaDao.nombreExisteActualizar(
							nombreCapitalize, hacienda.getId(), idEmpresa);
				} else {
					result = haciendaDao.nombreExiste(nombreCapitalize,
							idEmpresa);
				}
				if (result) {
					context.addMessage(idCampoText,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundleOrg.getString(mensaje), null));
				}
			}
			if (!EncodeFilter.validarXSS(nombreHacienda, idCampoText, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to build the name of the folder where the maps of the estate will,
	 * example: 1_2_elpomarroso where 1 is the id of the company, 2 id of the
	 * property and elpomarroso: the name of the estate without spaces or
	 * special characters .
	 * 
	 * @return name of the folder where the maps are stored on the farm.
	 */
	private String carpetaMapaHacienda() {
		String folderHacienda = "";
		if (this.hacienda != null && this.hacienda.getNombre() != null) {
			int idEmpresa = this.hacienda.getEmpresa().getId();
			int idHacienda = this.hacienda.getId();
			String nombreHacienda = ValidacionesAction
					.quitarCaracteresEspeciales(this.hacienda.getNombre()
							.trim());
			nombreHacienda = nombreHacienda.replaceAll(" ", "").toLowerCase();
			folderHacienda = idEmpresa + "_" + idHacienda + "_"
					+ nombreHacienda;

		}
		return folderHacienda;
	}

}