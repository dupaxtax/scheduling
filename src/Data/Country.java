package Data;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Country
{
    private static int countryId;
    private static String countryName;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;

    public Country(){}

    public Country(int countryId, String countryName, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCountryId() {return countryId;}

    public String getCountryName() {return countryName;}

    public void setCountryName(String countryName) {
        this.countryName = countryName;

        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE country SET country = ? WHERE countryId = ?");
            ps.setString(1, countryName);
            ps.setInt(2, getCountryId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public String getCreateDate() {return createDate;}

    public String getCreatedBy() {return  createdBy;}

    public String getLastUpdate() {return lastUpdate;}

    public String getLastUpdateBy() {return lastUpdateBy;}

    public Country getCountryObj(int countryId) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM country WHERE countryId = ?");
            ps.setInt(1, countryId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Country country = new Country(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6));

                return country;
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int getCountryId(String countryName)
    {
        Connection conn = DBConnection.getConnection();

        try
        {

            PreparedStatement ps = conn.prepareStatement("SELECT countryId FROM country WHERE country = ?");
            ps.setString(1, countryName);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                countryId = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return countryId;
    }

    public static int checkDuplicateCountry(String country) {
        Connection conn = DBConnection.getConnection();
        int countryId = -1;

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT countryId FROM country WHERE country = ?");
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                countryId = rs.getInt(1);
            } else {
                countryId = 0;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return countryId;
    }

    public static void addNewCountry(String countryName) {
        Connection conn = DBConnection.getConnection();
        User currUser = User.getUser();

        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO country (country, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (?, CURRENT_DATE, ?, ?)");
            ps.setString(1, countryName);
            ps.setString(2, currUser.getUsername());
            ps.setString(3, currUser.getUsername());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
