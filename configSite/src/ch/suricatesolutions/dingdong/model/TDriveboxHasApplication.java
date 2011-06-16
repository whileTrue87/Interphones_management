package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_drivebox_has_application database table.
 * 
 */
@Entity
@Table(name="t_drivebox_has_application")
@NamedQueries({ 
	@NamedQuery(name = "TDriveboxHasApplication.installedAppsFromPkDrivebox", query = "SELECT a FROM TDriveboxHasApplication a WHERE a.id.pfkDrivebox=:pk and a.enabled=true"),
	@NamedQuery(name = "TDriveboxHasApplication.appFromPks", query = "SELECT a FROM TDriveboxHasApplication a WHERE a.id.pfkDrivebox=:pkDrivebox and a.id.pfkApplication=:pkApplication"),
	@NamedQuery(name = "TDriveboxHasApplication.countAppFromPks", query = "SELECT count(a) FROM TDriveboxHasApplication a WHERE a.id.pfkDrivebox=:pkDrivebox and a.id.pfkApplication=:pkApplication")})
public class TDriveboxHasApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDriveboxHasApplicationPK id;

    @Lob()
	@Column(name="configuration_xml")
	private byte[] configurationXml;
    
    @Lob()
    @Column(name="parameters")
	private byte[] parameters;

	//bi-directional many-to-one association to TApplication
    @ManyToOne
	@JoinColumn(name="pfk_application")
	private TApplication TApplication;

	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="pfk_drivebox")
	private TDrivebox TDrivebox;
    
    private boolean enabled;

    public TDriveboxHasApplication() {
    }

	public TDriveboxHasApplicationPK getId() {
		return this.id;
	}

	public void setId(TDriveboxHasApplicationPK id) {
		this.id = id;
	}
	
	public byte[] getConfigurationXml() {
		return this.configurationXml;
	}

	public void setConfigurationXml(byte[] configurationXml) {
		this.configurationXml = configurationXml;
	}

	public TApplication getTApplication() {
		return this.TApplication;
	}

	public void setTApplication(TApplication TApplication) {
		this.TApplication = TApplication;
	}
	
	public TDrivebox getTDrivebox() {
		return this.TDrivebox;
	}

	public void setTDrivebox(TDrivebox TDrivebox) {
		this.TDrivebox = TDrivebox;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setParameters(byte[] parameters) {
		this.parameters = parameters;
	}

	public byte[] getParameters() {
		return parameters;
	}
	
}