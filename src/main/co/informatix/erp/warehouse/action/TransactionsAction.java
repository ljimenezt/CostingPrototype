package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Transactions;

/**
 * This class is all related logic with the management of transactions
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TransactionsAction implements Serializable {

	@EJB
	private TransactionsDao transactionsDao;

	private List<Transactions> listTransactions;
	private Paginador paginador = new Paginador();
	private Deposits depositSelected;

	/**
	 * @return listTransactions: list of transactions objects
	 */
	public List<Transactions> getListTransactions() {
		return listTransactions;
	}

	/**
	 * @param listTransactions: list of transactions objects
	 */
	public void setListTransactions(List<Transactions> listTransactions) {
		this.listTransactions = listTransactions;
	}

	/**
	 * @return paginador: Pager for the transactions list
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador: Pager for the transactions list
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return depositSelected: deposit object
	 */
	public Deposits getDepositSelected() {
		return depositSelected;
	}

	/**
	 * @param depositSelected: deposit object
	 */
	public void setDepositSelected(Deposits depositSelected) {
		this.depositSelected = depositSelected;
	}

	/**
	 * Consult the list of transactions
	 */
	public void consultTransaction() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listTransactions = new ArrayList<Transactions>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			advancedSearchTransactions(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = transactionsDao.quantityTransactions(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			if (cantidad != null && cantidad > 0) {
				this.listTransactions = transactionsDao.consultTransactions(
						paginador.getInicio(), paginador.getRango(), consulta,
						parametros);
			}
			if ((listTransactions == null || listTransactions.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listTransactions == null || listTransactions.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRecursosHumanos
										.getString("deposits_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

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
	private void advancedSearchTransactions(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.depositSelected != null) {
			consult.append("WHERE t.deposits = :deposit ");
			SelectItem item = new SelectItem(this.depositSelected, "deposit");
			parameters.add(item);
		}
	}

}
