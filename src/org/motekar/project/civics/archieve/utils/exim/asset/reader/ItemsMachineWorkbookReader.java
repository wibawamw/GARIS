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
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.ui.ArchieveMainframe;
import org.motekar.project.civics.archieve.utils.exim.AbstractWorkbookReader;
import org.motekar.project.civics.archieve.utils.exim.ColumnName;
import org.xml.sax.SAXException;

/**
 *
 * @author Muhamad Wibawa
 */

public class ItemsMachineWorkbookReader extends AbstractWorkbookReader<ItemsMachine> {

    public ItemsMachineWorkbookReader() {
    }
    
    @Override
    protected ArrayList<ColumnName> getColumnNames() {
        
        ArrayList<ColumnName> columnNames = new ArrayList<ColumnName>();
        
        columnNames.add(new ColumnName(Integer.valueOf(0),"No.",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(1),"Jenis Barang /\nNama Barang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(2),"Kode\nBarang",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(3),"Nomor\nRegister",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(4),"Merk/Type",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(5),"Ukuran (CC)",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(6),"Bahan",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(7),"Tahun Beli",INTEGER_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(8),"No. Pabrik",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(9),"No. Rangka",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(10),"No. Mesin",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(11),"No. Polisi",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(12),"No. BPKB",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(13),"Asal-Usul",STRING_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(14),"Harga Satuan\n(Rp)",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(15),"Honor+ADM",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(16),"Jumlah",BIGDECIMAL_TYPE));
        columnNames.add(new ColumnName(Integer.valueOf(17),"Keterangan",STRING_TYPE));
        
        return columnNames;
    }

    @Override
    protected List<ItemsMachine> getMappedObject(String fileName) throws IOException, SAXException,InvalidFormatException {
        ReaderConfig.getInstance().setSkipErrors(true);
        ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes( true );
        InputStream inaddXML = new BufferedInputStream(ArchieveMainframe.class.getResourceAsStream("/resource/xml/ItemsMachineXml.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inaddXML);
        //
        InputStream inaddXLS = new FileInputStream(new File(fileName));// excel file
        Unit unit = new Unit();
        List<ItemsMachine> itemsMachines = new ArrayList<ItemsMachine>();
        Map beans = new HashMap();
        beans.put("unit", unit);
        beans.put("itemsmachines", itemsMachines);
        XLSReadStatus readStatus = mainReader.read(inaddXLS, beans);
        
        System.out.println("Status OK = "+readStatus.isStatusOK());
        
        if (!itemsMachines.isEmpty()) {
            for (ItemsMachine im : itemsMachines) {
                im.setUnit(unit);
            }
        }
        
        return itemsMachines;
    }

    
}
