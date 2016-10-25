package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.organizations.dao.BusinessDao;
import co.informatix.erp.organizations.entities.Business;
import co.informatix.erp.humanResources.dao.PersonDao;
import co.informatix.erp.seguridad.dao.PermissionPersonBusinessDao;
import co.informatix.erp.seguridad.dao.UserDao;
import co.informatix.erp.utils.ControladorContexto;

/**
 * This class allows save in session the data of the company and the farm to
 * which you want manage the information of the system.
 * 
 * To use it from the layer of presentation is due use your name CDI: company,
 * of as follows:
 * 
 * #{sesionEmpresa.nombre} #{sesionEmpresa.id}
 * 
 * @author Gabriel.Moreno
 */
@SuppressWarnings("serial")
@Named("sesionEmpresa")
@SessionScoped
public class SessionBusinessAction implements Serializable {

	private List<SelectItem> itemsCompanies;
	private List<SelectItem> itemsFarms;
	protected String name;
	protected String nameFarm;
	private String load;
	protected int id;
	protected int idFarm;
	protected int idPersonSession;

	@EJB
	protected BusinessDao businessDao;
	@EJB
	private PersonDao personDao;
	@EJB
	private UserDao userDao;
	@EJB
	private FarmDao farmDao;
	@EJB
	private PermissionPersonBusinessDao permissionPersonBusinessDao;

	/**
	 * @return id: Returns the identification of the company in session at which
	 *         it makes the management of information in the system.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Returns the identification of the company in session at
	 *            which it makes the management of information in the system.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: Returns the name of the company in session at which it
	 *         makes the management of information in the system.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Returns the name of the company in session at which it makes
	 *            the management of information in the system.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the list of companies to which the user may manage the
	 * information.
	 * 
	 * @return itemsCompanies: items of the companies with access permissions,
	 *         which are displayed in the combo in the user interface.
	 */
	public List<SelectItem> getItemsCompanies() {
		return itemsCompanies;
	}

	/**
	 * @return itemsFarms: items farms with access permissions, which are
	 *         displayed in the combo in the user interface.
	 */
	public List<SelectItem> getItemsFarms() {
		return itemsFarms;
	}

	/**
	 * @return idFarm: id farm in session at which the information system is
	 *         loaded.
	 */
	public int getIdFarm() {
		return idFarm;
	}

	/**
	 * @param idFarm
	 *            : id farm in session at which the information system is
	 *            loaded.
	 */
	public void setIdFarm(int idFarm) {
		this.idFarm = idFarm;
	}

	/**
	 * @return nameFarm: the name of the farm in session at which it will load
	 *         your information.
	 */
	public String getNameFarm() {
		return nameFarm;
	}

	/**
	 * @param nameFarm
	 *            : the name of the farm in session at which it will load your
	 *            information.
	 */
	public void setNameFarm(String nameFarm) {
		this.nameFarm = nameFarm;
	}

	/**
	 * Method of uploading companies have authorized the user to manage
	 * information.
	 * 
	 * @author Fredy.Vera
	 * 
	 * @return load: Variable required to execute the method of consulting the
	 *         user companies.
	 */
	public String getLoad() {
		return load;
	}

	/**
	 * Method to load the combo of the estates leaving a default session.
	 * 
	 * @author marisol.calderon
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param idCompany
	 *            : company id to load the farms.
	 * 
	 * @return assignCompanyFarm: method that assigns the company and the farm
	 *         in session and returns to the starting template or home system.
	 */
	public String loadComboFarmsSession(int idCompany) {
		try {
			itemsFarms = new ArrayList<SelectItem>();
			if (idCompany == 0) {
				idCompany = this.id;
			}
			List<Farm> farmsCompany = farmDao
					.consultFarmsWithPermissionAccessCompany(idCompany,
							this.idPersonSession);
			if (farmsCompany != null && farmsCompany.size() > 0) {
				Collections.sort(farmsCompany);
				for (Farm farm : farmsCompany) {
					itemsFarms.add(new SelectItem(farm.getIdFarm(), farm
							.getName()));
					this.idFarm = farm.getIdFarm();
					this.nameFarm = farm.getName();
				}
				if (farmsCompany.size() == 1) {
					this.idFarm = farmsCompany.get(0).getIdFarm();
					this.nameFarm = farmsCompany.get(0).getName();
				} else {
					for (Farm farm : farmsCompany) {
						boolean defaultFarm = permissionPersonBusinessDao
								.farmPredetermined(idPersonSession, id,
										farm.getIdFarm());
						if (defaultFarm) {
							this.idFarm = farm.getIdFarm();
							this.nameFarm = farm.getName();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return assignCompanyFarm();
	}

	/**
	 * Method to allocate the company and property to which you are going to
	 * manage their information.
	 * 
	 * @author marisol.calderon
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @return urlHome: variable redirect navigation allowing the startup
	 *         screen.
	 */
	public String assignCompanyFarm() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			Business empresa = businessDao.getBusiness(this.id);
			Farm farm = farmDao.farmXId(this.idFarm);
			if (empresa != null && farm != null) {
				this.name = empresa.getName();
				this.nameFarm = farm.getName();
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle
								.getString("message_cambio_empresa_hacienda"),
								this.name, this.nameFarm));
			} else {
				cleanCompanySession();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "urlHome";
	}

	/**
	 * Cleaning method that allows the company variables in session.
	 */
	public void cleanCompanySession() {
		this.itemsCompanies = new ArrayList<SelectItem>();
		this.itemsFarms = new ArrayList<SelectItem>();
		this.name = "";
		this.id = 0;
		this.idFarm = 0;
		this.idPersonSession = 0;
		this.nameFarm = "";
	}

	/**
	 * Method to destroy the object when sesionEmpresa session closes
	 */
	@Remove
	public void destroy() {

	}

}
