package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import java.util.Date;

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
		@NamedQuery(name = "TDriveboxHasApplication.countAppFromPks", query = "SELECT count(a) FROM TDriveboxHasApplication a WHERE a.id.pfkDrivebox=:pkDrivebox and a.id.pfkApplication=:pkApplication") })
@NamedNativeQueries({ @NamedNativeQuery(name = "TDriveboxHasApplication.insertNewOne", query = "INSERT INTO t_drivebox_has_application (pfk_drivebox, pfk_application, x_position, y_position, enabled, configuration_xml, last_modification) VALUES(?, ?, ?, ?, ?, ?, ?)") 
})
public class TDriveboxHasApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDriveboxHasApplicationPK id;

    @Lob()
	@Column(name="configuration_xml")
	private byte[] configurationXml;

	private boolean enabled;

    @Lob()
	private byte[] parameters;

	//bi-directional many-to-one association to TApplication
    @ManyToOne
	@JoinColumn(name="pfk_application")
	private TApplication TApplication;

	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="pfk_drivebox")
	private TDrivebox TDrivebox;
    
    @Column(name="x_position")
	private int xPosition;
	
	@Column(name="y_position")
	private int yPosition;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="last_modification")
	private Date lastModification;

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

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

	public boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public byte[] getParameters() {
		return this.parameters;
	}

	public void setParameters(byte[] parameters) {
		this.parameters = parameters;
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
	
}