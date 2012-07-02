package org.motekar.project.civics.archieve.report.sqlapi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.report.object.BudgetRealization;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReportSQL {
    
    ArrayList<BudgetRealization> getBudgetRealizationAll(Connection conn, Integer years, Integer budgetType, String modifier) throws SQLException {
        ArrayList<BudgetRealization> realizations = new ArrayList<BudgetRealization>();

        StringBuilder query = new StringBuilder();
        query.append("select distinct a.*,(coalesce(b.price,0)) as price,(coalesce(c.countdiff,0)) as countdiff from ").
                append("(select eselon,activitycode,activityname,(sum(counted)) as counted,(sum(counted * amount)) as budget ").
                append("from budgetsubdetailchild bsdc ").
                append("inner join ").
                append("(select * from ").
                append("(select bsd.autoindex as subdetailindex, detailindex,activitycode,activityname ").
                append("from budgetsubdetail bsd ").
                append("inner join ").
                append("(select * from activity) act ").
                append("on bsd.activityindex = act.autoindex) bsd ").
                append("inner join ").
                append("(select * from ").
                append("(select bd.autoindex as detailindex,budgetindex from budgetdetail bd ").
                append(" inner join ").
                append("(select * from program )p ").
                append("on bd.programindex = p.autoindex) bd ").
                append("inner join ").
                append("(select * from budget ").
                append("where budgettype = ? ").
                append("and years = ?) b ").
                append("on bd.budgetindex = b.autoindex ").
                append(") bd ").
                append("on bsd.detailindex = bd.detailindex ").
                append(") bsd ").
                append("on bsd.subdetailindex = bsdc.subdetailindex ").
                append("group by eselon,activitycode,activityname) a ").
                //
                append("left join ").
                append("(select esselon,activitycode,activityname,(sum(price)) as price from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join  ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join  ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join ").
                append("(select * from activity) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by esselon,activitycode,activityname) b ").
                append("on a.eselon = b.esselon ").
                //
                append("left join ").
                append("(select esselon,activitycode,activityname,(count(esselon)) as countdiff from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join  ").
                append("(select * from activity) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by esselon,activitycode,activityname) c ").
                append("on  a.eselon = c.esselon ").
                append("order by activitycode,eselon ");
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, budgetType);


        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetRealization real = new BudgetRealization();

            Integer eselon = rs.getInt("eselon");

            real.setEselon(StandardPrice.ESELON[eselon]);
            real.setCounted(rs.getInt("counted"));
            real.setDiff(rs.getInt("countdiff"));
            
            BigDecimal budget = rs.getBigDecimal("budget");
            BigDecimal credit = rs.getBigDecimal("price");
            
            BigDecimal closing = BigDecimal.ZERO;
            closing = closing.add(budget);
            closing = closing.subtract(credit);
            
            real.setBudget(budget);
            real.setCredit(credit);
            real.setClosing(closing);

            StringBuilder builder = new StringBuilder();

            Integer countDiff = real.getCountedDiff();

            if (countDiff.compareTo(Integer.valueOf(0)) > 0) {
                builder.append(countDiff).append(" kali lagi perjalanan");
            }

            real.setDescription(builder.toString());

            String actCode = rs.getString("activitycode");
            String actName = rs.getString("activityname");
            
            if (!actName.toLowerCase().contains("kegiatan")) {
                actName = "Kegiatan "+actName;
            }

            real.setActivity(actCode+" "+actName);

            realizations.add(real);
        }

        rs.close();
        pstm.close();

        return realizations;
    }

    ArrayList<BudgetRealization> getBudgetRealizationByActivity(Connection conn, Integer years, Integer budgetType, Activity activity, String modifier) throws SQLException {
        ArrayList<BudgetRealization> realizations = new ArrayList<BudgetRealization>();

        StringBuilder query = new StringBuilder();
        query.append("select a.*,(coalesce(b.price,0)) as price,(coalesce(c.countdiff,0)) as countdiff from ").
                append("(select eselon,(sum(counted)) as counted,(sum(counted * amount)) as budget ").
                append("from budgetsubdetailchild bsdc ").
                append("inner join ").
                append("(select * from ").
                append("(select bsd.autoindex as subdetailindex, detailindex ").
                append("from budgetsubdetail bsd ").
                append("inner join ").
                append("(select * from activity ").
                append("where autoindex = ?) act ").
                append("on bsd.activityindex = act.autoindex) bsd ").
                append("inner join ").
                append("(select * from ").
                append("(select bd.autoindex as detailindex,budgetindex from budgetdetail bd ").
                append(" inner join ").
                append("(select * from program ) p ").
                append("on bd.programindex = p.autoindex) bd ").
                append("inner join ").
                append("(select * from budget ").
                append("where budgettype = ? ").
                append("and years = ?) b ").
                append("on bd.budgetindex = b.autoindex ").
                append(") bd ").
                append("on bsd.detailindex = bd.detailindex ").
                append(") bsd ").
                append("on bsd.subdetailindex = bsdc.subdetailindex ").
                append("group by eselon) a ").
                //
                append("left join ").
                append("(select esselon,(sum(price)) as price from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join  ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join  ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join ").
                append("(select * from activity ").
                append("where autoindex = ?) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by esselon) b ").
                append("on a.eselon = b.esselon ").
                //
                append("left join ").
                append("(select esselon,(count(esselon)) as countdiff from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join  ").
                append("(select * from activity ").
                append("where autoindex = ?) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by esselon) c ").
                append("on  a.eselon = c.esselon ").
                append("order by eselon ");
        

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, activity.getIndex());
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setLong(++col, activity.getIndex());
        pstm.setInt(++col, budgetType);
        pstm.setLong(++col, activity.getIndex());
        pstm.setInt(++col, budgetType);


        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetRealization real = new BudgetRealization();

            Integer eselon = rs.getInt("eselon");

            real.setEselon(StandardPrice.ESELON[eselon]);
            real.setCounted(rs.getInt("counted"));
            real.setDiff(rs.getInt("countdiff"));
            
            BigDecimal budget = rs.getBigDecimal("budget");
            BigDecimal credit = rs.getBigDecimal("price");
            
            BigDecimal closing = BigDecimal.ZERO;
            closing = closing.add(budget);
            closing = closing.subtract(credit);
            
            real.setBudget(budget);
            real.setCredit(credit);
            real.setClosing(closing);

            StringBuilder builder = new StringBuilder();

            Integer countDiff = real.getCountedDiff();

            if (countDiff.compareTo(Integer.valueOf(0)) > 0) {
                builder.append(countDiff).append(" kali lagi perjalanan");
            }

            real.setDescription(builder.toString());

            real.setActivity("");

            realizations.add(real);
        }

        rs.close();
        pstm.close();

        return realizations;
    }

    ArrayList<BudgetRealization> getBudgetRealizationByEselon(Connection conn, Integer years, Integer budgetType,Integer eselon, String modifier) throws SQLException {
        ArrayList<BudgetRealization> realizations = new ArrayList<BudgetRealization>();

        StringBuilder query = new StringBuilder();
        query.append("select a.*,(coalesce(b.price,0)) as price,(coalesce(c.countdiff,0)) as countdiff from ").
                append("(select sum(counted) as counted,activitycode,activityname,(sum(counted * amount)) as budget ").
                append("from budgetsubdetailchild bsdc ").
                append("inner join ").
                append("(select * from ").
                append("(select bsd.autoindex as subdetailindex, detailindex,act.activitycode,act.activityname ").
                append("from budgetsubdetail bsd ").
                append("inner join ").
                append("(select * from activity) act ").
                append("on bsd.activityindex = act.autoindex) bsd ").
                append("inner join ").
                append("(select * from ").
                append("(select bd.autoindex as detailindex,budgetindex from budgetdetail bd ").
                append("inner join ").
                append("(select * from program) p ").
                append("on bd.programindex = p.autoindex) bd ").
                append("inner join  ").
                append("(select * from budget ").
                append("where budgettype = ? ").
                append("and years = ?) b  ").
                append("on bd.budgetindex = b.autoindex ").
                append(") bd ").
                append("on bsd.detailindex = bd.detailindex ").
                append(") bsd ").
                append("on bsd.subdetailindex = bsdc.subdetailindex ").
                append("where eselon = ? ").
                append("group by activitycode,activityname) a ").
                //
                append("left join ").
                append("(select exp.activitycode,(sum(price)) as price from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice ").
                append("where esselon = ?) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join ").
                append("(select * from activity) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by activitycode) b ").
                append("on a.activitycode = b.activitycode ").
                //
                append("left join ").
                append("(select exp.activitycode,(count(activitycode)) as countdiff from expeditioncheque ec ").
                append("inner join ").
                append("(select * from standardprice ").
                append("where esselon = ?) sp ").
                append("on sp.autoindex = ec.amount ").
                append("inner join ").
                append("(select exp.autoindex,exp.startdate, activitycode,activityname ").
                append("from expedition exp ").
                append("inner join ").
                append("(select * from program) p ").
                append("on exp.program = p.autoindex ").
                append("inner join ").
                append("(select * from activity) act ").
                append("on exp.activity = act.autoindex) exp ").
                append("on ec.expeditionindex = exp.autoindex ").
                append("where budgettype = ? ").
                append(modifier).
                append(" group by activitycode) c ").
                append("on a.activitycode = c.activitycode ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, years);
        pstm.setInt(++col, eselon);
        pstm.setInt(++col, eselon);
        pstm.setInt(++col, budgetType);
        pstm.setInt(++col, eselon);
        pstm.setInt(++col, budgetType);


        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            BudgetRealization real = new BudgetRealization();


            real.setEselon("");
            real.setCounted(rs.getInt("counted"));
            real.setDiff(rs.getInt("countdiff"));
            
            BigDecimal budget = rs.getBigDecimal("budget");
            BigDecimal credit = rs.getBigDecimal("price");
            
            BigDecimal closing = BigDecimal.ZERO;
            closing = closing.add(budget);
            closing = closing.subtract(credit);
            
            real.setBudget(budget);
            real.setCredit(credit);
            real.setClosing(closing);

            StringBuilder builder = new StringBuilder();

            Integer countDiff = real.getCountedDiff();

            if (countDiff.compareTo(Integer.valueOf(0)) > 0) {
                builder.append(countDiff).append(" kali lagi perjalanan");
            }

            real.setDescription(builder.toString());
            
            String actCode = rs.getString("activitycode");
            String actName = rs.getString("activityname");
            
            if (!actName.toLowerCase().contains("kegiatan")) {
                actName = "Kegiatan "+actName;
            }

            real.setActivity(actCode+" "+actName);

            realizations.add(real);
        }

        rs.close();
        pstm.close();

        return realizations;
    }


    ArrayList<Employee> getEmployeeReport(Connection conn) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where isgorvernor = false ").
                append("and isnonemployee = false ").
                append("order by grade desc,eselon");

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
            employee.setEselon(rs.getInt("eselon"));
            employee.setCpnsTMT(rs.getDate("cpnstmt"));
            employee.setPnsTMT(rs.getDate("pnstmt"));
            employee.setGradeTMT(rs.getDate("gradetmt"));
            employee.setEselonTMT(rs.getDate("eselontmt"));
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }
    
    ArrayList<Employee> getEmployeeNonPNSReport(Connection conn,String modifier) throws SQLException {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        StringBuilder query = new StringBuilder();
        query.append("select * from employee ").
                append("where isgorvernor = false ").
                append("and isnonemployee = true ").
                append(modifier);

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
            employee.setNonEmployee(rs.getBoolean("isnonemployee"));

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
            
            employee.setMkYear(rs.getInt("mkyear"));
            employee.setMkMonth(rs.getInt("mkmonth"));

            employee.setStyled(true);

            employees.add(employee);
        }

        rs.close();
        pstm.close();

        return employees;
    }
}
