package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
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

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.PlotDao;
import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Plot;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;

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
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private PlotDao plotDao;

	private List<Section> listSection;
	private List<Plot> plotList;
	private List<Plot> subListPlot;
	private List<SelectItem> itemsCropNames;
	private Section section;
	private Section sectionSelected;
	private Plot plotSelected;
	private PlotAction plotAction;
	private Paginador pagination = new Paginador();
	private Paginador paginationPlot = new Paginador();
	private String nameSearch;
	private int idCropNames;
	private boolean flagButton;
	private boolean flagDelete;

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
	 * @return listPlots: list of plots.
	 */
	public List<Plot> getPlotList() {
		return plotList;
	}

	/**
	 * @param listPlots
	 *            :list of plots.
	 */
	public void setPlotList(List<Plot> plotList) {
		this.plotList = plotList;
	}

	/**
	 * @return subListPlot: sublist of plots.
	 */
	public List<Plot> getSubListPlot() {
		return subListPlot;
	}

	/**
	 * @param subListPlot
	 *            :sublist of plots.
	 */
	public void setSubListPlot(List<Plot> subListPlot) {
		this.subListPlot = subListPlot;
	}

	/**
	 * @return itemsCropNames: cropName associated with a section.
	 */
	public List<SelectItem> getItemsCropNames() {
		return itemsCropNames;
	}

	/**
	 * @param itemsCropNames
	 *            :cropName associated with a section.
	 */
	public void setItemsCropNames(List<SelectItem> itemsCropNames) {
		this.itemsCropNames = itemsCropNames;
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
	 * @return sectionSelected: Section selected in the section table.
	 */
	public Section getSectionSelected() {
		return sectionSelected;
	}

	/**
	 * @param sectionSelected
	 *            :Section selected in the section table.
	 */
	public void setSectionSelected(Section sectionSelected) {
		this.sectionSelected = sectionSelected;
	}

	/**
	 * @return plotSelected: Plot selected in the plot table.
	 */
	public Plot getPlotSelected() {
		return plotSelected;
	}

	/**
	 * @param plotSelected
	 *            :Plot selected in the plot table.
	 */
	public void setPlotSelected(Plot plotSelected) {
		this.plotSelected = plotSelected;
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
	 * @return paginationPlot: Management paged list plots.
	 */
	public Paginador getPaginationPlot() {
		return paginationPlot;
	}

	/**
	 * @param paginationPlot
	 *            :Management paged list plots.
	 */
	public void setPaginationPlot(Paginador paginationPlot) {
		this.paginationPlot = paginationPlot;
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
	 * @return idCropNames: CropNames identifier.
	 */
	public int getIdCropNames() {
		return idCropNames;
	}

	/**
	 * @param idCropNames
	 *            : CropNames identifier.
	 */
	public void setIdCropNames(int idCropNames) {
		this.idCropNames = idCropNames;
	}

	/**
	 * @return flagButton: this flag is true if it is loaded from the register.
	 */
	public boolean isFlagButton() {
		return flagButton;
	}

	/**
	 * @param flagButton
	 *            : this flag is true if it is loaded from the register.
	 */
	public void setFlagButton(boolean flagButton) {
		this.flagButton = flagButton;
	}

	/**
	 * @return flagDelete: Flag that indicate when it is eliminated.
	 */
	public boolean isFlagDelete() {
		return flagDelete;
	}

	/**
	 * @param flagDelete
	 *            : Flag that indicate when it is eliminated.
	 */
	public void setFlagDelete(boolean flagDelete) {
		this.flagDelete = flagDelete;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the section
	 * 
	 * @modify 17/08/2016 Wilhelm.Boada
	 * 
	 * @return consultSection :Consult the list of the section in the database
	 */
	public String initializeSearch() {
		try {
			if (ControladorContexto.getFacesContext() != null) {
				this.plotAction = ControladorContexto
						.getContextBean(PlotAction.class);
			}
			this.nameSearch = "";
			this.idCropNames = 0;
			this.sectionSelected = null;
			this.flagButton = false;
			this.plotList = null;
			loadCropNames();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultSection();
	}

	/**
	 * This method allows initialize values and load list.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void loadList() {
		this.sectionSelected = null;
		consultSection();
	}

	/**
	 * Consult the list of the section in the database
	 * 
	 * @return manSection: redirects to the template to manage sections
	 */
	public String consultSection() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
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
				this.pagination.setOpcion('f');
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
	 * @modify 17/08/2016 Wilhelm.Boada
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
	private void searchAdvance(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		boolean selection = false;
		if (this.idCropNames != 0) {
			consult.append("WHERE c.idCropName=:keyword2 ");
			SelectItem item = new SelectItem(this.idCropNames, "keyword2");
			parameters.add(item);
			selection = true;
		}

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("UPPER(s.name) LIKE UPPER(:keyword) ");
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
	 * @modify 17/08/2016 Wilhelm.Boada
	 * @modify 11/10/2016 Claudia.Rey
	 * 
	 * @param section
	 *            :section to be add or edit
	 * @return "regSection":redirects to register section template.
	 */
	public String addEditSection(Section section) {
		try {
			this.plotAction = ControladorContexto
					.getContextBean(PlotAction.class);
			this.plotAction.setFlagAction(Constantes.FLAG_ACTION_SECTION);
			if (section != null) {
				this.section = section;
				selectSection(section);
				showPlots();
			} else {
				this.section = new Section();
				this.section.setCropNames(new CropNames());
				this.section.getCropNames().setIdCropName(idCropNames);
				this.plotList = new ArrayList<Plot>();
				this.subListPlot = null;
			}
			this.flagButton = true;
			loadCropNames();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regSection";
	}

	/**
	 * To validate the name of the section, so it is not repeated in the
	 * database and valid against XSS.
	 * 
	 * @param context
	 *            : application context
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
			Section sectionAux = sectionDao.nameExist(name, id);
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
	 * @modify 17/08/2016 Wilhelm.Boada
	 * 
	 * @return initializeSearch(): Redirects to manage the list of sections
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
			savePlotsBySection();
			this.idCropNames = section.getCropNames().getIdCropName();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageRegister), section.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		this.flagButton = false;
		return initializeSearch();
	}

	/**
	 * Method to remove a section of the database
	 * 
	 * @modify 01/09/2016 Wilhelm.Boada
	 * 
	 * @return initializeSearch(): Consult the list of sections and returns to
	 *         manage view
	 */
	public String deleteSection() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			List<Plot> plotListBySection = plotDao
					.consultPlotsBySection(section.getIdSection());
			if (plotListBySection != null) {
				for (Plot plot : plotListBySection) {
					plot.setSection(null);
					plotDao.editPlot(plot);
				}
			}
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
		return initializeSearch();
	}

	/**
	 * This method allows load of nameCrops list.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void loadCropNames() throws Exception {
		itemsCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				itemsCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
	}

	/**
	 * Consult the plots list associated to the section.
	 * 
	 * @author Wilhelm.Boada
	 * @modify 13/10/2016 Claudia.Rey
	 */
	public void consultPlotsBySection() {
		try {
			this.flagDelete = false;
			subListPlot = new ArrayList<Plot>();
			Long quantity = plotDao.quantityPlotsBySection(this.sectionSelected
					.getIdSection());
			if (quantity != null && quantity > 0) {
				this.plotList = plotDao
						.consultPlotsBySection(this.sectionSelected
								.getIdSection());
				initializeList();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It initializes the pager with a fixed range to manage the plot list and
	 * it gets a sublist of plots.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void initializeList() {
		try {
			subListPlot = new ArrayList<Plot>();
			if (this.plotList != null) {
				Collections.sort(this.plotList);
				subListPlot.addAll(this.plotList);
			}
			Long paginationAmount = (long) this.subListPlot.size();

			this.paginationPlot.paginarRangoDefinido(paginationAmount, 5);
			int inicial = this.paginationPlot.getItemInicial() - 1;
			int fin = this.paginationPlot.getItemFinal();
			this.subListPlot = this.subListPlot.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Selects a single section for display the associated plots.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param sectionSelected
	 *            : Section selected on the view.
	 */
	public void selectSection(Section sectionSelected) {
		this.sectionSelected = new Section();
		this.flagButton = false;
		sectionSelected.setSelected(true);
		for (Section section : listSection) {
			if (section.isSelected()) {
				if (section.getIdSection() == sectionSelected.getIdSection()) {
					this.sectionSelected = section;
				} else {
					section.setSelected(false);
				}
			}
		}
	}

	/**
	 * Show the plots associated to section.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void showPlots() {
		if (plotAction != null) {
			plotAction.setIdSection(sectionSelected.getIdSection());
			consultPlotsBySection();
		}
	}

	/**
	 * This method allows save the plot list related to the section.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void savePlotsBySection() throws Exception {
		List<Plot> listPlotsSectionAsocciates = plotDao
				.consultPlotsBySection(this.section.getIdSection());
		if (this.plotList != null && listPlotsSectionAsocciates != null) {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			for (Plot listPlots : listPlotsSectionAsocciates) {
				currentIds.add(listPlots.getIdPlot());
			}
			for (Plot listPlotTem : this.plotList) {
				newsIds.add(listPlotTem.getIdPlot());
			}
			List<DatosGuardar> dataList = ValidacionesAction.validarListas(
					currentIds, newsIds);
			for (DatosGuardar saveData : dataList) {
				String action = saveData.getAccion();
				Plot plot = plotDao.plotById(saveData.getIdClase());
				if (Constantes.QUERY_DELETE.equals(action)) {
					plot.setSection(null);
					plotDao.editPlot(plot);
				} else if (Constantes.QUERY_INSERT.equals(action)) {
					plot.setSection(this.section);
					plotDao.editPlot(plot);
				}
			}
		}
	}

	/**
	 * Method that eliminates the plot selected by the user from the list
	 * plotList.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param plot
	 *            : plot to remove of the list.
	 */
	public void removePlotList(Plot plot) {
		for (Plot plotSelected : this.plotList) {
			if (plotSelected.equals(plot)) {
				this.plotList.remove(plot);
				break;
			}
		}
		initializeList();
	}
}