package Controller;

import Data.Appointment;
import Data.Customer;
import Data.User;
import Utils.MiscMethods;
import Utils.OutsideBusinessHoursException;
import Utils.OverlappingAppointmentException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable
{
    @FXML Text cText;
    @FXML Text userText;
    @FXML Text titleText;
    @FXML Text descripText;
    @FXML Text locatText;
    @FXML Text contactText;
    @FXML Text typeText;
    @FXML Text startText;
    @FXML Text endText;
    @FXML ComboBox<Customer> customer;
    @FXML ComboBox<User> user;
    @FXML TextField title;
    @FXML TextField description;
    @FXML TextField locat;
    @FXML TextField contact;
    @FXML TextField type;
    @FXML TextField url;
    @FXML DatePicker startDate;
    @FXML
    ComboBox<String> startHour;
    @FXML
    ComboBox<String> startMinute;
    @FXML DatePicker endDate;
    @FXML
    ComboBox<String> endHour;
    @FXML
    ComboBox<String> endMinute;

    private int apptId;
    public String missingInfo = "Please input values for all fields";
    public String afterBusinessHours = "Selected time is after business hours";
    public String overlappingAppointment = "The selected time overlaps with another appointment";

    public void initAppointmentData(Appointment appt) {
        customer.setItems(FXCollections.observableArrayList(Customer.getAllCustomers()));
        user.setItems(FXCollections.observableArrayList(User.getAllUsers()));
        Customer cust = new Customer().getCustomer(appt.getCustomerId());
        User u = new User().getUserNameById(appt.getUserId());
        LocalDateTime start = MiscMethods.createLDT(appt.getStart());
        LocalDateTime end = MiscMethods.createLDT(appt.getEnd());
        startHour.setItems(FXCollections.observableArrayList(MiscMethods.hourList()));
        endHour.setItems(FXCollections.observableArrayList(MiscMethods.hourList()));
        startMinute.setItems(FXCollections.observableArrayList(MiscMethods.minuteList()));
        endMinute.setItems(FXCollections.observableArrayList(MiscMethods.minuteList()));
        customer.setValue(cust);
        user.setValue(u);
        title.setText(appt.getTitle());
        description.setText(appt.getDescription());
        locat.setText(appt.getLocation());
        contact.setText(appt.getContact());
        type.setText(appt.getType());
        url.setText(appt.getUrl());
        startDate.setValue(start.toLocalDate());
        startHour.setValue(start.toLocalTime().toString().substring(0,2));
        startMinute.setValue(start.toLocalTime().toString().substring(3,5));
        endDate.setValue(end.toLocalDate());
        endHour.setValue(end.toLocalTime().toString().substring(0,2));
        endMinute.setValue(end.toLocalTime().toString().substring(3,5));

        apptId = appt.getAppointmentId();
    }

    @FXML Button save;

    public void saveAppointment(ActionEvent actionEvent) throws IOException
    {
        try
        {
            if(title.getText().isEmpty() || description.getText().isEmpty() || locat.getText().isEmpty() ||
                    contact.getText().isEmpty() || type.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText(missingInfo);
                alert.showAndWait();
            }
            Customer cust = customer.getSelectionModel().getSelectedItem();
            User selectedUser = user.getSelectionModel().getSelectedItem();
            LocalDateTime startLDT = MiscMethods.createLDT(startDate.getValue().toString() + " " + startHour.getValue()
                    + ":" + startMinute.getValue());
            ZonedDateTime startUTC = MiscMethods.convertLDTtoUTC(startLDT);
            LocalDateTime endLDT = MiscMethods.createLDT(endDate.getValue().toString() + " " + endHour.getValue() + ":"
                    + endMinute.getValue());
            ZonedDateTime endUTC = MiscMethods.convertLDTtoUTC(endLDT);
            if(startLDT.toLocalTime().isAfter(LocalTime.of(20, 0)) || endLDT.toLocalTime().isAfter(LocalTime.of(20, 0))
            || startLDT.toLocalTime().isBefore(LocalTime.of(8, 0)) || endLDT.toLocalTime().isBefore(LocalTime.of(8, 0)))
            {
                throw new OutsideBusinessHoursException();
            }
            //Create Appointment object
            Appointment appointment = new Appointment(apptId, cust.getCustomerId(), selectedUser.getUserId(selectedUser.getUsername()),
                    title.getText(), description.getText(), locat.getText(), contact.getText(), type.getText(), url.getText(),
                    startUTC.toLocalDateTime().toString().replace("T", " "), endUTC.toLocalDateTime().toString().replace("T", " "),
                    selectedUser.getUsername(), selectedUser.getUsername());
            if(!appointment.checkForOverlappingAppointments())
            {
                throw new OverlappingAppointmentException();
            }
            appointment.updateAppointment(apptId, cust.getCustomerId(), selectedUser.getUserId(selectedUser.getUsername()),
                    title.getText(), description.getText(), locat.getText(), contact.getText(), type.getText(), url.getText(),
                    startUTC.toLocalDateTime().toString().replace("T", " "), endUTC.toLocalDateTime().toString().replace("T", " "),
                    selectedUser.getUsername());

            MiscMethods.setupStage(save, "/Controller/appointments.fxml");
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(missingInfo);
            alert.showAndWait();
        } catch (OutsideBusinessHoursException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(afterBusinessHours);
            alert.showAndWait();
        } catch (OverlappingAppointmentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(overlappingAppointment);
            alert.showAndWait();
        }

    }

    @FXML Button cancel;

    public void cancelAppointment(ActionEvent actionEvent) throws IOException
    {
        MiscMethods.setupStage(cancel, "/Controller/appointments.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")) {
            cText.setText(rb.getString("customerId"));
            userText.setText(rb.getString("user"));
            titleText.setText(rb.getString("title"));
            descripText.setText(rb.getString("description"));
            locatText.setText(rb.getString("location"));
            contactText.setText(rb.getString("contact"));
            typeText.setText(rb.getString("type"));
            startText.setText(rb.getString("start"));
            endText.setText(rb.getString("end"));
            cancel.setText(rb.getString("cancel"));
            save.setText(rb.getString("save"));
            missingInfo = rb.getString("missingInfo");
            afterBusinessHours = rb.getString("afterBusinessHours");
            overlappingAppointment = rb.getString("appointmentOverlap");
        }
    }
}
