package Controller;

import Data.User;
import Utils.Log;
import Utils.MiscMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    private String errorText = "Incorrect username or password";
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Text incorrectPasswordText;
    @FXML
    Button login;
    @FXML Text usernameText;
    @FXML Text passwordText;

    @FXML
    public void login(MouseEvent mouseEvent) throws IOException
    {
        try
        {
            User.createUserSession(username.getText());
            User user = User.getUser();
            //convert username and password to strings
            String uName = username.getText();
            String pWord = password.getText();

            String password = user.getPassword(uName);

            FileWriter fileWriter = new FileWriter("LogFile.txt", true);
            PrintWriter logFile = new PrintWriter(fileWriter);
            /*A lambda expression here was most efficient because it allows the ability to use the println, print, or
            * other methods in the PrintWriter class without having to create a separate write method*/
            Log log = () -> logFile.print("Login attempted by username: " + uName + " at " + LocalDateTime.now());
            log.write();


            //Check if the password entered is correct
            if(pWord.equals(password)) {
                //Log user in
                MiscMethods.setupStage(login, "/Controller/main-screen.fxml");
                log = () -> logFile.println(" Login by " + uName + " successful.");
                log.write();
            } else {
                log = () -> logFile.println(" Login not successful");
                throw new RuntimeException();
            }
            logFile.close();
        }
        catch (RuntimeException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(errorText);
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle rb = ResourceBundle.getBundle("ResourceBundle", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es")) {
            usernameText.setText(rb.getString("username"));
            passwordText.setText(rb.getString("password"));
            login.setText(rb.getString("login"));
            errorText = rb.getString("invalidLogin");
        }
    }
}
