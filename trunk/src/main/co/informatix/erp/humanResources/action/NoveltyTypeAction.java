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

import co.informatix.erp.humanResources.dao.NoveltyTypeDao;
import co.informatix.erp.humanResources.entities.NoveltyType;
import co.informatix.erp.informacionBase.dao.ColorDao;
import co.informatix.erp.informacionBase.entities.Color;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the logic related to create, update, and delete types
 * of novelties in the system.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class NoveltyTypeAction implements Serializable {

	private NoveltyType noveltyType;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	private int idColor;

	private List<NoveltyType> listNoveltyType;
	private List<SelectItem> listColorItems;

	@EJB
	private NoveltyTypeDao noveltyTypeDao;
	@EJB
	private ColorDao colorDao;

	/**
	 * @return noveltyType: Object of the novelty type.
	 */
	public NoveltyType getNoveltyType() {
		return noveltyType;
	}

	/**
	 * @param noveltyType
	 *            : Object of the novelty type.
	 */
	public void setNoveltyType(NoveltyType noveltyType) {
		this.noveltyType = noveltyType;
	}

	/**
	 * @return pagination: Management paginated list of the types of novelty.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the types of novelty.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Novelty type to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Novelty type to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return idColor: Color identifier.
	 */
	public int getIdColor() {
		return idColor;
	}

	/**
	 * @param idColor
	 *            : Color identifier.
	 */
	public void setIdColor(int idColor) {
		this.idColor = idColor;
	}

	/**
	 * @return listNoveltyType: list of novelty type
	 */
	public List<NoveltyType> getListNoveltyType() {
		return listNoveltyType;
	}

	/**
	 * @param listNoveltyType
	 *            : list of novelty type
	 */
	public void setListNoveltyType(List<NoveltyType> listNoveltyType) {
		this.listNoveltyType = listNoveltyType;
	}

	/**
	 * @return listColorItems: color items that are loaded into the combo of the
	 *         novelty type interface.
	 */
	public List<SelectItem> getListColorItems() {
		return listColorItems;
	}

	/**
	 * Method to edit or create a new novelty type.
	 * 
	 * @param noveltyType
	 *            : Type of novelty that will add or edit.
	 * @return regNoveltyType: Navigation rule that redirects to the template
	 *         registerNoveltyType.
	 */
	public String addEditNoveltyType(NoveltyType noveltyType) {
		try {
			if (noveltyType != null) {
				this.noveltyType = noveltyType;
				this.idColor = this.noveltyType.getColor().getId();
			} else {
				this.noveltyType = new NoveltyType();
				this.idColor = 0;
			}
			loadColorCombo();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regNoveltyType";
	}

	/**
	 * Method that initialize the parameters of the search and load initial list
	 * of novelty type.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return consultNoveltyType: Method containing navigation rule that
	 *         redirect to manage novelty type.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		this.idColor = 0;
		return consultNoveltyType();
	}

	/**
	 * Method that consult the novelty type exist in database.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return gesNoveltyType: Navigation rule that redirect to manage novelty
	 *         type
	 */
	public String consultNoveltyType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleNoveltyType = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listNoveltyType = new ArrayList<NoveltyType>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";

		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = noveltyTypeDao.quantityNoveltyType(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listNoveltyType = noveltyTypeDao.consultNoveltyType(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listNoveltyType == null || listNoveltyType.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listNoveltyType == null || listNoveltyType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				messageSearch = MessageFormat.format(message,
						bundleNoveltyType.getString("novelty_type_label"),
						unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesNoveltyType";
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
			consult.append("WHERE UPPER(nt.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * It allows loading the item select the colors.
	 * 
	 * @throws Exception
	 */
	private void loadColorCombo() throws Exception {
		listColorItems = new ArrayList<SelectItem>();
		List<Color> colors = colorDao.queryColors();
		if (colors != null) {
			for (Color c : colors) {
				listColorItems.add(new SelectItem(c.getId(), c.getName()));
			}
		}
	}

	/**
	 * Method used to save or edit the novelty type.
	 * 
	 * @return consultNoveltyType(): method that redirects to manage the types
	 *         of novelty.
	 */
	public String saveNoveltyType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			noveltyType.setColor(colorDao.colorById(idColor));
			if (noveltyType.getId() != 0) {
				noveltyTypeDao.editNoveltyType(noveltyType);
			} else {
				messageLog = "message_registro_guardar";
				noveltyTypeDao.saveNoveltyType(noveltyType);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), noveltyType.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultNoveltyType();
	}

	/**
	 * To validate the name of the novelty types, to not repeat in the database
	 * and validates against XSS.
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
			NoveltyType noveltyTypeAux = noveltyTypeDao.nameExists(name,
					noveltyType.getId());
			if (noveltyTypeAux != null) {
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
	 * To validate the name of the color, to not repeat in the database.
	 */
	public void validateColor() {
		try {
			NoveltyType noveltyTypeAux = noveltyTypeDao.colorExists(
					noveltyType.getId(), idColor);
			if (noveltyTypeAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(
						"formNoveltyType:comboColor", messageExistence,
						"mensaje");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that eliminates the novelty type in database.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return consultNoveltyType: Method that consults novelty Types; it
	 *         redirects to the novelty management template.
	 */
	public String deleteNoveltyType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			noveltyTypeDao.removeNoveltyType(this.noveltyType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.noveltyType.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.noveltyType.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultNoveltyType();
	}

}
