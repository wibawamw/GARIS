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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.AbstractWorkbookReader;
import org.motekar.project.civics.archieve.utils.exim.ColumnName;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsLandWorkbookReader extends AbstractWorkbookReader<ItemsLand> {

    public ItemsLandWorkbookReader() {
    }
    
    @Override
    protected ArrayList<ColumnName> getColumnNames() {
        
        ArrayList<ColumnName> columnNames = new ArrayList<ColumnName>();
        
        columnNames.add(new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(4),"Luas\n(M2)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(5),"Tahun\nPengadaan",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(6),"Letak /\nLokasi/Alamat",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(7),"Hak",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(8),"Nomor\nSertifikat",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(9),"Tanggal\nSertifikat",DATE_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(10),"Penggunaan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(11),"Asal-Usul",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(12),"Harga\nPerolehan\n(Rp.)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(13),"Keterangan",STRING_TYPE));
        
        return columnNames;
    }

    @Override
    protected List<ItemsLand> getMappedObject(String fileName) throws IOException, SAXException,InvalidFormatException {
        ReaderConfig.getInstance().setSkipErrors(true);
        ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes( true );
        InputStream inputXML = new BufferedInputStream(ArchieveMainframe.class.getResourceAsStream("/resource/xml/ItemsLandXml.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        //
        InputStream inputXLS = new FileInputStream(new File(fileName));// excel file
        Unit unit = new Unit();
        List<ItemsLand> itemsLands = new ArrayList<ItemsLand>();
        Map beans = new HashMap();
        beans.put("unit", unit);
        beans.put("itemslands", itemsLands);
        XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
        
        System.out.println("Status OK = "+readStatus.isStatusOK());
        
        if (!itemsLands.isEmpty()) {
            for (ItemsLand il : itemsLands) {
                il.setUnit(unit);
            }
        }
        
        return itemsLands;
    }

    
}
