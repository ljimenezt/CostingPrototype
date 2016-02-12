package co.informatix.erp.warehouse.action;

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

import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.TypeOfManagementDao;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;
import co.informatix.erp.warehouse.entities.MeasurementUnits;
import co.informatix.erp.warehouse.entities.TypeOfManagement;

/**
 * This class is all related logic with creating, updating and removal of
 * material.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MaterialsAction implements Serializable {

	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private MaterialsTypeDao materialsTypeDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;
	@EJB
	private TypeOfManagementDao typeOfManagementDao;

	private String nombreBuscar;

	private Paginador paginador = new Paginador();

	private Materials materials;

	private List<Materials> listaMateriales;
	private List<SelectItem> itemsTipoMaterial;
	private List<SelectItem> itemsUnidadMedida;
	private List<SelectItem> itemsTipoGestion;

	/**
	 * @return nombreBuscar :Name by which you want to consult the material.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Name by which you want to consult the material.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return paginador: The paging controller object.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :The paging controller object.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return materials: Material object to which the management is done: edit
	 *         or record.
	 */
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            :Material object to which the management is done: edit or
	 *            record.
	 */
	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	/**
	 * @return listaMateriales: List of materials that are loaded into the user
	 *         interface.
	 */
	public List<Materials> getListaMateriales() {
		return listaMateriales;
	}

	/**
	 * @param listaMateriales
	 *            :List of materials that are loaded into the user interface.
	 */
	public void setListaMateriales(List<Materials> listaMateriales) {
		this.listaMateriales = listaMateriales;
	}

	/**
	 * @return itemsTipoMaterial: List of items of the types of materials to be
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsTipoMaterial() {
		return itemsTipoMaterial;
	}

	/**
	 * @param itemsTipoMaterial
	 *            :List of items of the types of materials to be loaded into the
	 *            combo in the user interface.
	 */
	public void setItemsTipoMaterial(List<SelectItem> itemsTipoMaterial) {
		this.itemsTipoMaterial = itemsTipoMaterial;
	}

	/**
	 * @return itemsUnidadMedida: List of items from the units of measure of
	 *         materials are loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsUnidadMedida() {
		return itemsUnidadMedida;
	}

	/**
	 * @param itemsUnidadMedida
	 *            :List of items from the units of measure of materials are
	 *            loaded into the combo in the user interface.
	 */
	public void setItemsUnidadMedida(List<SelectItem> itemsUnidadMedida) {
		this.itemsUnidadMedida = itemsUnidadMedida;
	}

	/**
	 * @return itemsTipoGestion :List of items of materials management types
	 *         that are loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsTipoGestion() {
		return itemsTipoGestion;
	}

	/**
	 * @param itemsTipoGestion
	 *            :List of items of materials management types that are loaded
	 *            into the combo in the user interface.
	 */
	public void setItemsTipoGestion(List<SelectItem> itemsTipoGestion) {
		this.itemsTipoGestion = itemsTipoGestion;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultarMateriales: Materials consulting method and redirects to
	 *         the template to manage materials.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		this.materials = null;
		return consultarMateriales();
	}

	/**
	 * Consult the list of Materials
	 * 
	 * @return gesMaterials: Navigation rule that redirects to manage materials
	 */
	public String consultarMateriales() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMateriales = new ArrayList<Materials>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = materialsDao.cantidadMateriales(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			if (cantidad != null && cantidad > 0) {
				listaMateriales = materialsDao.consultarMateriales(
						paginador.getInicio(), paginador.getRango(), consulta,
						parametros);
			}
			if ((listaMateriales == null || listaMateriales.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMateriales == null || listaMateriales.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("materials_label_s"),
								unionMensajesBusqueda);
			}
			cargarDetallesMateriales();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaterials";
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
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method of uploading the details of the list of materials or relationships
	 * with other objects in the database.
	 * 
	 * @throws Exception
	 */
	private void cargarDetallesMateriales() throws Exception {
		if (this.listaMateriales != null) {
			for (Materials materiales : this.listaMateriales) {
				cargarDetallesMaterial(materiales);
			}
		}
	}

	/**
	 * Method of uploading the details of a material or relationships with other
	 * objects in the database.
	 * 
	 * @param materials
	 *            : Materials to load the details.
	 * @throws Exception
	 */
	public void cargarDetallesMaterial(Materials materials) throws Exception {
		int idMaterials = materials.getIdMaterial();
		MaterialsType materialsType = (MaterialsType) materialsDao
				.consultarObjetoMaterials("materialType", idMaterials);
		MeasurementUnits measurementUnits = (MeasurementUnits) materialsDao
				.consultarObjetoMaterials("measurementUnits", idMaterials);
		TypeOfManagement typeOfManagement = (TypeOfManagement) materialsDao
				.consultarObjetoMaterials("typeOfManagement", idMaterials);
		Hr responsable = (Hr) materialsDao.consultarObjetoMaterials(
				"responsable", idMaterials);

		materials.setMaterialType(materialsType);
		materials.setMeasurementUnits(measurementUnits);
		materials.setTypeOfManagement(typeOfManagement);
		materials.setResponsable(responsable);
	}

	/**
	 * Method to edit or create a new material.
	 * 
	 * @param materiales
	 *            :material are adding or editing
	 * 
	 * @return "regMaterials":redirected to the template record materials.
	 */
	public String agregarEditarMaterials(Materials materiales) {
		try {
			if (materiales != null) {
				this.materials = materiales;
				Hr responsable = (Hr) materialsDao.consultarObjetoMaterials(
						"responsable", materiales.getIdMaterial());
				if (responsable != null) {
					this.materials.setResponsable(responsable);
				}
			} else {
				this.materials = new Materials();
				this.materials.setResponsable(new Hr());
			}
			cargarCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMaterials";
	}

	/**
	 * This method allows you to load combo interface for registering a new
	 * material.
	 * 
	 * @throws Exception
	 */
	private void cargarCombos() throws Exception {
		itemsTipoMaterial = new ArrayList<SelectItem>();
		itemsUnidadMedida = new ArrayList<SelectItem>();
		itemsTipoGestion = new ArrayList<SelectItem>();

		List<MaterialsType> materialsType = materialsTypeDao
				.consultarMaterialsTypes();
		if (materialsType != null) {
			for (MaterialsType materialsTypes : materialsType) {
				itemsTipoMaterial.add(new SelectItem(materialsTypes
						.getIdMaterialsType(), materialsTypes.getName()));
			}
		}
		List<MeasurementUnits> unidadesMedida = measurementUnitsDao
				.consultarMeasurementsUnits();
		if (unidadesMedida != null) {
			for (MeasurementUnits uMedidas : unidadesMedida) {
				itemsUnidadMedida.add(new SelectItem(uMedidas
						.getIdMeasurementUnits(), uMedidas.getName()));
			}
		}
		List<TypeOfManagement> tiposGestion = typeOfManagementDao
				.consultarTypesOfManagements();
		if (tiposGestion != null) {
			for (TypeOfManagement tGestion : tiposGestion) {
				itemsTipoGestion.add(new SelectItem(tGestion
						.getIdTypeOfManagement(), tGestion.getName()));
			}
		}
	}

	/**
	 * Method to clean the charge.
	 */
	public void limpiarResponsable() {
		this.materials.setResponsable(new Hr());
	}

	/**
	 * Method to load the selected charge.
	 * 
	 * @param responsable
	 *            : Selected object responsible.
	 */
	public void cargarResponsable(Hr responsable) {
		this.materials.setResponsable(responsable);
	}

	/**
	 * Method that allows materials to delete one database
	 * 
	 * 
	 * @return consultarMateriales: Consult the list of materials and returns to
	 *         manage materials
	 */
	public String eliminarMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			materialsDao.eliminarMateriales(materials);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					materials.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					materials.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultarMateriales();
	}

	/**
	 * Method used to save or edit the materials
	 * 
	 * @return consultarMateriales: Redirects to manage materials with a list of
	 *         updated materials
	 */
	public String guardarMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (materials.getIdMaterial() != 0) {
				materialsDao.editarMaterials(materials);
			} else {
				mensajeRegistro = "message_registro_guardar";
				materialsDao.guardarMaterials(materials);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), materials.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMateriales();
	}
}
