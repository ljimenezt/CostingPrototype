package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.dao.MaintenanceAndCalibrationDao;
import co.informatix.erp.machines.dao.MaintenanceLinesDao;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.machines.entities.MaintenanceAndCalibration;
import co.informatix.erp.machines.entities.MaintenanceLines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all related logic with the creation and updating of maintenance
 * lines in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@ManagedBean
@SuppressWarnings("serial")
@RequestScoped
public class MaintenanceLinesAction implements Serializable {
	private List<MaintenanceLines> listaMaintenanceLines;
	private List<SelectItem> opcionesMachines;
	private List<SelectItem> opcionesMaintenance;

	private Paginador paginador = new Paginador();

	private MaintenanceLines maintenanceLines;

	private String nombreMaquinaBuscar;

	@EJB
	private MaintenanceLinesDao maintenanceLinesDao;

	@EJB
	private MaintenanceAndCalibrationDao maintenanceAndCalibrationDao;

	@EJB
	private MachinesDao machinesDao;

	/**
	 * @return listaMaintenanceLines: gets the list of lines maintenance
	 */
	public List<MaintenanceLines> getListaMaintenanceLines() {
		return listaMaintenanceLines;
	}

	/**
	 * @param listaMaintenanceLines
	 *            : sets the list of lines maintenance
	 */
	public void setListaMaintenanceLines(
			List<MaintenanceLines> listaMaintenanceLines) {
		this.listaMaintenanceLines = listaMaintenanceLines;
	}

	/**
	 * @return opcionesMachines: gets the list of the machines attached to the
	 *         lines maintenance
	 */
	public List<SelectItem> getOpcionesMachines() {
		return opcionesMachines;
	}

	/**
	 * @param opcionesMachines
	 *            : sets the list of the machines attached to the lines
	 *            maintenance
	 */
	public void setOpcionesMachines(List<SelectItem> opcionesMachines) {
		this.opcionesMachines = opcionesMachines;
	}

	/**
	 * @return opcionesMaintenance: gets the list of the maintenance and
	 *         calibration lines associated with maintenance
	 */
	public List<SelectItem> getOpcionesMaintenance() {
		return opcionesMaintenance;
	}

	/**
	 * @param opcionesMaintenance
	 *            : sets the list of the maintenance and calibration lines
	 *            associated with maintenance
	 */
	public void setOpcionesMaintenance(List<SelectItem> opcionesMaintenance) {
		this.opcionesMaintenance = opcionesMaintenance;
	}

	/**
	 * @return paginador: gets the paged list maintenance lines may be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : sets the paged list maintenance lines may be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return maintenanceLines: gets the class object lines maintenance
	 */
	public MaintenanceLines getMaintenanceLines() {
		return maintenanceLines;
	}

	/**
	 * @param maintenanceLines
	 *            : sets the class object lines maintenance
	 */
	public void setMaintenanceLines(MaintenanceLines maintenanceLines) {
		this.maintenanceLines = maintenanceLines;
	}

	/**
	 * @return nombreMaquinaBuscar: gets the search parameter in the system
	 */
	public String getNombreMaquinaBuscar() {
		return nombreMaquinaBuscar;
	}

	/**
	 * @param nombreMaquinaBuscar
	 *            : sets the search parameter in the system
	 */
	public void setnombreMaquinaBuscar(String nombreMaquinaBuscar) {
		this.nombreMaquinaBuscar = nombreMaquinaBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of maintenance lines
	 * 
	 * @return consultarMaintenanceLines: Method consulting maintenance lines,
	 *         returns to the template management
	 */
	public String inicializarBusqueda() {
		nombreMaquinaBuscar = "";
		return consultarMaintenanceLines();
	}

	/**
	 * Consult the list of lines existing maintenance
	 * 
	 * @return gesMaintLin: Navigation rule that redirects manage maintenance
	 *         lines
	 */
	public String consultarMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMaintenanceLines = new ArrayList<MaintenanceLines>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			llenarMachine();
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = maintenanceLinesDao.cantidadMaintenanceLines(
					consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaMaintenanceLines = maintenanceLinesDao
					.consultarMaintenanceLines(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);

			if ((listaMaintenanceLines == null || listaMaintenanceLines.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMaintenanceLines == null
					|| listaMaintenanceLines.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("lineas_mantenimiento_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaintLin";
	}

	/**
	 * Method to edit or create a new line maintenance
	 * 
	 * @param maintenanceLines
	 *            :Object maintenance lines are adding or editing
	 * 
	 * @return regMaintLin: Redirected to the template record keeping lines
	 * 
	 */
	public String agregarEditarMaintenanceLines(
			MaintenanceLines maintenanceLines) {
		try {
			llenarMachine();
			if (maintenanceLines != null) {
				this.maintenanceLines = maintenanceLines;
			} else {
				this.maintenanceLines = new MaintenanceLines();
				this.maintenanceLines.setMachines(new Machines());
				this.maintenanceLines
						.setMaintenanceAndCalibration(new MaintenanceAndCalibration());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMaintLin";
	}

	/**
	 * Method to load the machines on a list
	 * 
	 * @throws Exception
	 */
	private void llenarMachine() throws Exception {
		opcionesMachines = new ArrayList<SelectItem>();
		List<Machines> listMachines = machinesDao.listMachines();
		if (listMachines != null) {
			for (Machines machines : listMachines) {
				opcionesMachines.add(new SelectItem(machines.getIdMachine(),
						machines.getName()));

			}
		}

	}

	/**
	 * Method to load the maintenance and calibration in a list
	 */
	public void llenarMaintenance() {
		opcionesMaintenance = new ArrayList<SelectItem>();
		SimpleDateFormat fecha = new SimpleDateFormat("yyyy/MM/dd");
		try {
			List<MaintenanceAndCalibration> listMaintenanceAndCalibration = maintenanceAndCalibrationDao
					.maintenanceCalibrationXId(this.maintenanceLines
							.getMachines().getIdMachine());
			if (listMaintenanceAndCalibration != null) {
				for (MaintenanceAndCalibration maintenanceAndCalibration : listMaintenanceAndCalibration) {
					opcionesMaintenance.add(new SelectItem(
							maintenanceAndCalibration.getIdMaintenance(), fecha
									.format(maintenanceAndCalibration
											.getDateTime())));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user, a machine name
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
		if (this.nombreMaquinaBuscar != null
				&& !"".equals(this.nombreMaquinaBuscar)) {
			consult.append("JOIN  ml.machines m ");
			consult.append("WHERE UPPER(m.name)=UPPER(:nombreMaquina )");
			SelectItem item = new SelectItem(this.nombreMaquinaBuscar,
					"nombreMaquina");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreMaquinaBuscar + '"');
		}
	}

	/**
	 * Method used to save or edit lines maintenance
	 * 
	 * @return consultarMaintenanceLines: Redirects to manage maintenance lines
	 *         with the list of names updated
	 */
	public String guardarMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (maintenanceLines.getIdMaintenanceline() != 0) {
				maintenanceLinesDao.editarMaintenanceLines(maintenanceLines);
			} else {
				mensajeRegistro = "message_registro_guardar";
				maintenanceLinesDao.guardarMaintenanceLines(maintenanceLines);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					maintenanceLines.getIdMaintenanceline()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMaintenanceLines();
	}

}
