package Data;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Address
{
    private static int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;

    public Address(){}

    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, String createDate, String createdBy,
                    String lastUpdate, String lastUpdateBy) {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAddressId() {return addressId;}

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE address SET address = ? WHERE addressId = ?");
            ps.setString(1, address);
            ps.setInt(2, getAddressId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public String getAddress2() { return address2; }

    public void setAddress2(String address2) {
        this.address2 = address2;

        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE address SET address2 = ? WHERE addressId = ?");
            ps.setString(1, address2);
            ps.setInt(2, getAddressId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public int getCityId() { return cityId; }

    public String getPostalCode() { return postalCode;}

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;

        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE address SET postalCode = ? WHERE addressId = ?");
            ps.setString(1, postalCode);
            ps.setInt(2, getAddressId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public String getPhone() { return phone;}

    public void setPhone(String phone) {
        this.phone = phone;

        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE address SET phone = ? WHERE addressId = ?");
            ps.setString(1, phone);
            ps.setInt(2, getAddressId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public String getCreateDate() {return createDate;}

    public String getCreatedBy() {return createdBy;}

    public String getLastUpdate() {return lastUpdate;}

    public String getLastUpdateBy() {return lastUpdateBy;}

    public Address getAddress(int id) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM address WHERE addressId = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Address address = new Address(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10));

                return address;
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int checkDuplicateAddress(String addressCheck) {
        Connection conn = DBConnection.getConnection();

        int addressId = -1;

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT addressId FROM address WHERE address = ?");
            ps.setString(1, addressCheck);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                addressId = rs.getInt(1);
            } else {
                addressId = 0;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return addressId;
    }

    public static void addNewAddress(String address, String address2, String city, String postal, String phone, String country) {
        Connection conn = DBConnection.getConnection();
        User currUser = User.getUser();

        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (?, ?, ?, ?, ?, CURRENT_DATE, ?, ?)");
            ps.setString(1, address);
            ps.setString(2, address2);
            ps.setInt(3, City.getCityId(city));
            ps.setString(4, postal);
            ps.setString(5, phone);
            ps.setString(6, currUser.getUsername());
            ps.setString(7, currUser.getUsername());

            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
