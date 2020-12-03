package Controller;

import Data.Appointment;
import Utils.MiscMethods;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportController implements Initializable
{
    @FXML Button apptsByMonth;
    @FXML ComboBox<Integer> month;
    @FXML ComboBox<Integer> year;

    public void getApptsByMonth() {
        int selectedMonth = month.getValue();
        int selectedYear = year.getValue();

        int result = Appointment.appointmentsByMonth(selectedMonth, selectedYear);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(Integer.toString(result));
        alert.showAndWait();
    }

    @FXML Button consultSched;

    public void consultSchedule() throws IOException
    {
        MiscMethods.setupStage(consultSched, "/Controller/consultant-report.fxml");
    }

    @FXML Button today;

    public void appointmentsToday() throws IOException
    {
        MiscMethods.setupStage(today, "/Controller/appointments-today.fxml");
    }

    @FXML Button cancel;

    public void cancel() throws IOException
    {
        MiscMethods.setupStage(cancel, "/Controller/main-screen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        List<Integer> months = new ArrayList<>(12);
        for(int i = 1; i <= 12; ++i) {
            months.add(i);
        }
        month.setItems(FXCollections.observableArrayList(months));
        List<Integer> years = new ArrayList<>(10);
        for(int i = 1; i <= 10; ++i) {
            if(i < 5) {
                years.add((LocalDate.now().getYear()) - i);
            } else if(i <= 10) {
                years.add((LocalDate.now().getYear()) + i - 5);
            }
        }
        year.setItems(FXCollections.observableArrayList(years));

        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")) {
            apptsByMonth.setText(rb.getString("appointmentsByMonth"));
            consultSched.setText(rb.getString("consultantSchedule"));
            today.setText(rb.getString("todaySchedule"));
            cancel.setText(rb.getString("cancel"));
        }
    }
}
