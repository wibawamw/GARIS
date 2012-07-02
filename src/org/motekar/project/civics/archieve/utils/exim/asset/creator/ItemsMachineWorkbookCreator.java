package org.motekar.project.civics.archieve.utils.exim.asset.creator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.AbstractWorkbookCreator;
import org.motekar.project.civics.archieve.utils.exim.ColumnName;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsMachineWorkbookCreator extends AbstractWorkbookCreator<ItemsMachine> {
    
    public static final String TEMPLATE_FILE_NAME = "Template KIB-B.xls";
    public static final String DATA_FILE_NAME = "Data KIB-B.xls";
    
    public ItemsMachineWorkbookCreator(ArchieveMainframe  mainframe,String fileName, List<ItemsMachine> data,List<Unit> lookupData) {
        super(mainframe,fileName, data,lookupData);
    }

    @Override
    protected Map<String, ColumnName> getColumnHeader() {
        Map<String, ColumnName> columnHeader = new HashMap<String, ColumnName>();
        
        columnHeader.put("001", new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnHeader.put("002", new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnHeader.put("003", new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnHeader.put("004", new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnHeader.put("005", new ColumnName(Integer.valueOf(4),"Merk/Type",STRING_TYPE));
        columnHeader.put("006", new ColumnName(Integer.valueOf(5),"Ukuran (CC)",INTEGER_TYPE));
        columnHeader.put("007", new ColumnName(Integer.valueOf(6),"Bahan",STRING_TYPE));
        columnHeader.put("008", new ColumnName(Integer.valueOf(7),"Tahun Beli",INTEGER_TYPE));
        columnHeader.put("009", new ColumnName(Integer.valueOf(8),"No. Pabrik",STRING_TYPE));
        columnHeader.put("010", new ColumnName(Integer.valueOf(9),"No. Rangka",STRING_TYPE));
        columnHeader.put("011", new ColumnName(Integer.valueOf(10),"No. Mesin",STRING_TYPE));
        columnHeader.put("012", new ColumnName(Integer.valueOf(11),"No. Polisi",STRING_TYPE));
        columnHeader.put("013", new ColumnName(Integer.valueOf(12),"No. BPKB",STRING_TYPE));
        columnHeader.put("014", new ColumnName(Integer.valueOf(13),"Asal-Usul",STRING_TYPE));
        columnHeader.put("015", new ColumnName(Integer.valueOf(14),"Harga Satuan\n(Rp)",BIGDECIMAL_TYPE));
        columnHeader.put("016", new ColumnName(Integer.valueOf(15),"Honor+ADM",BIGDECIMAL_TYPE));
        columnHeader.put("017", new ColumnName(Integer.valueOf(16),"Jumlah",BIGDECIMAL_TYPE));
        columnHeader.put("018", new ColumnName(Integer.valueOf(17),"Keterangan",STRING_TYPE));
        
        
        return columnHeader;
    }

    @Override
    protected String getTitle() {
        return "KARTU INVENTARIS BARANG PERALATAN DAN MESIN (KIB-B)";
    }

    @Override
    protected Map<String, String> getSubTitle() {
        Map<String, String> subTitle = new HashMap<String, String>();
        
        ProfileAccount profileAccount = mainframe.getProfileAccount();
        
        subTitle.put(profileAccount.getCompany()+" "+profileAccount.getStateType()+" "+profileAccount.getState(), "");
        subTitle.put("Nomor Kode Lokasi :", "");
        
        return subTitle;
    }

    @Override
    protected void createContent(Workbook wb, Sheet sheet, Font font, List<ItemsMachine> data) {
        try {
            Map<String, ColumnName> columnHeader = getColumnHeader();

            if (wb instanceof HSSFWorkbook) {
                int vSize = data.size();

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
                        if (cn.getName().equals("No.")) {
                            createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, Integer.valueOf(i + 1), font, true, false);
                        } else if (cn.getName().equals("Jenis Barang /\nNama Barang")) {
                            if (data.get(i).getShortName() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getShortName(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Kode\nBarang")) {
                            if (data.get(i).getItemCode() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getItemCode(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Nomor\nRegister")) {
                            if (data.get(i).getRegNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getRegNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Merk/Type")) {
                            if (data.get(i).getMachineType() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getMachineType(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Ukuran (CC)")) {
                            if (data.get(i).getVolume() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getVolume(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Bahan")) {
                            if (data.get(i).getMaterial() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getMaterial(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Tahun Beli")) {
                            if (data.get(i).getAcquisitionYear() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getAcquisitionYear(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("No. Pabrik")) {
                            if (data.get(i).getFactoryNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getFactoryNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("No. Rangka")) {
                            if (data.get(i).getFabricNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getFabricNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("No. Mesin")) {
                            if (data.get(i).getMachineNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getMachineNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("No. Polisi")) {
                            if (data.get(i).getPoliceNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getPoliceNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("No. BPKB")) {
                            if (data.get(i).getBpkbNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getBpkbNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Asal-Usul")) {
                            if (data.get(i).getAcquisitionWay() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getAcquisitionWay(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Harga Satuan\n(Rp)")) {
                            if (data.get(i).getPrice() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getPrice(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Honor+ADM")) {
                            if (data.get(i).getAdminCost() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getAdminCost(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Jumlah")) {
                            if (data.get(i).getTotalPrice() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getTotalPrice(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Keterangan")) {
                            if (data.get(i).getDescription() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getDescription(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        }
                    }
                }

            } 
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
