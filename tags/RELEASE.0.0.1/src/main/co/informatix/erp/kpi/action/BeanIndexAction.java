package co.informatix.erp.kpi.action;

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

import co.informatix.erp.kpi.dao.BeanIndexDao;
import co.informatix.erp.kpi.entities.BeanIndex;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class manage the reports of the bean index general trend.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class BeanIndexAction implements Serializable {

	@EJB
	private BeanIndexDao beanIndexDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private SectionDao sectionDao;

	private List<BeanIndex> listBeanIndex;
	private ArrayList<SelectItem> itemsCrops;
	private ArrayList<SelectItem> itemsSection;

	private BeanIndex beanIndex;
	private Paginador paginador = new Paginador();
	private int crop;
	private int section;

	/**
	 * @return listBeanIndex: list of the bean index shown in the user interface
	 */
	public List<BeanIndex> getListBeanIndex() {
		return listBeanIndex;
	}

	/**
	 * @param listBeanIndex
	 *            :list of the bean index shown in the user interface
	 */
	public void setListBeanIndex(List<BeanIndex> listBeanIndex) {
		this.listBeanIndex = listBeanIndex;
	}

	/**
	 * @return itemsCrops: list items of crops to reference a bean index
	 */
	public ArrayList<SelectItem> getItemsCrops() {
		return itemsCrops;
	}

	/**
	 * @param itemsCrops
	 *            :list items of crops to reference a bean index
	 */
	public void setItemsCrops(ArrayList<SelectItem> itemsCrops) {
		this.itemsCrops = itemsCrops;
	}

	/**
	 * @return itemsSection: list items of section to reference a bean index
	 */
	public ArrayList<SelectItem> getItemsSection() {
		return itemsSection;
	}

	/**
	 * @param itemsSection
	 *            : list items of section to reference a bean index
	 */
	public void setItemsSection(ArrayList<SelectItem> itemsSection) {
		this.itemsSection = itemsSection;
	}

	/**
	 * @return beanIndex: object containing data on the bean index
	 */
	public BeanIndex getBeanIndex() {
		return beanIndex;
	}

	/**
	 * @param beanIndex
	 *            : object containing data on the bean index
	 */
	public void setBeanIndex(BeanIndex beanIndex) {
		this.beanIndex = beanIndex;
	}

	/**
	 * @return paginador: Management paginated list of the bean index.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paginated list of the bean index.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return crop: crops to search the bean index
	 */
	public int getCrop() {
		return crop;
	}

	/**
	 * @param crop
	 *            :crops to search the bean index
	 */
	public void setCrop(int crop) {
		this.crop = crop;
	}

	/**
	 * @return section: section to search the bean index
	 */
	public int getSection() {
		return section;
	}

	/**
	 * @param section
	 *            :section to search the bean index
	 */
	public void setSection(int section) {
		this.section = section;
	}

	/**
	 * This method allow initialize the needles variables to create a report
	 * 
	 * @return rptGeneralTrend : display the view to manage the report
	 */
	public String initializeSearch() {
		this.section = 0;
		this.crop = 0;
		return consultBeanIndex();
	}

	/**
	 * Consult the list of the machine usages to show in the view
	 * 
	 * @return "gesMachineUsage": redirects to the template to manage the
	 *         machine usage
	 */
	public String consultBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleBeanIndex = ControladorContexto
				.getBundle("mensajeBeanIndex");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listBeanIndex = new ArrayList<BeanIndex>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			advancedSearch(consult, parameters, bundleBeanIndex,
					unionMessagesSearch);
			Long amount = beanIndexDao.amountBeanIndex(consult, parameters);
			if (amount > 0) {
				paginador.paginar(amount);
			}
			listBeanIndex = beanIndexDao.consultBeanIndex(
					paginador.getInicio(), paginador.getRango(), consult,
					parameters);
			if ((listBeanIndex == null || listBeanIndex.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listBeanIndex == null || listBeanIndex.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleBeanIndex.getString("bean_index_label_s"),
								unionMessagesSearch);
			}
			if (amount > 0) {
				loadDetailsBeanIndex();
			}
			loadComboCrops();
			loadComboSection();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manageBeanIndex";
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleCrop = ControladorContexto
				.getBundle("mensajeLifeCycle");
		if (this.crop != 0 && !"".equals(this.crop)) {
			consult.append("WHERE bi.crops.idCrop = :keyword ");
			SelectItem item = new SelectItem(this.crop, "keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundleCrop.getString("crops_label")
					+ ": " + '"'
					+ ValidacionesAction.getLabel(itemsCrops, this.crop) + '"');
			if (this.section != 0 && !"".equals(this.section)) {
				consult.append("AND bi.section.idSection = :keyword1 ");
				item = new SelectItem(this.section, "keyword1");
				parameters.add(item);
				unionMessagesSearch.append(", "
						+ bundle.getString("bean_index_label_section")
						+ ": "
						+ '"'
						+ ValidacionesAction.getLabel(itemsSection,
								this.section) + '"');
			}
		} else {
			if (this.section != 0 && !"".equals(this.section)) {
				consult.append("WHERE bi.section.idSection = :keyword1 ");
				SelectItem item = new SelectItem(this.section, "keyword1");
				parameters.add(item);
				unionMessagesSearch.append(bundle
						.getString("bean_index_label_section")
						+ ": "
						+ '"'
						+ ValidacionesAction.getLabel(itemsSection,
								this.section) + '"');
			}
		}
	}

	/**
	 * This method fills the various objects associated with a bean index
	 * 
	 * @throws Exception
	 */
	private void loadDetailsBeanIndex() throws Exception {
		List<BeanIndex> listBeanI = new ArrayList<BeanIndex>();
		listBeanI.addAll(this.listBeanIndex);
		this.listBeanIndex = new ArrayList<BeanIndex>();
		for (BeanIndex beanIndex : listBeanI) {
			int idBeanIndex = beanIndex.getIdBeanIndex();
			Crops crops = (Crops) beanIndexDao.consultObjectBeanIndex("crops",
					idBeanIndex);
			Section section = (Section) beanIndexDao.consultObjectBeanIndex(
					"section", idBeanIndex);
			beanIndex.setCrops(crops);
			beanIndex.setSection(section);
			this.listBeanIndex.add(beanIndex);
		}
	}

	/**
	 * Method to edit or create new bean index.
	 * 
	 * @param beanIndex
	 *            :bean index that you are adding or editing
	 * 
	 * @return "regBeanIndex": redirected to the template record machine usage.
	 */
	public String addEditBeanIndex(BeanIndex beanIndex) {
		try {
			if (beanIndex != null) {
				this.beanIndex = beanIndex;
			} else {
				this.beanIndex = new BeanIndex();
				this.beanIndex.setCrops(new Crops());
				this.beanIndex.setSection(new Section());
			}
			loadComboCrops();
			loadComboSection();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regBeanIndex";
	}

	/**
	 * Method that allows check the crops to fill the combo of the user
	 * interface.
	 * 
	 * @throws Exception
	 */
	private void loadComboCrops() throws Exception {
		itemsCrops = new ArrayList<SelectItem>();
		List<Crops> crops = cropsDao.listCrops();
		if (crops != null) {
			for (Crops crop : crops) {
				itemsCrops.add(new SelectItem(crop.getIdCrop(), crop
						.getDescription()));
			}
		}
	}

	/**
	 * Method that allows check the section to fill the combo of the user
	 * interface.
	 * 
	 * @throws Exception
	 */
	private void loadComboSection() throws Exception {
		itemsSection = new ArrayList<SelectItem>();
		List<Section> sections = sectionDao.listSection();
		if (sections != null) {
			for (Section section : sections) {
				itemsSection.add(new SelectItem(section.getIdSection(), section
						.getName()));
			}
		}
	}

	/**
	 * Method to delete a bean index in the database
	 * 
	 * 
	 * @return consultBeanIndex: Consult the list of the machine usages to show
	 *         in the view
	 */
	public String deleteBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			beanIndexDao.eliminarBeanIndex(beanIndex);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					beanIndex.getCycleNumber()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					beanIndex.getCycleNumber());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultBeanIndex();
	}

	/**
	 * This Method allow save or edit one bean index in the DB
	 * 
	 * @return consultBeanIndex: Redirects to manage bean index with the list of
	 *         bean index
	 */
	public String saveBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (beanIndex.getIdBeanIndex() != 0) {
				beanIndexDao.editarBeanIndex(beanIndex);
			} else {
				mensajeRegistro = "message_registro_guardar";
				beanIndexDao.guardarBeanIndex(beanIndex);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					beanIndex.getSampleWeight()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultBeanIndex();
	}

}
