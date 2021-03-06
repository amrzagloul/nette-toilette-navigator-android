/*
 * Copyright 2012 Andreas Bruns
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bruns.restrooms.android.data;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class RestroomData {

	private static final NumberFormat KM_FORMATTER = createKilometerFormatter();

	private int distanceInMeter;
	private double longitude;
	private double latitude;
	private String name;
	private String description;
	private String nameOfDescription;
	private String openingHours;
	private String address;
	private OpeningHoursData openingHoursData;

	public RestroomData() {
		openingHoursData = null;
	}

	public String getFullString() {
		return "RestroomData [longitude=" + longitude + ", latitude="
				+ latitude + ", name=" + name + ", nameOfDescription="
				+ nameOfDescription + ", address=" + address
				+ ", openingHours=" + openingHours + ", description="
				+ description + "]";
	}

	@Override
	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getDistanceInMeter() {
		return distanceInMeter;
	}

	public void setDistanceInMeter(int distanceInMeter) {
		this.distanceInMeter = distanceInMeter;
	}

	public void setPosition(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setDescription(String description) {
		this.description = description;

		address = "";
		openingHours = "";

		String[] splittedString = description.split(",");
		switch (splittedString.length) {
		case 3:
			nameOfDescription = splittedString[0].trim();
			address = splittedString[1].trim();
			openingHours = splittedString[2].trim();
			break;
		case 2:
			nameOfDescription = splittedString[0].trim();
			if (splittedString[1].contains("Uhr")) {
				openingHours = splittedString[1].trim();
			} else {
				address = splittedString[1].trim();
			}
			break;
		default:
			if (splittedString.length > 3) {
				nameOfDescription = splittedString[0].trim();
				address = splittedString[1].trim();

				String afterNameString = description.substring(description
						.indexOf(',') + 1);
				String afterStreetString = afterNameString
						.substring(afterNameString.indexOf(',') + 1);
				openingHours = afterStreetString.trim();
			} else {
				throw new RuntimeException("Unable to parse description: "
						+ description);
			}
		}
		
		try {
			openingHoursData = new OpeningHoursData(openingHours);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int isOpen(Date currentTime) {
		return openingHoursData.isOpen(currentTime);
	}

	public OpeningHoursData getOpeningHoursData() {
		return openingHoursData;
	}
	
	public String getDescription() {
		return description;
	}

	public String getNameOfDescription() {
		return nameOfDescription;
	}

	public String getOpeningHours() {
		return openingHours;
	}

	public String getAdresse() {
		return address;
	}

	public String getDistanceAsString() {
		if (distanceInMeter >= 1000) {
			return KM_FORMATTER.format(distanceInMeter / (double) 1000) + " km";
		}
		return Math.round(distanceInMeter / (double) 10) * 10 + " m";
	}

	private static NumberFormat createKilometerFormatter() {
		NumberFormat kmFormatter = NumberFormat.getInstance(Locale.GERMAN);
		kmFormatter.setMaximumFractionDigits(1);
		kmFormatter.setMinimumFractionDigits(1);
		return kmFormatter;
	}

}
