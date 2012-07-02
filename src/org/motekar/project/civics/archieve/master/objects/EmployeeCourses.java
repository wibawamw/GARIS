package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeCourses {

    public static final String[] COURSES = new String[]{"", "Jabatan",
        "Fungsional", "Teknis"};
    private Long parentIndex = Long.valueOf(0);
    private String coursesName = "";
    private Integer yearAttended = Integer.valueOf(0);
    private Integer totalHour = Integer.valueOf(0);
    private Integer coursesType = Integer.valueOf(0);

    public static final Integer JABATAN = 1;
    public static final Integer FUNGSIONAL = 2;
    public static final Integer TEKNIS = 3;
    
    private boolean selected = false;

    public EmployeeCourses() {
    }

    public String getCoursesName() {
        return coursesName;
    }

    public void setCoursesName(String coursesName) {
        this.coursesName = coursesName;
    }

    public Integer getCoursesType() {
        return coursesType;
    }

    public void setCoursesType(Integer coursesType) {
        this.coursesType = coursesType;
    }

    public Integer getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(Integer totalHour) {
        this.totalHour = totalHour;
    }

    public Integer getYearAttended() {
        return yearAttended;
    }

    public void setYearAttended(Integer yearAttended) {
        this.yearAttended = yearAttended;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getCoursesTypeAsString() {
        return COURSES[coursesType];
    }

    public static ArrayList<String> coursesAsList() {
        ArrayList<String> courses = new ArrayList<String>();
        courses.addAll(Arrays.asList(COURSES));

        return courses;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public String toString() {
        return coursesName;
    }
    
}
