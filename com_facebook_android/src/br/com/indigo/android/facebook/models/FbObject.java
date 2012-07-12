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


public class FbObject {
	
	public enum PICTURE_TYPE {
	    SQUARE,
	    SMALL,
	    NORMAL,
	    LARGE,
	}
	
	private String mId;
	private String mName;
	
	/* Getters and setters */	
	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}
	
	
	
	public String getPictureUrl() {
	    return String.format("http://graph.facebook.com/%s/picture", mId);
	}
	
	public String getPictureUrlWithType(PICTURE_TYPE type) {
		
	    String pictureType = null;
	    switch (type) {
	        case SQUARE:
	            pictureType = "square";
	            break;
	        case SMALL:
	            pictureType = "small";
	            break;
	        case NORMAL:
	            pictureType = "normal";
	            break;
	        case LARGE:
	            pictureType = "large";
	            break;
	        default:
	            pictureType = "";
	            break;
	    }
	    return String.format("http://graph.facebook.com/%s/picture?type=%s", mId, pictureType);
	}

//	- (NSDictionary *)dictionary
//	{
//	    return [self dictionaryWithValuesForKeys:[NSArray arrayWithObjects:@"objectId", @"name", nil]];
//	}
//
//	- (NSString *)description
//	{
//	    return [[self dictionary] description];
//	}
	
}
