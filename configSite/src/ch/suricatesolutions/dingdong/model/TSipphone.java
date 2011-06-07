package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_sipphone database table.
 * 
 */
@Entity
@Table(name="t_sipphone")
public class TSipphone implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pkSipphone;
	private String idSipphone;
	private List<TDrivebox> TDriveboxs;

    public TSipphone() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_sipphone")
	public int getPkSipphone() {
		return this.pkSipphone;
	}

	public void setPkSipphone(int pkSipphone) {
		this.pkSipphone = pkSipphone;
	}


	@Column(name="id_sipphone")
	public String getIdSipphone() {
		return this.idSipphone;
	}

	public void setIdSipphone(String idSipphone) {
		this.idSipphone = idSipphone;
	}


	//bi-directional many-to-many association to TDrivebox
	@ManyToMany(mappedBy="TSipphones")
	public List<TDrivebox> getTDriveboxs() {
		return this.TDriveboxs;
	}

	public void setTDriveboxs(List<TDrivebox> TDriveboxs) {
		this.TDriveboxs = TDriveboxs;
	}
	
}