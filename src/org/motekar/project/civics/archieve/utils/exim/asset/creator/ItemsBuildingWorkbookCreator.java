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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
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

public class ItemsBuildingWorkbookCreator extends AbstractWorkbookCreator<ItemsBuilding> {
    
    public static final String TEMPLATE_FILE_NAME = "Template KIB-C.xls";
    public static final String DATA_FILE_NAME = "Data KIB-C.xls";
    
    public ItemsBuildingWorkbookCreator(ArchieveMainframe  mainframe,String fileName, List<ItemsBuilding> data,List<Unit> lookupData) {
        super(mainframe,fileName, data,lookupData);
    }

    @Override
    protected Map<String, ColumnName> getColumnHeader() {
        Map<String, ColumnName> columnHeader = new HashMap<String, ColumnName>();
        
        columnHeader.put("001", new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnHeader.put("002", new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnHeader.put("003", new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnHeader.put("004", new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnHeader.put("005", new ColumnName(Integer.valueOf(4),"Kondisi\nBangunan\n(RB,R,KB,B,SB)",STRING_TYPE));
        columnHeader.put("006", new ColumnName(Integer.valueOf(5),"Kontruksi\nBangunan\nBertingkat\n(Bertingkat,Tidak)",STRING_TYPE));
        columnHeader.put("007", new ColumnName(Integer.valueOf(6),"Kontruksi\nBangunan\nBeton\n(Beton,Tidak)",STRING_TYPE));
        columnHeader.put("008", new ColumnName(Integer.valueOf(7),"Luas\nLantai\n(m2)",BIGDECIMAL_TYPE));
        columnHeader.put("009", new ColumnName(Integer.valueOf(8),"Letak/\nLokasi/Alamat",STRING_TYPE));
        columnHeader.put("010", new ColumnName(Integer.valueOf(9),"Tanggal\nDokumen\nGedung",DATE_TYPE));
        columnHeader.put("011", new ColumnName(Integer.valueOf(10),"Nomor\nDokumen\nGedung",STRING_TYPE));
        columnHeader.put("012", new ColumnName(Integer.valueOf(11),"Luas\nTanah\n(m2)",STRING_TYPE));
        columnHeader.put("013", new ColumnName(Integer.valueOf(12),"Status\nTanah",STRING_TYPE));
        columnHeader.put("014", new ColumnName(Integer.valueOf(13),"Nomor\nKode Tanah",STRING_TYPE));
        columnHeader.put("015", new ColumnName(Integer.valueOf(14),"Asal-usul\nPembiayaan",STRING_TYPE));
        columnHeader.put("016", new ColumnName(Integer.valueOf(15),"Harga Satuan\n(Rp)",BIGDECIMAL_TYPE));
        columnHeader.put("017", new ColumnName(Integer.valueOf(16),"Honor+ADM",BIGDECIMAL_TYPE));
        columnHeader.put("018", new ColumnName(Integer.valueOf(17),"Jumlah",BIGDECIMAL_TYPE));
        columnHeader.put("019", new ColumnName(Integer.valueOf(18),"Keterangan",STRING_TYPE));
        
        
        return columnHeader;
    }

    @Override
    protected String getTitle() {
        return "KARTU INVENTARIS BARANG GEDUNG DAN BANGUNAN (KIB-C)";
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
    protected void createContent(Workbook wb, Sheet sheet, Font font, List<ItemsBuilding> data) {
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
                        } else if (cn.getName().equals("Kondisi\nBangunan\n(RB,R,KB,B,SB)")) {
                            if (data.get(i).getCondition() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getCondition().toString(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Kontruksi\nBangunan\nBertingkat\n(Bertingkat,Tidak)")) {
                            if (data.get(i).getRaised() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getRaised(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Kontruksi\nBangunan\nBeton\n(Beton,Tidak)")) {
                            if (data.get(i).getFrameworks() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getFrameworks(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Letak/\nLokasi/Alamat")) {
                            StringBuilder builder = new StringBuilder();
                            builder.append(data.get(i).getAddress());

                            if (data.get(i).getUrban() != null) {
                                Region urban = data.get(i).getUrban();
                                builder.append(" Kecamatan ").append(urban.getRegionName());
                            }

                            if (data.get(i).getSubUrban() != null) {
                                Region subUrban = data.get(i).getSubUrban();
                                if (subUrban.getRegionType().equals(Region.TYPE_SUB_URBAN)) {
                                    builder.append(" Kelurahan ").append(subUrban.getRegionName());
                                } else {
                                    builder.append(" Desa ").append(subUrban.getRegionName());
                                }
                            }
                            createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, builder.toString(), font, true, false);
                        } else if (cn.getName().equals("Luas\nLantai\n(m2)")) {
                            if (data.get(i).getWide() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getWide(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Tanggal\nDokumen\nGedung")) {
                            if (data.get(i).getDocumentDate() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getDocumentDate(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Nomor\nDokumen\nGedung")) {
                            if (data.get(i).getDocumentNumber() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getDocumentNumber(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Luas\nTanah\n(m2)")) {
                            if (data.get(i).getLandWide() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getLandWide(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_RIGHT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Status\nTanah")) {
                            if (data.get(i).getLandState() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getLandState(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Nomor\nKode Tanah")) {
                            if (data.get(i).getLandCode() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getLandCode(), font, true, false);
                            } else {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "", font, true, false);
                            }
                        } else if (cn.getName().equals("Asal-usul\nPembiayaan")) {
                            if (data.get(i).getAcquitionWay() != null) {
                                createCell(wb, row, (short) j, HSSFCellStyle.ALIGN_LEFT, HSSFCellStyle.VERTICAL_CENTER, data.get(i).getAcquitionWay(), font, true, false);
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
