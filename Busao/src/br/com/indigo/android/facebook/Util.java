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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class supporting the Facebook Object.
 *
 * @author Massaki
 *
 */
public final class Util {

    public static Date parseDateFromISO8601(String dateString) {
		if (dateString != null) {
			String format = dateString.endsWith("Z") ? "yyyy-MM-dd'T'HH:mm:ss'Z'" : "yyyy-MM-dd'T'HH:mm:ssz";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			try {
				return sdf.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
    
}
