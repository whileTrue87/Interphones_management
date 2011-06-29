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

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_client")
	private int pkClient;

	private String login;

	private String name;

	private int zip;

	private String password;

	private String firstname;

	//bi-directional many-to-one association to TDrivebox
	@OneToMany(mappedBy="TClient")
	private List<TDrivebox> TDriveboxs;

    public TClient() {
    }

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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getZip() {
		return this.zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public List<TDrivebox> getTDriveboxs() {
		return this.TDriveboxs;
	}

	public void setTDriveboxs(List<TDrivebox> TDriveboxs) {
		this.TDriveboxs = TDriveboxs;
	}
	
}