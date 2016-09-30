package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InventoryServlet extends HttpServlet {
    
    Connection conn;
    String adminuser,adminpassword;
    public void init(ServletConfig config) throws ServletException {
            super.init(config);
            try {	
                    Class.forName(config.getInitParameter("jdbcClassName"));
                    //System.out.println("jdbcClassName: " + config.getInitParameter("jdbcClassName"));
                    String username = config.getInitParameter("dbUserName");
                    String password = config.getInitParameter("dbPassword");
                    adminuser=config.getInitParameter("AdminUser");
                    adminpassword=config.getInitParameter("AdminPass");
                    StringBuffer url = new StringBuffer(config.getInitParameter("jdbcDriverURL"))
                            .append("://")
                            .append(config.getInitParameter("dbHostName"))
                            .append(":")
                            .append(config.getInitParameter("dbPort"))
                            .append("/")
                            .append(config.getInitParameter("databaseName"));
                    conn = 
                      DriverManager.getConnection(url.toString(),username,password);
            } catch (SQLException sqle){
                    System.out.println("SQLException error occured - " 
                            + sqle.getMessage());
            } catch (ClassNotFoundException nfe){
                    System.out.println("ClassNotFoundException error occured - " 
                    + nfe.getMessage());
            }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String AdminUsername=request.getParameter("Username");
        String AdminPass=request.getParameter("Password");
        request.setAttribute("AdminUsername",AdminUsername);
        request.setAttribute("AdminPassword",AdminPass);
        RequestDispatcher rd;
        try{
        if (conn != null && AdminUsername.trim().equals(adminuser)&& AdminPass.trim().equals(adminpassword) ) {
            try{
                Statement stmt = conn.createStatement();
                ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY ORDER BY PRODUCT_NO");
                request.setAttribute("results", records);
                request.getRequestDispatcher("display.jsp").forward(request,response);
            }catch(Exception g)
            {
                rd = request.getRequestDispatcher("/error.jsp?errCode=2");
                rd.forward(request, response);
            }
        }else if(conn == null)
        {
         rd = request.getRequestDispatcher("/error.jsp?errCode=1");
                rd.forward(request, response);   
            
        }else if(AdminUsername.trim().length() == 0 || AdminPass.trim().length() == 0){
            rd = request.getRequestDispatcher("/error.jsp?errCode=2");
                rd.forward(request, response);
        }else if(!(AdminUsername.trim().equals(adminuser)) || !(AdminPass.trim().equals(adminpassword))){
            rd = request.getRequestDispatcher("/error.jsp?errCode=3");
                rd.forward(request, response);
        } 
        
        
        }catch (NumberFormatException e){
                response.sendRedirect("error.jsp");
        } 

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
