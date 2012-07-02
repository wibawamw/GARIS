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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.AbstractWorkbookReader;
import org.motekar.project.civics.archieve.utils.exim.ColumnName;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsFixedAssetWorkbookReader extends AbstractWorkbookReader<ItemsFixedAsset> {

    public ItemsFixedAssetWorkbookReader() {
    }
    
    @Override
    protected ArrayList<ColumnName> getColumnNames() {
        
        ArrayList<ColumnName> columnNames = new ArrayList<ColumnName>();
        
        columnNames.add(new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(4),"Judul/Pencipta\nBuku/Perpustakaan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(5),"Spesifikasi\nBuku/Perpustakaan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(6),"Asal Daerah\nBarang Bercorak\nKebudayaan/Kesenian",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(7),"Pencipta\nBarang Bercorak\nKebudayaan/Kesenian",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(8),"Bahan\nBarang Bercorak\nKebudayaan/Kesenian",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(9),"Jenis\nHewan/Ternak",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(10),"Ukuran\nHewan/Ternak",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(11),"Tahun\nCetak/Pembelian",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(12),"Asal-Usul/\nCara Perolehan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(13),"Harga Perolehan\n(Rp)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(14),"Keterangan",STRING_TYPE));
        
        return columnNames;
    }

    @Override
    protected List<ItemsFixedAsset> getMappedObject(String fileName) throws IOException, SAXException,InvalidFormatException {
        ReaderConfig.getInstance().setSkipErrors(true);
        ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes( true );
        InputStream inputXML = new BufferedInputStream(ArchieveMainframe.class.getResourceAsStream("/resource/xml/ItemsFixedAssetXml.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        //
        InputStream inputXLS = new FileInputStream(new File(fileName));// excel file
        Unit unit = new Unit();
        List<ItemsFixedAsset> itemsFixedAssets = new ArrayList<ItemsFixedAsset>();
        Map beans = new HashMap();
        beans.put("unit", unit);
        beans.put("itemsfixedassets", itemsFixedAssets);
        XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
        
        System.out.println("Status OK = "+readStatus.isStatusOK());
        
        if (!itemsFixedAssets.isEmpty()) {
            for (ItemsFixedAsset il : itemsFixedAssets) {
                il.setUnit(unit);
            }
        }
        
        return itemsFixedAssets;
    }

    
}
