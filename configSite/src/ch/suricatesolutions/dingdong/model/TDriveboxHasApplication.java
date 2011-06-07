package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_drivebox_has_application database table.
 * 
 */
@Entity
@Table(name="t_drivebox_has_application")
public class TDriveboxHasApplication implements Serializable {
	private static final long serialVersionUID = 1L;
	private TDriveboxHasApplicationPK id;
	private byte[] configurationXml;
	private TDrivebox TDrivebox;
	private TApplication TApplication;

    public TDriveboxHasApplication() {
    }


	@EmbeddedId
	public TDriveboxHasApplicationPK getId() {
		return this.id;
	}

	public void setId(TDriveboxHasApplicationPK id) {
		this.id = id;
	}
	

    @Lob()
	@Column(name="configuration_xml")
	public byte[] getConfigurationXml() {
		return this.configurationXml;
	}

	public void setConfigurationXml(byte[] configurationXml) {
		this.configurationXml = configurationXml;
	}


	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="pfk_drivebox")
	public TDrivebox getTDrivebox() {
		return this.TDrivebox;
	}

	public void setTDrivebox(TDrivebox TDrivebox) {
		this.TDrivebox = TDrivebox;
	}
	

	//bi-directional many-to-one association to TApplication
    @ManyToOne
	@JoinColumn(name="pfk_application")
	public TApplication getTApplication() {
		return this.TApplication;
	}

	public void setTApplication(TApplication TApplication) {
		this.TApplication = TApplication;
	}
	
}