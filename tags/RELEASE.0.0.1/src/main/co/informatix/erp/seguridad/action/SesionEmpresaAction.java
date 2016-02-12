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

import co.informatix.erp.organizaciones.dao.EmpresaDao;
import co.informatix.erp.organizaciones.dao.HaciendaDao;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Hacienda;
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.seguridad.dao.PermisoPersonaEmpresaDao;
import co.informatix.erp.seguridad.dao.UsuarioDao;
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
 * 
 */
@SuppressWarnings("serial")
@Named("sesionEmpresa")
@SessionScoped
public class SesionEmpresaAction implements Serializable {

	private List<SelectItem> itemsEmpresas;
	private List<SelectItem> itemsHaciendas;
	protected String nombre;
	protected String nombreHacienda;
	private String cargar;
	protected int id;
	protected int idHacienda;
	protected int idPersonaSesion;

	@EJB
	protected EmpresaDao empresaDao;
	@EJB
	private PersonaDao personaDao;
	@EJB
	private UsuarioDao usuarioDao;
	@EJB
	private HaciendaDao haciendaDao;
	@EJB
	private PermisoPersonaEmpresaDao permisoPersonaEmpresaDao;

	/**
	 * 
	 * @return id: Returns the identification of the company in session at which
	 *         it makes the management of information in the system.
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            : Returns the identification of the company in session at
	 *            which it makes the management of information in the system.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return nombre: Returns the name of the company in session at which it
	 *         makes the management of information in the system.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : Returns the name of the company in session at which it makes
	 *            the management of information in the system.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Returns the list of companies to which the user may manage the
	 * information.
	 * 
	 * @return itemsEmpresas: items of the companies with access permissions,
	 *         which are displayed in the combo in the user interface.
	 */
	public List<SelectItem> getItemsEmpresas() {
		return itemsEmpresas;
	}

	/**
	 * @return itemsHaciendas: items farms with access permissions, which are
	 *         displayed in the combo in the user interface.
	 */
	public List<SelectItem> getItemsHaciendas() {
		return itemsHaciendas;
	}

	/**
	 * @return idHacienda: id farm in session at which the information system is
	 *         loaded.
	 */
	public int getIdHacienda() {
		return idHacienda;
	}

	/**
	 * @param idHacienda
	 *            : id farm in session at which the information system is
	 *            loaded.
	 */
	public void setIdHacienda(int idHacienda) {
		this.idHacienda = idHacienda;
	}

	/**
	 * @return nombreHacienda: the name of the farm in session at which it will
	 *         load your information.
	 */
	public String getNombreHacienda() {
		return nombreHacienda;
	}

	/**
	 * @param nombreHacienda
	 *            : the name of the farm in session at which it will load your
	 *            information.
	 */
	public void setNombreHacienda(String nombreHacienda) {
		this.nombreHacienda = nombreHacienda;
	}

	/**
	 * Method of uploading companies have authorized the user to manage
	 * information.
	 * 
	 * @author Fredy.Vera
	 * 
	 * @return cargar: Variable required to execute the method of consulting the
	 *         user companies.
	 */
	public String getCargar() {
		return cargar;
	}

	/**
	 * Method to load the combo of the estates leaving a default session.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param idEmpresa
	 *            : company id to load the farms.
	 * 
	 * @return asignarEmpresaHacienda: method that assigns the company and the
	 *         farm in session and returns to the starting template or home
	 *         system.
	 */
	public String cargarComboHaciendasSesion(int idEmpresa) {
		try {
			itemsHaciendas = new ArrayList<SelectItem>();
			if (idEmpresa == 0) {
				idEmpresa = this.id;
			}
			List<Hacienda> haciendasEmpresa = haciendaDao
					.consultarHaciendasConPermisosAccesoEmpresa(idEmpresa,
							this.idPersonaSesion);
			if (haciendasEmpresa != null && haciendasEmpresa.size() > 0) {
				Collections.sort(haciendasEmpresa);
				for (Hacienda hacienda : haciendasEmpresa) {
					itemsHaciendas.add(new SelectItem(hacienda.getId(),
							hacienda.getNombre()));
					this.idHacienda = hacienda.getId();
					this.nombreHacienda = hacienda.getNombre();
				}
				if (haciendasEmpresa.size() == 1) {
					this.idHacienda = haciendasEmpresa.get(0).getId();
					this.nombreHacienda = haciendasEmpresa.get(0).getNombre();
				} else {
					for (Hacienda hacienda : haciendasEmpresa) {
						boolean haciendaPredeterminada = permisoPersonaEmpresaDao
								.haciendaPredeterminada(idPersonaSesion, id,
										hacienda.getId());
						if (haciendaPredeterminada) {
							this.idHacienda = hacienda.getId();
							this.nombreHacienda = hacienda.getNombre();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return asignarEmpresaHacienda();
	}

	/**
	 * Method to allocate the company and property to which you are going to
	 * manage their information.
	 * 
	 * @author marisol.calderon
	 * @return urlHome: variable redirect navigation allowing the startup
	 *         screen.
	 */
	public String asignarEmpresaHacienda() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			Empresa empresa = empresaDao.obtenerEmpresa(this.id);
			Hacienda hacienda = haciendaDao.consultarHacienda(this.idHacienda);
			if (empresa != null && hacienda != null) {
				this.nombre = empresa.getNombre();
				this.nombreHacienda = hacienda.getNombre();
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle
								.getString("message_cambio_empresa_hacienda"),
								this.nombre, this.nombreHacienda));
			} else {
				limpiarEmpresaSesion();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "urlHome";
	}

	/**
	 * Cleaning method that allows the company variables in session.
	 */
	public void limpiarEmpresaSesion() {
		this.itemsEmpresas = new ArrayList<SelectItem>();
		this.itemsHaciendas = new ArrayList<SelectItem>();
		this.nombre = "";
		this.id = 0;
		this.idHacienda = 0;
		this.idPersonaSesion = 0;
		this.nombreHacienda = "";
	}

	/**
	 * Method to destroy the object when sesionEmpresa session closes
	 */
	@Remove
	public void destroy() {

	}

}
