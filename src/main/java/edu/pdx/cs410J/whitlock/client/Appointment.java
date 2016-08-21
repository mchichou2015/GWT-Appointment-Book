package edu.pdx.cs410J.whitlock.client;

import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;

/**
 * This class is represents an <code>Appointment</code>.
 */
public class Appointment extends AbstractAppointment implements Comparable {
    private String owner;
    private String description;
    private String beginTimeString;   // "1/1/2016 23:00"
    private String endTimeString;
    private Date begin;
    private Date end;
    private int duration = 0;


    public Appointment() {
        owner = null;
        description = null;
        beginTimeString = null;
        endTimeString = null;
        begin = null;
        end = null;
        duration = 0;
    }

    /**
     * Creates a new <code>Appointment</code>
     *
     * @param owner The appointment's owner
     * @param desc  The appointment's description
     * @param begin The appointment's beginning time
     * @param end   The appointment's ending time
     */
    public Appointment(String owner, String desc, Date begin, Date end) {
        this.owner = owner;
        this.description = desc;
        this.beginTimeString = begin.toString();
        this.endTimeString = end.toString();
        this.begin = begin;
        this.end = end;
        this.duration = (int) (end.getTime() - begin.getTime());
    }

    public String getOwner() {
        return this.owner;
    }

    public int getDurationInMinutes() {
        return (this.duration / 1000) / 60;
    }

    @Override
    public String getBeginTimeString() {
        return this.beginTimeString;
    }

    @Override
    public String getEndTimeString() {
        return this.endTimeString;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Date getBeginTime() {

        return this.begin;
    }

    /**
     * @return a Date object instead of string
     */
    @Override
    public Date getEndTime() {
        return this.end;
    }

    public void setStartTime(String startTime) {
        this.beginTimeString = startTime.toString();
    }

    public void setEndTime(String endTime) {
        this.endTimeString = endTime.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    /**
     * @param o takes an object Appointment and compare based on it's fields
     * @return an int as a result to specify the order.
     */
    //This method will compare two appointments 1st by their start date,then by their end dates, then by description
    @Override
    public int compareTo(Object o) {
        int result = this.getBeginTime().compareTo(((Appointment) o).getBeginTime());
        if (result == 0) {// start dates are the same , compare end dates
            result = this.getEndTime().compareTo(((Appointment) o).getEndTime());
            if (result == 0) { // end Dates are the same, compare description
                result = this.getDescription().compareTo(((Appointment) o).getDescription());
                return result;
            }

        }

        return result;
    }


    /**
     * @param o takes an object and compare it to this object
     * @return  true if all fields are equal
     */
    @Override
    public boolean equals(Object o) {
        if( this.getOwner().equals(((Appointment) o).getOwner())
            && this.getBeginTime().equals(((Appointment) o).getBeginTime())
            && this.getEndTime().equals(((Appointment) o).getEndTime())
            && this.getDescription().equals(((Appointment) o).getDescription()))
            return true;
        return false;

    }




}
