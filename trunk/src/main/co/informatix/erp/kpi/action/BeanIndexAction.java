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
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class manages the reports of the bean index general trend.
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
	@EJB
	private CropNamesDao cropNamesDao;

	private List<BeanIndex> listBeanIndex;
	private ArrayList<SelectItem> itemsCrops;
	private ArrayList<SelectItem> itemsSection;

	private BeanIndex beanIndex;
	private Paginador pagination = new Paginador();
	private int crop;
	private int section;

	/**
	 * @return listBeanIndex: List of the bean index shown in the user
	 *         interface.
	 */
	public List<BeanIndex> getListBeanIndex() {
		return listBeanIndex;
	}

	/**
	 * @param listBeanIndex
	 *            : List of the bean index shown in the user interface.
	 */
	public void setListBeanIndex(List<BeanIndex> listBeanIndex) {
		this.listBeanIndex = listBeanIndex;
	}

	/**
	 * @return itemsCrops: List of crop items to reference a bean index.
	 */
	public ArrayList<SelectItem> getItemsCrops() {
		return itemsCrops;
	}

	/**
	 * @param itemsCrops
	 *            : List of crop items to reference a bean index.
	 */
	public void setItemsCrops(ArrayList<SelectItem> itemsCrops) {
		this.itemsCrops = itemsCrops;
	}

	/**
	 * @return itemsSection: List of section items to reference a bean index.
	 */
	public ArrayList<SelectItem> getItemsSection() {
		return itemsSection;
	}

	/**
	 * @param itemsSection
	 *            : List of section items to reference a bean index.
	 */
	public void setItemsSection(ArrayList<SelectItem> itemsSection) {
		this.itemsSection = itemsSection;
	}

	/**
	 * @return beanIndex: Object containing data on the bean index.
	 */
	public BeanIndex getBeanIndex() {
		return beanIndex;
	}

	/**
	 * @param beanIndex
	 *            : Object containing data on the bean index.
	 */
	public void setBeanIndex(BeanIndex beanIndex) {
		this.beanIndex = beanIndex;
	}

	/**
	 * @return pagination: Paged list of the bean index.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paged list of the bean index.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return crop: Crop to search the bean index.
	 */
	public int getCrop() {
		return crop;
	}

	/**
	 * @param crop
	 *            : Crop to search the bean index.
	 */
	public void setCrop(int crop) {
		this.crop = crop;
	}

	/**
	 * @return section: Section to search the bean index.
	 */
	public int getSection() {
		return section;
	}

	/**
	 * @param section
	 *            : Section to search the bean index.
	 */
	public void setSection(int section) {
		this.section = section;
	}

	/**
	 * This method allow initialize the needles variables to create a report.
	 * 
	 * @return rptGeneralTrend : Display the view to manage the report.
	 */
	public String initializeSearch() {
		this.section = 0;
		this.crop = 0;
		return searchBeanIndex();
	}

	/**
	 * Consult the beanIndex list usages to show in the view.
	 * 
	 * @return "manageBeanIndex": Redirects to the template to manage the
	 *         beanIndex usage.
	 */
	public String searchBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleBeanIndex = ControladorContexto
				.getBundle("messageBeanIndex");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listBeanIndex = new ArrayList<BeanIndex>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(consult, parameters, bundleBeanIndex,
					jointSearchMessages);
			Long amount = beanIndexDao.amountBeanIndex(consult, parameters);
			if (amount > 0) {
				pagination.paginar(amount);
			}
			listBeanIndex = beanIndexDao.queryBeanIndex(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			if ((listBeanIndex == null || listBeanIndex.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (listBeanIndex == null || listBeanIndex.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleBeanIndex.getString("bean_index_label_s"),
								jointSearchMessages);
			}
			if (amount > 0) {
				loadDetailsBeanIndex();
			}
			loadComboCrops();
			loadComboSection();
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manageBeanIndex";
	}

	/**
	 * This method builds a query with an advanced search< it also builds
	 * display messages depending on the search criteria selected by the user.
	 * 
	 * @param queryBuilder
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param jointSearchMessages
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		ResourceBundle bundleCrop = ControladorContexto
				.getBundle("messageLifeCycle");
		if (this.crop != 0 && !"".equals(this.crop)) {
			queryBuilder.append("WHERE bi.crops.idCrop = :keyword ");
			SelectItem item = new SelectItem(this.crop, "keyword");
			parameters.add(item);
			jointSearchMessages.append(bundleCrop.getString("crops_label")
					+ ": " + '"'
					+ ValidacionesAction.getLabel(itemsCrops, this.crop) + '"');
			if (this.section != 0 && !"".equals(this.section)) {
				queryBuilder.append("AND bi.section.idSection = :keyword1 ");
				item = new SelectItem(this.section, "keyword1");
				parameters.add(item);
				jointSearchMessages.append(", "
						+ bundle.getString("bean_index_label_section")
						+ ": "
						+ '"'
						+ ValidacionesAction.getLabel(itemsSection,
								this.section) + '"');
			}
		} else {
			if (this.section != 0 && !"".equals(this.section)) {
				queryBuilder.append("WHERE bi.section.idSection = :keyword1 ");
				SelectItem item = new SelectItem(this.section, "keyword1");
				parameters.add(item);
				jointSearchMessages.append(bundle
						.getString("bean_index_label_section")
						+ ": "
						+ '"'
						+ ValidacionesAction.getLabel(itemsSection,
								this.section) + '"');
			}
		}
	}

	/**
	 * This method fills the different objects that are associated with a bean
	 * index.
	 * 
	 * @throws Exception
	 */
	private void loadDetailsBeanIndex() throws Exception {
		List<BeanIndex> listBeanI = new ArrayList<BeanIndex>();
		listBeanI.addAll(this.listBeanIndex);
		this.listBeanIndex = new ArrayList<BeanIndex>();
		for (BeanIndex beanIndex : listBeanI) {
			int idBeanIndex = beanIndex.getIdBeanIndex();
			Crops crops = (Crops) beanIndexDao.queryObjectBeanIndex("crops",
					idBeanIndex);
			Section section = (Section) beanIndexDao.queryObjectBeanIndex(
					"section", idBeanIndex);
			beanIndex.setCrops(crops);
			beanIndex.setSection(section);
			this.listBeanIndex.add(beanIndex);
		}
	}

	/**
	 * Method to edit or create a new bean index.
	 * 
	 * @param beanIndex
	 *            : Bean index that you are adding or editing.
	 * 
	 * @return "regBeanIndex": Redirects to the the register Bean Index
	 *         template.
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
	 * Method that checks the itemsCrops to fill the combo of the user
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
	 * Method that checks the itemsSection to fill the combo of the user
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
	 * Method to delete a bean index in the database.
	 * 
	 * @return searchBeanIndex: Query the list of the machine usages to show in
	 *         the view.
	 */
	public String deleteBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleCrop = ControladorContexto
				.getBundle("messageLifeCycle");
		try {
			Crops crop = cropsDao.cropsById(beanIndex.getCrops().getIdCrop());
			CropNames cropName = cropNamesDao.cropNamesXId(crop.getCropNames()
					.getIdCropName());
			StringBuilder details = new StringBuilder();
			details.append("Id: ");
			details.append(crop.getIdCrop());
			details.append(", ");
			details.append(bundleCrop.getString("crop_names_label"));
			details.append(": ");
			details.append(cropName.getCropName());
			details.append(", ");
			details.append(bundleCrop.getString("section_label"));
			details.append(": ");
			beanIndexDao.editBeanIndex(beanIndex);
			details.append(beanIndex.getSection().getName());
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), details));
			beanIndexDao.deleteBeanIndex(beanIndex);
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					beanIndex.getCycleNumber());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchBeanIndex();
	}

	/**
	 * This Method saves or edits one bean index in the database.
	 * 
	 * @modify 23/03/2016 Sergio.Gelves
	 * 
	 * @return searchBeanIndex: Redirects to manage bean index with the list of
	 *         bean index
	 */
	public String saveBeanIndex() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleCrop = ControladorContexto
				.getBundle("messageLifeCycle");
		String registerMessage = "message_registro_modificar";
		try {

			Crops crop = cropsDao.cropsById(beanIndex.getCrops().getIdCrop());
			CropNames cropName = cropNamesDao.cropNamesXId(crop.getCropNames()
					.getIdCropName());
			StringBuilder details = new StringBuilder();
			details.append("Id: ");
			details.append(crop.getIdCrop());
			details.append(", ");
			details.append(bundleCrop.getString("crop_names_label"));
			details.append(": ");
			details.append(cropName.getCropName());
			details.append(", ");
			details.append(bundleCrop.getString("section_label"));
			details.append(": ");
			if (beanIndex.getIdBeanIndex() != 0) {
				beanIndexDao.editBeanIndex(beanIndex);
				details.append(beanIndex.getSection().getName());
			} else {
				registerMessage = "message_registro_guardar";
				beanIndexDao.saveBeanIndex(beanIndex);
				details.append(sectionDao.sectionXId(
						beanIndex.getSection().getIdSection()).getName());
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), details));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchBeanIndex();
	}
}
