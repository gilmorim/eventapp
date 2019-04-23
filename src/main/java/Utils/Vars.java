package Utils;

public class Vars {
    // ALERT ERROR CODES
    public static int INVALID_COORDINATES = -1;

    // DTN ERROR CODES
    public static int INVALID_MC_ADDRESS = -10;

    // MULTICAST SENDING PORT
    public static int MULTICAST_PORT_SEND = 9999;
    public static int MULTICAST_PORT_RECEIVE = 9998;

    // ROAD HOLE DETAILS
    public static String[] CRASH_TYPES = {"single", "chain"};
    public static String[] FOG_TYPES = {"light", "medium", "dense"};
    public static String[] RAIN_TYPES = {"light", "medium", "pouring"};
    public static String[] ROADBLOCK_TYPES = {"object", "authority"};
    public static String[] ROADHOLE_STATUSES = {"light", "medium", "rough"};
    public static String[] SNOW_TYPES = {"light", "medium", "heavy"};
    public static String[] TRAFFICJAM_TYPE = {"short", "medium", "long"};
}