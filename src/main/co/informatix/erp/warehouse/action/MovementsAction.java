package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.TransactionType;
import co.informatix.erp.warehouse.entities.Transactions;
import co.informatix.security.action.IdentityAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the movements that may exist.
 * 
 * @author Wilhelm.Boada
 * @modify Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MovementsAction implements Serializable {

	private List<Activities> activitiesList;
	private Paginador pagination = new Paginador();

	/**
	 * @return activitiesList: list of activity objects.
	 */
	public List<Activities> getActivitiesList() {
		return activitiesList;
	}

	/**
	 * @param activitiesList
	 *            : list of activity objects.
	 */
	public void setActivitiesList(List<Activities> activitiesList) {
		this.activitiesList = activitiesList;
	}

	/**
	 * @return pagination: Management paged list of activities.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of activities.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	// Field To Logic Expiration

	private Paginador paginationExpire = new Paginador();

	@EJB
	private DepositsDao depositsDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;
	@Resource
	private UserTransaction userTransaction;
	@Inject
	private IdentityAction identity;
	@EJB
	private TransactionsDao transactionsDao;
	private boolean selected = false;

	private Integer rangeExpiration;
	private String nameSearch;

	private List<Deposits> listDepositsExpired;
	private List<Deposits> listDepositsExpiredSelected;
	private List<Integer> rangeItems;

	/**
	 * @return paginationExpire :Management paged list of deposits expire.
	 */
	public Paginador getPaginationExpire() {
		return paginationExpire;
	}

	/**
	 * @param paginationExpire
	 *            :Management paged list of deposits expire.
	 */
	public void setPaginationExpire(Paginador paginationExpire) {
		this.paginationExpire = paginationExpire;
	}

	/**
	 * @return rangeExpiration: number of the day that user selected to consult
	 *         the materials expired
	 */
	public Integer getRangeExpiration() {
		return rangeExpiration;
	}

	/**
	 * @return nameSearch : Material name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Material name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @param rangeExpiration
	 *            :number of the day that user selected to consult the materials
	 *            expired
	 */
	public void setRangeExpiration(Integer rangeExpiration) {
		this.rangeExpiration = rangeExpiration;
	}

	/**
	 * @return listDepositsExpired : List of the deposits with materials expired
	 */
	public List<Deposits> getListDepositsExpired() {
		return listDepositsExpired;
	}

	/**
	 * @param listDepositsExpired
	 *            :List of the deposits with materials expired
	 */
	public void setListDepositsExpired(List<Deposits> listDepositsExpired) {
		this.listDepositsExpired = listDepositsExpired;
	}

	/**
	 * @return listDepositsExpiredSelected :List of the deposits selected with
	 *         materials expired
	 */
	public List<Deposits> getListDepositsExpiredSelected() {
		return listDepositsExpiredSelected;
	}

	/**
	 * @param listDepositsExpiredSelected
	 *            :List of the deposits selected with materials expired
	 */
	public void setListDepositsExpiredSelected(
			List<Deposits> listDepositsExpiredSelected) {
		this.listDepositsExpiredSelected = listDepositsExpiredSelected;
	}

	/**
	 * @return selected : boolean flag to mark all deposits selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            :boolean flag to mark all deposits selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return rangeItems :List of items from expiration of movements are loaded
	 *         into the combo in the user interface.
	 */
	public List<Integer> getRangeItems() {
		return rangeItems;
	}

	/**
	 * @param rangeItems
	 *            :List of items from expiration of movements are loaded into
	 *            the combo in the user interface.
	 */
	public void setRangeItems(List<Integer> rangeItems) {
		this.rangeItems = rangeItems;
	}

	/**
	 * This method allow initialize the parameter of the consult and get the
	 * navigation rule to manage the expiration of the materials
	 * 
	 * @return gesExpiration: Navigation rules to redirect a manage of materials
	 *         expired
	 */
	public String initializeExpirationConsult() {
		this.rangeExpiration = 0;
		this.selected = false;
		this.nameSearch = "";
		loadRange();
		consultExpirationMaterials();
		return "gesExpiration";
	}

	/**
	 * This method allows load the range for the select list
	 */
	public void loadRange() {
		this.rangeItems = new ArrayList<>();
		rangeItems.add(5);
		rangeItems.add(10);
		rangeItems.add(20);
		rangeItems.add(30);
		rangeItems.add(60);
		rangeItems.add(90);
		rangeItems.add(120);
	}

	/**
	 * This method allows consult the list of the materials expired according a
	 * range selected by the user
	 */
	public void consultExpirationMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listDepositsExpired = new ArrayList<Deposits>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder allMessageSearch = new StringBuilder();
		String messageSearch = "";
		advanceSearchExpiration(consult, parameters, bundle, allMessageSearch);
		try {
			Long quantity = depositsDao.amountDeposits(consult, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
				listDepositsExpired = depositsDao.consultDeposits(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
			if ((listDepositsExpired == null || listDepositsExpired.size() <= 0)
					&& !"".equals(allMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								allMessageSearch);
			} else if (listDepositsExpired == null
					|| listDepositsExpired.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(allMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("deposits_label"),
								allMessageSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
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
	 */
	private void advanceSearchExpiration(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		Date expireDate = new Date();
		if (this.rangeExpiration != 0) {
			expireDate = ControladorFechas.setDay(rangeExpiration, null);
			unionMessagesSearch.append(bundleWarehouse
					.getString("movements_label_expire_in")
					+ ": "
					+ '"'
					+ this.rangeExpiration
					+ '"'
					+ " "
					+ bundle.getString("label_days"));
		}
		consult.append("WHERE d.expireDate <= :expireDate ");
		consult.append("AND d.actualQuantity > 0 ");
		SelectItem item = new SelectItem(expireDate, "expireDate");
		parameters.add(item);

		if ((this.nameSearch != null && !"".equals(this.nameSearch))) {
			consult.append("AND UPPER(m.name) LIKE UPPER(:keywordName) ");
			SelectItem itemNombre = new SelectItem("%" + this.nameSearch + "%",
					"keywordName");
			parameters.add(itemNombre);
			unionMessagesSearch.append(bundleWarehouse
					.getString("materials_label")
					+ ": "
					+ '"'
					+ this.nameSearch + '"' + " ");
		}

	}

	/**
	 * This method allows select all the deposits in the view
	 */
	public void selectAll() {
		try {
			selected = selected ? false : true;
			for (Deposits d : this.listDepositsExpired) {
				d.setSelected(selected);
			}
			setListDepositsExpiredSelected();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate if all deposits was selected by the user
	 */
	public void validateSelected() {
		try {
			for (Deposits deposit : this.listDepositsExpired) {
				if (!deposit.isSelected()) {
					selected = false;
					break;
				}
				selected = true;
			}
			setListDepositsExpiredSelected();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows set the list deposits expire selected by the user
	 * 
	 * @throws Exception
	 * 
	 */
	public void setListDepositsExpiredSelected() {
		this.listDepositsExpiredSelected = new ArrayList<>();
		if (selected) {
			this.listDepositsExpiredSelected = this.listDepositsExpired;
		} else {
			for (Deposits d : this.listDepositsExpired) {
				if (d.isSelected()) {
					this.listDepositsExpiredSelected.add(d);
				}
			}
		}
		try {
			Long paginationAmount = (long) this.listDepositsExpiredSelected
					.size();
			this.paginationExpire.paginarRangoDefinido(paginationAmount, 5);
			int inicial = this.paginationExpire.getItemInicial() - 1;
			int fin = this.paginationExpire.getItemFinal();
			this.listDepositsExpiredSelected = this.listDepositsExpiredSelected
					.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates fields that are required in view of registering adjustment
	 * expire
	 */
	public void validateDepositSelected() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		if (listDepositsExpiredSelected == null
				|| listDepositsExpiredSelected.size() <= 0) {
			ControladorContexto.mensajeError(null, null,
					bundle.getString("message_select_one_deposit_expired"));
		}
	}

	/**
	 * This method allows adjust the list of deposits selected by user as
	 * expired
	 */
	public String adjustedDepositsAsExpired() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		try {
			this.userTransaction.begin();
			for (Deposits deposit : this.listDepositsExpiredSelected) {
				Transactions transactions = new Transactions();
				Double quantity = deposit.getActualQuantity();
				deposit.setActualQuantity(0d);
				TransactionType transactionType = transactionTypeDao
						.transactionTypeById(Constantes.IDENTIFIER_ADJUSTEMENT_ADJUST_TYPE_EXPIRED);
				depositsDao.editDeposits(deposit);
				transactions.setTransactionType(transactionType);
				transactions.setUserName(identity.getUserName());
				transactions.setDeposits(deposit);
				transactions.setDateTime(new Date());
				transactions.setQuantity(quantity);
				transactionsDao.saveTransaction(transactions);
			}
			userTransaction.commit();
			ControladorContexto
					.mensajeInformacion(
							null,
							bundle.getString("movements_message_expired_deposit_succesfully"));

		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.printErrorLog(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return initializeExpirationConsult();
	}

}
