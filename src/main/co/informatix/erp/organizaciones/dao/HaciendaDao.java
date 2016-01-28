package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.UnidadMedida;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Hacienda;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.utils.Constantes;

/**
 * This class data access and allows communication between action and haciendas
 * database
 * 
 * @author Oscar.Amaya
 * @modify 09/02/2012 marisol.calderon
 * @modify 09/02/2012 dario.lopez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HaciendaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Method that allows consulting the properties depending on the parameters
	 * sent by the user.
	 * 
	 * @param start
	 *            : where he started the consultation record.
	 * @param range
	 *            : Maximum number of records in the query.
	 * @return List<Hacienda>: list of existing farms in the database or null
	 *         but records exist.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> consultarHaciendas(int start, int range)
			throws Exception {
		return em
				.createQuery(
						"SELECT f FROM Hacienda f JOIN FETCH f.empresa emp "
								+ "JOIN FETCH f.contacto "
								+ "JOIN FETCH f.pais "
								+ "ORDER BY emp.nombre, f.nombre")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Returns the number of farms registered in the database that are in force
	 * 
	 * @return Long: number of farms found
	 * @throws Exception
	 */
	public Long cantidadHaciendas() throws Exception {
		return (Long) em.createQuery("SELECT COUNT(f) FROM Hacienda f")
				.getSingleResult();
	}

	/**
	 * Consult the detailed information of the farm receiving as parameter the
	 * id
	 * 
	 * @param id
	 *            : identifier of the Farm
	 * @return hacienda: Purpose of the farm with the information loaded
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hacienda consultarHacienda(int id) throws Exception {
		Hacienda hacienda = consultarHaciendaPorId(id);
		Empresa empresa = (Empresa) consultarObjetoDelaHacienda(
				Constantes.EMPRESA, id);
		Persona contacto = (Persona) consultarObjetoDelaHacienda(
				Constantes.CONTACTO, id);
		Pais pais = (Pais) consultarObjetoDelaHacienda(Constantes.PAIS, id);
		List<Departamento> departamentos = (List<Departamento>) consultarListaObjetosDelaHacienda(
				Constantes.DEPARTAMENTO, id);
		List<Municipio> municipios = (List<Municipio>) consultarListaObjetosDelaHacienda(
				Constantes.MUNICIPIO, id);
		List<UnidadMedida> unidadesMedida = (List<UnidadMedida>) consultarListaObjetosDelaHacienda(
				Constantes.UNIDADMEDIDA, id);
		hacienda.setEmpresa(empresa);
		hacienda.setContacto(contacto);
		hacienda.setPais(pais);
		if (departamentos.size() > 0) {
			hacienda.setDepartamento(departamentos.get(0));
		}
		if (municipios.size() > 0) {
			hacienda.setMunicipio(municipios.get(0));
		}
		if (unidadesMedida.size() > 0) {
			hacienda.setUnidadMedida(unidadesMedida.get(0));
		}
		return hacienda;
	}

	/**
	 * Method that allows consulting the farm by id without the farm relations
	 * 
	 * @param idHacienda
	 *            : identification of the farm on application
	 * @return hacienda: object of the Farm with information consulted
	 * 
	 * @throws Exception
	 */
	public Hacienda consultarHaciendaPorId(int idHacienda) throws Exception {
		return (Hacienda) em
				.createQuery("SELECT f FROM Hacienda f WHERE f.id=:idHacienda")
				.setParameter("idHacienda", idHacienda).getSingleResult();
	}

	/**
	 * Method that allows query a condition depending on the object sent as a
	 * parameter
	 * 
	 * @param nomObject
	 *            : name of the object to be found at the Farm
	 * @param idHacienda
	 *            : id of the farm on application
	 * @return Object information associated with the Farm or null but there.
	 * 
	 * @throws Exception
	 */
	public Object consultarObjetoDelaHacienda(String nomObject, int idHacienda)
			throws Exception {
		return (Object) em
				.createQuery(
						"SELECT f." + nomObject
								+ " FROM Hacienda f WHERE f.id=:idHacienda")
				.setParameter("idHacienda", idHacienda).getSingleResult();
	}

	/**
	 * Method that allows a list of objects depending on the condition sent as a
	 * parameter
	 * 
	 * @param nomObject
	 *            : name of the object to be found at the Farm
	 * @param idHacienda
	 *            : id of the farm on application
	 * @return List of Objects with information.
	 * 
	 * @throws Exception
	 */
	public List<?> consultarListaObjetosDelaHacienda(String nomObject,
			int idHacienda) throws Exception {
		return em
				.createQuery(
						"SELECT f." + nomObject
								+ " FROM Hacienda f WHERE f.id=:idHacienda")
				.setParameter("idHacienda", idHacienda).getResultList();
	}

	/**
	 * Save a farm in the database
	 * 
	 * @param hacienda
	 *            : Farm to save
	 * @throws Exception
	 */
	public void guardarHacienda(Hacienda hacienda) throws Exception {
		em.persist(hacienda);
	}

	/**
	 * Edit the information of an existing farm in the database
	 * 
	 * @param hacienda
	 *            : Farm to edit
	 * @throws Exception
	 */
	public void editarHacienda(Hacienda hacienda) throws Exception {
		em.merge(hacienda);
	}

	/**
	 * Check if the name of the farm already exists for the company
	 * 
	 * @modify 09/02/2012 marisol.calderon
	 * 
	 * @param name
	 *            :name of the farm that is validated
	 * @param idEmpresa
	 *            : id of the company to which the farm belongs
	 * @return boolean: value true if the name exists or false otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nombreExiste(String name, Integer idEmpresa)
			throws Exception {
		List<Hacienda> results = em
				.createQuery(
						"FROM Hacienda f WHERE f.nombre=:nombre "
								+ "AND f.empresa.id=:idEmpresa")
				.setParameter("nombre", name)
				.setParameter("idEmpresa", idEmpresa).getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the name of the farm already exists for the company when editing
	 * 
	 * @modify 09/02/2012 marisol.calderon
	 * 
	 * @param name
	 *            : name of the farm that is validated
	 * @param id
	 *            : id of the farm that is being edited
	 * @param idEmpresa
	 *            : id of the company to which the farm belongs
	 * @return boolean:value true if the name exists or false otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nombreExisteActualizar(String name, int id, Integer idEmpresa)
			throws Exception {
		List<Hacienda> results = em
				.createQuery(
						"FROM Hacienda f WHERE f.nombre=:nombre "
								+ "AND f.empresa.id=:idEmpresa AND f.id <>:id")
				.setParameter("nombre", name).setParameter("id", id)
				.setParameter("idEmpresa", idEmpresa).getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Allows to consult the farms for company id
	 * 
	 * @param idEmpresa
	 *            : Company ID
	 * @return List<Hacienda>: List of farms found for the company or null if
	 *         not present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> buscarHaciendasPorIdEmpresa(int idEmpresa)
			throws Exception {
		return em
				.createQuery(
						"SELECT f FROM Hacienda f "
								+ "WHERE f.empresa.id=:idEmpresa "
								+ "ORDER BY f.nombre")
				.setParameter("idEmpresa", idEmpresa).getResultList();
	}

	/**
	 * Returns the number of farms that exist by company name
	 * 
	 * @param nombreEmpresa
	 *            : company name by which you want to filter the farms.
	 * @return Long: number of existing farms in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	public Long cantidadHaciendasPorNombreEmpresa(String nombreEmpresa)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(f) FROM Hacienda f JOIN f.empresa emp "
								+ "WHERE UPPER(emp.nombre) LIKE :keyword ")
				.setParameter("keyword", "%" + nombreEmpresa + "%")
				.getSingleResult();
	}

	/**
	 * Returns the farms that exist company name
	 * 
	 * @param nombreEmpresa
	 *            : company name you want to refer to the farm
	 * @param start
	 *            : where he started the consultation record
	 * @param range
	 *            : Maximum number of records in the query
	 * @return List<Hacienda>: list of existing farms in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> buscarHaciendasPorNombreEmpresa(String nombreEmpresa,
			int start, int range) throws Exception {
		List<Hacienda> resultList = em
				.createQuery(
						"SELECT f FROM Hacienda f "
								+ "JOIN FETCH f.empresa emp JOIN FETCH f.contacto "
								+ "JOIN FETCH f.pais "
								+ "WHERE UPPER(emp.nombre) LIKE :keyword "
								+ "ORDER BY emp.nombre, f.nombre")
				.setParameter("keyword", "%" + nombreEmpresa + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
		return resultList;
	}

	/**
	 * Allows to consult to the farms ID number that exist in a company.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param number
	 *            : Identification number or code of the farm.
	 * @param idHacienda
	 *            : Identifier or primary key of the farm.
	 * @param idEmpresa
	 *            : identifier of the company to which the farm belongs
	 * @return List of existing farms or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> buscarHaciendasPorNumeroYEmpresaEdicion(
			String number, int idHacienda, Integer idEmpresa) throws Exception {
		return em
				.createQuery(
						"SELECT f FROM Hacienda f WHERE f.codigoRegistro=:numero "
								+ "AND f.empresa.id=:idEmpresa "
								+ "AND f.id <> :idHacienda")
				.setParameter("numero", number)
				.setParameter("idHacienda", idHacienda)
				.setParameter("idEmpresa", idEmpresa).getResultList();
	}

	/**
	 * Allows to consult to the farms ID number that exist in a company.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param numero
	 *            : Identification number or code of the farm.
	 * @param idEmpresa
	 *            : identifier of the company to which the farm belongs.
	 * @return List of existing farms or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> buscarHaciendasPorNumeroYEmpresa(String numero,
			Integer idEmpresa) throws Exception {
		return em
				.createQuery(
						"SELECT f FROM Hacienda f WHERE f.codigoRegistro=:numero "
								+ "AND f.empresa.id=:idEmpresa  ")
				.setParameter("numero", numero)
				.setParameter("idEmpresa", idEmpresa).getResultList();
	}

	/**
	 * Method that allows consulting existing the farms.
	 * 
	 * @author marisol.calderon
	 * 
	 * @return List of existing farms.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> consultarHaciendasVigentes() throws Exception {
		return em.createQuery(
				"SELECT f FROM Hacienda f WHERE f.fechaFinVigencia IS NULL "
						+ "ORDER BY f.nombre ").getResultList();
	}

	/**
	 * Method that allows check the properties of a company with access
	 * permissions a person has on the system.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param idEmpresa
	 *            : company id in session.
	 * @param idPersonaSesion
	 *            : identifier of the person in session at which it is consulted
	 *            by the farms that have permission to access.
	 * @return List of farms to which the company has access.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> consultarHaciendasConPermisosAccesoEmpresa(
			int idEmpresa, int idPersonaSesion) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT ppe.hacienda FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.empresa.id=:idEmpresa ");
		query.append("AND ppe.persona.id = :idPersonaSesion ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :fechaActual) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("idPersonaSesion", idPersonaSesion);
		q.setParameter("fechaActual", new Date());
		return q.getResultList();
	}

	/**
	 * Method that allows check the list of farms associated with a specific
	 * document.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idDocumento
	 *            : id of the document to which you want to see the farms.
	 * @return List<Hacienda>: List of farms associated with the document or
	 *         null if there are no records.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hacienda> consultarDocumentosHacienda(int idDocumento)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT df.llavePrimariaDocumentoHacienda.hacienda FROM DocumentoHacienda df ");
		query.append("JOIN FETCH df.llavePrimariaDocumentoHacienda ");
		query.append("WHERE df.llavePrimariaDocumentoHacienda.documento.id = :idDocumento ");
		query.append("AND df.fechaFinVigencia IS NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idDocumento", idDocumento);
		return q.getResultList();
	}
}