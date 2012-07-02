package br.edu.ufcg.dsc.httpmodule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
/**
 * Classe que trata da conectividade com o banco de dados atraves de webservices.
 * 
 * @author Anderson Almeida
 * @author Daniel Leite
 * @author Giovani Barbosa
 * @author Julio Henrique
 */
public class HTTPModule {
	
	private static ConnectivityManager connectivity;
	private static NetworkInfo wifiInfo, mobileInfo;
	
	public final static int TIMEOUTCONNECTION = 15000;
	public final static int TIMEOUTSOCKET = 15000;
	
	
	/**
	 * Function that get's the result of a get to the web service.
	 * @param url
	 *      The url to acess
	 * @return content the result from GET
	 * @throws Exception
	 *             throw exception if the time to connect is more than 3 seg or
	 *             the socket timeout is more than 5 seg.
	 */
	public static InputStream getInputStreamFromUrl(String url) throws Exception {
		
		InputStream content = null;
		HttpParams httpParameters = new BasicHttpParams();
		
		// Set the timeout in milliseconds until a connection is established.
		//int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUTCONNECTION);
		
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		//int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUTSOCKET);
		
		// encodes the uri to UTF-8, so "|" characters in google static maps
		// wont be invalid
		String newUri = Uri.encode(url, ":/?&=");

		HttpGet httpGet = new HttpGet(newUri);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		// Execute HTTP Get Request
		HttpResponse response = httpclient.execute(httpGet);
		content = response.getEntity().getContent();
		return content;
	}
	
	/**
	 * Function that result a string from acess a ulr. Make uses from function "getInputStreamFromUrl".
	 * @param url
	 *      Url to acess.
	 * @return contentOfMyInputStream String result of the acess.
	 * @throws Exception
	 */
	public static String getResult(String url) throws Exception {
		BufferedReader rd = new BufferedReader(new InputStreamReader(HTTPModule.getInputStreamFromUrl(url)), 4096);
		String line;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
		}
		
		rd.close();
		String contentOfMyInputStream = sb.toString();
		return contentOfMyInputStream;
	}
	
	
	
	/**
	 * Check if there is connectivity on the phone.
	 * @param myActivity
	 * @return true if connected, false otherwise.
	 */
	public static boolean checkStatus(Activity myActivity) {
			
		connectivity = (ConnectivityManager)myActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    
	    if(wifiInfo.isConnected() || mobileInfo.isConnected()) 
	    	return true;

	   	return false;
	}
	
}
