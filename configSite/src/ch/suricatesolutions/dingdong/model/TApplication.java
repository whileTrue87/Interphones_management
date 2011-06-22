package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the t_application database table.
 * 
 */
@Entity
@Table(name="t_application")
@NamedQueries({ @NamedQuery(name = "TApplication.allApps", query = "SELECT a FROM TApplication a"),
	@NamedQuery(name = "TApplication.selectedAppsFromId", query = "SELECT a FROM TApplication a JOIN a.TDriveboxHasApplications d WHERE d.id.pfkDrivebox=:id"),
	@NamedQuery(name = "TApplication.AppFromId", query = "SELECT a FROM TApplication a WHERE a.id=:id"),
	@NamedQuery(name = "TApplication.AppFromPk", query = "SELECT a FROM TApplication a WHERE a.pkApplication=:pk"),
	@NamedQuery(name = "TApplication.pkFromId", query = "SELECT a.pkApplication FROM TApplication a WHERE a.id=:id")
})
@NamedNativeQueries({ @NamedNativeQuery(name = "TApplication.insertNewApplication", query = "INSERT INTO t_application (pk_application, configuration_schema, drivebox_application, id, version, icone, name, configuration_link, configuration_page, back_bean_name, back_bean) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")}) 

public class TApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_application")
	private int pkApplication;

	@Column(name="configuration_link")
	private String configurationLink;

    @Lob()
	@Column(name="configuration_schema")
	private byte[] configurationSchema;

    @Lob()
	@Column(name="drivebox_application")
	private byte[] driveboxApplication;
    
    @Lob()
	@Column(name="configuration_page")
	private byte[] configurationPage;

    @Lob()
	private byte[] icone;

	private String id;

	private String name;

	private String version;
	
	@Column(name="back_bean_name")
	private String backBeanName;
	
	@Lob()
	@Column(name="back_bean")
	private byte[] backBean;

	//bi-directional many-to-one association to TDriveboxHasApplication
	@OneToMany(mappedBy="TApplication")
	private List<TDriveboxHasApplication> TDriveboxHasApplications;

    public TApplication() {
    }

	public int getPkApplication() {
		return this.pkApplication;
	}

	public void setPkApplication(int pkApplication) {
		this.pkApplication = pkApplication;
	}

	public String getConfigurationLink() {
		return this.configurationLink;
	}

	public void setConfigurationLink(String configurationLink) {
		this.configurationLink = configurationLink;
	}

	public byte[] getConfigurationSchema() {
		return this.configurationSchema;
	}

	public void setConfigurationSchema(byte[] configurationSchema) {
		this.configurationSchema = configurationSchema;
	}

	public byte[] getDriveboxApplication() {
		return this.driveboxApplication;
	}

	public void setDriveboxApplication(byte[] driveboxApplication) {
		this.driveboxApplication = driveboxApplication;
	}

	public byte[] getIcone() {
		return this.icone;
	}

	public void setIcone(byte[] icone) {
		this.icone = icone;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<TDriveboxHasApplication> getTDriveboxHasApplications() {
		return this.TDriveboxHasApplications;
	}

	public void setTDriveboxHasApplications(List<TDriveboxHasApplication> TDriveboxHasApplications) {
		this.TDriveboxHasApplications = TDriveboxHasApplications;
	}

	public void setConfigurationPage(byte[] configurationPage) {
		this.configurationPage = configurationPage;
	}

	public byte[] getConfigurationPage() {
		return configurationPage;
	}

	public void setBackBeanName(String backBeanName) {
		this.backBeanName = backBeanName;
	}

	public String getBackBeanName() {
		return backBeanName;
	}

	public void setBackBean(byte[] backBean) {
		this.backBean = backBean;
	}

	public byte[] getBackBean() {
		return backBean;
	}
	
}