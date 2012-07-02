package org.motekar.project.civics.archieve.master.sqlapi;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.objects.*;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class MasterSQL extends CommonSQL {

    void insertEmployee(Connection conn, Employee employee) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into employee(employeename,nip,birthplace,").
                append("birthdate,sex,religion,workforce,grade,fungsional,struktural,positionnotes,isgorvernor,").
                append("marital,soulmate,children,education,department,eselon,cpnstmt,pnstmt,").
                append("gradetmt,eselontmt,isnonemployee,mkyear,mkmonth)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, employee.getName());
        pstm.setString(++col, employee.getNip());
        pstm.setString(++col, employee.getBirthPlace());

        if (employee.getBirthDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getBirthDate().getTime()));

        }

        pstm.setInt(++col, employee.getSex());
        pstm.setInt(++col, employee.getReligion());
        pstm.setInt(++col, employee.getWorkforce());
        pstm.setInt(++col, employee.getGrade());
        pstm.setInt(++col, employee.getFungsional());
        pstm.setInt(++col, employee.getStruktural());
        pstm.setString(++col, employee.getPositionNotes());
        pstm.setBoolean(++col, employee.isGorvernor());

        pstm.setInt(++col, employee.getMarital());
        pstm.setString(++col, employee.getSoulmate());
        pstm.setInt(++col, employee.getChildren());
        pstm.setInt(++col, employee.getEducation());
        pstm.setString(++col, employee.getDepartment());
        pstm.setInt(++col, employee.getEselon());

        if (employee.getCpnsTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getCpnsTMT().getTime()));

        }

        if (employee.getPnsTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getPnsTMT().getTime()));

        }

        if (employee.getGradeTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getGradeTMT().getTime()));

        }

        if (employee.getEselonTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getEselonTMT().getTime()));

        }
        
        pstm.setBoolean(++col, employee.isNonEmployee());
        pstm.setInt(++col, employee.getMkYear());
        pstm.setInt(++col, employee.getMkMonth());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateEmployee(Connection conn, Long oldIndex, Employee employee) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update employee set employeename = ?, nip = ?, birthplace = ?, ").
                append("birthdate = ?, sex = ?,religion = ?,workforce = ?, grade = ?, ").
                append("fungsional = ?, struktural = ?, positionnotes = ?,isgorvernor = ?, ").
                append("marital = ?, soulmate = ?, children = ?,education = ?, department = ?,").
                append("eselon = ?, cpnstmt = ?, pnstmt = ?, gradetmt = ?, ").
                append("eselontmt = ?, isnonemployee = ?, mkyear = ?, mkmonth = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, employee.getName());
        pstm.setString(++col, employee.getNip());
        pstm.setString(++col, employee.getBirthPlace());
        if (employee.getBirthDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getBirthDate().getTime()));

        }
        pstm.setInt(++col, employee.getSex());
        pstm.setInt(++col, employee.getReligion());
        pstm.setInt(++col, employee.getWorkforce());
        pstm.setInt(++col, employee.getGrade());
        pstm.setInt(++col, employee.getFungsional());
        pstm.setInt(++col, employee.getStruktural());
        pstm.setString(++col, employee.getPositionNotes());
        pstm.setBoolean(++col, employee.isGorvernor());

        pstm.setInt(++col, employee.getMarital());
        pstm.setString(++col, employee.getSoulmate());
        pstm.setInt(++col, employee.getChildren());
        pstm.setInt(++col, employee.getEducation());
        pstm.setString(++col, employee.getDepartment());
        pstm.setInt(++col, employee.getEselon());

        if (employee.getCpnsTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getCpnsTMT().getTime()));

        }

        if (employee.getPnsTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getPnsTMT().getTime()));

        }

        if (employee.getGradeTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getGradeTMT().getTime()));

        }

        if (employee.getEselonTMT() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(employee.getEselonTMT().getTime()));

        }
        
        pstm.setBoolean(++col, employee.isNonEmployee());
        pstm.setInt(++col, employee.getMkYear());
        pstm.setInt(++col, employee.getMkMonth());
        
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteEmployee(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from employee where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    ArrayList<Employee> getEmployee(Connection conn,boolean isNonEmployee) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where isnonemployee = ? ").
                append("order by employeename");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setBoolean(1, isNonEmployee);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Employee employee = new Employee();
            employee.setIndex(rs.getLong("autoindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setReligion(rs.getInt("religion"));
            employee.setWorkforce(rs.getInt("workforce"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));
            employee.setPositionNotes(rs.getString("positionnotes"));
            employee.setGorvernor(rs.getBoolean("isgorvernor"));

            employee.setMarital(rs.getInt("marital"));
            employee.setSoulmate(rs.getString("soulmate"));
            employee.setChildren(rs.getInt("children"));
            employee.setEducation(rs.getInt("education"));
            employee.setDepartment(rs.getString("department"));
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            
            employee.setNonEmployee(rs.getBoolean("isnonemployee"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }

    ArrayList<Employee> getCommanderEmployee(Connection conn) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where struktural > 0 ").
                append("or isgorvernor = true ").
                append("order by employeename");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Employee employee = new Employee();
            employee.setIndex(rs.getLong("autoindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setReligion(rs.getInt("religion"));
            employee.setWorkforce(rs.getInt("workforce"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));
            employee.setPositionNotes(rs.getString("positionnotes"));
            employee.setGorvernor(rs.getBoolean("isgorvernor"));

            employee.setMarital(rs.getInt("marital"));
            employee.setSoulmate(rs.getString("soulmate"));
            employee.setChildren(rs.getInt("children"));
            employee.setEducation(rs.getInt("education"));
            employee.setDepartment(rs.getString("department"));
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            employee.setNonEmployee(rs.getBoolean("isnonemployee"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }


    ArrayList<Employee> getAssignedEmployee(Connection conn) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where isgorvernor = false ").
                append("order by employeename");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Employee employee = new Employee();
            employee.setIndex(rs.getLong("autoindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setReligion(rs.getInt("religion"));
            employee.setWorkforce(rs.getInt("workforce"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));
            employee.setPositionNotes(rs.getString("positionnotes"));
            employee.setGorvernor(rs.getBoolean("isgorvernor"));

            employee.setMarital(rs.getInt("marital"));
            employee.setSoulmate(rs.getString("soulmate"));
            employee.setChildren(rs.getInt("children"));
            employee.setEducation(rs.getInt("education"));
            employee.setDepartment(rs.getString("department"));
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            employee.setNonEmployee(rs.getBoolean("isnonemployee"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }

    public Employee getEmployeeByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from employee where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        Employee employee = null;
        while (rs.next()) {
            employee = new Employee();
            employee.setIndex(rs.getLong("autoindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setReligion(rs.getInt("religion"));
            employee.setWorkforce(rs.getInt("workforce"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));
            employee.setPositionNotes(rs.getString("positionnotes"));
            employee.setGorvernor(rs.getBoolean("isgorvernor"));

            employee.setMarital(rs.getInt("marital"));
            employee.setSoulmate(rs.getString("soulmate"));
            employee.setChildren(rs.getInt("children"));
            employee.setEducation(rs.getInt("education"));
            employee.setDepartment(rs.getString("department"));
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            employee.setNonEmployee(rs.getBoolean("isnonemployee"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(false);

        }

        rs.close();
        pstm.close();

        return employee;
    }


    boolean getEmployeeInExpeditionCheque(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where (autoindex in ").
                append("(select clerk from expeditioncheque) ").
                append("or autoindex in ").
                append("(select heademployee from expeditioncheque) ").
                append("or autoindex in ").
                append("(select pptk from expeditioncheque) ").
                append("or autoindex in ").
                append("(select paidto from expeditioncheque)) ").
                append("and autoindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
        }

        rs.close();
        pstm.close();

        return found;
    }

    void insertEmployeeCourses(Connection conn, EmployeeCourses courses) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into employeecourses(employeeindex,courses,attending)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, courses.getParentIndex());
        pstm.setInt(++col, courses.getCourses());
        pstm.setBoolean(++col, courses.isAttending());
        

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteEmployeeCourses(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from employeecourses where employeeindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<EmployeeCourses> getEmployeeCourses(Connection conn, Long index) throws SQLException {
        ArrayList<EmployeeCourses> courseses = new ArrayList<EmployeeCourses>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employeecourses ").
                append("where employeeindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            EmployeeCourses courses = new EmployeeCourses();
            courses.setParentIndex(rs.getLong("employeeindex"));
            courses.setCourses(rs.getInt("courses"));
            courses.setAttending(rs.getBoolean("attending"));
            

            courseses.add(courses);
        }

        rs.close();
        pstm.close();

        return courseses;
    }

    void insertEmployeeFacility(Connection conn, EmployeeFacility facility) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into employeefacility(employeeindex,facility,owned)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, facility.getParentIndex());
        pstm.setInt(++col, facility.getFacility());
        pstm.setBoolean(++col, facility.isOwned());


        pstm.executeUpdate();
        pstm.close();
    }

    void deleteEmployeeFacility(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from employeefacility where employeeindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<EmployeeFacility> getEmployeeFacility(Connection conn, Long index) throws SQLException {
        ArrayList<EmployeeFacility> facilitys = new ArrayList<EmployeeFacility>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employeefacility ").
                append("where employeeindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            EmployeeFacility facility = new EmployeeFacility();
            facility.setParentIndex(rs.getLong("employeeindex"));
            facility.setFacility(rs.getInt("facility"));
            facility.setOwned(rs.getBoolean("owned"));

            facilitys.add(facility);
        }

        rs.close();
        pstm.close();

        return facilitys;
    }

    void insertStandardPrice(Connection conn, StandardPrice price) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into standardprice(transactionname,esselon,transactiontype,transporttype, ").
                append("departure,destination,price,notes)").
                append("values(?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        
        int col = 0;
        
        pstm.setString(++col, price.getTransactionName());
        pstm.setInt(++col, price.getEselon());
        pstm.setInt(++col, price.getTransactionType());
        pstm.setInt(++col, price.getTransportType());
        pstm.setString(++col, price.getDeparture());
        pstm.setString(++col, price.getDestination());
        pstm.setBigDecimal(++col, price.getPrice());
        pstm.setString(++col, price.getNotes());
        pstm.executeUpdate();
        pstm.close();
    }

    void updateStandardPrice(Connection conn, Long oldIndex, StandardPrice price) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update standardprice set transactionname = ?,esselon = ?, transactiontype = ?, ").
                append("transporttype = ?, departure = ?, destination = ?, ").
                append("price = ?, notes = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;
        
        pstm.setString(++col, price.getTransactionName());
        pstm.setInt(++col, price.getEselon());
        pstm.setInt(++col, price.getTransactionType());
        pstm.setInt(++col, price.getTransportType());
        pstm.setString(++col, price.getDeparture());
        pstm.setString(++col, price.getDestination());
        pstm.setBigDecimal(++col, price.getPrice());
        pstm.setString(++col, price.getNotes());
        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteStandardPrice(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from standardprice where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<StandardPrice> getStandardPrice(Connection conn) throws SQLException {
        ArrayList<StandardPrice> prices = new ArrayList<StandardPrice>();

        StringBuilder query = new StringBuilder();
        query.append("select * from standardprice ").
                append("order by transactionname");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            StandardPrice price = new StandardPrice();
            price.setIndex(rs.getLong("autoindex"));
            price.setEselon(rs.getInt("esselon"));
            price.setTransactionName(rs.getString("transactionname"));
            price.setTransactionType(rs.getInt("transactiontype"));
            price.setTransportType(rs.getInt("transporttype"));
            price.setDeparture(rs.getString("departure"));
            price.setDestination(rs.getString("destination"));
            price.setPrice(rs.getBigDecimal("price"));
            price.setNotes(rs.getString("notes"));

            price.setStyled(true);

            prices.add(price);
        }

        rs.close();
        pstm.close();

        return prices;
    }
    
    ArrayList<StandardPrice> getStandardPrice(Connection conn,Activity activity) throws SQLException {
        ArrayList<StandardPrice> prices = new ArrayList<StandardPrice>();

        StringBuilder query = new StringBuilder();
        query.append("select * from standardprice ").
                append("where esselon in ").
                append("(select eselon from budgetsubdetailchild ").
                append(" where subdetailindex in  ").
                append(" (select autoindex from budgetsubdetail ").
                append(" where activityindex = ?)) ").
                append("order by transactionname");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, activity.getIndex());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            StandardPrice price = new StandardPrice();
            price.setIndex(rs.getLong("autoindex"));
            price.setEselon(rs.getInt("esselon"));
            price.setTransactionName(rs.getString("transactionname"));
            price.setTransactionType(rs.getInt("transactiontype"));
            price.setTransportType(rs.getInt("transporttype"));
            price.setDeparture(rs.getString("departure"));
            price.setDestination(rs.getString("destination"));
            price.setPrice(rs.getBigDecimal("price"));
            price.setNotes(rs.getString("notes"));

            price.setStyled(true);

            prices.add(price);
        }

        rs.close();
        pstm.close();

        return prices;
    }

    public StandardPrice getStandardPriceByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from standardprice ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        StandardPrice price = null;
        while (rs.next()) {
            price = new StandardPrice();
            price.setIndex(rs.getLong("autoindex"));
            price.setTransactionName(rs.getString("transactionname"));
            price.setEselon(rs.getInt("esselon"));
            price.setTransactionType(rs.getInt("transactiontype"));
            price.setTransportType(rs.getInt("transporttype"));
            price.setDeparture(rs.getString("departure"));
            price.setDestination(rs.getString("destination"));
            price.setPrice(rs.getBigDecimal("price"));
            price.setNotes(rs.getString("notes"));

            price.setStyled(true);
        }

        rs.close();
        pstm.close();

        return price;
    }

    void insertDivision(Connection conn, Division division) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into division(code,divisionname)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, division.getCode());
        pstm.setString(++col, division.getName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateDivision(Connection conn, String oldCode, Division division) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update division set code = ?, divisionname = ?").
                append("where code = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, division.getCode());
        pstm.setString(++col, division.getName());
        pstm.setString(++col, oldCode);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteDivision(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from division where code = ?");
        pstm.setString(1, code);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Division> getDivision(Connection conn, boolean isStyled) throws SQLException {
        ArrayList<Division> divisions = new ArrayList<Division>();

        StringBuilder query = new StringBuilder();
        query.append("select * from division ").
                append("order by code");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Division division = new Division();
            division.setCode(rs.getString("code"));
            division.setName(rs.getString("divisionname"));

            division.setStyled(isStyled);

            divisions.add(division);
        }

        rs.close();
        pstm.close();

        return divisions;
    }

    void insertAccount(Connection conn, Account account) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into account(accountcode,accountname,accountcategory,").
                append("accounttype,accountgroup)").
                append("values(?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, account.getAccountCode());
        pstm.setString(++col, account.getAccountName());
        pstm.setInt(++col, account.getAccountCategory());
        pstm.setInt(++col, account.getAccountType());
        pstm.setBoolean(++col, account.isAccountGroup());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateAccount(Connection conn, Long oldIndex,Account account) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update account set accountcode = ?, accountname = ?, accountcategory = ?, ").
                append("accounttype = ?, accountgroup = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, account.getAccountCode());
        pstm.setString(++col, account.getAccountName());
        pstm.setInt(++col, account.getAccountCategory());
        pstm.setInt(++col, account.getAccountType());
        pstm.setBoolean(++col, account.isAccountGroup());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteAccount(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from account where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteAccount(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from account where accountcode like '"+code+"%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Account> getAccount(Connection conn) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<Account>();

        StringBuilder query = new StringBuilder();
        query.append("select acc.*, coalesce(superaccount,0) superaccount from account acc ").
                append("left join accountstructure struct on acc.autoindex = subaccount ").
                append("order by acc.accountcode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Account account = new Account();
            account.setIndex(rs.getLong("autoindex"));
            account.setAccountCode(rs.getString("accountcode"));
            account.setAccountName(rs.getString("accountname"));
            account.setAccountCategory(rs.getInt("accountcategory"));
            account.setAccountType(rs.getInt("accounttype"));
            account.setAccountGroup(rs.getBoolean("accountgroup"));
            account.setParentIndex(rs.getLong("superaccount"));

            accounts.add(account);
        }

        rs.close();
        pstm.close();

        return accounts;
    }

    public Account getAccountByIndex(Connection conn,Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from account ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        Account account = null;

        while (rs.next()) {
            account = new Account();
            account.setIndex(rs.getLong("autoindex"));
            account.setAccountCode(rs.getString("accountcode"));
            account.setAccountName(rs.getString("accountname"));
            account.setAccountCategory(rs.getInt("accountcategory"));
            account.setAccountType(rs.getInt("accounttype"));
            account.setAccountGroup(rs.getBoolean("accountgroup"));

        }

        rs.close();
        pstm.close();

        return account;
    }

    public boolean getAccountInExpedition(Connection conn,String code) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from account ").
                append("where autoindex in ").
                append("(select chargebudget from expedition) ").
                append("and accountcode like '").
                append(code).append("%'");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        boolean found = false;

        while (rs.next()) {
            found = true;

        }

        rs.close();
        pstm.close();

        return found;
    }

    void insertAccountStructure(Connection conn, Account parent,Account child) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into accountstructure(superaccount,subaccount)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, parent.getIndex());
        pstm.setLong(++col, child.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteAccountStructureByChild(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from accountstructure where superindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }


    void insertProgram(Connection conn, Program program) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into program(programcode,programname)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, program.getProgramCode());
        pstm.setString(++col, program.getProgramName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateProgram(Connection conn, Long oldIndex,Program program) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update program set programcode = ?, programname = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, program.getProgramCode());
        pstm.setString(++col, program.getProgramName());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteProgram(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from program where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Program> getProgram(Connection conn) throws SQLException {
        ArrayList<Program> programs = new ArrayList<Program>();

        StringBuilder query = new StringBuilder();
        query.append("select * from program");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Program program = new Program();
            program.setIndex(rs.getLong("autoindex"));
            program.setProgramCode(rs.getString("programcode"));
            program.setProgramName(rs.getString("programname"));
            
            programs.add(program);
        }

        rs.close();
        pstm.close();

        return programs;
    }

    ArrayList<Program> getProgramNotInBudgetDetail(Connection conn,Integer years,Integer budgetType) throws SQLException {
        ArrayList<Program> programs = new ArrayList<Program>();

        StringBuilder query = new StringBuilder();
        query.append("select * from program ").
                append("where autoindex not in ").
                append("(select programindex from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?))").
                append(" order by programcode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, years);
        pstm.setInt(2, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Program program = new Program();
            program.setIndex(rs.getLong("autoindex"));
            program.setProgramCode(rs.getString("programcode"));
            program.setProgramName(rs.getString("programname"));

            programs.add(program);
        }

        rs.close();
        pstm.close();

        return programs;
    }


    public Program getProgramByIndex(Connection conn,Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from program ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        Program program = null;
        while (rs.next()) {
            program = new Program();
            program.setIndex(rs.getLong("autoindex"));
            program.setProgramCode(rs.getString("programcode"));
            program.setProgramName(rs.getString("programname"));

        }

        rs.close();
        pstm.close();

        return program;
    }

    boolean getProgramInExpedition(Connection conn,String code) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from program ").
                append("where autoindex in ").
                append("(select program from expedition) ").
                append("and programcode like '").
                append(code).append("%'");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
        }

        rs.close();
        pstm.close();

        return found;
    }

    void insertActivity(Connection conn, Activity activity) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into activity(programindex,activitycode,activityname)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, activity.getProgramIndex());
        pstm.setString(++col, activity.getActivityCode());
        pstm.setString(++col, activity.getActivityName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateActivity(Connection conn, Long oldIndex,Activity activity) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update activity set programindex = ?,activitycode = ?, activityname = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, activity.getProgramIndex());
        pstm.setString(++col, activity.getActivityCode());
        pstm.setString(++col, activity.getActivityName());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteActivity(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from activity where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteActivityByProgram(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from activity where programindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Activity> getActivity(Connection conn) throws SQLException {
        ArrayList<Activity> activitys = new ArrayList<Activity>();

        StringBuilder query = new StringBuilder();
        query.append("select * from activity ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Activity activity = new Activity();
            activity.setIndex(rs.getLong("autoindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            activity.setProgramIndex(rs.getLong("programindex"));

            activitys.add(activity);
        }

        rs.close();
        pstm.close();

        return activitys;
    }

    ArrayList<Activity> getActivity(Connection conn,Long programIndex) throws SQLException {
        ArrayList<Activity> activitys = new ArrayList<Activity>();

        StringBuilder query = new StringBuilder();
        query.append("select * from activity ").
                append("where programindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, programIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Activity activity = new Activity();
            activity.setIndex(rs.getLong("autoindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            activity.setProgramIndex(rs.getLong("programindex"));

            activitys.add(activity);
        }

        rs.close();
        pstm.close();

        return activitys;
    }

    ArrayList<Activity> getActivityNotInBudgetSubDetail(Connection conn,Integer years,Integer budgetType) throws SQLException {
        ArrayList<Activity> activitys = new ArrayList<Activity>();

        StringBuilder query = new StringBuilder();
        query.append("select * from activity ").
                append("where autoindex not in ").
                append("(select activityindex from budgetsubdetail ").
                append("where detailindex in ").
                append("(select autoindex from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?)))").
                append(" order by activitycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, years);
        pstm.setInt(2, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Activity activity = new Activity();
            activity.setIndex(rs.getLong("autoindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            activity.setProgramIndex(rs.getLong("programindex"));

            activitys.add(activity);
        }

        rs.close();
        pstm.close();

        return activitys;
    }

    public Activity getActivityByIndex(Connection conn,Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from activity ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        Activity activity = null;
        while (rs.next()) {
            activity = new Activity();
            activity.setIndex(rs.getLong("autoindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            activity.setProgramIndex(rs.getLong("programindex"));
        }

        rs.close();
        pstm.close();

        return activity;
    }


    public boolean getActivityInExpedition(Connection conn,String code) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from activity ").
                append("where autoindex in ").
                append("(select activity from expedition) ").
                append("and activitycode like '").
                append(code).append("%'");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = true;
        }

        rs.close();
        pstm.close();

        return found;
    }

    void insertBudget(Connection conn, Budget budget) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into budget(years,budgettype)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setInt(++col, budget.getYears());
        pstm.setInt(++col, budget.getBudgetType());
       
        pstm.executeUpdate();
        pstm.close();
    }

    void updateBudget(Connection conn, Budget oldBudget, Budget newBudget) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update budget set years = ?, budgettype = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setInt(++col, newBudget.getYears());
        pstm.setInt(++col, newBudget.getBudgetType());
        
        pstm.setLong(++col, oldBudget.getIndex());
        
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteBudget(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from budget where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Budget> getBudget(Connection conn) throws SQLException {
        ArrayList<Budget> budgets = new ArrayList<Budget>();

        StringBuilder query = new StringBuilder();
        query.append("select * from budget ").
                append("order by years");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Budget budget = new Budget();
            budget.setIndex(rs.getLong("autoindex"));
            budget.setYears(rs.getInt("years"));
            budget.setBudgetType(rs.getInt("budgettype"));

            budgets.add(budget);
        }

        rs.close();
        pstm.close();

        return budgets;
    }

    Budget getBudget(Connection conn,Integer years, Integer budgetType) throws SQLException {
        Budget budget = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from budget ").
                append("where years = ? ").
                append("and budgettype = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, years);
        pstm.setInt(2, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            budget = new Budget();
            budget.setIndex(rs.getLong("autoindex"));
            budget.setYears(rs.getInt("years"));
            budget.setBudgetType(rs.getInt("budgettype"));
        }

        rs.close();
        pstm.close();

        return budget;
    }

    void insertBudgetDetail(Connection conn, BudgetDetail detail) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into budgetdetail(budgetindex,programindex)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, detail.getParentIndex());
        pstm.setLong(++col, detail.getProgram().getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateBudgetDetail(Connection conn, BudgetDetail oldDetail, BudgetDetail newDetail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update budgetdetail set budgetindex = ?, programindex = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, newDetail.getParentIndex());
        pstm.setLong(++col, newDetail.getProgram().getIndex());

        pstm.setLong(++col, oldDetail.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteBudgetDetail(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from budgetdetail where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<BudgetDetail> getBudgetDetail(Connection conn,Long parentIndex) throws SQLException {
        ArrayList<BudgetDetail> details = new ArrayList<BudgetDetail>();

        StringBuilder query = new StringBuilder();
        query.append("select bd.*,p.programcode,programname from budgetdetail bd").
                append("inner join program p ").
                append("on p.autoindex = bd.programindex ").
                append("where budgetindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetDetail detail = new BudgetDetail();
            detail.setIndex(rs.getLong("autoindex"));
            detail.setParentIndex(parentIndex);

            Program program = new Program();
            program.setIndex(rs.getLong("programindex"));
            program.setProgramCode(rs.getString("programcode"));
            program.setProgramName(rs.getString("programname"));
            detail.setProgram(program);

            details.add(detail);
        }

        rs.close();
        pstm.close();

        return details;
    }

    ArrayList<BudgetDetail> getBudgetDetail(Connection conn,Integer year,Integer budgetType) throws SQLException {
        ArrayList<BudgetDetail> details = new ArrayList<BudgetDetail>();

        StringBuilder query = new StringBuilder();
        query.append("select bd.*,p.programcode,programname from budgetdetail bd ").
                append("inner join program p ").
                append("on p.autoindex = bd.programindex ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?)").
                append(" order by programcode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, year);
        pstm.setInt(2, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetDetail detail = new BudgetDetail();
            detail.setIndex(rs.getLong("autoindex"));
            detail.setParentIndex(rs.getLong("budgetindex"));

            Program program = new Program();
            program.setIndex(rs.getLong("programindex"));
            program.setProgramCode(rs.getString("programcode"));
            program.setProgramName(rs.getString("programname"));
            detail.setProgram(program);

            details.add(detail);
        }

        rs.close();
        pstm.close();

        return details;
    }

    void insertBudgetSubDetail(Connection conn, BudgetSubDetail subDetail) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into budgetsubdetail(detailindex,activityindex,amount)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, subDetail.getParentIndex());
        pstm.setLong(++col, subDetail.getActivity().getIndex());
        pstm.setBigDecimal(++col, subDetail.getAmount());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateBudgetSubDetail(Connection conn, BudgetSubDetail oldSubDetail, BudgetSubDetail newSubDetail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update budgetsubdetail set detailindex = ?, activityindex = ?, amount = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, newSubDetail.getParentIndex());
        pstm.setLong(++col, newSubDetail.getActivity().getIndex());
        pstm.setBigDecimal(++col, newSubDetail.getAmount());

        pstm.setLong(++col, oldSubDetail.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteBudgetSubDetail(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from budgetsubdetail where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<BudgetSubDetail> getBudgetSubDetail(Connection conn,Long parentIndex) throws SQLException {
        ArrayList<BudgetSubDetail> subDetails = new ArrayList<BudgetSubDetail>();

        StringBuilder query = new StringBuilder();
        query.append("select bsd.*,activitycode,activityname from budgetsubdetail bsd ").
                append("inner join activity a ").
                append("on a.autoindex =  bsd.activityindex ").
                append("where detailindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetSubDetail subDetail = new BudgetSubDetail();
            subDetail.setIndex(rs.getLong("autoindex"));
            subDetail.setParentIndex(parentIndex);
            subDetail.setAmount(rs.getBigDecimal("amount"));

            Activity activity = new Activity();
            activity.setIndex(rs.getLong("activityindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            subDetail.setActivity(activity);


            subDetails.add(subDetail);
        }

        rs.close();
        pstm.close();

        return subDetails;
    }

    ArrayList<BudgetSubDetail> getBudgetSubDetail(Connection conn,Integer years,Integer budgetType) throws SQLException {
        ArrayList<BudgetSubDetail> subDetails = new ArrayList<BudgetSubDetail>();

        StringBuilder query = new StringBuilder();
        query.append("select bsd.*,activitycode,activityname from budgetsubdetail bsd ").
                append("inner join activity a ").
                append("on a.autoindex =  bsd.activityindex ").
                append("where detailindex in ").
                append("(select autoindex from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?))").
                append(" order by activitycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, years);
        pstm.setInt(2, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetSubDetail subDetail = new BudgetSubDetail();
            subDetail.setIndex(rs.getLong("autoindex"));
            subDetail.setParentIndex(rs.getLong("detailindex"));
            subDetail.setAmount(rs.getBigDecimal("amount"));

            Activity activity = new Activity();
            activity.setIndex(rs.getLong("activityindex"));
            activity.setActivityCode(rs.getString("activitycode"));
            activity.setActivityName(rs.getString("activityname"));
            subDetail.setActivity(activity);


            subDetails.add(subDetail);
        }

        rs.close();
        pstm.close();

        return subDetails;
    }

    BigDecimal getBudgetAmount(Connection conn,Long activityIndex,Integer years,Integer budgetType) throws SQLException {
        BigDecimal amount = BigDecimal.ZERO;

        StringBuilder query = new StringBuilder();
        query.append("select coalesce(sum(amount),0) as amount from budgetsubdetail bd ").
                append("where activityindex = ? ").
                append("and detailindex in ").
                append("(select autoindex from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?))");



        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, activityIndex);
        pstm.setInt(2, years);
        pstm.setInt(3, budgetType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            amount = rs.getBigDecimal("amount");
        }

        rs.close();
        pstm.close();

        return amount;
    }
    
    void insertBudgetSubDetailChild(Connection conn, BudgetSubDetailChild subDetailChild) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into budgetsubdetailchild(subdetailindex, description,eselon, counted, units, amount)").
                append("values(?, ?, ?, ?, ? ,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, subDetailChild.getParentIndex());
        pstm.setString(++col, subDetailChild.getDescription());
        pstm.setInt(++col, subDetailChild.getEselon());
        pstm.setInt(++col, subDetailChild.getCounted());
        pstm.setString(++col, subDetailChild.getUnits());
        pstm.setBigDecimal(++col, subDetailChild.getAmount());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteBudgetSubDetailChild(Connection conn, BudgetSubDetail subDetail) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from budgetsubdetailchild where subdetailindex = ?");
        pstm.setLong(1, subDetail.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<BudgetSubDetailChild> getBudgetSubDetailChild(Connection conn, Long parentIndex) throws SQLException {
        ArrayList<BudgetSubDetailChild> subDetailChilds = new ArrayList<BudgetSubDetailChild>();

        StringBuilder query = new StringBuilder();
        query.append("select * from budgetsubdetailchild ").
                append("where subdetailindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetSubDetailChild subDetailChild = new BudgetSubDetailChild();
            subDetailChild.setParentIndex(parentIndex);
            subDetailChild.setDescription(rs.getString("description"));
            subDetailChild.setEselon(rs.getInt("eselon"));
            subDetailChild.setCounted(rs.getInt("counted"));
            subDetailChild.setUnits(rs.getString("units"));
            subDetailChild.setAmount(rs.getBigDecimal("amount"));


            subDetailChilds.add(subDetailChild);
        }

        rs.close();
        pstm.close();

        return subDetailChilds;
    }
}
