package org.motekar.project.civics.archieve.sqlapi;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchieveTableCreatorSQL {

    public void installEmployeeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE employee").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("employeename text,").
                append("nip text,").
                append("birthplace text,").
                append("birthdate date,").
                append("sex smallint,").
                append("religion smallint,").
                append("marital smallint,").
                append("soulmate text,").
                append("children smallint,").
                append("education smallint,").
                append("department text,").
                append("graduatedyear integer,").
                append("grade smallint,").
                append("fungsional smallint,").
                append("eselon smallint,").
                append("struktural smallint,").
                append("cpnstmt date,").
                append("pnstmt date,").
                append("gradetmt date,").
                append("eselontmt date,").
                append("positionnotes text,").
                append("mkyear integer,").
                append("mkmonth integer,").
                append("mutation text,").
                append("isgorvernor boolean,").
                append("employeestatus smallint,").
                append("unit bigint,").
                append("picture bytea,").
                append("CONSTRAINT employee_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT employee_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Table Installed");
    }

    public void uninstallEmployeeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE employee CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Table Uninstalled");
    }

    public void installEmployeeCoursesTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE employeecourses").
                append("(").
                append("employeeindex bigint NOT NULL,").
                append("coursesname text,").
                append("yearattended integer,").
                append("totalhour integer,").
                append("coursestype smallint,").
                append("CONSTRAINT employee_employeecourses_fkey FOREIGN KEY (employeeindex)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Courses Table Installed");
    }

    public void uninstallEmployeeCoursesTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE employeecourses CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Courses Table Uninstalled");
    }

    public void installEmployeeFacilityTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE employeefacility").
                append("(").
                append("employeeindex bigint NOT NULL,").
                append("facility smallint,").
                append("owned boolean,").
                append("CONSTRAINT employee_employeefacility_fkey FOREIGN KEY (employeeindex)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Facility Table Installed");
    }

    public void uninstallEmployeeFacilitysTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE employeecourses CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Employee Facility Table Uninstalled");
    }

    public void installCurriculumVitaeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE curriculumvitae").
                append("(").
                append("cvindex bigserial NOT NULL,").
                append("employeeindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("CONSTRAINT curriculumvitae_pkey PRIMARY KEY (cvindex),").
                append("CONSTRAINT curriculumvitae_employee_fkey FOREIGN KEY (employeeindex)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Inboxfile Table Installed");
    }

    public void uninstallCurriculumVitaeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE curriculumvitae CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Inboxfile Table Uninstalled");
    }

    public void installExpeditionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE expedition").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("letterindex bigint NOT NULL,").
                append("docnumber text NOT NULL,").
                append("assignedemployee bigint NOT NULL,").
                append("departure text,").
                append("destination text,").
                append("transportation smallint,").
                append("startdate date,").
                append("enddate date,").
                append("notes text,").
                append("approvalplace text,").
                append("approvaldate date,").
                append("charge text,").
                append("program bigint,").
                append("activity bigint,").
                append("chargebudget bigint,").
                append("unit bigint,").
                append("CONSTRAINT expedition_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT expedition_assignedemployee_fkey FOREIGN KEY (assignedemployee)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT expedition_assignmentletter_fkey FOREIGN KEY (letterindex)").
                append("    REFERENCES assignmentletter (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expedition_account_fkey FOREIGN KEY (chargebudget)").
                append("    REFERENCES account (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expedition_program_fkey FOREIGN KEY (program)").
                append("    REFERENCES program (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expedition_activity_fkey FOREIGN KEY (activity)").
                append("    REFERENCES activity (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expedition_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Table Installed");
    }

    public void uninstallExpeditionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE expedition CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Table Uninstalled");
    }

    public void installExpeditionFollowerTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE expeditionfollower").
                append("(").
                append("expeditionindex bigint,").
                append("employeeindex bigint,").
                append("notes text,").
                append("CONSTRAINT expeditionfollower_expindex_fkey FOREIGN KEY (expeditionindex)").
                append("    REFERENCES expedition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT expeditionfollower_empindex_fkey FOREIGN KEY (employeeindex)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Follower Table Installed");
    }

    public void uninstallExpeditionFollowerTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE expeditionfollower CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Follower Table Uninstalled");
    }

    public void installExpeditionJournalTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE expeditionjournal").
                append("(").
                append("journalindex bigserial NOT NULL,").
                append("reportnumber text NOT NULL,").
                append("letterindex bigint NOT NULL,").
                append("reportplace text,").
                append("reportdate date NOT NULL,").
                append("unit bigint,").
                append("CONSTRAINT expeditionjournal_pkey PRIMARY KEY (journalindex),").
                append("CONSTRAINT expeditionjournal_letterindex_fkey FOREIGN KEY (letterindex)").
                append("    REFERENCES assignmentletter (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE, ").
                append("CONSTRAINT expeditionjournal_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Journal Table Installed");
    }

    public void uninstallExpeditionJournalTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE expeditionjournal CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Journal Table Uninstalled");
    }

    public void installExpeditionResultTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE expeditionresult").
                append("(").
                append("resultindex bigserial NOT NULL,").
                append("journal bigint NOT NULL,").
                append("notes text,").
                append("CONSTRAINT expeditionresult_pkey PRIMARY KEY (resultindex),").
                append("CONSTRAINT expeditionresult_journal_fkey FOREIGN KEY (journal)").
                append("    REFERENCES expeditionjournal (journalindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Result Table Installed");
    }

    public void uninstallExpeditionResultTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE expeditionresult CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Result Table Uninstalled");
    }

    public void installInboxTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE inbox").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("maildate date,").
                append("mailnumber text NOT NULL,").
                append("subject text,").
                append("sender text,").
                append("senderaddress text,").
                append("dispositiondate date,").
                append("receiver text,").
                append("unit bigint,").
                append("CONSTRAINT inbox_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT inbox_mailnumber_key UNIQUE (mailnumber),").
                append("CONSTRAINT inbox_receiver_fkey FOREIGN KEY (receiver)").
                append("    REFERENCES division (code) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT inbox_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Inbox Table Installed");
    }

    public void uninstallInboxTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE inbox CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Inbox Table Uninstalled");
    }

    public void installInboxDispositionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE inboxdisposition").
                append("(").
                append("autoindex bigserial not null,").
                append("inboxindex bigint,").
                append("division text,").
                append("receivedate date,").
                append("status smallint,").
                append("CONSTRAINT inboxdisposition_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT inboxdisposition_inbox_fkey FOREIGN KEY (inboxindex)").
                append("    REFERENCES inbox (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT inboxdisposition_division_fkey FOREIGN KEY (division)").
                append("    REFERENCES division (code) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Inbox Disposition Table Installed");
    }

    public void uninstallInboxDispositionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE inboxdisposition CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Inbox Disposition Table Uninstalled");
    }

    public void installInboxFileTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE inboxfile").
                append("(").
                append("inboxindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("CONSTRAINT inboxfile_inbox_fkey FOREIGN KEY (inboxindex)").
                append("    REFERENCES inbox (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Inboxfile Table Installed");
    }

    public void uninstallInboxFileTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE inboxfile CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Inboxfile Table Uninstalled");
    }

    public void installOutboxTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE outbox").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("maildate date,").
                append("mailnumber text NOT NULL,").
                append("subject text,").
                append("unit bigint,").
                append("CONSTRAINT outbox_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT outbox_mailnumber_key UNIQUE (mailnumber),").
                append("CONSTRAINT outbox_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Outbox Table Installed");
    }

    public void uninstallOutboxTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE outbox CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Outbox Table Uninstalled");
    }

    public void installOutboxDispositionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE outboxdisposition").
                append("(").
                append("autoindex bigserial not null,").
                append("outboxindex bigint,").
                append("receiver text,").
                append("receiveraddress text,").
                append("receivedate date,").
                append("status smallint,").
                append("CONSTRAINT outboxdisposition_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT outboxdisposition_outbox_fkey FOREIGN KEY (outboxindex)").
                append("    REFERENCES outbox (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Outbox Disposition Table Installed");
    }

    public void uninstallOutboxDispositionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE outboxdisposition CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Outbox Disposition Table Uninstalled");
    }

    public void installOutboxFileTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE outboxfile").
                append("(").
                append("outboxindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("CONSTRAINT outboxfile_inbox_fkey FOREIGN KEY (outboxindex)").
                append("    REFERENCES outbox (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Outboxfile Table Installed");
    }

    public void uninstallOutboxFileTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE outboxfile CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Outboxfile Table Uninstalled");
    }

    public void installStandarPriceTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE standardprice").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("transactionname text,").
                append("transactiontype smallint,").
                append("transporttype smallint,").
                append("departure text,").
                append("destination text,").
                append("price numeric(1000,2),").
                append("notes text,").
                append("CONSTRAINT standardprice_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Standar Price Table Installed");
    }

    public void uninstallStandarPriceTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE outbox CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Standar Price Table Uninstalled");
    }

    public void installDivisionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE division").
                append("(").
                append("code text NOT NULL,").
                append("divisionname text,").
                append("parentcode text,").
                append("unit bigint,").
                append("CONSTRAINT division_pkey PRIMARY KEY (code),").
                append("CONSTRAINT division_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Division Table Installed");
    }

    public void uninstallDivisionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE division CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Division Table Uninstalled");
    }

    public void installAssignmentLetterTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE assignmentletter").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("docnumber text NOT NULL,").
                append("commander bigint NOT NULL,").
                append("purpose text,").
                append("goals text,").
                append("startdate date,").
                append("enddate date,").
                append("notes text,").
                append("approvalplace text,").
                append("approvaldate date,").
                append("unit bigint,").
                append("CONSTRAINT assignmentletter_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT assignmentletter_commander_fkey FOREIGN KEY (commander)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT assigmentletter_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Assignment Letter Table Installed");
    }

    public void uninstallAssignmentLetterTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE assignmentletter CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Assignment Letter Table Uninstalled");
    }

    public void installAssignedEmployeeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE assignedemployee").
                append("(").
                append("assignmentindex  bigint,").
                append("employeeindex bigint,").
                append("CONSTRAINT assignedemployee_assignmentindex_fkey FOREIGN KEY (assignmentindex)").
                append("    REFERENCES assignmentletter (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT assignedemployee_employeeindex_fkey FOREIGN KEY (employeeindex)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Assigned Employee Table Installed");
    }

    public void uninstallAssignedEmployeeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE assignedemployee CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Assigned Employee Table Uninstalled");
    }

    public void installCarbonCopyTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE carboncopy").
                append("(").
                append("assignmentindex bigint,").
                append("copyto text,").
                append("CONSTRAINT carboncopy_assignmentindex_fkey FOREIGN KEY (assignmentindex)").
                append("    REFERENCES assignmentletter (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Carbon Copy Table Installed");
    }

    public void uninstallCarbonCopyTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE carboncopy CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Carbon Copy Table Uninstalled");
    }

    public void installAccountTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE account").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("accountcode text,").
                append("accountname text,").
                append("accountcategory smallint,").
                append("accounttype smallint,").
                append("accountgroup boolean,").
                append("CONSTRAINT account_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Account Table Installed");
    }

    public void uninstallAccountTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE account CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Account Table Uninstalled");
    }

    public void installAccountStructureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE accountstructure").
                append("(").
                append("superaccount bigint NOT NULL,").
                append("subaccount bigint NOT NULL,").
                append("CONSTRAINT accountstructure_superaccount_fkey FOREIGN KEY (superaccount)").
                append("    REFERENCES account (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT accountstructure_subaccount_fkey FOREIGN KEY (subaccount)").
                append("    REFERENCES account (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Account Structure Table Installed");
    }

    public void uninstallAccountStructureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE accountstructure CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Account Structure Table Uninstalled");
    }

    public void installProgramTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE program").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("programcode text,").
                append("programname text,").
                append("CONSTRAINT program_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Program Table Installed");
    }

    public void uninstallProgramTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE program CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Program Table Uninstalled");
    }

    public void installActivityTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE activity").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("programindex bigint,").
                append("activitycode text,").
                append("activityname text,").
                append("CONSTRAINT activity_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT activity_program_fkey FOREIGN KEY (programindex)").
                append("    REFERENCES program (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Activity Table Installed");
    }

    public void uninstallActivityTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE program CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Activity Table Uninstalled");
    }

    public void installExpeditionChequeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE expeditioncheque").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("expeditionindex bigint,").
                append("payer text,").
                append("bankaccount text,").
                append("taxnumber text,").
                append("receivedfrom text,").
                append("amount bigint,").
                append("notes text,").
                append("receivedplace text,").
                append("heademployee bigint,").
                append("pptk bigint,").
                append("clerk bigint,").
                append("paidto bigint,").
                append("budgettype smallint,").
                append("unit bigint,").
                append("CONSTRAINT expeditioncheque_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT expeditioncheque_expedition_fkey FOREIGN KEY (expeditionindex)").
                append("    REFERENCES expedition (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_heademployee_fkey FOREIGN KEY (heademployee)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_pptk_fkey FOREIGN KEY (pptk)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_clerk_fkey FOREIGN KEY (clerk)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_paidto_fkey FOREIGN KEY (paidto)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_amount_fkey FOREIGN KEY (amount)").
                append("    REFERENCES standardprice (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT expeditioncheque_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Cheque Table Installed");
    }

    public void uninstallExpeditionChequeTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE expeditioncheque CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Expedition Cheque Table Uninstalled");
    }

    public void installBudgetTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE budget").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("years smallint,").
                append("budgettype smallint,").
                append("CONSTRAINT budget_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Budget Table Installed");
    }

    public void uninstallBudgetTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE budget CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Budget Table Uninstalled");
    }

    public void installBudgetDetailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE budgetdetail").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("budgetindex bigint NOT NULL,").
                append("programindex bigint NOT NULL,").
                append("CONSTRAINT budgetdetail_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT budgetdetail_budget_fkey FOREIGN KEY (budgetindex)").
                append("    REFERENCES budget (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT budgetdetail_program_fkey FOREIGN KEY (programindex)").
                append("    REFERENCES program (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("BudgetDetail Table Installed");
    }

    public void uninstallBudgetDetailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE budgetdetail CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("BudgetDetail Table Uninstalled");
    }

    public void installBudgetSubDetailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE budgetsubdetail").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("detailindex bigint NOT NULL,").
                append("activityindex bigint NOT NULL,").
                append("amount numeric(1000,2),").
                append("CONSTRAINT budgetsubdetail_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT budgetsubdetail_budgetdetail_fkey FOREIGN KEY (detailindex)").
                append("    REFERENCES budgetdetail (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT budgetsubdetail_program_fkey FOREIGN KEY (activityindex)").
                append("    REFERENCES activity (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("BudgetSubDetail Table Installed");
    }

    public void uninstallBudgetSubDetailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE budgetsubdetail CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("BudgetSubDetail Table Uninstalled");
    }

    // Assets
    public void installItemsCategoryTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemscategory").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("categorycode text,").
                append("categoryname text,").
                append("isgroup boolean,").
                append("types smallint,").
                append("CONSTRAINT itemcategory_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Items Category Table Installed");
    }

    public void uninstallItemsCategoryTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemscategory CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Items Category Table Uninstalled");
    }

    public void installItemsCategoryStructureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemscategorystructure").
                append("(").
                append("supercategory bigint NOT NULL,").
                append("subcategory bigint NOT NULL,").
                append("CONSTRAINT itemscategorystructure_supercategory_fkey FOREIGN KEY (supercategory)").
                append("    REFERENCES itemscategory (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemscategorystructure_subcategory_fkey FOREIGN KEY (subcategory)").
                append("    REFERENCES itemscategory (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Items Category Structure Table Installed");
    }

    public void uninstallItemsCategoryStructureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemscategorystructure CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Items Category Structure Table Uninstalled");
    }

    public void installRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE room").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("roomcode text,").
                append("roomname text,").
                append("CONSTRAINT room_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Room Table Installed");
    }

    public void uninstallRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE room CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Room Table Uninstalled");
    }

    public void installConditionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE condition").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("conditioncode text,").
                append("conditionname text,").
                append("startvalue integer,").
                append("endvalue integer,").
                append("CONSTRAINT condition_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Condition Table Installed");
    }

    public void uninstallConditionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE condition CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Condition Table Uninstalled");
    }

    public void installItemsLandTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsland").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("wide numeric(1000,2),").
                append("addresslocation text,").
                append("urban bigint,").
                append("suburban bigint,").
                append("landprice numeric(1000,2),").
                append("acquisitionyear integer,").
                append("fundingsources text,").
                append("acquisitionway text,").
                append("description text,").
                append("ownershipstatename text,").
                append("ownershipnumberstate text,").
                append("ownershipdate date,").
                append("ownershipissuedby text,").
                append("owners text,").
                append("ownershipstatus text,").
                append("ownershiprelation text,").
                append("ownershipoccupancy text,").
                append("ownershipoccupying text,").
                append("landcertificatenumber text,").
                append("landcertificatedate date,").
                append("landtaxnumber text,").
                append("landtaxdate date,").
                append("buildpermitnumber text,").
                append("buildpermitdate date,").
                append("advisplanningnumber text,").
                append("advisplanningdate date,").
                append("condition text,").
                append("shape text,").
                append("topographcondition text,").
                append("currentuse text,").
                append("closerroad text,").
                append("roadsurface text,").
                append("heightabove text,").
                append("heightunder text,").
                append("elevation text,").
                append("allotment text,").
                append("pricebasedchief numeric(1000,2),").
                append("pricebasednjop numeric(1000,2),").
                append("pricebasedexamination numeric(1000,2),").
                append("pricedescription text,").
                append("locations text,").
                append("positions text,").
                append("roadaccess text,").
                append("roadwidth text,").
                append("pavement text,"). // Perkerasan
                append("traffic text,").
                append("topographlocation text,").
                append("environment text,").
                append("density text,").
                append("sociallevels text,").
                append("facilities text,").
                append("drainage text,").
                append("publictransportation text,").
                append("securitys text,").
                append("securitydisturbance text,").
                append("flooddangers text,").
                append("locationeffect text,").
                append("environmentalinfluences text,").
                append("allotmentpoint text,").
                append("utilizationpoint text,").
                append("locationpoint text,").
                append("accessionpoint text,").
                append("nursingpoint text,").
                append("soilconditionspoint text,").
                append("marketinterestpoint text,").
                append("comparativeprice1 numeric(1000,2),").
                append("comparativewide1 numeric(1000,2),").
                append("comparativesource1 text,").
                append("comparativeprice2 numeric(1000,2),").
                append("comparativewide2 numeric(1000,2),").
                append("comparativesource2 text,").
                append("comparativeprice3 numeric(1000,2),").
                append("comparativewide3 numeric(1000,2),").
                append("comparativesource3 text,").
                append("comparativeprice4 numeric(1000,2),").
                append("comparativewide4 numeric(1000,2),").
                append("comparativesource4 text,").
                append("comparativeprice5 numeric(1000,2),").
                append("comparativewide5 numeric(1000,2),").
                append("comparativesource5 text,").
                append("landaddress text,").
                append("njopyear integer,").
                append("njoplandwide numeric(1000,2),").
                append("njoplandclass text,").
                append("njoplandvalue numeric(1000,2),").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsland_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsland_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsland_urban_fkey FOREIGN KEY (urban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsland_suburban_fkey FOREIGN KEY (suburban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsLand Table Installed");
    }

    public void uninstallItemsLandTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsland CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsLand Table Uninstalled");
    }

    public void installItemsMachineTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsmachine").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("bpkbnumber text,").
                append("machineType text,").
                append("volume integer,").
                append("factorynumber text,").
                append("fabricnumber text,").
                append("machinenumber text,").
                append("policenumber text,").
                append("acquisitionyear integer,").
                append("yearbuilt integer,").
                append("location text,").
                append("presentlocation text,").
                append("material text,").
                append("condition bigint,").
                append("price numeric(1000,2),").
                append("admincost numeric(1000,2),").
                append("fundingsources text,").
                append("acquisitionway text,").
                append("description text,").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsmachine_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsmachine_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsmachine_condition_fkey FOREIGN KEY (condition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsMachine Table Installed");
    }

    public void uninstallItemsMachineTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsmachine CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsMachine Table Uninstalled");
    }

    public void installItemsBuildingTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsbuilding").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("wide numeric(1000,2),").
                append("israised text,").
                append("condition bigint,").
                append("buildingprice numeric(1000,2),").
                append("admincost numeric(1000,2),").
                append("acquisitionyear integer,").
                append("address text,").
                append("urban bigint,").
                append("suburban bigint,").
                append("documentdate date,").
                append("documentnumber text,").
                append("landwide numeric(1000,2),").
                append("landcode text,").
                append("landstate text,").
                append("fundingsources text,").
                append("acquisitionway text,").
                append("description text,").
                append("eastlongitudecoord text,").
                append("southlatitudecoord text,").
                append("ownershipstatename text,").
                append("ownershiprelation text,").
                append("ownershipoccupancy text,").
                append("ownershipoccupying text,").
                append("landtaxnumber text,").
                append("landtaxdate date,").
                append("buildpermitnumber text,").
                append("buildpermitdate date,").
                append("advisplanningnumber text,").
                append("advisplanningdate date,").
                append("types text,").
                append("shape text,").
                append("utilization text,").
                append("ages text,").
                append("levels text,").
                append("framework text,").
                append("foundation text,").
                append("floortype text,").
                append("walltype text,").
                append("ceilingtype text,").
                append("rooftype text,").
                append("frametype text,").
                append("doortype text,").
                append("windowtype text,").
                append("roomtype text,").
                append("materialquality text,").
                append("facilities text,").
                append("buildingcondition text,").
                append("nursing text,").
                append("buildingclass text,").
                append("allotmentpoint text,").
                append("utilizationpoint text,").
                append("locationpoint text,").
                append("accessionpoint text,").
                append("qualitypoint text,").
                append("conditionpoint text,").
                append("layoutpoint text,").
                append("nursingpoint text,").
                append("marketinterestpoint text,").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsbuilding_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsbuilding_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsbuilding_condition_fkey FOREIGN KEY (condition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsbuilding_urban_fkey FOREIGN KEY (urban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsbuilding_suburban_fkey FOREIGN KEY (suburban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsBuilding Table Installed");
    }

    public void uninstallItemsBuildingTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsbuilding CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsBuilding Table Uninstalled");
    }

    public void installItemsNetworkTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsnetwork").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("documentnumber text,").
                append("documentdate date,").
                append("constructiontype text,").
                append("lengths integer,").
                append("width integer,").
                append("wide numeric(1000,2),").
                append("price numeric(1000,2),").
                append("condition bigint,").
                append("addresslocation text,").
                append("urban bigint,").
                append("suburban bigint,").
                append("landstatus text,").
                append("landcode text,").
                append("fundingsources text,").
                append("acquisitionway text,").
                append("description text,").
                append("styearbuilt integer,").
                append("stlocation text,").
                append("stflatness text,").
                append("ststartkm integer,").
                append("stendkm integer,").
                append("stwidth text,").
                append("stsurface text,").
                append("stside text,").
                append("sttrotoire text,").
                append("stchannel text,").
                append("stsafetyzone boolean,").
                append("brdgstandardtype text,").
                append("brdgyearbuild integer,").
                append("brdglengths integer,").
                append("brdgwidth integer,").
                append("brdgpurpose text,").
                append("brdgmainmaterial text,").
                append("brdgtopshape text,").
                append("brdgothershape text,").
                append("brdgheadshape text,").
                append("brdgheadmaterial text,").
                append("brdgpillar text,").
                append("brdgpillarmaterial text,").
                append("irryearbuilt integer,").
                append("irrbuildingtype text,").
                append("irrlengths integer,").
                append("irrheight integer,").
                append("irrwidth integer,").
                append("irrbuildingmaterial text,").
                append("irrcondition bigint,").
                // Pembawa

                append("irrcarriertype text,").
                append("irrcarrierlengths integer,").
                append("irrcarrierheight integer,").
                append("irrcarrierwidth integer,").
                append("irrcarriermaterial text,").
                append("irrcarriercondition bigint,").
                // Pengukur Debit

                append("irrdebitthresholdwidth integer,").
                append("irrdebitcdg integer,").
                append("irrdebitcipolleti integer,").
                append("irrdebitlengths integer,").
                append("irrdebitheight integer,").
                append("irrdebitwidth integer,").
                append("irrdebitbuildingmaterial text,").
                append("irrdebitcondition bigint,").
                // Bangunan Kolam Olak

                append("irrpoolusbr3 integer,").
                append("irrpoolblock integer,").
                append("irrpoolusbr4 integer,").
                append("irrpoolvlogtor integer,").
                append("irrpoollengths integer,").
                append("irrpoolheight integer,").
                append("irrpoolwidth integer,").
                append("irrpoolbuildingmaterial text,").
                append("irrpoolcondition bigint,").
                // Tinggi Muka Air
                append("irrhwfslotblock integer,").
                append("irrhwftrapesium integer,").
                append("irrhwfslide integer,").
                append("irrhwftreetop integer,").
                append("irrhwflengths integer,").
                append("irrhwfheight integer,").
                append("irrhwfwidth integer,").
                append("irrhwfbuildingmaterial text,").
                append("irrhwfcondition bigint,").
                // Bangunan Lindung

                append("irrprottransfer integer,").
                append("irrprotdisposal integer,").
                append("irrprotdrain integer,").
                append("irrprotlengths integer,").
                append("irrprotheight integer,").
                append("irrprotwidth integer,").
                append("irrprotbuildingmaterial text,").
                append("irrprotcondition bigint,").
                // Bangunan Bagi dan Sadap

                append("irrtapfor integer,").
                append("irrtapsecond integer,").
                append("irrtapregulator integer,").
                append("irrtapthird integer,").
                append("irrtaplength integer,").
                append("irrtapheight integer,").
                append("irrtapwidth integer,").
                append("irrtapbuildingmaterial text,").
                append("irrtapcondition bigint,").
                // Bangunan Pelengkap

                append("irrsupplevee integer,").
                append("irrsuppwash integer,").
                append("irrsuppbridge integer,").
                append("irrsuppkrib integer,").
                append("irrsupplength integer,").
                append("irrsuppheight integer,").
                append("irrsuppwidth integer,").
                append("irrsuppbuildingmaterial text,").
                append("irrsuppcondition bigint,").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsnetwork_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsnetwork_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition_fkey FOREIGN KEY (condition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition2_fkey FOREIGN KEY (irrcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition3_fkey FOREIGN KEY (irrdebitcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition4_fkey FOREIGN KEY (irrpoolcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition5_fkey FOREIGN KEY (irrhwfcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition6_fkey FOREIGN KEY (irrprotcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition7_fkey FOREIGN KEY (irrtapcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_condition8_fkey FOREIGN KEY (irrsuppcondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE,").
                append("CONSTRAINT itemsnetwork_condition9_fkey FOREIGN KEY (irrcarriercondition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_urban_fkey FOREIGN KEY (urban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsnetwork_suburban_fkey FOREIGN KEY (suburban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsNetwork Table Installed");
    }

    public void uninstallItemsNetworkTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsnetwork CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsNetwork Table Uninstalled");
    }

    public void installItemsFixedAssetTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsfixedasset").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("bookauthor text,").
                append("bookspec text,").
                append("artregion text,").
                append("artauthor text,").
                append("artmaterial text,").
                append("cattletype text,").
                append("cattlesize text,").
                append("price numeric(1000,2),").
                append("acquisitionyear integer,").
                append("condition bigint,").
                append("fundingsources text,").
                append("acquisitionway text,").
                append("description text,").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsfixedasset_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsfixedasset_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsfixedasset_condition_fkey FOREIGN KEY (condition)").
                append("    REFERENCES condition (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsFixedAsset Table Installed");
    }

    public void uninstallItemsFixedAssetTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsfixedasset CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsFixedAsset Table Uninstalled");
    }

    public void installItemsConstructionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsconstruction").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("unit bigint,").
                append("itemcode text,").
                append("itemname text,").
                append("regnumber text,").
                append("documentnumber text,").
                append("documentdate date,").
                append("buildingcategory text,").
                append("israised text,").
                append("framework text,").
                append("buildingwide numeric(1000,2),").
                append("worktype text,").
                append("price numeric(1000,2),").
                append("addresslocation text,").
                append("urban bigint,").
                append("suburban bigint,").
                append("startdate date,").
                append("landstatus text,").
                append("landcode text,").
                append("fundingsources text,").
                append("description text,").
                append("username text,").
                append("lastusername text,").
                append("CONSTRAINT itemsconstruction_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsconstruction_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsconstruction_urban_fkey FOREIGN KEY (urban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsconstruction_suburban_fkey FOREIGN KEY (suburban)").
                append("    REFERENCES region (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsConstruction Table Installed");
    }

    public void uninstallItemsConstructionTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsconstruction CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsConstruction Table Uninstalled");
    }

    public void installItemsMachineRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsmachineroom").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("room bigint,").
                append("items bigint,").
                append("CONSTRAINT itemsmachineroom_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsmachineroom_room_fkey FOREIGN KEY (room)").
                append("    REFERENCES room (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsmachineroom_items_fkey FOREIGN KEY (items)").
                append("    REFERENCES itemsmachine (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("itemsmachine Table Installed");
    }

    public void uninstallItemsMachineRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsmachineroom CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("itemsmachine Table Uninstalled");
    }

    public void installItemsFixedAssetRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsfixedassetroom").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("room bigint,").
                append("items bigint,").
                append("CONSTRAINT itemsfixedassetroom_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT itemsfixedassetroom_room_fkey FOREIGN KEY (room)").
                append("    REFERENCES room (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE,").
                append("CONSTRAINT itemsfixedassetroom_items_fkey FOREIGN KEY (items)").
                append("    REFERENCES itemsfixedasset (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("itemsfixedassetroom Table Installed");
    }

    public void uninstallItemsFixedAssetRoomTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsfixedassetroom CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("itemsfixedassetroom Table Uninstalled");
    }

    //
    public void installConfigTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE configurationsetting").
                append("(").
                append("ckey text NOT NULL,").
                append("cvalue text,").
                append("cvalue2 bytea,").
                append("CONSTRAINT configutationsetting_pkey PRIMARY KEY (ckey)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("configurationsetting Table Installed");
    }

    public void uninstallConfigTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE configurationsetting CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("configurationsetting Table Uninstalled");
    }

    public void installDocumentsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE documents").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("description text,").
                append("CONSTRAINT documents_pkey PRIMARY KEY (autoindex)").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Documents Table Installed");
    }

    public void uninstallDocumentsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE documents CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Documents Table Uninstalled");
    }
    //

    public void installProcurementTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE procurement").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("itemscode text,").
                append("itemsname text,").
                append("contractdate date,").
                append("contractnumber text,").
                append("documentdate date,").
                append("documentnumber text,").
                append("amount integer,").
                append("price numeric(1000,2),").
                append("unit bigint NOT NULL,").
                append("description text,").
                append("CONSTRAINT procurement_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT procurement_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Procurement Table Installed");
    }

    public void uninstallProcurementTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE procurement CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Procurement Table Uninstalled");
    }

    public void installApprovalBookTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE approvalbook").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("receivedate date NOT NULL,").
                append("receivefrom text,").
                append("invoicenumber text,").
                append("invoicedate date,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("amount integer,").
                append("price numeric(1000,2),").
                append("documentnumber text,").
                append("documentdate date,").
                append("description text,").
                append("warehouse bigint,").
                append("contractdate date,").
                append("contractnumber text,").
                append("CONSTRAINT approvalbook_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT approvalbook_unit_fkey FOREIGN KEY (warehouse)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Procurement Table Installed");
    }

    public void uninstallApprovalBookTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE approvalbook CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ApprovalBook Table Uninstalled");
    }

    public void installSignerTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE signer").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("signername text NOT NULL,").
                append("signernip text,").
                append("signertype smallint,").
                append("CONSTRAINT approvalbooksigner_pkey PRIMARY KEY (autoindex) ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Signer Table Installed");
    }

    public void uninstallSignerTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE signer CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Signer Table Uninstalled");
    }

    public void installReleaseBookTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE releasebook").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("releasedate date NOT NULL,").
                append("sortingnumber text,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("billnumber text,").
                append("billdate date,").
                append("amount integer,").
                append("price numeric(1000,2),").
                append("allotment text,").
                append("delegatedate date,").
                append("description text,").
                append("warehouse bigint,").
                append("CONSTRAINT releasebook_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT releasebook_unit_fkey FOREIGN KEY (warehouse)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("Releasebook Table Installed");
    }

    public void uninstallReleasebookTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE releasebook CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("Releasebook Table Uninstalled");
    }

    public void installItemsCardTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemcard").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("itemcode text NOT NULL,").
                append("itemname text NOT NULL,").
                append("specification text,").
                append("CONSTRAINT itemcard_pkey PRIMARY KEY (autoindex) ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsCard Table Installed");
    }

    public void uninstallItemsCardTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemcard CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsCard Table Uninstalled");
    }

    public void installInventoryCardTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE inventorycard").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("warehouse bigint NOT NULL,").
                append("itemcode text NOT NULL,").
                append("itemname text NOT NULL,").
                append("units text,").
                append("cardnumber text,").
                append("spesification text,").
                append("CONSTRAINT inventorycard_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT inventorycard_unit_fkey FOREIGN KEY (warehouse)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("InventoryCard Table Installed");
    }

    public void uninstallInventoryCardTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE inventorycard CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("InventoryCard Table Uninstalled");
    }
    
    public void installThirdPartyItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE thirdpartyitems").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("specificationtype text,").
                append("specificationnumber text,").
                append("acquisitionyear integer,").
                append("acquisitionway text,").
                append("thirdpartyname text,").
                append("itemsunit text,").
                append("condition text,").
                append("amount integer,").
                append("price numeric(1000,2),").
                append("description text,").
                append("unit bigint,").
                append("CONSTRAINT thirdparty_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT thirdpartyitems_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ThirdPartyItems Table Installed");
    }

    public void uninstallThirdPartyItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE thirdpartyitems CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ThirdPartyItems Table Uninstalled");
    }
    
    //
    
    public void installUnrecycledItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE unrecycleditems").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("receivedate date NOT NULL,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("specification text,").
                append("productionyear integer,").
                append("receiveamount integer,").
                append("contractdate date,").
                append("contractnumber text,").
                append("documentnumber text,").
                append("documentdate date,").
                append("releasedate date,").
                append("submitted text,").
                append("releaseamount integer,").
                append("releasedocumentnumber text,").
                append("releasedocumentdate date,").
                append("description text,").
                append("unit bigint,").
                append("CONSTRAINT unrecycleditems_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT unrecycleitems_unit_fkey FOREIGN KEY (unit)").
                append("    REFERENCES unit (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("UnrecycledItems Table Installed");
    }

    public void uninstallUnrecycledItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE unrecycleditems CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("UnrecycledItems Table Uninstalled");
    }
    
    public void installDeleteDraftItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE deletedraftitems").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("itemsindex bigint,").
                append("itemstype smallint,").
                append("locationcode text,").
                append("specificationtype text,").
                append("owneddocument text,").
                append("acqusitionyear integer,").
                append("price numeric(1000,2),").
                append("condition text,").
                append("description text,").
                append("CONSTRAINT deletedraftitems_pkey PRIMARY KEY (autoindex) ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("DeleteDraftItems Table Installed");
    }

    public void uninstallDeleteDraftItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE deletedraftitems CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("DeleteDraftItems Table Uninstalled");
    }
    
    
    public void installUsedItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE useditems").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("itemscode text NOT NULL,").
                append("itemsname text,").
                append("itemsindex bigint,").
                append("itemstype smallint,").
                append("locationcode text,").
                append("regnumber text,").
                append("documentnumber text,").
                append("itemsaddress text,").
                append("acqusitionway text,").
                append("acqusitionyear integer,").
                append("construction text,").
                append("condition text,").
                append("wide numeric(1000,2),").
                append("price numeric(1000,2),").
                append("decreenumber text,").
                append("cooperationperiod text,").
                append("thirdpartyaddress text,").
                append("description text,").
                append("CONSTRAINT useditems_pkey PRIMARY KEY (autoindex) ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("UsedItems Table Installed");
    }

    public void uninstallUsedItemsTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE useditems CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("UsedItems Table Uninstalled");
    }
    
    //
    
    public void installItemsLandPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemslandpicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemslandpicture_itemsland_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsland (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsLandPicture Table Installed");
    }

    public void uninstallItemsLandPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemslandpicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsLandPicture Table Uninstalled");
    }
    
    public void installItemsMachinePictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsmachinepicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemsmachinepicture_itemsmachine_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsmachine (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsMachinePicture Table Installed");
    }

    public void uninstallItemsMachinePictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsmachinepicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsMachinePicture Table Uninstalled");
    }
    
    public void installItemsBuildingPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsbuildingpicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemsbuildingpicture_itemsbuilding_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsbuilding (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsBuildingPicture Table Installed");
    }

    public void uninstallItemsBuildingPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsbuildingpicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsBuildingPicture Table Uninstalled");
    }
    
    public void installItemsNetworkPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsnetworkpicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemsnetworkpicture_itemsnetwork_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsnetwork (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsNetworkPicture Table Installed");
    }

    public void uninstallItemsNetworkPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsnetworkpicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsNetworkPicture Table Uninstalled");
    }
    
    public void installItemsFixedAssetPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsfixedassetpicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemsfixedassetpicture_itemsfixedasset_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsfixedasset (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsFixedAssetPicture Table Installed");
    }

    public void uninstallItemsFixedAssetPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsfixedassetpicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsFixedAssetPicture Table Uninstalled");
    }
    
    public void installItemsConstructionPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE itemsconstructionpicture").
                append("(").
                append("itemindex bigint,").
                append("filename text NOT NULL,").
                append("filebyte bytea,").
                append("filetype smallint,").
                append("CONSTRAINT itemsconstructionpicture_itemsconstruction_fkey FOREIGN KEY (itemindex)").
                append("    REFERENCES itemsconstruction (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsConstructionPicture Table Installed");
    }

    public void uninstallItemsConstructionPictureTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE itemsconstructionpicture CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("ItemsFixedAssetPicture Table Uninstalled");
    }
    
    //
    
    public void installSicknessMailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE sicknessmail").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("documentnumber text,").
                append("patiencename text,").
                append("patienceage integer,").
                append("jobs text,").
                append("address text,").
                append("startdate date,").
                append("enddate date,").
                append("approvalplace text,").
                append("approvaldate date,").
                append("approval bigint,").
                append("CONSTRAINT sicknessmail_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT sicknessmail_employee_fkey FOREIGN KEY (approval)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("SicknessMail Table Installed");
    }

    public void uninstallSicknessMailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE sicknessmail CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("SicknessMail Table Uninstalled");
    }
    
    public void installDoctorMailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE doctormail").
                append("(").
                append("autoindex bigserial NOT NULL,").
                append("documentnumber text,").
                append("patiencename text,").
                append("birthplace text,").
                append("birthdate date,").
                append("jobs text,").
                append("address text,").
                append("requested text,").
                append("maildate date,").
                append("mailnumber text,").
                append("checked text,").
                append("term text,").
                append("height integer,").
                append("weight integer,").
                append("bloodpreasure integer,").
                append("bloodpreasure2 integer,").
                append("expireddate date,").
                append("approvalplace text,").
                append("approvaldate date,").
                append("approval bigint,").
                append("CONSTRAINT doctormail_pkey PRIMARY KEY (autoindex), ").
                append("CONSTRAINT doctormail_employee_fkey FOREIGN KEY (approval)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE ").
                append(")");

        stm.executeUpdate(query.toString());
        System.out.println("DoctorMail Table Installed");
    }

    public void uninstallDoctorMailTable(Statement stm) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE doctormail CASCADE");

        stm.executeUpdate(query.toString());
        System.out.println("DoctorMail Table Uninstalled");
    }
}
