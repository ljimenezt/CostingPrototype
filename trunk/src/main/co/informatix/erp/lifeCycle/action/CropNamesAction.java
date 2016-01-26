package co.informatix.erp.lifeCycle.action;

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

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
/**
 * This class allows the logical names that may be crop in the database
 * 
 * The logic is to consult, edit or add names crop
 * 
 * @author Johnatan.Naranjo
 * 
 */
public class CropNamesAction implements Serializable {
	@EJB
	private CropNamesDao cropNamesDao;

	private List<CropNames> listaCropNames;

	private CropNames cropNames;
	private Paginador paginador = new Paginador();

	private String nombreBuscar;

	/**
	 * @return List<CropNames>: list of names shown in crop user interface
	 */
	public List<CropNames> getListaCropNames() {
		return listaCropNames;
	}

	/**
	 * @param listaCropNames
	 *            : list of names shown in crop user interface
	 */
	public void setListaCropNames(List<CropNames> listaCropNames) {
		this.listaCropNames = listaCropNames;
	}

	/**
	 * Get data from a name crop
	 * 
	 * @return cropNames: data object containing a name Harvest
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * Set data from a name crop
	 * 
	 * @param cropNames
	 *            : data object containing a name Harvest
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return Paginador: Management paginated list of the names of crops.
	 * 
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paginated list of the names of crops.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name of the crop to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name of the crop to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the names of crops
	 * 
	 * @return consultarCropNames: query method names crop returns to the
	 *         template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarCropNames();
	}

	/**
	 * Consult the list of the names of the crops
	 * 
	 * @return "gesCropNames": redirects to the template to manage the names of
	 *         crops
	 */
	public String consultarCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaCropNames = new ArrayList<CropNames>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = cropNamesDao
					.cantidadCropNames(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaCropNames = cropNamesDao.consultarCropNames(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaCropNames == null || listaCropNames.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaCropNames == null || listaCropNames.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("crop_names_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCropNames";
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
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
			consult.append("WHERE UPPER(cn.cropName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new crop name.
	 * 
	 * @param cropNames
	 *            :Crop name to be add or edit
	 * 
	 * @return "regCropNames":redirects to the record template crop.
	 */
	public String agregarEditarCropNames(CropNames cropNames) {
		if (cropNames != null) {
			this.cropNames = cropNames;
		} else {
			this.cropNames = new CropNames();
		}
		return "regCropNames";
	}

	/**
	 * To validate the name of the crop, so it is not repeated in the database
	 * and validates against XSS.
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
			int id = cropNames.getIdCropName();
			CropNames cropNamesAux = new CropNames();
			cropNamesAux = cropNamesDao.nombreExiste(nombre, id);
			if (cropNamesAux != null) {
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
	 * Method used to save or edit the names of crops
	 * 
	 * @return consultarCropNames: Redirects to manage harvests names with the
	 *         list of names updated
	 */
	public String guardarCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (cropNames.getIdCropName() != 0) {
				cropNamesDao.editarCropNames(cropNames);
			} else {
				mensajeRegistro = "message_registro_guardar";
				cropNamesDao.guardarCropNames(cropNames);
			}
			ControladorContexto
					.mensajeInformacion(null, MessageFormat.format(
							bundle.getString(mensajeRegistro),
							cropNames.getCropName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarCropNames();
	}

	/**
	 * Method to remove a name from the crop of the database
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarCropNames: Consult the list of names of crops and
	 *         returns to manage the crop name.
	 */
	public String eliminarCropNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cropNamesDao.eliminarCropNames(cropNames);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					cropNames.getCropName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					cropNames.getCropName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarCropNames();
	}

}
