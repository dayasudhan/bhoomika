package khaanavali.customer.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import khaanavali.customer.model.Address;
import khaanavali.customer.model.FavouriteAddress;



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

	public static final String KEY_FAVOURITE_ADDRESS = "favourite_address";
	//To store the firebase id in shared preferences
	public static final String KEY_ORDER_ID = "orderid";

	//To store the firebase id in shared preferences
	public static final String KEY_ORDER_ID_LIST = "orderidlist";

	//To store the firebase id in shared preferences
	public static final String KEY_LAST_AREA_SERCHED = "lastareasearched";
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String phone,String email){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_PHONE,phone);
//		// Storing name in pref
		editor.putString(KEY_NAME, name);
//
//		// Storing email in pref
		editor.putString(KEY_EMAIL, email);


//
//		//Storing the unique id
//		editor.putString(Constants.UNIQUE_ID, uniqueId);
		// commit changes
		editor.commit();

	}
	public void setlastareasearched(String orderId)
	{
		editor.putString(KEY_LAST_AREA_SERCHED,orderId);
		editor.commit();
	}
	public String getlastareasearched()
	{
		String id = pref.getString(KEY_LAST_AREA_SERCHED, "");
		return id;
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

	public String getEmail()
	{
		String id = pref.getString(KEY_EMAIL, null);
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
	public void setCurrentOrderId(String orderId)
	{
		editor.putString(KEY_ORDER_ID,orderId);
		editor.commit();
		addOrderId(orderId);
	}
	public String getCurrentOrderId()
	{
		String id = pref.getString(KEY_ORDER_ID, null);
		return id;

	}
	public void setOrderIdList(List<String> list)
	{
		Gson gson = new Gson();
		String json = gson.toJson(list);
		editor.putString(KEY_ORDER_ID_LIST,json);
		editor.commit();
	}

	public void addOrderId(String orderid)
	{
		String orders = pref.getString(KEY_ORDER_ID_LIST, null);

		Gson gson = new Gson();
		List<String> list = null;
		if(orders != null) {
			list = (List<String>) gson.fromJson(orders, Object.class);
		}
		else
		{
			list = new ArrayList<String>();
		}

		list.add(0,orderid);
		if(list.size()> 10)
		{
			list.remove(10);
		}
		setOrderIdList(list);
	}
	public List<String> getOrderIdList()
	{
		String orders = pref.getString(KEY_ORDER_ID_LIST, null);
		List<String> list = null;
		if(orders != null) {
			Gson gson = new Gson();
			list = (List<String>) gson.fromJson(orders, Object.class);
		}
		return list;
	}

	public void setFavoutrateAddress(FavouriteAddress favoutrateAddress)
	{
		String faddrlist = pref.getString(KEY_FAVOURITE_ADDRESS, null);
		ArrayList<FavouriteAddress> faddresslist = null;
		if(faddrlist != null)
		{
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<FavouriteAddress>>() {}.getType();
			faddresslist =  gson.fromJson(faddrlist, listType);
		}
		else {
			faddresslist = new ArrayList<FavouriteAddress>();
		}
		faddresslist.add(favoutrateAddress);
		Gson gson = new Gson();
		String json = gson.toJson(faddresslist);
		editor.putString(KEY_FAVOURITE_ADDRESS,json);
		editor.commit();
	}
	public void clearAddress()
	{
		editor.putString(KEY_FAVOURITE_ADDRESS, null);
		editor.commit();
	}
	public ArrayList<FavouriteAddress> getFavoutrateAddress()
	{
		String faddrlist = pref.getString(KEY_FAVOURITE_ADDRESS, null);
		ArrayList<FavouriteAddress> faddresslist = null;
		if(faddrlist != null) {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<FavouriteAddress>>() {}.getType();
			faddresslist =  gson.fromJson(faddrlist, listType);
		}
		return faddresslist;
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
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
