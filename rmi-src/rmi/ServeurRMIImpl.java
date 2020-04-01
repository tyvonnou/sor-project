package rmi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Config;

public class ServeurRMIImpl implements ServeurRMI {
	private static final Logger logger = Logger.getLogger(ServeurRMIImpl.class.getName());
	
	@Override
	public String meth() throws RemoteException {
		logger.info("meth");
		return "reponse du serveur rmi";
	}
	
	@Override
	public void newImage(String title, Integer size) throws RemoteException {
		logger.log(Level.INFO, "newImage '{}'", title);
		Integer bufferSize = Config.config.getBufferSize();
		Integer length = size / bufferSize + 1;
		this.tmpImages.put(title, new TmpImage(length));
	}

	@Override
	public void insertByte(String title, Integer begin, byte[] buffer) throws RemoteException {
		String message = String.format("%s, %d", title, begin); 
		TmpImage tmp = this.tmpImages.get(title);
		logger.log(Level.INFO, "insertByte: {}", message);
		if (!tmp.add(begin, buffer)) {
			logger.severe("insertByte: tmp is full");
			return;
		}
		if (tmp.isFull()) {
			byte[] bytes = tmp.join();
			Path p = Paths.get(title).toAbsolutePath();
			logger.info(p.toString());
			try {
				FileOutputStream out = new FileOutputStream(p.toFile());
				out.write(bytes);
			} catch (IOException e) {
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
}
