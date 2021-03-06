package editor;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DataAccessObjects.Groupe;
import DataAccessObjects.GroupeListDAO;
import sessionManagement.SessionVerifier;
import sessionManagement.User;

public class VisualisationGroupeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (SessionVerifier.getInstance().verify(request, response)) {
			return;
		}
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/admin/visualisationGroupe.jsp");
		
		// OBTENTION DES GROUPES
			
		GroupeListDAO groupeListDAO = new GroupeListDAO();
		List<Groupe> listGroupes = groupeListDAO.getGroupeList();
		
		try {
			request.setAttribute("listGroupe", listGroupes);
			rd.forward(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (SessionVerifier.getInstance().verify(request, response)) {
			return;
		}
		
		User user = (User) request.getSession().getAttribute("user");

		String creeGroupe = request.getParameter("creeGroupe");
		String supprGroupe =  request.getParameter("supprGroupe");
		String searchText = request.getParameter("searchText");
		request.setAttribute("searchText", searchText);
		
		GroupeListDAO gld = new GroupeListDAO();

		
		if(creeGroupe != null) {
			System.out.println(user.getLogin());
			gld.creerGroupe(searchText, user.getLogin());
			doGet(request,response);
		}
		else if(supprGroupe != null) {
			String editor = user.getLogin();
			if(user.getRights().contentEquals("admin")) {
				editor = "";
			}
			gld.supprGroupe(searchText, editor);
			doGet(request,response);
		}
	}
}
