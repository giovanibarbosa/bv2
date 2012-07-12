/*
 * Copyright 2012 I.ndigo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.indigo.android.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import br.com.indigo.android.facebook.models.FbComment;
import br.com.indigo.android.facebook.models.FbEvent;
import br.com.indigo.android.facebook.models.FbSimpleApplication;
import br.com.indigo.android.facebook.models.FbSimplePost;
import br.com.indigo.android.facebook.models.FbSimpleUser;
import br.com.indigo.android.facebook.models.FbSimpleUser.RSVP_STATUS;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.Util;

public class SocialFacebook {
	
	private static final String TAG = SocialFacebook.class.getSimpleName();
	
	private static final String PREFERENCES = "br.com.indigo.android.facebook";
	private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
	
    private Context mAppContext;
    private Facebook mFacebook;
	private AsyncFacebookRunner mAsync;
    private String[] mPermissions;
    private FbSimpleApplication mApp;
    private String mAppSecret;
    private String mAppAccessToken;
    
    private SimpleDateFormat mDateFormat;
    private TimeZone mFacebookTimeZone;
	
	
	/** Singleton Instance **/
	private static SocialFacebook mInstance;
	
	public static SocialFacebook getInstance() {
		if (mInstance == null) {
			throw new RuntimeException("There is no singleton instance. You must invoke getInstance(Context, String, String, String[]) first.");
		}
		return mInstance;
	}
	
	public static SocialFacebook getInstance(Context context, String appId, String appSecret, String[] permissions){
		synchronized (SocialFacebook.class) {
			if(mInstance == null){
				mInstance = new SocialFacebook(context, appId, appSecret, permissions);
			}
		}
		return mInstance;
	}
	
	private SocialFacebook(Context context, String appId, String appSecret, String[] permissions) {
		
		if (appId == null) {
            new AlertDialog.Builder(context)
            	.setTitle("Facebook Setup Error")
            	.setMessage("Missing app ID. You cannot run the app until you provide this in the code.")
            	.setIcon(android.R.drawable.ic_dialog_alert)
            	.setPositiveButton("OK", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// Quit the app
						System.exit(1);
					}
				})
            	.create().show();
        } else {

        	// Everything is OK
        	mFacebook = new Facebook(appId);
        	mAsync = new AsyncFacebookRunner(mFacebook);
        	mAppContext = context.getApplicationContext();
        	
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mFacebookTimeZone = TimeZone.getTimeZone("PST8PDT");
            mPermissions = permissions;
            
        	// restore session if one exists
        	loadLoginInfo();

        	mApp = new FbSimpleApplication();
        	mApp.setId(appId);

        	mAppSecret = appSecret;
        }
		
	}
	
	
	/**
     * IMPORTANT: This method must be invoked at the top of the calling
     * activity's onActivityResult() function or Facebook authentication will
     * not function properly!
     *
     * If your calling activity does not currently implement onActivityResult(),
     * you must implement it and include a call to this method if you intend to
     * use the authorize() method in this SDK.
     *
     * For more information, see
     * http://developer.android.com/reference/android/app/
     *   Activity.html#onActivityResult(int, int, android.content.Intent)
     */
	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	public boolean isSessionValid() {
		return mFacebook.isSessionValid();
	}
	
	public void getAppAccessToken(final AppTokenRequestListener listener) {
		new Thread() {
			@Override
			public void run() {
				
				try {
					Bundle params = new Bundle();
					params.putString("client_id", mApp.getId());
					params.putString("client_secret", mAppSecret);
					params.putString("grant_type", "client_credentials");
					
					String response = Util.openUrl("https://graph.facebook.com/oauth/access_token", "GET", params);
					String[] components = response.split("=");
					
					if (components.length == 2) {
                        // Success
						mAppAccessToken = components[1];
                        listener.onComplete(mAppAccessToken);
                    } else {
                        // Error
                    	Log.d(TAG, "Could not parse App Login Acess Token");
                    	listener.onFail(new Exception("Could not parse App Login Acess Token"));
                    }
					
				} catch (MalformedURLException e) {
					Log.d(TAG, "Error retrieving App Acess Token", e);
					listener.onFail(e);
				} catch (IOException e) {
					Log.d(TAG, "Error retrieving App Acess Token", e);
					listener.onFail(e);
				}
			}
		}.start();
	}
	
	public void login(Activity activity, final SimpleRequestListener listener) {
		mFacebook.authorize(activity, mPermissions, new DialogAdapter() {
			
			public void onComplete(Bundle values) {
				Log.d(TAG, "User logged in");
				saveLoginInfo();
				listener.onComplete();
			}
			
			public void onCancel() {
				Log.d(TAG, "User cancelled login");
				listener.onCancel();
			}
			
			public void onFail(Throwable thr) {
				Log.d(TAG, "Did not login");
				listener.onFail(thr);
			}
		});
	}
	
	public void logout(Context context, final SimpleRequestListener listener) {
		mAsync.logout(context, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				
				Log.d(TAG, "Logout");
				clearLoginInfo();
				
				listener.onComplete();
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void uninstallApp(final SimpleRequestListener listener) {
	    
	    if (mFacebook.isSessionValid()) {
	        // Passing empty (no) parameters unauthorizes the entire app. To revoke individual permissions
	        // add a permission parameter with the name of the permission to revoke.
	        Bundle params = new Bundle();
	        
	        facebookRequest(null, "me/permissions", params, "DELETE", true, new RequestAdapter() {
				
				public void onComplete(JSONObject jsonResponse, Object state) {
//		            [self logoutWithSuccess:nil];
		            listener.onComplete();
				}
				
				public void onFail(Throwable thr, Object state) {
					listener.onFail(thr);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
			});
	        
	    }
	    else {
	    	// Failure
	    	listener.onFail(new RuntimeException("User is not logged in"));
	    }
	}
	
	
	public void getAppInfo(final AppInfoListener listener) {
	
	    if (mApp.getName() != null) {
	    	listener.onComplete(mApp);
	    } else {
	    	Bundle params = new Bundle();
	    	params.putString("fields", "name,icon_url");
	    	
	    	facebookRequest(null, mApp.getId(), params, false, new RequestAdapter() {
				
				public void onComplete(JSONObject jsonResponse, Object state) {
					try {
						mApp.setName(jsonResponse.getString("name"));
						mApp.setIconUrl(jsonResponse.getString("icon_url"));
						
						listener.onComplete(mApp);
					} catch (JSONException e) {
						Log.d(TAG, "Could not parse Json response", e);
						listener.onFail(new RuntimeException("Could not parse Json response", e));	
					}
				}
				
				public void onFail(Throwable thr, Object state) {
					listener.onFail(thr);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
			});
	    }
	}
	
	
	public void getProfileFeed(Activity activity, String profileId, int pageSize, boolean needsLogin, final FeedListener listener) {
		
		Bundle params = new Bundle();
		params.putString("date_format", "U");
		params.putString("limit", String.valueOf(pageSize));
		params.putString("fields", "id,from,message,picture,link,name,caption,description,source,type,created_time,updated_time,comments,likes");
		
		facebookRequest(activity, profileId + "/feed", params, needsLogin, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				handleFeedResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void getProfileFeedNextPage(final String nextPageUrl, final FeedListener listener) {
		new Thread() {
			@Override 
			public void run() {
				try {
					String response = Util.openUrl(nextPageUrl, "GET", null);
					JSONObject jsonResponse = Util.parseJson(response);
					handleFeedResponse(jsonResponse, listener);
				} catch (FileNotFoundException e) {
					listener.onFail(e);
				} catch (MalformedURLException e) {
					listener.onFail(e);
				} catch (IOException e) {
					listener.onFail(e);
				} catch (FacebookError e) {
					listener.onFail(e);
				} catch (JSONException e) {
					Util.logd(TAG, "Could not parse Json response", e);
					listener.onFail(e);
				}
			}
		}.start();
	}
	
	
	public void publish(final Activity activity, final FbSimplePost post, final NewObjectListener listener) {
		
	    if (!mFacebook.isSessionValid()) {
	    	
	    	login(activity, new SimpleRequestListener() {
	    		
				public void onComplete() {
					publish(activity, post, listener);
				}
	    		
				public void onFail(Throwable thr) {
					listener.onFail(thr);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
				
			});
	    }
	    else
	    {
	        mFacebook.dialog(activity, "feed", paramsForPost(post), new DialogAdapter() {
				
				public void onComplete(Bundle values) {
					
					String postId = values.getString("post_id");
					// Successful posts return a post_id
					if (postId != null) {
						Log.d(TAG, "Feed post ID: " + postId);
						listener.onComplete(postId);
					}
				}
				
				public void onCancel() {
					listener.onCancel();
				}
				
				public void onFail(Throwable thr) {
					listener.onFail(thr);
				}
			}); 
	    }
	}
	
	public void publishWithoutDialog(Activity activity, FbSimplePost post, final NewObjectListener listener) {
		
		facebookRequest(activity, "me/feed", paramsForPost(post), "POST", true, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				listener.onComplete(jsonResponse.optString("id"));
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	
	public void getFriends(Activity activity, int pageSize, final UsersListener listener) {
		
	    Bundle params = new Bundle(1);
	    params.putString("limit", String.valueOf(pageSize));
	    
	    facebookRequest(activity, "me/friends", params, true, new RequestAdapter() {
	    	
	    	public void onComplete(JSONObject jsonResponse, Object state) {
				handleUsersResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}

	public void getFriendsNextPage(final String nextPageUrl, final UsersListener listener) {
		new Thread() {
			@Override 
			public void run() {
				try {
					String response = Util.openUrl(nextPageUrl, "GET", null);
					JSONObject jsonResponse = Util.parseJson(response);
					handleUsersResponse(jsonResponse, listener);
				} catch (FileNotFoundException e) {
					listener.onFail(e);
				} catch (MalformedURLException e) {
					listener.onFail(e);
				} catch (IOException e) {
					listener.onFail(e);
				} catch (FacebookError e) {
					listener.onFail(e);
				} catch (JSONException e) {
					Util.logd(TAG, "Could not parse Json response", e);
					listener.onFail(e);
				}
			}
		}.start();
	}
	
	
	public void getEvent(Activity activity, String eventId, boolean needsLogin, final EventListener listener) {
		
		Bundle params = new Bundle();
		params.putString("date_format", "U");
		
		facebookRequest(activity, eventId, params, needsLogin, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				try {
					listener.onComplete(parseEvent(jsonResponse));
				} catch (JSONException e) {
					Log.d(TAG, "Could not parse Json response", e);
					listener.onFail(new RuntimeException("Could not parse Json response", e));	
				}
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void getEvents(Activity activity, int pageSize, final EventsListener listener) {
		
		Bundle params = new Bundle(3);
	    params.putString("limit", String.valueOf(pageSize));
	    params.putString("fields",	"id,name,start_time,end_time,location");
	    params.putString("date_format", "U");
	    	    
	    facebookRequest(activity, "me/events", params, true, new RequestAdapter() {
	    	
	    	public void onComplete(JSONObject jsonResponse, Object state) {
				handleEventsResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void getEventsNextPage(final String nextPageUrl, final EventsListener listener) {
		
		new Thread() {
			@Override 
			public void run() {
				try {
					String response = Util.openUrl(nextPageUrl, "GET", null);
					JSONObject jsonResponse = Util.parseJson(response);
					handleEventsResponse(jsonResponse, listener);
				} catch (FileNotFoundException e) {
					listener.onFail(e);
				} catch (MalformedURLException e) {
					listener.onFail(e);
				} catch (IOException e) {
					listener.onFail(e);
				} catch (FacebookError e) {
					listener.onFail(e);
				} catch (JSONException e) {
					Util.logd(TAG, "Could not parse Json response", e);
					listener.onFail(e);
				}
			}
		}.start();
	}
	
	public void createEvent(Activity activity, FbEvent event, final NewObjectListener listener) {
		
		facebookRequest(activity, "me/events", paramsForEvent(event), "POST", true, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				listener.onComplete(jsonResponse.optString("id"));
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void inviteUsers(Activity activity, String[] userIds, String eventId, final SimpleRequestListener listener) {
		
		StringBuilder users = new StringBuilder();
	    for (String userId : userIds) {
	    	users.append(userId);
	    	users.append(",");
	    }
	    
	    if (users.length() > 0) {
	    	users.deleteCharAt(users.length() - 1); // Remove last colon
	    }
	    
	    Bundle params = new Bundle();
	    params.putString("users", users.toString());
	    
	    facebookRequest(activity, eventId + "/invited", params, "POST", true, new RequestAdapter() {
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				listener.onComplete();
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void getInvitedUsers(Activity activity, String eventId, int pageSize, UsersListener listener) {
		getInvitedUsers(activity, eventId, RSVP_STATUS.UNKNOWN, pageSize, listener);
	}

	public void getInvitedUsers(Activity activity, String eventId, RSVP_STATUS rsvpStatus, int pageSize, final UsersListener listener) {
	
	    String inviteeType = null;
	    
	    switch (rsvpStatus) {
	        case UNKNOWN:
	            inviteeType = "invited";
	            break;
	        case ATTENDING:
	            inviteeType = "attending";
	            break;
	        case MAYBE:
	            inviteeType = "maybe";
	            break;
	        case DECLINED:
	            inviteeType = "declined";
	            break;
	        case NOT_REPLIED:
	            inviteeType = "noreply";
	            break;
	        default:
	            break;
	    }
	    
	    Bundle params = new Bundle();
	    params.putString("limit", String.valueOf(pageSize));
	    
	    facebookRequest(activity, String.format("%s/%s", eventId, inviteeType), params, true, new RequestAdapter() {
			
	    	public void onComplete(JSONObject jsonResponse, Object state) {
	    		handleUsersResponse(jsonResponse, listener);
			}
	    	
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}

	public void getInvitedUsersNextPage(final String nextPageUrl, final UsersListener listener) {
		new Thread() {
			@Override 
			public void run() {
				try {
					String response = Util.openUrl(nextPageUrl, "GET", null);
					JSONObject jsonResponse = Util.parseJson(response);
					handleUsersResponse(jsonResponse, listener);
				} catch (FileNotFoundException e) {
					listener.onFail(e);
				} catch (MalformedURLException e) {
					listener.onFail(e);
				} catch (IOException e) {
					listener.onFail(e);
				} catch (FacebookError e) {
					listener.onFail(e);
				} catch (JSONException e) {
					Util.logd(TAG, "Could not parse Json response", e);
					listener.onFail(e);
				}
			}
		}.start();    
	}
	
	public void attendEvent(Activity activity, String eventId, final SimpleRequestListener listner) {
		
		facebookRequest(activity, eventId + "/attending", new Bundle(), "POST", true, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				listner.onComplete();
			}
			
			public void onFail(Throwable thr, Object state) {
				listner.onFail(thr);
			}
			
			public void onCancel() {
				listner.onCancel();
			}
		});
	}
	
	public void getCommentsFromPost(Activity activity, String postId, int pageSize, boolean needsLogin, final CommentsListener listener) {
		
		Bundle params = new Bundle();
		params.putString("date_format", "U");
		params.putString("limit", String.valueOf(pageSize));
		
		facebookRequest(activity, postId + "/comments", params, needsLogin, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				handleCommentsResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}

	public void getCommentsFromPostNextPage(final String nextPageUrl, final CommentsListener listener) {
		
		facebookRequest(nextPageUrl, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				handleCommentsResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void getUsersWhoLikedPost(Activity activity, String postId, int pageSize, boolean needsLogin, final UsersListener listener) {
		
		Bundle params = new Bundle();
		params.putString("date_format", "U");
		params.putString("limit", String.valueOf(pageSize));
		
		facebookRequest(activity, postId + "/likes", params, needsLogin, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				handleUsersResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}

	public void getUsersWhoLikedPostNextPage(String nextPageUrl, final UsersListener listener) {
		
		facebookRequest(nextPageUrl, new RequestAdapter() {
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				handleUsersResponse(jsonResponse, listener);
			}
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
	
	public void commentPost(final Activity activity, final String postId, final NewObjectListener listener) {
	
		if (!mFacebook.isSessionValid()) {
			login(activity, new SimpleRequestListener() {
				
				public void onComplete() {
					commentPost(activity, postId, listener);
				}
				
				public void onFail(Throwable thr) {
					listener.onFail(thr);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
			});
		} else {
			
			final FbTextDialog dialog = new FbTextDialog(activity);
			dialog.setDialogListener(new DialogListener() {
				
				public void onComplete(Bundle values) {
					
					final ProgressDialog spinner = new ProgressDialog(activity);
					spinner.setMessage(spinner.getContext().getString(R.string.loading));
					spinner.setCancelable(false);
					spinner.show();
					
					facebookRequest(activity, postId + "/comments", values, "POST", true, new RequestAdapter() {
						
						public void onFail(Throwable thr, Object state) {
							listener.onFail(thr);
							spinner.dismiss();
							dialog.dismiss();
						}
						
						public void onComplete(JSONObject jsonResponse, Object state) {
							listener.onComplete(jsonResponse.optString("id"));
							spinner.dismiss();
							dialog.dismiss();
						}
						
						public void onCancel() {
							listener.onCancel();
							spinner.dismiss();
							dialog.dismiss();
						}
					});
				}
				
				public void onFacebookError(FacebookError e) {
					listener.onFail(e);
				}
				
				public void onError(DialogError e) {
					listener.onFail(e);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
			});
			
			dialog.show();
	    }
	}
	
	public void likeObject(Activity activity, String objectId, final SimpleRequestListener listener) {
		
	    facebookRequest(activity, objectId + "/likes", new Bundle(), "POST", true, new RequestAdapter() {
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onComplete(JSONObject jsonResponse, Object state) {
	            listener.onComplete();
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}
		
	public void publish(final Activity activity, final FbSimplePost post, final String pageId, final NewObjectListener listener) {
		
	    if (!mFacebook.isSessionValid()) {
	        
	    	login(activity, new SimpleRequestListener() {
				
				public void onFail(Throwable thr) {
					listener.onFail(thr);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
				
				public void onComplete() {
					publish(activity, post, pageId, listener);
				}
			});
	    } else {
	        
	    	final FbTextDialog dialog = new FbTextDialog(activity);
	    	dialog.setTitle(R.string.dialog_post_to_wall_title);
	    	dialog.setPlaceHolder(R.string.dialog_post_to_wall_placeholder);
	    	
			dialog.setDialogListener(new DialogListener() {
				
				public void onComplete(Bundle values) {
					
					final ProgressDialog spinner = new ProgressDialog(activity);
					spinner.setMessage(spinner.getContext().getString(R.string.loading));
					spinner.setCancelable(false);
					spinner.show();
					
					Bundle params = paramsForPost(post);
					params.putAll(values);
					
					facebookRequest(activity, pageId + "/feed", params, "POST", true, new RequestAdapter() {
						
						public void onFail(Throwable thr, Object state) {
							listener.onFail(thr);
							spinner.dismiss();
							dialog.dismiss();
						}
						
						public void onComplete(JSONObject jsonResponse, Object state) {
							listener.onComplete(jsonResponse.optString("id"));
							spinner.dismiss();
							dialog.dismiss();
						}
						
						public void onCancel() {
							listener.onCancel();
							spinner.dismiss();
							dialog.dismiss();
						}
					});
				}
				
				public void onFacebookError(FacebookError e) {
					listener.onFail(e);
				}
				
				public void onError(DialogError e) {
					listener.onFail(e);
				}
				
				public void onCancel() {
					listener.onCancel();
				}
			});
			
			dialog.show();
	    }
	}
	
	public void publishWithoutDialog(Activity activity, FbSimplePost post, String pageId, final NewObjectListener listener) {
		
		facebookRequest(activity, pageId + "/feed", paramsForPost(post), "POST", true, new RequestAdapter() {
			
			public void onFail(Throwable thr, Object state) {
				listener.onFail(thr);
			}
			
			public void onComplete(JSONObject jsonResponse, Object state) {
				listener.onComplete(jsonResponse.optString("id"));
			}
			
			public void onCancel() {
				listener.onCancel();
			}
		});
	}

	
	
	/** Private Methods **/
	
	private void facebookRequest(Activity activity, String graphPath, boolean needsLogin, RequestAdapter adapter) {
		facebookRequest(activity, graphPath, new Bundle(), "GET", needsLogin, adapter);
	}
	
	private void facebookRequest(Activity activity, String graphPath, Bundle params, boolean needsLogin, RequestAdapter adapter) {
		facebookRequest(activity, graphPath, params, "GET", needsLogin, adapter);
	}
	
	private void facebookRequest(Activity activity, final String graphPath, final Bundle params, final String httpMethod, boolean needsLogin, final RequestAdapter adapter) {

	    if (needsLogin && !mFacebook.isSessionValid()) {
	    	login(activity, new SimpleRequestListener() {
				
				public void onComplete() {
					mAsync.request(graphPath, params, httpMethod, adapter, null);
				}
	    		
				public void onFail(Throwable thr) {
					adapter.onFail(thr, null);
				}
				
				public void onCancel() {
					adapter.onCancel();
				}
			});
	    }
	    else if (!needsLogin && mAppAccessToken == null && !mFacebook.isSessionValid()) {
	    	
	    	getAppAccessToken(new AppTokenRequestListener() {
				
				public void onFail(Throwable thr) {
					adapter.onFail(thr, null);
				}
				
				public void onComplete(String accessToken) {
					params.putString("access_token", accessToken);
					mAsync.request(graphPath, params, httpMethod, adapter, null);
				}
			});
	    }
	    else {
	    	if (mAppAccessToken != null) {
	    		params.putString("access_token", mAppAccessToken);
	    	}
	    	mAsync.request(graphPath, params, httpMethod, adapter, null);
	    }
	}
	
	private void facebookRequest(final String url, final RequestAdapter adapter) {
		new Thread() {
			@Override 
			public void run() {
				try {
					String response = Util.openUrl(url, "GET", null);
					JSONObject jsonResponse = Util.parseJson(response);
					adapter.onComplete(jsonResponse, null);
				} catch (FileNotFoundException e) {
					adapter.onFail(e, null);
				} catch (MalformedURLException e) {
					adapter.onFail(e, null);
				} catch (IOException e) {
					adapter.onFail(e, null);
				} catch (FacebookError e) {
					adapter.onFail(e, null);
				} catch (JSONException e) {
					Util.logd(TAG, "Could not parse Json response", e);
					adapter.onFail(e, null);
				}
			}
		}.start();
	}
	
	private void saveLoginInfo() {
		Editor editor = mAppContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, mFacebook.getAccessToken());
        editor.putLong(EXPIRES, mFacebook.getAccessExpires());
        editor.commit();
        
	    Log.d(TAG, "Access token info saved");
	}
	
	
	private void loadLoginInfo() {
		// Retrieve authorization information
		SharedPreferences prefs = mAppContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mFacebook.setAccessToken(prefs.getString(TOKEN, null));
        mFacebook.setAccessExpires(prefs.getLong(EXPIRES, 0));
	}
	
	
	private void clearLoginInfo() {
		// Remove saved authorization information if it exists
		Editor editor = mAppContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
		editor.remove(TOKEN);
		editor.remove(EXPIRES);
        editor.commit();
	}
	
	
	private Bundle paramsForPost(FbSimplePost post) {
		
		Bundle params = new Bundle();
	    
	    if (post != null) {
	        // The action links to be shown with the post in the feed
	        String actionName = post.getActionName();
	        String actionLink = post.getActionLink();
	        
	        if (actionName != null && actionLink != null) {
	        	params.putString("actions", String.format("[{name:\"%s\",link:\"%s\"}]", actionName, actionLink));
	        }
	        
	        // Dialog parameters
	        String value = null;
	        List<FbSimpleUser> to = post.getTo();
	        if (to != null && to.size() > 0) {
	            value = to.get(0).getId();
	            if (value != null) { params.putString("to", value); }
	        }
	        value = post.getName();
	        if (value != null) { params.putString("name", value); }
	        value = post.getCaption();
	        if (value != null) { params.putString("caption", value); }
	        value = post.getDescription();
	        if (value != null) { params.putString("description", value); }
	        value = post.getLink();
	        if (value != null) { params.putString("link", value); }
	        value = post.getPicture();
	        if (value != null) { params.putString("picture", value); }
	        value = post.getSource();
	        if (value != null) { params.putString("source", value); }
	        value = post.getMessage();
	        if (value != null) { params.putString("message", value); }
	    }
	    
	    return params;
	}
	
	private Bundle paramsForEvent(FbEvent event) {
		
        Bundle params = new Bundle();
	    
	    if (event != null) {
	    	
	        String value = event.getName();
	        if (value != null) { params.putString("name", value); }
	        
	        Date date = event.getStartTime();
	        if (date != null) {
	        	params.putString("start_time", String.valueOf(facebookUnixTimestampFromDate(date) / 1000));
	        	
	        	date = event.getEndTime();
	        	if (date != null) {
	        		params.putString("end_time", String.valueOf(facebookUnixTimestampFromDate(date) / 1000));
	        	}
	        }
	        
	        value = event.getDescription();
	        if (value != null) { params.putString("description", value); }
	        value = event.getLocation();
	        if (value != null) { params.putString("location", value); }
	        value = event.getPrivacy();
	        if (value != null) {
	        	params.putString("privacy_type", value);
	        } else {
	        	params.putString("privacy_type", FbEvent.PRIVACY_SECRET);
	        }
	    }
	    
	    return params;
	}
	
	private Date dateWithFacebookUnixTimestamp(long unixTimestamp) {
		
		mDateFormat.setTimeZone(mFacebookTimeZone);
		
		Date sourceDate = new Date(unixTimestamp * 1000);
		String dateString = mDateFormat.format(sourceDate);
	    
	    mDateFormat.setTimeZone(TimeZone.getDefault());
	    Date resultDate = null;
	    try {
			resultDate = mDateFormat.parse(dateString);
		} catch (ParseException e) {
			//	Should never enter here
			Log.d(TAG, "Could not convert Unix timestamp to date", e);
		}
		return resultDate;
	}
	
	private long facebookUnixTimestampFromDate(Date date) {
	
	    mDateFormat.setTimeZone(TimeZone.getDefault());
		String dateString = mDateFormat.format(date);
	    
		mDateFormat.setTimeZone(mFacebookTimeZone);
		long resultTime = 0;
		try {
			resultTime = mDateFormat.parse(dateString).getTime();
		} catch (ParseException e) {
			// Should never enter here
			Util.logd(TAG, "Could not convert date to Facebook Unix timestamp", e);
		}
		
		return resultTime;
	}
	
	private void handleFeedResponse(JSONObject jsonResponse, FeedListener listener) {
		FbSimplePost post;
		ArrayList<FbSimplePost> posts = new ArrayList<FbSimplePost>();
        
		try {
			JSONArray objs = jsonResponse.getJSONArray("data");
			
			for (int i = 0; i < objs.length(); i++) {
				JSONObject obj = objs.getJSONObject(i);
				
	            post = new FbSimplePost();
	            post.setId(obj.getString("id"));
	            
	            JSONObject fromJson = obj.optJSONObject("from");
	            if (fromJson != null) {
	                FbSimpleUser from = new FbSimpleUser();
	                from.setId(fromJson.getString("id"));
	                from.setName(fromJson.getString("name"));
	                
	                post.setFrom(from);
	            }
	            
	            post.setMessage(obj.optString("message"));
	            post.setPicture(obj.optString("picture"));
	            post.setLink(obj.optString("link"));
	            post.setName(obj.optString("name"));
	            post.setCaption(obj.optString("caption"));
	            post.setDescription(obj.optString("description"));
	            post.setSource(obj.optString("source"));
	            post.setType(obj.optString("type"));
	            
	            post.setCreatedTime(new Date(obj.getLong("created_time")));
	            post.setUpdatedTime(new Date(obj.getLong("updated_time")));
	            
	            JSONObject comments = obj.optJSONObject("comments");
	            if (comments != null) {
	                post.setNumberOfComments(comments.getInt("count"));
	            }
	            
	            JSONObject likes = obj.optJSONObject("likes");
	            if (likes != null) {
	                post.setNumberOfLikes(likes.getInt("count"));
	            }
	            
	            posts.add(post);
	        }
			
			String nextPage = null;
			JSONObject paging = jsonResponse.optJSONObject("paging");
			if (paging != null) {
				nextPage = paging.optString("next");
			}
			
			listener.onComplete(posts, nextPage);
			
		} catch (JSONException e) {
			Util.logd(TAG, "Could not parse Json response", e);
			listener.onFail(e);	
		}
	}
	
	private void handleUsersResponse(JSONObject jsonResponse, UsersListener listener) {
		
		FbSimpleUser user = null;
		ArrayList<FbSimpleUser> friends = new ArrayList<FbSimpleUser>();
		
		try {
			JSONArray objs = jsonResponse.getJSONArray("data");
			
			for (int i = 0; i < objs.length(); i++) {
				JSONObject obj = objs.getJSONObject(i);
				
				user = new FbSimpleUser();
				user.setId(obj.getString("id"));
				user.setName(obj.getString("name"));
				
				String rsvpStatus = obj.optString("rsvp_status");
	            
	            if (rsvpStatus != null) {
	                if (rsvpStatus.equals("not_replied")) {
	                	user.setRSVPStatus(RSVP_STATUS.NOT_REPLIED);
	                }
	                else if (rsvpStatus.equals("attending")) {
	                    user.setRSVPStatus(RSVP_STATUS.ATTENDING);
	                }
	                else if (rsvpStatus.equals("declined")) {
	                    user.setRSVPStatus(RSVP_STATUS.DECLINED);
	                }
	                else if (rsvpStatus.equals("unsure")) {
	                    user.setRSVPStatus(RSVP_STATUS.MAYBE);
	                }
	            }
				
				friends.add(user);
			}
			
			String nextPage = null;
			JSONObject paging = jsonResponse.optJSONObject("paging");
			if (paging != null) {
				nextPage = paging.optString("next");
			}
	    	
	    	listener.onComplete(friends, nextPage);
	    	
		} catch (JSONException e) {
			Util.logd(TAG, "Could not parse Json response", e);
			listener.onFail(e);
		}
	}
	
	private void handleEventsResponse(JSONObject jsonResponse, EventsListener listener) {
		
		ArrayList<FbEvent> events = new ArrayList<FbEvent>();
		
		try {
			JSONArray objs = jsonResponse.getJSONArray("data");
			
			for (int i = 0; i < objs.length(); i++) {
				JSONObject obj = objs.getJSONObject(i);
				
				events.add(parseEvent(obj));
			}
			
			String nextPage = null;
			JSONObject paging = jsonResponse.optJSONObject("paging");
			if (paging != null) {
				nextPage = paging.optString("next");
			}
	    	
	    	listener.onComplete(events, nextPage);
	    	
		} catch (JSONException e) {
			Util.logd(TAG, "Could not parse Json response", e);
			listener.onFail(e);
		}
	}
	
	private FbEvent parseEvent(JSONObject eventJson) throws JSONException {
		FbEvent event = new FbEvent();

		event.setId(eventJson.getString("id"));
		event.setName(eventJson.getString("name"));
		event.setDescription(eventJson.optString("description"));
		event.setStartTime(dateWithFacebookUnixTimestamp(eventJson.getLong("start_time")));
		event.setEndTime(dateWithFacebookUnixTimestamp(eventJson.optLong("end_time")));
		event.setLocation(eventJson.optString("location"));
		event.setPrivacy(eventJson.optString("privacy"));
		
		JSONObject ownerJson = eventJson.optJSONObject("owner");
		if (ownerJson != null) {
			FbSimpleUser owner = new FbSimpleUser();
			owner.setId(ownerJson.getString("id"));
			owner.setName(ownerJson.optString("name"));
			event.setOwner(owner);
		}
		
		return event;
	}
	
	private void handleCommentsResponse(JSONObject jsonResponse, CommentsListener listener) {
        
		FbComment comment = null;
		ArrayList<FbComment> comments = new ArrayList<FbComment>();
		
		try {
			JSONArray objs = jsonResponse.getJSONArray("data");
			
			for (int i = 0; i < objs.length(); i++) {
				JSONObject obj = objs.getJSONObject(i);
				
				comment = new FbComment();
				comment.setId(obj.getString("id"));
				comment.setMessage(obj.optString("message"));
				comment.setCreatedTime(new Date(obj.optLong("created_time") * 1000));
				comment.setNumberOfLikes(obj.optInt("likes"));
				
				JSONObject fromJson = obj.optJSONObject("from");
				if (fromJson != null) {
					FbSimpleUser fromUser = new FbSimpleUser();
					fromUser.setId(fromJson.getString("id"));
					fromUser.setName(fromJson.optString("name"));
					
					comment.setFrom(fromUser);
				}
				
				comments.add(comment);
			}
			
			String nextPage = null;
			JSONObject paging = jsonResponse.optJSONObject("paging");
			if (paging != null) {
				nextPage = paging.optString("next");
			}
	    	
	    	listener.onComplete(comments, nextPage);
	    	
		} catch (JSONException e) {
			Util.logd(TAG, "Could not parse Json response", e);
			listener.onFail(e);
		}
	}
	
	/** Shingle **/
	public void shingleConfiguration(final String url, final int area, final ShingleListener listener) {
		new Thread() {
			@Override
			public void run() {
				
				try {
					Bundle params = new Bundle();
					params.putString("area_id",	String.valueOf(area));
					
					String response = Util.openUrl(url + "facebook/getFacebook", "GET", params);
					
					try {
						JSONArray result = new JSONArray(response);

						String profileId = result.getJSONObject(0).getString("account_number");
						Log.d(TAG, "Shingle profileId = " + profileId);
						
						listener.onComplete(profileId, false);
					} catch (JSONException e) {
						Log.d(TAG, "Could not parse server response", e);
						listener.onFail(new RuntimeException("Could not parse server response", e));
					}
					
				} catch (MalformedURLException e) {
					Log.d(TAG, "Error retrieving App Acess Token", e);
					listener.onFail(e);
				} catch (IOException e) {
					Log.d(TAG, "Error retrieving App Acess Token", e);
					listener.onFail(e);
				}
			}
		}.start();
	}
	
	
	/** Listeners **/
	
	public interface AppTokenRequestListener {
		public void onComplete(String accessToken);
		public void onFail(Throwable thr);
	}
	
	public static interface ShingleListener {
		public void onComplete(String profile, boolean needsLogin);
		public void onFail(Throwable thr);
	}
	
	private static interface FacebookListener {
		public void onFail(Throwable thr);
		public void onCancel();
	}
	
	public static interface SimpleRequestListener extends FacebookListener {
		public void onComplete();
	}
	
	public static interface AppInfoListener extends FacebookListener {
		public void onComplete(FbSimpleApplication app);
	}
	
	public static interface EventListener extends FacebookListener {
		public void onComplete(FbEvent event);
	}
	
	public static interface NewObjectListener extends FacebookListener {
		public void onComplete(String id);
	}
	
	public static interface FeedListener extends FacebookListener {
		public void onComplete(List<FbSimplePost> posts, String nextPage);
	}
	
	public static interface UsersListener extends FacebookListener {
		public void onComplete(List<FbSimpleUser> users, String nextPage);
	}
	
	public static interface EventsListener extends FacebookListener {
		public void onComplete(List<FbEvent> events, String nextPage);
	}
	
	public static interface CommentsListener extends FacebookListener {
		public void onComplete(List<FbComment> comments, String nextPage);
	}
	
	
	private abstract class RequestAdapter implements RequestListener {
		
		public abstract void onFail(Throwable thr, Object state);
		public abstract void onCancel();
		public abstract void onComplete(JSONObject jsonResponse, Object state);
		
		@Override
		public void onComplete(String response, Object state) {
			try {
				JSONObject jsonResponse = Util.parseJson(response);
				onComplete(jsonResponse, state);
			} catch (FacebookError e) {
				if (e.getErrorCode() == 190) { // RESTAPIAccessTokenErrorCode
					mFacebook.setAccessToken(null);
					mFacebook.setAccessExpires(0);
					clearLoginInfo();
				}
				onFail(e, state);
			} catch (JSONException e) {
				Util.logd(TAG, "Could not parse Json response", e);
				onFail(e, state);
			}
		}
		
		@Override
		public void onIOException(IOException e, Object state) {
			onFail(e, state);
		}
		
		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			onFail(e, state);
		}
		
		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			onFail(e, state);
		}
		
		@Override
		public void onFacebookError(FacebookError e, Object state) {
			onFail(e, state);
		}
	}
	
	private abstract class DialogAdapter implements DialogListener {

		public abstract void onFail(Throwable thr);
		
		public void onFacebookError(FacebookError e) {
			onFail(e);
		}

		public void onError(DialogError e) {
			onFail(e);
		}

	}
	
}
