package org.motekar.project.civics.archieve.utils.exim.asset.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.ReaderConfig;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.AbstractWorkbookReader;
import org.motekar.project.civics.archieve.utils.exim.ColumnName;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsBuildingWorkbookReader extends AbstractWorkbookReader<ItemsBuilding> {

    public ItemsBuildingWorkbookReader() {
    }
    
    @Override
    protected ArrayList<ColumnName> getColumnNames() {
        
        ArrayList<ColumnName> columnNames = new ArrayList<ColumnName>();
        
        columnNames.add(new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(4),"Kondisi\nBangunan\n(RB,R,KB,B,SB)",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(5),"Kontruksi\nBangunan\nBertingkat\n(Bertingkat,Tidak)",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(6),"Kontruksi\nBangunan\nBeton\n(Beton,Tidak)",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(7),"Luas\nLantai\n(m2)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(8),"Letak/\nLokasi/Alamat",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(9),"Tanggal\nDokumen\nGedung",DATE_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(10),"Nomor\nDokumen\nGedung",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(11),"Luas\nTanah\n(m2)",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(12),"Status\nTanah",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(13),"Nomor\nKode Tanah",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(14),"Asal-usul\nPembiayaan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(15),"Harga Satuan\n(Rp)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(16),"Honor+ADM",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(17),"Jumlah",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(18),"Keterangan",STRING_TYPE));
        
        return columnNames;
    }

    @Override
    protected List<ItemsBuilding> getMappedObject(String fileName) throws IOException, SAXException,InvalidFormatException {
        ReaderConfig.getInstance().setSkipErrors(true);
        ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes( true );
        InputStream inputXML = new BufferedInputStream(ArchieveMainframe.class.getResourceAsStream("/resource/xml/ItemsBuildingXml.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        //
        InputStream inputXLS = new FileInputStream(new File(fileName));// excel file
        Unit unit = new Unit();
        List<ItemsBuilding> itemsBuildings = new ArrayList<ItemsBuilding>();
        Map beans = new HashMap();
        beans.put("unit", unit);
        beans.put("itemsbuildings", itemsBuildings);
        XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
        
        System.out.println("Status OK = "+readStatus.isStatusOK());
        
        if (!itemsBuildings.isEmpty()) {
            for (ItemsBuilding il : itemsBuildings) {
                il.setUnit(unit);
            }
        }
        
        return itemsBuildings;
    }

    
}
