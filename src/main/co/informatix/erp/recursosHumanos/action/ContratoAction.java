package co.informatix.erp.recursosHumanos.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import co.informatix.erp.recursosHumanos.dao.ContratoDao;
import co.informatix.erp.recursosHumanos.entities.Contrato;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class handles the business logic of contracts, which communicates with
 * the view to record and manage contracts.
 * 
 * The logic is to consult, edit and add contracts.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ContratoAction implements Serializable {

	@EJB
	private ContratoDao contratoDao;
	@Inject
	private IdentityAction identity;

	private List<Contrato> listaContrato;

	private Contrato contrato;
	private Paginador paginador = new Paginador();

	private String nombreBuscar = "";
	private String vigencia = Constantes.SI;

	/**
	 * @return listaContrato: list of contracts shown in the user interface.
	 */
	public List<Contrato> getListaContrato() {
		return listaContrato;
	}

	/**
	 * @param listaContrato
	 *            :list of contracts shown in the user interface.
	 */
	public void setListaContrato(List<Contrato> listaContrato) {
		this.listaContrato = listaContrato;
	}

	/**
	 * @return contrato: Object containing contract information.
	 */
	public Contrato getContrato() {
		return contrato;
	}

	/**
	 * @param contrato
	 *            :Object containing contract information.
	 */
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return paginador: Management paged list of contracts and historical.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paged list of contracts and historical.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: value by which you want to consult the contract.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :value by which you want to consult the contract.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return vigencia: gets the value for the validity management of records
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : sets the value for the validity management of records
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of contracts.
	 * 
	 * @return consultarContratos: method consulting contracts, returns to the
	 *         template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarContratos();
	}

	/**
	 * Consult the list of contracts
	 * 
	 * @modify 31/08/2015 Andres.Gomez
	 * 
	 * @return retorno: redirects to the template to manage contracts.
	 * 
	 */
	public String consultarContratos() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String enModal = ControladorContexto.getParam("param2");
		listaContrato = new ArrayList<Contrato>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		boolean desdeModal = (enModal != null && Constantes.SI.equals(enModal)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesContrato";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = contratoDao.cantidadContratos(consulta, parametros);
			if (cantidad != null) {
				if (desdeModal) {
					paginador.paginarRangoDefinido(cantidad, 5);
				} else {
					paginador.paginar(cantidad);
				}
			}
			listaContrato = contratoDao.consultarContratos(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaContrato == null || listaContrato.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaContrato == null || listaContrato.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("contract_label_s"),
								unionMensajesBusqueda);
			}
			cargarDetallesContratos();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
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
	 * 
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(c.persona.nombres) LIKE UPPER(:keyword) ");
			consult.append("OR UPPER(c.persona.apellidos) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new contract.
	 * 
	 * @param contrato
	 *            :contract that will add or edit
	 * 
	 * @return "regContrato": redirected to the template record contract.
	 * @throws Exception
	 */
	public String agregarEditarContrato(Contrato contrato) throws Exception {
		if (contrato != null) {
			this.contrato = contrato;
			cargarDetallesContrato(contrato);
		} else {
			this.contrato = new Contrato();
			this.contrato.setPersona(new Persona());
		}
		return "regContrato";
	}

	/**
	 * This method fills the various objects associated with a contract.
	 * 
	 * @throws Exception
	 */
	public void cargarDetallesContratos() throws Exception {
		List<Contrato> contratos = new ArrayList<Contrato>();
		if (this.listaContrato != null) {
			contratos.addAll(this.listaContrato);
			this.listaContrato = new ArrayList<Contrato>();
			for (Contrato contrato : contratos) {
				cargarDetallesContrato(contrato);
				this.listaContrato.add(contrato);
			}
		}
	}

	/**
	 * Method to load the details of a contract.
	 * 
	 * @param contrato
	 *            : contract which will carry the details.
	 * @throws Exception
	 */
	public void cargarDetallesContrato(Contrato contrato) throws Exception {
		int idContrato = contrato.getId();
		Persona persona = (Persona) this.contratoDao.consultarObjetoContrato(
				"persona", idContrato);
		contrato.setPersona(persona);
	}

	/**
	 * Method that eliminates the contract database.
	 * 
	 * @return consultarContratos: method consulting contracts, returns to the
	 *         template management.
	 */
	public String eliminarContrato() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			contratoDao.eliminarContrato(contrato);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), contrato
							.getPersona().getNombres()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					contrato.getPersona().getNombres());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarContratos();
	}

	/**
	 * Cleaning method that allows the person associated with the contract.
	 */
	public void limpiarPersona() {
		this.contrato.setPersona(new Persona());
	}

	/**
	 * Method to load the selected person.
	 * 
	 * @param persona
	 *            : object of the selected person.
	 */
	public void cargarPersona(Persona persona) {
		this.contrato.setPersona(persona);
	}

	/**
	 * Method used to save or edit the contracts.
	 * 
	 * @return inicializarBusqueda(): Method consulting contracts and returns to
	 *         the template management.
	 */
	public String guardarContrato() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = bundle.getString("message_registro_modificar");
		try {
			if (this.contrato.getId() != 0) {
				contratoDao.editarContrato(contrato);
			} else {
				key = bundle.getString("message_registro_guardar");
				this.contrato.setFechaCreacion(new Date());
				this.contrato.setUserName(identity.getUserName());
				contratoDao.guardarContrato(contrato);
			}
			this.nombreBuscar = "";
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					key, contrato.getPersona().getNombreCompleto()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}
}
