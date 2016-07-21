package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.PluviometerDao;
import co.informatix.erp.lifeCycle.entities.Pluviometer;
import co.informatix.erp.lifeCycle.entities.PluviometerPojo;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the rain gauge readings that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PluviometerAction implements Serializable {

	@EJB
	private PluviometerDao pluviometerDao;

	private Paginador pagination = new Paginador();
	private Pluviometer pluviometer;
	private PluviometerPojo pluviometerPojo;
	private Date initialDateSearch;
	private Date finalDateSearch;
	private List<PluviometerPojo> pluviometerPojoList;
	private List<PluviometerPojo> pluviometerPojoSubList;
	private List<Integer> readingList;
	private boolean flag;

	/**
	 * @return pagination: Management paged list of rain gauge readings.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of rain gauge readings.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return pluviometer: Object of class rain gauge readings for register o
	 *         edit.
	 */
	public Pluviometer getPluviometer() {
		return pluviometer;
	}

	/**
	 * @param pluviometer
	 *            : Object of class rain gauge readings for register o edit.
	 */
	public void setPluviometer(Pluviometer pluviometer) {
		this.pluviometer = pluviometer;
	}

	/**
	 * @return pluviometerPojo: Object having information of readings taken in a
	 *         week.
	 */
	public PluviometerPojo getPluviometerPojo() {
		return pluviometerPojo;
	}

	/**
	 * @param pluviometerPojo
	 *            : Object having information of readings taken in a week.
	 */
	public void setPluviometerPojo(PluviometerPojo pluviometerPojo) {
		this.pluviometerPojo = pluviometerPojo;
	}

	/**
	 * @return initialDateSearch: gets the date of the reading to search in a
	 *         range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            : gets the date of the reading to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the date of the reading to search in a
	 *         range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            : gets the date of the reading to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return pluviometerPojoList : pluviometerPojo list containing dates
	 *         introduced grouped by weeks.
	 */
	public List<PluviometerPojo> getPluviometerPojoList() {
		return pluviometerPojoList;
	}

	/**
	 * @param pluviometerPojoList
	 *            : pluviometerPojo list containing dates introduced grouped by
	 *            weeks.
	 */
	public void setPluviometerPojoList(List<PluviometerPojo> pluviometerPojoList) {
		this.pluviometerPojoList = pluviometerPojoList;
	}

	/**
	 * @return pluviometerPojoList : pluviometerPojo list containing dates
	 *         introduced grouped by weeks to show in the view.
	 */
	public List<PluviometerPojo> getPluviometerPojoSubList() {
		return pluviometerPojoSubList;
	}

	/**
	 * @param pluviometerPojoSubList
	 *            : pluviometerPojo list containing dates introduced grouped by
	 *            weeks to show in the view.
	 */
	public void setPluviometerPojoSubList(
			List<PluviometerPojo> pluviometerPojoSubList) {
		this.pluviometerPojoSubList = pluviometerPojoSubList;
	}

	/**
	 * @return readingList: reading list for edit or save.
	 */
	public List<Integer> getReadingList() {
		return readingList;
	}

	/**
	 * @param readingList
	 *            : reading list for edit or save.
	 */
	public void setReadingList(List<Integer> readingList) {
		this.readingList = readingList;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultPluviometer: Method that consultation rain gauge readings
	 *         and load the template with the information found.
	 */
	public String initializePluviometer() {
		pluviometerPojoList = new ArrayList<PluviometerPojo>();
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		pagination = new Paginador();
		pluviometerPojo = null;
		flag = false;
		return consultPluviometer();
	}

	/**
	 * Method to edit or create a new assignment of rain gauge readings.
	 * 
	 * @param pluviometer
	 *            :Object of pluviometer are adding or editing.
	 * 
	 * @return regPluviometer: Template redirects to register rain gauge
	 *         reading.
	 * 
	 */
	public String addEditPluviometer(Pluviometer pluviometer) {
		if (pluviometer != null) {
			this.pluviometer = pluviometer;
		} else {
			this.pluviometer = new Pluviometer();
		}
		return "regPluviometer";
	}

	/**
	 * See the existing rain gauge readings list.
	 * 
	 * @return gesPluviometer: Navigation rule that redirects to manage the rain
	 *         gauge reading.
	 */
	public String consultPluviometer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		pluviometerPojoSubList = new ArrayList<PluviometerPojo>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		try {
			advancedSearch(consult, parameters, bundle, unionSearchMessages);
			if (!flag) {
				flag = true;
				List<Pluviometer> pluviometerList = pluviometerDao
						.consultPluviometer(consult, parameters);
				pluviometerPojoList = new ArrayList<PluviometerPojo>();
				for (Pluviometer pluviometer : pluviometerList) {
					Date date = pluviometer.getDateRecord();
					if (pluviometerPojo == null
							|| !((date.after(pluviometerPojo.getStartWeek()) && date
									.before(pluviometerPojo.getEndWeek())))) {
						pluviometerPojo = new PluviometerPojo();
						pluviometerPojo.setWeek(ControladorFechas
								.getNumberWeek(pluviometer.getDateRecord()));
						pluviometerPojo.setStartWeek(ControladorFechas
								.diaInicialSemana(pluviometer.getDateRecord()));
						pluviometerPojo.setEndWeek(ControladorFechas
								.diaFinalSemana(pluviometer.getDateRecord()));
						pluviometerPojo.setTotal(0);
						pluviometerPojo.setVector(new int[7]);
						pluviometerPojoList.add(pluviometerPojo);
					}
					int numberDay = ControladorFechas.getNumberDay(date);
					if (numberDay != 1) {
						numberDay = numberDay - 1;
					} else {
						numberDay = 7;
					}
					pluviometerPojo.setVectorPos(numberDay - 1,
							pluviometer.getReading());
					pluviometerPojo.setTotal(pluviometerPojo.getTotal()
							+ pluviometer.getReading());
				}
			}
			long amount = (long) pluviometerPojoList.size();
			pagination.paginar(amount);
			int totalReg = pagination.getRango();
			int start = pagination.getInicio();
			int rank = start + totalReg;
			if (pluviometerPojoList.size() < rank) {
				rank = pluviometerPojoList.size();
			}
			this.pluviometerPojoSubList = pluviometerPojoList.subList(start,
					rank);
			if ((pluviometerPojoList == null || pluviometerPojoList.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (pluviometerPojoList == null
					|| pluviometerPojoList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("rain_gauge_label_s"),
								unionSearchMessages);
			}
			validate.setMensajeBusqueda(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPluviometer";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		boolean flag = false;

		if (this.initialDateSearch != null || this.finalDateSearch != null) {

			if (this.initialDateSearch != null && this.finalDateSearch != null) {
				consult.append("WHERE p.dateRecord BETWEEN :initialDateSearch AND :finalDateSearch ");
				flag = true;
			}

			if (this.initialDateSearch != null && this.finalDateSearch == null) {
				consult.append(flag ? "AND " : "WHERE ");
				consult.append("p.dateRecord >= :initialDateSearch ");
				flag = true;
			}
			if (this.initialDateSearch == null && this.finalDateSearch != null) {
				consult.append(flag ? "AND " : "WHERE ");
				consult.append("p.dateRecord <= :finalDateSearch ");
			}

			if (this.initialDateSearch != null) {
				SelectItem item = new SelectItem(
						ControladorFechas.diaInicialSemana(initialDateSearch),
						"initialDateSearch");
				parameters.add(item);
				Date date = ControladorFechas
						.diaInicialSemana(initialDateSearch);
				String dateFrom = bundle.getString("label_start_date")
						+ ": "
						+ '"'
						+ ControladorFechas.formatDate(date,
								Constantes.DATE_FORMAT_MESSAGE_SIMPLE) + '"'
						+ " ";
				unionMessagesSearch.append(dateFrom);
			}

			if (this.finalDateSearch != null) {
				SelectItem item2 = new SelectItem(
						ControladorFechas.diaFinalSemana(finalDateSearch),
						"finalDateSearch");
				parameters.add(item2);
				Date date = ControladorFechas.diaFinalSemana(finalDateSearch);
				String dateTo = bundle.getString("label_end_date")
						+ ": "
						+ '"'
						+ ControladorFechas.formatDate(date,
								Constantes.DATE_FORMAT_MESSAGE_SIMPLE) + '"'
						+ " ";
				unionMessagesSearch.append(dateTo);
			}
			this.flag = false;
		}
	}

	/**
	 * Method used to save or edit rain gauge reading
	 * 
	 * @return initializePluviometer(): Redirects to manage the list of rain
	 *         gauge reading with rain gauge reading updated.
	 */
	public String saveUpdatePluviometer() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			pluviometer.setYear(ControladorFechas.getYear(pluviometer
					.getDateRecord()));
			pluviometer.setWeek(ControladorFechas.getNumberWeek(pluviometer
					.getDateRecord()));
			int numberDay = ControladorFechas.getNumberDay(pluviometer
					.getDateRecord());
			if (numberDay != 1) {
				pluviometer.setDay(numberDay - 1);
			} else {
				pluviometer.setDay(7);
			}
			if (pluviometer.getId() != 0) {
				pluviometerDao.editPluviometer(pluviometer);
			} else {
				pluviometerDao.savePluviometer(pluviometer);
				mensajeRegistro = "message_registro_guardar";
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), ControladorFechas
							.formatDate(pluviometer.getDateRecord(),
									Constantes.DATE_FORMAT_MESSAGE_SIMPLE)));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializePluviometer();
	}

	/**
	 * To validate the date reading of the pluviometer, so it is not repeated in
	 * the database and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateDate(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Date date = (Date) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = pluviometer.getId();
			Pluviometer pluviometerAux = pluviometerDao.dateExists(date, id);
			if (pluviometerAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the reading list for save or edit.
	 * 
	 * @param pluviometerPojo
	 *            : Object having information of readings taken in a week.
	 * 
	 */
	public void loadList(PluviometerPojo pluviometerPojo) {
		this.pluviometerPojo = pluviometerPojo;
		readingList = new ArrayList<Integer>();
		for (int reading : pluviometerPojo.getVector()) {
			readingList.add(reading);
		}
	}

	/**
	 * This method allows save and edit the readings of the week.
	 * 
	 */
	public String saveListPluviometer() {
		int addDay = 0;
		List<Pluviometer> pluviometerListEdit = new ArrayList<Pluviometer>();
		List<Pluviometer> pluviometerListSave = new ArrayList<Pluviometer>();
		try {
			for (int reading : readingList) {
				Date date = ControladorFechas.sumarDias(
						pluviometerPojo.getStartWeek(), addDay);
				Pluviometer pluviometer = pluviometerDao.findDate(date);
				if (pluviometer != null) {
					if (pluviometer.getReading() != reading) {
						pluviometer.setReading(reading);
						pluviometerListEdit.add(pluviometer);
					}
				} else if (reading != 0) {
					pluviometer = new Pluviometer();
					pluviometer.setReading(reading);
					pluviometer.setDateRecord(date);
					pluviometer.setYear(ControladorFechas.getYear(date));
					pluviometer.setWeek(ControladorFechas.getNumberWeek(date));
					pluviometer.setDay(addDay + 1);
					pluviometerListSave.add(pluviometer);
				}
				addDay++;
			}
			if (pluviometerListSave.size() > 0) {
				for (Pluviometer pluviometer : pluviometerListSave) {
					pluviometerDao.savePluviometer(pluviometer);
				}
			}
			if (pluviometerListEdit.size() > 0) {
				for (Pluviometer pluviometer : pluviometerListEdit) {
					pluviometerDao.editPluviometer(pluviometer);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializePluviometer();
	}
}