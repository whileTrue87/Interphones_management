package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_drivebox database table.
 * 
 */
@Entity
@Table(name="t_drivebox")
@NamedQueries({ @NamedQuery(name = "TDrivebox.driveboxFromUser", query = "SELECT d FROM TDrivebox d JOIN d.TClient c WHERE c.login=:login"),
	@NamedQuery(name = "TDrivebox.pkFromId", query = "SELECT d.pkDrivebox FROM TDrivebox d WHERE d.idDrivebox=:id"),
	@NamedQuery(name = "TDrivebox.getDriveboxFromPk", query = "SELECT d FROM TDrivebox d WHERE d.pkDrivebox=:pkDrivebox"),	
	@NamedQuery(name = "TDrivebox.lastDashboardModification", query = "SELECT d.lastDashboardModification FROM TDrivebox d WHERE d.idDrivebox=:id")	
})

public class TDrivebox implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_drivebox")
	private int pkDrivebox;

	@Column(name="id_drivebox")
	private String idDrivebox;

	private boolean mute;
	
	private String name;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="last_dashboard_modification")
	private Date lastDashboardModification;

	//bi-directional many-to-one association to TClient
    @ManyToOne
	@JoinColumn(name="fk_client")
	private TClient TClient;

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
	private List<TSipphone> TSipphones1;

	//bi-directional many-to-one association to TDriveboxHasApplication
	@OneToMany(mappedBy="TDrivebox")
	private List<TDriveboxHasApplication> TDriveboxHasApplications;

	//bi-directional many-to-many association to TSipphone
	@ManyToMany(mappedBy="TDriveboxs2")
	private List<TSipphone> TSipphones2;

	//bi-directional many-to-one association to TTransfertNumber
	@OneToMany(mappedBy="TDrivebox")
	private List<TTransfertNumber> TTransfertNumbers;

    public TDrivebox() {
    }

	public int getPkDrivebox() {
		return this.pkDrivebox;
	}

	public void setPkDrivebox(int pkDrivebox) {
		this.pkDrivebox = pkDrivebox;
	}

	public String getIdDrivebox() {
		return this.idDrivebox;
	}

	public void setIdDrivebox(String idDrivebox) {
		this.idDrivebox = idDrivebox;
	}

	public boolean getMute() {
		return this.mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	public TClient getTClient() {
		return this.TClient;
	}

	public void setTClient(TClient TClient) {
		this.TClient = TClient;
	}
	
	public List<TSipphone> getTSipphones1() {
		return this.TSipphones1;
	}

	public void setTSipphones1(List<TSipphone> TSipphones1) {
		this.TSipphones1 = TSipphones1;
	}
	
	public List<TDriveboxHasApplication> getTDriveboxHasApplications() {
		return this.TDriveboxHasApplications;
	}

	public void setTDriveboxHasApplications(List<TDriveboxHasApplication> TDriveboxHasApplications) {
		this.TDriveboxHasApplications = TDriveboxHasApplications;
	}
	
	public List<TSipphone> getTSipphones2() {
		return this.TSipphones2;
	}

	public void setTSipphones2(List<TSipphone> TSipphones2) {
		this.TSipphones2 = TSipphones2;
	}
	
	public List<TTransfertNumber> getTTransfertNumbers() {
		return this.TTransfertNumbers;
	}

	public void setTTransfertNumbers(List<TTransfertNumber> TTransfertNumbers) {
		this.TTransfertNumbers = TTransfertNumbers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLastDashboardModification(Date lastDashboardModification) {
		this.lastDashboardModification = lastDashboardModification;
	}

	public Date getLastDashboardModification() {
		return lastDashboardModification;
	}
	
}