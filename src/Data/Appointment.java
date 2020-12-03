package Data;

import Utils.DBConnection;
import Utils.MiscMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment
{
    private int appointmentId;
    private int customerId;
    private int userId;
    private String username;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;

    public Appointment() {}

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location,
                       String contact, String type, String url, String start, String end, String createdBy, String lastUpdateBy)
    {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createdBy = createdBy;
        this.lastUpdateBy = lastUpdateBy;
    }


    public int getAppointmentId()
    {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId)
    {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy)
    {
        this.lastUpdateBy = lastUpdateBy;
    }

    public static List getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();

        Connection conn = DBConnection.getConnection();

        try
        {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM appointment WHERE start > CURRENT_DATE");

            while(rs.next()) {
                Appointment appointment = new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                        rs.getString(13), rs.getString(15));
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        for(Appointment appt : appointmentList) {
            LocalDateTime start = MiscMethods.createLDT(appt.getStart());
            ZonedDateTime startZDT = MiscMethods.convertToCurrentTimeZone(start);
            appt.setStart(startZDT.toLocalDateTime().toString().replace("T", " "));
            LocalDateTime end = MiscMethods.createLDT(appt.getEnd());
            ZonedDateTime endZDT = MiscMethods.convertToCurrentTimeZone(end);
            appt.setEnd(endZDT.toLocalDateTime().toString().replace("T", " "));
        }
        return appointmentList;
    }

    public void addNewAppointment(Appointment appt) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, ?)");
            ps.setInt(1, appt.getCustomerId());
            ps.setInt(2, appt.getUserId());
            ps.setString(3, appt.getTitle());
            ps.setString(4, appt.getDescription());
            ps.setString(5, appt.getLocation());
            ps.setString(6, appt.getContact());
            ps.setString(7, appt.getType());
            ps.setString(8, appt.getUrl());
            ps.setString(9, appt.getStart());
            ps.setString(10, appt.getEnd());
            ps.setString(11, getCreatedBy());
            ps.setString(12, getLastUpdateBy());

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static List<Appointment> getAppointmentsByWeek() {
        Connection conn = DBConnection.getConnection();

        List<Appointment> apptList = new ArrayList<>();

        try
        {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM appointment WHERE start BETWEEN CURRENT_DATE AND CURRENT_DATE + 7");

            while(rs.next()) {
                Appointment appointment = new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                        rs.getString(13), rs.getString(15));

                apptList.add(appointment);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        for(Appointment appt : apptList) {
            LocalDateTime start = MiscMethods.createLDT(appt.getStart());
            ZonedDateTime startZDT = MiscMethods.convertToCurrentTimeZone(start);
            appt.setStart(startZDT.toLocalDateTime().toString().replace("T", " "));
            LocalDateTime end = MiscMethods.createLDT(appt.getEnd());
            ZonedDateTime endZDT = MiscMethods.convertToCurrentTimeZone(end);
            appt.setEnd(endZDT.toLocalDateTime().toString().replace("T", " "));
        }
        return apptList;
    }

    public static List<Appointment> getAppointmentsByMonth() {
        Connection conn = DBConnection.getConnection();

        List<Appointment> appointmentList = new ArrayList<>();

        try
        {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM appointment WHERE start BETWEEN CURRENT_DATE AND CURRENT_DATE + 30");

            while(rs.next())
            {
                Appointment appointment = new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                        rs.getString(13), rs.getString(15));

                appointmentList.add(appointment);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        for(Appointment appt : appointmentList) {
            LocalDateTime start = MiscMethods.createLDT(appt.getStart());
            ZonedDateTime startZDT = MiscMethods.convertToCurrentTimeZone(start);
            appt.setStart(startZDT.toLocalDateTime().toString().replace("T", " "));
            LocalDateTime end = MiscMethods.createLDT(appt.getEnd());
            ZonedDateTime endZDT = MiscMethods.convertToCurrentTimeZone(end);
            appt.setEnd(endZDT.toLocalDateTime().toString().replace("T", " "));
        }
        return appointmentList;
    }

    public void updateAppointment(int apptId, int custId, int userId, String tit, String desc, String locat, String cont,
                                  String type, String url, String start, String end, String lasUpdateUser) {
        Connection conn = DBConnection.getConnection();

        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE appointment\n" +
                    "SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, contact = ?, type = ?, \n" +
                    "url = ?, start = ?, end = ?, lastUpdateBy = ?\n" +
                    "WHERE appointmentId = ?");
            ps.setInt(1, custId);
            ps.setInt(2, userId);
            ps.setString(3, tit);
            ps.setString(4, desc);
            ps.setString(5, locat);
            ps.setString(6, cont);
            ps.setString(7, type);
            ps.setString(8, url);
            ps.setString(9, start);
            ps.setString(10, end);
            ps.setString(11, lasUpdateUser);
            ps.setInt(12, apptId);

            ps.executeUpdate();
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public static int appointmentsByMonth(int month, int year) {
        Connection conn = DBConnection.getConnection();
        int numAppointments = 0;

        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(appointmentId)" +
                    "FROM appointment WHERE MONTH(start) = ? AND YEAR(start) = ?");
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                numAppointments = rs.getInt(1);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return numAppointments;
    }

    public static List getCurrentDayAppointments(){
        Connection conn = DBConnection.getConnection();

        List<Appointment> appointmentList = new ArrayList<>();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE start > CURRENT_DATE AND start < CURRENT_DATE + 1;");

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Appointment appointment = new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                        rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11),
                        rs.getString(13), rs.getString(15));

                appointmentList.add(appointment);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        for(Appointment appt : appointmentList) {
            LocalDateTime start = MiscMethods.createLDT(appt.getStart());
            ZonedDateTime startZDT = MiscMethods.convertToCurrentTimeZone(start);
            appt.setStart(startZDT.toLocalDateTime().toString().replace("T", " "));
            LocalDateTime end = MiscMethods.createLDT(appt.getEnd());
            ZonedDateTime endZDT = MiscMethods.convertToCurrentTimeZone(end);
            appt.setEnd(endZDT.toLocalDateTime().toString().replace("T", " "));
        }

        return appointmentList;
    }

    public boolean checkForOverlappingAppointments()
    {
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE(? BETWEEN start AND end) AND userId = ?;");
            ps.setString(1, getStart());
            ps.setInt(2, getUserId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
            ps.setString(1, getEnd());
            ResultSet rs2 = ps.executeQuery();
            if (rs2.next()) {
                return false;
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return true;
    }
}
