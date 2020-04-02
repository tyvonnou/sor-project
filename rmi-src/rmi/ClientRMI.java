package rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
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
			logger.info(serveur.meth());
			String title = "test-" + Long.toString(System.currentTimeMillis(), 36);
			File p = Paths.get("638198.jpg").toFile();
			InputStream in = new FileInputStream(p);
			byte[] bytes = new byte[Config.config.getBufferSize()];
			int res;
			Long begin = 0L;
			serveur.newImage(title, p.length());
			while ((res = in.read(bytes)) != -1) {
				serveur.insertByte(title, begin, bytes);
				begin += res;
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}
