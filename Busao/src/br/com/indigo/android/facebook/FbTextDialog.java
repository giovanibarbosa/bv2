/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.indigo.android.facebook;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.indigo.android.facebook.SocialFacebook.AppInfoListener;
import br.com.indigo.android.facebook.models.FbSimpleApplication;

import com.facebook.android.Facebook.DialogListener;
import br.edu.ufcg.dsc.R;
import com.facebook.android.Util;

public class FbTextDialog extends Dialog {
	
	static final String TAG = FbTextDialog.class.getSimpleName();
	
    private DialogListener mListener;
    private ImageView mCrossImage;
    private FrameLayout mContent;
    
    
    private EditText mEditText;
    private TextView mViaLabel;
    private CharSequence mPlaceHolder;
    private CharSequence mTitle;
    private TextView mCancelButton;
    private TextView mSendButton;
    private TextView mTitleView;
    
    @Override
    public void setTitle(CharSequence title) {
    	mTitle = title;
    }
    
    public void setPlaceHolder(CharSequence placeHolder) {
    	mPlaceHolder = placeHolder;
    }
    
    public void setPlaceHolder(int placeHolderId) {
    	setPlaceHolder(getContext().getString(placeHolderId));
    }
    
    public void setDialogListener(DialogListener listener) {
    	mListener = listener;
    }
    
    public FbTextDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        
        mTitle = context.getString(R.string.dialog_comment_title);
        mPlaceHolder = context.getString(R.string.dialog_comment_placeholder);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = new FrameLayout(getContext());

        /* Create the 'x' image, but don't add to the mContent layout yet
         * at this point, we only need to know its drawable width and height 
         * to place the webview
         */
        createCrossImage();
        
        /* Now we know 'x' drawable width and height, 
         * layout the webivew and add it the mContent layout
         */
        int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
        setupContainer(crossWidth / 2);
        
        /* Finally add the 'x' image to the mContent layout and
         * add mContent to the Dialog view
         */
        mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        
        /*================================*/
        
        
        mTitleView = (TextView) mContent.findViewById(R.id.title);
        
        mCancelButton = (TextView) mContent.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mListener.onCancel();
				dismiss();
			}
		});
        
        mSendButton = (TextView) mContent.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("message", mEditText.getText().toString());
				mListener.onComplete(bundle);
			}
		});
        
        mEditText = (EditText) mContent.findViewById(R.id.message);
        mEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
				if (s.toString().trim().length() == 0) {
					mSendButton.setEnabled(false);
			    } else {
			    	mSendButton.setEnabled(true);
			    }
			}
		});
        mEditText.setHint(mPlaceHolder);
        
        
        mViaLabel = (TextView) mContent.findViewById(R.id.via_label);
        
        SocialFacebook.getInstance().getAppInfo(new AppInfoListener() {
			
			public void onFail(Throwable thr) {
				Util.logd(TAG, "App info request failed", thr);
			}
			
			public void onCancel() {
			}
			
			public void onComplete(final FbSimpleApplication app) {
				
				mViaLabel.post(new Runnable() {
					public void run() {
						mViaLabel.setText("via " + app.getName());
					}
				});
	            
				new Thread(new Runnable() {
					public void run() {
						
						InputStream is = null;
						try {
							URL url = new URL(app.getIconUrl());
							is = (InputStream) url.getContent();
						} catch (MalformedURLException e) {
							Util.logd(TAG, "App icon request failed", e);
						} catch (IOException e) {
							Util.logd(TAG, "App icon request failed", e);
						}
						
						if (is != null) {
							final BitmapDrawable compoundDrawable = new BitmapDrawable(is);
							compoundDrawable.setBounds(0, 0, 16, 16);
							mViaLabel.post(new Runnable() {
								public void run() {
									mViaLabel.setCompoundDrawables(compoundDrawable, null, null, null);
								}
							});
						}
					}
				}).run();
			}
		});
    }
    
    private void createCrossImage() {
        mCrossImage = new ImageView(getContext());
        // Dismiss the dialog when user click on the 'x'
        mCrossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                FbTextDialog.this.dismiss();
            }
        });
        Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.close);
        mCrossImage.setImageDrawable(crossDrawable);
        /* 'x' should not be visible while webview is loading
         * make it visible only after webview has fully loaded
        */
//        mCrossImage.setVisibility(View.INVISIBLE);
    }
    
    private void setupContainer(int margin) {
        LinearLayout container = new LinearLayout(getContext());
        container.setPadding(margin, margin, margin, margin);
        
        getLayoutInflater().inflate(R.layout.text_dialog, container);
        mContent.addView(container, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	
    	mListener.onCancel();
    }
    
    @Override
    public void show() {
    	super.show();
    	
    	mTitleView.setText(mTitle);
    	mEditText.setText(null);
    	mEditText.requestFocus();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
