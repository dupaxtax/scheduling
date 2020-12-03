package Data;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class City
{
    private static int cityId;
    private String city;
    private int countryId;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;

    public City(){}

    public City(int cityId, String city, int countryId, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCityId() {return cityId;}

    public String getCity() {return city;}

    public void setCity(String city) {
        this.city = city;

        try
        {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE city SET city = ? WHERE cityId = ?");
            ps.setString(1, city);
            ps.setInt(2, getCityId());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public int getCountryId() {return countryId;}

    public String getCreateDate() {return createDate;}

    public String getCreatedBy() {return createdBy;}

    public String getLastUpdate() {return lastUpdate;}

    public String getLastUpdateBy() {return lastUpdateBy;}

    public City getCityObj(int id) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM city WHERE cityId = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                City city = new City(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7));

                return city;
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    public static int getCityId(String cityName) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT cityId FROM city WHERE city = ?");
            ps.setString(1, cityName);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                cityId = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return cityId;
    }

    public static int checkDuplicateCountry(String cityName) {
        Connection conn = DBConnection.getConnection();
        int cityId = -1;

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT cityId FROM city WHERE city = ?");
            ps.setString(1, cityName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                cityId = rs.getInt(1);
            } else {
                cityId = 0;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return cityId;
    }

    public static void addNewCity(String cityName, String countryName)
    {
        Connection conn = DBConnection.getConnection();
        User currUser = User.getUser();

        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (?, ?, CURRENT_DATE, ?, ?)");
            ps.setString(1, cityName);
            ps.setInt(2, Country.getCountryId(countryName));
            ps.setString(3, currUser.getUsername());
            ps.setString(4, currUser.getUsername());
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
