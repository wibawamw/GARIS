package org.motekar.project.civics.archieve.utils.report;

import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Muhamad Wibawa
 */
public class ViewerPanel extends MotekarViewerPanel{

    public ViewerPanel(JasperPrint jrPrint, Locale locale, ResourceBundle resBundle) {
        super(jrPrint, locale, resBundle);
        translateTooltiptoIndonesian();
    }

    public ViewerPanel(JasperPrint jrPrint, Locale locale) {
        super(jrPrint, locale);
        translateTooltiptoIndonesian();
    }

    public ViewerPanel(JasperPrint jrPrint) {
        super(jrPrint);
        translateTooltiptoIndonesian();
    }

    private void translateTooltiptoIndonesian() {
        btnSave.setToolTipText("Simpan");
        btnPrint.setToolTipText("Cetak");
        btnReload.setToolTipText("Muat Ulang");
        btnFirst.setToolTipText("Halaman Pertama");
        btnLast.setToolTipText("Halaman Terakhir");
        btnPrevious.setToolTipText("Halaman Sebelumnya");
        btnNext.setToolTipText("Halaman Selanjutnya");

        cmbZoom.setToolTipText("Ukuran");

        btnZoomOut.setToolTipText("Perkecil");
        btnZoomIn.setToolTipText("Perbesar");

        btnActualSize.setToolTipText("Ukuran Sebenarnya");
        btnFitPage.setToolTipText("Sesuai Halaman");
        btnFitWidth.setToolTipText("Sesuai Lebar");

        
    }

    public void setButtonEnable(boolean flag) {
        btnSave.setEnabled(flag);
        btnPrint.setEnabled(flag);
        btnReload.setEnabled(flag);
        btnFirst.setEnabled(flag);
        btnLast.setEnabled(flag);
        btnPrevious.setEnabled(flag);
        btnNext.setEnabled(flag);

        cmbZoom.setEnabled(flag);

        btnZoomOut.setEnabled(flag);
        btnZoomIn.setEnabled(flag);

        btnActualSize.setEnabled(flag);
        btnFitPage.setEnabled(flag);
        btnFitWidth.setEnabled(flag);
    }

    public void reload(JasperPrint jrPrint) {
        cmbZoom.setSelectedIndex(0);
        btnFitWidth.setSelected(true);
        loadReport(jrPrint);
        refreshPage();
    }

}
