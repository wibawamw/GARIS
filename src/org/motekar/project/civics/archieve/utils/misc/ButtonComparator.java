package org.motekar.project.civics.archieve.utils.misc;

import java.util.Comparator;
import org.pushingpixels.flamingo.api.common.JCommandButton;

/**
 *
 * @author Muhamad Wibawa
 */
public class ButtonComparator implements Comparator<JCommandButton> {

    public int compare(JCommandButton o1, JCommandButton o2) {
        return o1.getText().compareTo(o2.getText());
    }
    
}
