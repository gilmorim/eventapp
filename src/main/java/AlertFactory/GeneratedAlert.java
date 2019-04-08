package AlertFactory;

import Alerts.Alert;

public class GeneratedAlert {
     Alert alert;
     String errorMessage;

     public GeneratedAlert(){
         alert = null;
         errorMessage = null;
     }

    public GeneratedAlert(Alert alert, String errorMessage) {
        this.alert = alert;
        this.errorMessage = errorMessage;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void appendError(String s){
         if(errorMessage == null){
             errorMessage = s + "; ";
         } else {
             errorMessage += s + "; ";
         }
    }
}
