package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

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
    //
    public static final String[] GRADES = new String[]{"", "Juru Muda, IA", "Juru Muda Tingkat I, IB",
        "Juru, IC", "Juru Tingkat I, ID",
        "Pengatur Muda, IIA", "Pengatur Muda Tingkat I, IIB", "Pengatur, IIC", "Pengatur Tingkat I, IID",
        "Penata Muda, IIIA", "Penata Muda Tingkat I, IIIB", "Penata, IIIC", "Penata Tingkat I, IIID",
        "Pembina, IVA", "Pembina Tingkat I, IVB", "Pembina Utama Muda, IVC", "Pembina Utama Madya, IVD",
        "Pembina Utama, IVE"};
    //
    public static final String[] RUANG = new String[]{"", "IA", "IB",
        "IC", "ID", "IIA", "IIB", "IIC", "IID",
        "IIIA", "IIIB", "IIIC", "IIID",
        "IVA", "IVB", "IVC", "IVD", "IVE"};
    //
    public static final Integer GRADE_IA = Integer.valueOf(1);
    public static final Integer GRADE_IB = Integer.valueOf(2);
    public static final Integer GRADE_IC = Integer.valueOf(3);
    public static final Integer GRADE_ID = Integer.valueOf(4);
    public static final Integer GRADE_IIA = Integer.valueOf(5);
    public static final Integer GRADE_IIB = Integer.valueOf(6);
    public static final Integer GRADE_IIC = Integer.valueOf(7);
    public static final Integer GRADE_IID = Integer.valueOf(8);
    public static final Integer GRADE_IIIA = Integer.valueOf(9);
    public static final Integer GRADE_IIIB = Integer.valueOf(10);
    public static final Integer GRADE_IIIC = Integer.valueOf(11);
    public static final Integer GRADE_IIID = Integer.valueOf(12);
    public static final Integer GRADE_IVA = Integer.valueOf(13);
    public static final Integer GRADE_IVB = Integer.valueOf(14);
    public static final Integer GRADE_IVC = Integer.valueOf(15);
    public static final Integer GRADE_IVD = Integer.valueOf(16);
    public static final Integer GRADE_IVE = Integer.valueOf(17);
    //
    public static final String[] FUNGSIONAL = new String[]{"", 
        "Dokter Pertama", "Dokter Muda", "Dokter Madya", "Dokter Utama",
        "Dokter Gigi Pertama", "Dokter Gigi Muda", "Dokter Gigi Madya", "Dokter Gigi Utama",
        "Perawat Pertama", "Perawat Muda", "Perawat Madya", 
        "Perawat Pelaksana Pemula", "Perawat Pelaksana", "Perawat Pelaksana Lanjutan", "Perawat Penyelia",
        "Bidan Pelaksana","Bidan Pelaksana Lanjutan","Bidan Penyelia",
        "Perawat Gigi Pelaksana Pemula", "Perawat Gigi Pelaksana", "Perawat Gigi Pelaksana Lanjutan", "Perawat Gigi Pelaksana Penyelia",
        "Apoteker Pertama", "Apoteker Muda", "Apoteker Madya", "Apoteker Utama",
        "Asisten Apoteker Pelaksana Pemula", "Asisten Apoteker Pelaksana", "Asisten Apoteker Pelaksana Lanjutan", "Asisten Apoteker Penyelia",
        "Pranata Labkes Pertama", "Pranata Labkes Muda", "Pranata Labkes Madya", 
        "Pranata Labkes Pelaksana Pemula", "Pranata Labkes Pelaksana", "Pranata Labkes Pelaksana Lanjutan", "Pranata Labkes Penyelia",
        "Sanitarian Pertama", "Sanitarian Muda", "Sanitarian Madya", 
        "Sanitarian Pelaksana Pemula", "Sanitarian Pelaksana", "Sanitarian Pelaksana Lanjutan", "Sanitarian Penyelia",
        "Nutrisionis Pertama", "Nutrisionis Muda", "Nutrisionis Madya", 
        "Nutrisionis Pelaksana", "Nutrisionis Pelaksana Lanjutan", "Nutrisionis Penyelia",
        "Staf"};
    
    
    public static final String[] FUNGSIONAL_GRADE = new String[]{"", 
        "Dokter Pertama, III.a-III.b", "Dokter Muda, III.c-III.d", "Dokter Madya, IV.a-IV.b", "Dokter Utama, IV.c-IV.d",
        "Dokter Gigi Pertama, III.a-III.b", "Dokter Gigi Muda, III.c-III.d", "Dokter Gigi Madya, IV.a-IV.b", "Dokter Gigi Utama, IV.c-IV.d",
        "Perawat Pertama, III.a-III.b", "Perawat Muda, III.c-III.d", "Perawat Madya, IV.a-IV.c", 
        "Perawat Pelaksana Pemula, II.a", "Perawat Pelaksana, II.b-II.d", "Perawat Pelaksana Lanjutan, III.a-III.b", "Perawat Penyelia, III.c-III.d",
        "Bidan Pelaksana, II.b-II.d","Bidan Pelaksana Lanjutan, III.a-III.b","Bidan Penyelia, III.c-III.d",
        "Perawat Gigi Pelaksana Pemula, II.a", "Perawat Gigi Pelaksana, II.b-II.d", "Perawat Gigi Pelaksana Lanjutan, III.a-III.b", "Perawat Gigi Pelaksana Penyelia, III.c-III.d",
        "Apoteker Pertama, III.a-III.b", "Apoteker Muda, III.c-III.d", "Apoteker Madya, IV.a-IV.b", "Apoteker Utama, IV.c-IV.d",
        "Asisten Apoteker Pelaksana Pemula, II.a", "Asisten Apoteker Pelaksana, II.b-II.d", "Asisten Apoteker Pelaksana Lanjutan, III.a-III.b", "Asisten Apoteker Penyelia, III.c-III.d",
        "Pranata Labkes Pertama, III.a-III.b", "Pranata Labkes Muda, III.c-III.d", "Pranata Labkes Madya, IV.a-IV.c", 
        "Pranata Labkes Pelaksana Pemula, II.a", "Pranata Labkes Pelaksana, II.b-II.d", "Pranata Labkes Pelaksana Lanjutan, III.a-III.b", "Pranata Labkes Penyelia, III.c-III.d",
        "Sanitarian Pertama, III.a-III.b", "Sanitarian Muda, III.c-III.d", "Sanitarian Madya, IV.a-IV.c", 
        "Sanitarian Pelaksana Pemula, II.a", "Sanitarian Pelaksana, II.b-II.d", "Sanitarian Pelaksana Lanjutan, III.a-III.b", "Sanitarian Penyelia, III.c-III.d",
        "Nutrisionis Pertama, III.a-III.b", "Nutrisionis Muda, III.c-III.d", "Nutrisionis Madya, IV.a-IV.c", 
        "Nutrisionis Pelaksana, II.b-II.d", "Nutrisionis Pelaksana Lanjutan, III.a-III.b", "Nutrisionis Penyelia, III.c-III.d",
        "Staf"};
    
    //
    public static final String[] STRUKTURAL = new String[]{"", "SEKRETARIS DAERAH",
        "KEPALA DINAS", "KEPALA BADAN", "STAF AHLI",
        "CAMAT", "SEKRETARIS CAMAT",
        "KEPALA BAGIAN",
        "KEPALA BIDANG",
        "KEPALA SEKSI",
        "KEPALA SUB BAGIAN",
        "KEPALA SUB BIDANG",
        "KEPALA SUB SEKSI",
        "SEKRETARIS DINAS"
    };
    //
    public static final String[] EMPLOYEE_STATUS = new String[]{"", "CPNS", "PNS"};
    public static final String[] ESELON = new String[]{"", "IA", "IB",
        "IIA", "IIB", "IIIA", "IIIB", "IVA", "IVB"};
    public static final String[] MARITAL = new String[]{"", "LAJANG", "MENIKAH",
        "DUDA", "JANDA", "CERAI"};
    public static final Integer[] CHILDREN = new Integer[]{0, 1, 2, 3};
    public static final String[] EDUCATION = new String[]{"", "S3", "S2",
        "S1", "D3", "D1", "SMA", "SMK", "SMP", "SD"};
    public static final String[] RELIGION = new String[]{"", "Islam", "Kristen Protestan",
        "Katolik", "Budha", "Hindu", "Konghucu", "Lainnya"};
    public static final String SEKRETARIS_DAERAH = STRUKTURAL[1];
    public static final String KEPALA_DINAS = STRUKTURAL[2];
    public static final String KEPALA_BADAN = STRUKTURAL[3];
    public static final String[] SEX = new String[]{"", "Pria", "Wanita"};
    public static final Integer MALE = 1;
    public static final Integer FEMALE = 2;
    private Long index = Long.valueOf(0);
    private String name = "";
    private String nip = "";
    private String birthPlace = "";
    private Date birthDate = new Date();
    private Integer sex = Integer.valueOf(0);
    private Integer religion = Integer.valueOf(0);
    private Integer marital = Integer.valueOf(0);
    private String soulmate = "";
    private Integer children = Integer.valueOf(0);
    private Integer education = Integer.valueOf(0);
    private String department = "";
    private Integer graduatedYear = Integer.valueOf(0);
    private Integer grade = Integer.valueOf(0);
    private Integer eselon = Integer.valueOf(0);
    private Integer fungsional = Integer.valueOf(0);
    private Integer struktural = Integer.valueOf(0);
    private String positionNotes = "";
    private Integer mkYear = Integer.valueOf(0);
    private Integer mkMonth = Integer.valueOf(0);
    private String mutation = "";
    private Integer employeeStatus = Integer.valueOf(0);
    private Date cpnsTMT = null;
    private Date pnsTMT = null;
    private Date gradeTMT = null;
    private Date eselonTMT = null;
    private byte[] picture = null;
    //
    //
    private boolean styled = false;
    private boolean justName = false;
    private boolean gorvernor = false;
    private ArrayList<CurriculumVitae> curriculumVitae = new ArrayList<CurriculumVitae>();
    private ArrayList<EmployeeCourses> courseses = new ArrayList<EmployeeCourses>();
    private ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();
    
    private boolean selected = false;
    
    //
    
    private Unit unit = null;

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

    public Integer getReligion() {
        return religion;
    }

    public void setReligion(Integer religion) {
        this.religion = religion;
    }

    public String getReligionAsString() {
        return RELIGION[religion];
    }

    public static ArrayList<String> religionAsList() {
        ArrayList<String> religion = new ArrayList<String>();
        religion.addAll(Arrays.asList(RELIGION));

        return religion;
    }

    public String getFungsionalAsString() {
        return FUNGSIONAL[fungsional];
    }
    
    public String getFungsionalGradeAsString() {
        return FUNGSIONAL_GRADE[fungsional];
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

    public String getRuangAsString() {
        return RUANG[grade];
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

    public String getMutation() {
        return mutation;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    public void setPositionNotes(String positionNotes) {
        this.positionNotes = positionNotes;
    }

    public static ArrayList<String> sexAsList() {
        ArrayList<String> sex = new ArrayList<String>();
        sex.addAll(Arrays.asList(SEX));

        return sex;
    }

    public boolean isGorvernor() {
        return gorvernor;
    }

    public void setGorvernor(boolean gorvernor) {
        this.gorvernor = gorvernor;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public ArrayList<EmployeeCourses> getCourseses() {
        return courseses;
    }

    public void setCourseses(ArrayList<EmployeeCourses> courseses) {
        this.courseses = courseses;
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

    public ArrayList<EmployeeFacility> getFacilitys() {
        return facilitys;
    }

    public void setFacilitys(ArrayList<EmployeeFacility> facilitys) {
        this.facilitys = facilitys;
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

    public ArrayList<CurriculumVitae> getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(ArrayList<CurriculumVitae> curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getGraduatedYear() {
        return graduatedYear;
    }

    public void setGraduatedYear(Integer graduatedYear) {
        this.graduatedYear = graduatedYear;
    }

    public Integer getMkMonth() {
        return mkMonth;
    }

    public void setMkMonth(Integer mkMonth) {
        this.mkMonth = mkMonth;
    }

    public Integer getMkYear() {
        return mkYear;
    }

    public void setMkYear(Integer mkYear) {
        this.mkYear = mkYear;
    }

    public Integer getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Integer employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeStatusAsString() {
        return EMPLOYEE_STATUS[employeeStatus];
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
    
    public static ArrayList<String> fungsionalGradeAsList() {
        ArrayList<String> f = new ArrayList<String>();
        f.addAll(Arrays.asList(FUNGSIONAL_GRADE));

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

    public static ArrayList<String> employeeStatusAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(EMPLOYEE_STATUS));

        return str;
    }

    public boolean isJustName() {
        return justName;
    }

    public void setJustName(boolean justName) {
        this.justName = justName;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public String toString() {

        if (isJustName()) {
            return name;
        }

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
