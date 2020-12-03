package Data;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Customer
{
    private int customerId;
    private String customerName;
    private int addressId;
    private int active;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;
    private String customerAddress;
    private String customerPhone;

    public Customer() {}

    public Customer(int id, String name, int addressId, String address, String phone) {
        customerId = id;
        customerName = name;
        this.addressId = addressId;
        customerAddress = address;
        customerPhone = phone;
    }

    public int getCustomerId(){
        return customerId;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String name) {
        customerName = name;

        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE customer SET customerName = ? WHERE customerId = ?");
            ps.setString(1, name);
            ps.setInt(2, getCustomerId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public int getAddressId() {return addressId;}

    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public Customer getCustomer(int id) {
        Connection conn = DBConnection.getConnection();

        Customer cust = new Customer();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE customerId = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                cust = new Customer(rs.getInt(1), rs.getString(2), rs.getInt(3), "address", "phone");
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return cust;
    }

    public static List<Customer> getAllCustomers() {
        Connection conn = DBConnection.getConnection();
        List customerList = new ArrayList();

        try
        {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT customer.customerId, customer.customerName, customer.addressId, address.address, city.city, address.postalCode, country.country, address.phone\n" +
                            "FROM customer\n" +
                            "LEFT JOIN address ON customer.addressId = address.addressId\n" +
                            "LEFT JOIN city ON address.cityId = city.cityId\n" +
                            "LEFT JOIN country ON city.countryId = country.countryId");

            while(rs.next()) {
                Customer cust = new Customer (rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4)
                        + ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7),
                        rs.getString(8));

                customerList.add(cust);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return customerList;
    }

    public static int checkDuplicateCustomer(String customerName) {
        Connection conn = DBConnection.getConnection();

        int customerId = -1;


        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT customerId FROM customer WHERE customerName = ?");
            ps.setString(1, customerName);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                customerId = rs.getInt(1);
            } else {
                customerId = 0;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return customerId;
    }

    public static void addNewCustomer(String customerName, String address, int active) {
        Connection conn = DBConnection.getConnection();
        User currUser = User.getUser();

        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO customer (customerName, addressId, active, createDate," +
                    "createdBy, lastUpdateBy)" +
                    "VALUES (?, (SELECT addressId FROM address WHERE address = ?), 1, CURRENT_DATE, ?, ?)");
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, currUser.getUsername());
            ps.setString(4, currUser.getUsername());

            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> getCustomerInfo(int id) {
        Connection conn = DBConnection.getConnection();

        List<String> custInfo = new ArrayList<>();

        try
        {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT customer.customerId, customer.customerName, address.address, city.city, address.postalCode, country.country, address.phone\n" +
                            "FROM customer\n" +
                            "LEFT JOIN address ON customer.addressId = address.addressId\n" +
                            "LEFT JOIN city ON address.cityId = city.cityId\n" +
                            "LEFT JOIN country ON city.countryId = country.countryId\n" +
                            "WHERE customerId = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                custInfo.add(rs.getString(1));
                custInfo.add(rs.getString(2));
                custInfo.add(rs.getString(3));
                custInfo.add(rs.getString(4));
                custInfo.add(rs.getString(5));
                custInfo.add(rs.getString(6));
                custInfo.add(rs.getString(7));
            }
            }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return custInfo;
    }

    public void deleteCustomer(int id) {
        Connection conn = DBConnection.getConnection();

        try
        {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM customer WHERE customerId = ?");
        ps.setInt(1, id);

        ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return customerName;
    }


}
