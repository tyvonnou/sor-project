package rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import config.Config;

public class ClientRMI {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: $0 <image filename>");
			return;
		}
		try {
			Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
			ServeurRMI serveur = (ServeurRMI) registry.lookup(Config.config.getService());
			logger.info(serveur.meth());
			String title = "test-" + Long.toString(System.currentTimeMillis(), 36);
			File src = Paths.get(args[0]).toFile();
			byte[] bytes;
			try (InputStream in = new FileInputStream(src)) {
				bytes = new byte[Config.config.getBufferSize()];
				int res;
				Long begin = 0L;
				serveur.newImage(title, src.length());
				while ((res = in.read(bytes)) != -1) {
					serveur.insertByte(title, begin, bytes);
					begin += res;
				}
			}
			try (OutputStream out = new FileOutputStream(title + ".jpg")) {
				bytes = serveur.getImage(title);
				out.write(bytes);
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	private static final Logger logger = Logger.getLogger(ClientRMI.class.getName());
}
