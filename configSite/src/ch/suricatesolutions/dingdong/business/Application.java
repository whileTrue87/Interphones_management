package ch.suricatesolutions.dingdong.business;

public class Application {
	private String name;
	private String icone;
	
	public Application(String name, String icone) {
		super();
		this.name = name;
		this.icone = icone;
	}
	
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getIcone() {
		return icone;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
