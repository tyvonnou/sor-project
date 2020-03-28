package servlets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Config;
import rmi.ServeurRMI;

/**
 * Servlet implementation class Test
 */
@WebServlet("/meth")
public class Meth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Meth.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Meth() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Registry registry = LocateRegistry.getRegistry(Config.config.getPort());
			ServeurRMI serveurRMI;
			serveurRMI = (ServeurRMI) registry.lookup(Config.config.getService());
			String res = serveurRMI.meth();
			response.getWriter().append(res);
		} catch (RemoteException | NotBoundException e) {
			logger.warning(e.getMessage());
		}
	}

}
