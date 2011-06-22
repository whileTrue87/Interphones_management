package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_other_softs database table.
 * 
 */
@Entity
@Table(name="t_other_softs")
public class TOtherSoft implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_other_softs")
	private int pkOtherSofts;

    @Lob()
	@Column(name="soft_jar")
	private byte[] softJar;

	private String version;

	//bi-directional many-to-one association to TDriveboxHasOtherSoft
	@OneToMany(mappedBy="TOtherSoft")
	private List<TDriveboxHasOtherSoft> TDriveboxHasOtherSofts;

    public TOtherSoft() {
    }

	public int getPkOtherSofts() {
		return this.pkOtherSofts;
	}

	public void setPkOtherSofts(int pkOtherSofts) {
		this.pkOtherSofts = pkOtherSofts;
	}

	public byte[] getSoftJar() {
		return this.softJar;
	}

	public void setSoftJar(byte[] softJar) {
		this.softJar = softJar;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<TDriveboxHasOtherSoft> getTDriveboxHasOtherSofts() {
		return this.TDriveboxHasOtherSofts;
	}

	public void setTDriveboxHasOtherSofts(List<TDriveboxHasOtherSoft> TDriveboxHasOtherSofts) {
		this.TDriveboxHasOtherSofts = TDriveboxHasOtherSofts;
	}
	
}