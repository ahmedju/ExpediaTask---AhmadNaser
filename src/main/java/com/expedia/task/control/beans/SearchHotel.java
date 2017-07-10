/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expedia.task.control.beans;

import com.expedia.task.control.json.JsonTools;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dell
 */
@ManagedBean(name = "searchHotel", eager = true)
@RequestScoped
public class SearchHotel {

    List<AllBeans> hotelsFoundInfo;

    public String searchHotel() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        DestinationHotel destinationHotel = (DestinationHotel) request.getAttribute("destinationHotel");
        HotelInfo hotelInfo = (HotelInfo) request.getAttribute("hotelInfo");
        OfferDateRangeHotel offerDateRangeHotel = (OfferDateRangeHotel) request.getAttribute("offerDateRangeHotel");

        JsonTools jsonTools = new JsonTools(buildParamsMap(destinationHotel, hotelInfo, offerDateRangeHotel));
        hotelsFoundInfo = jsonTools.getHotelsJsonObject(destinationHotel, hotelInfo, offerDateRangeHotel);
        request.getSession(true).setAttribute("allRowsData", hotelsFoundInfo);

        return "resultSearh.xhtml?faces-redirect=true";
    }

    public List<AllBeans> getHotelsFoundInfo() {
        return hotelsFoundInfo;
    }

    public void setHotelsFoundInfo(List<AllBeans> hotelsFoundInfo) {

        this.hotelsFoundInfo = hotelsFoundInfo;
    }

    private Map<String, ?> buildParamsMap(DestinationHotel destinationHotel, HotelInfo hotelInfo, OfferDateRangeHotel offerDateRangeHotel) {
        Map params = new HashMap();
        params.put("destinationName", destinationHotel.getDestinationName());
        params.put("country", destinationHotel.getCountry());
        params.put("city", destinationHotel.getCity());
        params.put("regionIds", destinationHotel.getRegionID());

        params.put("hotelName", hotelInfo.getHotelName());
        params.put("hotelCountryCode", hotelInfo.getHotelCountryCode());

        params.put("lengthOfStay", offerDateRangeHotel.getLengthOfStay());
        params.put("minTripStartDate", offerDateRangeHotel.getMinTripStartDate());
        params.put("minTripEndDate", offerDateRangeHotel.getMinTripEndDate());

        return params;
    }
}