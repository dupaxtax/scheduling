package Controller;

import Data.Address;
import Data.City;
import Data.Country;
import Data.Customer;
import Utils.MiscMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable
{
    @FXML Text cName;
    @FXML Text address;
    @FXML Text address2;
    @FXML Text cityText;
    @FXML Text zipText;
    @FXML Text countryText;
    @FXML Text phoneNumber;
    @FXML TextField name;
    @FXML TextField street;
    @FXML TextField street2;
    @FXML TextField city;
    @FXML TextField zip;
    @FXML TextField country;
    @FXML TextField phone;
    @FXML Button cancel;
    @FXML Button save;

    public String missingInfo = "Please input values for all fields";


    @FXML
    public void cancel() throws IOException
    {
        //Go back to the customer screen when cancel button is clicked
        MiscMethods.setupStage(cancel, "/Controller/customer.fxml");
    }

    @FXML
    public void saveFields() throws IOException
    {
        try
        {
            if(name.getText().isEmpty() || street.getText().isEmpty() || street2.getText().isEmpty() || city.getText().isEmpty()
            || zip.getText().isEmpty() || country.getText().isEmpty() || phone.getText().isEmpty()) {
                throw new RuntimeException();
            }
            //I'll want to store those values so that I can check them individually to see which tables I need to update
            String cName = name.getText();
            String cStreet = street.getText();
            String cStreet2 = street2.getText();
            String cCity = city.getText();
            String cZip = zip.getText();
            String cCountry = country.getText();
            String cPhone = phone.getText();
            //Check if country already exists, if not add new country
            int countryId = Country.checkDuplicateCountry(cCountry);

            if(countryId != 0)
            {
                System.out.println("Duplicate country");
            } else {
                System.out.println("Unique country");
                Country.addNewCountry(cCountry);
                //Update countryId so it has the new countries correct id
            }

            //Check if city already exists, if not add new city
            int cityId = City.checkDuplicateCountry(cCity);

            if (cityId != 0) {
                System.out.println("Duplicate city");
            } else {
                System.out.println("Unique city");
                City.addNewCity(cCity, cCountry);
            }

            //Check if address already exists, if not add new address
            int addressId = Address.checkDuplicateAddress(cStreet);

            if(addressId != 0) {
                System.out.println("Duplicate address");
            } else {
                System.out.println("Unique Address");
                Address.addNewAddress(cStreet, cStreet2, cCity, cZip, cPhone, cCountry);
            }

            //Check if customer already exists, if not add new customer
            int customerId = Customer.checkDuplicateCustomer(cName);

            if(customerId != 0){
                System.out.println("Duplicate customer");
            } else {
                System.out.println("Unique customer");
                Customer.addNewCustomer(cName, cStreet, 1);
            }

            //Close add window as display customer page
            MiscMethods.setupStage(save, "/Controller/customer.fxml");
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(missingInfo);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("es")) {
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
