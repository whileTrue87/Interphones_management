package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the t_drivebox_has_other_softs database table.
 * 
 */
@Embeddable
public class TDriveboxHasOtherSoftPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="t_other_softs_pk_other_softs", insertable=false, updatable=false)
	private int tOtherSoftsPkOtherSofts;

	@Column(name="t_drivebox_pk_drivebox", insertable=false, updatable=false)
	private int tDriveboxPkDrivebox;

    public TDriveboxHasOtherSoftPK() {
    }
	public int getTOtherSoftsPkOtherSofts() {
		return this.tOtherSoftsPkOtherSofts;
	}
	public void setTOtherSoftsPkOtherSofts(int tOtherSoftsPkOtherSofts) {
		this.tOtherSoftsPkOtherSofts = tOtherSoftsPkOtherSofts;
	}
	public int getTDriveboxPkDrivebox() {
		return this.tDriveboxPkDrivebox;
	}
	public void setTDriveboxPkDrivebox(int tDriveboxPkDrivebox) {
		this.tDriveboxPkDrivebox = tDriveboxPkDrivebox;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDriveboxHasOtherSoftPK)) {
			return false;
		}
		TDriveboxHasOtherSoftPK castOther = (TDriveboxHasOtherSoftPK)other;
		return 
			(this.tOtherSoftsPkOtherSofts == castOther.tOtherSoftsPkOtherSofts)
			&& (this.tDriveboxPkDrivebox == castOther.tDriveboxPkDrivebox);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tOtherSoftsPkOtherSofts;
		hash = hash * prime + this.tDriveboxPkDrivebox;
		
		return hash;
    }
}