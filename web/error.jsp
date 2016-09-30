<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Jomar's Inventory System</title>
    </head>
    <body>
        <%
            int error = 0;
            
            if (request.getParameter("errCode") != null)
                error = Integer.parseInt(request.getParameter("errCode"));
            
            switch(error){
                case 1: %><h1>NO CONNECTION TO DB</h1>
                          <form method="post" action="index.jsp">
                            <input type="submit" value="Go Back">
                            </form><%
                        break;
                case 2: %><h1>NO USERNAME OR PASSWORD</h1>
                <form method="post" action="index.jsp">
                            <input type="submit" value="Go Back">
                            </form><%
                        break;
                case 3: %><h1>INVALID USERNAME OR PASS</h1>
                <form method="post" action="index.jsp">
                            <input type="submit" value="Go Back">
                            </form><%
                        break;
                case 4: %><h1>NOTHING TO ADD</h1><form method="post" action="CRUDServlet">
                            <input type="submit" value="Go Back" name="operation">
                            </form><%
                        break;
                case 5: %><h1>PRODUCT NUMBER ALREADY EXISTS</h1><form method="post" action="CRUDServlet">
                            <input type="submit" value="Go Back" name="operation">
                            </form><%
                        break;
                case 6: %><h1>INVALID INPUT</h1><form method="post" action="CRUDServlet">
                            <input type="submit" value="Go Back" name="operation">
                            </form><%
                        break;
                case 7: %><h1>PRODUCT DOES NOT EXIST</h1><form method="post" action="CRUDServlet">
                            <input type="submit" value="Go Back" name="operation">
                            </form><%
                        break;
                 case 8: %><h1>MISSING PRODUCT NUMBER</h1><form method="post" action="CRUDServlet">
                            <input type="submit" value="Go Back" name="operation">
                            </form><%
                        break;        
                        
            }   

         %>   
        
        
        
  
    </body>
</html>
