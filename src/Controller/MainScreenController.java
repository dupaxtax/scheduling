package Controller;

import Data.User;
import Utils.MiscMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable
{
    @FXML
    Button customers;

    @FXML
    public void viewCustomers() throws IOException
    {
        MiscMethods.setupStage(customers, "/Controller/customer.fxml");
    }

    @FXML Button appointments;

    public void viewAppointments() throws IOException
    {
        MiscMethods.setupStage(appointments, "/Controller/appointments.fxml");
    }

    @FXML Button report;

    public void viewReports() throws IOException
    {
        MiscMethods.setupStage(report, "/Controller/reports.fxml");
    }

    @FXML Button logOut;
    public void logUserOut() throws IOException
    {
        MiscMethods.setupStage(logOut, "/Controller/login-screen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        String upcomingAppointment = "You have an appointment within the next 15 minutes";
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es")) {
            customers.setText(rb.getString("customer"));
            appointments.setText(rb.getString("appointment"));
            report.setText(rb.getString("report"));
            logOut.setText(rb.getString("logout"));
            upcomingAppointment = rb.getString("upcomingAppointment");
        }
        User user = User.getUser();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        try
        {
            if(user.checkForUpcomingAppointments() == 1) {
                alert.setContentText(upcomingAppointment);
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
