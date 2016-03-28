package co.informatix.erp.lifeCycle.action;

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

import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the section that may be in the BD
 * 
 * The logic is to consult, edit or add sections
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class SectionAction implements Serializable {
	@EJB
	private SectionDao sectionDao;

	private List<Section> listSection;
	private Section section;
	private Paginador pagination = new Paginador();
	private String nameSearch;

	/**
	 * @return listSection: list of section
	 */
	public List<Section> getListSection() {
		return listSection;
	}

	/**
	 * @param listSection
	 *            :list of section
	 */
	public void setListSection(List<Section> listSection) {
		this.listSection = listSection;
	}

	/**
	 * @return section: gets the registration of section
	 */
	public Section getSection() {
		return section;
	}

	/**
	 * @param section
	 *            :sets the registration of section
	 */
	public void setSection(Section section) {
		this.section = section;
	}

	/**
	 * @return pagination: Management paged list section.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Management paged list section.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name section to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Name section to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the section
	 * 
	 * @return consultSection :Consult the list of the section in the database
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return consultSection();
	}

	/**
	 * Consult the list of the section in the database
	 * 
	 * @return manSection: redirects to the template to manage sections
	 */
	public String consultSection() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listSection = new ArrayList<Section>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMenssageSearch = new StringBuilder();
		String messageSearch = "";
		try {
			searchAdvance(consult, parameters, bundle, unionMenssageSearch);
			Long amount = sectionDao.amountSection(consult, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			listSection = sectionDao.consultSections(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			if ((listSection == null || listSection.size() <= 0)
					&& !"".equals(unionMenssageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMenssageSearch);
			} else if (listSection == null || listSection.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMenssageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("section_label_s"),
								unionMenssageSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manSection";
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
	private void searchAdvance(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(s.name) LIKE UPPER(:keyword) ");
			consult.append("OR UPPER(s.description) LIKE UPPER(:keyword)");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new section.
	 * 
	 * @param section
	 *            :section to be add or edit
	 * 
	 * @return "regSection":redirects to register section template.
	 */
	public String addEditSection(Section section) {
		if (section != null) {
			this.section = section;
		} else {
			this.section = new Section();
		}
		return "regSection";
	}

	/**
	 * To validate the name of the section, so it is not repeated in the
	 * database and valid against XSS.
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = section.getIdSection();
			Section sectionAux = new Section();
			sectionAux = sectionDao.nameExist(name, id);
			if (sectionAux != null) {
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
	 * Method allows save or edit the section
	 * 
	 * @return consultSection: Redirects to manage the list of sections
	 */
	public String saveSection() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageRegister = "message_registro_modificar";
		try {
			if (section.getIdSection() != 0) {
				sectionDao.editSection(section);
			} else {
				messageRegister = "message_registro_guardar";
				sectionDao.saveSection(section);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageRegister), section.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultSection();
	}

	/**
	 * Method to remove a section of the database
	 * 
	 * @return consultSection(): Consult the list of sections and returns to
	 *         manage view
	 */
	public String deleteSection() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			sectionDao.deleteSection(section);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					section.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					section.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultSection();
	}

}