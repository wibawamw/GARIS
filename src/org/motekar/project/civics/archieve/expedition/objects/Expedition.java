package org.motekar.project.civics.archieve.expedition.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;

/**
 *
 * @author Muhamad Wibawa
 */
public class Expedition {

    private Long index = Long.valueOf(0);
    private String documentNumber = "";
    private String departure = "";
    private String destination = "";
    private Integer transportation = Integer.valueOf(0);
    private Date startDate = null;
    private Date endDate = null;
    private Employee assignedEmployee = null;
    private String notes = "";
    private String approvalPlace = "";
    private Date approvalDate = null;
    private boolean styled = false;

    private String charge = "";

    private AssignmentLetter letter = null;
    private Program program = null;
    private Activity activity = null;
    private Account account = null;

    private boolean done = false;
    private ArrayList<ExpeditionFollower> follower = new ArrayList<ExpeditionFollower>();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Expedition() {
    }

    public Expedition(Long index) {
        this.index = index;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalPlace() {
        return approvalPlace;
    }

    public void setApprovalPlace(String approvalPlace) {
        this.approvalPlace = approvalPlace;
    }

    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

     public String getTransportationAsString() {
        return StandardPrice.TRANSPORT_TYPE[transportation];
    }

    public Integer getTransportation() {
        return transportation;
    }

    public void setTransportation(Integer transportation) {
        this.transportation = transportation;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public ArrayList<ExpeditionFollower> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<ExpeditionFollower> follower) {
        this.follower = follower;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public AssignmentLetter getLetter() {
        return letter;
    }

    public void setLetter(AssignmentLetter letter) {
        this.letter = letter;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getIterationDocumentNumber() {
        String it = "";
        ArrayList<String> arrayTokens = new ArrayList<String>();


        StringTokenizer token = new StringTokenizer(documentNumber, "/");

        while(token.hasMoreElements()) {
            arrayTokens.add(token.nextToken());
        }

        if (!arrayTokens.isEmpty()) {
            it = arrayTokens.get(1);
        }

        return it;
    }

    public String getYearDocumentNumber() {
        String it = "";
        ArrayList<String> arrayTokens = new ArrayList<String>();


        StringTokenizer token = new StringTokenizer(documentNumber, "/");

        while(token.hasMoreElements()) {
            arrayTokens.add(token.nextToken());
        }

        if (!arrayTokens.isEmpty()) {
            it = arrayTokens.get(3);
        }

        return it;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Expedition other = (Expedition) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.documentNumber == null) ? (other.documentNumber != null) : !this.documentNumber.equals(other.documentNumber)) {
            return false;
        }
        if ((this.departure == null) ? (other.departure != null) : !this.departure.equals(other.departure)) {
            return false;
        }
        if ((this.destination == null) ? (other.destination != null) : !this.destination.equals(other.destination)) {
            return false;
        }
        if (this.transportation != other.transportation && (this.transportation == null || !this.transportation.equals(other.transportation))) {
            return false;
        }
        if (this.startDate != other.startDate && (this.startDate == null || !this.startDate.equals(other.startDate))) {
            return false;
        }
        if (this.endDate != other.endDate && (this.endDate == null || !this.endDate.equals(other.endDate))) {
            return false;
        }
        if (this.assignedEmployee != other.assignedEmployee && (this.assignedEmployee == null || !this.assignedEmployee.equals(other.assignedEmployee))) {
            return false;
        }
        if ((this.approvalPlace == null) ? (other.approvalPlace != null) : !this.approvalPlace.equals(other.approvalPlace)) {
            return false;
        }
        if (this.approvalDate != other.approvalDate && (this.approvalDate == null || !this.approvalDate.equals(other.approvalDate))) {
            return false;
        }
        if ((this.charge == null) ? (other.charge != null) : !this.charge.equals(other.charge)) {
            return false;
        }
        if (this.letter != other.letter && (this.letter == null || !this.letter.equals(other.letter))) {
            return false;
        }
        if (this.program != other.program && (this.program == null || !this.program.equals(other.program))) {
            return false;
        }
        if (this.activity != other.activity && (this.activity == null || !this.activity.equals(other.activity))) {
            return false;
        }
        if (this.account != other.account && (this.account == null || !this.account.equals(other.account))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 13 * hash + (this.documentNumber != null ? this.documentNumber.hashCode() : 0);
        hash = 13 * hash + (this.departure != null ? this.departure.hashCode() : 0);
        hash = 13 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 13 * hash + (this.transportation != null ? this.transportation.hashCode() : 0);
        hash = 13 * hash + (this.startDate != null ? this.startDate.hashCode() : 0);
        hash = 13 * hash + (this.endDate != null ? this.endDate.hashCode() : 0);
        hash = 13 * hash + (this.assignedEmployee != null ? this.assignedEmployee.hashCode() : 0);
        hash = 13 * hash + (this.approvalPlace != null ? this.approvalPlace.hashCode() : 0);
        hash = 13 * hash + (this.approvalDate != null ? this.approvalDate.hashCode() : 0);
        hash = 13 * hash + (this.charge != null ? this.charge.hashCode() : 0);
        hash = 13 * hash + (this.letter != null ? this.letter.hashCode() : 0);
        hash = 13 * hash + (this.program != null ? this.program.hashCode() : 0);
        hash = 13 * hash + (this.activity != null ? this.activity.hashCode() : 0);
        hash = 13 * hash + (this.account != null ? this.account.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + documentNumber + "</b>"
                    + "<br style=\"font-size:40%\">"
                    + sdf.format(startDate) + " - " + sdf.format(endDate)
                    + "  (" + departure + " - " + destination + ")" + "</br>";
        }


        StringBuilder builder = new StringBuilder();
        builder.append("[").append(documentNumber).
                append("] ").append(" (").append(departure).
                append(" - ").append(destination).append(")");

        if (documentNumber.equals("") || departure.equals("") || destination.equals("")) {
            builder = builder.deleteCharAt(builder.indexOf("["));
            builder = builder.deleteCharAt(builder.indexOf("]"));
            builder = builder.deleteCharAt(builder.indexOf("("));
            builder = builder.deleteCharAt(builder.indexOf("-"));
            builder = builder.deleteCharAt(builder.indexOf(")"));
        }

        return builder.toString();
    }
}
