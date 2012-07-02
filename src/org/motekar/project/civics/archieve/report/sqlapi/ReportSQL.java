package org.motekar.project.civics.archieve.report.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.report.object.BudgetRealization;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReportSQL {
    
    ArrayList<BudgetRealization> getBudgetRealization(Connection conn,Integer years,Integer budgetType) throws SQLException {
        ArrayList<BudgetRealization> realizations = new ArrayList<BudgetRealization>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select act.activitycode as code,act.activityname as description, ").
                append("coalesce(amount,0) as opening,coalesce(null,0) as debet, ").
                append("coalesce(expenses,0) as credit, ").
                append("coalesce((coalesce(amount,0) - coalesce(expenses,0)),0) closing,  ").
                append("'Activity' as flags ").
                append("from activity act ").
                append("left join ").
                append("(select activityindex,sum(amount) as amount ").
                append("from budgetsubdetail ").
                append("where detailindex in ").
                append("(select autoindex from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?)) ").
                append("group by activityindex) bsd ").
                append("on bsd.activityindex = act.autoindex ").
                append("left join ").
                append("(select exp.activity,sum(sp.price) as expenses from expeditioncheque ec ").
                append("inner join ").
                append("(select * from expedition ").
                append("where date_part('year',startdate) = ?) exp ").
                append("on exp.autoindex = ec.expeditionindex ").
                append("inner join standardprice sp ").
                append("on sp.autoindex = ec.amount ").
                append("where budgettype = ? ").
                append("group by exp.activity) realize ").
                append("on realize.activity = act.autoindex) realact ").
                //
                append("union ").
                append("select * from ").
                append("(select pr.programcode,pr.programname as description, ").
                append("coalesce(amount,0) as opening,coalesce(null,0) as debet, ").
                append("coalesce(expenses,0) as credit, ").
                append("coalesce((coalesce(amount,0) - coalesce(expenses,0)),0) closing, ").
                append("'Program' as flags ").
                append("from program pr ").
                append("left join ").
                append("(select bd.programindex, sum(amount) as amount ").
                append("from budgetsubdetail bsd ").
                append("inner join ").
                append("(select * from budgetdetail ").
                append("where budgetindex in ").
                append("(select autoindex from budget ").
                append("where years = ? and budgettype = ?)) bd ").
                append("on bsd.detailindex = bd.autoindex ").
                append("group by programindex) bsd ").
                append("on bsd.programindex = pr.autoindex ").
                append("left join ").
                append("(select exp.program,sum(sp.price) as expenses from expeditioncheque ec ").
                append("inner join ").
                append("(select * from expedition ").
                append("where date_part('year',startdate) = ?) exp ").
                append("on exp.autoindex = ec.expeditionindex ").
                append("inner join standardprice sp ").
                append("on sp.autoindex = ec.amount ").
                append("where budgettype = ? ").
                append("group by exp.program) realize ").
                append("on realize.program = pr.autoindex) as realprog ").
                //
                append("union ").
                append("select * from ").
                append("(select (act.activitycode||'.01') as code,bsd.docnumber as description, ").
                append("coalesce(null,0) as opening,coalesce(null,0) as debet, ").
                append("coalesce(expenses,0) as credit, ").
                append("coalesce((coalesce(null,0) - coalesce(expenses,0)),0) closing, ").
                append("'Detail' as flags ").
                append("from activity act ").
                append("inner join ").
                append("(select exp.activity,exp.docnumber, sp.price as expenses from expeditioncheque ec ").
                append("inner join ").
                append("(select * from expedition ").
                append("where date_part('year',startdate) = ?) exp ").
                append("on exp.autoindex = ec.expeditionindex ").
                append("inner join standardprice sp ").
                append("on sp.autoindex = ec.amount ").
                append("where budgettype = ?) bsd ").
                append("on bsd.activity = act.autoindex) as realdetail ").
                append("order by code,description");


        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);


        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetRealization real = new BudgetRealization();
            real.setCode(rs.getString("code"));
            real.setDescription(rs.getString("description"));
            real.setOpening(rs.getBigDecimal("opening"));
            real.setDebet(rs.getBigDecimal("debet"));
            real.setCredit(rs.getBigDecimal("credit"));
            real.setClosing(rs.getBigDecimal("closing"));
            real.setMarker(rs.getString("flags"));

            realizations.add(real);
        }

        rs.close();
        pstm.close();

        return realizations;
    }

    ArrayList<Employee> getEmployeeReport(Connection conn,String modifier) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where isgorvernor = false ").
                append(modifier).
                append(" order by grade desc,eselon");

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
            employee.setGraduatedYear(rs.getInt("graduatedyear"));
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));
            
            employee.setReligion(rs.getInt("religion"));
            employee.setMutation(rs.getString("mutation"));
            employee.setEmployeeStatus(rs.getInt("employeestatus"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }
}
