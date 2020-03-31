package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import config.Config;

public class ServeurRMIImpl implements ServeurRMI {
	private static final Logger logger = Logger.getLogger(ServeurRMIImpl.class.getName());
	
	@Override
	public String meth() throws RemoteException {
		logger.info("RMI : meth");
		return "reponse du serveur rmi";
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

}
