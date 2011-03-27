package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Muhamad Wibawa
 */
public class EmployeeCourses {

    public static final String[] COURSES = new String[]{"", "PERJENJANG",
        "FUNGSIONAL", "TEKNIS"};
    private Long parentIndex = Long.valueOf(0);
    private Integer courses = Integer.valueOf(0);
    private boolean attending = false;

    public static final Integer PERJENJANG = 1;
    public static final Integer FUNGSIONAL = 2;
    public static final Integer TEKNIS = 3;

    public EmployeeCourses() {
    }

    public boolean isAttending() {
        return attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    public Integer getCourses() {
        return courses;
    }

    public void setCourses(Integer courses) {
        this.courses = courses;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getCoursesAsString() {
        return COURSES[courses];
    }

    public static ArrayList<String> coursesAsList() {
        ArrayList<String> courses = new ArrayList<String>();
        courses.addAll(Arrays.asList(COURSES));

        return courses;
    }

    public static ArrayList<EmployeeCourses> createCourses() {
        ArrayList<EmployeeCourses> courseses = new ArrayList<EmployeeCourses>();

        ArrayList<String> cList = coursesAsList();

        for (int i=0;i<cList.size();i++) {
            if (!cList.get(i).equals("")) {
                EmployeeCourses courses = new EmployeeCourses();
                courses.setCourses(Integer.valueOf(i));
                courses.setAttending(false);
                courseses.add(courses);
            }
        }

        return courseses;
    }

    @Override
    public String toString() {
        return getCoursesAsString();
    }
    
}
