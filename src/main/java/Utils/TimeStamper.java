package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamper {

    public TimeStamper(){
    }

    public String now(Date now){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }
}
