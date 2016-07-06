package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.dao.ActivityMaterialsDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMaterials;
import co.informatix.erp.costs.entities.ActivityMaterialsPK;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.action.MaterialsAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.TransactionType;
import co.informatix.erp.warehouse.entities.Transactions;

/**
 * This class allows the logic of the relationship between the activities and
 * materials in the database. The logic is to record the relationship and
 * materials activities.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivityMaterialsAction implements Serializable {

	private List<ActivityMaterials> listActivityMaterialsTemp;
	private List<ActivityMaterials> listActivityMaterials;
	private List<Materials> materialsList;
	private List<Deposits> depositsListEdit;
	private List<Transactions> transactionsList;
	private Activities selectedActivity;
	private ActivityMaterials activityMaterials;
	private Materials materialSelected;
	private Paginador paginationActivityMaterials = new Paginador();
	private MaterialsAction materialsAction;
	private double quantityEdit;
	private double costActualEdit;
	private boolean fromModal;
	private boolean validateMaterial;

	@EJB
	private ActivityMaterialsDao activityMaterialsDao;
	@EJB
	private DepositsDao depositsDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private TransactionsDao transactionsDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;

	/**
	 * @return listActivityMaterialsTemp: material list assigned to the
	 *         activity.
	 */
	public List<ActivityMaterials> getListActivityMaterialsTemp() {
		return listActivityMaterialsTemp;
	}

	/**
	 * @param listActivityMaterialsTemp
	 *            : material list assigned to the activity.
	 */
	public void setListActivityMaterialsTemp(
			List<ActivityMaterials> listActivityMaterialsTemp) {
		this.listActivityMaterialsTemp = listActivityMaterialsTemp;
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
	 * @return materialsList: materialsList selected for add to activity
	 */
	public List<Materials> getMateriaslList() {
		return materialsList;
	}

	/**
	 * @param materialsList
	 *            : materialsList selected for add to activity
	 */
	public void setMaterialsList(List<Materials> materialsList) {
		this.materialsList = materialsList;
	}

	/**
	 * @return selectedActivity: activities selected.
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : activities selected.
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return activityMaterials: activityMaterials for save or edit or remove.
	 */
	public ActivityMaterials getActivityMaterials() {
		return activityMaterials;
	}

	/**
	 * @param activityMaterials
	 *            : activityMaterials for save or edit or remove.
	 */
	public void setActivityMaterials(ActivityMaterials activityMaterials) {
		this.activityMaterials = activityMaterials;
	}

	/**
	 * @return materialSelected: material selected for assign to the activity.
	 */
	public Materials getMaterialSelected() {
		return materialSelected;
	}

	/**
	 * @return materialSelected: material selected for assign to the activity.
	 */
	public void setMaterialSelected(Materials materialSelected) {
		this.materialSelected = materialSelected;
	}

	/**
	 * @return paginationActivityMaterials: Pager of the activityMaterials list.
	 */
	public Paginador getPaginationActivityMaterials() {
		return paginationActivityMaterials;
	}

	/**
	 * @param paginationActivityMaterials
	 *            : Pager of the activityMaterials list.
	 */
	public void setPaginationActivityMaterials(
			Paginador paginationActivityMaterials) {
		this.paginationActivityMaterials = paginationActivityMaterials;
	}

	/**
	 * @return quantityEdit: this field store the material quantity to save or
	 *         edit.
	 */
	public double getQuantityEdit() {
		return quantityEdit;
	}

	/**
	 * @param quantityEdit
	 *            : this field store the material quantity to save or edit.
	 */
	public void setQuantityEdit(double quantityEdit) {
		this.quantityEdit = quantityEdit;
	}

	/**
	 * @return costActualEdit: this field store the material cost to save or
	 *         edit.
	 */
	public double getCostActualEdit() {
		return costActualEdit;
	}

	/**
	 * @param costActualEdit
	 *            : this field store the material cost to save or edit.
	 */
	public void setCostActualEdit(double costActualEdit) {
		this.costActualEdit = costActualEdit;
	}

	/**
	 * @return fromModal : this field is true if the query is made from
	 *         recordActivitiesActualsAction and is false in other case.
	 */
	public boolean isFromModal() {
		return fromModal;
	}

	/**
	 * @param fromModal
	 *            : this field is true if the query is made from
	 *            recordActivitiesActualsAction and is false in other case.
	 */
	public void setFromModal(boolean fromModal) {
		this.fromModal = fromModal;
	}

	/**
	 * @return validateMaterial: : handles the validation status of materials.
	 */
	public boolean isValidateMaterial() {
		return validateMaterial;
	}

	/**
	 * @param validateMaterial
	 *            : handles the validation status of materials.
	 */
	public void setValidateMaterial(boolean validateMaterial) {
		this.validateMaterial = validateMaterial;
	}

	/**
	 * consult the material list associated to the activity.
	 * 
	 */
	public void consultMaterialsByActivity() {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		String messageSearch = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			if (fromModal) {
				this.selectedActivity = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			advancedSearchMaterialsByActivity(consult, parameters);
			Long quantity = activityMaterialsDao.amountMaterialsByActivity(
					consult, parameters);
			if (quantity != null) {
				paginationActivityMaterials.paginarRangoDefinido(quantity, 5);
				this.listActivityMaterialsTemp = activityMaterialsDao
						.queryMaterialsByActivity(
								paginationActivityMaterials.getInicio(),
								paginationActivityMaterials.getRango(),
								consult, parameters);
			}
			if (fromModal) {
				recordActivitiesActualsAction.currentCost();
			}
			validations.setMensajeBusqueda(messageSearch);
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
	 */
	private void advancedSearchMaterialsByActivity(StringBuilder consult,
			List<SelectItem> parameters) {
		consult.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(this.selectedActivity.getIdActivity(),
				"id");
		parameters.add(item);
	}

	/**
	 * This method allows delete a material associated to the activity.
	 * 
	 */
	public void deleteActivityMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			selectedActivity.setCostMaterialsBudget(selectedActivity
					.getCostMaterialsBudget()
					- activityMaterials.getCostBudget());
			selectedActivity
					.setGeneralCostBudget(selectedActivity
							.getGeneralCostBudget()
							- activityMaterials.getCostBudget());
			activitiesDao.editActivities(this.selectedActivity);
			activityMaterialsDao.deleteActivityMaterials(activityMaterials);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					activityMaterials.getActivityMaterialsPK().getMaterials()
							.getName()));
			consultMaterialsByActivity();
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activityMaterials.getActivityMaterialsPK().getMaterials()
							.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows initialize the values.
	 * 
	 */
	public void initializeList() {
		materialsAction = ControladorContexto
				.getContextBean(MaterialsAction.class);
		materialsList = new ArrayList<Materials>();
		listActivityMaterials = new ArrayList<ActivityMaterials>();
		materialsAction.initializeMaterials();
	}

	/**
	 * This method allows validate the materials quantity in the deposit.
	 * 
	 * @param material
	 *            : material to validate quantity.
	 * 
	 */
	public void validateMaterialsInDeposits(Materials material) {
		try {
			validateMaterial = false;
			activityMaterials = new ActivityMaterials();
			if (!material.isSelected()) {
				Double materialQuantity = depositsDao
						.quantityMaterialsById(material.getIdMaterial());
				if (materialQuantity == null || materialQuantity == 0) {
					ControladorContexto.mensajeErrorArg1(
							"formMaterials:mensajeBusquedaMenu",
							"deposits_message_no_materials",
							"mensajeWarehouse", material.getName());
					validateMaterial = true;
				} else {
					materialSelected = material;
				}
			} else {
				ActivityMaterials activityMaterials = new ActivityMaterials();
				for (ActivityMaterials activityMaterial : listActivityMaterials) {
					int idMaterial = activityMaterial.getActivityMaterialsPK()
							.getMaterials().getIdMaterial();
					int idActivity = activityMaterial.getActivityMaterialsPK()
							.getActivities().getIdActivity();
					if (idMaterial == material.getIdMaterial()
							&& selectedActivity.getIdActivity() == idActivity) {
						activityMaterials = activityMaterial;
					}
				}
				validateMaterial = true;
				listActivityMaterials.remove(activityMaterials);
				material.setSelected(false);
				materialsList.remove(material);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows select or remove a material for assign to the
	 * activity.
	 * 
	 */
	public void materialSelection() {
		try {
			if (listActivityMaterials == null) {
				listActivityMaterials = new ArrayList<ActivityMaterials>();
			}
			Double quantityActual = depositsDao
					.quantityMaterialsById(materialSelected.getIdMaterial());
			if (activityMaterials.getQuantityBudget() > quantityActual) {
				ControladorContexto.mensajeErrorEspecifico(
						"formAddMaterials:txtQuantityBudget",
						"deposits_message_not_enough_records_in_deposit",
						"mensajeWarehouse");
				activityMaterials.setCostBudget(0.0);
			} else {
				if (activityMaterials.getQuantityBudget() > 0) {
					calculateCostBudget(activityMaterials.getQuantityBudget(),
							materialSelected.getIdMaterial());
				} else {
					activityMaterials.setCostBudget(0.0);
				}
			}

			if (activityMaterials.getQuantityBudget() <= 0) {
				ControladorContexto.mensajeErrorEspecifico(
						"formAddMaterials:txtQuantityBudget",
						"message_campo_positivo", "mensaje");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the material quantity to edit for activity.
	 * 
	 */
	public void validateQuantityMaterial() {
		try {
			Double quantityActual = depositsDao
					.quantityMaterialsById(activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial());
			if (quantityEdit > quantityActual) {
				ControladorContexto.mensajeErrorEspecifico(
						"formUpdateActivitiesMaterials:txtQuantityBudget",
						"deposits_message_not_enough_records_in_deposit",
						"mensajeWarehouse");
			}
			if (quantityEdit <= 0) {
				ControladorContexto.mensajeErrorEspecifico(
						"formUpdateActivitiesMaterials:txtQuantityBudget",
						"message_campo_positivo", "mensaje");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows edit a material associated to a activity.
	 * 
	 */
	public void editActivityMaterial() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			selectedActivity.setCostMaterialsBudget(selectedActivity
					.getCostMaterialsBudget()
					- activityMaterials.getCostBudget());
			selectedActivity
					.setGeneralCostBudget(selectedActivity
							.getGeneralCostBudget()
							- activityMaterials.getCostBudget());
			activityMaterials.setQuantityBudget(quantityEdit);
			activityMaterials.setCostBudget(0.0);
			calculateCostBudget(activityMaterials.getQuantityBudget(),
					activityMaterials.getActivityMaterialsPK().getMaterials()
							.getIdMaterial());
			selectedActivity.setCostMaterialsBudget(selectedActivity
					.getCostMaterialsBudget()
					+ activityMaterials.getCostBudget());
			selectedActivity
					.setGeneralCostBudget(selectedActivity
							.getGeneralCostBudget()
							+ activityMaterials.getCostBudget());
			activityMaterialsDao.editActivityMaterials(activityMaterials);
			activitiesDao.editActivities(this.selectedActivity);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_modificar"),
					activityMaterials.getActivityMaterialsPK().getMaterials()
							.getName()));
			consultMaterialsByActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows to calculate the total cost of the material to assign
	 * to the activity.
	 * 
	 * @param amount
	 *            : amount of material to be assigned to the activity.
	 * @param idMaterial
	 *            : material identifier.
	 * @throws Exception
	 * 
	 */
	private void calculateCostBudget(double amount, int idMaterial)
			throws Exception {
		double costBudget = 0.0;
		List<Deposits> depositsListActual = depositsDao
				.consultDepositsActualQuantityById(idMaterial);
		while (amount > 0) {
			Deposits depositsActual = null;
			depositsActual = depositsListActual.get(0);
			if (amount > depositsActual.getActualQuantity()) {
				costBudget = costBudget + depositsActual.getActualQuantity()
						* depositsActual.getUnitCost();
				amount = amount - depositsActual.getActualQuantity();
			} else {
				costBudget = costBudget + amount * depositsActual.getUnitCost();
				amount = 0;
			}
			depositsListActual.remove(depositsActual);
			activityMaterials.setCostBudget(costBudget);
		}
	}

	/**
	 * This method save the material list to activity.
	 * 
	 */
	public void saveActivityMaterialsList() {
		try {
			double costMaterialsBudget = 0.0;
			if (listActivityMaterials != null
					&& listActivityMaterials.size() > 0) {
				if (selectedActivity.getCostMaterialsBudget() == null) {
					selectedActivity.setCostMaterialsBudget(0.0);
				}
				if (selectedActivity.getGeneralCostBudget() == null) {
					selectedActivity.setGeneralCostBudget(0.0);
				}
				for (ActivityMaterials activityMaterials : listActivityMaterials) {
					activityMaterials.setDateTime(selectedActivity
							.getInitialDtBudget());
					activityMaterialsDao
							.saveActivityMaterials(activityMaterials);
					costMaterialsBudget += activityMaterials.getCostBudget();
				}
				selectedActivity.setCostMaterialsBudget(selectedActivity
						.getCostMaterialsBudget() + costMaterialsBudget);
				selectedActivity.setGeneralCostBudget(selectedActivity
						.getGeneralCostBudget() + costMaterialsBudget);
				activitiesDao.editActivities(this.selectedActivity);
			}
			consultMaterialsByActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows to add a material to the materials list to assign to
	 * the activity.
	 * 
	 */
	public void addMaterials() {
		if (activityMaterials.getCostBudget() <= 0) {
			ControladorContexto
					.mensajeErrorEspecifico(
							"formAddMaterials:txtQuantityBudget",
							"activities_and_materials_message_not_correctly_calculated_materials",
							"messageCosts");
		} else {
			materialSelected.setSelected(true);
			activityMaterials.setActivityMaterialsPK(new ActivityMaterialsPK());
			activityMaterials.getActivityMaterialsPK().setMaterials(
					materialSelected);
			activityMaterials.getActivityMaterialsPK().setActivities(
					selectedActivity);
			listActivityMaterials.add(activityMaterials);
		}
	}

	/**
	 * It copies the fields budget to current fields.
	 * 
	 */
	public void budgetCopyMaterials() {
		quantityEdit = this.activityMaterials.getQuantityBudget();
		costActualEdit = this.activityMaterials.getCostBudget();
	}

	/**
	 * This method allows save or update the actual fields of the materials
	 * assigned to the activity.
	 * 
	 */
	public void updateActivityMaterial() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			if (transactionsList != null) {
				for (Transactions transactions : transactionsList) {
					transactionsDao.saveTransaction(transactions);
				}
			}
			if (depositsListEdit != null) {
				for (Deposits deposits : depositsListEdit) {
					depositsDao.editDeposits(deposits);
				}
			}
			this.activityMaterials.setQuantityActual(quantityEdit);
			this.activityMaterials.setCostActual(costActualEdit);
			activityMaterialsDao.editActivityMaterials(this.activityMaterials);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_modificar"),
					this.activityMaterials.getActivityMaterialsPK()
							.getMaterials().getName()));
			consultMaterialsByActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the actual fields of the materials assigned
	 * to the activity.
	 * 
	 */
	public void validateCostActualMaterials() {
		try {
			Double materialQuantity = depositsDao
					.quantityMaterialsById(this.activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial());
			if (quantityEdit > materialQuantity) {
				costActualEdit = 0.0;
				ControladorContexto.mensajeErrorEspecifico(
						"formAddActivitiesMaterials:quantity",
						"deposits_message_not_enough_materials",
						"mensajeWarehouse");

			}
			if (quantityEdit <= 0) {
				costActualEdit = 0.0;
				ControladorContexto.mensajeErrorEspecifico(
						"formAddActivitiesMaterials:quantity",
						"message_campo_positivo", "mensaje");
			}
			if (quantityEdit <= materialQuantity && quantityEdit > 0) {
				calculateCostActualMaterials();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows calculate the actual fields of the materials assigned
	 * to the activity.
	 * 
	 * @throws Exception
	 * 
	 */
	private void calculateCostActualMaterials() throws Exception {
		double costActual = 0.0;
		double amount = 0.0;
		depositsListEdit = new ArrayList<Deposits>();
		transactionsList = new ArrayList<Transactions>();
		if (this.activityMaterials.getQuantityActual() == null
				|| quantityEdit > this.activityMaterials.getQuantityActual()) {
			if (activityMaterials.getQuantityActual() != null
					&& quantityEdit > this.activityMaterials
							.getQuantityActual()) {
				amount = quantityEdit
						- this.activityMaterials.getQuantityActual();
			} else {
				amount = quantityEdit;
			}
			List<Deposits> depositsListActual = depositsDao
					.consultDepositsActualQuantityById(this.activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial());
			TransactionType transactionType = transactionTypeDao
					.transactionTypeById(Constantes.TRANSACTION_TYPE_ID_WITHDRAWAL);
			while (amount > 0) {
				Transactions transactions = new Transactions();
				Deposits depositsActual = depositsListActual.get(0);
				if (amount > depositsActual.getActualQuantity()) {
					costActual = costActual
							+ depositsActual.getActualQuantity()
							* depositsActual.getUnitCost();
					transactions
							.setQuantity(depositsActual.getActualQuantity());
					amount = amount - depositsActual.getActualQuantity();
					depositsListActual.remove(depositsActual);
					depositsActual.setActualQuantity(0.0);
				} else {
					costActual = costActual + amount
							* depositsActual.getUnitCost();
					transactions.setQuantity(amount);
					depositsListActual.remove(depositsActual);
					depositsActual.setActualQuantity(depositsActual
							.getActualQuantity() - amount);
					amount = 0;
				}
				updateList(depositsActual, transactions, transactionType);
			}
			if (this.activityMaterials.getCostActual() != null) {
				costActualEdit = this.activityMaterials.getCostActual()
						+ costActual;
			} else {
				costActualEdit = costActual;
			}
		} else if (quantityEdit < this.activityMaterials.getQuantityActual()) {
			amount = this.activityMaterials.getQuantityActual() - quantityEdit;
			List<Deposits> depositsListActual = depositsDao
					.consultDepositsByTransactions(this.activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial(),
							this.selectedActivity.getIdActivity(),
							Constantes.TRANSACTION_TYPE_ID_WITHDRAWAL);
			TransactionType transactionType = transactionTypeDao
					.transactionTypeById(Constantes.TRANSACTION_TYPE_ID_RETURN);
			while (amount > 0) {
				Transactions transactions = new Transactions();
				Deposits depositsActual = depositsListActual.get(0);
				Transactions transactionDrawal = transactionsDao
						.consultTransactionsByDepositsActivityAndTransactionType(
								depositsActual.getIdDeposit(),
								this.selectedActivity.getIdActivity(),
								Constantes.TRANSACTION_TYPE_ID_WITHDRAWAL);
				if (amount > transactionDrawal.getQuantity()) {
					costActual = costActual + transactionDrawal.getQuantity()
							* depositsActual.getUnitCost();
					depositsListActual.remove(depositsActual);
					depositsActual.setActualQuantity(depositsActual
							.getActualQuantity()
							+ transactionDrawal.getQuantity());
					amount = amount - transactionDrawal.getQuantity();
					transactions.setQuantity(transactionDrawal.getQuantity());
				} else {
					costActual = costActual + amount
							* depositsActual.getUnitCost();
					depositsListActual.remove(depositsActual);
					depositsActual.setActualQuantity(depositsActual
							.getActualQuantity() + amount);
					transactions.setQuantity(amount);
					amount = 0;
				}
				updateList(depositsActual, transactions, transactionType);
			}
			costActualEdit = this.activityMaterials.getCostActual()
					- costActual;
		} else if (quantityEdit == this.activityMaterials.getQuantityActual()) {
			costActualEdit = this.activityMaterials.getCostActual();
		}
	}

	/**
	 * This method allows update transactionsList and depositsListEdit to save.
	 * 
	 * @param depositsActual
	 *            : deposit to add in the list.
	 * @param transactions
	 *            : movements of the materials in the deposit.
	 * @param transactionType
	 *            : type of transaction done.
	 * 
	 */
	private void updateList(Deposits depositsActual, Transactions transactions,
			TransactionType transactionType) {
		depositsListEdit.add(depositsActual);
		transactions.setTransactionType(transactionType);
		transactions.setDateTime(new Date());
		transactions.setDeposits(depositsActual);
		transactions.setActivities(selectedActivity);
		transactionsList.add(transactions);
	}

	/**
	 * This method allows cloned the activity Material and copy the actual
	 * fields in other variables for calculate.
	 * 
	 * @param activityMaterials
	 *            : activityMaterials to clone.
	 * 
	 */
	public void cloneActivityMaterials(ActivityMaterials activityMaterials) {
		this.activityMaterials = activityMaterials;
		if (this.activityMaterials.getQuantityActual() != null) {
			quantityEdit = this.activityMaterials.getQuantityActual();
			costActualEdit = this.activityMaterials.getCostActual();
		} else {
			quantityEdit = 0.0;
			costActualEdit = 0.0;
		}
	}
}
