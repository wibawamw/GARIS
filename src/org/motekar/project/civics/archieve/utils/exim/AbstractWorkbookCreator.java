package org.motekar.project.civics.archieve.utils.exim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public abstract class AbstractWorkbookCreator<T> {

    public static final String STRING_TYPE = "java.lang.String";
    public static final String INTEGER_TYPE = "java.lang.Integer";
    public static final String DOUBLE_TYPE = "java.lang.Double";
    public static final String DATE_TYPE = "java.util.Date";
    public static final String BIGDECIMAL_TYPE = "java.math.BigDecimal";
    private int rowCount = 0;
    private int columnCount = 0;
    private int titleGap = 0;
    private int subTitleGap = 0;
    protected PropertyChangeSupport support = new PropertyChangeSupport(this);

    protected abstract Map<String, ColumnName> getColumnHeader();
    protected abstract String getTitle();
    protected abstract void createContent(Workbook wb, Sheet sheet, Font font, List<T> data);

    protected abstract Map<String, String> getSubTitle();
    private String fileName;
    private List<T> datas;
    private List<Unit> lookupData;
    //
    private Name namedRange;
    private CellRangeAddressList addressList;
    protected ArchieveMainframe mainframe;

    public AbstractWorkbookCreator(ArchieveMainframe mainframe, String fileName, List<T> datas, List<Unit> lookupData) {
        this.mainframe = mainframe;
        this.fileName = fileName;
        this.datas = datas;
        this.lookupData = lookupData;
    }

    public void createXLSFile() throws IOException {

        Workbook wb = new HSSFWorkbook(); //or new HSSFWorkbook();

        Font fontPlain = wb.createFont();
        fontPlain.setFontHeightInPoints((short) 10);

        Font fontBold = wb.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontBold.setFontHeightInPoints((short) 12);

        Font fontBold2 = wb.createFont();
        fontBold2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontBold2.setFontHeightInPoints((short) 10);

        Sheet sheet = wb.createSheet("Sheet1");
        Sheet sheet2 = wb.createSheet("Sheet2");

        Map<String, ColumnName> columnHeader = getColumnHeader();
        String title = getTitle();
        Map<String, String> subTitle = getSubTitle();

        createLookUpContent(wb, sheet2, fontPlain, lookupData);

        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint("MyList");
        DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
        dataValidation.setSuppressDropDownArrow(false);
        sheet.addValidationData(dataValidation);


        createTitle(wb, sheet, fontBold, title, columnHeader);
        createSubTitle(wb, sheet, fontBold, subTitle);
        createColumnHeader(wb, sheet, fontBold2, columnHeader);

        if (!datas.isEmpty()) {
            createContent(wb, sheet, fontPlain, datas);
        } else {
            try {
                createEmptyContent(wb, sheet, fontPlain, columnHeader);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        int columnSize = columnHeader.size();

        for (int i = 0; i < columnSize; i++) {
            sheet.autoSizeColumn(i, true);
        }

        for (int i = 0; i < 2; i++) {
            sheet2.autoSizeColumn(i, true);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(fileName);
        wb.write(fileOut);
        fileOut.close();

        System.out.println("Write File Success");
    }

    private void createTitle(Workbook wb, Sheet sheet, Font font, String titleName, Map<String, ColumnName> columnHeader) {
        Row row = sheet.createRow((short) getRowCount());
        row.setHeightInPoints(30);

        if (wb instanceof HSSFWorkbook) {
            createCell(wb, row, (short) 0, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, titleName, font, false, false);
            incrementRowCount(1);
        } else if (wb instanceof XSSFWorkbook) {
            createCell(wb, row, (short) 0, XSSFCellStyle.ALIGN_CENTER, XSSFCellStyle.VERTICAL_CENTER, titleName, font, false, false);
            incrementRowCount(1);
        }

        incrementRowCount(1);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                getRowCount(), //last row  (0-based)
                0, //first column (0-based)
                columnHeader.size() - 1//last column  (0-based)
                ));

        incrementRowCount(1);
    }

    private void createSubTitle(Workbook wb, Sheet sheet, Font font, Map<String, String> subTitle) {
        if (wb instanceof HSSFWorkbook) {
            Set<String> keys = subTitle.keySet();

            if (!keys.isEmpty()) {
                for (String key : keys) {
                    Row row = sheet.createRow((short) getRowCount());
                    row.setHeightInPoints(20);
                    createCell(wb, row, (short) 1, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, key, font, false, false);
                    incrementRowCount(1);
                }
                sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 4));
                //
            }
        } else if (wb instanceof XSSFWorkbook) {
            Set<String> keys = subTitle.keySet();

            if (!keys.isEmpty()) {
                for (String key : keys) {
                    Row row = sheet.createRow((short) getRowCount());
                    row.setHeightInPoints(20);
                    createCell(wb, row, (short) 1, XSSFCellStyle.ALIGN_LEFT, XSSFCellStyle.VERTICAL_CENTER, key, font, false, false);
                    incrementRowCount(1);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 4));
            //
        }
        incrementRowCount(1);
    }

    private void createColumnHeader(Workbook wb, Sheet sheet, Font font, Map<String, ColumnName> columnHeader) {
        Row row = sheet.createRow((short) getRowCount());
        row.setHeightInPoints(55);

        if (wb instanceof HSSFWorkbook) {
            Set<String> keys = columnHeader.keySet();

            SortedSet<String> kk = new TreeSet<String>();
            kk.addAll(keys);

            if (!kk.isEmpty()) {
                int col = 0;
                for (String key : kk) {
                    createCell(wb, row, (short) col++, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, columnHeader.get(key).getName(), font, true, true);
                }
            }
        } else if (wb instanceof XSSFWorkbook) {
            Set<String> keys = columnHeader.keySet();

            SortedSet<String> kk = new TreeSet<String>();
            kk.addAll(keys);

            if (!kk.isEmpty()) {
                int col = 0;
                for (String key : kk) {
                    createCell(wb, row, (short) col++, XSSFCellStyle.ALIGN_CENTER, XSSFCellStyle.VERTICAL_CENTER, columnHeader.get(key).getName(), font, true, true);
                }
            }
        }
        incrementRowCount(1);
    }

    private void createEmptyContent(Workbook wb, Sheet sheet, Font font, Map<String, ColumnName> columnHeader) throws Exception {
        if (wb instanceof HSSFWorkbook) {
            int vSize = 100;

            Comparator<ColumnName> comparator = new Comparator<ColumnName>() {

                public int compare(ColumnName o1, ColumnName o2) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
            };

            ArrayList<ColumnName> ch = new ArrayList<ColumnName>();
            ch.addAll(columnHeader.values());

            Collections.sort(ch, comparator);

            int colSize = ch.size();
            int rc = getRowCount();

            for (int i = 0; i < vSize; i++) {
                Row row = sheet.createRow((short) rc++);
                row.setHeightInPoints(15);
                for (int j = 0; j < colSize; j++) {
                    ColumnName cn = ch.get(j);
                    if (cn.getColumnType().endsWith(STRING_TYPE)) {
                        createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                    } else if (cn.getColumnType().endsWith(INTEGER_TYPE)) {
                        Integer inVal = Integer.valueOf(0);
                        if (j == 0) {
                            inVal = Integer.valueOf(i + 1);
                            createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, inVal, font, true, false,false);
                        } else {
                            createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, inVal, font, true, false,false);
                        }
                    } else if (cn.getColumnType().endsWith(DOUBLE_TYPE)) {
                        createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, Double.valueOf(0.0), font, true, false);
                    } else if (cn.getColumnType().endsWith(BIGDECIMAL_TYPE)) {
                        createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, BigDecimal.ZERO, font, true, false);
                    } else if (cn.getColumnType().endsWith(DATE_TYPE)) {
                        createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, new Date(), font, true, false);
                    }
                }
            }

        } else if (wb instanceof XSSFWorkbook) {
            int vSize = 100;

            Comparator<ColumnName> comparator = new Comparator<ColumnName>() {

                public int compare(ColumnName o1, ColumnName o2) {
                    return o1.getIndex().compareTo(o2.getIndex());
                }
            };

            ArrayList<ColumnName> ch = new ArrayList<ColumnName>();
            ch.addAll(columnHeader.values());

            Collections.sort(ch, comparator);

            int colSize = ch.size();
            int rc = getRowCount();
            for (int i = 0; i < vSize; i++) {
                Row row = sheet.createRow((short) rc++);
                row.setHeightInPoints(15);
                for (int j = 0; j < colSize; j++) {
                    ColumnName cn = ch.get(j);
                    if (cn.getColumnType().endsWith(STRING_TYPE)) {
                        createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_LEFT, XSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                    } else if (cn.getColumnType().endsWith(INTEGER_TYPE)) {
                        Integer inVal = Integer.valueOf(0);
                        if (j == 0) {
                            inVal = Integer.valueOf(i + 1);
                            createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_CENTER, XSSFCellStyle.VERTICAL_CENTER, inVal, font, true, false);
                        } else {
                            createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_CENTER, XSSFCellStyle.VERTICAL_CENTER, inVal, font, true, false);
                        }
                    } else if (cn.getColumnType().endsWith(DOUBLE_TYPE)) {
                        createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_RIGHT, XSSFCellStyle.VERTICAL_CENTER, Double.valueOf(0.0), font, true, false);
                    } else if (cn.getColumnType().endsWith(BIGDECIMAL_TYPE)) {
                        createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_RIGHT, XSSFCellStyle.VERTICAL_CENTER, BigDecimal.ZERO, font, true, false);
                    } else if (cn.getColumnType().endsWith(DATE_TYPE)) {
                        createCell(wb, row, (short) j, XSSFCellStyle.ALIGN_CENTER, XSSFCellStyle.VERTICAL_CENTER, new Date(), font, true, false);
                    }
                }
            }
        }
    }

    private void createLookUpContent(Workbook wb, Sheet sheet, Font font, List<Unit> data) {
        if (wb instanceof HSSFWorkbook) {
            int rc = 0;
            if (!data.isEmpty()) {
                for (Unit du : data) {
                    Row row = sheet.createRow((short) rc++);
                    row.setHeightInPoints(20);
                    createCell(wb, row, (short) 0, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, du.getUnitCode(), font, false, false);
                    createCell(wb, row, (short) 1, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, du.getUnitName(), font, false, false);
                }
                namedRange = wb.createName();
                namedRange.setNameName("MyList");
                namedRange.setRefersToFormula("'Sheet2'!$A$1:$A$" + rc);
                addressList = new CellRangeAddressList(4, 4, 2, 4);
            }
        } else if (wb instanceof XSSFWorkbook) {
            int rc = 0;
            if (!data.isEmpty()) {
                for (Unit du : data) {
                    Row row = sheet.createRow((short) rc++);
                    row.setHeightInPoints(20);
                    createCell(wb, row, (short) 0, XSSFCellStyle.ALIGN_LEFT, XSSFCellStyle.VERTICAL_CENTER, du.getUnitCode(), font, false, false);
                    createCell(wb, row, (short) 1, XSSFCellStyle.ALIGN_LEFT, XSSFCellStyle.VERTICAL_CENTER, du.getUnitName(), font, false, false);
                }
            }
            namedRange = wb.createName();
            namedRange.setNameName("MyList");
            namedRange.setRefersToFormula("'Sheet2'!$A$1:$A$" + rc);
            addressList = new CellRangeAddressList(4, 4, 2, 4);
        }
    }

    protected void createCell(Workbook wb, Row row, short column, short halign, short valign, Object content, Font font, boolean withBorder, boolean withForeColor) {
        if (content instanceof String) {
            createCell(wb, row, column, halign, valign, (String) content, font, true, false);
        } else if (content instanceof Integer) {
            createCell(wb, row, column, halign, valign, (Integer) content, font, true, false, false);
        } else if (content instanceof Double) {
            createCell(wb, row, column, halign, valign, (Double) content, font, true, false);
        } else if (content instanceof Date) {
            createCell(wb, row, column, halign, valign, (Date) content, font, true, false);
        } else if (content instanceof BigDecimal) {
            createCell(wb, row, column, halign, valign, ((BigDecimal) content).doubleValue(), font, true, false);
        }
    }

    protected void createCell(Workbook wb, Row row, short column, short halign, short valign, String content, Font font, boolean withBorder, boolean withForeColor) {
        Cell cell = row.createCell(column);

        if (wb instanceof HSSFWorkbook) {
            cell.setCellValue(new HSSFRichTextString(content));
        } else if (wb instanceof XSSFWorkbook) {
            cell.setCellValue(new XSSFRichTextString(content));
        }

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setWrapText(true);

        cellStyle.setFont(font);

        if (withBorder) {
            createCellBorder(cellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
        }

        if (withForeColor) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        cell.setCellStyle(cellStyle);
    }

    protected void createCell(Workbook wb, Row row, short column, short halign, short valign, Integer content, Font font, boolean withBorder, boolean withForeColor, boolean useSeparator) {
        CreationHelper createHelper = wb.getCreationHelper();

        Cell cell = row.createCell(column);
        cell.setCellValue(content);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        if (useSeparator) {
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        } else {
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0"));
        }

        cellStyle.setWrapText(true);

        cellStyle.setFont(font);

        if (withBorder) {
            createCellBorder(cellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
        }

        if (withForeColor) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        cell.setCellStyle(cellStyle);
    }

    protected void createCell(Workbook wb, Row row, short column, short halign, short valign, Double content, Font font, boolean withBorder, boolean withForeColor) {
        CreationHelper createHelper = wb.getCreationHelper();
        Cell cell = row.createCell(column);
        cell.setCellValue(content);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
        cellStyle.setWrapText(true);

        cellStyle.setFont(font);

        if (withBorder) {
            createCellBorder(cellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
        }

        if (withForeColor) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        cell.setCellStyle(cellStyle);
    }

    protected void createCell(Workbook wb, Row row, short column, short halign, short valign, Date content, Font font, boolean withBorder, boolean withBackColor) {
        CreationHelper createHelper = wb.getCreationHelper();

        Cell cell = row.createCell(column);
        cell.setCellValue(content);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        cellStyle.setWrapText(true);

        cellStyle.setFont(font);

        if (withBorder) {
            createCellBorder(cellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
        }

        if (withBackColor) {
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        cell.setCellStyle(cellStyle);
    }

    protected void createCellBorder(CellStyle cellStyle, short borderStyle, short color) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBottomBorderColor(color);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setLeftBorderColor(color);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setRightBorderColor(color);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setTopBorderColor(color);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        int oldRowCount = getRowCount();
        this.rowCount = rowCount;
        support.firePropertyChange("rowCount", oldRowCount, getRowCount());
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        int oldColCount = getColumnCount();
        this.columnCount = columnCount;
        support.firePropertyChange("columnCount", oldColCount, getColumnCount());
    }

    public int getSubTitleGap() {
        return subTitleGap;
    }

    public void setSubTitleGap(int subTitleGap) {
        int oldSubTitleGap = getSubTitleGap();
        this.subTitleGap = subTitleGap;
        support.firePropertyChange("subTitleGap", oldSubTitleGap, getSubTitleGap());
    }

    public int getTitleGap() {
        return titleGap;
    }

    public void incrementRowCount(int size) {
        setRowCount(getRowCount() + size);
    }

    public void incrementColumnCount(int size) {
        setColumnCount(getColumnCount() + size);
    }

    public void incrementSubTitleGap() {
        setSubTitleGap(getSubTitleGap() + 1);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }
}
