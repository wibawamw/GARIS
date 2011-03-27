package org.motekar.project.civics.archieve.master.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Muhamad Wibawa
 */
public class StandardPrice {

    public static final String[] TRANSACTION_TYPE = new String[]{"", "Dalam Daerah",
        "Luar Daerah", "Luar Negeri"};
    public static final String[] TRANSPORT_TYPE = new String[]{"", "Mobil","Bis",
        "Kereta Api", "Kapal Laut", "Pesawat Terbang"};
    public static final Integer TYPE_CAR = 1;
    public static final Integer TYPE_BUS = 2;
    public static final Integer TYPE_TRAIN = 3;
    public static final Integer TYPE_SAILING = 4;
    public static final Integer TYPE_PLANE = 5;
    private Long index = Long.valueOf(0);
    private String transactionName = "";
    private Integer transactionType = Integer.valueOf(0);
    private Integer transportType = Integer.valueOf(0);
    private String departure = "";
    private String destination = "";
    private BigDecimal price = BigDecimal.ZERO;
    private String notes = "";
    private boolean styled = false;

    public StandardPrice() {
    }

    public StandardPrice(Long index) {
        this.index = index;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransportType() {
        return transportType;
    }

    public void setTransportType(Integer transportType) {
        this.transportType = transportType;
    }

    public static ArrayList<String> transactionTypeAsList() {
        ArrayList<String> trtype = new ArrayList<String>();
        trtype.addAll(Arrays.asList(TRANSACTION_TYPE));

        return trtype;
    }

    public static ArrayList<String> transportTypeAsList() {
        ArrayList<String> trtype = new ArrayList<String>();
        trtype.addAll(Arrays.asList(TRANSPORT_TYPE));

        return trtype;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + transactionName + "</b>"
                    + "<br style=\"font-size:40%\"><i>" + departure + " - " + destination + "</i></br>";
        }
        return transactionName;
    }
}
