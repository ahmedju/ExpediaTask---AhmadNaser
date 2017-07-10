package com.expedia.task.control.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ahmad Khaled Naser
 */
@ManagedBean
@RequestScoped
public class DestinationHotel {
    private String destinationName;
    private String country;
    private String city;
    private long regionID;

    public long getRegionID() {
        return regionID;
    }

    public void setRegionID(long regionID) {
        this.regionID = regionID;
    }
    
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}


/*

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date convertedCurrentDate = sdf.parse("2013-09-18");
    String date=sdf.format(convertedCurrentDate );
    System.out.println(date);

*/