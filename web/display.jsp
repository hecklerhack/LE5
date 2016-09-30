<%@page import="java.sql.ResultSet"%>
<%@page import="Model.EditValues"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <!-- Optional theme -->
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="css/style.css">
        <title>Inventory System</title>
        <header id="header">
        <p class="logo" align="center"><strong>Jomar's</strong> Inventory System</p>
        </header>
        <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script type="text/javascript">
            function add(){
               var aval = prompt("Enter Product Specs, seperate them with a comma: ", "");
               if(aval == null)
               {
                 aval = "cancel";  
                 document.form1.hiddenValue.value = aval;   
                 document.form1.submit();  
               }else{
               document.form1.hiddenValue.value = aval;   
               document.form1.submit();  
           }}
           
           
           $( function(){
               var dialog, form, productNo = $("#productNo"), productName = $("#productName"), productPrice = $("#productPrice"), productQuantity = $("#productQuantity");
               dialog = $( "#dialogEditForm" ).dialog({
                autoOpen: false,
                height: 300,
                width: 350,
                modal: true,
                buttons: {
                  "Submit": editValue,
                  Cancel: function() {
                    dialog.dialog( "close" );
                  }
                },
                close: function() {
                  form[ 0 ].reset();
                  allFields.removeClass( "ui-state-error" );
                }
              });
              

           function editValue(){
            productNo = $('#productNo').val();
            productName = $('#productName').val();
            productPrice = $('#productPrice').val();
            productQuantity = $('#productQuantity').val();
              var sData = "productNo="+productNo+"&operation=Edit&productName=" + productName + "&productPrice=" + productPrice + "&productQuantity=" + productQuantity;
              $.ajax({
                  type: "POST",
                  url: "CRUDServlet",
                  data: sData,
                  dataType: "json",

                  success: function(data, textStatus, jqXhr)
                  {
                    if(data.success)
                    {
                     // dialog.dialog("close");
                      window.location.reload();
                      window.location.reload();
                    }
                    else
                    {
                      dialog.dialog("close");
                      alert("Edit data not successful. ");
                      return false;
                    }
                  },
                  error: function(jqXHR, textStatus, errorThrown){
                     console.log("Something really bad happened " + textStatus);
                      $("#ajaxResponse").html(jqXHR.responseText);
                    },

                     beforeSend: function(jqXHR, settings){
                    //adding some Dummy data to the request
                    settings.data += "&dummyData=whatever";
                     $('#editButton').attr("disabled", false);
                   },

                   complete: function(jqXHR, textStatus){
                    //enable the button 
                    $('#editButton').attr("disabled", false);
                }
              });

           }

           $( "#editButton" ).button().on( "click", function(){
            var pno = prompt("Enter Product No.:", "");
               if(pno == "" || pno == null)
               {
                  alert("No data entered.");
                  return false;

               }
               else
               {
                  var dataString = "productNo=" + pno;
                  $.ajax({
                    type: "POST",
                    url: "EditValuesServlet",
                    data: dataString,
                    dataType: "json",

                    success: function(data, textStatus, jqXhR){
                      if(data.success)
                      {
                          $('#dialogEditForm').html("");
                          $('#dialogEditForm').append("<form action='CRUDServlet' name='operation' method='POST'>");
                          $('#dialogEditForm').append("<fieldset>");
                          $('#dialogEditForm').append("<label for='productNo'>Product No.: </label>");
                          $('#dialogEditForm').append("<input type='text' name='Product_No' id='productNo' value='" + data.productInfo.Product_No +"' class='text ui-widget-content ui-corner-all'>");
                          $('#dialogEditForm').append("<label for='productName'>Product Name:</label>");
                          $('#dialogEditForm').append("<input type='text' name='Product_Name' id='productName' value='" + data.productInfo.Product_Name + "' class='text ui-widget-content ui-corner-all'>");
                          $('#dialogEditForm').append("<label for='productPrice'>Product Price: </label>");
                          $('#dialogEditForm').append("<input type='text' name='productPrice' id='productPrice' value='"+ data.productInfo.Product_Price+"' class='text ui-widget-content ui-corner-all'>");
                          $('#dialogEditForm').append("<label for='productQuantity'>Product Quantity: </label>");
                          $('#dialogEditForm').append("<input type='text' name='productQuantity' id='productQuantity' value='"+data.productInfo.Product_Quantity+ "' class='text ui-widget-content ui-corner-all'>");
                          $('#dialogEditForm').append("<input type='submit' tabindex='-1' style='position:absolute; top:-1000px'>");
                          $('#dialogEditForm').append("</fieldset>");
                          $('#dialogEditForm').append("</form>");
                          
                          dialog.dialog("open");
                          
                          
                      }
                      else
                      {
                        alert("Product No. does not exist.");
                        return false;
                      }
                    },

                    error: function(jqXHR, textStatus, errorThrown){
                     console.log("Something really bad happened " + textStatus);
                      $("#ajaxResponse").html(jqXHR.responseText);
                    },

                     beforeSend: function(jqXHR, settings){
                    //adding some Dummy data to the request
                    settings.data += "&dummyData=whatever";
                     $('#editButton').attr("disabled", false);
                   },

                   complete: function(jqXHR, textStatus){
                    //enable the button 
                    $('#editButton').attr("disabled", false);
                }
                  });
                  
               }
             });
             
            
        });
       
           
           function deleteValue(){
               var pno = prompt("Enter Product No.:", "");
               document.form1.hiddenValue.value = pno;   
               document.form1.submit();  
           }/*
           function searchValue(){
               var pno = prompt("Enter Product Name:", "");
               document.form.hiddenValue.value = pno;   
               document.form.submit();  
           }*/
            
      </script>
    </head>
    <body style="background-image: url('Images/BackgroundShop.jpg'); background-size: 100%">
        
        <div id="dialogEditForm">

            
            
        </div>
       <div class="container-fluid" >
       <h1 align="center">Inventory System</h1>
       <form class="navbar-form pull-right" method="post" action ="CRUDServlet" name="form">
          <input type="text" class="span2" placeholder="Search..." name="pno">
          <input type="submit" class="btn btn-info btn-lg" value="Search" name="operation">
        </form>
  <table border="1" class="table table-hover" align="center">
            <thead>
    <tr class="danger">
      <th>PRODUCT_NO</th>
      <th>PRODUCT_NAME</th>
      <th>PRODUCT_PRICE</th>
      <th>PRODUCT_QUANTITY</th>
                        
    </tr>
            </thead>
            <tbody>
    <%
                    ResultSet results = (ResultSet)request.getAttribute("results");
      while (results.next()) { %>
                        <tr class="info">
        <td data-title="PRODUCT_NO"><%=results.getString("PRODUCT_NO") %></td>
        <td data-title="PRODUCT_NAME"><%=results.getString("PRODUCT_NAME") %></td>
                                <td data-title="PRODUCT_PRICE"><%=results.getString("PRODUCT_PRICE") %></td>
                                <td data-title="PRODUCT_QUANTITY"><%=results.getString("PRODUCT_QUANTITY") %></td>
      </tr> 
    <%  }
    %>
            </tbody>   
          
  </table>
                <table align="right">
                    <tr>
                        <td colspan="2">   
                            <button id="addButton" class="btn btn-success btn-lg pull-right" onclick="add()">Add</button>

                            <button id="editButton" class="btn btn-success btn-lg pull-right">Edit</button>
                   
                            <button id="deleteButton"  class="btn btn-success btn-lg pull-right"  onclick="deleteValue();" >Delete</button>
                       
                            <button id="refreshButton" class="btn btn-success btn-lg pull-right"  onclick="window.location.reload()">Refresh</button>
                        
                        <input type=hidden name="hiddenValue"/>  
                    </td>
                </tr>
            </table>
            
         
       </div>               
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>   
    </body>
</html>
