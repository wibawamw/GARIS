package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class Employee {

    public static final String[] PANGKAT = new String[]{"", "Juru Muda", "Juru Muda Tingkat I",
        "Juru", "Juru Tingkat I",
        "Pengatur Muda", "Pengatur Muda Tingkat I", "Pengatur", "Pengatur Tingkat I",
        "Penata Muda", "Penata Muda Tingkat I", "Penata", "Penata Tingkat I",
        "Pembina", "Pembina Tingkat I", "Pembina Utama Muda", "Pembina Utama Madya",
        "Pembina Utama"};

    public static final String[] GRADES = new String[]{"", "Juru Muda, IA", "Juru Muda Tingkat I, IB",
        "Juru, IC", "Juru Tingkat I, ID",
        "Pengatur Muda, IIA", "Pengatur Muda Tingkat I, IIB", "Pengatur, IIC", "Pengatur Tingkat I, IID",
        "Penata Muda, IIIA", "Penata Muda Tingkat I, IIIB", "Penata, IIIC", "Penata Tingkat I, IIID",
        "Pembina, IVA", "Pembina Tingkat I, IVB", "Pembina Utama Muda, IVC", "Pembina Utama Madya, IVD",
        "Pembina Utama, IVE"};

    public static final String[] FUNGSIONAL = new String[]{"", "Staf", "Dokter", "Perawat",
        "Kepala Sekolah", "Guru", "Pengawas"};

    public static final String[] STRUKTURAL = new String[]{"","SEKRETARIS DAERAH",
        "KEPALA DINAS","KEPALA BADAN","STAF AHLI",
        "CAMAT","SEKRETARIS CAMAT",
        "KEPALA BAGIAN",
        "KEPALA BIDANG",
        "KEPALA SEKSI",
        "KEPALA SUB BAGIAN",
        "KEPALA SUB BIDANG",
        "KEPALA SUB SEKSI"};

    public static final String[] ESELON = new String[]{"", "IA", "IB",
        "IIA", "IIB", "IIIA", "IIIB", "IVA", "IVB"};

    public static final String[] MARITAL = new String[]{"", "LAJANG", "MENIKAH",
        "DUDA", "JANDA", "CERAI"};

    public static final Integer[] CHILDREN = new Integer[]{0, 1, 2, 3};

    public static final String[] EDUCATION = new String[]{"", "S3", "S2",
        "S1", "D3", "D1", "SMA", "SMK", "SMP", "SD"};

    public static final String SEKRETARIS_DAERAH = STRUKTURAL[1];
    public static final String SEKRETARIS_DEWAN = STRUKTURAL[2];
    public static final String KEPALA_DINAS = STRUKTURAL[3];
    public static final String KEPALA_BADAN = STRUKTURAL[4];
    
    public static final String[] SEX = new String[]{"", "Pria", "Wanita"};
    public static final Integer MALE = 1;
    public static final Integer FEMALE = 2;

    private Long index = Long.valueOf(0);
    private String name = "";
    private String nip = "";
    private String birthPlace = "";
    private Date birthDate = new Date();
    
    private Integer marital = Integer.valueOf(0);
    private String soulmate = "";
    private Integer children = Integer.valueOf(0);
    private Integer education = Integer.valueOf(0);

    private Integer sex = Integer.valueOf(0);
    private Integer eselon = Integer.valueOf(0);
    private Integer grade = Integer.valueOf(0);
    private Integer fungsional = Integer.valueOf(0);
    private Integer struktural = Integer.valueOf(0);
    private String positionNotes = "";

    private Date cpnsTMT = null;
    private Date pnsTMT = null;
    private Date gradeTMT = null;
    private Date eselonTMT = null;
    
    //
    //
    private boolean styled = false;
    private boolean gorvernor = false;

    private ArrayList<EmployeeCourses> courseses = new ArrayList<EmployeeCourses>();
    private ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();

    public Employee() {
    }

    public Employee(Long index) {
        this.index = index;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getSexAsString() {
        return SEX[sex];
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getFungsionalAsString() {
        return FUNGSIONAL[fungsional];
    }

    public Integer getFungsional() {
        return fungsional;
    }

    public void setFungsional(Integer fungsional) {
        this.fungsional = fungsional;
    }

    public String getGradeAsString() {
        return GRADES[grade];
    }

    public String getPangkatAsString() {
        return PANGKAT[grade];
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStrukturalAsString() {
        return STRUKTURAL[struktural];
    }

    public String getEselonAsString() {
        return ESELON[eselon];
    }

    public String getMaritalAsString() {
        return MARITAL[marital];
    }

    public String getEducationAsString() {
        return EDUCATION[education];
    }

    public Integer getStruktural() {
        return struktural;
    }

    public void setStruktural(Integer struktural) {
        this.struktural = struktural;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public String getPositionNotes() {
        return positionNotes;
    }

    public void setPositionNotes(String positionNotes) {
        this.positionNotes = positionNotes;
    }

    public static ArrayList<String> sexAsList() {
        ArrayList<String> sex = new ArrayList<String>();
        sex.addAll(Arrays.asList(SEX));

        return sex;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Date getCpnsTMT() {
        return cpnsTMT;
    }

    public void setCpnsTMT(Date cpnsTMT) {
        this.cpnsTMT = cpnsTMT;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getEselon() {
        return eselon;
    }

    public void setEselon(Integer eselon) {
        this.eselon = eselon;
    }

    public Date getEselonTMT() {
        return eselonTMT;
    }

    public void setEselonTMT(Date eselonTMT) {
        this.eselonTMT = eselonTMT;
    }

    public Date getGradeTMT() {
        return gradeTMT;
    }

    public void setGradeTMT(Date gradeTMT) {
        this.gradeTMT = gradeTMT;
    }

    public Integer getMarital() {
        return marital;
    }

    public void setMarital(Integer marital) {
        this.marital = marital;
    }

    public Date getPnsTMT() {
        return pnsTMT;
    }

    public void setPnsTMT(Date pnsTMT) {
        this.pnsTMT = pnsTMT;
    }

    public String getSoulmate() {
        return soulmate;
    }

    public void setSoulmate(String soulmate) {
        this.soulmate = soulmate;
    }

    public boolean isGorvernor() {
        return gorvernor;
    }

    public void setGorvernor(boolean gorvernor) {
        this.gorvernor = gorvernor;
    }

    public ArrayList<EmployeeCourses> getCourseses() {
        return courseses;
    }

    public void setCourseses(ArrayList<EmployeeCourses> courseses) {
        this.courseses = courseses;
    }

    public ArrayList<EmployeeFacility> getFacilitys() {
        return facilitys;
    }

    public void setFacilitys(ArrayList<EmployeeFacility> facilitys) {
        this.facilitys = facilitys;
    }

    public static ArrayList<String> gradeAsList() {
        ArrayList<String> grade = new ArrayList<String>();
        grade.addAll(Arrays.asList(GRADES));

        return grade;
    }

    public static ArrayList<String> fungsionalAsList() {
        ArrayList<String> f = new ArrayList<String>();
        f.addAll(Arrays.asList(FUNGSIONAL));

        return f;
    }

    public static ArrayList<String> strukturalAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(STRUKTURAL));

        return str;
    }

    public static ArrayList<String> eselonAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(ESELON));

        return str;
    }

    public static ArrayList<String> maritalAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(MARITAL));

        return str;
    }

    public static ArrayList<Integer> childrenAsList() {
        ArrayList<Integer> intgr = new ArrayList<Integer>();
        intgr.addAll(Arrays.asList(CHILDREN));

        return intgr;
    }

    public static ArrayList<String> educationAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(EDUCATION));

        return str;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            if (isGorvernor()) {
                return "<html><b>" + name + "</b>";
            } else {
                return "<html><b>" + nip + "</b>"
                        + "<br style=\"font-size:40%\"><i>" + name + "</i></br>";
            }
        }
        if (nip.equals("") || (name.equals(""))) {
            return nip + " " + name;
        }
        return "[" + nip + "]  " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.nip == null) ? (other.nip != null) : !this.nip.equals(other.nip)) {
            return false;
        }
        if ((this.birthPlace == null) ? (other.birthPlace != null) : !this.birthPlace.equals(other.birthPlace)) {
            return false;
        }
        if (this.birthDate != other.birthDate && (this.birthDate == null || !this.birthDate.equals(other.birthDate))) {
            return false;
        }
        if (this.sex != other.sex && (this.sex == null || !this.sex.equals(other.sex))) {
            return false;
        }
        if (this.grade != other.grade && (this.grade == null || !this.grade.equals(other.grade))) {
            return false;
        }
        if (this.fungsional != other.fungsional && (this.fungsional == null || !this.fungsional.equals(other.fungsional))) {
            return false;
        }
        if (this.struktural != other.struktural && (this.struktural == null || !this.struktural.equals(other.struktural))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.nip != null ? this.nip.hashCode() : 0);
        hash = 47 * hash + (this.birthPlace != null ? this.birthPlace.hashCode() : 0);
        hash = 47 * hash + (this.birthDate != null ? this.birthDate.hashCode() : 0);
        hash = 47 * hash + (this.sex != null ? this.sex.hashCode() : 0);
        hash = 47 * hash + (this.grade != null ? this.grade.hashCode() : 0);
        hash = 47 * hash + (this.fungsional != null ? this.fungsional.hashCode() : 0);
        hash = 47 * hash + (this.struktural != null ? this.struktural.hashCode() : 0);
        return hash;
    }
}
