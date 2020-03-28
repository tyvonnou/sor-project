package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import config.Config;

public class ClientRMI {
	private static final Logger logger = Logger.getLogger(ClientRMI.class.getName());

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
			ServeurRMI serveur = (ServeurRMI) registry.lookup(Config.config.getService());
			String res = serveur.meth();
			logger.info(res);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}
