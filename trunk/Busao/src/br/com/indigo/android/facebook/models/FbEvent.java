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

public class FbEvent extends FbObject {

	public static final String PRIVACY_PUBLIC = "PUBLIC";
	public static final String PRIVACY_CLOSED = "CLOSED";
	public static final String PRIVACY_SECRET = "SECRET";
	
	private FbSimpleUser mOwner;
	private String mDescription;
	private Date mStartTime;
	private Date mEndTime;
	private String mLocation;
	private String mPrivacy;
	
	/* Getters and Setters */
	public void setOwner(FbSimpleUser owner) {
		this.mOwner = owner;
	}
	public FbSimpleUser getOwner() {
		return mOwner;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}
	public Date getStartTime() {
		return mStartTime;
	}
	public void setStartTime(Date startTime) {
		this.mStartTime = startTime;
	}
	public Date getEndTime() {
		return mEndTime;
	}
	public void setEndTime(Date endTime) {
		this.mEndTime = endTime;
	}
	public String getLocation() {
		return mLocation;
	}
	public void setLocation(String location) {
		this.mLocation = location;
	}
	public String getPrivacy() {
		return mPrivacy;
	}
	public void setPrivacy(String privacy) {
		this.mPrivacy = privacy;
	}
	
}
