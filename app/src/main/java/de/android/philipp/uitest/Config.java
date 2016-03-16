package de.android.philipp.uitest;

public interface Config {

    // used to share GCM regId with application server - using php app server
    static final String REGISTER_URL = "http://homeserverpb.no-ip.org/gcm/register.php?";
    static final String GROUP_URL = "http://homeserverpb.no-ip.org/gcm/erstellen.php?";
    static final String INVITE_URL = "http://homeserverpb.no-ip.org/gcm/einladen.php?";
    static final String CHECK_USER_URL = "http://homeserverpb.no-ip.org/gcm/checkUsername.php?";
    static final String CHECK_LOGIN_URL = "http://homeserverpb.no-ip.org/gcm/checkLogin.php?";
    static final String CHECK_REGID_URL = "http://homeserverpb.no-ip.org/gcm/checkRegistration.php?";
    static final String DELETE_REGID_URL = "http://homeserverpb.no-ip.org/gcm/deleteRegistration.php?";
    static final String GET_ALL_GROUPS_URL = "http://homeserverpb.no-ip.org/gcm/allGroups.php?";
    static final String GET_ALL_USERS_URL = "http://homeserverpb.no-ip.org/gcm/allUsers.php?";
    static final String GET_MY_GROUPS_URL = "http://homeserverpb.no-ip.org/gcm/myGroups.php?";

     // Google Project Number
    static final String GPID = "438051207740";

}