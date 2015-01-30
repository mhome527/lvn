package teach.vietnam.asia.utils;

import teach.vietnam.asia.BuildConfig;

public class Constant {

    final static public boolean isMyDebug = false;
    // ////////
    final static public boolean isPro;
    final static public String PACKAGE_VOICE = "market://details?id=com.google.android.voicesearch";
    final static public String PACKAGE_PREMIUM = "market://details?id=learn.vietnamese.vn.pro";

    // /////////
//	final static public String APP_KEY = "784aqs9zpcajd47";
//	final static public String APP_SECRET = "8i4fgakaa12k8q6";
//	static public String APP_TOCKEN = "ukTwK3tqg4YAAAAAAAAUc5W0O_qi3r7hEKLszLaTmdo_md7TWJSLNF5bJrY2Zj5G";
    // final static public String APP_KEY = "784aqs9zpcajd47";
    // final static public String APP_SECRET = "8i4fgakaa12k8q6";
    // final static public String APP_TOCKEN = "ukTwK3tqg4YAAAAAAAAQTK0gVjiMjB5P_vrpMab3zHhmqNGEu_OVPdkYBKXvsavp";

    public static final String DB_NAME = "VN.db";
//	public static final String KEY_ORG = "origins";
//	public static final String KEY_DES = "destinations";
//	public static String HCM = "HCM";
//	public static String HANNOI = "HANOI2";

//	 public static String JSON_WORDS_NAME = "VN.txt";
//	 public static String JSON_MAPNAME_NAME = "MapName.txt";
//	 public static String JSON_WORDS_NAME = "VN.data";
//	 public static String JSON_MAPNAME_NAME = "MapName.data";

    public static final String JSON_WORDS_NAME;
    public static final String JSON_RECOGNIZE_NAME;
    public static final String JSON_MAPNAME_NAME;
    public static final String CREATE_DB = "DB72";
//    public static final String macAllow = "a";

    static {
        if (isMyDebug) {
            JSON_WORDS_NAME = "VN.txt";
            JSON_RECOGNIZE_NAME = "recognize.txt";
            JSON_MAPNAME_NAME = "MapName.txt";
        } else {
            JSON_WORDS_NAME = "VN.data";
            JSON_RECOGNIZE_NAME = "recognize.txt";
            JSON_MAPNAME_NAME = "MapName.data";
        }


//        if (Utility.getMacAddress(MyApplication.getInstance()).equals(macAllow))
//            isPro = true;
//        else
        if (BuildConfig.APPLICATION_ID.equals("learn.vietnamese.vn.pro"))
            isPro = true;
        else
            isPro = false;
    }

    public static final String KEY_UPDATE = Constant.JSON_WORDS_NAME + Constant.CREATE_DB;

    public static boolean bLog = BuildConfig.DEBUG;
    public static String PREF_PAGE = "curr_page";

//	public static String LIST_STREET_HCM = "listStreetHCM";
//	public static String LIST_STREET_HN = "listStreetHN";
//
//	public static String KEY_CITY = "city";
//	public static final String PREF_MODIFY_AD = "date_modify_ad";
    //
    // /////// dropbox
//	static public String FOLDER_NAME;
//	static {
//		if (BuildConfig.DEBUG)
//			FOLDER_NAME = "/Bus_debug/";
//		else
//			FOLDER_NAME = "/Bus/";
//	}
//	final static public String JSON_AD = "ad.json";
//	final static public String JSON_BUS_HCM = "HCM";
//	final static public String JSON_BUS_HN = "HANOI2";
//	final static public String CLEAR_CACHE_HCM = "clear_cache_HCM";
//	final static public String CLEAR_CACHE_HN = "clear_cache_HN";
//
//	final static public String PRICE1_TAXI = "price1_taxi";
//	final static public String PRICE2_TAXI = "price2_taxi";
//	final static public String PRICE_BEGIN_TAXI = "price_begin_taxi";
//	final static public String KM_TAXI = "km_taxi";
//	static public int KM_DEFAULT = 31;

    // GA
	public static String KEY_ANALYSIS = "UA-54709178-3"; // daohuynh7
//	public static String GA_RECOGNIZE_LEARN_FRAGMENT = "LEARN RECOGNIZE";
//	public static String GA_RECOGNIZE_TEST_FRAGMENT = "TEST RECOGNIZE";

//
//	// /////////
//	public static final String COLUMN_SOUND = tblMapNameDao.Properties.Sound.columnName;
    public static final String INTENT_KIND = "intent_kind";
    public static final String INTENT_POSITION = "intent_pos";
    public static final String INTENT_WORD = "intent_word";
    public static final String PREF_BG_THEME = "bg_theme2";

    public static final int REQ_CODE_SPEECH_INPUT = 1000;

}
