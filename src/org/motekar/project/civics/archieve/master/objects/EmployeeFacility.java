package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeFacility {

    public static final String[] FACILITY = new String[]{"", "RUMAH DINAS",
        "KENDARAAN RODA DUA", "KENDARAAN RODA EMPAT"};
    private Long parentIndex = Long.valueOf(0);
    private Integer facility = Integer.valueOf(0);
    private boolean owned = false;

    public static final Integer HOUSE = 1;
    public static final Integer MOTORCYCLE = 2;
    public static final Integer CAR = 3;

    public EmployeeFacility() {
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public Integer getFacility() {
        return facility;
    }

    public void setFacility(Integer facility) {
        this.facility = facility;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getFacilityAsString() {
        return FACILITY[facility];
    }

    public static ArrayList<String> facilityAsList() {
        ArrayList<String> grade = new ArrayList<String>();
        grade.addAll(Arrays.asList(FACILITY));

        return grade;
    }

    public static ArrayList<EmployeeFacility> createFacility() {
        ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();

        ArrayList<String> fList = facilityAsList();

        for (int i = 0; i < fList.size(); i++) {
            if (!fList.get(i).equals("")) {
                EmployeeFacility facility = new EmployeeFacility();
                facility.setFacility(Integer.valueOf(i));
                facility.setOwned(false);
                facilitys.add(facility);
            }
        }

        return facilitys;
    }

    @Override
    public String toString() {
        return getFacilityAsString();
    }
}
