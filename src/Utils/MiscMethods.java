package Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MiscMethods
{
    //Create a stage and display it when a button is clicked
    public static void setupStage(Button button, String screen) throws IOException
    {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(MiscMethods.class.getResource(screen));
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }

    //Populate hour dropdowns
    public static List<String> hourList() {
        List<String> hours = new ArrayList<>();
        for(int i = 0; i < 24; ++i) {
            if(i < 10) {
                hours.add(("0" + i));
            } else {
                hours.add(Integer.toString(i));
            }
        }
        return hours;
    }

    //Populate minute dropdowns
    public static List<String> minuteList() {
        List<String> minutes = new ArrayList<>();
        for(int i = 0; i <= 60; ++i) {
            if(i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add(Integer.toString(i));
            }
        }
        return minutes;
    }

    //Convert LocalDateTime to UTC
    public static ZonedDateTime convertLDTtoUTC(LocalDateTime ldt) {
        ZoneId currZId = ZoneId.systemDefault();
        ZonedDateTime currZDT = ZonedDateTime.of(ldt, currZId);

        ZoneId utcZId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(currZDT.toInstant(), utcZId);

        return utcZDT;
    }

    //Convert LDT to current timezone
    public static ZonedDateTime convertToCurrentTimeZone(LocalDateTime ldt) {
        ZoneId utcZId = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.of(ldt, utcZId);

        ZoneId currZId = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(utcZDT.toInstant(), currZId);

        return zdt;
    }

    //Create LocalDateTime
    public static LocalDateTime createLDT(String date) {
        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)),
                Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)));

        return localDateTime;
    }
}
