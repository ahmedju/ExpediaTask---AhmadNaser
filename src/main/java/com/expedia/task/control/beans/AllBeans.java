package com.expedia.task.control.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ahmad Khaled Naser
 */
@ManagedBean(name = "allBeans", eager = true)
@RequestScoped
public class AllBeans {

    DestinationHotel destinationHotel;
    HotelInfo hotelInfo;
    OfferDateRangeHotel offerDateRangeHotel;

    public DestinationHotel getDestinationHotel() {
        return destinationHotel;
    }

    public void setDestinationHotel(DestinationHotel destinationHotel) {
        this.destinationHotel = destinationHotel;
    }

    public HotelInfo getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(HotelInfo hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public OfferDateRangeHotel getOfferDateRangeHotel() {
        return offerDateRangeHotel;
    }

    public void setOfferDateRangeHotel(OfferDateRangeHotel offerDateRangeHotel) {
        this.offerDateRangeHotel = offerDateRangeHotel;
    }
    
    public List listElements() {
        List allElements = new ArrayList();
        allElements.add(destinationHotel.getRegionID());
        allElements.add(destinationHotel.getDestinationName());
        allElements.add(destinationHotel.getCountry());
        allElements.add(destinationHotel.getCity());
        
        allElements.add(hotelInfo.getHotelCountryCode());
        allElements.add(hotelInfo.getHotelName());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        allElements.add(offerDateRangeHotel.getLengthOfStay());
        allElements.add(dateFormat.format(offerDateRangeHotel.getMinTripStartDate()));
        allElements.add(dateFormat.format(offerDateRangeHotel.getMinTripEndDate()));
        
        return allElements;
                
    }
}
