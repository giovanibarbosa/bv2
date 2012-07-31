package br.edu.ufcg.dsc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.dsc.R;

public class PontoAdapter extends BaseAdapter {
	private List<PontoTuristico> pontos;
	private LayoutInflater mInflater;
	private ViewHolder holder;
	private Context context;

	static class ViewHolder {
		private TextView texto;
		private ImageView imagem;
		private ImageView face, twitter;
	}

	public PontoAdapter(Activity activity, List<PontoTuristico> pontos) {
		this.context = activity;
		mInflater = LayoutInflater.from(context);
		this.pontos = pontos;
	}

	@Override
	public int getCount() {
		return pontos.size();
	}

	@Override
	public Object getItem(int index) {
		return pontos.get(index);
	}

	@Override
	public long getItemId(int index) {
		// return pessoas(index).getImgRes();
		return index;
	}

	@Override
	public View getView(int posicao, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ponto_adapter_item, null);
			holder = new ViewHolder();

			holder.texto = (TextView) convertView.findViewById(R.id.text1);
			holder.imagem = (ImageView) convertView.findViewById(R.id.image1);
			holder.face = (ImageView) convertView.findViewById(R.id.botaoFace);
			holder.twitter = (ImageView) convertView
					.findViewById(R.id.botaoTwitter);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PontoTuristico p = pontos.get(posicao);

		holder.texto.setText(p.getDescricao());
		holder.imagem.setImageResource(p.getImagem());

		holder.face.setOnClickListener(new FaceClickListener(p));
		holder.twitter.setOnClickListener(new TwitterClickListener(p));

		return convertView;
	}

	private class FaceClickListener implements OnClickListener {
		private PontoTuristico ponto;
		private Map<String,String> knownFacebookClients;
		private Map<String, ActivityInfo> foundFacebookClients;
		private String preferredFacebookClient;
		
		public FaceClickListener(PontoTuristico ponto) {
			this.ponto = ponto;
		}

		@Override
		public void onClick(View v) {
			detectFacebookClients();
			try{
				// Facebook button handler
				ComponentName targetComponent = getFacebookClientComponentName();
			    if(targetComponent != null) {
			        Intent intent = new Intent(Intent.ACTION_SEND);
			        intent.setComponent(targetComponent);
			        String intentType = (targetComponent.getClassName().contains("com.facebook")) ? "application/facebook" : "text/plain";
			        intent.setType(intentType);
			        intent.putExtra(Intent.EXTRA_TEXT, "http://www.facebook.com/busaoapp");
			        v.getContext().startActivity(intent);
			    } else {
			    	Toast.makeText(v.getContext(), R.string.nenhum_app_facebook, Toast.LENGTH_LONG).show();      
			    }
				
			}catch(Exception e){
				Toast.makeText(context, R.string.error_facebook, Toast.LENGTH_LONG).show();
			}
			
		}
		
		// Detect Facebook Clients
		public void detectFacebookClients() {
		    knownFacebookClients = new HashMap<String, String>();
		    knownFacebookClients.put("Facebook", "com.facebook.katana.ShareLinkActivity");  //So tem esse app do facebook
		    foundFacebookClients = new HashMap<String, ActivityInfo>();
		     
		    Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");
		    PackageManager pm = context.getPackageManager();
		    List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
		    int len = activityList.size();
		    for (int i = 0; i < len; i++) {
		        ResolveInfo app = (ResolveInfo) activityList.get(i);
		        ActivityInfo activity = app.activityInfo;
		        if (knownFacebookClients.containsValue(activity.name)) {
		        	foundFacebookClients.put(activity.name, activity);
		        }
		    }
		}
		
		public ComponentName getFacebookClientComponentName() {
		    ComponentName result = null;
		         
		    if (foundFacebookClients.size() > 0) {
		        ActivityInfo facebookActivity = null;
		        for(Map.Entry<String, ActivityInfo> entry : foundFacebookClients.entrySet()) {
		        	facebookActivity = entry.getValue();
		           	break;
		        }
		             
		        if (preferredFacebookClient != null) {
		            String activityName = knownFacebookClients.get(preferredFacebookClient);
		            if(foundFacebookClients.containsKey(activityName)) {
		            	facebookActivity = foundFacebookClients.get(activityName);
		            }
		        }
		         
		        result = new ComponentName(facebookActivity.applicationInfo.packageName, facebookActivity.name);
		    }
		     
		    return result;
		}

	}

	private class TwitterClickListener implements OnClickListener {
		private PontoTuristico ponto;
		private Map<String,String> knownTwitterClients;
		private Map<String, ActivityInfo> foundTwitterClients;
		private SharedPreferences prefs;
		private String preferredTwitterClient;

		public TwitterClickListener(PontoTuristico ponto) {
			this.ponto = ponto;
			this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
		}

		@Override 
		public void onClick(View v) { 
			detectTwitterClients();
			try{
				// Tweet button handler
				ComponentName targetComponent = getTwitterClientComponentName();
				String tweet = "Eu conheci o " + ponto.getNome() + " pelo @busaoapp !!!";
			    if(targetComponent != null) {
			        Intent intent = new Intent(Intent.ACTION_SEND);
			        intent.setComponent(targetComponent);
			        String intentType = (targetComponent.getClassName().contains("com.twidroid")) ? "application/twitter" : "text/plain";
			        intent.setType(intentType);
			        intent.putExtra(Intent.EXTRA_TEXT, tweet);
			        v.getContext().startActivity(intent);
			    } else {
			    	Toast.makeText(v.getContext(), R.string.nenhum_app_twitter, Toast.LENGTH_LONG).show();      
			    }
			} catch(Exception e) {
				Toast.makeText(v.getContext(), R.string.error_ao_twittar, Toast.LENGTH_LONG).show();
			}

		}
		
		// Build list of Known Twitter Clients
		private void buildKnownTwitterClientsList() {
		    knownTwitterClients = new HashMap<String, String>();
		    knownTwitterClients.put("Twitter", "com.twitter.android.PostActivity");
		    knownTwitterClients.put("UberSocial", "com.twidroid.activity.SendTweet");
		    knownTwitterClients.put("TweetDeck", "com.tweetdeck.compose.ComposeActivity");
		    knownTwitterClients.put("Seesmic", "com.seesmic.ui.Composer");
		    knownTwitterClients.put("TweetCaster", "com.handmark.tweetcaster.ShareSelectorActivity");
		    knownTwitterClients.put("Plume", "com.levelup.touiteur.appwidgets.TouiteurWidgetNewTweet");
		    knownTwitterClients.put("Twicca", "jp.r246.twicca.statuses.Send");
		}
		 
		// Detect Twitter Clients
		public void detectTwitterClients() {
		    buildKnownTwitterClientsList(); 
		    foundTwitterClients = new HashMap<String, ActivityInfo>();
		     
		    Intent intent = new Intent(Intent.ACTION_SEND);
		    intent.setType("text/plain");
		    PackageManager pm = context.getPackageManager();
		    List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
		    int len = activityList.size();
		    for (int i = 0; i < len; i++) {
		        ResolveInfo app = (ResolveInfo) activityList.get(i);
		        ActivityInfo activity = app.activityInfo;
		        if (knownTwitterClients.containsValue(activity.name)) {
		            foundTwitterClients.put(activity.name, activity);
		        }
		    }
		}
		
		// Resolve the twitter client component name
		public ComponentName getTwitterClientComponentName() {
		    ComponentName result = null;
		         
		    if (foundTwitterClients.size() > 0) {
		        ActivityInfo tweetActivity = null;
		        for(Map.Entry<String, ActivityInfo> entry : foundTwitterClients.entrySet()) {
		            tweetActivity = entry.getValue();
		            break;
		        }
		             
		        if (preferredTwitterClient != null) {
		            String activityName = knownTwitterClients.get(preferredTwitterClient);
		            if(foundTwitterClients.containsKey(activityName)) {
		                tweetActivity = foundTwitterClients.get(activityName);
		            }
		        }
		         
		        result = new ComponentName(tweetActivity.applicationInfo.packageName, tweetActivity.name);
		    }
		     
		    return result;
		}
	}
}
