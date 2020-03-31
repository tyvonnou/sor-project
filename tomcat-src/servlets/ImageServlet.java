package servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


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
      throws ServletException, IOException {}

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("doPost image");

    try {
      // Wrong form type
      if (!ServletFileUpload.isMultipartContent(request)) {
        response.setStatus(422);
        return;
      }

      List<FileItem> multipartItems =
          new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

      // Get Titre img
      String titleimg = null;
      for (FileItem multipartItem : multipartItems) {
        if (multipartItem.isFormField()) {
        	titleimg = multipartItem.getString();
          break;
        }
      }

      // TODO: LINK TO JAVA RMI
      
      // Get the images
      for (FileItem multipartItem : multipartItems) {
        if (!multipartItem.isFormField()) {

          // TODO: ADD IMAGE TO RMI
       

          // TODO: Query execution
        }
       
      }

    } catch (Exception e) {
      response.setStatus(500);
      e.printStackTrace();
    }
  }

}
