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

package br.com.indigo.android.facebook.models;

import java.util.Date;

public class FbComment {
	
	private String mId;
	private FbSimpleUser mFrom;
	private String mMessage;
	private Date mCreatedTime;
	private int mNumberOfLikes;
	private boolean mUserLikes;
	
	/* Getters and Setters */
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		this.mId = id;
	}
	public FbSimpleUser getFrom() {
		return mFrom;
	}
	public void setFrom(FbSimpleUser from) {
		this.mFrom = from;
	}
	public String getMessage() {
		return mMessage;
	}
	public void setMessage(String message) {
		this.mMessage = message;
	}
	public Date getCreatedTime() {
		return mCreatedTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.mCreatedTime = createdTime;
	}
	public int getNumberOfLikes() {
		return mNumberOfLikes;
	}
	public void setNumberOfLikes(int numberOfLikes) {
		this.mNumberOfLikes = numberOfLikes;
	}
	public boolean isUserLikes() {
		return mUserLikes;
	}
	public void setUserLikes(boolean userLikes) {
		this.mUserLikes = userLikes;
	}
	
	
	
}
