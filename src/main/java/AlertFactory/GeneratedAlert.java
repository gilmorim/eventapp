package AlertFactory;

import Alerts.Alert;

public class GeneratedAlert {
     Alert alert;
     int errorCode;

     public GeneratedAlert(){
         alert = null;
         errorCode = 0;
     }

    public GeneratedAlert(Alert alert, int errorCode) {
        this.alert = alert;
        this.errorCode = errorCode;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
