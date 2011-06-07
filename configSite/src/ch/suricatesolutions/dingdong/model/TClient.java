package ch.suricatesolutions.dingdong.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_client database table.
 * 
 */
@Entity
@Table(name="t_client")
public class TClient implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pkClient;
	private String login;
	private String nom;
	private int npa;
	private String password;
	private String prenom;
	private List<TDrivebox> TDriveboxs;

    public TClient() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_client")
	public int getPkClient() {
		return this.pkClient;
	}

	public void setPkClient(int pkClient) {
		this.pkClient = pkClient;
	}


	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}


	public int getNpa() {
		return this.npa;
	}

	public void setNpa(int npa) {
		this.npa = npa;
	}


	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	//bi-directional many-to-one association to TDrivebox
	@OneToMany(mappedBy="TClient")
	public List<TDrivebox> getTDriveboxs() {
		return this.TDriveboxs;
	}

	public void setTDriveboxs(List<TDrivebox> TDriveboxs) {
		this.TDriveboxs = TDriveboxs;
	}
	
}