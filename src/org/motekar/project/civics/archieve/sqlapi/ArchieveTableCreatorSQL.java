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
                append("marital smallint,").
                append("soulmate text,").
                append("children smallint,").
                append("education smallint,").
                append("grade smallint,").
                append("fungsional smallint,").
                append("eselon smallint,").
                append("struktural smallint,").
                append("cpnstmt date,").
                append("pnstmt date,").
                append("gradetmt date,").
                append("eselontmt date,").
                append("positionnotes text,").
                append("isgorvernor boolean,").

                append("CONSTRAINT employee_pkey PRIMARY KEY (autoindex)").

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
                append("courses smallint,").
                append("attending boolean,").

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
                append("    REFERENCES activity (autoindex) MATCH SIMPLE").

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
                append("CONSTRAINT expeditionjournal_pkey PRIMARY KEY (journalindex),").
                append("CONSTRAINT expeditionjournal_letterindex_fkey FOREIGN KEY (letterindex)").
                append("    REFERENCES assignmentletter (autoindex) MATCH SIMPLE").
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
                append("CONSTRAINT inbox_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT inbox_mailnumber_key UNIQUE (mailnumber),").
                append("CONSTRAINT inbox_receiver_fkey FOREIGN KEY (receiver)").
                append("    REFERENCES division (code) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").
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
                append("CONSTRAINT outbox_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT outbox_mailnumber_key UNIQUE (mailnumber)").
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
                append("CONSTRAINT division_pkey PRIMARY KEY (code)").
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
                append("approvalplace text,").
                append("approvaldate date,").

                append("CONSTRAINT assignmentletter_pkey PRIMARY KEY (autoindex),").
                append("CONSTRAINT assignmentletter_commander_fkey FOREIGN KEY (commander)").
                append("    REFERENCES employee (autoindex) MATCH SIMPLE").
                append("    ON UPDATE CASCADE ON DELETE CASCADE").

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
                append("    REFERENCES standardprice (autoindex) MATCH SIMPLE").
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

}
