package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.AllocationDao;
import co.informatix.erp.costs.entities.Allocation;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the names of the assignments they can exist.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class AllocationAction implements Serializable {

	@EJB
	private AllocationDao allocationDao;

	private Allocation allocation;
	private List<Allocation> listaAllocation;
	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	/**
	 * @return allocation: returns an assignment.
	 */
	public Allocation getAllocation() {
		return allocation;
	}

	/**
	 * @param allocation
	 *            : returns an assignment.
	 */
	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	/**
	 * @return listaAllocation: list of assignments.
	 */
	public List<Allocation> getListaAllocation() {
		return listaAllocation;
	}

	/**
	 * @param listaAllocation
	 *            : list of assignments.
	 */
	public void setListaAllocation(List<Allocation> listaAllocation) {
		this.listaAllocation = listaAllocation;
	}

	/**
	 * @return paginador: Management paged list of assignments.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of assignments.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name of the assignment to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name of the assignment to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of assignments
	 * 
	 * @return consultarAllocation: Method to initialize the parameters of the
	 *         search and load the initial list of assignments
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarAllocation();
	}

	/**
	 * Consult the list of assignments.
	 * 
	 * @return "gesAllocation": navigation rule to manage allocation
	 */
	public String consultarAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaAllocation = new ArrayList<Allocation>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = allocationDao.cantidadAllocation(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaAllocation = allocationDao.consultarAllocation(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaAllocation == null || listaAllocation.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaAllocation == null || listaAllocation.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("tipo_recurso_humano_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesAllocation";
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @param consulta
	 *            : query to concatenate
	 * @param parametros
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * @param unionMensajesBusqueda
	 *            : Message search
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consulta.append("WHERE UPPER(ec.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new assignment.
	 * 
	 * @param allocation
	 *            : assignment to be adding or editing
	 * @return regAllocation: redirected to the page register allocation.
	 */
	public String agregarEditarAllocation(Allocation allocation) {
		if (allocation != null) {
			this.allocation = allocation;
		} else {
			this.allocation = new Allocation();
		}
		return "regAllocation";
	}

	/**
	 * Method used to save or edit the assignments.
	 * 
	 * @return consultarAllocation: redirects to manage staff assignments with
	 *         the updated list of assignments
	 */
	public String guardarAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (allocation.getIdAllocation() != 0) {
				allocationDao.editarAllocation(allocation);
			} else {
				mensajeRegistro = "message_registro_guardar";
				allocationDao.guardarAllocation(allocation);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), allocation.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarAllocation();
	}

	/**
	 * Method to remove an allocation of the database.
	 * 
	 * @return consultarAllocation: Consult the list of allocation and returns
	 *         to manage allocation.
	 */
	public String eliminarAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			allocationDao.eliminarAllocation(allocation);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					allocation.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					allocation.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarAllocation();
	}

}
