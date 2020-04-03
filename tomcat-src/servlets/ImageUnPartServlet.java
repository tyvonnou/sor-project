package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Config;
import helpers.PartFormater;
import helpers.HttpStatusCode;
import rmi.ServeurRMI;

/**
 * Servlet implementation class Image
 */
@WebServlet("/image_onepart")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 5 * 5)
public class ImageUnPartServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(ImageUnPartServlet.class.getName());


  /**
   * @see HttpServlet#HttpServlet()
   */
  public ImageUnPartServlet() {
    super();
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	  logger.info("POST");

    try {

      // Get Data
      PartFormater formater = new PartFormater(request);
      String title = formater.readString("title");
      Long pictureSize = formater.readLong("size");
      Long begin = 0L;

      PartFormater.File picture = formater.getFile("picture");
      
      if (formater.sendError(response)) return;
		ServeurRMI serveurRMI = getServerRMI();
		serveurRMI.newImage(title, pictureSize);
		byte[] bytes = new byte[Config.config.getBufferSize()];
		try (InputStream in = picture.getInputStream()) {
			int res;
			while ((res = in.read(bytes)) != -1) {
				serveurRMI.insertByte(title, begin, bytes);
				begin += res;
			}
		}

    } catch (Exception e) {
		HttpStatusCode.InternalServerError.sendStatus(response);
		logger.warning(e.getMessage());
    }
  }
  
  protected ServeurRMI getServerRMI() throws RemoteException, NotBoundException {
	  Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
	  return (ServeurRMI) registry.lookup(Config.config.getService());
  }

}
