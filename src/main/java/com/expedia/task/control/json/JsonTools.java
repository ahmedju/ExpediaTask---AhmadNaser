/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expedia.task.control.json;

import com.expedia.task.control.beans.AllBeans;
import com.expedia.task.control.beans.DestinationHotel;
import com.expedia.task.control.beans.HotelInfo;
import com.expedia.task.control.beans.OfferDateRangeHotel;
import com.expedia.task.utils.TextUtils;
import com.sun.istack.internal.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jdk.nashorn.internal.runtime.ParserException;

/**
 *
 * @author Ahmad Khaled Naser
 * @info : This responsible for read JSON and search for hotel
 *
 */
public class JsonTools {

    /**
     * fis : is read file
     */
    private InputStream fis;

    /**
     * fis : is read JSON
     */
    private JsonReader jsonReader;

    /**
     * jsonObject : is root object for JSON file
     */
    private JsonObject objectRoot;

    /**
     * objectOffers : for "offers" JsonObject in json
     */
    private JsonObject objectOffers;

    /**
     * arrayHotel : is repersent all hotels offers in JSON
     */
    private JsonArray arrayHotel;

    /**
     * allHotels : is repersent all hotels offers in JSON
     */
    private List<JsonObject> allHotels;

    URL urlJsonOffers;

    
    public JsonTools(Map params) {
        initVar(params);
    }

    /**
     * @author: Ahmad Khaled Naser
     * @param params: is used to call buildUrlWithParametersQuery
     */
    private void initVar(Map<String, ?> params) {
        try {
            urlJsonOffers = new URL(buildUrlWithParametersQuery(params).replaceAll(" ", "%20"));
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) (urlJsonOffers).openConnection()).getInputStream(), Charset.forName("UTF-8")));
            jsonReader = Json.createReader(reader);

            objectRoot = jsonReader.readObject();
            objectOffers = objectRoot.getJsonObject("offers");
            arrayHotel = objectOffers.getJsonArray("Hotel");
            allHotels = arrayHotel.getValuesAs(JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param params:is Map for each parameter contain keyMap as name of parameter and valueMap as value of parameter
     * @return url with parameters
     */
    private String buildUrlWithParametersQuery(Map<String, ?> params) {
        String resultParams = null;
        StringBuilder queryParams = new StringBuilder("https://offersvc.expedia.com/offers/v2/getOffers?scenario=deal-finder&page=foo&uid=foo&productType=Hotel");

        for (Map.Entry<String, ?> entry : params.entrySet()) {
            queryParams.append("&");
            queryParams.append(entry.getKey());
            queryParams.append("=");

            if (entry.getKey().equals("minTripStartDate") || entry.getKey().equals("minTripEndDate")) {
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String myStr = df.format(entry.getValue());
                    queryParams.append(myStr);
                } catch (ParserException e) {
                    e.printStackTrace();
                }
            } else {
                queryParams.append(entry.getValue());
            }
        }
        resultParams = queryParams.toString();
        queryParams.delete(0, resultParams.length());
        queryParams = null;
        return resultParams;
    }

    /**
     *
     * @param destinationHotel : bean object for destination hotel properties
     * @param hotelInfo : bean object for pricing hotel properties
     * @param offerDateRangeHotel : bean object for offer data range request
     * properties
     * @return List<JsonObject> : for result search hotel for these beans
     */
    public List<AllBeans> getHotelsJsonObject(DestinationHotel destinationHotel, HotelInfo hotelInfo, OfferDateRangeHotel offerDateRangeHotel) {

        List<AllBeans> hotelsFound = new ArrayList();

        for (JsonObject currentHotel : allHotels) {
            AllBeans allBeansForOneHotel = getIfFoundHotel(currentHotel, destinationHotel, hotelInfo, offerDateRangeHotel);
            if (allBeansForOneHotel != null) {
                System.out.println("Found hotel");
                hotelsFound.add(allBeansForOneHotel);
            }
        }

        if (hotelsFound.isEmpty()) {
            return null;
        } else {
            return hotelsFound;
        }
    }

    /**
     *
     * @param currentHotel
     * @param destinationHotel
     * @param offerDateRangeHotel
     * @return boolean : this method check if 'currentHotel' have destination
     * and price data, return true if found hotel, else return false
     */
    @Nullable
    private AllBeans getIfFoundHotel(JsonObject currentHotel, DestinationHotel destinationHotel, HotelInfo hotelInfo, OfferDateRangeHotel offerDateRangeHotel) {

        DestinationHotel destinationJson = getIfContainsData(currentHotel.getJsonObject("destination"), destinationHotel);
        HotelInfo hotelInfoJson = getIfContainsData(currentHotel.getJsonObject("hotelInfo"), hotelInfo);
        OfferDateRangeHotel offerDateRangeHotelJson = getIfContainsData(currentHotel.getJsonObject("offerDateRange"), offerDateRangeHotel);

        if (destinationJson != null && hotelInfoJson != null && offerDateRangeHotelJson != null) {
            AllBeans allBeansForHotel = new AllBeans();
            allBeansForHotel.setDestinationHotel(destinationJson);
            allBeansForHotel.setHotelInfo(hotelInfoJson);
            allBeansForHotel.setOfferDateRangeHotel(offerDateRangeHotelJson);
            return allBeansForHotel;
        }

        return null;
    }

    /**
     *
     * @param jsonObject
     * @param destinationHotel
     * @return instance DestinationHotel if and only if data of destinationHotel contains in jsonObject
     */
    @Nullable
    private DestinationHotel getIfContainsData(JsonObject jsonObject, DestinationHotel destinationHotel) {
        String destinationCity = jsonObject.getString("city");
        String destinationCountry = jsonObject.getString("country");
        String destinationName = jsonObject.getString("longName");
        long destinationRegionId = Long.parseLong(jsonObject.getString("regionID"));

        boolean destinationCityResult = true;
        if (paramRequired(destinationHotel.getCity())) {
            destinationCityResult = destinationCity.contains(destinationHotel.getCity());
        }

        boolean destinationCountryResult = true;
        if (paramRequired(destinationHotel.getCountry())) {
            destinationCountryResult = destinationCountry.contains(destinationHotel.getCountry());
        }

        boolean destinationNameResult = true;
        if (paramRequired(destinationHotel.getDestinationName())) {
            destinationNameResult = destinationName.contains(destinationHotel.getDestinationName());
        }

        boolean destinationRegionIdResult = true;
        if (paramRequired(destinationRegionId)) {
            destinationRegionIdResult = (destinationRegionId == destinationHotel.getRegionID());
        }

        if (destinationCityResult && destinationCountryResult && destinationNameResult && destinationRegionIdResult) {
            DestinationHotel destinationHotelJson = new DestinationHotel();
            destinationHotelJson.setCity(destinationCity);
            destinationHotelJson.setCountry(destinationCountry);
            destinationHotelJson.setDestinationName(destinationName);
            destinationHotelJson.setRegionID(destinationRegionId);
            return destinationHotelJson;
        }

        return null;
    }

    
    /**
     * @param jsonObject
     * @param hotelInfo
     * @author Ahmad Khaled Naser
     * @return instance HotelInfo if and only if data of hotelInfo contains in jsonObject
     */
    private HotelInfo getIfContainsData(JsonObject jsonObject, HotelInfo hotelInfo) {
        String hotelInfoCountryCode = jsonObject.getString("hotelCountryCode");
        String hotelInfoName = jsonObject.getString("hotelName");

        boolean hotelInfoCountryCodeResult = true;
        if (paramRequired(hotelInfo.getHotelCountryCode())) {
            hotelInfoCountryCodeResult = hotelInfoCountryCode.contains(hotelInfo.getHotelCountryCode());
        }

        boolean hotelInfoNameResult = true;
        if (paramRequired(hotelInfo.getHotelName())) {
            hotelInfoNameResult = hotelInfoName.contains(hotelInfo.getHotelName());
        }

        if (hotelInfoCountryCodeResult && hotelInfoNameResult) {
            HotelInfo hotelInfoJson = new HotelInfo();
            hotelInfoJson.setHotelCountryCode(hotelInfoCountryCode);
            hotelInfoJson.setHotelName(hotelInfoName);
            return hotelInfoJson;
        }

        return null;
    }

    /**
     * @param jsonObject
     * @param offerDateRangeHotel
     * @author Ahmad Khaled Naser
     * @return instance OfferDateRangeHotel if and only if data of offerDateRangeHotel contains in jsonObject
     */
    private OfferDateRangeHotel getIfContainsData(JsonObject jsonObject, OfferDateRangeHotel offerDateRangeHotel) {
        Date minTripStartDate = buildDateJson(jsonObject.getJsonArray("travelStartDate"));
        Date minTripEndDate = buildDateJson(jsonObject.getJsonArray("travelEndDate"));
        int lengthOfStay = jsonObject.getInt("lengthOfStay");

        boolean minTripStartDateResult = true;
        if (paramRequired(offerDateRangeHotel.getMinTripStartDate())) {
            minTripStartDateResult = isSameDatesOrAfter(minTripStartDate, offerDateRangeHotel.getMinTripStartDate());
        }

        boolean minTripEndDateResult = true;
        if (paramRequired(offerDateRangeHotel.getMinTripEndDate())) {
            minTripEndDateResult = isSameDatesOrAfter(minTripEndDate, offerDateRangeHotel.getMinTripEndDate());
            
        }

        boolean lengthOfStayResult = true;
        if (paramRequired(lengthOfStay)) {
            lengthOfStayResult = (lengthOfStay == offerDateRangeHotel.getLengthOfStay());
        }

        if (minTripStartDateResult && minTripEndDateResult && lengthOfStayResult) {
            OfferDateRangeHotel offerDateRangeHotelJson = new OfferDateRangeHotel();
            offerDateRangeHotelJson.setLengthOfStay(lengthOfStay);
            offerDateRangeHotelJson.setMinTripEndDate(minTripEndDate);
            offerDateRangeHotelJson.setMinTripStartDate(minTripStartDate);
            return offerDateRangeHotelJson;
        }

        return null;
    }

    boolean isSameDatesOrAfter(Date d1, Date d2){
        return d1.equals(d2) || d1.after(d2);
    }
    
    private boolean paramRequired(String param) {
        return !TextUtils.isStringEmptyOrNull(param);
    }

    private boolean paramRequired(long param) {
        return param != 0;
    }

    private boolean paramRequired(Date date) {
        Date defaultValue = new Date(0, 0, 1);
        if (!date.equals(defaultValue)) {
            return true;
        }

        return false;
    }

    private Date buildDateJson(JsonArray jsonArray) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = null;
//    Date convertedCurrentDate = sdf.parse("2013-09-18");
//    String date=sdf.format(convertedCurrentDate );
//    System.out.println(date);

//        minTripStartDate = [2017, 8, 1]
        StringBuilder dateString = new StringBuilder();
        dateString.append(jsonArray.getInt(0));
        dateString.append("-");

        String month = String.valueOf(jsonArray.getInt(1));
        if (month.length() == 1) {
            dateString.append("0").append(month);
        } else {
            dateString.append(month);
        }

        dateString.append("-");
        
        String day = String.valueOf(jsonArray.getInt(2));
        if (day.length() == 1) {
            dateString.append("0").append(day);
        } else {
            dateString.append(day);
        }

        try {
            convertedDate = sdf.parse(dateString.toString());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return convertedDate;
    }

}
