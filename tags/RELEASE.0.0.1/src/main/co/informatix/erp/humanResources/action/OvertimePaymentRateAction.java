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
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.OvertimePaymentRateDao;
import co.informatix.erp.humanResources.entities.OvertimePaymentRate;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of overtime payment rate who can be in the
 * database. The logic is to see, edit, add or remove clients.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class OvertimePaymentRateAction implements Serializable {

	@EJB
	private OvertimePaymentRateDao overtimePaymentRateDao;

	private List<OvertimePaymentRate> listOvertimePayments;

	private OvertimePaymentRate overtimePaymentRate;
	private OvertimePaymentRate overtimePaymentDefault;
	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	/**
	 * @return listOvertimePaymentes: overtime payment rate list
	 */
	public List<OvertimePaymentRate> getListOvertimePayments() {
		return listOvertimePayments;
	}

	/**
	 * @param listOvertimePayments
	 *            :overtime payment rate list
	 */
	public void setListOvertimePayments(
			List<OvertimePaymentRate> listOvertimePayments) {
		this.listOvertimePayments = listOvertimePayments;
	}

	/**
	 * @return overtimePaymentRate: gets the overtime payment rate record
	 */
	public OvertimePaymentRate getOvertimePaymentRate() {
		return overtimePaymentRate;
	}

	/**
	 * @param overtimePaymentRate
	 *            :sets the overtime payment rate record
	 */
	public void setOvertimePaymentRate(OvertimePaymentRate overtimePaymentRate) {
		this.overtimePaymentRate = overtimePaymentRate;
	}

	/**
	 * @return overtimePaymentDefault: overtime payment rate object default
	 */
	public OvertimePaymentRate getOvertimePaymentDefault() {
		return overtimePaymentDefault;
	}

	/**
	 * @param overtimePaymentDefault
	 *            :overtime payment rate object default
	 */
	public void setOvertimePaymentDefault(
			OvertimePaymentRate overtimePaymentDefault) {
		this.overtimePaymentDefault = overtimePaymentDefault;
	}

	/**
	 * @return paginador: Management paged list of overtime payment rate.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paged list of overtime payment rate.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: overtime payment rate name search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :overtime payment rate name search.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of overtime payment rate
	 * 
	 * @return consultOvertimePaymentRate: query method that overtime payment
	 *         rate return to the template management.
	 */
	public String initializingSearch() {
		nombreBuscar = "";
		return consultOvertimePaymentRate();
	}

	/**
	 * Consult the list of overtime payment rate in the data base
	 * 
	 * @modify Cristhian.Pico
	 * 
	 * @return "gesOvertimePayment": redirects to the template to manage of
	 *         overtime payment rate.
	 */
	public String consultOvertimePaymentRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle mensajeRH = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listOvertimePayments = new ArrayList<OvertimePaymentRate>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMensajesBusqueda);
			Long amount = overtimePaymentRateDao.amountOvertimePaymentRate(
					consult, parameters);
			if (amount != null) {
				paginador.paginar(amount);
			}
			List<OvertimePaymentRate> overtimePaymentsTemp = overtimePaymentRateDao
					.consultOvertimePaymentRate(paginador.getInicio(),
							paginador.getRango(), consult, parameters);
			for (OvertimePaymentRate otPayment : overtimePaymentsTemp) {
				double paymentRatio = otPayment.getOvertimeRateRatio();
				otPayment
						.setOvertimeRateRatio(Math.round(paymentRatio * 10.0) / 10.0);
				this.listOvertimePayments.add(otPayment);
			}
			if ((this.listOvertimePayments == null || this.listOvertimePayments
					.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (this.listOvertimePayments == null
					|| this.listOvertimePayments.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								mensajeRH
										.getString("overtime_payment_rate_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesOvertimePayment";
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(op.overtimeRateType) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new overtime payment rate.
	 * 
	 * @param overtimePaymentRate
	 *            :overtime payment rate that you are adding or editing
	 * 
	 * @return "regOvertimePayment":redirects the overtime payment rate record
	 *         template.
	 */
	public String addEditOvertimePayment(OvertimePaymentRate overtimePaymentRate) {
		if (overtimePaymentRate != null) {
			this.overtimePaymentRate = overtimePaymentRate;
		} else {
			this.overtimePaymentRate = new OvertimePaymentRate();
		}
		return "regOvertimePayment";
	}

	/**
	 * Method allows assign the current overtime payment rate to set a default
	 * 
	 * @param overtimePaymentRate
	 *            : current overtime payment rate selected by the user
	 */
	public void assignOvertimePayment(OvertimePaymentRate overtimePaymentRate) {
		overtimePaymentRate.setByDefault(true);
		try {
			this.overtimePaymentRateDao
					.editOvertimePaymentRate(overtimePaymentRate);
			for (OvertimePaymentRate overtimePayment : this.listOvertimePayments) {
				if (overtimePayment != overtimePaymentRate
						&& overtimePayment.getByDefault() == true) {
					overtimePayment.setByDefault(false);
					this.overtimePaymentRateDao
							.editOvertimePaymentRate(overtimePayment);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * Method used to save or edit overtime payment rate
	 * 
	 * @return consultOvertimePaymentRate: Redirects overtime payment rate to
	 *         manage the list of clients updated
	 */
	public String saveOvertimePaymentRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (this.overtimePaymentRate.getOvertimepaymentid() != 0) {
				this.overtimePaymentRateDao
						.editOvertimePaymentRate(this.overtimePaymentRate);
			} else {
				mensajeRegistro = "message_registro_guardar";
				this.overtimePaymentRate.setByDefault(false);
				this.overtimePaymentRateDao
						.saveOvertimePaymentRate(this.overtimePaymentRate);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					this.overtimePaymentRate.getOvertimeRateType()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultOvertimePaymentRate();
	}

	/**
	 * Method for removing a overtime payment rate from the database.
	 * 
	 * @return consultOvertimePaymentRate: navigation rule and redirect to
	 *         template to manages overtime payment rate.
	 */
	public String deleteOvertimePaymentRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			overtimePaymentRateDao
					.deleteOvertimePaymentRate(overtimePaymentRate);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.overtimePaymentRate.getOvertimeRateType()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					overtimePaymentRate.getOvertimeRateType());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultOvertimePaymentRate();
	}

}
