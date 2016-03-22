package co.informatix.erp.organizaciones.action;

import java.awt.Color;
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

import co.informatix.erp.informacionBase.dao.UnidadMedidaDao;
import co.informatix.erp.informacionBase.entities.UnidadMedida;
import co.informatix.erp.organizaciones.dao.ZonaDao;
import co.informatix.erp.organizaciones.entities.Zona;
import co.informatix.erp.seguridad.action.SesionEmpresaAction;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class allows business logic of the areas that has a farm. The logic is
 * to see, edit, add or change the life of an area.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ZonaAction implements Serializable {

	@Inject
	private IdentityAction identity;
	@Inject
	private SesionEmpresaAction empresaHaciendaSesion;
	@Resource
	private UserTransaction userTransaction;

	@EJB
	private ZonaDao zonaDao;
	@EJB
	private UnidadMedidaDao unidadMedidaDao;
	@EJB
	private FileUploadBean fileUploadBean;

	private List<Zona> listaZonas;
	private List<SelectItem> itemsUnidadesMedida;

	private Color colorControl = new Color(255, 255, 255);
	private Paginador pagination = new Paginador();
	private Zona zona;

	private String vigencia = Constantes.SI;
	private String nombreBuscar;
	private String carpetaArchivosReal;
	private String carpetaArchivosTemporal;
	private boolean cargarFotoTemporal;

	/**
	 * @return vigencia: allows obtaining the selected value 'yes' of existing
	 *         and 'no' for not applicable
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : allows obtaining the selected value 'yes' of existing and
	 *            'no' for not applicable
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return List<Zona>: Allows to obtain the list of places that are loaded
	 *         into the user interface.
	 */
	public List<Zona> getListaZonas() {
		return listaZonas;
	}

	/**
	 * @param listaZonas
	 *            :Allows to obtain the list of places that are loaded into the
	 *            user interface.
	 */
	public void setListaZonas(List<Zona> listaZonas) {
		this.listaZonas = listaZonas;
	}

	/**
	 * Gets an area to which the issue or registration is implemented.
	 * 
	 * @return Zona: Attribute refers to a Zone
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * Sets an area to which the issue or registration is implemented.
	 * 
	 * @param zona
	 *            : Attribute refers to a Zone
	 */
	public void setZona(Zona zona) {
		fileUploadBean.setFileName(zona.getFoto());
		this.zona = zona;
	}

	/**
	 * 
	 * @return pagination: paging management of the list of areas in the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            : paging management of the list of areas in the view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nombreBuscar: name by which you want to see the area.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : name by which you want to see the area.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return empresaHaciendaSesion: Object of the company in session that the
	 *         user has selected.
	 */
	public SesionEmpresaAction getEmpresaHaciendaSesion() {
		return empresaHaciendaSesion;
	}

	/**
	 * @return itemsUnidadesMedida: items as units that are loaded into the
	 *         combo in the user interface.
	 */
	public List<SelectItem> getItemsUnidadesMedida() {
		return itemsUnidadesMedida;
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
	 * @return carpetaArchivosReal: the actual folder path where the photos are
	 *         loaded zones
	 */
	public String getCarpetaArchivosReal() {
		this.carpetaArchivosReal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_FOTOS_ZONAS;
		return carpetaArchivosReal;
	}

	/**
	 * @return carpetaArchivosTemporal: the actual folder path where the photos
	 *         are loaded zones
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
	 * @return colorControl: color used to assign the ColorPicker to save the
	 *         color information of the edge of the area.
	 */
	public Color getColorControl() {
		return colorControl;
	}

	/**
	 * @param colorControl
	 *            : color used to assign the ColorPicker to save the color
	 *            information of the edge of the area.
	 */
	public void setColorControl(Color colorControl) {
		this.colorControl = colorControl;
	}

	/**
	 * Method to edit or create a new area.
	 * 
	 * @param zona
	 *            : Area to be edited
	 * @return regZona: allows redirecting the record template area.
	 */
	public String agregarEditarZona(Zona zona) {
		try {
			fileUploadBean = new FileUploadBean();
			if (zona != null) {
				this.zona = zona;
				fileUploadBean.setFileName(this.zona.getFoto());
				this.cargarFotoTemporal = false;
				if (this.zona.getColorBordeMapa() != null) {
					colorControl = ValidacionesAction.parseStringtoColor("FF"
							+ this.zona.getColorBordeMapa());
				}
			} else {
				this.zona = new Zona();
				fileUploadBean.setFileName(this.zona.getFoto());
				this.cargarFotoTemporal = true;
				colorControl = ValidacionesAction
						.parseStringtoColor("FFFFFFFF");
			}
			cargarComboUnidadMedida();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regZona";
	}

	/**
	 * Method of uploading the information from combo unit of measure.
	 * 
	 * @throws Exception
	 */
	private void cargarComboUnidadMedida() throws Exception {
		itemsUnidadesMedida = new ArrayList<SelectItem>();
		List<UnidadMedida> listaUnidadesMedidaVigentes = unidadMedidaDao
				.consultarUnidadesMedidaVigentes(Constantes.TIPO_UNIDAD_LONGITUD);
		if (listaUnidadesMedidaVigentes != null) {
			for (UnidadMedida unidadMedida : listaUnidadesMedidaVigentes) {
				itemsUnidadesMedida.add(new SelectItem(unidadMedida.getId(),
						unidadMedida.getNombre()));
			}
		}
	}

	/**
	 * Method that allows initialize the parameters of the search and load the
	 * initial list of areas.
	 * 
	 * @return consultarZonas: query method that returns to the zones and
	 *         template management.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarZonas();
	}

	/**
	 * Consult the list of zones.
	 * 
	 * @return retorno: redirects to the template to manage areas.
	 */
	public String consultarZonas() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String enModal = ControladorContexto.getParam("param2");
		String idMessage = ControladorContexto.getParam("idMessage");
		this.listaZonas = new ArrayList<Zona>();
		List<Zona> listaZonasTemp = new ArrayList<Zona>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		boolean desdeModal = (enModal != null && Constantes.SI.equals(enModal)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesZona";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidadZonas = zonaDao.cantidadZonas(consulta, parametros);
			if (cantidadZonas != null) {
				if (desdeModal)
					pagination.paginarRangoDefinido(cantidadZonas, 5);
				else
					pagination.paginar(cantidadZonas);
			}
			listaZonasTemp = zonaDao.consultarZonas(pagination.getInicio(),
					pagination.getRango(), consulta, parametros);
			if ((listaZonasTemp == null || listaZonasTemp.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaZonasTemp == null || listaZonasTemp.size() <= 0) {
				mensajeBusqueda = bundle
						.getString("message_no_existen_registros");
				if (!desdeModal) {
					ControladorContexto.mensajeInformacion(null,
							mensajeBusqueda);
					mensajeBusqueda = "";
				}
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrg.getString("zona_label_s"),
								unionMensajesBusqueda);
			}
			if (!"".equals(mensajeBusqueda) && desdeModal) {
				ControladorContexto.mensajeInformacion(idMessage,
						mensajeBusqueda);
			} else {
				validaciones.setMensajeBusqueda(mensajeBusqueda);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
	}

	/**
	 * This method allows to build the query to the advanced search and to
	 * display messages depending on the search criteria selected by the user.
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
		consult.append("WHERE z.fechaFinVigencia ");
		String condicionVigencia = Constantes.IS_NULL + " ";
		if (Constantes.NOT.equals(vigencia)) {
			condicionVigencia = Constantes.IS_NOT_NULL + " ";
		}
		consult.append(condicionVigencia);
		if (empresaHaciendaSesion.getIdHacienda() != 0) {
			consult.append("AND z.hacienda.id=:idHacienda ");
			SelectItem item = new SelectItem(
					empresaHaciendaSesion.getIdHacienda(), "idHacienda");
			parameters.add(item);
		}
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			SelectItem item = new SelectItem("%" + nombreBuscar + "%",
					"keyword");
			consult.append("AND UPPER(z.nombre) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method that allows validate the null values in the area, in this case
	 * assigned to Zone null object to avoid exception TransientObjectException
	 */
	private void validarNulos() {
		if (this.zona.getUnidadMedida() != null
				&& this.zona.getUnidadMedida().getId() == 0) {
			this.zona.setUnidadMedida(null);
		}
	}

	/**
	 * Effective starts or ends areas.
	 * 
	 * @param vigente
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * @return consultarZonas(): method that allows consult the areas and return
	 *         to the template management.
	 */
	public String vigenciaZonas(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
		try {
			validarNulos();
			zona.setUserName(identity.getUserName());
			if (vigente) {
				zona.setFechaFinVigencia(new Date());
			} else {
				mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
				zona.setFechaFinVigencia(null);
			}
			zonaDao.editarZona(zona);
			ControladorContexto.mensajeInformacion(
					null,
					bundle.getString(mensajeCambioVigencia) + ": "
							+ zona.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarZonas();
	}

	/**
	 * Method that allows validate the name of the area by the Treasury in
	 * session, so that it is not repeated in the database and validates against
	 * XSS.
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
			int id = zona.getId();
			Zona zonaAux = zonaDao.nombreZonaExiste(nombre, id,
					empresaHaciendaSesion.getIdHacienda());
			if (zonaAux != null) {
				if (zonaAux.getFechaFinVigencia() != null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				}
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
	 * Method that allows delete files from the actual location
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivoReal(String fileName) {
		String ubicaciones[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH
						+ this.getCarpetaArchivosReal(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getCarpetaArchivosReal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

}