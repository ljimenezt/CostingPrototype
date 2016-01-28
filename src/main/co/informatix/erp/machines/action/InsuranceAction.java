package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.machines.dao.InsuranceDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.Insurance;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows load logic of insurance that may be in the BD The logic is
 * to consult, edit or add insurance
 * 
 * @author Sergio.Ortiz
 **/
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class InsuranceAction implements Serializable {

	@EJB
	private MachinesDao machinesDao;

	@EJB
	private InsuranceDao insuranceDao;

	@EJB
	private MachineTypesDao machineTypesDao;

	private ArrayList<SelectItem> listaMachines;
	private ArrayList<SelectItem> opcionesMachinesType;
	private List<Insurance> listaInsurance;

	private Date fechaInicioBuscar;
	private Date fechaFinBuscar;

	private Paginador paginador = new Paginador();
	private Insurance insurance;
	private Machines machines;
	private MachineTypes machineTypes;
	private MachineTypes machineTypesConsulta;

	/**
	 * @return listaMachines: list of machines associated with insurance
	 */
	public ArrayList<SelectItem> getListaMachines() {
		return listaMachines;
	}

	/**
	 * @param listaMachines
	 *            : list of machines associated with insurance
	 */
	public void setListaMachines(ArrayList<SelectItem> listaMachines) {
		this.listaMachines = listaMachines;
	}

	/**
	 * @return opcionesMachinesType: machinesType list
	 */
	public ArrayList<SelectItem> getOpcionesMachinesType() {
		return opcionesMachinesType;
	}

	/**
	 * @param opcionesMachinesType
	 *            : machinesType list
	 */
	public void setOpcionesMachinesType(
			ArrayList<SelectItem> opcionesMachinesType) {
		this.opcionesMachinesType = opcionesMachinesType;
	}

	/**
	 * @return listaInsurance: insurance list
	 */
	public List<Insurance> getListaInsurance() {
		return listaInsurance;
	}

	/**
	 * @param listaInsurance
	 *            : insurance list
	 */
	public void setListaInsurance(List<Insurance> listaInsurance) {
		this.listaInsurance = listaInsurance;
	}

	/**
	 * @return insurance: class object insurance
	 */
	public Insurance getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance
	 *            : class object insurance
	 * 
	 */
	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return machines: machines object class
	 */
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : machines object class
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return machineTypes: machine object type that is associated with the
	 *         insurance
	 */
	public MachineTypes getMachineTypes() {
		return machineTypes;
	}

	/**
	 * @param machineTypes
	 *            : machine object type that is associated with the insurance
	 */
	public void setMachineTypes(MachineTypes machineTypes) {
		this.machineTypes = machineTypes;
	}

	/**
	 * @return fechaInicioBuscar: Determines the initial range to search for
	 *         insurance in the system
	 */
	public Date getFechaInicioBuscar() {
		return fechaInicioBuscar;
	}

	/**
	 * @param fechaInicioBuscar
	 *            : Determines the initial range to search for insurance in the
	 *            system
	 */
	public void setFechaInicioBuscar(Date fechaInicioBuscar) {
		this.fechaInicioBuscar = fechaInicioBuscar;
	}

	/**
	 * @return fechaFinBuscar: Determines the final range to search for
	 *         insurance in the system
	 */
	public Date getFechaFinBuscar() {
		return fechaFinBuscar;
	}

	/**
	 * @param fechaFinBuscar
	 *            : Determines the final range to search for insurance in the
	 *            system
	 */
	public void setFechaFinBuscar(Date fechaFinBuscar) {
		this.fechaFinBuscar = fechaFinBuscar;
	}

	/**
	 * @return paginador: paged list of insurance that may be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : paged list of insurance that may be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return machineTypesConsulta: type of machine to compare the associated
	 *         DB and selected from the view
	 */
	public MachineTypes getMachineTypesConsulta() {
		return machineTypesConsulta;
	}

	/**
	 * @param machineTypesConsulta
	 *            : type of machine to compare the associated DB and selected
	 *            from the view
	 */
	public void setMachineTypesConsulta(MachineTypes machineTypesConsulta) {
		this.machineTypesConsulta = machineTypesConsulta;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of insurance
	 * 
	 * @return gesInsurance: Navigation rule that redirects manage Insurance
	 */
	public String inicializarBusqueda() {
		try {
			llenarMachinesType();
			fechaInicioBuscar = null;
			fechaFinBuscar = null;
			this.listaInsurance = new ArrayList<Insurance>();
			this.insurance = new Insurance();
			this.insurance.setMachines(new Machines());
			this.machines = new Machines();
			this.machines.setMachineTypes(new MachineTypes());
			this.machineTypes = new MachineTypes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesInsurance";
	}

	/**
	 * Consult the list of existing insurance
	 * 
	 */
	public void consultarInsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInsurance = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaInsurance = new ArrayList<Insurance>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = insuranceDao
					.cantidadInsurance(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaInsurance = insuranceDao.consultarInsurance(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaInsurance == null || listaInsurance.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaInsurance == null || listaInsurance.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInsurance.getString("insurance_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build consultation for advanced search also displayed
	 * messages depending on the search criteria selected by the user.
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
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		int idMachine = machines.getIdMachine();
		if (this.fechaInicioBuscar != null && this.fechaFinBuscar != null) {
			consult.append("WHERE ins.dateTime BETWEEN :fechaInicioBuscar AND :fechaFinBuscar ");
			consult.append("AND m.idMachine =:idMachine ");
			SelectItem item = new SelectItem(fechaInicioBuscar,
					"fechaInicioBuscar");
			parameters.add(item);
			SelectItem item2 = new SelectItem(fechaFinBuscar, "fechaFinBuscar");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formato.format(this.fechaInicioBuscar) + '"' + ", ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formato.format(fechaFinBuscar) + '"';
			unionMessagesSearch.append(dateTo);
			parameters.add(item2);

		} else {
			consult.append("WHERE m.idMachine =:idMachine ");
		}
		SelectItem item3 = new SelectItem(idMachine, "idMachine");
		parameters.add(item3);
	}

	/**
	 * Method to edit or create an insurance.
	 * 
	 * @param insurance
	 *            : Object of insurance that will add or edit
	 * 
	 * @return regInsurance: Redirects to register a template Insurance
	 * 
	 */
	public String agregarEditarInsurance(Insurance insurance) {
		try {
			llenarMachinesType();
			if (insurance != null) {
				this.insurance = insurance;
				this.insurance.setMachines(machines);
			} else {
				this.insurance = new Insurance();
				this.insurance.setMachines(new Machines());
				this.machines = new Machines();
				this.machines.setMachineTypes(new MachineTypes());
				this.machineTypes = new MachineTypes();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regInsurance";
	}

	/**
	 * Method that loads a MachinesTypes list
	 * 
	 * @throws Exception
	 */
	private void llenarMachinesType() throws Exception {
		opcionesMachinesType = new ArrayList<SelectItem>();
		List<MachineTypes> listMachineType = machineTypesDao.listaMachineType();
		if (listMachineType != null) {
			for (MachineTypes listamachinesTypes : listMachineType) {
				opcionesMachinesType.add(new SelectItem(listamachinesTypes
						.getIdMachineType(), listamachinesTypes.getName()));
			}
		}
		llenarMachine();
	}

	/**
	 * Method used to save or edit insurance
	 * 
	 * @return inicializarBusqueda: Redirects to manage the insurance with
	 *         updated list of insurance
	 */
	public String guardarInsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			this.insurance.setMachines(machines);
			this.insurance.getMachines().setMachineTypes(machineTypes);
			if (this.insurance.getIdInsurance() != 0) {
				insuranceDao.editarInsurance(this.insurance);
			} else {
				mensajeRegistro = "message_registro_guardar";
				insuranceDao.guardarInsurance(this.insurance);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					this.insurance.getDescripcion()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}

	/**
	 * Redirects to manage the insurance with updated list of insurance
	 * 
	 **/
	public void llenarMachine() {
		int idMachine = 0;
		try {

			listaMachines = new ArrayList<SelectItem>();
			if (this.insurance != null && machineTypes == null) {
				idMachine = this.insurance.getMachines().getIdMachine();
				this.machines = machinesDao.machinesXId(idMachine);
				listaMachines.add(new SelectItem(machines.getIdMachine(),
						machines.getName()));

				machineTypes = machineTypesDao.machineTypeXMachine(idMachine);
				machines.setMachineTypes(machineTypesConsulta);
			} else {
				if (machineTypes != null) {
					idMachine = machineTypes.getIdMachineType();
				}
				List<Machines> listMachines = machinesDao
						.listaMachinesPorTypes(idMachine);
				if (listMachines != null) {
					for (Machines machines : listMachines) {
						listaMachines.add(new SelectItem(machines
								.getIdMachine(), machines.getName()));
					}
				}
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that allows a Insurance delete the database
	 * 
	 * 
	 * @return inicializarBusqueda: Redirects to manage the insurance with
	 *         updated list of insurance
	 */
	public String eliminarInsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			insuranceDao.eliminarInsurance(insurance);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.insurance.getDescripcion()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					insurance.getDateTime());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return inicializarBusqueda();
	}
}
