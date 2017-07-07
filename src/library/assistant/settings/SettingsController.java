package library.assistant.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
    
    @FXML
    private JFXTextField nDaysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }    
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        int ndays = Integer.parseInt(nDaysWithoutFine.getText());
        float fine = Float.parseFloat(finePerDay.getText());
        String uname = username.getText();
        String pass = password.getText();
        
        Preferences preferences = Preferences.getPreferences();
        preferences.setnDaysWithoutFine(ndays);
        preferences.setFinePerDay(fine);
        preferences.setUsername(uname);
        preferences.setPassword(pass);
        
        Preferences.writePreferenceToFile(preferences);
    }
    
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage)nDaysWithoutFine.getScene().getWindow()).close();
    }
    
    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        username.setText(String.valueOf(preferences.getUsername()));
        password.setText(String.valueOf(preferences.getPassword()));
    }
    
}
