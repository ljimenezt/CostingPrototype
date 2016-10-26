package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import co.informatix.erp.humanResources.dao.NoveltyTypeDao;
import co.informatix.erp.humanResources.entities.NoveltyType;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;

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

	@EJB
	private NoveltyTypeDao noveltyTypeDao;

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
	 * Method to edit or create a new novelty type.
	 * 
	 * @param noveltyType
	 *            : Type of novelty that will add or edit.
	 * @return regNoveltyType: Navigation rule that redirects to the template
	 *         registerNoveltyType.
	 */
	public String addEditNoveltyType(NoveltyType noveltyType) {
		if (noveltyType != null) {
			this.noveltyType = noveltyType;
		} else {
			this.noveltyType = new NoveltyType();
		}
		return "regNoveltyType";
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
	 * Consult the list of the novelty type.
	 * 
	 * @return gesContrType: Navigation rule that redirects to manage contract
	 *         types.
	 */
	public String consultNoveltyType() {
		return "";
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
}