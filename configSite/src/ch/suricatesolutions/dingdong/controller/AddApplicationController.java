package ch.suricatesolutions.dingdong.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ch.suricatesolutions.dingdong.business.DaoManager;
import ch.suricatesolutions.dingdong.business.FileManager;
import ch.suricatesolutions.dingdong.model.TApplication;

@ManagedBean
@RequestScoped
public class AddApplicationController {

	@EJB
	DaoManager dao;

	@EJB
	FileManager file;

	private TApplication uploadedApp;
	private static int appplicationToDelete;

	private boolean panelRender;

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println("Upload");
		UploadedFile uploadFile = event.getFile();

		try {
			byte[] jar = uploadFile.getContents();
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			String rootPath = ec.getRealPath("/");
			String tempStr = rootPath + File.separator + "temp";
			File temp = new File(tempStr);
			if (!temp.exists()) {
				temp.mkdir();
			}
			String jarTempStr = tempStr + File.separator + "appJar" + System.currentTimeMillis() + ".jar";
			File jarTemp = new File(jarTempStr);
			jarTemp.setWritable(true);
			FileOutputStream fos = new FileOutputStream(jarTemp);
			fos.write(jar);
			fos.close();

			JarFile jf = new JarFile(jarTemp);
			byte[] desc = getEntry(jf, "application.xml");

			SAXBuilder sxb = new SAXBuilder();
			Document doc = null;
			ByteArrayInputStream bais = new ByteArrayInputStream(desc);
			doc = sxb.build(bais);
			Element root = doc.getRootElement();

			// Getting the icon

			byte[] icon = getEntry(jf, root.getChildText("icon"));

			// Getting the executable JAR
			byte[] execJar = getEntry(jf, root.getChildText("execJar"));

			// Getting the param configuration page
			String confPageName = root.getChild("confPage").getText();
			JarEntry confPageEntry = jf.getJarEntry(confPageName);
			byte[] confPage = new byte[(int) confPageEntry.getSize()];
			jf.getInputStream(confPageEntry).read(confPage);

			// Getting the param configuration backbean
			String backBeanName = root.getChild("backBean").getText();
			JarEntry backBeanEntry = jf.getJarEntry(backBeanName + ".class");
			byte[] backBean = new byte[(int) backBeanEntry.getSize()];
			jf.getInputStream(backBeanEntry).read(backBean);

			// Getting the configuration file and its information
			byte[] confFile = getEntry(jf, root.getChildText("confFile"));

			sxb = new SAXBuilder();
			bais.close();
			bais = new ByteArrayInputStream(confFile);
			doc = sxb.build(bais);
			root = doc.getRootElement();

			String id = root.getChild("id").getText();
			String name = root.getChild("name").getText();
			String version = root.getChild("version").getText();

			// Inserting in the database
			dao.insertNewApplication(id, name, version, icon, execJar, confFile, confPageName, confPage, backBeanName, backBean);
			this.uploadedApp = dao.getApplicationFromId(id);
			this.setPanelRender(true);

			// Copying page and backBean on the file system for the glassfish
			// server
			file.copyBackBeanOnDisk(backBeanName, backBean);
			file.copyPageOnDisk(confPageName, confPage);

			// System.out.println("Adding new Application");
			// System.out.println("ID = " + id);
			// System.out.println("name = " + name);
			// System.out.println("iconName = " + iconName);
			// System.out.println("version = " + version);
			// System.out.println("confFileName = " + confFileName);
			// System.out.println("execJarName = " + execJarName);

			bais.close();
			jf.close();
		} catch (JarException e) {
			String error = "Un erreur est présente dans un des fichiers de configuration";
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		} catch (JDOMException e) {
			String error = "Un erreur est présente dans un des fichiers de configuration";
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		} catch (IOException e) {
			String error = "Un erreur survenue lors de l'accès au fichier";
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		} catch (EJBException e) {
			String error = "L'application que vous désirer ajouter existe déjà";
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		} catch (Exception e) {
			String error = "Une erreur innatendue est survenue !";
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			e.printStackTrace();
		}
	}

	private byte[] getEntry(JarFile jf, String entry) throws IOException, JarException {
		JarEntry descEntry = jf.getJarEntry(entry);
		if (descEntry == null)
			throw new JarException("L'entrée " + entry + " n'est pas présente dans le JAR " + jf);
		byte[] desc = new byte[(int) descEntry.getSize()];
		jf.getInputStream(descEntry).read(desc);
		return desc;
	}

	public void setUploadedApp(TApplication uploadedApp) {
		this.uploadedApp = uploadedApp;
	}

	public TApplication getUploadedApp() {
		return uploadedApp;
	}

	public void setPanelRender(boolean panelRender) {
		this.panelRender = panelRender;
	}

	public boolean isPanelRender() {
		System.out.println("Render=" + panelRender);
		return panelRender;
	}

	public void delete() {
		// System.out.println("Delete application " +
		// AddApplicationController.appplicationToDelete);
		this.panelRender = false;
		String error = "L'application n'a pas pu être supprimée";
		try {
			boolean ok = dao.deleteApplication(AddApplicationController.appplicationToDelete);
			if (!ok) {
				System.err.println(error);
				addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			}
		} catch (Exception e) {
			System.err.println(error);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
		}
	}

	public List<TApplication> getAppsList() {
		return dao.getAllApps();
	}

	public void copyAllAppsOnDisk() {
		List<TApplication> apps = getAppsList();
		try {
			for (TApplication app : apps) {
				file.copyBackBeanOnDisk(app.getBackBeanName(), app.getBackBean());
				file.copyPageOnDisk(app.getConfigurationLink(), app.getConfigurationPage());
			}
		} catch (IOException e) {

		}
	}

	public void setApplicationToDelete(int applicationToDelete) {
		AddApplicationController.appplicationToDelete = applicationToDelete;
	}

	public int getApplicationToDelete() {
		return AddApplicationController.appplicationToDelete;
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
