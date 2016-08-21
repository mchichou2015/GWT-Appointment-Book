package edu.pdx.cs410J.whitlock.client;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Mohammed Mchichou
 * Project5
 * This class is represents an <code>AppointmentBook</code> .
 */
public class AppointmentBook extends AbstractAppointmentBook {
    private String owner = null;
    private ArrayList<Appointment> appointments = null;

    public AppointmentBook() {
        this.owner = null;
        this.appointments = new ArrayList<>();
    }

    public AppointmentBook(String owner, ArrayList<Appointment> appointments) {
        this.owner = owner;
        this.appointments = appointments;
    }

    public boolean emptyOwner() {
        return this.owner == null;
    }

    @Override
    public String getOwnerName() {
        return this.owner;
    }

    @Override
    public ArrayList<Appointment> getAppointments() {

        return this.appointments;
    }

    public void sortAppointments() {
        Collections.sort(this.appointments);
    }

    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {
        this.appointments.add((Appointment) abstractAppointment);
        this.sortAppointments();
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



}