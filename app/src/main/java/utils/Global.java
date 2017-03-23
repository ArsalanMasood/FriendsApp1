package utils;

import java.util.ArrayList;
import java.util.List;

import com.friends.friends.model.FriendInfo;

import android.app.Activity;
import android.content.Intent;

public class Global {
	// http://fastweightlosscoffee.com/test/james/user/get_conversation_user?user_id=1
	// http://fastweightlosscoffee.com/test/james/user/send_message?sender_id=1&receiver_id=2&message=hiiiii%20anju%20nhere

	// user/get_user_mesage?receiver_id=2 &sender_id =1

	// http://fastweightlosscoffee.com/test/james/user/get_user_message?receiver_id=2&sender_id=1
	// http://fastweightlosscoffee.com/test/james/user/update_user_slogan?id=69&slogan=dddd
	// http://fastweightlosscoffee.com/test/james/user/user_profile?id=69
//	public static String Global_Url = "http://192.168.0.116/james/user/";
	public static String Global_Url = "http://fastweightlosscoffee.com/test/james/user/";
	public static String User_Login = Global_Url + "login2";
	public static String User_Profile = Global_Url + "user_profile";
	public static String User_Register = Global_Url + "register";
	public static String Post_Image = Global_Url + "insert_image_url";
	public static String get_Images = Global_Url + "get_images_list";

	public static String Get_Users_List = Global_Url + "get_user_list";
	public static String Report_User = Global_Url + "report";
	public static String Report_Image = Global_Url + "report_image";
	public static String Remove_Image = Global_Url + "remove_image";
	public static String Update_User_Profile = Global_Url + "update_user_profile";
	public static String Update_User_Password = Global_Url + "reset_password_app";
	public static String Update_User_DeviceToken = Global_Url + "update_user_devicetoken";
	public static String get_conversation_user = Global_Url + "get_conversation_user";
	public static String send_message = Global_Url + "send_message2";
	public static String get_user_message = Global_Url + "get_user_message";
	public static String delete_user_message = Global_Url + "delete_user_message";
	public static String send_file_message = Global_Url + "upload_file_message";
	
//	http://fastweightlosscoffee.com/test/james/user/delete_user_message?id=0&pos=0
	
	public static String user_id = "";
	public static String g_deviceToken;
	public static Activity g_currentActivity;
	public static int g_notifyTemp = 0;
	
	/***
	 * Code by Master
	 */
	public static List<FriendInfo> g_friendList = new ArrayList<>();
	public static List<String> g_friendList_pics = new ArrayList<>();

	public static int g_curPhotoDownloadIndex = 0;
	public static Intent g_downloadServiceIntent = null;
	public static final int g_preLoadPhotoCount = 6;
	
	public static final String RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO = "start_download_friend_photo";
	public static final String RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_FINISH = "start_download_friend_photo_finish";
	public static final String RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP = "start_download_friend_photo_MAP";
}
