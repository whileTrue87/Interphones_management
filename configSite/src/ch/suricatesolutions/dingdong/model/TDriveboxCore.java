package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the t_drivebox_core database table.
 * 
 */
@Entity
@Table(name="t_drivebox_core")
@NamedQueries({ 
	@NamedQuery(name = "TDriveboxCore.LatestCoreVersionNumber", query = "SELECT d.version FROM TDriveboxCore d order by d.releaseDate"),
	@NamedQuery(name = "TDriveboxCore.LatestCore", query = "SELECT d.applicationJar FROM TDriveboxCore d order by d.releaseDate"),
})
public class TDriveboxCore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_drivebox_core")
	private int pkDriveboxCore;

    @Lob()
	@Column(name="application_jar")
	private byte[] applicationJar;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="release_date")
	private Date releaseDate;

	private String version;

    public TDriveboxCore() {
    }

	public int getPkDriveboxCore() {
		return this.pkDriveboxCore;
	}

	public void setPkDriveboxCore(int pkDriveboxCore) {
		this.pkDriveboxCore = pkDriveboxCore;
	}

	public byte[] getApplicationJar() {
		return this.applicationJar;
	}

	public void setApplicationJar(byte[] applicationJar) {
		this.applicationJar = applicationJar;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}