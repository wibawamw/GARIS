package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Muhamad Wibawa
 */

public class NodeCode {

    private String code = "";
    private DefaultMutableTreeNode node = null;

    public NodeCode(String code, DefaultMutableTreeNode node) {
        this.code = code;
        this.node = node;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultMutableTreeNode node) {
        this.node = node;
    }

    public static NodeCode searchArrays(ArrayList<NodeCode> nodeCodes, String parentCode) {
        NodeCode nodeCode = null;

        if (!nodeCodes.isEmpty()) {
            for (NodeCode idx : nodeCodes) {
                if (idx.getCode().equals(parentCode)) {
                    nodeCode = idx;
                    break;
                }
            }
        }

        return nodeCode;
    }
}
