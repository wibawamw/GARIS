package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Muhamad Wibawa
 */
public class NodeIndex {

    private Long index = Long.valueOf(0);
    private DefaultMutableTreeNode node = null;

    public NodeIndex(Long index, DefaultMutableTreeNode node) {
        this.index = index;
        this.node = node;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultMutableTreeNode node) {
        this.node = node;
    }

    public static NodeIndex searchArrays(ArrayList<NodeIndex> nodeIndexs, Long parentIndex) {
        NodeIndex nodeIndex = null;

        if (!nodeIndexs.isEmpty()) {
            for (NodeIndex idx : nodeIndexs) {
                if (idx.getIndex() == parentIndex) {
                    nodeIndex = idx;
                    break;
                }
            }
        }

        return nodeIndex;
    }
}
