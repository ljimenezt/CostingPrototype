package co.informatix.erp.humanResources.action;

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

import co.informatix.erp.humanResources.dao.ContractTypeDao;
import co.informatix.erp.humanResources.entities.ContractType;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, update, and delete types
 * of contracts in the system
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ContractTypeAction implements Serializable {
	private List<ContractType> listaContractType;
	private Paginador paginador = new Paginador();
	private ContractType contractType;

	private String nombreBuscar;

	@EJB
	private ContractTypeDao contractTypeDao;

	/**
	 * @return listaContractType: List of types of contract
	 */
	public List<ContractType> getListaContractType() {
		return listaContractType;
	}

	/**
	 * @param listaContractType
	 *            : List of types of contract
	 */
	public void setListaContractType(List<ContractType> listaContractType) {
		this.listaContractType = listaContractType;
	}

	/**
	 * @return paginador: Paginated list of the types of contract may be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of the types of contract may be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return contractType: Object of the contract types
	 */
	public ContractType getContractType() {
		return contractType;
	}

	/**
	 * @param contractType
	 *            : Object of the contract types
	 */
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return nombreBuscar: Name by which you want to query the type of
	 *         contract
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to query the type of contract
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load initial
	 * listing of the types of contract
	 * 
	 * @return consultarContractType: Method consulting contract types, returns
	 *         to the template management
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarContractType();
	}

	/**
	 * Consult the list of the types of contract
	 * 
	 * @return gesContrType: Navigation rule that redirects to manage contract
	 *         types
	 */
	public String consultarContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaContractType = new ArrayList<ContractType>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = contractTypeDao.cantidadContractType(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaContractType = contractTypeDao.consultarContractType(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaContractType == null || listaContractType.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaContractType == null
					|| listaContractType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				mensajeBusqueda = MessageFormat.format(message,
						bundleRecursosHumanos.getString("contract_type_label"),
						unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesContrType";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to be displayed depending on the search criteria selected by the
	 * user
	 * 
	 * @param consulta
	 *            : Consult concatenate
	 * @param parametros
	 *            : List of search parameters
	 * @param bundle
	 *            : Access language tags
	 * 
	 * @param unionMensajesBusqueda
	 *            : Message search
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consulta.append("WHERE UPPER(ct.nombre) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new type of contract
	 * 
	 * @param contractType
	 *            : Type of contract that will add or edit
	 * 
	 * @return regContrType: Redirected to the template record type contract
	 */
	public String agregarEditarContractType(ContractType contractType) {
		if (contractType != null) {
			this.contractType = contractType;
		} else {
			this.contractType = new ContractType();
		}
		return "regContrType";
	}

	/**
	 * To validate the name of the contract types, to not repeat in the database
	 * and validates against XSS.
	 * 
	 * @param contexto
	 *            : Application context
	 * 
	 * @param toValidate
	 *            : Validate component
	 * @param value
	 *            : Field value is validated
	 */
	public void validarNombreXSS(FacesContext contexto, UIComponent toValidate,
			Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		try {
			int id = contractType.getId();
			ContractType contractTypeAux = new ContractType();
			contractTypeAux = contractTypeDao.nombreExiste(nombre, id);
			if (contractTypeAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						mensajeExistencia, "mensaje");
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
	 * Method used to save or edit the types of contract
	 * 
	 * @return consultarContractType: Redirects to manage the types of contracts
	 *         with the list of names updated.
	 */
	public String guardarContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (contractType.getId() != 0) {
				contractTypeDao.editarContractType(contractType);
			} else {
				mensajeRegistro = "message_registro_guardar";
				contractTypeDao.guardarContractType(contractType);
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(mensajeRegistro),
							contractType.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarContractType();
	}

	/**
	 * Method to delete a type of contract database
	 * 
	 * @return consultarContractType: Redirects to manage the types of contracts
	 *         with the list of names updated
	 */
	public String eliminarContractType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			contractTypeDao.eliminarContractType(contractType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					contractType.getNombre()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					contractType.getNombre());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarContractType();
	}

}
