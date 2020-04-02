package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import base.Base;
import config.Config;
import models.Image;

public class ServeurRMIImpl implements ServeurRMI {	
	@Override
	public String meth() throws RemoteException {
		logger.info("meth");
		return "reponse du serveur rmi";
	}
	
	@Override
	public void newImage(String title, Long size) throws RemoteException {
		Integer bufferSize = Config.config.getBufferSize();
		Integer length = (int) (size / bufferSize + 1);
		if (this.tmpImages.putIfAbsent(title, new TmpImage(length)) == null) {
			logger.log(Level.INFO, "newImage {}", title);			
		}
	}

	@Override
	public void insertByte(String title, Long begin, byte[] buffer) throws RemoteException {
		String message = String.format("%s, %d", title, begin); 
		TmpImage tmp = this.tmpImages.get(title);
		logger.log(Level.INFO, "insertByte: {}", message);
		if (!tmp.add(begin, buffer)) {
			logger.severe("insertByte: tmp is full");
			return;
		}
		if (tmp.isFull()) {
			this.tmpImages.remove(title);
			Image img = new Image(title, tmp.join());
			Base b = new Base();
			try {
				b.insert(img);
			} catch (IllegalAccessException | SQLException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		int port = Config.config.getPort();
		
		// Création registry
		Registry registry = null;
		try {
			LocateRegistry.createRegistry(port);
			registry = LocateRegistry.getRegistry(port);
		} catch (RemoteException e) {
			logger.severe(e.getMessage());
		}
		// Création objet distant
		ServeurRMIImpl serveurImpl = new ServeurRMIImpl();
		ServeurRMI serveur = null;
		try {
			serveur = (ServeurRMI) UnicastRemoteObject.exportObject(serveurImpl, 0);
		} catch (RemoteException e) {
			logger.severe(e.getMessage());
		}
		
		// Enregistrement objet distant
		try {
			registry.rebind(Config.config.getService(), serveurImpl);
		} catch (NullPointerException | RemoteException e) {
			logger.severe(e.getMessage());
		}
		logger.info("Serveur RMI lancé");
		
	}

	protected Map<String, TmpImage> tmpImages = new HashMap<>();
	private static final Logger logger = Logger.getLogger(ServeurRMIImpl.class.getName());
}
