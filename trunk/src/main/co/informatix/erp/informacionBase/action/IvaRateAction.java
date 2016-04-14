package co.informatix.erp.informacionBase.action;

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

import co.informatix.erp.informacionBase.dao.IvaRateDao;
import co.informatix.erp.informacionBase.entities.IvaRate;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of IvaRate which may be in the database. The
 * logic is to consult, edit or add IvaRate.
 * 
 * @author Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class IvaRateAction implements Serializable {
	@EJB
	private IvaRateDao ivaRateDao;

	private IvaRate ivaRate;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	private List<IvaRate> listIvaRates;

	/**
	 * @return ivaRate: object that contains the data of iva rate.
	 */
	public IvaRate getIvaRate() {
		return ivaRate;
	}

	/**
	 * @param ivaRate
	 *            : object that contains the data of iva rate.
	 */
	public void setIvaRate(IvaRate ivaRate) {
		this.ivaRate = ivaRate;
	}

	/**
	 * @return pagination: Management paged list names iva rate.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list names iva rate.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: iva rate name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : iva rate name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listIvaRate: list of objects of iva rate.
	 */
	public List<IvaRate> getListIvaRates() {
		return listIvaRates;
	}

	/**
	 * @param listIvaRate
	 *            : list of objects of iva rate.
	 */
	public void setListIvaRates(List<IvaRate> listIvaRates) {
		this.listIvaRates = listIvaRates;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of iva rate.
	 * 
	 * @return consultIvaRate: method that allows consulting the iva rate, it
	 *         redirects to the manage IvaRate template.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return consultIvaRate();
	}

	/**
	 * Consult the list of the IvaRate.
	 * 
	 * @return "manIvaRate": It redirects to the template to manage the iva
	 *         rate.
	 */
	public String consultIvaRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleIvaRate = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listIvaRates = new ArrayList<IvaRate>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = ivaRateDao.quantityIvaRate(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listIvaRates = ivaRateDao.consultIvaRate(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			if ((listIvaRates == null || listIvaRates.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listIvaRates == null || listIvaRates.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleIvaRate.getString("iva_rate_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manIvaRate";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(ir.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new iva rate.
	 * 
	 * @param ivaRate
	 *            : iva rate to be add or edit.
	 * 
	 * @return "regIvaRate":redirected to the template to manage iva rate.
	 */
	public String addEditIvaRate(IvaRate ivaRate) throws Exception {
		if (ivaRate != null) {
			this.ivaRate = ivaRate;
		} else {
			this.ivaRate = new IvaRate();
		}
		return "regIvaRate";
	}

	/**
	 * Method used to save or edit iva rate.
	 * 
	 * @return consultIvaRate: iva rate redirects to manage the list of updated
	 *         IvaRate.
	 */
	public String saveIvaRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (ivaRate.getIdIva() != 0) {
				ivaRateDao.editIvaRate(ivaRate);
			} else {
				messageLog = "message_registro_guardar";
				ivaRateDao.saveIvaRate(ivaRate);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							ivaRate.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultIvaRate();
	}

	/**
	 * IvaRate redirects to manage the list of updated iva rate.
	 * 
	 * @return consultIvaRate(): Consult the list of iva rate and returns to
	 *         manage IvaRate.
	 */
	public String removeIvaRate() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			ivaRateDao.removeIvaRate(ivaRate);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					ivaRate.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					ivaRate.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultIvaRate();
	}

	/**
	 * To validate the name of the iva rate, so it is not repeated in the
	 * database and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = ivaRate.getIdIva();
			IvaRate ivaRateAux = new IvaRate();
			ivaRateAux = ivaRateDao.nameExists(name, id);
			if (ivaRateAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates the range of percentage and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNumberPercentage(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Double number = (Double) value;
		String clientId = toValidate.getClientId(context);
		try {
			if (number != null) {
				if (number < 0 || number > 100) {
					String message = "message_add_range_percentage";
					context.addMessage(clientId,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundle.getString(message), null));
					((UIInput) toValidate).setValid(false);
				}
				if (!EncodeFilter.validarXSS(Double.toString(number), clientId,
						"locate.regex.numeros.decimales")) {
					((UIInput) toValidate).setValid(false);
				}
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}
