package com.expressflow.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Attribute;
import org.jdom.JDOMException;

import com.expressflow.Constants;
import com.expressflow.jdo.MobileHumanTask;
import com.expressflow.jdo.Process;
import com.expressflow.datastore.PMF;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ManageProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 2849368246109483697L;

	private static final Logger log = Logger
			.getLogger(ManageProcessServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/javascript");

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			
			// Query all deployed processes
			Query query = pm.newQuery(Process.class);
			query.setFilter("creator == creatorParam && state == 'deployed'");
			query.declareParameters("String creatorParam");
			try {
				PrintWriter out = new PrintWriter(response.getWriter());

				// Ext.regModel
				out.println(JavaScriptFormatter.ExtSetup("Process"));

				out.println(JavaScriptFormatter.ExtModelField("name",
						"url"));

				out.println("]});");

				out.println(JavaScriptFormatter.ExtGroupingBase("Process",
						"name", "url"));

				List<Process> results = (List<Process>) query
						.execute(user.getEmail());
				if (!results.isEmpty()) {
					for (Process p : results) {
						
						// Query all matching Mobile Forms
						Query mQ = pm.newQuery(MobileHumanTask.class);
						mQ.setFilter("method == methodParam");
						mQ.declareParameters("String methodParam");
						
						String host = Constants.HOST;
						List<MobileHumanTask> mResults = (List<MobileHumanTask>)mQ.execute(p.getId());
						if(mResults.size() > 0){
							MobileHumanTask m = mResults.get(0);
							out.println(JavaScriptFormatter.ExtModelField("name", p.getName(), "url", host + "/mobilehumantask/" + m.getId()));
							out.println(",");
						}
					}
				} else {
					// ... no results ...
				}
				
				// Ext handling close
				out.println("]})};");
				
				// Ext loading of the Grouping Base ...
				out.println(JavaScriptFormatter.ExtLoadGroupingBase());
				
				// ... and cleaning everything up
				out.println("}});");
				
			} catch (Exception jde) {
				PrintWriter out = new PrintWriter(response.getWriter());
				out.println("<html>");
				out.println("<head>");
				out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
				out.println("<title>expressFlow - Mobile Human Task - Exception</title>");
				out.println("</head>");
				out.println("<body>");
				out.println(jde.getMessage());
				out.println("</body>");
				out.println("</html>");
			} finally {
				query.closeAll();
			}
		}
	}
}
