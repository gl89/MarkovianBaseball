import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EchoServlet extends HttpServlet {
 
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
      // Set the response message's MIME type
      response.setContentType("text/html; charset=UTF-8");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
	  
	  
	  Connection conn = null;
      Statement stmt = null;
      // Write the response message, in an HTML page
      try {
		
		//what ever the person posts
		//String year = request.getParameter("year");
        String team = request.getParameter("team");
		
		// Step 1: Allocate a database Connection object
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/baseballdb?useSSL=false", "myuser", "xxxx"); // <== Check!
            // database-URL(hostname, port, default database), username, password
 
         // Step 2: Allocate a Statement object within the Connection
         stmt = conn.createStatement();
		  
         // Step 3: Execute a SQL SELECT query database = baseballdb table = years
         String sqlStr = "SELECT * FROM stats WHERE name ='" + team + "'"; //stats = year later
			  
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        out.println("<title>Echo Servlet</title></head>");
        out.println("<body><h2>You have enter</h2>");
		
		ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
        
		// Step 4: Process the query result set
		
		/*Debugging shows that query is getting proper rows
        while(rset.next()) {
            // Print a paragraph <p>...</p> for each record
            out.println("<p>" + rset.getInt("freepass")
                 + ", " + rset.getInt("single")
				 + ", " + rset.getInt("doubles")
				 + ", " + rset.getInt("triple")
				 + ", " + rset.getInt("homerun")
				 + ", " + rset.getInt("outs")
                 + ", " + rset.getInt("runs") + "</p>");
         }
		 */
		 
		 
         // Hyperlink "BACK" to input page
         out.println("<a href='form.html'>BACK</a>");
         out.println("</body></html>");
		 
    }catch (SQLException ex) {
        ex.printStackTrace();
	}finally {
         out.close();  // Always close the output writer
		 try {
           // Step 5: Close the resources
           if (stmt != null) stmt.close();
           if (conn != null) conn.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }
   }
 
   // Redirect POST request to GET request.
   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
      doGet(request, response);
   }
 
   // Filter the string for special HTML characters to prevent
   // command injection attack
   private static String htmlFilter(String message) {
      if (message == null) return null;
      int len = message.length();
      StringBuffer result = new StringBuffer(len + 20);
      char aChar;
 
      for (int i = 0; i < len; ++i) {
         aChar = message.charAt(i);
         switch (aChar) {
             case '<': result.append("&lt;"); break;
             case '>': result.append("&gt;"); break;
             case '&': result.append("&amp;"); break;
             case '"': result.append("&quot;"); break;
             default: result.append(aChar);
         }
      }
      return (result.toString());
   }
}