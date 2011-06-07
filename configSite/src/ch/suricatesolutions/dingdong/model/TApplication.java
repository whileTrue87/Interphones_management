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
public class TApplication implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pkApplication;
	private String configurationLink;
	private byte[] configurationSchema;
	private byte[] driveboxApplication;
	private byte[] icone;
	private String id;
	private String version;
	private List<TDriveboxHasApplication> TDriveboxHasApplications;

    public TApplication() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_application")
	public int getPkApplication() {
		return this.pkApplication;
	}

	public void setPkApplication(int pkApplication) {
		this.pkApplication = pkApplication;
	}


	@Column(name="configuration_link")
	public String getConfigurationLink() {
		return this.configurationLink;
	}

	public void setConfigurationLink(String configurationLink) {
		this.configurationLink = configurationLink;
	}


    @Lob()
	@Column(name="configuration_schema")
	public byte[] getConfigurationSchema() {
		return this.configurationSchema;
	}

	public void setConfigurationSchema(byte[] configurationSchema) {
		this.configurationSchema = configurationSchema;
	}


    @Lob()
	@Column(name="drivebox_application")
	public byte[] getDriveboxApplication() {
		return this.driveboxApplication;
	}

	public void setDriveboxApplication(byte[] driveboxApplication) {
		this.driveboxApplication = driveboxApplication;
	}


    @Lob()
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


	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	//bi-directional many-to-one association to TDriveboxHasApplication
	@OneToMany(mappedBy="TApplication")
	public List<TDriveboxHasApplication> getTDriveboxHasApplications() {
		return this.TDriveboxHasApplications;
	}

	public void setTDriveboxHasApplications(List<TDriveboxHasApplication> TDriveboxHasApplications) {
		this.TDriveboxHasApplications = TDriveboxHasApplications;
	}
	
}