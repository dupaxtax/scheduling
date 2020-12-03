package Controller;

import Data.Appointment;
import Data.User;
import Utils.MiscMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentsTodayController implements Initializable
{
    @FXML TableView<Appointment> appointmentTable;
    @FXML TableColumn<Appointment, String> consultant;
    @FXML TableColumn<Appointment, String> title;
    @FXML TableColumn<Appointment, String> start;
    @FXML TableColumn<Appointment, String> end;

    @FXML Button back;

    public void back() throws IOException
    {
        MiscMethods.setupStage(back, "/Controller/reports.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        User user = new User();
        List<Appointment> apptList = Appointment.getCurrentDayAppointments();
        for(Appointment appt: apptList) {
            appt.setUsername(user.getUserNameById(appt.getUserId()).toString());
        }

        consultant.setCellValueFactory(new PropertyValueFactory<Appointment, String>("username"));
        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));

        appointmentTable.getItems().setAll(apptList);

        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")){
            consultant.setText(rb.getString("consultant"));
            title.setText(rb.getString("title"));
            start.setText(rb.getString("start"));
            end.setText(rb.getString("end"));
            back.setText(rb.getString("back"));
        }
    }
}
