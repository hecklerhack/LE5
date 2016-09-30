package Controller;

import Model.JavaBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CRUDServlet extends HttpServlet {
    
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
        String operation = request.getParameter("operation");
        RequestDispatcher rd;
         
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        PrintWriter out = response.getWriter();
        try{
        if (conn != null) {
            
            Statement stmt = conn.createStatement();
            
            
            if (operation.equalsIgnoreCase("Add")){
                if(request.getParameter("hiddenValue").trim().length() == 0 ){
                    rd = request.getRequestDispatcher("/error.jsp?errCode=4");
                    rd.forward(request, response);
                }
                
            String z = request.getParameter("hiddenValue").trim();
            
            if(z.equals("cancel")){
                ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY ORDER BY PRODUCT_NO");
            request.setAttribute("results", records);
            request.getRequestDispatcher("display.jsp").forward(request,response);
            }else{
            
            String[] ar = new String[10];
            ar = z.split(",");
            
            String pno = ar[0];
            String pna = ar[1];
            String ppr = ar[2];
            String pqu = ar[3];
            
            ResultSet rs = stmt.executeQuery("SELECT PRODUCT_NO FROM INVENTORY WHERE PRODUCT_NO =" +pno);
            if(rs.next() == true)
            {
                rd = request.getRequestDispatcher("/error.jsp?errCode=5");
                    rd.forward(request, response);
            }else{
            
                 
            stmt.executeUpdate("INSERT INTO INVENTORY VALUES ("+pno+",'"+pna+"',"+ppr+","+pqu+")");

            ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY ORDER BY PRODUCT_NO");
            request.setAttribute("results", records);
            request.getRequestDispatcher("display.jsp").forward(request,response);   
            }}
            }else if(operation.equalsIgnoreCase("Edit")) {
            //String b = request.getParameter("hiddenValue").trim();
            
                try{
                String a = request.getParameter("productNo");
                String pna = request.getParameter("productName");
                String ppr = request.getParameter("productPrice");
                String pqu = request.getParameter("productQuantity");
                
                Gson gson = new Gson(); 
                JsonObject myObj = new JsonObject();
                
                
                int s = stmt.executeUpdate("UPDATE INVENTORY SET PRODUCT_NAME = '"+pna+"', PRODUCT_PRICE = "+ppr+", PRODUCT_QUANTITY = "+pqu+" WHERE PRODUCT_NO = "+a);    
                System.out.println(pna + " "+ppr + " " +pqu);
                JavaBean j = new JavaBean();
                j.setProduct_No(Integer.parseInt(a));
                j.setProduct_Name(pna);
                j.setProduct_Price(Double.parseDouble(ppr));
                j.setProduct_Quantity(Integer.parseInt(pqu));
                
                JsonElement productObj = gson.toJsonTree(j);
                if(s > 0)
                {
                    myObj.addProperty("success", true);
                }
                else
                {
                    myObj.addProperty("success", false);
                }
                
                myObj.add("productInfo", productObj);
                out.println(myObj.toString());
                
                
                
                ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY ORDER BY PRODUCT_NO");
                request.setAttribute("results", records);
                //request.getRequestDispatcher("display.jsp").forward(request,response);
                }catch(Exception e)
                {
                    e.printStackTrace();
                    rd = request.getRequestDispatcher("/error.jsp?errCode=6");
                    rd.forward(request, response);
                }
            
            }else if(operation.equalsIgnoreCase("Delete")) {
            String z = request.getParameter("hiddenValue");    
              
            
            ResultSet rs = stmt.executeQuery("SELECT PRODUCT_NO FROM INVENTORY WHERE PRODUCT_NO =" +z);
            if(rs.next() == false)
            {
                rd = request.getRequestDispatcher("/error.jsp?errCode=7");
                    rd.forward(request, response);
            }else{
            stmt.executeUpdate("DELETE FROM INVENTORY WHERE PRODUCT_NO ="+z);    
                
            ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY ORDER BY PRODUCT_NO");
            request.setAttribute("results", records);
            request.getRequestDispatcher("display.jsp").forward(request,response);
            }
            }else if(operation.equalsIgnoreCase("Search")) {
            String z = request.getParameter("pno");    
            
            ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY WHERE PRODUCT_NAME = '"+z+"'");
            request.setAttribute("results", records);
            request.getRequestDispatcher("display.jsp").forward(request,response);
            }else if(operation.equalsIgnoreCase("Refresh"))
            {
                ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY");
                request.setAttribute("results", records);
                request.getRequestDispatcher("display.jsp").forward(request,response);
            }else if(operation.equalsIgnoreCase("Go Back"))
            {
                ResultSet records = stmt.executeQuery("SELECT * FROM INVENTORY");
                request.setAttribute("results", records);
                request.getRequestDispatcher("display.jsp").forward(request,response);
            }
            stmt.close();

        }else {
            response.sendRedirect("error.jsp");
        } 
       
        } catch (SQLException sqle){
                if(operation.equalsIgnoreCase("Add")){
                rd = request.getRequestDispatcher("/error.jsp?errCode=6");
                    rd.forward(request, response);
                }else if(operation.equalsIgnoreCase("Edit")){
                rd = request.getRequestDispatcher("/error.jsp?errCode=6");
                sqle.printStackTrace();
                    rd.forward(request, response);
                }else if(operation.equalsIgnoreCase("Delete")){
                rd = request.getRequestDispatcher("/error.jsp?errCode=6");
                    rd.forward(request, response);
                }
                if(operation.equals("Search"))
                {
                    System.out.println("Error on Search");
                }
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
