package de.android.philipp.uitest;

public interface Config {

    // used to share GCM regId with application server - using php app server
    static final String APP_SERVER_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/gcm.php?shareRegId=1";
    static final String REGISTER_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/register.php?";
    static final String GROUP_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/erstellen.php?";
    static final String INVITE_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/einladen.php?";
    static final String CHECK_USER_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/checkUsername.php?";
    static final String CHECK_LOGIN_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/checkLogin.php?";
    static final String CHECK_REGID_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/checkRegistration.php?";
    static final String GET_ALL_GROUPS_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/allGroups.php?";
    static final String GET_ALL_USERS_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/allUsers.php?";
    static final String GET_MY_GROUPS_URL = "http://lamp.wlan.hwr-berlin.de/museum/gcm/myGroups.php?";

     // Google Project Number
    static final String GPID = "438051207740";

}