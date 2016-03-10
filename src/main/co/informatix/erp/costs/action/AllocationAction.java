package co.informatix.erp.costs.action;

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

import co.informatix.erp.costs.dao.AllocationDao;
import co.informatix.erp.costs.entities.Allocation;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the names of the assignments they can exist.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class AllocationAction implements Serializable {

	@EJB
	private AllocationDao allocationDao;

	private Allocation allocation;
	private List<Allocation> listAllocation;
	private Paginador paginador = new Paginador();
	private String nameSearch;

	/**
	 * @return allocation: returns an assignment.
	 */
	public Allocation getAllocation() {
		return allocation;
	}

	/**
	 * @param allocation
	 *            : returns an assignment.
	 */
	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	/**
	 * @return listAllocation: list of assignments.
	 */
	public List<Allocation> getListAllocation() {
		return listAllocation;
	}

	/**
	 * @param listAllocation
	 *            : list of assignments.
	 */
	public void setListAllocation(List<Allocation> listAllocation) {
		this.listAllocation = listAllocation;
	}

	/**
	 * @return paginador: Management paged list of assignments.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of assignments.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nameSearch: Name of the assignment to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of the assignment to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of assignments.
	 * 
	 * @return consultAllocation: Method to initialize the parameters of the
	 *         search and load the initial list of assignments.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultAllocation();
	}

	/**
	 * Consult the list of assignments.
	 * 
	 * @return "gesAllocation": navigation rule to manage allocation
	 */
	public String consultAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listAllocation = new ArrayList<Allocation>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = allocationDao.quantityAllocation(query, parameters);
			if (quantity != null) {
				paginador.paginar(quantity);
			}
			listAllocation = allocationDao.consultAllocation(
					paginador.getInicio(), paginador.getRango(), query,
					parameters);
			if ((listAllocation == null || listAllocation.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listAllocation == null || listAllocation.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("tipo_recurso_humano_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesAllocation";
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(a.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new assignment.
	 * 
	 * @param allocation
	 *            : assignment to be adding or editing.
	 * @return regAllocation: redirected to the page register allocation.
	 */
	public String addEditAllocation(Allocation allocation) {
		if (allocation != null) {
			this.allocation = allocation;
		} else {
			this.allocation = new Allocation();
		}
		return "regAllocation";
	}

	/**
	 * Method used to save or edit the assignments.
	 * 
	 * @return consultAllocation: redirects to manage staff assignments with the
	 *         updated list of assignments.
	 */
	public String saveAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (allocation.getIdAllocation() != 0) {
				allocationDao.editAllocation(allocation);
			} else {
				messageLog = "message_registro_guardar";
				allocationDao.saveAllocation(allocation);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), allocation.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultAllocation();
	}

	/**
	 * Method to remove an allocation of the database.
	 * 
	 * @return consultAllocation: Consult the list of allocation and returns to
	 *         manage allocation.
	 */
	public String removeAllocation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			allocationDao.removeAllocation(allocation);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					allocation.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					allocation.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultAllocation();
	}

	/**
	 * To validate the name of the allocation, to not repeat in the database and
	 * validates against XSS.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param context
	 *            : Application context.
	 * 
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
			int id = allocation.getIdAllocation();
			Allocation allocationTypeAux = new Allocation();
			allocationTypeAux = allocationDao.nameExists(name, id);
			if (allocationTypeAux != null) {
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
