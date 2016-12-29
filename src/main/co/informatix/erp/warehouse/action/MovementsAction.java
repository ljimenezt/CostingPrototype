package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.dao.ActivityMaterialsDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMaterials;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControllerAccounting;
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

	@EJB
	private DepositsDao depositsDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;
	@EJB
	private TransactionsDao transactionsDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private ActivityMaterialsDao activityMaterialsDao;
	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private List<Deposits> listDepositsExpired;
	private List<Deposits> listDepositsExpiredSelected;
	private List<Integer> rangeItems;
	private List<Activities> activitiesList;
	private List<ActivityMaterials> listActivityMaterials;
	private List<ActivityMaterials> listActivityMaterialsSelected;
	private List<Transactions> listMaterialsTransaction;
	private HashMap<Integer, String> humanMaterialMap;
	private Paginador pagination = new Paginador();
	private Paginador paginationExpire = new Paginador();
	private Activities activityActualSelected;
	private ActivityMaterials activityMaterials;
	private Hr hr;
	private Integer rangeExpiration;
	private Integer rangeExpirationAux;
	private String nameSearch;
	private String nameSearchMaterials;
	private boolean selected = false;
	private boolean isReturns = false;

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
	 * @return listActivityMaterials: material list assigned to the activity.
	 */
	public List<ActivityMaterials> getListActivityMaterials() {
		return listActivityMaterials;
	}

	/**
	 * @param listActivityMaterials
	 *            : material list assigned to the activity.
	 */
	public void setListActivityMaterials(
			List<ActivityMaterials> listActivityMaterials) {
		this.listActivityMaterials = listActivityMaterials;
	}

	/**
	 * @return listActivityMaterialsSelected: material list selected to the
	 *         activity.
	 */
	public List<ActivityMaterials> getListActivityMaterialsSelected() {
		return listActivityMaterialsSelected;
	}

	/**
	 * @param listActivityMaterialsSelected
	 *            : material list selected to the activity.
	 */
	public void setListActivityMaterialsSelected(
			List<ActivityMaterials> listActivityMaterialsSelected) {
		this.listActivityMaterialsSelected = listActivityMaterialsSelected;
	}

	/**
	 * @return listMaterialsTransaction : List of the materials of have a
	 *         transaction withdraw the current day
	 */
	public List<Transactions> getListMaterialsTransaction() {
		return listMaterialsTransaction;
	}

	/**
	 * @param listMaterialsTransaction
	 *            :List of the materials of have a transaction withdraw the
	 *            current day
	 */
	public void setListMaterialsTransaction(
			List<Transactions> listMaterialsTransaction) {
		this.listMaterialsTransaction = listMaterialsTransaction;
	}

	/**
	 * @return humanMaterialMap: Save the responsible of the materials
	 *         withdrawal.
	 */
	public HashMap<Integer, String> getHumanMaterialMap() {
		return humanMaterialMap;
	}

	/**
	 * @param humanMaterialMap
	 *            : Save the responsible of the materials withdrawal.
	 */
	public void setHumanMaterialMap(HashMap<Integer, String> humanMaterialMap) {
		this.humanMaterialMap = humanMaterialMap;
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
	 * @return activityActualSelected: activity selected in the activity table.
	 */
	public Activities getActivityActualSelected() {
		return activityActualSelected;
	}

	/**
	 * @param activityActualSelected
	 *            : activity selected in the activity table.
	 */
	public void setActivityActualSelected(Activities activityActualSelected) {
		this.activityActualSelected = activityActualSelected;
	}

	/**
	 * @return activityMaterials: activityMaterial for the transaction.
	 */
	public ActivityMaterials getActivityMaterials() {
		return activityMaterials;
	}

	/**
	 * @param activityMaterials
	 *            : activityMaterial for the transaction.
	 */
	public void setActivityMaterials(ActivityMaterials activityMaterials) {
		this.activityMaterials = activityMaterials;
	}

	/**
	 * @return hr: hr belonging to the transaction.
	 */
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : hr belonging to the transaction.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return rangeExpiration: number of the day that user selected to consult
	 *         the materials expired
	 */
	public Integer getRangeExpiration() {
		return rangeExpiration;
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
	 * @return rangeExpirationAux: number of the day that user selected outside
	 *         the set range to consult the materials expired
	 */
	public Integer getRangeExpirationAux() {
		return rangeExpirationAux;
	}

	/**
	 * @param rangeExpirationAux
	 *            : number of the day that user selected outside the set range
	 *            to consult the materials expired
	 */
	public void setRangeExpirationAux(Integer rangeExpirationAux) {
		this.rangeExpirationAux = rangeExpirationAux;
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
	 * @return nameSearchMaterials : Material name to search.
	 */
	public String getNameSearchMaterials() {
		return nameSearchMaterials;
	}

	/**
	 * @param nameSearchMaterials
	 *            : Material name to search.
	 */
	public void setNameSearchMaterials(String nameSearchMaterials) {
		this.nameSearchMaterials = nameSearchMaterials;
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
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultActivities: Method that consultation activities and load
	 *         the template with the information found.
	 */
	public String initializeWithdraws() {
		cleanparameters();
		return consultActivities();
	}

	/**
	 * This method allows clean the variables of this class
	 */
	private void cleanparameters() {
		this.rangeExpiration = 0;
		this.rangeExpirationAux = 0;
		this.selected = false;
		this.nameSearch = "";
		this.listActivityMaterialsSelected = new ArrayList<ActivityMaterials>();
		this.listActivityMaterials = new ArrayList<ActivityMaterials>();
		this.activityActualSelected = null;
		this.hr = new Hr();
		pagination = new Paginador();
	}

	/**
	 * See the existing activities list.
	 * 
	 * @return gesWithdraws: Navigation rule that redirects to manage the
	 *         withdraws.
	 */
	public String consultActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleCostos = ControladorContexto
				.getBundle("messageCosts");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		try {
			advancedSearch(query, parameters, bundle, unionSearchMessages);
			Long quantity = activitiesDao.amountActivitiesByActivityMaterials(
					query, parameters);
			if (quantity != null && quantity > 0) {
				pagination.paginar(quantity);

				this.activitiesList = activitiesDao
						.queryActivitiesByActivityMaterials(
								pagination.getInicio(), pagination.getRango(),
								query, parameters);
			}
			if ((this.activitiesList == null || activitiesList.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (activitiesList == null || activitiesList.size() <= 0) {
				ControladorContexto
						.mensajeInfoEspecifico(
								"movements_message_no_activities_for_withdraw_materials",
								"mensajeWarehouse");
			} else if (!"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleCostos.getString("activities_label_s"),
								unionSearchMessages);
			}
			validations.setMensajeBusqueda(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesWithdraws";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
		consult.append("AND TO_CHAR(a.initialDtBudget,'YYYY-MM-dd') = :date ");
		SelectItem item2 = new SelectItem(
				ControladorFechas
						.getFechaActual(Constantes.DATE_FORMAT_CONSULT),
				"date");
		parameters.add(item2);
	}

	/**
	 * Selects a single activity for display the associated transaction.
	 * 
	 * @param activitySelected
	 *            : activity selected on the view.
	 */
	public void selectActivity(Activities activitySelected) {
		this.activityActualSelected = new Activities();
		this.listActivityMaterialsSelected = new ArrayList<ActivityMaterials>();
		activitySelected.setSeleccionado(true);
		for (Activities activity : activitiesList) {
			if (activity.isSeleccionado()) {
				if (activity.getIdActivity() == activitySelected
						.getIdActivity()) {
					this.activityActualSelected = activity;
				} else {
					activity.setSeleccionado(false);
				}
			}
		}
	}

	/**
	 * Method to clean the HR associated with the transaction.
	 */
	public void cleanHr() {
		this.hr = new Hr();
	}

	/**
	 * Method to load the selected HR.
	 * 
	 * @param hr
	 *            : object HR selected.
	 */
	public void loadHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * Method to initialize the fields in the search.
	 */
	public void initializeMaterialsByActivity() {
		this.nameSearchMaterials = "";
		consultMaterialsByActivity();
	}

	/**
	 * Consult the material list associated to the activity.
	 * 
	 */
	public void consultMaterialsByActivity() {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		String messageSearch = "";
		StringBuilder unionSearchMessages = new StringBuilder();
		try {
			advancedSearchMaterialsByActivity(consult, parameters, bundle,
					unionSearchMessages);
			Long quantity = activityMaterialsDao.amountMaterialsByActivity(
					consult, parameters);
			if (quantity != null) {
				paginationExpire.paginarRangoDefinido(quantity, 5);
				this.listActivityMaterials = activityMaterialsDao
						.queryMaterialsByActivity(paginationExpire.getInicio(),
								paginationExpire.getRango(), consult,
								parameters);
				List<Transactions> transactionsList = transactionsDao
						.consultTransactionsByActivityAndTransactionType(
								this.activityActualSelected.getIdActivity(),
								Constantes.TRANSACTION_TYPE_ID_WITHDRAWAL);
				humanMaterialMap = new HashMap<Integer, String>();
				if (transactionsList != null) {
					for (Transactions transactions : transactionsList) {
						Hr hr = transactions.getHr();
						humanMaterialMap.put(transactions.getDeposits()
								.getMaterials().getIdMaterial(), hr.getName()
								+ " " + hr.getFamilyName());
					}
				}
			}
			if (this.listActivityMaterials != null
					&& this.listActivityMaterials.size() >= 0) {
				persistentActivityMaterialsSelected();
			}
			if ((listActivityMaterials == null || listActivityMaterials.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (listActivityMaterials == null
					|| listActivityMaterials.size() <= 0) {
				messageSearch = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				messageSearch = MessageFormat.format(message,
						bundleWarehouse.getString("materials_label_s"),
						unionSearchMessages);
			}
			validations.setMensajeBusquedaPopUp(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds the query for an advanced search for relations between
	 * activities and materials, it also builds display messages depending on
	 * the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of the search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearchMaterialsByActivity(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.activityActualSelected.getIdActivity() != 0) {
			consult.append("WHERE ac.idActivity = :id ");
			SelectItem item = new SelectItem(
					this.activityActualSelected.getIdActivity(), "id");
			parameters.add(item);
		}
		if (this.nameSearchMaterials != null
				&& !"".equals(this.nameSearchMaterials)) {
			consult.append("AND UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearchMaterials
					+ "%", "keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearchMaterials + '"');
		}
	}

	/**
	 * Selects a single activityMaterials for display the associated
	 * transaction.
	 * 
	 * @param activityMaterials
	 *            : this field validate if activityMaterials is selected.
	 */
	public void selectActivityMaterials(ActivityMaterials activityMaterials) {
		try {
			if (activityMaterials.isSelected()) {
				Double materialQuantity;
				ValidacionesAction validations = (ValidacionesAction) ControladorContexto
						.getContextBean(ValidacionesAction.class);
				ResourceBundle bundle = ControladorContexto
						.getBundle("mensajeWarehouse");
				materialQuantity = depositsDao.quantityMaterialsById(
						activityMaterials.getActivityMaterialsPK()
								.getMaterials().getIdMaterial(), null);

				if (materialQuantity == null
						|| materialQuantity < activityMaterials
								.getQuantityBudget()) {
					String searchMessages = MessageFormat
							.format(bundle
									.getString("deposits_message_not_enough_materials"),
									activityMaterials.getActivityMaterialsPK()
											.getMaterials().getName());
					activityMaterials.setSelected(false);
					validations.setMensajeBusquedaPopUp(searchMessages);
				} else {
					this.listActivityMaterialsSelected.add(activityMaterials);
				}
			} else {
				this.listActivityMaterialsSelected.remove(activityMaterials);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows maintaining list of selected activityMaterials whether
	 * the search of activityMaterials is run again.
	 * 
	 */
	public void persistentActivityMaterialsSelected() {
		for (ActivityMaterials activityMaterial : listActivityMaterials) {
			for (ActivityMaterials activityMaterialSelected : listActivityMaterialsSelected) {
				if (activityMaterial.getActivityMaterialsPK().getMaterials()
						.getIdMaterial() == activityMaterialSelected
						.getActivityMaterialsPK().getMaterials()
						.getIdMaterial()) {
					activityMaterial.setSelected(true);
				}
			}
		}
	}

	/**
	 * This method allows create the withdrawal transaction and edit the
	 * deposits for save in the database.
	 */
	public void createTransactionWithdrawal() {
		try {
			if (this.listActivityMaterialsSelected != null
					&& this.listActivityMaterialsSelected.size() >= 0) {
				for (ActivityMaterials activityMaterials : this.listActivityMaterialsSelected) {
					double costActual = 0.0;
					if (activityMaterials.getQuantityBudget() > 0) {
						double amount = activityMaterials.getQuantityBudget();
						List<Deposits> depositsListActual = depositsDao
								.consultDepositsActualQuantityById(activityMaterials
										.getActivityMaterialsPK()
										.getMaterials().getIdMaterial());
						TransactionType transactionType = transactionTypeDao
								.transactionTypeById(Constantes.TRANSACTION_TYPE_ID_WITHDRAWAL);
						while (amount > 0) {
							Transactions transactions = new Transactions();
							Deposits depositsActual = depositsListActual.get(0);
							if (amount > depositsActual.getActualQuantity()) {
								costActual = ControllerAccounting
										.add(costActual, ControllerAccounting
												.multiply(depositsActual
														.getActualQuantity(),
														depositsActual
																.getUnitCost()));
								transactions.setQuantity(depositsActual
										.getActualQuantity());
								amount = ControllerAccounting.subtract(amount,
										depositsActual.getActualQuantity());
								depositsListActual.remove(depositsActual);
								depositsActual.setActualQuantity(0.0);
							} else {
								costActual = ControllerAccounting
										.add(costActual, ControllerAccounting
												.multiply(amount,
														depositsActual
																.getUnitCost()));
								transactions.setQuantity(amount);
								depositsListActual.remove(depositsActual);
								depositsActual
										.setActualQuantity(ControllerAccounting
												.subtract(depositsActual
														.getActualQuantity(),
														amount));
								amount = 0;
							}
							transactions.setTransactionType(transactionType);
							transactions.setDateTime(new Date());
							transactions.setDeposits(depositsActual);
							transactions.setHr(hr);
							transactions
									.setActivities(this.activityActualSelected);
							transactions.setUserName(identity.getUserName());
							depositsDao.editDeposits(depositsActual);
							transactionsDao.saveTransaction(transactions);
						}
						activityMaterials.setCostActual(costActual);
						activityMaterials.setQuantityActual(activityMaterials
								.getQuantityBudget());
						activityMaterialsDao
								.editActivityMaterials(activityMaterials);
					}
				}
			}
			ControladorContexto.mensajeInfoArg2(
					"movements_message_withdrawal_materials_succesfully",
					"mensajeWarehouse", this.activityActualSelected
							.getActivityName().getActivityName());
			consultMaterialsByActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate if be can add more workers to the team.
	 */
	public void validateRequired() {
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");

		if (hr.getName() == null || hr.getName().equals("")) {
			String field = isReturns ? "formReturns:txtHrs"
					: "formWithdraws:txtHrs";
			ControladorContexto.mensajeRequeridos(field);
		}
		if (this.listActivityMaterialsSelected == null
				|| this.listActivityMaterialsSelected.size() <= 0) {
			String searchMessage = bundle
					.getString("movements_message_no_materials_selected");
			validations.setMensajeBusquedaPopUp(searchMessage);
		}
		if (isReturns) {
			boolean flagError = true;
			for (ActivityMaterials am : listActivityMaterialsSelected) {
				if (am.isSelected()) {
					flagError = false;
					break;
				}
			}
			String message = "movements_message_no_material_selected_return";
			if (flagError) {
				ControladorContexto.mensajeError(null, null,
						bundle.getString(message));
			}
		}
	}

	/**
	 * This method allow initialize the parameter of the consult and get the
	 * navigation rule to manage the expiration of the materials
	 * 
	 * @return gesExpiration: Navigation rules to redirect a manage of materials
	 *         expired
	 */
	public String initializeExpirationConsult() {
		cleanparameters();
		loadRange();
		consultExpirationMaterials();
		return "gesExpiration";
	}

	/**
	 * This method allows load the range for the select list
	 */
	public void loadRange() {
		this.rangeItems = new ArrayList<Integer>();
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
		if (this.rangeExpiration < 0 && this.rangeExpiration == 0) {
			return;
		}
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
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advanceSearchExpiration(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		Date expireDate = new Date();
		Integer rangeE = this.rangeExpiration > 0 ? this.rangeExpiration
				: this.rangeExpirationAux > 0 ? this.rangeExpirationAux : 0;
		if (rangeE != 0) {
			expireDate = ControladorFechas.setDay(rangeE, null, true);
			unionMessagesSearch.append(bundleWarehouse
					.getString("movements_label_expire_in")
					+ ": "
					+ '"'
					+ rangeE + '"' + " " + bundle.getString("label_days"));
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
	 * This method allows set the list deposits expire selected by the user.
	 * 
	 */
	public void setListDepositsExpiredSelected() {
		this.listDepositsExpiredSelected = new ArrayList<Deposits>();
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
	 * 
	 * @return gesExpiration: Navigation rules to redirect a manage of materials
	 *         expired
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
						.transactionTypeById(Constantes.TRANSACTION_TYPE_EXPIRED);
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

	/**
	 * This method allow initialize the parameter of the consult and get the
	 * navigation rule to manage the returns of the materials
	 * 
	 * @return gesExpiration: Navigation rules to redirect a manage of materials
	 *         returns.
	 */
	public String initializeReturns() {
		cleanparameters();
		isReturns = true;
		consultMaterialsTransactionToday();
		return "gesReturns";
	}

	/**
	 * This method allow consult the materials with transaction like withdraw in
	 * the current day
	 * 
	 */
	public void consultMaterialsTransactionToday() {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String messageSearch = "";
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		try {
			if (!fromModal) {
				List<ActivityMaterials> listActivityM = activityMaterialsDao
						.consultActivityMaterialInTrasaction();
				if (listActivityM != null) {
					for (ActivityMaterials activityMaterial : listActivityM) {
						int idActivity = activityMaterial
								.getActivityMaterialsPK().getActivities()
								.getIdActivity();
						int idMaterial = activityMaterial
								.getActivityMaterialsPK().getMaterials()
								.getIdMaterial();
						activityMaterial.setSumQuantityReturn(transactionsDao
								.consultSumReturnByActivityMaterial(idActivity,
										idMaterial,
										Constantes.TRANSACTION_TYPE_RETURN));
					}
					setTotalQuantityWithdrawn(listActivityM);
				}
			} else {
				this.listActivityMaterials = this.listActivityMaterialsSelected;
			}
			if (listActivityMaterials.size() > 0) {
				Long amount = (long) listActivityMaterials.size();
				pagination.paginar(amount);
				int inicial = this.pagination.getItemInicial() - 1;
				int fin = this.pagination.getItemFinal();
				this.listActivityMaterials = listActivityMaterials.subList(
						inicial, fin);
			}
			if (listActivityMaterials == null
					|| listActivityMaterials.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate and add the flag selected to another list of
	 * the activity materials
	 * 
	 * @param activityM
	 *            : object to change the values to selected
	 */
	public void validateSelectionActivityMaterial(ActivityMaterials activityM) {
		boolean flag = activityM.isSelected();
		int idActivity = activityM.getActivityMaterialsPK().getActivities()
				.getIdActivity();
		int idMaterial = activityM.getActivityMaterialsPK().getMaterials()
				.getIdMaterial();
		for (ActivityMaterials am : this.listActivityMaterialsSelected) {
			int auxIdActivity = am.getActivityMaterialsPK().getActivities()
					.getIdActivity();
			int auxIdMaterial = am.getActivityMaterialsPK().getMaterials()
					.getIdMaterial();
			if (idActivity == idMaterial && auxIdActivity == auxIdMaterial) {
				am.setSelected(flag);
			}
		}
	}

	/**
	 * This method allows consult the total quantity withdraw of every activity
	 * materials
	 * 
	 * @param listActivityM
	 *            : list of materials activity to consult the total quantity
	 *            withdraw
	 * @throws Exception
	 */
	private void setTotalQuantityWithdrawn(List<ActivityMaterials> listActivityM)
			throws Exception {
		for (ActivityMaterials am : listActivityM) {
			int idActivity = am.getActivityMaterialsPK().getActivities()
					.getIdActivity();
			int idMaterial = am.getActivityMaterialsPK().getMaterials()
					.getIdMaterial();
			Double quantityWithdrawn = transactionsDao.totalQuantityWithdrawn(
					idActivity, idMaterial);
			am.setQuantityWithdrawn(quantityWithdrawn);
		}
		this.listActivityMaterials = listActivityM;
		this.listActivityMaterialsSelected = listActivityM;
	}

	/**
	 * This method allows save the material activity returned by the user
	 * creating a transaction editing the deposit of the material
	 * 
	 * @return initializeReturns(): Initialize the values of the consult.
	 */
	public String saveReturnMaterialActivity() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		try {
			this.userTransaction.begin();
			for (ActivityMaterials am : this.listActivityMaterialsSelected) {
				if (am.isSelected()) {
					int idMaterial = am.getActivityMaterialsPK().getMaterials()
							.getIdMaterial();
					int idActivity = am.getActivityMaterialsPK()
							.getActivities().getIdActivity();
					Double returned = am.getQuantityReturn();
					Double withdraw = am.getQuantityActual();
					Double quantityActual = ControllerAccounting.subtract(
							withdraw, returned);
					am.setQuantityActual(quantityActual);
					TransactionType transactionType = transactionTypeDao
							.transactionTypeById(Constantes.TRANSACTION_TYPE_RETURN);
					List<Deposits> listDeposits = depositsDao
							.consultDepositsToReturns(idMaterial, idActivity);
					int count = 0;
					double costActual = am.getCostActual();
					while (returned > 0) {
						Transactions transaction = new Transactions();
						Deposits deposit = listDeposits.get(count);
						Transactions transactionAux = transactionsDao
								.consultTransactionsByDepositsActivityAndTransactionType(
										deposit.getIdDeposit(), idActivity,
										Constantes.TRANSACTION_TYPE_WITHDRAWAL);
						Double depositQuantity = deposit.getActualQuantity();
						Double unitCost = deposit.getUnitCost();
						if (returned > transactionAux.getQuantity()) {
							returned = ControllerAccounting.add(returned,
									depositQuantity);
							transaction.setQuantity(depositQuantity);
							deposit.setActualQuantity(0d);
							costActual = ControllerAccounting.subtract(
									costActual, ControllerAccounting.multiply(
											depositQuantity, unitCost));
						} else {
							deposit.setActualQuantity(ControllerAccounting.add(
									depositQuantity, returned));
							transaction.setQuantity(returned);
							costActual = ControllerAccounting.subtract(
									costActual, ControllerAccounting.multiply(
											returned, unitCost));
							returned = 0d;
						}
						transaction.setTransactionType(transactionType);
						transaction.setDateTime(new Date());
						transaction.setDeposits(deposit);
						transaction.setHr(hr);
						transaction.setActivities(am.getActivityMaterialsPK()
								.getActivities());
						transaction.setUserName(identity.getUserName());
						transactionsDao.saveTransaction(transaction);
						depositsDao.editDeposits(deposit);
						count++;
					}
					am.setCostActual(costActual);
					activityMaterialsDao.editActivityMaterials(am);
				}
			}
			userTransaction.commit();
			ControladorContexto
					.mensajeInformacion(
							null,
							bundle.getString("movements_message_return_materials_succesfully"));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.printErrorLog(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return initializeReturns();
	}

	/**
	 * This method allows validate the return value enter by the user
	 */
	public void validateQuantityReturn() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String idForm = "";
		int count = 0;
		for (ActivityMaterials am : this.listActivityMaterialsSelected) {
			if (am.isSelected()) {
				idForm = "formConfirmar:repeatInputs:" + count + ":message";
				if (am.getQuantityReturn() > 0) {
					Double returned = am.getQuantityReturn();
					Double withdraw = am.getQuantityWithdrawn();
					if (returned.compareTo(withdraw) > 0) {
						ControladorContexto.mensajeErrorEspecifico(idForm,
								"movements_messasge_quantity_greater_withdraw",
								"mensajeWarehouse");
					}
				} else {
					ControladorContexto.mensajeError(null, idForm,
							bundle.getString("message_campo_mayo_cero"));
				}
			}
			count++;
		}
	}

}