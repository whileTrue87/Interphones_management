package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_drivebox_has_other_softs database table.
 * 
 */
@Entity
@Table(name="t_drivebox_has_other_softs")
public class TDriveboxHasOtherSoft implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDriveboxHasOtherSoftPK id;

	@Column(name="installed_version")
	private String installedVersion;

	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="t_drivebox_pk_drivebox")
	private TDrivebox TDrivebox;

	//bi-directional many-to-one association to TOtherSoft
    @ManyToOne
	@JoinColumn(name="t_other_softs_pk_other_softs")
	private TOtherSoft TOtherSoft;

    public TDriveboxHasOtherSoft() {
    }

	public TDriveboxHasOtherSoftPK getId() {
		return this.id;
	}

	public void setId(TDriveboxHasOtherSoftPK id) {
		this.id = id;
	}
	
	public String getInstalledVersion() {
		return this.installedVersion;
	}

	public void setInstalledVersion(String installedVersion) {
		this.installedVersion = installedVersion;
	}

	public TDrivebox getTDrivebox() {
		return this.TDrivebox;
	}

	public void setTDrivebox(TDrivebox TDrivebox) {
		this.TDrivebox = TDrivebox;
	}
	
	public TOtherSoft getTOtherSoft() {
		return this.TOtherSoft;
	}

	public void setTOtherSoft(TOtherSoft TOtherSoft) {
		this.TOtherSoft = TOtherSoft;
	}
	
}