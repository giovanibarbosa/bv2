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
import java.util.List;

public class FbSimplePost {
	
	private String mId;
	private FbSimpleUser mFrom;
	private String mMessage;
	private String mPicture;
	private String mLink;
	private String mName;
	private String mCaption;
	private String mDescription;
	private String mSource;
	private String mType;
	private List<FbSimpleUser> mTo;
	private String mActionName;
	private String mActionLink;
	private Date mCreatedTime;
	private Date mUpdatedTime;
	private int mNumberOfLikes;
	private int mNumberOfComments;
	
	/* Getters and Setters */
	public String getId() {
		return mId;
	}
	public void setId(String objectId) {
		this.mId = objectId;
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
	public String getPicture() {
		return mPicture;
	}
	public void setPicture(String picture) {
		this.mPicture = picture;
	}
	public String getLink() {
		return mLink;
	}
	public void setLink(String link) {
		this.mLink = link;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getCaption() {
		return mCaption;
	}
	public void setCaption(String caption) {
		this.mCaption = caption;
	}
	public String getDescription() {
		return mDescription;
	}
	public void setDescription(String description) {
		this.mDescription = description;
	}
	public String getSource() {
		return mSource;
	}
	public void setSource(String source) {
		this.mSource = source;
	}
	public String getType() {
		return mType;
	}
	public void setType(String type) {
		this.mType = type;
	}
	public List<FbSimpleUser> getTo() {
		return mTo;
	}
	public void setTo(List<FbSimpleUser> to) {
		this.mTo = to;
	}
	public String getActionName() {
		return mActionName;
	}
	public void setActionName(String actionName) {
		this.mActionName = actionName;
	}
	public String getActionLink() {
		return mActionLink;
	}
	public void setActionLink(String actionLink) {
		this.mActionLink = actionLink;
	}
	public Date getCreatedTime() {
		return mCreatedTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.mCreatedTime = createdTime;
	}
	public Date getUpdatedTime() {
		return mUpdatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.mUpdatedTime = updatedTime;
	}
	public int getNumberOfLikes() {
		return mNumberOfLikes;
	}
	public void setNumberOfLikes(int numberOfLikes) {
		this.mNumberOfLikes = numberOfLikes;
	}
	public int getNumberOfComments() {
		return mNumberOfComments;
	}
	public void setNumberOfComments(int numberOfComments) {
		this.mNumberOfComments = numberOfComments;
	}
	
	@Override
	public String toString() {
		return mMessage;
	}
	
	public String getIntervalDescriptionFromCreationDate() {
		
		long timeDifference = mCreatedTime.getTime() - System.currentTimeMillis();
		String description;
		
		timeDifference = (-1) * timeDifference / 60000; // Minutes
		
		if (timeDifference >= 60) {
			timeDifference = timeDifference / 60; // Hours
			
			if (timeDifference >= 24) {
				timeDifference = timeDifference / 24; // Days
				if (timeDifference == 1) {
					description = "about a day ago";
				}
				else if (timeDifference < 7) {
					description = String.format("%d days ago", timeDifference);
				}
				else if (timeDifference == 7) {
					description = "about a week ago";
				}
				else if (timeDifference < 30) {
					timeDifference = timeDifference / 7; // Weeks
					description = String.format("%d weeks ago", timeDifference);
				}
				else if (timeDifference < 60) {
					description = "about a month ago";
				}
				else if (timeDifference < 365) {
					timeDifference = timeDifference / 30; // Months
					description = String.format("%d months ago", timeDifference);
				}
				else if (timeDifference < 730) {
					description = "about a year ago";
				}
				else{
					timeDifference = timeDifference / 365; // Years
					description = String.format("%d years ago", timeDifference);
				}
			}
			else {
				if (timeDifference == 1) {
					description = "about a hour ago";
				}
				else {
					description = String.format("%d hours ago", timeDifference);
				}
			}
		}
		else {
			if (timeDifference == 1) {
				description = "about a minute ago";
			}
			else {
				description = String.format("%d minutes ago", timeDifference);
			}
		}

		return description;
	}
}
