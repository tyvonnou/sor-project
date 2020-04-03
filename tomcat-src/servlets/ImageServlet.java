package servlets;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Config;
import helpers.PartFormater;
import rmi.ServeurRMI;


/**
 * Servlet implementation class Image
 */
@WebServlet("/image")
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
	  System.out.println("do get image");
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
      Long size = formater.readLong("picture-size");
      Long begin = formater.readLong("begin");

      PartFormater.File picture = formater.getFile("picture");
      
      if (formater.sendError(response)) return;
      
      
      // TODO: LINK TO JAVA RMI
      	Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
		ServeurRMI serveurRMI;
		serveurRMI = (ServeurRMI) registry.lookup(Config.config.getService());
		serveurRMI.newImage(title, size);;
		serveurRMI.insertByte(title, begin, picture.getPart());
		
      // Get the images
      

    } catch (Exception e) {
      response.setStatus(500);
      e.printStackTrace();
    }
  }

}
