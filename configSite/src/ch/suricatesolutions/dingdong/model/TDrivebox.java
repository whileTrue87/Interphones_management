package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_drivebox database table.
 * 
 */
@Entity
@Table(name="t_drivebox")
public class TDrivebox implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pkDrivebox;
	private String idDrivebox;
	private byte mute;
	private TClient TClient;
	private List<TDriveboxHasApplication> TDriveboxHasApplications;
	private List<TSipphone> TSipphones;
	private List<TTransfertNumber> TTransfertNumbers;

    public TDrivebox() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_drivebox")
	public int getPkDrivebox() {
		return this.pkDrivebox;
	}

	public void setPkDrivebox(int pkDrivebox) {
		this.pkDrivebox = pkDrivebox;
	}


	@Column(name="id_drivebox")
	public String getIdDrivebox() {
		return this.idDrivebox;
	}

	public void setIdDrivebox(String idDrivebox) {
		this.idDrivebox = idDrivebox;
	}


	public byte getMute() {
		return this.mute;
	}

	public void setMute(byte mute) {
		this.mute = mute;
	}


	//bi-directional many-to-one association to TClient
    @ManyToOne
	@JoinColumn(name="fk_client")
	public TClient getTClient() {
		return this.TClient;
	}

	public void setTClient(TClient TClient) {
		this.TClient = TClient;
	}
	

	//bi-directional many-to-one association to TDriveboxHasApplication
	@OneToMany(mappedBy="TDrivebox")
	public List<TDriveboxHasApplication> getTDriveboxHasApplications() {
		return this.TDriveboxHasApplications;
	}

	public void setTDriveboxHasApplications(List<TDriveboxHasApplication> TDriveboxHasApplications) {
		this.TDriveboxHasApplications = TDriveboxHasApplications;
	}
	

	//bi-directional many-to-many association to TSipphone
    @ManyToMany
	@JoinTable(
		name="t_drivebox_has_sipphone"
		, joinColumns={
			@JoinColumn(name="pfk_drivebox")
			}
		, inverseJoinColumns={
			@JoinColumn(name="pfk_sipphone")
			}
		)
	public List<TSipphone> getTSipphones() {
		return this.TSipphones;
	}

	public void setTSipphones(List<TSipphone> TSipphones) {
		this.TSipphones = TSipphones;
	}
	

	//bi-directional many-to-one association to TTransfertNumber
	@OneToMany(mappedBy="TDrivebox")
	public List<TTransfertNumber> getTTransfertNumbers() {
		return this.TTransfertNumbers;
	}

	public void setTTransfertNumbers(List<TTransfertNumber> TTransfertNumbers) {
		this.TTransfertNumbers = TTransfertNumbers;
	}
	
}