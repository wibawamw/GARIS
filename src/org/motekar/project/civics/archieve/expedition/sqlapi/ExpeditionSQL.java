package org.motekar.project.civics.archieve.expedition.sqlapi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionCheque;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionCost;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionFollower;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionJournal;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionResult;
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionSQL extends CommonSQL {

    void insertExpedition(Connection conn, Expedition expedition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expedition(letterindex,docnumber,assignedemployee,").
                append("departure,destination,transportation,startdate,enddate,").
                append("notes, approvalplace, approvaldate, charge,program,activity,chargebudget,unit)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, expedition.getLetter().getIndex());
        pstm.setString(++col, expedition.getDocumentNumber());
        pstm.setLong(++col, expedition.getAssignedEmployee().getIndex());
        pstm.setString(++col, expedition.getDeparture());
        pstm.setString(++col, expedition.getDestination());
        pstm.setInt(++col, expedition.getTransportation());
        pstm.setDate(++col, new java.sql.Date(expedition.getStartDate().getTime()));
        pstm.setDate(++col, new java.sql.Date(expedition.getEndDate().getTime()));
        pstm.setString(++col, expedition.getNotes());
        pstm.setString(++col, expedition.getApprovalPlace());
        pstm.setDate(++col, new java.sql.Date(expedition.getApprovalDate().getTime()));
        pstm.setString(++col, expedition.getCharge());

        if (expedition.getProgram() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getProgram().getIndex());
        }

        if (expedition.getActivity() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getActivity().getIndex());
        }

        if (expedition.getAccount() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getAccount().getIndex());
        }
        
        if (expedition.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getUnit().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateExpedition(Connection conn, Long oldIndex, Expedition expedition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update expedition set letterindex = ?, docnumber = ?, assignedemployee = ?, ").
                append("departure = ?, destination = ?, transportation = ?, startdate = ?, enddate = ?,").
                append("notes = ?, approvalplace = ?, approvaldate = ?, charge = ?, ").
                append("program = ?, activity = ?, chargebudget = ?, unit = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, expedition.getLetter().getIndex());
        pstm.setString(++col, expedition.getDocumentNumber());
        pstm.setLong(++col, expedition.getAssignedEmployee().getIndex());
        pstm.setString(++col, expedition.getDeparture());
        pstm.setString(++col, expedition.getDestination());
        pstm.setInt(++col, expedition.getTransportation());
        pstm.setDate(++col, new java.sql.Date(expedition.getStartDate().getTime()));
        pstm.setDate(++col, new java.sql.Date(expedition.getEndDate().getTime()));
        pstm.setString(++col, expedition.getNotes());
        pstm.setString(++col, expedition.getApprovalPlace());
        pstm.setDate(++col, new java.sql.Date(expedition.getApprovalDate().getTime()));
        pstm.setString(++col, expedition.getCharge());

        if (expedition.getProgram() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getProgram().getIndex());
        }

        if (expedition.getActivity() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getActivity().getIndex());
        }

        if (expedition.getAccount() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getAccount().getIndex());
        }
        
        if (expedition.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, expedition.getUnit().getIndex());
        }
        
        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpedition(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expedition where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Expedition> getExpedition(Connection conn,String modifier) throws SQLException {
        ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

        StringBuilder query = new StringBuilder();
        query.append("select * from expedition ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Expedition expedition = new Expedition();
            expedition.setIndex(rs.getLong("autoindex"));
            expedition.setDocumentNumber(rs.getString("docnumber"));
            expedition.setDeparture(rs.getString("departure"));
            expedition.setDestination(rs.getString("destination"));
            expedition.setTransportation(rs.getInt("transportation"));
            expedition.setStartDate(rs.getDate("startdate"));
            expedition.setEndDate(rs.getDate("enddate"));
            expedition.setNotes(rs.getString("notes"));
            expedition.setApprovalPlace(rs.getString("approvalplace"));
            expedition.setApprovalDate(rs.getDate("approvaldate"));

            Employee assignedEmployee = new Employee(rs.getLong("assignedemployee"));
            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));
            Account account = new Account(rs.getLong("chargebudget"));
            Program program = new Program(rs.getLong("program"));
            Activity activity = new Activity(rs.getLong("activity"));

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(letter);

            expedition.setCharge(rs.getString("charge"));
            expedition.setAccount(account);
            expedition.setProgram(program);
            expedition.setActivity(activity);
            
            expedition.setUnit(unit);

            expedition.setStyled(true);
            expeditions.add(expedition);
        }

        rs.close();
        pstm.close();

        return expeditions;
    }

    ArrayList<Expedition> getExpeditionByLetter(Connection conn, Long letterIndex) throws SQLException {
        ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

        StringBuilder query = new StringBuilder();
        query.append("select exp.*,e.nip,e.employeename from expedition exp ").
                append("inner join employee e ").
                append("on exp.assignedemployee = e.autoindex ").
                append("where letterindex = ? ").
                append("order by docnumber");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, letterIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Expedition expedition = new Expedition();
            expedition.setIndex(rs.getLong("autoindex"));
            expedition.setDocumentNumber(rs.getString("docnumber"));
            expedition.setDeparture(rs.getString("departure"));
            expedition.setDestination(rs.getString("destination"));
            expedition.setTransportation(rs.getInt("transportation"));
            expedition.setStartDate(rs.getDate("startdate"));
            expedition.setEndDate(rs.getDate("enddate"));
            expedition.setNotes(rs.getString("notes"));
            expedition.setApprovalPlace(rs.getString("approvalplace"));
            expedition.setApprovalDate(rs.getDate("approvaldate"));

            Employee assignedEmployee = new Employee(rs.getLong("assignedemployee"));
            assignedEmployee.setNip(rs.getString("nip"));
            assignedEmployee.setName(rs.getString("employeename"));
            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));
            Account account = new Account(rs.getLong("chargebudget"));
            Program program = new Program(rs.getLong("program"));
            Activity activity = new Activity(rs.getLong("activity"));

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(letter);

            expedition.setCharge(rs.getString("charge"));
            expedition.setAccount(account);
            expedition.setProgram(program);
            expedition.setActivity(activity);
            
            expedition.setUnit(unit);

            expedition.setStyled(true);
            expeditions.add(expedition);
        }

        rs.close();
        pstm.close();

        return expeditions;
    }

    Expedition getExpeditionByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from expedition ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        Expedition expedition = null;
        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            expedition = new Expedition();
            expedition.setIndex(rs.getLong("autoindex"));
            expedition.setDocumentNumber(rs.getString("docnumber"));
            expedition.setDeparture(rs.getString("departure"));
            expedition.setDestination(rs.getString("destination"));
            expedition.setTransportation(rs.getInt("transportation"));
            expedition.setStartDate(rs.getDate("startdate"));
            expedition.setEndDate(rs.getDate("enddate"));
            expedition.setNotes(rs.getString("notes"));
            expedition.setApprovalPlace(rs.getString("approvalplace"));
            expedition.setApprovalDate(rs.getDate("approvaldate"));

            Employee assignedEmployee = new Employee(rs.getLong("assignedemployee"));
            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));
            Account account = new Account(rs.getLong("chargebudget"));
            Program program = new Program(rs.getLong("program"));
            Activity activity = new Activity(rs.getLong("activity"));

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(letter);
            expedition.setProgram(program);
            expedition.setActivity(activity);
            
            expedition.setUnit(unit);

            expedition.setCharge(rs.getString("charge"));
            expedition.setAccount(account);

        }

        rs.close();
        pstm.close();

        return expedition;
    }

    ArrayList<Expedition> getExpedition(Connection conn, Date startDate, Date endDate,String modifier) throws SQLException {
        ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

        StringBuilder query = new StringBuilder();
        query.append("select exp.*,coalesce(al.autoindex,0,1) status,").
                append("emp.nip assnip, emp.employeename assname,al2.purpose from expedition exp ").
                append(" inner join employee emp ").
                append("on emp.autoindex = exp.assignedemployee ").
                append(" left join ").
                append("(select * from assignmentletter ").
                append("where autoindex in ").
                append("(select letterindex from expeditionjournal)) al  ").
                append("on al.autoindex = exp.letterindex ").
                append(" left join assignmentletter al2 ").
                append("on al2.autoindex = exp.letterindex ").
                append("where startdate between ").
                append("'").append(new java.sql.Date(startDate.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(endDate.getTime())).append("' ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Expedition expedition = new Expedition();
            expedition.setIndex(rs.getLong("autoindex"));
            expedition.setDocumentNumber(rs.getString("docnumber"));
            expedition.setDeparture(rs.getString("departure"));
            expedition.setDestination(rs.getString("destination"));
            expedition.setTransportation(rs.getInt("transportation"));
            expedition.setStartDate(rs.getDate("startdate"));
            expedition.setEndDate(rs.getDate("enddate"));
            expedition.setNotes(rs.getString("notes"));
            expedition.setApprovalPlace(rs.getString("approvalplace"));
            expedition.setApprovalDate(rs.getDate("approvaldate"));

            Employee assignedEmployee = new Employee(rs.getLong("assignedemployee"));
            assignedEmployee.setName(rs.getString("assname"));
            assignedEmployee.setNip(rs.getString("assnip"));

            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));
            letter.setPurpose(rs.getString("purpose"));

            Account account = new Account(rs.getLong("chargebudget"));
            Program program = new Program(rs.getLong("program"));
            Activity activity = new Activity(rs.getLong("activity"));

            Integer status = rs.getInt("status");

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(letter);

            expedition.setCharge(rs.getString("charge"));
            expedition.setAccount(account);
            expedition.setProgram(program);
            expedition.setActivity(activity);

            expedition.setDone(status == 1 ? true : false);
            
            expedition.setUnit(unit);

            expedition.setStyled(true);

            expeditions.add(expedition);
        }

        rs.close();
        pstm.close();

        return expeditions;
    }

    ArrayList<Expedition> getExpedition(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<Expedition> expeditions = new ArrayList<Expedition>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select exp.*,coalesce(al.autoindex,0,1) status,").
                    append("emp.nip assnip, emp.employeename assname,al2.purpose from expedition exp ").
                    append(" inner join employee emp ").
                    append("on emp.autoindex = exp.assignedemployee ").
                    append(" left join ").
                    append("(select * from assignmentletter ").
                    append("where autoindex in ").
                    append("(select letterindex from expeditionjournal)) al  ").
                    append("on al.autoindex = exp.letterindex ").
                    append(" left join assignmentletter al2 ").
                    append("on al2.autoindex = exp.letterindex ").
                    append("where date_part('year',startdate) = ").
                    append(year).append(modifier);
        } else {
            query.append("select exp.*,coalesce(al.autoindex,0,1) status,").
                    append("emp.nip assnip, emp.employeename assname,al2.purpose from expedition exp ").
                    append(" inner join employee emp ").
                    append("on emp.autoindex = exp.assignedemployee ").
                    append(" left join ").
                    append("(select * from assignmentletter ").
                    append("where autoindex in ").
                    append("(select letterindex from expeditionjournal)) al  ").
                    append("on al.autoindex = exp.letterindex ").
                    append(" left join assignmentletter al2 ").
                    append("on al2.autoindex = exp.letterindex ").
                    append("where date_part('month',startdate) = ").
                    append(month).
                    append(" and date_part('year',startdate) = ").
                    append(year).append(modifier);
        }
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Expedition expedition = new Expedition();
            expedition.setIndex(rs.getLong("autoindex"));
            expedition.setDocumentNumber(rs.getString("docnumber"));
            expedition.setDeparture(rs.getString("departure"));
            expedition.setDestination(rs.getString("destination"));
            expedition.setTransportation(rs.getInt("transportation"));
            expedition.setStartDate(rs.getDate("startdate"));
            expedition.setEndDate(rs.getDate("enddate"));
            expedition.setNotes(rs.getString("notes"));
            expedition.setApprovalPlace(rs.getString("approvalplace"));
            expedition.setApprovalDate(rs.getDate("approvaldate"));

            Employee assignedEmployee = new Employee(rs.getLong("assignedemployee"));
            assignedEmployee.setName(rs.getString("assname"));
            assignedEmployee.setNip(rs.getString("assnip"));

            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));
            letter.setPurpose(rs.getString("purpose"));

            Account account = new Account(rs.getLong("chargebudget"));
            Program program = new Program(rs.getLong("program"));
            Activity activity = new Activity(rs.getLong("activity"));

            Integer status = rs.getInt("status");

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(letter);

            expedition.setCharge(rs.getString("charge"));
            expedition.setAccount(account);
            expedition.setProgram(program);
            expedition.setActivity(activity);

            expedition.setDone(status > 0 ? true : false);
            
            expedition.setUnit(unit);

            expedition.setStyled(true);

            expeditions.add(expedition);
        }

        rs.close();
        pstm.close();

        return expeditions;
    }

    boolean getExpeditionRangeCheck(Connection conn, Date startdate,Date enddate, Long indexEmployee) throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("SELECT (DATE (to_char(max(startdate), 'yyyy-MM-dd')), ").
                append("DATE (to_char(max(enddate), 'yyyy-MM-dd'))) OVERLAPS ").
                append("(DATE '").append(new java.sql.Date(startdate.getTime())).append("', ").
                append("DATE '").append(new java.sql.Date(enddate.getTime())).append("') ").
                append("as rcheck ").
                append("from expedition ").
                append("where assignedemployee = ").
                append(indexEmployee);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();
        boolean found = false;
        while (rs.next()) {
            found = rs.getBoolean("rcheck");
        }

        rs.close();
        pstm.close();

        return found;
    }

    void insertExpeditionFollower(Connection conn, Long expIndex, ExpeditionFollower follower) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expeditionfollower(expeditionindex,employeeindex,notes)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, expIndex);
        pstm.setLong(++col, follower.getFollower().getIndex());
        pstm.setString(++col, follower.getNotes());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpeditionFollower(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expeditionfollower where expeditionindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ExpeditionFollower> getExpeditionFollower(Connection conn, Long index) throws SQLException {
        ArrayList<ExpeditionFollower> followers = new ArrayList<ExpeditionFollower>();

        StringBuilder query = new StringBuilder();
        query.append("select ef.*,employeename,nip,birthplace,birthdate,").
                append("sex,grade,fungsional,struktural ").
                append("from expeditionfollower ef ").
                append("inner join employee e ").
                append("on e.autoindex = ef.employeeindex ").
                append("where expeditionindex = ?");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ExpeditionFollower follower = new ExpeditionFollower();
            follower.setNotes(rs.getString("notes"));

            Employee employee = new Employee();
            employee.setIndex(rs.getLong("employeeindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));

            follower.setFollower(employee);

            followers.add(follower);
        }

        rs.close();
        pstm.close();

        return followers;
    }

    void insertExpeditionCost(Connection conn, Long expIndex, ExpeditionCost cost) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expeditioncost(expeditionindex,transactionname,count,price,notes)").
                append("values(?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, expIndex);
        pstm.setString(++col, cost.getTransactionName());
        pstm.setInt(++col, cost.getCount());
        pstm.setBigDecimal(++col, cost.getPrice());
        pstm.setString(++col, cost.getNotes());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpeditionCost(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expeditioncost where expeditionindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ExpeditionCost> getExpeditionCost(Connection conn, Long index) throws SQLException {
        ArrayList<ExpeditionCost> costs = new ArrayList<ExpeditionCost>();

        StringBuilder query = new StringBuilder();
        query.append("select * from expeditioncost ").
                append("where expeditionindex = ?");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ExpeditionCost cost = new ExpeditionCost();
            cost.setTransactionName(rs.getString("transactionname"));
            cost.setCount(rs.getInt("count"));
            cost.setPrice(rs.getBigDecimal("price"));
            cost.setNotes(rs.getString("notes"));

            costs.add(cost);
        }

        rs.close();
        pstm.close();

        return costs;
    }

    void insertExpeditionJournal(Connection conn, ExpeditionJournal journal) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expeditionjournal(reportnumber,letterindex,").
                append("reportplace,reportdate)").
                append("values(?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, journal.getReportNumber());
        pstm.setLong(++col, journal.getLetter().getIndex());
        pstm.setString(++col, journal.getReportPlace());
        pstm.setDate(++col, new java.sql.Date(journal.getReportDate().getTime()));

        pstm.executeUpdate();
        pstm.close();
    }

    void updateExpeditionJournal(Connection conn, Long oldIndex, ExpeditionJournal journal) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update expeditionjournal set reportnumber = ?, letterindex = ?,  ").
                append("reportplace = ?, reportdate = ?  ").
                append("where journalindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, journal.getReportNumber());
        pstm.setLong(++col, journal.getLetter().getIndex());
        pstm.setString(++col, journal.getReportPlace());
        pstm.setDate(++col, new java.sql.Date(journal.getReportDate().getTime()));
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpeditionJournal(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expeditionjournal where journalindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ExpeditionJournal> getExpeditionJournal(Connection conn,String modifier) throws SQLException {
        ArrayList<ExpeditionJournal> journals = new ArrayList<ExpeditionJournal>();

        StringBuilder query = new StringBuilder();
        query.append("select * from expeditionjournal ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            ExpeditionJournal journal = new ExpeditionJournal();
            journal.setJournalIndex(rs.getLong("journalindex"));
            journal.setReportNumber(rs.getString("reportnumber"));
            journal.setReportPlace(rs.getString("reportplace"));
            journal.setReportDate(rs.getDate("reportdate"));

            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));

            journal.setLetter(letter);

            journal.setUnit(unit);
            
            journal.setStyled(true);
            

            journals.add(journal);
        }

        rs.close();
        pstm.close();

        return journals;
    }

    ArrayList<ExpeditionJournal> getExpeditionJournal(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<ExpeditionJournal> journals = new ArrayList<ExpeditionJournal>();

        StringBuilder query = new StringBuilder();
        query.append("select * from expeditionjournal ").
                append("where date_part('month',reportdate) = ").
                append(month).
                append(" and date_part('year',reportdate) = ").
                append(year).append(modifier);
        
        System.out.println(query.toString());

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            ExpeditionJournal journal = new ExpeditionJournal();
            journal.setJournalIndex(rs.getLong("journalindex"));
            journal.setReportNumber(rs.getString("reportnumber"));
            journal.setReportPlace(rs.getString("reportplace"));
            journal.setReportDate(rs.getDate("reportdate"));

            AssignmentLetter letter = new AssignmentLetter(rs.getLong("letterindex"));

            journal.setLetter(letter);
            
            journal.setUnit(unit);

            journal.setStyled(true);

            journals.add(journal);
        }

        rs.close();
        pstm.close();

        return journals;
    }

    void insertExpeditionResult(Connection conn, Long indexJournal, ExpeditionResult result) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expeditionresult(journal,notes)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, indexJournal);
        pstm.setString(++col, result.getNotes());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpeditionResult(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expeditionresult where journal = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ExpeditionResult> getExpeditionResult(Connection conn, Long index) throws SQLException {
        ArrayList<ExpeditionResult> results = new ArrayList<ExpeditionResult>();

        StringBuilder query = new StringBuilder();
        query.append("select * from expeditionresult ").
                append("where journal = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ExpeditionResult result = new ExpeditionResult();
            result.setResultIndex(rs.getLong("resultindex"));

            result.setNotes(rs.getString("notes"));

            results.add(result);
        }

        rs.close();
        pstm.close();

        return results;
    }

    void insertAssignmentLetter(Connection conn, AssignmentLetter letter) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into assignmentletter(docnumber,commander,").
                append("purpose, approvalplace, approvaldate,unit, startdate,enddate,goals,notes)").
                append("values(?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, letter.getDocumentNumber());
        pstm.setLong(++col, letter.getCommander().getIndex());
        pstm.setString(++col, letter.getPurpose());
        pstm.setString(++col, letter.getApprovalPlace());
        pstm.setDate(++col, new java.sql.Date(letter.getApprovalDate().getTime()));
        
        if (letter.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, letter.getUnit().getIndex());
        }
        
        pstm.setDate(++col, new java.sql.Date(letter.getStartDate().getTime()));
        pstm.setDate(++col, new java.sql.Date(letter.getEndDate().getTime()));
        pstm.setString(++col, letter.getGoals());
        pstm.setString(++col, letter.getNotes());
        
        pstm.executeUpdate();
        pstm.close();
    }

    void updateAssignmentLetter(Connection conn, Long oldIndex, AssignmentLetter letter) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update assignmentletter set docnumber = ?, commander = ?, ").
                append("purpose = ?, approvalplace = ?, approvaldate = ?, unit = ?, ").
                append("startdate = ?,enddate = ?,goals = ?, notes = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, letter.getDocumentNumber());
        pstm.setLong(++col, letter.getCommander().getIndex());
        pstm.setString(++col, letter.getPurpose());
        pstm.setString(++col, letter.getApprovalPlace());
        pstm.setDate(++col, new java.sql.Date(letter.getApprovalDate().getTime()));
        
        if (letter.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, letter.getUnit().getIndex());
        }
        
        pstm.setDate(++col, new java.sql.Date(letter.getStartDate().getTime()));
        pstm.setDate(++col, new java.sql.Date(letter.getEndDate().getTime()));
        pstm.setString(++col, letter.getGoals());
        pstm.setString(++col, letter.getNotes());
        
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteAssignmentLetter(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from assignmentletter where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<AssignmentLetter> getAssigmentLetter(Connection conn,String modifier) throws SQLException {
        ArrayList<AssignmentLetter> letters = new ArrayList<AssignmentLetter>();

        StringBuilder query = new StringBuilder();
        query.append("select * from assignmentletter ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            AssignmentLetter letter = new AssignmentLetter();
            letter.setIndex(rs.getLong("autoindex"));
            letter.setDocumentNumber(rs.getString("docnumber"));
            letter.setPurpose(rs.getString("purpose"));
            letter.setApprovalPlace(rs.getString("approvalplace"));
            letter.setApprovalDate(rs.getDate("approvaldate"));
            
            letter.setStartDate(rs.getDate("startdate"));
            letter.setEndDate(rs.getDate("enddate"));
            letter.setGoals(rs.getString("goals"));
            letter.setNotes(rs.getString("notes"));

            Employee commander = new Employee(rs.getLong("commander"));
            letter.setCommander(commander);
            
            letter.setUnit(unit);

            letter.setStyled(true);

            letters.add(letter);
        }

        rs.close();
        pstm.close();

        return letters;
    }

    ArrayList<AssignmentLetter> getAssigmentLetterInExpedition(Connection conn,String modifier) throws SQLException {
        ArrayList<AssignmentLetter> letters = new ArrayList<AssignmentLetter>();

        StringBuilder query = new StringBuilder();
        query.append("select * from assignmentletter ").
                append("where autoindex in ").
                append("(select letterindex from expedition) ").
                append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            AssignmentLetter letter = new AssignmentLetter();
            letter.setIndex(rs.getLong("autoindex"));
            letter.setDocumentNumber(rs.getString("docnumber"));
            letter.setPurpose(rs.getString("purpose"));
            letter.setApprovalPlace(rs.getString("approvalplace"));
            letter.setApprovalDate(rs.getDate("approvaldate"));
            
            letter.setStartDate(rs.getDate("startdate"));
            letter.setEndDate(rs.getDate("enddate"));
            letter.setGoals(rs.getString("goals"));
            letter.setNotes(rs.getString("notes"));

            Employee commander = new Employee(rs.getLong("commander"));
            letter.setCommander(commander);
            
            letter.setUnit(unit);

            letter.setStyled(true);

            letters.add(letter);
        }

        rs.close();
        pstm.close();

        return letters;
    }

    AssignmentLetter getAssignmentLetterByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from assignmentletter ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();
        AssignmentLetter letter = null;
        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            letter = new AssignmentLetter();
            letter.setIndex(rs.getLong("autoindex"));
            letter.setDocumentNumber(rs.getString("docnumber"));
            letter.setPurpose(rs.getString("purpose"));
            letter.setApprovalPlace(rs.getString("approvalplace"));
            letter.setApprovalDate(rs.getDate("approvaldate"));
            
            letter.setStartDate(rs.getDate("startdate"));
            letter.setEndDate(rs.getDate("enddate"));
            letter.setGoals(rs.getString("goals"));
            letter.setNotes(rs.getString("notes"));

            Employee commander = new Employee(rs.getLong("commander"));
            letter.setCommander(commander);
            
            letter.setUnit(unit);
            
        }

        rs.close();
        pstm.close();

        return letter;
    }

    ArrayList<AssignmentLetter> getAssignmentLetter(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<AssignmentLetter> letters = new ArrayList<AssignmentLetter>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from assignmentletter ").
                    append("where date_part('year',approvaldate) = ").
                    append(year);
        } else {
            query.append("select * from assignmentletter ").
                    append("where date_part('month',approvaldate) = ").
                    append(month).
                    append(" and date_part('year',approvaldate) = ").
                    append(year);
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            AssignmentLetter letter = new AssignmentLetter();
            letter.setIndex(rs.getLong("autoindex"));
            letter.setDocumentNumber(rs.getString("docnumber"));
            letter.setPurpose(rs.getString("purpose"));
            letter.setApprovalPlace(rs.getString("approvalplace"));
            letter.setApprovalDate(rs.getDate("approvaldate"));
            
            letter.setStartDate(rs.getDate("startdate"));
            letter.setEndDate(rs.getDate("enddate"));
            letter.setGoals(rs.getString("goals"));
            letter.setNotes(rs.getString("notes"));

            Employee commander = new Employee(rs.getLong("commander"));
            letter.setCommander(commander);

            letter.setStyled(true);
            letter.setUnit(unit);

            letters.add(letter);
        }

        rs.close();
        pstm.close();

        return letters;
    }

    boolean getAssignmentLetterInExpedition(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from assignmentletter ").
                append("where autoindex in ").
                append("(select letterindex from expedition) ").
                append("and autoindex = ?");

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

    void insertAssignedEmployee(Connection conn, Long letterIndex, Employee assignedEmployee) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into assignedemployee(assignmentindex,employeeindex)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, letterIndex);
        pstm.setLong(++col, assignedEmployee.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteAssignedEmployee(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from assignedemployee where assignmentindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Employee> getAssignedEmployee(Connection conn, Long index) throws SQLException {
        ArrayList<Employee> assignedEmployee = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select ae.*,employeename,nip,birthplace,birthdate,").
                append("sex,grade,fungsional,struktural, positionnotes, isgorvernor ").
                append("from assignedemployee ae ").
                append("inner join employee e ").
                append("on e.autoindex = ae.employeeindex ").
                append("where assignmentindex = ?");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Employee employee = new Employee();
            employee.setIndex(rs.getLong("employeeindex"));
            employee.setNip(rs.getString("nip"));
            employee.setName(rs.getString("employeename"));
            employee.setBirthPlace(rs.getString("birthplace"));
            employee.setBirthDate(rs.getDate("birthdate"));
            employee.setSex(rs.getInt("sex"));
            employee.setGrade(rs.getInt("grade"));
            employee.setFungsional(rs.getInt("fungsional"));
            employee.setStruktural(rs.getInt("struktural"));
            employee.setPositionNotes(rs.getString("positionnotes"));
            employee.setGorvernor(rs.getBoolean("isgorvernor"));

            assignedEmployee.add(employee);
        }

        rs.close();
        pstm.close();

        return assignedEmployee;
    }

    void insertCarbonCopy(Connection conn, Long letterIndex, String copyto) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into carboncopy(assignmentindex,copyto)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, letterIndex);
        pstm.setString(++col, copyto);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteCarbonCopy(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from carboncopy where assignmentindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<String> getCarbonCopy(Connection conn, Long index) throws SQLException {
        ArrayList<String> copy = new ArrayList<String>();

        StringBuilder query = new StringBuilder();
        query.append("select * from carboncopy ").
                append("where assignmentindex = ?");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            String copyTo = rs.getString("copyto");

            copy.add(copyTo);
        }

        rs.close();
        pstm.close();

        return copy;
    }

    void insertExpeditionCheque(Connection conn, ExpeditionCheque cheque) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into expeditioncheque(expeditionindex,payer,bankaccount,taxnumber,").
                append("receivedfrom,amount,notes,receivedplace,heademployee,").
                append("pptk, clerk, paidto,budgettype,unit)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, cheque.getExpedition().getIndex());
        pstm.setString(++col, cheque.getPayer());
        pstm.setString(++col, cheque.getBankAccount());
        pstm.setString(++col, cheque.getTaxNumber());
        pstm.setString(++col, cheque.getReceivedFrom());
        pstm.setLong(++col, cheque.getAmount().getIndex());
        pstm.setString(++col, cheque.getNotes());
        pstm.setString(++col, cheque.getReceivedPlace());
        pstm.setLong(++col, cheque.getHeadEmployee().getIndex());
        pstm.setLong(++col, cheque.getPptk().getIndex());
        pstm.setLong(++col, cheque.getClerk().getIndex());
        pstm.setLong(++col, cheque.getPaidTo().getIndex());
        pstm.setInt(++col, cheque.getBudgetType());
        
        if (cheque.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, cheque.getUnit().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateExpeditionCheque(Connection conn, Long oldIndex, ExpeditionCheque cheque) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update expeditioncheque set expeditionindex = ?, payer = ?, bankaccount = ?,taxnumber = ?, ").
                append("receivedfrom = ?, amount = ?, notes = ?, receivedplace = ?, heademployee = ?,").
                append("pptk = ?, clerk = ?, paidto = ?, budgettype = ?, unit = ?  ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, cheque.getExpedition().getIndex());
        pstm.setString(++col, cheque.getPayer());
        pstm.setString(++col, cheque.getBankAccount());
        pstm.setString(++col, cheque.getTaxNumber());
        pstm.setString(++col, cheque.getReceivedFrom());
        pstm.setLong(++col, cheque.getAmount().getIndex());
        pstm.setString(++col, cheque.getNotes());
        pstm.setString(++col, cheque.getReceivedPlace());
        pstm.setLong(++col, cheque.getHeadEmployee().getIndex());
        pstm.setLong(++col, cheque.getPptk().getIndex());
        pstm.setLong(++col, cheque.getClerk().getIndex());
        pstm.setLong(++col, cheque.getPaidTo().getIndex());
        pstm.setInt(++col, cheque.getBudgetType());
        
        if (cheque.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, cheque.getUnit().getIndex());
        }
        
        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteExpeditionCheque(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from expeditioncheque where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ExpeditionCheque> getExpeditionCheque(Connection conn,String modifier) throws SQLException {
        ArrayList<ExpeditionCheque> cheques = new ArrayList<ExpeditionCheque>();

        StringBuilder query = new StringBuilder();
        query.append("select ec.*,e.employeename,e.nip from expeditioncheque ec ").
                append("inner join employee e ").
                append("on e.autoindex = ec.paidto ").
                append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            ExpeditionCheque cheque = new ExpeditionCheque();
            cheque.setIndex(rs.getLong("autoindex"));
            cheque.setPayer(rs.getString("payer"));
            cheque.setBankAccount(rs.getString("bankaccount"));
            cheque.setTaxNumber(rs.getString("taxnumber"));
            cheque.setReceivedFrom(rs.getString("receivedfrom"));
            cheque.setNotes(rs.getString("notes"));
            cheque.setReceivedPlace(rs.getString("receivedplace"));
            cheque.setBudgetType(rs.getInt("budgettype"));

            Expedition expedition = new Expedition(rs.getLong("expeditionindex"));
            Employee headEmployee = new Employee(rs.getLong("heademployee"));
            Employee pptk = new Employee(rs.getLong("pptk"));
            Employee clerk = new Employee(rs.getLong("clerk"));
            Employee paidTo = new Employee(rs.getLong("paidTo"));
            paidTo.setName(rs.getString("employeename"));
            paidTo.setNip(rs.getString("nip"));
            StandardPrice amount = new StandardPrice(rs.getLong("amount"));

            cheque.setExpedition(expedition);
            cheque.setHeadEmployee(headEmployee);
            cheque.setPptk(pptk);
            cheque.setClerk(clerk);
            cheque.setPaidTo(paidTo);
            cheque.setAmount(amount);
            
            cheque.setUnit(unit);

            cheque.setStyled(true);

            cheques.add(cheque);
        }

        rs.close();
        pstm.close();

        return cheques;
    }

    BigDecimal getBudgetUsed(Connection conn,Integer years,Integer budgetType) throws SQLException {
        BigDecimal used = BigDecimal.ZERO;

        StringBuilder query = new StringBuilder();
        query.append("select coalesce(sum(price),0) as amount from expeditioncheque ec ").
                append("inner join standardprice sp ").
                append("on sp.autoindex = ec.autoindex ").
                append("where budgettype = ? ").
                append("and expeditionindex in ").
                append("(select autoindex from expedition ").
                append("where date_part('year',startdate) = ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, budgetType);
        pstm.setInt(2, years);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            used = rs.getBigDecimal("amount");
        }

        rs.close();
        pstm.close();

        return used;
    }

    BigDecimal getBudgetUsed(Connection conn,Long indexCheque,Integer years,Integer budgetType) throws SQLException {
        BigDecimal used = BigDecimal.ZERO;

        StringBuilder query = new StringBuilder();
        query.append("select coalesce(sum(price),0) as amount from expeditioncheque ec ").
                append("inner join standardprice sp ").
                append("on sp.autoindex = ec.autoindex ").
                append("where budgettype = ? ").
                append("and ec.autoindex != ?  ").
                append("and expeditionindex in ").
                append("(select autoindex from expedition ").
                append("where date_part('year',startdate) = ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, budgetType);
        pstm.setLong(2, indexCheque);
        pstm.setInt(3, years);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            used = rs.getBigDecimal("amount");
        }

        rs.close();
        pstm.close();

        return used;
    }
}
