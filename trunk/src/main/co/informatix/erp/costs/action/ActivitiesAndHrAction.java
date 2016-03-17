package co.informatix.erp.costs.action;

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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesAndHrDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivitiesAndHrPK;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.dao.OvertimePaymentRateDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.humanResources.entities.OvertimePaymentRate;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the relationship between the activities and
 * human resources in the database. The logic is to record the relationship and
 * human resources activities.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndHrAction implements Serializable {

	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private HrDao hrDao;
	@EJB
	private HrTypesDao hrTypeDao;
	@EJB
	private ActivitiesAndHrDao activitiesAndHrDao;
	@EJB
	private OvertimePaymentRateDao overtimePaymentRateDao;

	private int idTrabajador;
	private int idOvertimeRate;
	private boolean validacionTrabajador;
	private boolean workHoursValid;
	private boolean estadoMensaje = false;
	private boolean actividadCertificada = false;
	private boolean reportingActuals = false;

	private List<SelectItem> listaTipoTrabajador;
	private List<SelectItem> listOvertimePaymentRate;
	private List<Hr> trabajadores;
	private List<Hr> trabajadoresSeleccionados;
	private List<ActivitiesAndHr> listaActivitiesAndHrTemp;
	private List<ActivitiesAndHr> listaActivitiesAndHr;

	private ActivitiesAndHrPK activitiesAndHrPK;
	private ActivitiesAndHr activitiesAndHr;
	private Activities activities;
	private Activities actividadSeleccionada;
	private Hr trabajador;
	private Paginador paginador = new Paginador();
	private Paginador paginadorTrabajador = new Paginador();
	private Paginador paginadorActivitiesAndHr = new Paginador();
	private String mensaje;
	private String messageWorkersAvailability;

	/**
	 * @return idTrabajador: id for idTrabajador.
	 */
	public int getIdTrabajador() {
		return idTrabajador;
	}

	/**
	 * @param idTrabajador
	 *            : id for idTrabajador.
	 */
	public void setIdTrabajador(int idTrabajador) {
		this.idTrabajador = idTrabajador;
	}

	/**
	 * @return idOvertimeRate: id for OvertimeRate
	 */
	public int getIdOvertimeRate() {
		return idOvertimeRate;
	}

	/**
	 * @param idOvertimeRate
	 *            : id for OvertimeRate
	 */
	public void setIdOvertimeRate(int idOvertimeRate) {
		this.idOvertimeRate = idOvertimeRate;
	}

	/**
	 * @return listaTipoTrabajador: List of type worker.
	 */
	public List<SelectItem> getListaTipoTrabajador() {
		return listaTipoTrabajador;
	}

	/**
	 * @param listaTipoTrabajador
	 *            : List of type worker.
	 */
	public void setListaTipoTrabajador(List<SelectItem> listaTipoTrabajador) {
		this.listaTipoTrabajador = listaTipoTrabajador;
	}

	/**
	 * @return listOvertimePaymentRate: list of over time payments rate
	 */
	public List<SelectItem> getListOvertimePaymentRate() {
		return listOvertimePaymentRate;
	}

	/**
	 * @param listOvertimePaymentRate
	 *            : list of over time payments rate
	 */
	public void setListOvertimePaymentRate(
			List<SelectItem> listOvertimePaymentRate) {
		this.listOvertimePaymentRate = listOvertimePaymentRate;
	}

	/**
	 * @return trabajadores: List type worker.
	 */
	public List<Hr> getTrabajadores() {
		return trabajadores;
	}

	/**
	 * @param trabajadores
	 *            : List type worker.
	 */
	public void setTrabajadores(List<Hr> trabajadores) {
		this.trabajadores = trabajadores;
	}

	/**
	 * @return trabajadoresSeleccionados: list of selected workers
	 */
	public List<Hr> getTrabajadoresSeleccionados() {
		return trabajadoresSeleccionados;
	}

	/**
	 * @param trabajadoresSeleccionados
	 *            : list of selected workers
	 */
	public void setTrabajadoresSeleccionados(List<Hr> trabajadoresSeleccionados) {
		this.trabajadoresSeleccionados = trabajadoresSeleccionados;
	}

	/**
	 * @return listaActivitiesAndHrTemp: list of the relationship between
	 *         activity and human resources
	 */
	public List<ActivitiesAndHr> getListaActivitiesAndHrTemp() {
		return listaActivitiesAndHrTemp;
	}

	/**
	 * @param listaActivitiesAndHrTemp
	 *            : list of the relationship between activity and human
	 *            resources
	 */
	public void setListaActivitiesAndHrTemp(
			List<ActivitiesAndHr> listaActivitiesAndHrTemp) {
		this.listaActivitiesAndHrTemp = listaActivitiesAndHrTemp;
	}

	/**
	 * @return listaActivitiesAndHr: list of the relationship between activity
	 *         and human resources
	 */
	public List<ActivitiesAndHr> getListaActivitiesAndHr() {
		return listaActivitiesAndHr;
	}

	/**
	 * @param listaActivitiesAndHr
	 *            : list of the relationship between activity and human
	 *            resources
	 */
	public void setListaActivitiesAndHr(
			List<ActivitiesAndHr> listaActivitiesAndHr) {
		this.listaActivitiesAndHr = listaActivitiesAndHr;
	}

	/**
	 * @return activitiesAndHrPK: primary key activitiesAndHr
	 */
	public ActivitiesAndHrPK getActivitiesAndHrPK() {
		return activitiesAndHrPK;
	}

	/**
	 * @param activitiesAndHrPK
	 *            : primary key activitiesAndHr
	 */
	public void setActivitiesAndHrPK(ActivitiesAndHrPK activitiesAndHrPK) {
		this.activitiesAndHrPK = activitiesAndHrPK;
	}

	/**
	 * @return trabajador: Object type of worker.
	 */
	public Hr getTrabajador() {
		return trabajador;
	}

	/**
	 * @param trabajador
	 *            : Object type of worker.
	 */
	public void setTrabajador(Hr trabajador) {
		this.trabajador = trabajador;
	}

	/**
	 * @return activities: activity performed.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : activity performed.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return actividadSeleccionada: activities object.
	 */
	public Activities getActividadSeleccionada() {
		return actividadSeleccionada;
	}

	/**
	 * @param actividadSeleccionada
	 *            : activities object.
	 */
	public void setActividadSeleccionada(Activities actividadSeleccionada) {
		this.actividadSeleccionada = actividadSeleccionada;
	}

	/**
	 * @return activitiesAndHr: Object of the relationship between the
	 *         activities and human resources.
	 */
	public ActivitiesAndHr getActivitiesAndHr() {
		return activitiesAndHr;
	}

	/**
	 * @param activitiesAndHr
	 *            : Object of the relationship between the activities and human
	 *            resources.
	 */
	public void setActivitiesAndHr(ActivitiesAndHr activitiesAndHr) {
		this.activitiesAndHr = activitiesAndHr;
	}

	/**
	 * @return paginador: Management paged list of activities.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of activities.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return mensaje: validation message.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            : validation message.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return messageWorkersAvailability: state responsible for displaying
	 *         message availability of workers
	 */
	public String getMessageWorkersAvailability() {
		return messageWorkersAvailability;
	}

	/**
	 * @param messageWorkersAvailability
	 *            : state responsible for displaying message availability of
	 *            workers
	 */
	public void setMessageWorkersAvailability(String messageWorkersAvailability) {
		this.messageWorkersAvailability = messageWorkersAvailability;
	}

	/**
	 * @return paginadorTrabajador: Management paged list of workerd.
	 */
	public Paginador getPaginadorTrabajador() {
		return paginadorTrabajador;
	}

	/**
	 * @param paginadorTrabajador
	 *            : Management paged list of workerd.
	 */
	public void setPaginadorTrabajador(Paginador paginadorTrabajador) {
		this.paginadorTrabajador = paginadorTrabajador;
	}

	/**
	 * @return paginadorActivitiesAndHr: Pager relationship and human resources
	 *         activities.
	 */
	public Paginador getPaginadorActivitiesAndHr() {
		return paginadorActivitiesAndHr;
	}

	/**
	 * @param paginadorActivitiesAndHr
	 *            : Pager relationship and human resources activities.
	 */
	public void setPaginadorActivitiesAndHr(Paginador paginadorActivitiesAndHr) {
		this.paginadorActivitiesAndHr = paginadorActivitiesAndHr;
	}

	/**
	 * @return validacionTrabajador: handles the validation status of workers.
	 */
	public boolean isValidacionTrabajador() {
		return validacionTrabajador;
	}

	/**
	 * @param validacionTrabajador
	 *            : handles the validation status of workers.
	 */
	public void setValidacionTrabajador(boolean validacionTrabajador) {
		this.validacionTrabajador = validacionTrabajador;
	}

	/**
	 * @return estadoMensaje: status to check if the message is displayed
	 *         certificated staff
	 */
	public boolean isEstadoMensaje() {
		return estadoMensaje;
	}

	/**
	 * @param estadoMensaje
	 *            : status to check if the message is displayed certificated
	 *            staff
	 */
	public void setEstadoMensaje(boolean estadoMensaje) {
		this.estadoMensaje = estadoMensaje;
	}

	/**
	 * @return actividadCertificada: status to check if the message is displayed
	 *         certified activity
	 */
	public boolean isActividadCertificada() {
		return actividadCertificada;
	}

	/**
	 * @param actividadCertificada
	 *            : status to check if the message is displayed certified
	 *            activity
	 */
	public void setActividadCertificada(boolean actividadCertificada) {
		this.actividadCertificada = actividadCertificada;
	}

	/**
	 * @return reportingActuals: status to check if this interface is the
	 *         actuals reporting
	 */
	public boolean isReportingActuals() {
		return reportingActuals;
	}

	/**
	 * @param reportingActuals
	 *            : status to check if this interface is the actuals reporting
	 */
	public void setReportingActuals(boolean reportingActuals) {
		this.reportingActuals = reportingActuals;
	}

	/**
	 * @return workHoursValid: determines whether the human resource hours
	 *         allocated to meet the validation or not.
	 */
	public boolean isWorkHoursValid() {
		return workHoursValid;
	}

	/**
	 * @param workHoursValid
	 *            : determines whether the human resource hours allocated to
	 *            meet the validation or not..
	 */
	public void setWorkHoursValid(boolean workHoursValid) {
		this.workHoursValid = workHoursValid;
	}

	/**
	 * It is responsible for initializing the parameters for the search of
	 * workers.
	 */
	public void inicializarTrabajadores() {
		paginadorTrabajador = new Paginador();
		consultarTrabajador();
	}

	/**
	 * Consult the list of workers required.
	 */
	public void consultarTrabajador() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.estadoMensaje = false;
		this.actividadCertificada = false;
		this.trabajadores = new ArrayList<Hr>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			Long actividadesCertificadas = activitiesDao
					.queryCertifiedActivities(actividadSeleccionada
							.getIdActivity());
			Long hrCertificados = hrDao.consultarHrCertificados(
					actividadSeleccionada.getIdActivity(), this.idTrabajador);
			Long searchHrCertifiedAndMaternity = hrDao
					.hrCertifiedAndMaternityAmount(
							actividadSeleccionada.getIdActivity(),
							this.idTrabajador);
			busquedaAvanzadaTrabajador(consulta, parametros,
					actividadesCertificadas, hrCertificados,
					searchHrCertifiedAndMaternity);
			Long cantidad = hrDao.hrAmount(consulta, parametros);
			if (cantidad != null) {
				if (cantidad > 5) {
					paginadorTrabajador.paginarRangoDefinido(cantidad, 5);
				} else {
					paginadorTrabajador.paginar(cantidad);
				}
				this.trabajadores = hrDao.queryHr(
						paginadorTrabajador.getInicio(),
						paginadorTrabajador.getRango(), consulta, parametros);
			}
			if ((trabajadores == null || trabajadores.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (trabajadores == null || trabajadores.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("recurso_humano_label"),
								unionMensajesBusqueda);
			}
			if (trabajadores != null)
				mantenerTrabajadores();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for workers assembling
	 * also allows messages to be displayed depending on the search criteria
	 * selected by the user.
	 * 
	 * @modify Cristhian.Pico
	 * 
	 * @param consulta
	 *            : query to concatenate
	 * @param parametros
	 *            : list of search parameters.
	 * @param actividadesCertificadas
	 *            : list of the certifications required to perform the selected
	 *            activity
	 * @param hrCertificados
	 *            : list of the human resources who have the certifications
	 *            required to perform the selected activity
	 * @param searchHrCertifiedAndMaternity
	 *            : list of the human resources who have the certifications
	 *            required to perform the selected activity but are on maternity
	 *            or breast feeding period
	 */
	private void busquedaAvanzadaTrabajador(StringBuilder consulta,
			List<SelectItem> parametros, Long actividadesCertificadas,
			Long hrCertificados, Long searchHrCertifiedAndMaternity) {
		this.messageWorkersAvailability = null;
		Date fechaMinima = ControladorFechas.restarAnyos(
				ControladorFechas.fechaActual(),
				Constantes.EDAD_MINIMA_ACTIVIDAD_PELIGROSA);
		boolean seleccion = false;
		consulta.append("JOIN h.hrTypes ht ");
		if (this.idTrabajador != 0) {
			consulta.append("WHERE ht.idHrType = :idTrabajador ");
			SelectItem itemHrType = new SelectItem(this.idTrabajador,
					"idTrabajador");
			parametros.add(itemHrType);
			seleccion = true;
		}
		consulta.append(seleccion ? "AND " : "WHERE ");
		consulta.append("h NOT IN ");
		consulta.append("(SELECT h FROM ActivitiesAndHr ah ");
		consulta.append("JOIN ah.activitiesAndHrPK ahp ");
		consulta.append("JOIN ahp.hr h ");
		consulta.append("WHERE ah.initialDateTimeBudget BETWEEN :fechaInicial AND :fechaFinal ");
		consulta.append("OR ah.initialDateTimeActual BETWEEN :fechaInicial AND :fechaFinal ");
		consulta.append("OR ah.finalDateTimeBudget BETWEEN :fechaInicial AND :fechaFinal ");
		consulta.append("OR ah.finalDateTimeActual BETWEEN :fechaInicial AND :fechaFinal) ");
		if (actividadesCertificadas != null && actividadesCertificadas > 0) {
			consulta.append("AND h IN ");
			consulta.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
			consulta.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
			consulta.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
			consulta.append("WHERE ca IN ");
			consulta.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
			consulta.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
			consulta.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
			consulta.append("WHERE aca.idActivity = :actividadSeleccion )) ");
			SelectItem itemActividadSeleccion = new SelectItem(
					this.actividadSeleccionada.getIdActivity(),
					"actividadSeleccion");
			parametros.add(itemActividadSeleccion);
			this.messageWorkersAvailability = Constantes.STATE_CERTIFIED;
			this.actividadCertificada = true;
			if (hrCertificados > 0) {
				this.estadoMensaje = true;
				this.messageWorkersAvailability = Constantes.STATE_HR_CERTIFIED;
			}
		}
		if (actividadSeleccionada.getDangerous() != null
				&& actividadSeleccionada.getDangerous()) {
			consulta.append("AND h.birthDate < :fechaMinima ");
			consulta.append("AND h.maternityBreastFeeding = false  ");
			SelectItem itemFechaMinima = new SelectItem(fechaMinima,
					"fechaMinima");
			parametros.add(itemFechaMinima);
			if (searchHrCertifiedAndMaternity > 0) {
				this.messageWorkersAvailability = Constantes.STATE_CERTIFIED_MATERNITY;
			}
		}
		SelectItem itemInicial = new SelectItem(
				this.actividadSeleccionada.getInitialDtBudget(), "fechaInicial");
		SelectItem itemFinal = new SelectItem(
				this.actividadSeleccionada.getFinalDtBudget(), "fechaFinal");
		parametros.add(itemInicial);
		parametros.add(itemFinal);
	}

	/**
	 * load the OvertimePaymentsRate list.
	 * 
	 * @throws Exception
	 */
	private void loadOvertimePaymentRate() throws Exception {
		this.listOvertimePaymentRate = new ArrayList<SelectItem>();
		List<OvertimePaymentRate> overtimePaymentsRate = overtimePaymentRateDao
				.listOvertimePaymentRate();
		if (overtimePaymentsRate != null) {
			for (OvertimePaymentRate overtimePaymentRate : overtimePaymentsRate) {
				listOvertimePaymentRate.add(new SelectItem(overtimePaymentRate
						.getOvertimepaymentid(), overtimePaymentRate
						.getOvertimeRateType()));
			}
		}
	}

	/**
	 * It allows charging workers available on a list of SelectItem type.
	 * 
	 */
	public void mostrarTipoTrabajador() {
		try {
			idTrabajador = 0;
			listaActivitiesAndHr = new ArrayList<ActivitiesAndHr>();
			trabajadoresSeleccionados = new ArrayList<Hr>();
			consultarTrabajador();
			trabajadoresSeleccionados = new ArrayList<Hr>();
			listaTipoTrabajador = new ArrayList<SelectItem>();
			List<HrTypes> tipoTrabajador = hrTypeDao.queryHrTypes();
			if (tipoTrabajador != null) {
				for (HrTypes tipo : tipoTrabajador) {
					listaTipoTrabajador.add(new SelectItem(tipo.getIdHrType(),
							tipo.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Adds a list of workers who are selected validating age and time of
	 * motherhood.
	 * 
	 * @param trabajadorSeleccionado
	 *            : Human Resource object type.
	 */
	public void seleccionarTrabajador(Hr trabajadorSeleccionado) {
		this.mensaje = "";
		this.trabajador = new Hr();
		activitiesAndHrPK = new ActivitiesAndHrPK();
		activitiesAndHr = new ActivitiesAndHr();
		validacionTrabajador = false;
		try {
			trabajador = trabajadorSeleccionado;
			if (!trabajadorSeleccionado.isSeleccionado()) {
				activitiesAndHr.setDurationBudget(actividadSeleccionada
						.getDurationBudget());
			} else {
				ActivitiesAndHr actividadesHr = new ActivitiesAndHr();
				for (ActivitiesAndHr actividadHr : listaActivitiesAndHr) {
					int actividadIdHr = actividadHr.getActivitiesAndHrPK()
							.getHr().getIdHr();
					int idActivity = actividadHr.getActivitiesAndHrPK()
							.getActivities().getIdActivity();
					if (actividadIdHr == trabajador.getIdHr()
							&& actividadSeleccionada.getIdActivity() == idActivity) {
						actividadesHr = actividadHr;
					}
				}
				listaActivitiesAndHr.remove(actividadesHr);
				trabajador.setSeleccionado(false);
				validacionTrabajador = true;
				trabajadoresSeleccionados.remove(trabajador);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for adding workers are selected to list workers and
	 * selected the list of activities and resources humans.
	 */
	public void agregarTrabajadores() {
		try {
			OvertimePaymentRate overTimePaymentRate = overtimePaymentRateDao
					.overtimePaymentRateXDefaultRate(true);
			trabajador.setSeleccionado(true);
			Double costNormalHours = trabajador.getHourCost()
					* activitiesAndHr.getNormalHours();
			Double costOvertimeHours = trabajador.getHourCostOvertime()
					* activitiesAndHr.getOvertimeHours()
					* overTimePaymentRate.getOvertimeRateRatio();
			Double totalCostBudget = Math
					.round((costNormalHours + costOvertimeHours) * 10.0) / 10.0;
			activitiesAndHrPK.setHr(trabajador);
			activitiesAndHrPK.setActivities(actividadSeleccionada);
			activitiesAndHr.setActivitiesAndHrPK(activitiesAndHrPK);
			activitiesAndHr.setInitialDateTimeBudget(actividadSeleccionada
					.getInitialDtBudget());
			activitiesAndHr.setFinalDateTimeBudget(actividadSeleccionada
					.getFinalDtBudget());
			activitiesAndHr.setTotalCostBudget(totalCostBudget);
			activitiesAndHr.setOvertimePaymentRate(overTimePaymentRate);
			listaActivitiesAndHr.add(activitiesAndHr);
			trabajadoresSeleccionados.add(trabajador);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for maintaining selected workers regardless of whether
	 * they workers run the search again.
	 */
	private void mantenerTrabajadores() {
		for (Hr trabajador : trabajadores) {
			for (Hr trabajadorSeleccionado : trabajadoresSeleccionados) {
				if (trabajador.getIdHr() == trabajadorSeleccionado.getIdHr()) {
					trabajador.setSeleccionado(true);
				}
			}
		}
	}

	/**
	 * Save the relationship between activities and human resources.
	 * 
	 */
	public void crearListaActivitiesAndHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		double costHrBudget = 0;
		try {
			if (actividadSeleccionada.getCostHrBudget() == null)
				actividadSeleccionada.setCostHrBudget(new Double(0));
			if (this.listaActivitiesAndHr != null
					&& this.listaActivitiesAndHr.size() > 0) {
				for (ActivitiesAndHr actividadAndHr : listaActivitiesAndHr) {
					activitiesAndHrDao.saveActivitiesAndHr(actividadAndHr);
					costHrBudget = costHrBudget
							+ actividadAndHr.getTotalCostBudget();
				}
				costHrBudget = actividadSeleccionada.getCostHrBudget()
						+ costHrBudget;
				actividadSeleccionada.setCostHrBudget(costHrBudget);
				activitiesDao.editActivities(this.actividadSeleccionada);
				setListaActivitiesAndHr(null);
				consultarActivitiesAndHrXActividad();
			} else {
				OvertimePaymentRate overtimePaymentRate = overtimePaymentRateDao
						.overtimePaymentRateXId(idOvertimeRate);
				Double costNormalHours = activitiesAndHr.getNormalHours()
						* activitiesAndHr.getActivitiesAndHrPK().getHr()
								.getHourCost();
				Double costOvertimeHours = activitiesAndHr.getOvertimeHours()
						* activitiesAndHr.getActivitiesAndHrPK().getHr()
								.getHourCostOvertime()
						* overtimePaymentRate.getOvertimeRateRatio();
				Double totalCostBudget = costNormalHours + costOvertimeHours;
				ActivitiesAndHr activitiesAndHrAnterior = activitiesAndHrDao
						.activitiesAndHrById(activitiesAndHr
								.getActivitiesAndHrPK());
				activitiesAndHr.setTotalCostBudget(Math
						.round(totalCostBudget * 10.0) / 10.0);
				activitiesAndHr.setOvertimePaymentRate(overtimePaymentRate);
				Double costHr = (actividadSeleccionada.getCostHrBudget() - activitiesAndHrAnterior
						.getTotalCostBudget())
						+ activitiesAndHr.getTotalCostBudget();
				actividadSeleccionada.setCostHrBudget(costHr);
				activitiesAndHrDao.editActivitiesAndHr(activitiesAndHr);
				activitiesDao.editActivities(actividadSeleccionada);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(mensajeRegistro),
								activitiesAndHr.getActivitiesAndHrPK().getHr()
										.getName()));
				consultarActivitiesAndHrXActividad();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Check the relationship between activities and human resources
	 * 
	 * @modify 12/01/2016 Wilhelm.Boada
	 * 
	 */
	public void consultarActivitiesAndHrXActividad() {
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listaActivitiesAndHrTemp = new ArrayList<ActivitiesAndHr>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		String mensajeBusqueda = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			if (desdeModal) {
				this.actividadSeleccionada = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			busquedaAvanzadaActivitiesAndHr(consulta, parametros);
			Long cantidad = activitiesAndHrDao.amountActivitiesAndHr(
					consulta, parametros);
			busquedaAvanzadaActivitiesAndHr(consulta, parametros);
			if (cantidad != null) {
				if (cantidad > 5) {
					paginadorActivitiesAndHr.paginarRangoDefinido(cantidad, 5);
				} else {
					paginadorActivitiesAndHr.paginar(cantidad);
				}
				this.listaActivitiesAndHrTemp = activitiesAndHrDao
						.queryActivitiesAndHr(
								paginadorActivitiesAndHr.getInicio(),
								paginadorActivitiesAndHr.getRango(), consulta,
								parametros);
				if (param2 != null && (desdeModal || param2.equals("mostrar"))) {
					recordActivitiesActualsAction
							.setListActivitiesAndHr(listaActivitiesAndHrTemp);
					recordActivitiesActualsAction
							.setCalculateCostsButtonActivated(false);
				}
			}
			loadOvertimePaymentRate();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for relationship
	 * between activities and human resources, also it allows messages to build
	 * show depending on the search criteria selected by the user.
	 * 
	 * @param consulta
	 *            : query to concatenate
	 * @param parametros
	 *            : list of the search parameters.
	 */
	private void busquedaAvanzadaActivitiesAndHr(StringBuilder consulta,
			List<SelectItem> parametros) {
		boolean seleccion = false;
		if (consulta.length() > 0) {
			consulta.setLength(0);
			seleccion = true;
		}
		consulta.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consulta.append("ah.activitiesAndHrPK.hr h ");
		consulta.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consulta.append("ah.activitiesAndHrPK.activities ac ");
		consulta.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consulta.append("ah.overtimePaymentRate op ");
		consulta.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(
				this.actividadSeleccionada.getIdActivity(), "id");
		parametros.add(item);
	}

	/**
	 * It is responsible for validating that the time duration entered does not
	 * exceed the total time of the activity and that the guidelines are met
	 * workload
	 * 
	 * @modify 28/08/2015 Cristhian.Pico
	 * 
	 * @param context
	 *            : Context of sight.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validar(FacesContext context, UIComponent toValidate,
			Object value) {
		int idHr;
		String clientId = toValidate.getClientId(context);
		String modal = (String) toValidate.getAttributes().get("var");
		boolean desdeModal = (modal != null && Constantes.SI.equals(modal)) ? true
				: false;
		this.workHoursValid = true;
		Double duracion = (Double) value;
		Double duracionActividad;
		if (!desdeModal) {
			duracionActividad = (Double) ControladorFechas.restarFechas(
					actividadSeleccionada.getInitialDtBudget(),
					actividadSeleccionada.getFinalDtBudget());
		} else {
			duracionActividad = (Double) ControladorFechas.restarFechas(
					activitiesAndHr.getInitialDateTimeBudget(),
					activitiesAndHr.getFinalDateTimeBudget());
		}
		try {
			if (duracion > 0) {
				if (duracion.compareTo(duracionActividad) > 0) {
					String mensaje = "message_duracion_actividad";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensaje, "mensaje");
					((UIInput) toValidate).setValid(false);
				} else {
					if (activitiesAndHr.getActivitiesAndHrPK() != null) {
						idHr = this.activitiesAndHr.getActivitiesAndHrPK()
								.getHr().getIdHr();
					} else {
						idHr = this.trabajador.getIdHr();
					}
					validarWorkLoad(duracion, idHr, false);
					if (!this.workHoursValid) {
						String mensaje = "message_overtime_week";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								mensaje, "mensaje");
						((UIInput) toValidate).setValid(false);
					}
				}
			} else {
				String mensaje = "message_duration_mayor_cero";
				ControladorContexto.mensajeErrorEspecifico(clientId, mensaje,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method is responsible for validating the hours assigned to the
	 * worker This activity does not exceed the hours of weekly overtime
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param duracionHrActividad
	 *            : Value entered by the user as long as the human resource work
	 *            on that activity.
	 * @param humanReosurceId
	 *            : human resource id assigned to the activity.
	 * @param var
	 *            : variable that indicates whether the user is editing a record
	 *            or not.
	 * 
	 * @throws Exception
	 */
	public void validarWorkLoad(Double duracionHrActividad,
			int humanReosurceId, boolean var) throws Exception {
		Date activityDate = this.actividadSeleccionada.getInitialDtBudget();
		Date mindDateTime = ControladorFechas.diaInicialSemana(activityDate);
		Date maxDateTime = ControladorFechas.diaFinalSemana(activityDate);
		try {
			Double overtimeWeek = activitiesAndHrDao.calculateOverTimeHours(
					humanReosurceId, mindDateTime, maxDateTime,
					this.actividadSeleccionada.getIdActivity());
			Double workedHoursDay = activitiesAndHrDao.calculateNormalHours(
					humanReosurceId, activityDate,
					this.actividadSeleccionada.getIdActivity());
			if (duracionHrActividad <= (8 - workedHoursDay)) {
				this.activitiesAndHr.setNormalHours(duracionHrActividad);
				this.activitiesAndHr.setOvertimeHours(0.0);
			} else {
				Double activityNormalHours = (8 - workedHoursDay);
				this.activitiesAndHr.setNormalHours(activityNormalHours);
				if ((overtimeWeek + duracionHrActividad - activityNormalHours) <= 12) {
					this.activitiesAndHr
							.setOvertimeHours(Math
									.round((duracionHrActividad - activityNormalHours) * 10.0) / 10.0);
				} else {
					Double overtimeActivity = 12 - overtimeWeek;
					this.activitiesAndHr.setOvertimeHours(Math
							.round(overtimeActivity * 10.0) / 10.0);
					if (!var) {
						this.activitiesAndHr
								.setDurationBudget(activityNormalHours
										+ overtimeActivity);
					} else {
						this.activitiesAndHr
								.setDurationActual(activityNormalHours
										+ overtimeActivity);
					}
					this.setWorkHoursValid(false);
				}
			}
			this.activitiesAndHr
					.setTotalHours(this.activitiesAndHr.getNormalHours()
							+ this.activitiesAndHr.getOvertimeHours());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Deletes a relationship of activity and human resource data base.
	 * 
	 */
	public void eliminarActivitiesAndHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_eliminar";
		try {
			activitiesAndHrDao.deleteActivitiesAndHr(activitiesAndHr);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(message), activitiesAndHr
							.getActivitiesAndHrPK().getHr().getName()));
			this.actividadSeleccionada
					.setCostHrBudget(this.actividadSeleccionada
							.getCostHrBudget()
							- activitiesAndHr.getTotalCostBudget());
			activitiesDao.editActivities(this.actividadSeleccionada);
			consultarActivitiesAndHrXActividad();
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activitiesAndHr.getActivitiesAndHrPK().getHr().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It will calculate the length considering the difference two dates
	 */
	public void calculateDuration() {
		try {
			Double durationBudget = ControladorFechas.restarFechas(
					activitiesAndHr.getInitialDateTimeBudget(),
					activitiesAndHr.getFinalDateTimeBudget());
			int idHr = activitiesAndHr.getActivitiesAndHrPK().getHr().getIdHr();
			activitiesAndHr.setDurationBudget(durationBudget);
			validarWorkLoad(durationBudget, idHr, false);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}
