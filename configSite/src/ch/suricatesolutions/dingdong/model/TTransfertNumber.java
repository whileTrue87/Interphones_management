package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_transfert_number database table.
 * 
 */
@Entity
@Table(name="t_transfert_number")
public class TTransfertNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pkTransfertNumber;
	private String number;
	private TDrivebox TDrivebox;

    public TTransfertNumber() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_transfert_number")
	public int getPkTransfertNumber() {
		return this.pkTransfertNumber;
	}

	public void setPkTransfertNumber(int pkTransfertNumber) {
		this.pkTransfertNumber = pkTransfertNumber;
	}


	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="fk_drivebox")
	public TDrivebox getTDrivebox() {
		return this.TDrivebox;
	}

	public void setTDrivebox(TDrivebox TDrivebox) {
		this.TDrivebox = TDrivebox;
	}
	
}