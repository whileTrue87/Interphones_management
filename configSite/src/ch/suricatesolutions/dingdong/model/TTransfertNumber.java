package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_transfert_number database table.
 * 
 */
@Entity
@Table(name="t_transfert_number")
@NamedQueries({ @NamedQuery(name = "TTransfertNumber.getNumberForDrivebox", query = "SELECT t FROM TTransfertNumber t WHERE t.TDrivebox.pkDrivebox=:pkDrivebox")
})
public class TTransfertNumber implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_transfert_number")
	private int pkTransfertNumber;

	private String number;

	//bi-directional many-to-one association to TDrivebox
    @ManyToOne
	@JoinColumn(name="fk_drivebox")
	private TDrivebox TDrivebox;

    public TTransfertNumber() {
    }

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

	public TDrivebox getTDrivebox() {
		return this.TDrivebox;
	}

	public void setTDrivebox(TDrivebox TDrivebox) {
		this.TDrivebox = TDrivebox;
	}
	
}