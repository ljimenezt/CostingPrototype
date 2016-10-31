package co.informatix.erp.informacionBase.action;

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

import co.informatix.erp.informacionBase.dao.ColorDao;
import co.informatix.erp.informacionBase.entities.Color;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the logic related to create, update, and delete Colors
 * in the system.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ColorAction implements Serializable {

	private Color color;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	private List<Color> listColors;

	@EJB
	private ColorDao colorDao;

	/**
	 * @return color: Object of the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            : Object of the color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return pagination: Management paginated list of colors.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of colors.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: color type to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : color type to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listColors: list of colors
	 */
	public List<Color> getListColors() {
		return listColors;
	}

	/**
	 * @param listColors
	 *            : list of colors.
	 */
	public void setListColors(List<Color> listColors) {
		this.listColors = listColors;
	}

	/**
	 * Method to edit or create a new color.
	 * 
	 * @param color
	 *            : Color that will add or edit.
	 * @return regColor: Navigation rule that redirects to the template
	 *         registerColor.
	 */
	public String addEditColor(Color color) {
		if (color != null) {
			this.color = color;
		} else {
			this.color = new Color();
		}
		return "regColor";
	}

	/**
	 * Method that initialize the parameters of the search and load initial list
	 * of color.
	 * 
	 * @return consultColor: Method containing navigation rule that redirect to
	 *         manage color.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return consultColor();
	}

	/**
	 * Method that consult the color exist in database.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return gesColor: Navigation rule that redirect to manage color.
	 */
	public String consultColor() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleColor = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listColors = new ArrayList<Color>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";

		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = colorDao.quantityColor(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listColors = colorDao.consultColor(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			if ((listColors == null || listColors.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listColors == null || listColors.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				messageSearch = MessageFormat.format(message,
						bundleColor.getString("color_label"),
						unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesColor";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param consult
	 *            : Consult concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(c.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method used to save or edit the color.
	 * 
	 * @return consultColor(): method that redirects to manage the color.
	 */
	public String saveColor() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			if (color.getId() != 0) {
				colorDao.editColor(color);
			} else {
				messageLog = "message_registro_guardar";
				colorDao.saveColor(color);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							color.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultColor();
	}

	/**
	 * To validate the name of the color, to not repeat in the database and
	 * validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Color colorAux = colorDao.nameExists(name, color.getId());
			if (colorAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						messageExistence, "mensaje");
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
	 * To validate the code of the color, to not repeat in the database and
	 * validates the character formatting.
	 * 
	 * @param context
	 *            : Application context.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateCode(FacesContext context, UIComponent toValidate,
			Object value) {
		String code = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			if ((code).matches("((^#)([A-Fa-f0-9]+)|\\s)")
					&& code.length() == 7) {
				Color colorAux = colorDao.codeExists(code, color.getId());
				if (colorAux != null) {
					String messageExistence = "message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							messageExistence, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			} else {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_validate_format_color", "mensaje");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that eliminates the color in database.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return consultColor: Method that consults colors; it redirects to the
	 *         color management template.
	 */
	public String deleteColor() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			colorDao.removeColor(this.color);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.color.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.color.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultColor();
	}
}