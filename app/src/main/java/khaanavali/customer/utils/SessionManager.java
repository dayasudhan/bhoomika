package khaanavali.customer.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import khaanavali.customer.model.Address;
import khaanavali.customer.model.Customer;


public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	public static final String PREF_NAME = "KhaanavaliCustomer";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	public static final String KEY_PHONE = "phone";
	public static final String KEY_AREANAME = "areaname";
	public static final String KEY_LANDMARK = "landmark	";
	public static final String KEY_ADDRESSLINE1 = "addressline1";
	public static final String KEY_ADDRESSLINE2 = "addressline2";
	public static final String KEY_CITY = "city";


	//To store the firebase id in shared preferences
	public static final String KEY_ORDER_ID = "orderid";
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
//		// Storing name in pref
//		editor.putString(KEY_NAME, name);
//
//		// Storing email in pref
//		editor.putString(KEY_EMAIL, email);
//
//		//Storing the unique id
//		editor.putString(Constants.UNIQUE_ID, uniqueId);
		// commit changes
		editor.commit();

	}

    public void setCurrentOrderId(String orderId)
    {
        editor.putString(KEY_ORDER_ID,orderId);
        editor.commit();
    }
	public void setKeyPhone(String orderId)
	{
		editor.putString(KEY_PHONE,orderId);
		editor.commit();
	}
	public String getKeyPhone()
	{
		String id = pref.getString(KEY_PHONE, null);
		return id;
	}
	public String getCurrentOrderId()
	{
		String id = pref.getString(KEY_ORDER_ID, null);
		return id;

	}
	public void setAddress(String areaname,String landmark, String addressline1 ,String addressline2, String city)
	{
		editor.putString(KEY_AREANAME,areaname);
		editor.putString(KEY_LANDMARK,landmark);
		editor.putString(KEY_ADDRESSLINE1,addressline1);
		editor.putString(KEY_ADDRESSLINE2,addressline2);
		editor.putString(KEY_CITY,city);
		editor.commit();
	}
	public Address getAddress()
	{
		Address addr = new Address();
		addr.setAddressLine1(pref.getString(KEY_ADDRESSLINE1, null));
		addr.setAddressLine2(pref.getString(KEY_ADDRESSLINE2, null));
		addr.setLandMark(pref.getString(KEY_LANDMARK, null));
		addr.setAreaName(pref.getString(KEY_AREANAME, null));
		addr.setCity(pref.getString(KEY_CITY, null));

		return addr;

	}
	public void setName(String orderId)
	{
		editor.putString(KEY_NAME,orderId);
		editor.commit();

	}
	public String getName()
	{
		String id = pref.getString(KEY_NAME, null);
		return id;

	}
	public String getCustomer()
	{
		String id = pref.getString(KEY_PHONE, null);
		return id;
	}
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */

//	public boolean checkLogin(){
//		// Check login status
//		if(!this.isLoggedIn()){
//			// user is not logged in redirect him to Login Activity
//			Intent i = new Intent(_context, LoginActivity.class);
//
//			// Closing all the Activities
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//			// Add new Flag to start new Activity
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//			// Staring Login Activity
//			_context.startActivity(i);
//		}
//		return true;
//	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
//    public void logoutUser(){
//        // Clearing all data from Shared Preferences
//        editor.clear();
//        editor.commit();
//        //final boolean b = _context.stopService(new Intent(_context, NotificationListener.class));
//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        _context.startActivity(i);
//    }
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
