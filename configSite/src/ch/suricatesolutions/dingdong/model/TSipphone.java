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

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_sipphone")
	private int pkSipphone;

	@Column(name="id_sipphone")
	private String idSipphone;

	//bi-directional many-to-many association to TDrivebox
	@ManyToMany(mappedBy="TSipphones1")
	private List<TDrivebox> TDriveboxs1;

	//bi-directional many-to-many association to TDrivebox
    @ManyToMany
	@JoinTable(
		name="t_drivebox_has_sipphone"
		, joinColumns={
			@JoinColumn(name="pfk_sipphone")
			}
		, inverseJoinColumns={
			@JoinColumn(name="pfk_drivebox")
			}
		)
	private List<TDrivebox> TDriveboxs2;

    public TSipphone() {
    }

	public int getPkSipphone() {
		return this.pkSipphone;
	}

	public void setPkSipphone(int pkSipphone) {
		this.pkSipphone = pkSipphone;
	}

	public String getIdSipphone() {
		return this.idSipphone;
	}

	public void setIdSipphone(String idSipphone) {
		this.idSipphone = idSipphone;
	}

	public List<TDrivebox> getTDriveboxs1() {
		return this.TDriveboxs1;
	}

	public void setTDriveboxs1(List<TDrivebox> TDriveboxs1) {
		this.TDriveboxs1 = TDriveboxs1;
	}
	
	public List<TDrivebox> getTDriveboxs2() {
		return this.TDriveboxs2;
	}

	public void setTDriveboxs2(List<TDrivebox> TDriveboxs2) {
		this.TDriveboxs2 = TDriveboxs2;
	}
	
}