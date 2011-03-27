package org.motekar.project.civics.archieve.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Muhamad Wibawa
 */
public class CommonSQL {
    public Long getMaxIndex(Connection conn,String tableName, String fieldName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select max(").
                append(fieldName).
                append(") as maxindex ").
                append("from ").
                append(tableName);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        Long maxIndex = Long.valueOf(-1);

        while (rs.next()) {
            maxIndex  = rs.getLong("maxindex");
        }

        rs.close();
        pstm.close();

        return maxIndex;
    }


    public String getMaxCode(Connection conn,String tableName, String fieldName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select coalesce(max(").
                append(fieldName).
                append("),'') as maxcode ").
                append("from ").
                append(tableName);


        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        String maxCode = "";

        while (rs.next()) {
            maxCode  = rs.getString("maxcode");
        }

        rs.close();
        pstm.close();

        return maxCode;
    }


    public String getMaxCode(Connection conn,String tableName, String fieldName,String originalCode) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select coalesce(max(").
                append(fieldName).
                append("),'') as maxcode ").
                append("from ").
                append(tableName).
                append(" where ").
                append(fieldName).
                append(" like '%").
                append(originalCode).
                append("%'");


        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        String maxCode = "";

        while (rs.next()) {
            maxCode  = rs.getString("maxcode");
        }

        rs.close();
        pstm.close();

        return maxCode;
    }
}
