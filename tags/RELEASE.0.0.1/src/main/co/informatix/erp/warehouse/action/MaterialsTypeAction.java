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

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.entities.MaterialsType;

/**
 * This class allows the logic of MaterialsType which may be in the BD The logic
 * is to consult, edit or add MaterialsType
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MaterialsTypeAction implements Serializable {

	@EJB
	private MaterialsTypeDao materialsTypeDao;

	private MaterialsType materialsType;

	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	private List<MaterialsType> listaMaterialsType;
	private ArrayList<SelectItem> opciones;

	/**
	 * @return materialsType: object that contains the material data Type
	 */
	public MaterialsType getMaterialsType() {
		return materialsType;
	}

	/**
	 * @param materialsType
	 *            : object that contains the material data Type
	 */
	public void setMaterialsType(MaterialsType materialsType) {
		this.materialsType = materialsType;
	}

	/**
	 * @return paginador: Management paged list names materialsType
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list names materialsType
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: name of the material type to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : name of the material type to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return listaMaterialsType: list of objects of type material Type
	 */
	public List<MaterialsType> getListaMaterialsType() {
		return listaMaterialsType;
	}

	/**
	 * @param listaMaterialsType
	 *            : list of objects of type material Type
	 */
	public void setListaMaterialsType(List<MaterialsType> listaMaterialsType) {
		this.listaMaterialsType = listaMaterialsType;
	}

	/**
	 * @return opciones: list of the types materialsType
	 */
	public ArrayList<SelectItem> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones
	 *            : list of the types materialsType
	 */
	public void setOpciones(ArrayList<SelectItem> opciones) {
		this.opciones = opciones;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of materialsType
	 * 
	 * @return consultarMaterialsType: Consult also the materialsType method
	 *         that returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarMaterialsType();
	}

	/**
	 * Consult the list of the machines
	 * 
	 * @return "gesMaterialsType": redirects to the template to manage Materials
	 *         Type
	 */
	public String consultarMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMaterialsType = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMaterialsType = new ArrayList<MaterialsType>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = materialsTypeDao.cantidadMaterialsType(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaMaterialsType = materialsTypeDao.consultarMaterialsType(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaMaterialsType == null || listaMaterialsType.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMaterialsType == null
					|| listaMaterialsType.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMaterialsType
										.getString("materials_type_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaterialsType";
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
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(mt.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');

		}

	}

	/**
	 * Method to edit or create a new MaterialsType.
	 * 
	 * @param materialsType
	 *            :Type materials that are adding or editing
	 * 
	 * @return "regMaterialsType":redirected to the template record Material
	 *         Type.
	 */
	public String agregarEditarMaterialsType(MaterialsType materialsType) {

		if (materialsType != null) {
			this.materialsType = materialsType;

		} else {
			this.materialsType = new MaterialsType();

		}
		return "regMaterialsType";
	}

	/**
	 * Method used to save or edit materialsType
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return consultarMaterialsType: Type redirects to manage materials with a
	 *         list of updated materials Type
	 */
	public String guardarMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";

		try {
			if (materialsType.getIdMaterialsType() != 0) {
				materialsTypeDao.editarMaterialsType(materialsType);
			} else {
				mensajeRegistro = "message_registro_guardar";
				materialsTypeDao.guardarMaterialsType(materialsType);
			}
			ControladorContexto
					.mensajeInformacion(null, MessageFormat.format(
							bundle.getString(mensajeRegistro),
							materialsType.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMaterialsType();
	}

	/**
	 * Method that allows one materialsType delete the database
	 * 
	 * @return consultarMaterialsType(): Consult the list of Type material
	 *         returns to manage materials Type
	 */
	public String eliminarMaterialsType() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			materialsTypeDao.eliminarMaterialsType(materialsType);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					materialsType.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					materialsType.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMaterialsType();
	}
}
