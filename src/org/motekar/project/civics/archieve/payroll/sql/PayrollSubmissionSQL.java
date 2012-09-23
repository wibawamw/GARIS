package org.motekar.project.civics.archieve.payroll.sql;

import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmissionFile;
import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmissionRequirement;
import org.motekar.project.civics.archieve.payroll.objects.SubmissionRequirement;
import org.motekar.project.civics.archieve.payroll.objects.SubmissionType;
import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.objects.*;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmissionSQL extends CommonSQL {

    void insertSubmissionType(Connection conn, SubmissionType submissiontype) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into submissiontype(typename)").
                append("values(?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, submissiontype.getTypeName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateSubmissionType(Connection conn, Long oldIndex, SubmissionType submissiontype) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update submissiontype set typename = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, submissiontype.getTypeName());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSubmissionType(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from submissiontype where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<SubmissionType> getSubmissionType(Connection conn) throws SQLException {
        ArrayList<SubmissionType> submissiontypes = new ArrayList<SubmissionType>();

        StringBuilder query = new StringBuilder();
        query.append("select * from submissiontype ").
                append("order by typename");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            SubmissionType submissiontype = new SubmissionType();
            submissiontype.setIndex(rs.getLong("autoindex"));
            submissiontype.setTypeName(rs.getString("typename"));

            submissiontype.setStyled(true);

            submissiontypes.add(submissiontype);
        }

        rs.close();
        pstm.close();

        return submissiontypes;
    }

    SubmissionType getSubmissionType(Connection conn, Long index) throws SQLException {
        SubmissionType submissiontype = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from submissiontype ").
                append("where autoindex = ? ").
                append("order by typename");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            submissiontype = new SubmissionType();
            submissiontype.setIndex(rs.getLong("autoindex"));
            submissiontype.setTypeName(rs.getString("typename"));

            submissiontype.setStyled(false);
        }

        rs.close();
        pstm.close();

        return submissiontype;
    }

    void insertSubmissionRequirement(Connection conn, SubmissionRequirement submissionrequirement) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into submissionrequirement(requirementname)").
                append("values(?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, submissionrequirement.getRequirementName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateSubmissionRequirement(Connection conn, Long oldIndex, SubmissionRequirement submissionrequirement) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update submissionrequirement set requirementname = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, submissionrequirement.getRequirementName());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSubmissionRequirement(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from submissionrequirement where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<SubmissionRequirement> getSubmissionRequirement(Connection conn) throws SQLException {
        ArrayList<SubmissionRequirement> submissionrequirements = new ArrayList<SubmissionRequirement>();

        StringBuilder query = new StringBuilder();
        query.append("select * from submissionrequirement ").
                append("order by requirementname");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            SubmissionRequirement submissionrequirement = new SubmissionRequirement();
            submissionrequirement.setIndex(rs.getLong("autoindex"));
            submissionrequirement.setRequirementName(rs.getString("requirementname"));

            submissionrequirement.setStyled(true);

            submissionrequirements.add(submissionrequirement);
        }

        rs.close();
        pstm.close();

        return submissionrequirements;
    }

    SubmissionRequirement getSubmissionRequirement(Connection conn, Long index) throws SQLException {
        SubmissionRequirement submissionrequirement = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from submissionrequirement ").
                append("where autoindex = ? ").
                append("order by requirementname");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            submissionrequirement = new SubmissionRequirement();
            submissionrequirement.setIndex(rs.getLong("autoindex"));
            submissionrequirement.setRequirementName(rs.getString("requirementname"));

            submissionrequirement.setStyled(false);
        }

        rs.close();
        pstm.close();

        return submissionrequirement;
    }

    void insertPayrollSubmission(Connection conn, PayrollSubmission payrollSubmission) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into payrollsubmission(skpd, employee, submissiondate, submissiontype, proccesed)").
                append("values(?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, payrollSubmission.getSkpd().getIndex());
        pstm.setLong(++col, payrollSubmission.getEmployee().getIndex());
        pstm.setDate(++col, new java.sql.Date(payrollSubmission.getSubmissionDate().getTime()));
        pstm.setLong(++col, payrollSubmission.getType().getIndex());
        pstm.setBoolean(++col, payrollSubmission.isProccesed());

        pstm.executeUpdate();
        pstm.close();
    }

    void updatePayrollSubmission(Connection conn, Long oldIndex, PayrollSubmission payrollSubmission) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update payrollsubmission set skpd=?, employee=?, submissiondate=?,  ").
                append("submissiontype=?,proccesed=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, payrollSubmission.getSkpd().getIndex());
        pstm.setLong(++col, payrollSubmission.getEmployee().getIndex());
        pstm.setDate(++col, new java.sql.Date(payrollSubmission.getSubmissionDate().getTime()));
        pstm.setLong(++col, payrollSubmission.getType().getIndex());
        pstm.setBoolean(++col, payrollSubmission.isProccesed());
        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deletePayrollSubmission(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from payrollsubmission where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<PayrollSubmission> getPayrollSubmission(Connection conn) throws SQLException {
        ArrayList<PayrollSubmission> payrollSubmissions = new ArrayList<PayrollSubmission>();

        StringBuilder query = new StringBuilder();
        query.append("select * from payrollsubmission ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            PayrollSubmission payrollSubmission = new PayrollSubmission();
            payrollSubmission.setIndex(rs.getLong("autoindex"));
            payrollSubmission.setSubmissionDate(rs.getDate("submissiondate"));
            payrollSubmission.setProccesed(rs.getBoolean("proccesed"));
            
            SKPD skpd = new SKPD(rs.getLong("skpd"));
            Employee employee = new Employee(rs.getLong("employee"));
            SubmissionType type = new SubmissionType(rs.getLong("submissiontype"));
            
            payrollSubmission.setSkpd(skpd);
            payrollSubmission.setEmployee(employee);
            payrollSubmission.setType(type);
            

            payrollSubmission.setStyled(true);

            payrollSubmissions.add(payrollSubmission);
        }

        rs.close();
        pstm.close();

        return payrollSubmissions;
    }

    PayrollSubmission getPayrollSubmission(Connection conn, Long index) throws SQLException {
        PayrollSubmission payrollSubmission = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from payrollsubmission ").
                append("where autoindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            payrollSubmission = new PayrollSubmission();
            payrollSubmission.setIndex(rs.getLong("autoindex"));
            payrollSubmission.setSubmissionDate(rs.getDate("submissiondate"));
            payrollSubmission.setProccesed(rs.getBoolean("proccesed"));
            
            SKPD skpd = new SKPD(rs.getLong("skpd"));
            Employee employee = new Employee(rs.getLong("employee"));
            SubmissionType type = new SubmissionType(rs.getLong("type"));
            
            payrollSubmission.setSkpd(skpd);
            payrollSubmission.setEmployee(employee);
            payrollSubmission.setType(type);
            

            payrollSubmission.setStyled(true);
        }

        rs.close();
        pstm.close();

        return payrollSubmission;
    }
    
    void insertPayrollSubmissionFile(Connection conn, PayrollSubmissionFile file) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into payrollsubmissionfile(submissionindex,filename,filebyte)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, file.getSubmissionIndex());
        pstm.setString(++col, file.getFileName());
        pstm.setBytes(++col, file.getFileStream());

        pstm.executeUpdate();
        pstm.close();
    }

    void deletePayrollSubmissionFile(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from payrollsubmissionfile ").
                append("where submissionindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<PayrollSubmissionFile> getPayrollSubmissionFile(Connection conn, Long index) throws SQLException {
        ArrayList<PayrollSubmissionFile> payrollsubmissionFiles = new ArrayList<PayrollSubmissionFile>();

        StringBuilder query = new StringBuilder();
        query.append("select * from payrollsubmissionfile ").
                append("where submissionindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            PayrollSubmissionFile file = new PayrollSubmissionFile();

            file.setSubmissionIndex(rs.getLong("submissionindex"));
            file.setFileName(rs.getString("filename"));
            file.setFileStream(rs.getBytes("filebyte"));

            payrollsubmissionFiles.add(file);
        }

        rs.close();
        pstm.close();

        return payrollsubmissionFiles;
    }
    
    
    void insertPayrollSubmissionRequirement(Connection conn, PayrollSubmissionRequirement requirement) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into payrollsubmissionrequirement(submissionindex, requirement, checked)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, requirement.getSubmissionIndex());
        pstm.setLong(++col, requirement.getRequirement().getIndex());
        pstm.setBoolean(++col, requirement.isChecked());

        pstm.executeUpdate();
        pstm.close();
    }

    void deletePayrollSubmissionRequirement(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from payrollsubmissionrequirement ").
                append("where submissionindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<PayrollSubmissionRequirement> getPayrollSubmissionRequirement(Connection conn, Long index) throws SQLException {
        ArrayList<PayrollSubmissionRequirement> submissionRequirements = new ArrayList<PayrollSubmissionRequirement>();

        StringBuilder query = new StringBuilder();
        query.append("select * from payrollsubmissionrequirement ").
                append("where submissionindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            PayrollSubmissionRequirement submissionRequirement = new PayrollSubmissionRequirement();

            submissionRequirement.setSubmissionIndex(rs.getLong("submissionindex"));
            submissionRequirement.setChecked(rs.getBoolean("checked"));
            
            SubmissionRequirement requirement = new SubmissionRequirement(rs.getLong("requirement"));
            submissionRequirement.setRequirement(requirement);

            submissionRequirements.add(submissionRequirement);
        }

        rs.close();
        pstm.close();

        return submissionRequirements;
    }
}
