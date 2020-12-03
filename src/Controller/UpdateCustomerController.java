package Controller;

import Data.Address;
import Data.City;
import Data.Country;
import Data.Customer;
import Utils.MiscMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable
{
    @FXML Text cId;
    @FXML Text cName;
    @FXML Text address;
    @FXML Text address2;
    @FXML Text cityText;
    @FXML Text zipText;
    @FXML Text countryText;
    @FXML Text phoneNumber;
    @FXML TextField id;
    @FXML TextField name;
    @FXML TextField street;
    @FXML TextField street2;
    @FXML TextField city;
    @FXML TextField country;
    @FXML TextField zip;
    @FXML TextField phone;

    public String missingInfo = "Please input values for all fields";

    @FXML Button save;

    public void saveFields() throws IOException
    {
        try
        {
            if(name.getText().isEmpty() || street.getText().isEmpty() || street2.getText().isEmpty() || city.getText().isEmpty()
            || country.getText().isEmpty() || zip.getText().isEmpty() || phone.getText().isEmpty()) {
                throw new RuntimeException();
            }
            Customer customer = new Customer();
            customer = customer.getCustomer(Integer.parseInt(id.getText()));
            int addressId = customer.getAddressId();
            Address address = new Address();
            address = address.getAddress(addressId);
            int cityid = address.getCityId();
            City city = new City();
            city = city.getCityObj(cityid);
            int countryid = city.getCountryId();
            Country country = new Country();
            country = country.getCountryObj(countryid);
            if(!customer.getCustomerName().equals(name.getText())) {
                customer.setCustomerName(name.getText());
            }
            if(!address.getAddress().equals(street.getText())) {
                address.setAddress(street.getText());
            }
            if(!address.getAddress2().equals(street2.getText())) {
                address.setAddress2(street2.getText());
            }
            if(!address.getPostalCode().equals(zip.getText())) {
                address.setPostalCode(zip.getText());
            }
            if(!address.getPhone().equals(phone.getText())) {
                address.setPhone(phone.getText());
            }
            if(!city.getCity().equals(this.city.getText())) {
                city.setCity(this.city.getText());
            }
            if(!country.getCountryName().equals(this.country.getText())) {
                country.setCountryName(this.country.getText());
            }

            MiscMethods.setupStage(save, "/Controller/customer.fxml");
            } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(missingInfo);
            alert.showAndWait();
        }
    }

    @FXML Button cancel;

    public void cancel() throws IOException
    {
        MiscMethods.setupStage(cancel, "/Controller/customer.fxml");
    }

    public void initCustomerData(Customer cust) {
        List custList = cust.getCustomerInfo(cust.getCustomerId());
        id.setText(custList.get(0).toString());
        name.setText(custList.get(1).toString());
        street.setText(custList.get(2).toString());
        city.setText(custList.get(3).toString());
        zip.setText(custList.get(4).toString());
        country.setText(custList.get(5).toString());
        phone.setText(custList.get(6).toString());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")) {
            cId.setText(rb.getString("customerId"));
            cName.setText(rb.getString("customerName"));
            address.setText(rb.getString("address"));
            address2.setText(rb.getString("secondAddress"));
            cityText.setText(rb.getString("city"));
            zipText.setText(rb.getString("zip"));
            countryText.setText(rb.getString("country"));
            phoneNumber.setText(rb.getString("phone"));
            cancel.setText(rb.getString("cancel"));
            save.setText(rb.getString("save"));
            missingInfo = rb.getString("missingInfo");
        }
    }
}
