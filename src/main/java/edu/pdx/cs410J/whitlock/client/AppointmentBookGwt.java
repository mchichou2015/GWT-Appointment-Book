package edu.pdx.cs410J.whitlock.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.thirdparty.common.css.compiler.passes.PrettyPrinter;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sun.tools.hat.internal.model.Root;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mohammed Mchichou
 *
 * A GWT class that makes sure that we can create,list and search appointment book back from the server
 */
    public class AppointmentBookGwt implements EntryPoint {

    private final Map<String, AppointmentBook> bookData = new HashMap<>();


    private final Alerter alerter;

    @VisibleForTesting
    Button button;
    TextBox textBox;


    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yy hh:mm a");


    // for usiing when adding an appointment
    String addOwner;
    String addDescription;
    TextBox addOwnerTextBox = new TextBox();
    TextBox addDescriptionTextBox = new TextBox();
    TextBox addBeginTimeTextBox = new TextBox();
    TextBox addEndTimeTextBox = new TextBox();
    Date addBeginTime;
    Date addEndTime;
    DateBox addBeginDateBox = new DateBox();
    DateBox addEndDateBox = new DateBox();

    //when searching for an appointment
    String searchOwner;
    TextBox searchOwnerTextBox = new TextBox();
    TextBox searchDescriptionTextBox = new TextBox();
    TextBox searchBeginTimeTextBox = new TextBox();
    TextBox searchEndTimeTextBox = new TextBox();
    Date searchBeginTime;
    Date searchEndTime;
    DateBox searchBeginDateBox = new DateBox();
    DateBox searchEndDateBox = new DateBox();

    //when listing an appointment
    String listOwner;
    TextBox listOwnerTextBox = new TextBox();


    public AppointmentBookGwt() {
        this(new Alerter() {
            @Override
            public void alert(String message) {
                Window.alert(message);
            }
        });
    }

    @VisibleForTesting
    AppointmentBookGwt(Alerter alerter) {
        this.alerter = alerter;

        addWidgets();
    }

    private void addWidgets() {
        button = new Button("Create");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                createAppointments();
            }
        });

        this.textBox = new TextBox();
    }

    private void createAppointments() {
        AppointmentBookServiceAsync async = GWT.create(AppointmentBookService.class);
        int numberOfAppointments = getNumberOfAppointments();
        async.createAppointmentBook(numberOfAppointments, new AsyncCallback<AppointmentBook>() {

            @Override
            public void onSuccess(AppointmentBook appointmentBook) {
                displayAppointments(appointmentBook);
            }

            @Override
            public void onFailure(Throwable ex) {
                alert(ex);
            }
        });
    }

    private int getNumberOfAppointments() {
        String number = this.textBox.getText();

        return Integer.parseInt(number);
    }

    private void displayAppointments(AppointmentBook appointmentBook) {
        StringBuilder sb = new StringBuilder(appointmentBook.toString());
        sb.append("\n");

        Collection<Appointment> appointments = appointmentBook.getAppointments();
        for (Appointment app : appointments) {
            sb.append(app);
            sb.append("\n");
        }
        alerter.alert(sb.toString());
    }

    private void alert(Throwable ex) {
        alerter.alert(ex.toString());
    }

    // Make a command that we will show the readMe .
    Command readMe = new Command() {
        public void execute() {
            showReadMe();

        }
    };

    //new command for creating Appointments
    Command addAppointments = new Command() {
        public void execute() {
            showAddPage();
        }
    };

    //new command for listing all Appointments of an owner
    Command listAppointments = new Command() {
        public void execute() {
            showListPage();
        }
    };

    //new command for listing all Appointments of an owner
    Command searchAppointments = new Command() {
        public void execute() {
            showSearchPage();
        }
    };

    @Override
    public void onModuleLoad() {


        showMainMenu();

    }

    /**
     * This will display the menu and give the user the option to make a slection
     */
    private void showMainMenu() {

        MenuBar newMenu = new MenuBar(true);
        newMenu.addItem("Appointment", addAppointments );
        VerticalPanel vPanel = new VerticalPanel();
        MenuBar listMenu = new MenuBar(true);
        listMenu.addItem("List Appointments for :", listAppointments);
        MenuBar helpMenu = new MenuBar(true);
        helpMenu.addItem("View Readme", readMe);
        MenuBar searchMenu = new MenuBar(true);
        searchMenu.addItem("Find appointments :", searchAppointments);

        // Make a new menu bar, adding a few cascading menus to it.
        MenuBar menu = new MenuBar();
        menu.addItem("New", newMenu);
        menu.addItem("List", listMenu);
        menu.addItem("Search", searchMenu);
        menu.addItem("Help", helpMenu);

        // Add it to the root panel.
        vPanel.add(menu);
        RootPanel.get().add(vPanel);

    }

    /**
     * print a readme help file
     */
    private void showReadMe() {
        String readme =
          "<br/>************************** README **************************************************"
        + "<br/>CS410J: Project 5."
        + "<br/>@author Mohammed Mchichou."
        + "<br/>In this Project, you can create an appointment and add it to an appointment Book."
        + "<br/>Each appointment has the owner name, a description and begin and end date and time."
        + "<br/>The begin and end time format must be as following: MM/DD/YYYY hours:minutes am/pm."
        + "<br/>The time must be in H:MM AM/PM format"
        + "<br/>Display will list all appointments of a specific owner."
        + "<br/>Search will display appointments of an owner between a specific begin and end time"
        + "<br/>************************************************************************************";
        RootPanel.get().clear();
        showMainMenu();
        DialogBox readMe = new DialogBox();
        readMe.setHTML(readme);
        RootPanel.get().add(readMe);
    }

    /**
     * display the sub page that allows to add/create an appointment
     */
    private void showAddPage() {

        RootPanel.get().clear();
        showMainMenu();


        VerticalPanel vPanel = new VerticalPanel();
        Label mainAppLabel = new Label("Create and Add an appointment here :");
        vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        vPanel.add(mainAppLabel);

        //owner
        HorizontalPanel hPanel = new HorizontalPanel();
        Label ownerLabel = new Label("Owner : ");
        hPanel.add(ownerLabel);
        hPanel.setSpacing(8);
        hPanel.add(addOwnerTextBox);
        vPanel.add(hPanel);

        //description
        HorizontalPanel hPanel2 = new HorizontalPanel();
        Label descriptionLabel = new Label("Description : ");
        hPanel2.add(descriptionLabel);
        hPanel2.setSpacing(8);
        hPanel2.add(addDescriptionTextBox);
        vPanel.add(hPanel2);

//        beginTime
        HorizontalPanel hPanel3 = new HorizontalPanel();
        Label beginTimeLabel = new Label("BeginTime : ");
        hPanel3.add(beginTimeLabel);
        hPanel3.setSpacing(8);
        addBeginDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        addBeginDateBox.getDatePicker().setYearArrowsVisible(true);
        hPanel3.add(addBeginDateBox);
        vPanel.add(hPanel3);

        //endTime
        HorizontalPanel hPanel4 = new HorizontalPanel();
        Label endTimeLabel = new Label("EndTime : ");
        hPanel4.add(endTimeLabel);
        hPanel4.setSpacing(8);
        addEndDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        addEndDateBox.getDatePicker().setYearArrowsVisible(true);
        hPanel4.add(addEndDateBox);
        vPanel.add(hPanel4);


        HorizontalPanel hPanel5 = new HorizontalPanel();
        Button addButton = new Button("Add");
        hPanel5.add(addButton);

        addButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                addAppointment();
            }
        });

        vPanel.add(hPanel5);
        RootPanel.get().add(vPanel);
    }

    /**
     * Uses user input to add an appointment to an appointment book if one exist
     * if owner does not have an existing book , a new one will be created.
     */
    private void addAppointment() {
        addOwner = addOwnerTextBox.getText();
        addDescription = addDescriptionTextBox.getText();
        addBeginTime = addBeginDateBox.getValue();
        addEndTime = addEndDateBox.getValue();
        if(addOwner == "" || addDescription == "" || addBeginTime == null || addEndTime == null){

            Window.alert("Missing parameter(s)!\nAll fields are required !");

        }
        else if (addEndTime.before(addBeginTime)) {
            Window.alert("End time before start time!");
        }else {

            // Create appointment section
            Appointment app = new Appointment(addOwner, addDescription, addBeginTime, addEndTime);
            // create an ArrayList of appointments and add the the current appointments to it
            ArrayList<Appointment> appointments = new ArrayList();
            AppointmentBook book = bookData.get(addOwner);
            if (book == null)
                book = new AppointmentBook(addOwner, appointments);
            if ((book.getAppointments()).contains(app)) {
            Window.alert("This appointment exist already !");

            }
            else{
                book.addAppointment(app);
                book.sortAppointments();
                bookData.put(addOwner, book);
                //send an okay that the AppointmentBook was created and added successfully .
                RootPanel.get().clear();
                showMainMenu();
                showAddPage();
                DialogBox outbox = new DialogBox();
                outbox.setHTML("You just added this appointment :</br>" + app.toString()
                        + " Duration : " + app.getDurationInMinutes() + " minutes");
                RootPanel.get().add(outbox);
            }
        }
    }
    /**
     * display the sub page that allows to list all appointments of an owner
     * */
    private void showListPage() {

        RootPanel.get().clear();
        showMainMenu();

        VerticalPanel vPanel = new VerticalPanel();
        Label mainAppLabel = new Label("List all appointments of an owner:");
        vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        vPanel.add(mainAppLabel);

        //owner
        HorizontalPanel hPanel = new HorizontalPanel();
        Label ownerLabel = new Label("Owner : ");
        hPanel.add(ownerLabel);
        hPanel.add(listOwnerTextBox);
        vPanel.add(hPanel);

        HorizontalPanel hPanel1 = new HorizontalPanel();
        Button listButton = new Button("List");
        hPanel1.add(listButton);

        listButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                listAppointments();
            }
        });

        vPanel.add(hPanel1);
        RootPanel.get().add(vPanel);

    }
    /**
     * Uses user input to list all appointments from the owner's appointment book.
     */
    private void listAppointments() {

        listOwner = listOwnerTextBox.getText();

        if (listOwner == ""){
            Window.alert("Please enter the owner name!");

        }
        else {
            AppointmentBook book = bookData.get(listOwner);

            if (book == null) {
                Window.alert("No Appointments for this owner!");

            } else if (book.getOwnerName().contentEquals(listOwner)) {
                //dump all appointments inside the book of this owner
                Iterator iter = book.getAppointments().iterator();
                String out = ("This is " + book.getOwnerName() + "'s appointment Book :");
                while (iter.hasNext()) {
                    Appointment app = (Appointment) iter.next();
                    out += "</br>" + app.toString() + " Duration : " + app.getDurationInMinutes() + " minutes\n";

                }
                RootPanel.get().clear();
                showMainMenu();
                showListPage();
                DialogBox outbox = new DialogBox();
                outbox.setHTML(out);
                RootPanel.get().add(outbox);
            }

        }
    }

    /**
     * display the sub page that allows to search for  all appointments of an owner based on :
     * owner's name
     * begin date and time
     * end date and time
     * */
    private void showSearchPage() {

        RootPanel.get().clear();
        showMainMenu();


        VerticalPanel vPanel = new VerticalPanel();
        Label mainAppLabel = new Label("Search for an appointment    :");
        vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        vPanel.add(mainAppLabel);

        //owner
        HorizontalPanel hPanel = new HorizontalPanel();
        Label ownerLabel = new Label("Owner : ");
        hPanel.add(ownerLabel);
        hPanel.add(searchOwnerTextBox);
        vPanel.add(hPanel);


//        beginTime
        HorizontalPanel hPanel3 = new HorizontalPanel();
        Label beginTimeLabel = new Label("BeginTime : ");
        hPanel3.add(beginTimeLabel);
        searchBeginDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        searchBeginDateBox.getDatePicker().setYearArrowsVisible(true);
        hPanel3.add(searchBeginDateBox);
        vPanel.add(hPanel3);

        //endTime
        HorizontalPanel hPanel4 = new HorizontalPanel();
        Label endTimeLabel = new Label("EndTime : ");
        hPanel4.add(endTimeLabel);
        searchEndDateBox.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        searchEndDateBox.getDatePicker().setYearArrowsVisible(true);
        hPanel4.add(searchEndDateBox);
        vPanel.add(hPanel4);


        HorizontalPanel hPanel5 = new HorizontalPanel();
        Button searchButton = new Button("Search");
        hPanel5.add(searchButton);

        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                searchAppointment();
            }
        });

        vPanel.add(hPanel5);
        RootPanel.get().add(vPanel);


    }

    /**
     * return a list of all matching appointments between begin and end time of a given owner.
     */
    private void searchAppointment() {
        searchOwner = searchOwnerTextBox.getText();
        searchBeginTime = searchBeginDateBox.getValue();
        searchEndTime = searchEndDateBox.getValue();
        if(searchOwner == "" || searchBeginTime == null || searchEndTime == null){

            Window.alert("Missing search keyword(s)!\nAll fields are required !");

        }
        else if (searchEndTime.before(searchBeginTime)) {
            Window.alert("End time before start time!");
        }else {


            AppointmentBook book = bookData.get(searchOwner);

            AppointmentBook resultsBook = new AppointmentBook();

            if (book != null && book.getOwnerName().contentEquals(searchOwner)) {
                ArrayList<Appointment> apps = book.getAppointments();
                Iterator iter = book.getAppointments().iterator();
                while (iter.hasNext()) {
                    Appointment app = (Appointment) iter.next();
                    if (app.getOwner().contentEquals(searchOwner)
                            && (app.getBeginTime().after(searchBeginTime) || app.getBeginTime().equals(searchBeginTime))
                            && (app.getEndTime().before(searchEndTime)) || app.getEndTime().equals(searchEndTime))
                        resultsBook.addAppointment(app);
                }
                if (resultsBook.getAppointments().size() == 0){
                    Window.alert("No appointments found");
                }else{
                    //dump all matching appointments
                    Iterator iter2 = resultsBook.getAppointments().iterator();
                    String out = ("This is all matching appointments :");
                    while (iter2.hasNext()) {
                        Appointment app = (Appointment) iter2.next();
                        out += "</br>" + app.toString() + " Duration : " + app.getDurationInMinutes() + " minutes\n";

                    }
                    RootPanel.get().clear();
                    showMainMenu();
                    showSearchPage();
                    DialogBox outbox = new DialogBox();
                    outbox.setHTML(out);
                    RootPanel.get().add(outbox);                }


            }else{
                Window.alert("No appointments found");
            }
        }

    }

    @VisibleForTesting
    interface Alerter {
        void alert(String message);
    }

}

