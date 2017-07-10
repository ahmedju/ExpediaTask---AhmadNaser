package com.expedia.task.control.beans;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ahmad Khaled Naser
 */
@ManagedBean
@RequestScoped
public class OfferDateRangeHotel {

    private int lengthOfStay;
    private Date minTripStartDate;
    private Date minTripEndDate;

    public int getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(int lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    public Date getMinTripStartDate() {
        return minTripStartDate;
    }

    public void setMinTripStartDate(Date minTripStartDate) {
        this.minTripStartDate = minTripStartDate;
    }

    public Date getMinTripEndDate() {
        return minTripEndDate;
    }

    public void setMinTripEndDate(Date minTripEndDate) {
        this.minTripEndDate = minTripEndDate;
    }
    
    public void printTripStartDate(){
        System.out.println(minTripStartDate);
    }
}
