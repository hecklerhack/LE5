package Model;

public class JavaBean 
{
    public int Product_No;
    public String Product_Name;
    public double Product_Price;
    public int Product_Quantity;

    public int getProduct_No() {
        return Product_No;
    }

    public void setProduct_No(int Product_No) {
        this.Product_No = Product_No;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String Product_Name) {
        this.Product_Name = Product_Name;
    }

    public double getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(double Product_Price) {
        this.Product_Price = Product_Price;
    }

    public int getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(int Product_Quantity) {
        this.Product_Quantity = Product_Quantity;
    }
    
    //String add = "INSERT INTO INVENTORY VALUES (" + .getProduct_No() + ", '" + JavaBean.getProduct_Name() + "', " + JavaBean.getProduct_Price() + ", " + JavaBean.getProduct_Quantity() + ")";
  
}
