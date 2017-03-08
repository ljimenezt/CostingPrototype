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
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.dao.ActivityMaterialsDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMaterials;
import co.informatix.erp.costs.entities.ActivityMaterialsPK;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.action.MaterialsAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Materials;
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
	@EJB
	private CycleDao cycleDao;

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
	 * @param materialSelected
	 *            : material selected for assign to the activity.
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
	 * Consult the material list associated to the activity.
	 * 
	 * @modify 05/10/2016 Luna.Granados
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

				int idActivity = this.selectedActivity.getIdActivity();
				double totalCostMaterials = activityMaterialsDao
						.calculateTotalCostMaterials(idActivity);
				recordActivitiesActualsAction
						.setTotalCostMaterials(totalCostMaterials);
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
			selectedActivity.setCostMaterialsBudget(ControllerAccounting
					.subtract(selectedActivity.getCostMaterialsBudget(),
							activityMaterials.getCostBudget()));
			selectedActivity.setGeneralCostBudget(ControllerAccounting
					.subtract(selectedActivity.getGeneralCostBudget(),
							activityMaterials.getCostBudget()));
			if (selectedActivity.getCycle() != null) {
				selectedActivity.getCycle().setCostMaterialsBudget(
						ControllerAccounting.subtract(selectedActivity
								.getCycle().getCostMaterialsBudget(),
								activityMaterials.getCostBudget()));
				cycleDao.editCycle(selectedActivity.getCycle());
			}
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
	 * @modify 14/07/2016 Andres.Gomez
	 * 
	 * @param material
	 *            : material to validate quantity.
	 */
	public void validateMaterialsInDeposits(Materials material) {
		try {
			validateMaterial = false;
			activityMaterials = new ActivityMaterials();
			quantityEdit = 0.0;
			costActualEdit = 0.0;
			if (!material.isSelected()) {
				Double materialQuantity = depositsDao.quantityMaterialsById(
						material.getIdMaterial(), null);
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
				double quantityBudget = activityMaterials.getQuantityBudget();
				double materialsBudget = material.getTotalMaterialsBudget();
				double totalMaterialsBudget = ControllerAccounting.subtract(
						materialsBudget, quantityBudget);
				material.setTotalMaterialsBudget(totalMaterialsBudget);
				materialsList.remove(material);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the material quantity to edit for activity.
	 * 
	 * @modify 14/07/2016 Andres.Gomez
	 * 
	 * @param idMaterial
	 *            : material identifier.
	 * @param flag
	 *            : flag that identifies the form.
	 */
	public void validateQuantityMaterial(int idMaterial, boolean flag) {
		try {
			String form = "";
			if (flag) {
				form = "formAddMaterials";
			} else {
				form = "formUpdateActivitiesMaterials";
			}
			Double quantityActual = depositsDao.quantityMaterialsById(
					idMaterial, null);
			if (quantityEdit > quantityActual) {
				ControladorContexto.mensajeErrorEspecifico(form
						+ ":txtQuantity",
						"deposits_message_not_enough_records_in_deposit",
						"mensajeWarehouse");
				costActualEdit = 0;
			} else if (quantityEdit > 0) {
				calculateCostBudget(quantityEdit, idMaterial);
			} else {
				costActualEdit = 0;
			}
			if (quantityEdit <= 0) {
				ControladorContexto.mensajeErrorEspecifico(form
						+ ":txtQuantity", "message_campo_positivo", "mensaje");
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
			selectedActivity.setCostMaterialsBudget(ControllerAccounting
					.subtract(selectedActivity.getCostMaterialsBudget(),
							activityMaterials.getCostBudget()));
			selectedActivity.setGeneralCostBudget(ControllerAccounting
					.subtract(selectedActivity.getGeneralCostBudget(),
							activityMaterials.getCostBudget()));
			if (selectedActivity.getCycle() != null) {
				selectedActivity.getCycle().setCostMaterialsBudget(
						ControllerAccounting.subtract(selectedActivity
								.getCycle().getCostMaterialsBudget(),
								activityMaterials.getCostBudget()));
			}
			activityMaterials.setQuantityBudget(quantityEdit);
			activityMaterials.setCostBudget(costActualEdit);
			selectedActivity.setCostMaterialsBudget(ControllerAccounting.add(
					selectedActivity.getCostMaterialsBudget(),
					activityMaterials.getCostBudget()));
			selectedActivity.setGeneralCostBudget(ControllerAccounting.add(
					selectedActivity.getGeneralCostBudget(),
					activityMaterials.getCostBudget()));
			if (selectedActivity.getCycle() != null) {
				selectedActivity.getCycle().setCostMaterialsBudget(
						ControllerAccounting.add(selectedActivity.getCycle()
								.getCostMaterialsBudget(), activityMaterials
								.getCostBudget()));
				cycleDao.editCycle(selectedActivity.getCycle());
			}
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
	 */
	private void calculateCostBudget(double amount, int idMaterial)
			throws Exception {
		double costBudget = 0.0;
		List<Deposits> depositsListActual = depositsDao
				.consultDepositsActualQuantityById(idMaterial);
		while (amount > 0) {
			Deposits depositsActual = depositsListActual.get(0);
			if (amount > depositsActual.getActualQuantity()) {
				costBudget = ControllerAccounting.add(costBudget,
						ControllerAccounting.multiply(
								depositsActual.getActualQuantity(),
								depositsActual.getUnitCost()));
				amount = ControllerAccounting.subtract(amount,
						depositsActual.getActualQuantity());
			} else {
				costBudget = ControllerAccounting.add(
						costBudget,
						ControllerAccounting.multiply(amount,
								depositsActual.getUnitCost()));
				amount = 0;
			}
			depositsListActual.remove(depositsActual);
			costActualEdit = costBudget;
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
					costMaterialsBudget = ControllerAccounting.add(
							costMaterialsBudget,
							activityMaterials.getCostBudget());
				}
				selectedActivity.setCostMaterialsBudget(ControllerAccounting
						.add(selectedActivity.getCostMaterialsBudget(),
								costMaterialsBudget));
				selectedActivity.setGeneralCostBudget(ControllerAccounting.add(
						selectedActivity.getGeneralCostBudget(),
						costMaterialsBudget));
				if (selectedActivity.getCycle() != null) {
					if (selectedActivity.getCycle().getCostMaterialsBudget() == null) {
						selectedActivity.getCycle().setCostMaterialsBudget(0.0);
					}
					selectedActivity.getCycle().setCostMaterialsBudget(
							ControllerAccounting.add(selectedActivity
									.getCycle().getCostMaterialsBudget(),
									costMaterialsBudget));
					cycleDao.editCycle(selectedActivity.getCycle());
				}
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
		if (costActualEdit <= 0) {
			ControladorContexto
					.mensajeErrorEspecifico(
							"formAddMaterials:txtQuantity",
							"activities_and_materials_message_not_correctly_calculated_materials",
							"messageCosts");
		} else {
			materialSelected.setSelected(true);
			double materialsBudget = materialSelected.getTotalMaterialsBudget();
			double totalMaterialsBudget = ControllerAccounting.add(
					quantityEdit, materialsBudget);
			materialSelected.setTotalMaterialsBudget(totalMaterialsBudget);
			activityMaterials.setActivityMaterialsPK(new ActivityMaterialsPK());
			activityMaterials.getActivityMaterialsPK().setMaterials(
					materialSelected);
			activityMaterials.getActivityMaterialsPK().setActivities(
					selectedActivity);
			activityMaterials.setQuantityBudget(quantityEdit);
			activityMaterials.setCostBudget(costActualEdit);
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
	 * @modify 14/07/2016 Andres.Gomez
	 */
	public void validateCostActualMaterials() {
		try {
			Double materialQuantity = depositsDao.quantityMaterialsById(
					this.activityMaterials.getActivityMaterialsPK()
							.getMaterials().getIdMaterial(), null);
			if (quantityEdit > materialQuantity) {
				costActualEdit = 0.0;
				ControladorContexto.mensajeErrorEspecifico(
						"formUpdateActivitiesMaterials:txtQuantity",
						"deposits_message_not_enough_records_in_deposit",
						"mensajeWarehouse");
			} else {
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
	 */
	private void calculateCostActualMaterials() throws Exception {
		double costActual = 0.0;
		double amount = 0.0;
		if (this.activityMaterials.getQuantityActual() == null
				|| quantityEdit > this.activityMaterials.getQuantityActual()) {
			if (activityMaterials.getQuantityActual() != null
					&& quantityEdit > this.activityMaterials
							.getQuantityActual()) {
				amount = ControllerAccounting.subtract(quantityEdit,
						this.activityMaterials.getQuantityActual());
			} else {
				amount = quantityEdit;
			}
			List<Deposits> depositsListActual = depositsDao
					.consultDepositsActualQuantityById(this.activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial());
			while (amount > 0) {
				Deposits depositsActual = depositsListActual.get(0);
				if (amount > depositsActual.getActualQuantity()) {
					costActual = ControllerAccounting.add(costActual,
							ControllerAccounting.multiply(
									depositsActual.getActualQuantity(),
									depositsActual.getUnitCost()));
					amount = ControllerAccounting.subtract(amount,
							depositsActual.getActualQuantity());
				} else {
					costActual = ControllerAccounting.add(costActual,
							ControllerAccounting.multiply(amount,
									depositsActual.getUnitCost()));
					amount = 0;
				}
				depositsListActual.remove(depositsActual);
			}
			if (this.activityMaterials.getCostActual() != null) {
				costActualEdit = ControllerAccounting.add(
						this.activityMaterials.getCostActual(), costActual);
			} else {
				costActualEdit = costActual;
			}
		} else if (quantityEdit < this.activityMaterials.getQuantityActual()) {
			amount = ControllerAccounting.subtract(
					this.activityMaterials.getQuantityActual(), quantityEdit);
			List<Deposits> depositsListActual = depositsDao
					.consultDepositsByTransactions(this.activityMaterials
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial(),
							this.selectedActivity.getIdActivity(),
							Constantes.TRANSACTION_TYPE_WITHDRAWAL);
			while (amount > 0) {
				Deposits depositsActual = depositsListActual.get(0);
				Transactions transactionDrawal = transactionsDao
						.consultTransactionsByDepositsActivityAndTransactionType(
								depositsActual.getIdDeposit(),
								this.selectedActivity.getIdActivity(),
								Constantes.TRANSACTION_TYPE_WITHDRAWAL);
				if (amount > transactionDrawal.getQuantity()) {
					costActual = ControllerAccounting.add(costActual,
							ControllerAccounting.multiply(
									transactionDrawal.getQuantity(),
									depositsActual.getUnitCost()));
					amount = ControllerAccounting.subtract(amount,
							transactionDrawal.getQuantity());
				} else {
					costActual = ControllerAccounting.add(costActual,
							ControllerAccounting.multiply(amount,
									depositsActual.getUnitCost()));
					amount = 0;
				}
				depositsListActual.remove(depositsActual);
			}
			costActualEdit = ControllerAccounting.subtract(
					this.activityMaterials.getCostActual(), costActual);
		} else if (quantityEdit == this.activityMaterials.getQuantityActual()) {
			costActualEdit = this.activityMaterials.getCostActual();
		}
	}

	/**
	 * This method allows cloned the activity Material and copy the actual
	 * fields in other variables for calculate.
	 * 
	 * @param activityMaterials
	 *            : activityMaterials to clone.
	 * @param flag
	 *            : flag that identifies the initialize values.
	 */
	public void cloneActivityMaterials(ActivityMaterials activityMaterials,
			boolean flag) {
		this.activityMaterials = activityMaterials;
		if (flag) {
			if (this.activityMaterials.getQuantityActual() != null) {
				quantityEdit = this.activityMaterials.getQuantityActual();
				costActualEdit = this.activityMaterials.getCostActual();
			} else {
				quantityEdit = 0.0;
				costActualEdit = 0.0;
			}
		} else {
			quantityEdit = this.activityMaterials.getQuantityBudget();
			costActualEdit = this.activityMaterials.getCostBudget();
		}
	}

	/**
	 * This method allows validate the budget cost and actual cost.
	 * 
	 */
	public void validateCost() {
		if (costActualEdit <= 0) {
			ControladorContexto
					.mensajeErrorEspecifico(
							"formUpdateActivitiesMaterials:txtQuantity",
							"activities_and_materials_message_not_correctly_calculated_materials",
							"messageCosts");
		}
	}
}
