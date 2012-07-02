/*
 * $Id: JXGroupableTableHeaderAddon.java 789 2006-03-31 06:51:23Z evickroy $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;



import org.jdesktop.swingx.JXGroupableTableHeader;

/**
 * Addon for <code>JXGroupableTableHeader</code>.<br>
 *
 */
public class JXGroupableTableHeaderAddon extends AbstractComponentAddon {

    public JXGroupableTableHeaderAddon() {
        super("JXGroupableTableHeader");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI");
    }

    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.metal.MetalGroupableTableHeaderUI");
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.windows.WindowsGroupableTableHeaderUI");
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.windows.WindowsClassicGroupableTableHeaderUI");
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.substance.SubstanceGroupableTableHeaderUI");
        
    }

    @Override
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.metal.MetalGroupableTableHeaderUI");
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.substance.SubstanceGroupableTableHeaderUI");
    }

    @Override
    protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.linux.LinuxGroupableTableHeaderUI");
        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.substance.SubstanceGroupableTableHeaderUI");
    }
    
    
}
