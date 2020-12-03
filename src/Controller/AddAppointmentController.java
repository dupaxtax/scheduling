package Controller;

import Data.Appointment;
import Data.Customer;
import Data.User;
import Utils.MiscMethods;
import Utils.OutsideBusinessHoursException;
import Utils.OverlappingAppointmentException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable
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
    @FXML ComboBox user;
    @FXML TextField title;
    @FXML TextField description;
    @FXML TextField location;
    @FXML TextField contact;
    @FXML TextField type;
    @FXML TextField url;
    @FXML DatePicker startDate;
    @FXML ComboBox<String> startHour;
    @FXML ComboBox<String> startMinute;
    @FXML DatePicker endDate;
    @FXML ComboBox<String> endHour;
    @FXML ComboBox<String> endMinute;
    @FXML Button save;

    public String missingInfo = "Please input values for all fields";
    public String afterBusinessHours = "Selected time is after business hours";
    public String overlappingAppointment = "The selected time overlaps with another appointment";

    public void saveAppointment() throws IOException, ParseException
    {
        try
        {
            //Check to make sure all fields have values
            if(title.getText().isEmpty() || description.getText().isEmpty() || location.getText().isEmpty() ||
                    contact.getText().isEmpty() || type.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText(missingInfo);
                alert.showAndWait();
            }
            Customer cust = new Customer();
            if(customer.getSelectionModel().getSelectedItem() instanceof Customer) {
                cust = customer.getSelectionModel().getSelectedItem();
            }
            User user = User.getUser();
            LocalDateTime start = MiscMethods.createLDT(startDate.getValue().toString() + " " + startHour.getValue() + "+" + startMinute.getValue());
            LocalDateTime end = MiscMethods.createLDT(endDate.getValue().toString() + " " + endHour.getValue() + ":" + endMinute.getValue());

            ZonedDateTime startZDT = MiscMethods.convertLDTtoUTC(start);
            ZonedDateTime endZDT = MiscMethods.convertLDTtoUTC(end);

            if(start.toLocalTime().isAfter(LocalTime.of(20, 0)) || end.toLocalTime().isAfter(LocalTime.of(20, 0)))
            {
                throw new OutsideBusinessHoursException();
            }

            Appointment appointment = new Appointment(1, cust.getCustomerId(), user.getUserId(user.getUsername()), title.getText(),
                    description.getText(), location.getText(), contact.getText(), type.getText(), url.getText(),
                    startZDT.toLocalDate().toString() + " " + startZDT.toLocalTime().toString(),
                    endZDT.toLocalDate().toString() + " " + endZDT.toLocalTime().toString(),
                    user.getUsername(), user.getUsername());

            if(!appointment.checkForOverlappingAppointments()){
                throw new OverlappingAppointmentException();
            }

            appointment.addNewAppointment(appointment);

            MiscMethods.setupStage(save, "/Controller/appointments.fxml");
        } catch (NullPointerException e) {
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

    public void cancelAppointment() throws IOException
    {
        MiscMethods.setupStage(cancel, "/Controller/appointments.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        customer.setItems(FXCollections.observableArrayList(Customer.getAllCustomers()));
        user.setItems(FXCollections.observableArrayList(User.getAllUsers()));
        User currUser = User.getUser();
        user.setValue(currUser.getUsername());
        List<String> hourList = MiscMethods.hourList();
        endHour.setItems(FXCollections.observableArrayList(hourList));
        startHour.setItems(FXCollections.observableArrayList(hourList));
        List<String> minuteList = MiscMethods.minuteList();
        endMinute.setItems(FXCollections.observableArrayList(minuteList));
        startMinute.setItems(FXCollections.observableArrayList(minuteList));
    }
}
