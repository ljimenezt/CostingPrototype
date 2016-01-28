package co.informatix.erp.warehouse.action;

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

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DiseasesDao;
import co.informatix.erp.warehouse.entities.Diseases;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * diseases in the system
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DiseasesAction implements Serializable {
	private List<Diseases> listaDiseases;
	private Paginador paginador = new Paginador();
	private Diseases diseases;
	private String nombreBuscar;

	@EJB
	private DiseasesDao diseasesDao;

	/**
	 * @return listaDiseases: List of diseases
	 */
	public List<Diseases> getListaDiseases() {
		return listaDiseases;
	}

	/**
	 * @param listaDiseases
	 *            : List of diseases
	 */
	public void setListaDiseases(List<Diseases> listaDiseases) {
		this.listaDiseases = listaDiseases;
	}

	/**
	 * @return paginador: Paging from the list of diseases that can be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paging from the list of diseases that can be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return diseases: Object of diseases
	 */
	public Diseases getDiseases() {
		return diseases;
	}

	/**
	 * @param diseases
	 *            : Object of diseases
	 */
	public void setDiseases(Diseases diseases) {
		this.diseases = diseases;
	}

	/**
	 * @return nombreBuscar: Name by which you want to consult disease
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to consult disease
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of diseases
	 * 
	 * @return consultarDiseases: Method consulting diseases, returns to the
	 *         template management
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarDiseases();
	}

	/**
	 * Consult the list of existing diseases
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return gesDiseases: Navigation rule that redirects manage diseases
	 */
	public String consultarDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaDiseases = new ArrayList<Diseases>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = diseasesDao.cantidadDiseases(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaDiseases = diseasesDao.consultarDiseases(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaDiseases == null || listaDiseases.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaDiseases == null || listaDiseases.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("diseases_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesDiseases";
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
			consult.append("WHERE UPPER(d.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new disease
	 * 
	 * @param diseases
	 *            : Disease are adding or editing
	 * 
	 * @return regDiseases: Template redirects to register disease
	 */
	public String agregarEditarDiseases(Diseases diseases) {
		if (diseases != null) {
			this.diseases = diseases;
		} else {
			this.diseases = new Diseases();
		}
		return "regDiseases";
	}

	/**
	 * To validate the name of the disease, so it is not repeated in the
	 * database and validates against XSS.
	 * 
	 * @param context
	 *            : Application context
	 * 
	 * @param toValidate
	 *            : Validate component
	 * @param value
	 *            : Field value is validated
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = diseases.getIdDisease();
			Diseases diseasesAux = new Diseases();
			diseasesAux = diseasesDao.nombreExiste(nombre, id);
			if (diseasesAux != null) {
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
	 * Method used to save or edit diseases
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultarDiseases: Redirects to manage the disease with the list
	 *         of names updated
	 */
	public String guardarDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (diseases.getIdDisease() != 0) {
				diseasesDao.editarDiseases(diseases);
			} else {
				mensajeRegistro = "message_registro_guardar";
				diseasesDao.guardarDiseases(diseases);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), diseases.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarDiseases();
	}

	/**
	 * Method for eliminating a disease of the database
	 * 
	 * 
	 * @return consultarDiseases: Redirects to manage the disease with the list
	 *         of names updated
	 */
	public String eliminarDiseases() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			diseasesDao.eliminarDiseases(diseases);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(bundle
							.getString("message_registro_eliminar")), diseases
							.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					diseases.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarDiseases();
	}

}
