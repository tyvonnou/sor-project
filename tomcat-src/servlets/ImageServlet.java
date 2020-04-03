package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
@WebServlet("/image/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 5 * 5)
public class ImageServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ImageServlet() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
		String title = request.getPathInfo();
		if (title == null || title.isEmpty()) {
			HttpStatusCode.NotFound.sendStatus(response);
			return;
		}
		String[] split = title.split("/", 3);
		if (split.length >= 3) {
			HttpStatusCode.NotFound.sendStatus(response);
			return;
		}
		title = split[1];
		try {
			ServeurRMI serveurRMI = getServerRMI();
			byte[] bytes = serveurRMI.getImage(title);
			if (bytes.length == 0) {
				HttpStatusCode.NotFound.sendStatus(response);
			}
			response.getOutputStream().write(bytes);
		} catch (Exception e) {
			// TODO: handle exception
		}
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("doPost image");

    try {

      // Get Data
      PartFormater formater = new PartFormater(request);
      String title = formater.readString("title");
      Long pictureSize = formater.readLong("picture-size");
      Integer size = formater.readInteger("size");
      Long begin = formater.readLong("begin");

      PartFormater.File picture = formater.getFile("picture");
      
      if (formater.sendError(response)) return;
		ServeurRMI serveurRMI = getServerRMI();
		serveurRMI.newImage(title, pictureSize);
		byte[] bytes = new byte[size];
		try (InputStream in = picture.getInputStream()) {
			if (in.read(bytes) == -1) {
				throw new IOException("Buffer is invalid");
			}
			serveurRMI.insertByte(title, begin, bytes);			
		}
		
      // Get the images
      

    } catch (Exception e) {
      response.setStatus(500);
      e.printStackTrace();
    }
  }
  
  protected ServeurRMI getServerRMI() throws RemoteException, NotBoundException {
	  Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
	  return (ServeurRMI) registry.lookup(Config.config.getService());
  }

}
