package co.informatix.erp.warehouse.action;

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

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class implements the logic business for creating and updating the system
 * suppliers.
 * 
 * @author Mabell.Boada
 * 
 */

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class SuppliersAction implements Serializable {

	private List<Suppliers> suppliersList;
	private Paginador paginador = new Paginador();
	private Suppliers suppliers;
	private String nameSearch;

	@EJB
	private SuppliersDao suppliersDao;

	/**
	 * @return suppliersList: List of providers.
	 */
	public List<Suppliers> getSuppliersList() {
		return suppliersList;
	}

	/**
	 * @param suppliersList
	 *            : List of providers.
	 */
	public void setSuppliersList(List<Suppliers> suppliersList) {
		this.suppliersList = suppliersList;
	}

	/**
	 * @return paginador: Paging from the list of providers who may be in the
	 *         view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paging from the list of providers who may be in the view.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return suppliers: Object provider.
	 */
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            : Object provider.
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return nameSearch: Name of a supplier.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of a supplier.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of suppliers.
	 * 
	 * @return searchSuppliers: method to look for suppliers, redirects to the
	 *         suppliers management template.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchSuppliers();
	}

	/**
	 * Get a list of providers.
	 * 
	 * @return gesSuppliers: Navigation rule that redirects to manage suppliers.
	 */
	public String searchSuppliers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		suppliersList = new ArrayList<Suppliers>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = suppliersDao
					.suppliersAmount(queryBuilder, parameters);
			if (amount != null) {
				paginador.paginar(amount);
			}
			suppliersList = suppliersDao.suppliersFilteredSearch(
					paginador.getInicio(), paginador.getRango(), queryBuilder,
					parameters);
			if ((suppliersList == null || suppliersList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (suppliersList == null || suppliersList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("suppliers_label"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesSuppliers";
	}

	/**
	 * This method builds the query for an advanced search and allows to
	 * construct messages displayed depending on the search criteria selected by
	 * the user.
	 * 
	 * @param queryBuilder
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param jointSearchMessages
	 *            : Search message.
	 * 
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			queryBuilder.append("WHERE UPPER(s.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new provider.
	 * 
	 * @param suppliers
	 *            : Provider object that you are adding or editing.
	 * 
	 * @return regSuppliers: Template that redirects to register suppliers.
	 * 
	 */
	public String addEditSuppliers(Suppliers suppliers) {
		if (suppliers != null) {
			this.suppliers = suppliers;
		} else {
			this.suppliers = new Suppliers();
		}
		return "regSuppliers";
	}

	/**
	 * To validate the names of the suppliers, so it is not repeated in the
	 * database and it validates against XSS.
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
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = suppliers.getIdSupplier();
			Suppliers suppliersAux = new Suppliers();
			suppliersAux = suppliersDao.nameExists(name, id);
			if (suppliersAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
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
	 * Method used to save or edit providers.
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return searchSuppliers: Redirects to manage suppliers with the list of
	 *         names updated.
	 */
	public String saveSupplier() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (suppliers.getIdSupplier() != 0) {
				suppliersDao.editSuppliers(suppliers);
			} else {
				mensajeRegistro = "message_registro_guardar";
				suppliersDao.saveSuppliers(suppliers);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), suppliers.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchSuppliers();
	}

	/**
	 * Method to delete a provider of database.
	 * 
	 * 
	 * @return searchSuppliers: Consult the list of providers and redirects to
	 *         suppliers manage template.
	 */
	public String deleteSupplier() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			suppliersDao.deleteSuppliers(suppliers);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					suppliers.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					suppliers.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchSuppliers();
	}

}
