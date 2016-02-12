package co.informatix.erp.lifeCycle.action;

import java.io.File;
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

import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the properties that may be in the BD.
 * 
 * The logic is to consult, edit or add farm.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FarmAction implements Serializable {
	@EJB
	private FarmDao farmDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;

	private List<Farm> listaFarms;

	private Farm farm;
	private Paginador paginador = new Paginador();

	private String nombreBuscar;
	private String carpetaArchivos;
	private String carpetaArchivosTemporal;
	private String nombreFotoLogo;
	private boolean cargarFotoTemporal;

	private List<SelectItem> itemsPaises;
	private List<SelectItem> itemDepartamentos;
	private List<SelectItem> itemsMunicipios;

	/**
	 * @return List<Farm>: list of farms that are displayed in the user
	 *         interface
	 */
	public List<Farm> getListaFarms() {
		return listaFarms;
	}

	/**
	 * @param listaFarms
	 *            : list of farms that are displayed in the user interface
	 */
	public void setListaFarms(List<Farm> listaFarms) {
		this.listaFarms = listaFarms;
	}

	/**
	 * Gets data of a farm
	 * 
	 * @return Farm: object containing the data of a farm
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * Sets data of a farm
	 * 
	 * @param farm
	 *            : object containing the data of a farm
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return Paginador: Management paged list estates.
	 * 
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list estates.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name of the farm to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name of the farm to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return itemsPaises: Items of the countries that are displayed in a combo
	 *         in the user interface
	 */
	public List<SelectItem> getItemsPaises() {
		return itemsPaises;
	}

	/**
	 * @return itemDepartamentos: Items of the countries that are displayed in a
	 *         combo in the user interface
	 */
	public List<SelectItem> getItemDepartamentos() {
		return itemDepartamentos;
	}

	/**
	 * @return itemsMunicipios: Items of the municipalities that are displayed
	 *         in a combo in the user interface
	 */
	public List<SelectItem> getItemsMunicipios() {
		return itemsMunicipios;
	}

	/**
	 * @return nombreFotoLogo: Items of the municipalities that are displayed in
	 *         a combo in the user interface
	 */
	public String getNombreFotoLogo() {
		return nombreFotoLogo;
	}

	/**
	 * @param nombreFotoLogo
	 *            : Items of the municipalities that are displayed in a combo in
	 *            the user interface
	 */
	public void setNombreFotoLogo(String nombreFotoLogo) {
		this.nombreFotoLogo = nombreFotoLogo;
	}

	/**
	 * @return carpetaArchivos: route real folder where the pictures of the
	 *         logos of the farms are loaded
	 */
	public String getCarpetaArchivos() {
		this.carpetaArchivos = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_LOGOS_EMPRESAS;
		return carpetaArchivos;
	}

	/**
	 * @return carpetaArchivosTemporal: path of the temporary folder where the
	 *         logos of the farms are loaded
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return carpetaArchivosTemporal;
	}

	/**
	 * 
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
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the farms
	 * 
	 * @return consultarFarms: estates query method returns to the template
	 *         management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarFarms();
	}

	/**
	 * Consult the list of estates
	 * 
	 * @return "gesFarm": redirects to the template to manage the estates
	 */
	public String consultarFarms() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaFarms = new ArrayList<Farm>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = farmDao.cantidadFarms(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaFarms = farmDao.consultarFarms(paginador.getInicio(),
					paginador.getRango(), consulta, parametros);
			if ((listaFarms == null || listaFarms.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaFarms == null || listaFarms.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("farm_label_s"),
								unionMensajesBusqueda);
			}
			cargarDetallesFarms();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFarm";
	}

	/**
	 * This method weapon consultation for advanced search arm also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
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
			consult.append("WHERE UPPER(f.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new farm.
	 * 
	 * @param farm
	 *            :property that you are adding or editing
	 * 
	 * @return "regFarm": redirects to the record template farm.
	 * @throws Exception
	 */
	public String agregarEditarFarm(Farm farm) throws Exception {
		if (farm != null) {
			this.farm = farm;
			this.nombreFotoLogo = this.farm.getLogo();
			this.cargarFotoTemporal = false;
			cargarCombos();
		} else {
			this.farm = new Farm();
			this.cargarCombos();
			this.nombreFotoLogo = null;
			this.fileUploadBean = new FileUploadBean();
			this.cargarFotoTemporal = true;
		}
		return "regFarm";
	}

	/**
	 * Method used to save or edit the properties
	 * 
	 * @return consultarFarms: Redirects to manage farms with updated list of
	 *         farms
	 */
	public String guardarFarm() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		boolean seCambioLogo = false;
		String nombreFotoBorrar = null;
		try {
			/* They are not required fields */
			if (this.farm.getPais().getId() == 0) {
				this.farm.setPais(null);
			}
			if (this.farm.getDepartamento().getId() == 0) {
				this.farm.setDepartamento(null);
			}
			if (this.farm.getMunicipio().getId() == 0) {
				this.farm.setMunicipio(null);
			}
			if (farm.getIdFarm() != 0) {
				if (this.farm.getLogo() != null
						&& !"".equals(this.farm.getLogo())
						&& !this.farm.getLogo().equals(this.nombreFotoLogo)) {
					this.borrarArchivoReal(this.farm.getLogo());
					seCambioLogo = true;
				} else if (farm.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					seCambioLogo = true;
				}
				this.farm.setLogo(this.nombreFotoLogo);
				if (farm.getLogo() != null && seCambioLogo) {
					nombreFotoBorrar = this.nombreFotoLogo;
					/* The image is loaded into the actual location */
					subirImagenUbicacionReal();
				}
				farmDao.editarFarm(farm);
			} else {
				if (this.nombreFotoLogo != null
						&& !"".equals(this.nombreFotoLogo.trim())) {
					nombreFotoBorrar = this.nombreFotoLogo;
					subirImagenUbicacionReal();
				}
				this.farm.setLogo(this.nombreFotoLogo);
				mensajeRegistro = "message_registro_guardar";
				farmDao.guardarFarm(farm);
			}
			/* The temporary file is deleted */
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), farm.getName()));
		} catch (Exception e) {
			if (this.nombreFotoLogo != null && !"".equals(this.nombreFotoLogo)
					&& farm.getIdFarm() == 0) {
				this.borrarArchivoReal(this.nombreFotoLogo);
			}
			ControladorContexto.mensajeError(e);
		}
		return consultarFarms();
	}

	/**
	 * To validate the name of the farm, so it is not repeated in the database
	 * and valid against XSS.
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = farm.getIdFarm();
			Farm farmAux = new Farm();
			farmAux = farmDao.nombreExiste(nombre, id);
			if (farmAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows you to load combo country, department and municipality
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @throws Exception
	 * 
	 */
	private void cargarCombos() throws Exception {
		itemsPaises = new ArrayList<SelectItem>();
		List<Pais> paises = paisDao.consultarPaisesVigentes();
		if (paises != null) {
			for (Pais pais : paises) {
				itemsPaises.add(new SelectItem(pais.getId(), pais.getNombre()));
			}
		}
		cargarDepartamentos();
		cargarMunicipios();
	}

	/**
	 * This method performs the consultation of the departments registered in
	 * the database, associated with a selected country.
	 * 
	 * @author Cristhian.Pico
	 * 
	 */
	public void cargarDepartamentos() {
		itemDepartamentos = new ArrayList<SelectItem>();
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Pais pais = farm.getPais();
			if (pais != null && pais.getId() > 0) {
				short idPais = pais.getId();
				List<Departamento> departamentos = departamentoDao
						.consultarDepartamentosPaisVigentes(idPais);
				if (departamentos != null) {
					for (Departamento d : departamentos) {
						itemDepartamentos.add(new SelectItem(d.getId(), d
								.getNombre()));
					}
				}
				cargarMunicipios();
			} else {
				farm.setDepartamento(new Departamento());
				farm.setMunicipio(new Municipio());
				farm.getDepartamento().setId(0);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered in the
	 * database, associated with a department selected a partner country.
	 * 
	 * @author Cristhian.Pico
	 * 
	 */
	public void cargarMunicipios() {
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Departamento departamento = farm.getDepartamento();
			if (departamento != null && departamento.getId() > 0
					&& this.itemDepartamentos.size() > 0) {
				int idDepartamento = departamento.getId();
				List<Municipio> municipios = municipioDao
						.consultarMunicipiosVigentes(idDepartamento);
				if (municipios != null) {
					for (Municipio m : municipios) {
						itemsMunicipios.add(new SelectItem(m.getId(), m
								.getNombre()));
					}
				}
			} else {
				itemsMunicipios = new ArrayList<SelectItem>();
				farm.setDepartamento(new Departamento());
				farm.setMunicipio(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated to a farm
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @throws Exception
	 */
	public void cargarDetallesFarms() throws Exception {
		List<Farm> farms = new ArrayList<Farm>();
		farms.addAll(this.listaFarms);
		this.listaFarms = new ArrayList<Farm>();
		for (Farm farm : farms) {
			cargarDetallesUnaFarm(farm);
			this.listaFarms.add(farm);
		}
	}

	/**
	 * Method of uploading the details of a farm.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param farm
	 *            : farm to which the details will be loaded.
	 * @throws Exception
	 */
	public void cargarDetallesUnaFarm(Farm farm) throws Exception {
		int idFarm = farm.getIdFarm();
		Pais pais = (Pais) this.farmDao.consultarObjetoFarm("pais", idFarm);
		Departamento departamento = (Departamento) this.farmDao
				.consultarObjetoFarm("departamento", idFarm);
		Municipio municipio = (Municipio) this.farmDao.consultarObjetoFarm(
				"municipio", idFarm);
		farm.setPais(pais);
		farm.setDepartamento(departamento);
		farm.setMunicipio(municipio);
	}

	/**
	 * Delete the file name.
	 * 
	 * @author Cristhian.Pico
	 */
	public void borrarFilename() {
		if (nombreFotoLogo != null && !"".equals(nombreFotoLogo)
				&& !nombreFotoLogo.equals(farm.getLogo())
				&& this.cargarFotoTemporal) {
			borrarArchivo(nombreFotoLogo);
		}
		nombreFotoLogo = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @author Cristhian.Pico
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
	 * @author Cristhian.Pico
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
	 * Method allows you to load the file system
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param e
	 *            : Fileupload event for the file to be uploaded to the server.
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
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			mensaje = "error_carga_archivo";
		}
		if (!"".equals(mensaje)) {
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					bundle.getString(mensaje));
		}
		if (farm.getIdFarm() != 0) {
			cargarFotoTemporal = true;
		}
		nombreFotoLogo = fileUploadBean.getFileName();
	}

	/**
	 * Upload the logo image to the actual folder
	 * 
	 * @author Cristhian.Pico
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

		/* It checks whether the destinations are created there but */
		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, fileUploadBean.getFileName());
		File fileDestino1 = new File(destino1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destino2, fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Method for removing a farm in the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarFarms: See the list of the properties and returns to
	 *         manage finances
	 */
	public String eliminarFarm() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			farmDao.eliminarFarm(farm);
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(
							bundle.getString("message_registro_eliminar"),
							farm.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					farm.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarFarms();
	}
}
