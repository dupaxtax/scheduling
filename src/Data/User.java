package Data;

import Utils.DBConnection;
import Utils.MiscMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class User
{
    private int userId;
    private String username;
    private String password;
    private static User currentUser;

    public User(){}

    public User(String name){
        username = name;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword(String username)
    {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT password FROM user WHERE username = ?");
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();
            while(results.next()) {
                String password = results.getString("password");
                this.password = password;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return password;
    }

    public int getUserId(String username) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT userId FROM user WHERE username = ?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return -1;
    }

    public static void createUserSession(String user){
        currentUser = new User(user);
    }

    public static User getUser(){
        return currentUser;
    }

    public static List getAllUsers() {
        Connection conn = DBConnection.getConnection();
        List<String> userList = new ArrayList<>();

        try
        {
            ResultSet rs = conn.createStatement().executeQuery("SELECT userName FROM user");

            while(rs.next()) {
                userList.add(rs.getString(1));
            }
            return userList;
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return null;
    }

    public User getUserNameById(int id) {
        Connection connection = DBConnection.getConnection();

        User user = new User();

        try
        {
            PreparedStatement ps = connection.prepareStatement("SELECT userName FROM user WHERE userId = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                user = new User(rs.getString(1));
            }

        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return user;
    }

    public int checkForUpcomingAppointments() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        int upcomingAppointment = -1;

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE userId = ?");
        ps.setInt(1, getUserId(getUsername()));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            LocalDateTime apptLDT = MiscMethods.createLDT(rs.getString(10));
            ZoneId zID = ZoneId.of("UTC");
            LocalDateTime currLDT = LocalDateTime.now(zID);
            if(apptLDT.isAfter(currLDT.minusMinutes(15)))
            {
                upcomingAppointment = 1;
            } else {
                upcomingAppointment = 0;
            }
        }
        return upcomingAppointment;
    }

    @Override
    public String toString() {return username;}
}
