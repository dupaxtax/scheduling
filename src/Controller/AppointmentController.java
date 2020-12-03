package Controller;

import Data.Appointment;
import Utils.MiscMethods;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable
{
    @FXML Button displayWeek;

    public void displayWeek() throws IOException
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusWeek = LocalDateTime.now().plusDays(7);
        FilteredList<Appointment> filteredList = new FilteredList<Appointment>(FXCollections.observableArrayList(Appointment.getAllAppointments()));
        /*Using a lambda expression to filter what is displayed in the table is more efficient than creating an additional method or class
        to do the same.*/
        filteredList.setPredicate(row -> {
            LocalDateTime rowDate = LocalDateTime.of(Integer.parseInt(row.getStart().substring(0, 4)), Integer.parseInt(row.getStart().substring(5, 7)),
                    Integer.parseInt(row.getStart().substring(8, 10)), Integer.parseInt(row.getStart().substring(11, 13)), Integer.parseInt(row.getStart().substring(14, 16)));
            return rowDate.isAfter(now) && rowDate.isBefore(nowPlusWeek);
        });

        appointmentTable.setItems(filteredList);
    }

    @FXML Button displayMonth;

    public void displayMonth() throws IOException
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusMonth = LocalDateTime.now().plusMonths(1);
        FilteredList<Appointment> filteredList = new FilteredList<Appointment>(FXCollections.observableArrayList(Appointment.getAllAppointments()));
        /*Using a lambda expression to filter what is displayed in the table is more efficient than creating an additional method or class
        to do the same.*/
        filteredList.setPredicate(row -> {
            LocalDateTime rowDate = LocalDateTime.of(Integer.parseInt(row.getStart().substring(0, 4)), Integer.parseInt(row.getStart().substring(5, 7)),
                    Integer.parseInt(row.getStart().substring(8, 10)), Integer.parseInt(row.getStart().substring(11, 13)), Integer.parseInt(row.getStart().substring(14, 16)));
            return rowDate.isAfter(now) && rowDate.isBefore(nowPlusMonth);
        });

        appointmentTable.setItems(filteredList);
    }

    @FXML Button displayAll;

    public void displayAll()
    {
        displayAll.setOnAction(e ->
        {
            try
            {
                MiscMethods.setupStage(displayAll, "/Controller/appointments.fxml");
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        });

        appointmentTable.setItems(FXCollections.observableArrayList(Appointment.getAllAppointments()));
    }

    @FXML TableView appointmentTable;
    @FXML
    TableColumn<Appointment, String> title;
    @FXML
    TableColumn<Appointment, String> locat;
    @FXML
    TableColumn<Appointment, String> start;
    @FXML
    TableColumn<Appointment, String> end;
    @FXML Button add;

    public void addCustomer() throws IOException
    {
        MiscMethods.setupStage(add, "/Controller/add-appointment.fxml");
    }

    @FXML Button update;

    public void updateAppointment() throws IOException
    {
        Appointment appointment;
        Object obj = appointmentTable.getSelectionModel().getSelectedItem();
        if (obj instanceof Appointment) {
            appointment = (Appointment) obj;

            Stage currStage = (Stage) update.getScene().getWindow();
            currStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().<UpdateAppointmentController>getResource("update-appointment.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));

            UpdateAppointmentController controller = loader.getController();
            controller.initAppointmentData(appointment);

            stage.show();
        }
    }

    @FXML Button delete;

    public void deleteAppointment() throws IOException
    {
        if(appointmentTable.getSelectionModel().getSelectedItem() instanceof Appointment) {
            Appointment appt = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

            appt.deleteAppointment(appt.getAppointmentId());

            MiscMethods.setupStage(delete, "/Controller/appointments.fxml");
        }
    }

    @FXML Button cancel;

    @FXML
    public void cancel() throws IOException
    {
        MiscMethods.setupStage(cancel, "/Controller/main-screen.fxml");
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")) {
            displayWeek.setText(rb.getString("week"));
            displayMonth.setText(rb.getString("month"));
            displayAll.setText(rb.getString("all"));
            title.setText(rb.getString("title"));
            locat.setText(rb.getString("location"));
            start.setText(rb.getString("start"));
            end.setText(rb.getString("end"));
            add.setText(rb.getString("add"));
            update.setText(rb.getString("update"));
            delete.setText(rb.getString("delete"));
            cancel.setText(rb.getString("cancel"));
        }

        List appointmentList = Appointment.getAllAppointments();

        title.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        locat.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        start.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        end.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));

        appointmentTable.getItems().setAll(appointmentList);
    }
}
