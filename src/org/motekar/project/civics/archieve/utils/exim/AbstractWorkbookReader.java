package org.motekar.project.civics.archieve.utils.exim;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.motekar.util.user.misc.MotekarException;
import org.openide.util.Exceptions;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */
public abstract class AbstractWorkbookReader<T> {

    public static final String STRING_TYPE = "java.lang.String";
    public static final String INTEGER_TYPE = "java.lang.Integer";
    public static final String DOUBLE_TYPE = "java.lang.Double";
    public static final String DATE_TYPE = "java.util.Date";
    public static final String BIGDECIMAL_TYPE = "java.math.BigDecimal";
    
    public List<T> loadXLSFile(String fileName) throws IOException, InvalidFormatException, MotekarException, SAXException {

        ArrayList<ColumnName> columnNames = getColumnNames();

        InputStream is = new FileInputStream(fileName);
        Workbook wb = WorkbookFactory.create(is);

        Sheet sheet = wb.getSheetAt(0);

        if (!isValidTemplate(sheet, columnNames)) {
            throw new MotekarException("File template tidak sesuai");
        }

        return getMappedObject(fileName);
    }

    private boolean isValidTemplate(Sheet sheet, ArrayList<ColumnName> columnNames) {
        boolean valid = false;
        try {
            if (!columnNames.isEmpty()) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getRowIndex() == 6) {
                            if (columnNames.contains(new ColumnName(cell.toString()))) {
                                valid = true;
                            } else {
                                valid = false;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return valid;
    }

    public ArrayList<T> loadXLSXFile(String fileName) {
        ArrayList<T> datas = new ArrayList<T>();

        return datas;
    }

    protected abstract ArrayList<ColumnName> getColumnNames();

    protected abstract List<T> getMappedObject(String fileName) throws IOException, SAXException, InvalidFormatException;
}
