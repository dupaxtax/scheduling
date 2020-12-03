package Controller;

import Data.Customer;
import Utils.MiscMethods;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustomerController implements Initializable
{
    @FXML
    TableView customerTable;

    @FXML TableColumn<Customer, Integer> id;
    @FXML TableColumn<Customer, String> name;
    @FXML TableColumn<Customer, String> address;
    @FXML TableColumn<Customer, String> phone;

    @FXML
    Button add;

    public void addCustomer() throws IOException
    {
        MiscMethods.setupStage(add, "/Controller/add-customer.fxml");
    }

    @FXML Button update;
    public void updateCustomer() throws IOException
    {
        Customer customer;
        Object obj = customerTable.getSelectionModel().getSelectedItem();
        if (obj instanceof Customer) {
            customer = (Customer) obj;

            Stage currStage = (Stage) update.getScene().getWindow();
            currStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-customer.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene((Pane) loader.load()));

            UpdateCustomerController controller = loader.<UpdateCustomerController>getController();
            controller.initCustomerData(customer);

            stage.show();

        } else {
            System.out.println("Not a customer");
            //Some kind of message preventing the ability to move on
        }
    }

    @FXML Button cancel;

    public void cancel() throws IOException
    {
        //Go back to main-screen if cancel button is clicked
        MiscMethods.setupStage(cancel, "/Controller/main-screen.fxml");
    }

    @FXML Button delete;

    public void deleteCustomer() throws IOException
    {
        if(customerTable.getSelectionModel().getSelectedItem() instanceof Customer) {
            Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();

            customer.deleteCustomer(customer.getCustomerId());

            MiscMethods.setupStage(delete, "/Controller/customer.fxml");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es")){
            name.setText(rb.getString("name"));
            address.setText(rb.getString("address"));
            phone.setText(rb.getString("phone"));
            add.setText(rb.getString("add"));
            update.setText(rb.getString("update"));
            delete.setText(rb.getString("delete"));
            cancel.setText(rb.getString("cancel"));
        }
        List customerList = Customer.getAllCustomers();

        id.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerAddress"));
        phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerPhone"));

        customerTable.getItems().setAll(customerList);
    }
}

//For the update customer page make sure there is an option to make the customer inactive