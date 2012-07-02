package org.jdesktop.swingx.plaf.linux;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI;

/**
 *
 * @author Muhamad Wibawa
 */
public class LinuxGroupableTableHeaderUI extends BasicGroupableTableHeaderUI {

    public static ComponentUI createUI(JComponent c) {
        return new LinuxGroupableTableHeaderUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
    }
}
