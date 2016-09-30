/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.JavaBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Trish Moreno
 */
@WebServlet(name = "EditValuesServlet", urlPatterns = {"/EditValuesServlet"})
public class EditValuesServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Connection con;
    
    @Override
     public void init(ServletConfig config) throws ServletException {
            super.init(config);
            try {	
                    Class.forName(config.getInitParameter("jdbcClassName"));
                    //System.out.println("jdbcClassName: " + config.getInitParameter("jdbcClassName"));
                    String username = config.getInitParameter("dbUserName");
                    String password = config.getInitParameter("dbPassword");
                    
                    StringBuffer url = new StringBuffer(config.getInitParameter("jdbcDriverURL"))
                            .append("://")
                            .append(config.getInitParameter("dbHostName"))
                            .append(":")
                            .append(config.getInitParameter("dbPort"))
                            .append("/")
                            .append(config.getInitParameter("databaseName"));
                    con = 
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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String productNo = request.getParameter("productNo");
        int n = 0;
        try
        {
            n = Integer.parseInt(productNo);
        }
        catch(NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }
        
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        
        Gson gson = new Gson(); 
        JsonObject myObj = new JsonObject();
        
        JavaBean productInfo = getInfo(n);
        JsonElement productObj = gson.toJsonTree(productInfo);
        
        if(productInfo.getProduct_No() <= 0)
        {
            myObj.addProperty("success", false);
        }
        else
        {
            myObj.addProperty("success", true);
        }
        myObj.add("productInfo", productObj);
   //     con.close();
        out.println(myObj.toString());
        out.close();
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EditValuesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EditValuesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private JavaBean getInfo(int productNo) {
        JavaBean bean = new JavaBean();
        
        PreparedStatement ps = null;
        String query = null;
        int pno = productNo;
        System.out.println(pno);
        try
        {
            if(con != null)
            {
               query = "SELECT * FROM INVENTORY WHERE PRODUCT_NO=?";
               ps = con.prepareStatement(query);
               ps.setInt(1, pno);
               ResultSet rs = ps.executeQuery();
              
               while(rs.next())
               {
                   bean.setProduct_No(rs.getInt("PRODUCT_NO"));
                   bean.setProduct_Name(rs.getString("PRODUCT_NAME"));
                   bean.setProduct_Price(rs.getDouble("PRODUCT_PRICE"));
                   bean.setProduct_Quantity(rs.getInt("PRODUCT_QUANTITY"));
                   
               }
               
               rs.close();
               ps.close();
             //  con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(EditValuesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bean;
    }

}
