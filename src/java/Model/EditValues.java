/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Trish Moreno
 */
public class EditValues {
    Connection con;
    
    public EditValues()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/PersonDatabase", "app", "app");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditValues.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EditValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getRow(int productNo)
    {
        ResultSet rs = null;
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM INVENTORY WHERE PRODUCT_NO = ?");
            ps.setInt(1, productNo);
            rs = ps.executeQuery();
            ps.close();
           
        } catch (SQLException ex) {
            Logger.getLogger(EditValues.class.getName()).log(Level.SEVERE, null, ex);
        }
         return rs;
    }
}
