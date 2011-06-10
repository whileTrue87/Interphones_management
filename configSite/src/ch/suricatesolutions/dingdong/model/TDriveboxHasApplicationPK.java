package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the t_drivebox_has_application database table.
 * 
 */
@Embeddable
public class TDriveboxHasApplicationPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="pfk_drivebox", insertable=false, updatable=false)
	private int pfkDrivebox;

	@Column(name="pfk_application", insertable=false, updatable=false)
	private int pfkApplication;

    public TDriveboxHasApplicationPK() {
    }
	public int getPfkDrivebox() {
		return this.pfkDrivebox;
	}
	public void setPfkDrivebox(int pfkDrivebox) {
		this.pfkDrivebox = pfkDrivebox;
	}
	public int getPfkApplication() {
		return this.pfkApplication;
	}
	public void setPfkApplication(int pfkApplication) {
		this.pfkApplication = pfkApplication;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDriveboxHasApplicationPK)) {
			return false;
		}
		TDriveboxHasApplicationPK castOther = (TDriveboxHasApplicationPK)other;
		return 
			(this.pfkDrivebox == castOther.pfkDrivebox)
			&& (this.pfkApplication == castOther.pfkApplication);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.pfkDrivebox;
		hash = hash * prime + this.pfkApplication;
		
		return hash;
    }
}