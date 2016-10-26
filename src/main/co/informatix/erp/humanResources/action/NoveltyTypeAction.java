package co.informatix.erp.humanResources.action;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.humanResources.entities.NoveltyType;

/**
 * This class implements the logic related to create, update, and delete types
 * of novelties in the system.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class NoveltyTypeAction implements Serializable {

	private NoveltyType noveltyType;

	/**
	 * @return
	 */
	public NoveltyType getNoveltyType() {
		return noveltyType;
	}

	/**
	 * @param noveltyType
	 */
	public void setNoveltyType(NoveltyType noveltyType) {
		this.noveltyType = noveltyType;
	}

	/**
	 * Method to edit or create a new type of novelty.
	 * 
	 * @param noveltyType
	 *            : Type of novelty that will add or edit.
	 * @return regNoveltyType: Redirected to the template registerNoveltyType.
	 */
	public String addEditNoveltyType(NoveltyType noveltyType) {
		if (noveltyType != null) {
			this.noveltyType = noveltyType;
		} else {
			this.noveltyType = new NoveltyType();
		}
		return "regNoveltyType";
	}

}
