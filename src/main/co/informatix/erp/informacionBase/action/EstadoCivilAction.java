package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.informacionBase.dao.EstadoCivilDao;
import co.informatix.erp.informacionBase.entities.EstadoCivil;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class can handle business logic of civil states They exist in the
 * system, which allows you to insert, modify, and query
 * 
 * @author Gerson.Cespedes
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class EstadoCivilAction implements Serializable {

	private List<EstadoCivil> listaEstadosCiviles;
	private Paginador pagination = new Paginador();
	private EstadoCivil estadoCivil;
	private String vigencia = "si";
	private String nombreBuscar;

	@EJB
	private EstadoCivilDao estadoCivilDao;
	@Inject
	private IdentityAction identity;

	/**
	 * 
	 * @return listaEstadosCiviles: get a list of civil states.
	 */
	public List<EstadoCivil> getListaEstadosCiviles() {
		return listaEstadosCiviles;
	}

	/**
	 * 
	 * @param listaEstadosCiviles
	 *            : get a list of civil states.
	 */
	public void setListaEstadosCiviles(List<EstadoCivil> listaEstadosCiviles) {
		this.listaEstadosCiviles = listaEstadosCiviles;
	}

	/**
	 * 
	 * @return estadoCivil: gets an object variable of civil status which will
	 *         perform the management.
	 */
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * 
	 * @param estadoCivil
	 *            : sets an object variable of civil status which will perform
	 *            the management.
	 */
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * 
	 * @return vigencia:It is giving the selected value 'yes' of the current and
	 *         'no' for not applicable.
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * 
	 * @param vigencia
	 *            : It is giving the selected value 'yes' of the current and
	 *            'no' for not applicable.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nombreBuscar: name by which you want to view the status civil.
	 * 
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : name by which you want to view the status civil.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * 
	 * @return pagination: paging management of the list of civil States May Have
	 *         the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : mapaging management of the list of civil States May Have the
	 *            view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * A new object instance of civil status, if this is different from null
	 * loads for editing.
	 * 
	 * @param estadoCivil
	 *            : object variable civil status you want to upload edition.
	 * 
	 * @return regEstadoCivil: Navigation rule that redirects to register civil
	 *         status.
	 */
	public String nuevoEstadoCivil(EstadoCivil estadoCivil) {
		if (estadoCivil != null) {
			this.estadoCivil = estadoCivil;
		} else {
			this.estadoCivil = new EstadoCivil();
		}
		return "regEstadoCivil";

	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultarEstadosCiviles: method consulting civil status and load
	 *         the template with the information found.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarEstadosCiviles();
	}

	/**
	 * Consult the list of civil states.
	 * 
	 * @return gesEstadoCivil: Navigation rule that redirects to manage civil
	 *         status.
	 */
	public String consultarEstadosCiviles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInfoBase = ControladorContexto
				.getBundle("mensajeInformacionBase");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listaEstadosCiviles = new ArrayList<EstadoCivil>();
		String mensajeBusqueda = "";

		try {
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadEstadosCiviles = estadoCivilDao
					.cantidadEstadosCiviles(condicionVigencia,
							this.nombreBuscar);
			if (cantidadEstadosCiviles != null) {
				pagination.paginar(cantidadEstadosCiviles);
			}
			this.listaEstadosCiviles = estadoCivilDao.consultarEstadosCiviles(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.nombreBuscar);
			if (listaEstadosCiviles == null || listaEstadosCiviles.size() <= 0
					&& this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (listaEstadosCiviles == null
					|| listaEstadosCiviles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInfoBase
										.getString("estado_civil_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesEstadoCivil";
	}

	/**
	 * That rule Navigation redirects to manage civil status.
	 * 
	 * @param vigente
	 *            : boolean to find out the validity ends with 'true' or INICA
	 *            with 'false', the selected record in the user interface.
	 * @return consultarEstadosCiviles: boolean to find out the validity ends
	 *         with 'true' or INICA with 'false', the selected record in the
	 *         user interface.
	 */
	public String vigenciaEstadoCivil(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			estadoCivil.setUserName(identity.getUserName());
			String mensaje = "";
			if (vigente) {
				estadoCivil.setFechaFinVigencia(new Date());
				estadoCivilDao.editarEstadoCivil(estadoCivil);
				mensaje = bundle
						.getString("message_fin_vigencia_satisfactorio") + ": ";
			} else {
				estadoCivil.setFechaFinVigencia(null);
				estadoCivilDao.editarEstadoCivil(estadoCivil);
				mensaje = bundle
						.getString("message_inicio_vigencia_satisfactorio")
						+ ": ";
			}
			ControladorContexto.mensajeInformacion(null,
					mensaje + estadoCivil.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstadosCiviles();
	}

	/**
	 * To validate the name of civil status, so that it is not repeated in the
	 * database and validates against XSS.
	 * 
	 * @param contexto
	 *            : JSF context of view.
	 * @param toValidate
	 *            : View component to validate.
	 * @param value
	 *            : Value of the object in view.
	 */
	public void validarNombreXSS(FacesContext contexto, UIComponent toValidate,
			Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		String mensajeVigencia = "message_ya_existe_sin_vigencia";
		try {
			int id = estadoCivil.getId();
			String nombreCapitalize = WordUtils.capitalizeFully(nombre);
			EstadoCivil estadoCivilAux = new EstadoCivil();
			if (id != 0) {
				estadoCivilAux = estadoCivilDao.nombreExisteActualizar(
						nombreCapitalize, id);
			} else {
				estadoCivilAux = estadoCivilDao.nombreExiste(nombreCapitalize);
			}
			if (estadoCivilAux != null) {
				if (estadoCivilAux.getFechaFinVigencia() != null) {
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeVigencia, "mensaje");
					((UIInput) toValidate).setValid(false);
				} else {
					mensajeVigencia = "message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeVigencia, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(nombre, clientId, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the filing statuses.
	 * 
	 * @return consultarEstadosCiviles: method for querying the states civil and
	 *         redirect staff to manage.
	 */
	public String guardarEstadoCivil() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			estadoCivil.setNombre(WordUtils.capitalizeFully(estadoCivil
					.getNombre()));

			estadoCivil.setUserName(identity.getUserName());
			if (estadoCivil.getId() != 0) {
				estadoCivilDao.editarEstadoCivil(estadoCivil);
			} else {
				key = "message_registro_guardar";
				estadoCivil.setFechaCreacion(new Date());
				estadoCivilDao.guardarEstadoCivil(estadoCivil);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							estadoCivil.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstadosCiviles();
	}

}
