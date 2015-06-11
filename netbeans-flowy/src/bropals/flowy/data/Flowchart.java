/*
 * Flowy is a simple flowchart making software.
 * Copyright (C) 2015  Jonathon Prehn, Kevin Prehn
 * 
 * This file is a part of Flowy.
 *
 * Flowy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Flowy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Flowy.  If not, see <http://www.gnu.org/licenses/>.
 */
package bropals.flowy.data;

import bropals.flowy.FlowchartWindow;
import bropals.flowy.StyleManager;
import bropals.flowy.style.LineStyle;
import bropals.flowy.style.NodeStyle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * An object to represent a flowchart.
 * @author Jonathon
 */
public class Flowchart implements BinaryData {
    /**
     * The nodes that make up this Flowchart.
     */
    private ArrayList<Node> nodes;
    /**
     * A reference to the style manager
     */
    private StyleManager styleManager;
    
    /**
     * Create the default flowchart.
     * @param def whether or not it should be the default flowchart.
     */
    public Flowchart(boolean def) {
        nodes = new ArrayList<>();
        if (def) {
            Node firstNode = new Node(100, 100);
            nodes.add(firstNode);
        }
    }
    
    /**
     * Gets the nodes that make up this flowchart.
     * @return the nodes that make up this flowchart.
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * This is only used for getting the node lines. Adding a NodeLine 
     * to the returned array will NOT add the line to the flowchart.
     * @return 
     */
    public ArrayList<NodeLine> getNodeLines() {
        ArrayList<NodeLine> lines = new ArrayList<>();
        for (Node n : getNodes()) {
            for (NodeLine nl : n.getLinesConnected()) {
                if (!lines.contains(nl)) {
                    lines.add(nl);
                }
            }
        }
        return lines;
    }
    
    /**
     * Lend a reference to style manager to Flowchart for saving and loading.
     * @param styleManager the style manager.
     */
    public void passStyleManager(StyleManager styleManager) {
        this.styleManager = styleManager;
    }

    @Override
    public int bytes() {
        ArrayList<NodeLine> nodeLines = getNodeLines();
        NodeStyle[] nodeStyles = styleManager.listNodeStyles();
        LineStyle[] lineStyles = styleManager.listLineStyles();
        String[] nodeStyleNames = styleManager.listNodeStyleNames();
        String[] lineStyleNames = styleManager.listLineStyleNames();
        int size = 16; //4 integers to store the size of each type
        int nodeSize = 0;
        int nodeLineSize = 0;
        int nodeStyleSize = 0;
        int lineStyleSize = 0;
        for (int i=0; i<nodes.size(); i++) {
            nodeSize += nodes.get(i).bytes();
        }
        for (int i=0; i<nodeLines.size(); i++) {
            nodeLineSize += nodeLines.get(i).bytes();
        }
        for (int i=0; i<nodeStyles.length; i++) {
            nodeStyleSize += (BinaryUtil.bytesForString(nodeStyleNames[i]) + nodeStyles[i].bytes());
        }
        for (int i=0; i<lineStyles.length; i++) {
            lineStyleSize += (BinaryUtil.bytesForString(lineStyleNames[i]) + lineStyles[i].bytes());
        }
        size += nodeSize;
        size += nodeLineSize;
        size += nodeStyleSize;
        size += lineStyleSize;
        System.out.println("Flowchart file byte breakdown");
        System.out.println("\tHeader: 16 bytes");
        System.out.println("\tNodes: " + nodeSize + " bytes");
        System.out.println("\tNode Lines: " + nodeLineSize + " bytes");
        System.out.println("\tNode Styles: " + nodeStyleSize + " bytes");
        System.out.println("\tLine Styles: " + lineStyleSize + " bytes");
        System.out.println("\tTotal: " + size + " bytes");
        System.out.println();
        return size;
    }

    @Override
    public void toBinary(byte[] arr, int pos) {
        ArrayList<NodeLine> nodeLines = getNodeLines();
        NodeStyle[] nodeStyles = styleManager.listNodeStyles();
        LineStyle[] lineStyles = styleManager.listLineStyles();
        String[] nodeStyleNames = styleManager.listNodeStyleNames();
        String[] lineStyleNames = styleManager.listLineStyleNames();
        //Write the number of node styles
        BinaryUtil.intToBytes(nodeStyles.length, arr, pos);
        //Write the number of line styles
        BinaryUtil.intToBytes(lineStyles.length, arr, pos+4);
        //Write the number of nodes
        BinaryUtil.intToBytes(nodes.size(), arr, pos+8);
        //Write the number of node lines
        BinaryUtil.intToBytes(nodeLines.size(), arr, pos+12);
        int mark = 16;
        int i;
        for (i=0; i<nodeStyles.length; i++) {
            BinaryUtil.stringToBytes(nodeStyleNames[i], arr, pos+mark);
            mark += BinaryUtil.bytesForString(nodeStyleNames[i]);
            nodeStyles[i].toBinary(arr, pos+mark);
            mark += nodeStyles[i].bytes();
        }
        for (i=0; i<lineStyles.length; i++) {
            BinaryUtil.stringToBytes(lineStyleNames[i], arr, pos+mark);
            mark += BinaryUtil.bytesForString(lineStyleNames[i]);
            lineStyles[i].toBinary(arr, pos+mark);
            mark += lineStyles[i].bytes();
        }
        for (Node node : nodes) {
            node.toBinary(arr, pos+mark);
            mark += node.bytes();
        }
        for (NodeLine nodeLine : nodeLines) {
            BinaryUtil.intToBytes(nodes.indexOf(nodeLine.getChild()), arr, pos+mark);
            mark += 4;
            BinaryUtil.intToBytes(nodes.indexOf(nodeLine.getParent()), arr, pos+mark);
            mark += 4;
            nodeLine.toBinary(arr, pos+mark);
            mark += (nodeLine.bytes()-8); 
            //Minus 8 because the other bytes have already been added
        }
    }

    @Override
    public void fromBinary(byte[] arr, int pos, FlowchartWindow window) {
        nodes.clear();
        int nodeStyleCount = BinaryUtil.bytesToInt(arr, pos);
        int lineStyleCount = BinaryUtil.bytesToInt(arr, pos+4);
        int nodeCount = BinaryUtil.bytesToInt(arr, pos+8);
        int nodeLineCount = BinaryUtil.bytesToInt(arr, pos+12);
        int mark = 16; //The position in the byte array from position
        int i;
        String name;
        NodeStyle nodeStyle;
        for (i=0; i<nodeStyleCount; i++) {
            nodeStyle = new NodeStyle();
            name = BinaryUtil.bytesToString(arr, pos+mark);
            mark += BinaryUtil.bytesForString(name);
            nodeStyle.fromBinary(arr, pos+mark, window);
            mark += nodeStyle.bytes();
            //Place it in the style manager after it is loaded in
            styleManager.saveNodeStyle(name, nodeStyle);
        }
        LineStyle lineStyle;
        for (i=0; i<lineStyleCount; i++) {
            lineStyle = new LineStyle();
            name = BinaryUtil.bytesToString(arr, pos+mark);
            mark += BinaryUtil.bytesForString(name);
            lineStyle.fromBinary(arr, pos+mark, window);
            mark += lineStyle.bytes();
            //Place it in the style manager after it is loaded in
            styleManager.saveLineStyle(name, lineStyle);
        }
        Node node;
        for (i=0; i<nodeCount; i++) {
            node = new Node(0, 0);
            node.fromBinary(arr, pos+mark, window);
            mark += node.bytes();
            nodes.add(node);
        }
        NodeLine nodeLine;
        int nodeLineChild;
        int nodeLineParent;
        for (i=0; i<nodeLineCount; i++) {
            nodeLine = new NodeLine(null, null);
            nodeLineChild = BinaryUtil.bytesToInt(arr, pos+mark);
            mark += 4;
            nodeLineParent = BinaryUtil.bytesToInt(arr, pos+mark);
            mark += 4;
            nodeLine.fromBinary(arr, pos+mark, window);
            Node child = nodes.get(nodeLineChild);
            Node parent = nodes.get(nodeLineParent);
            nodeLine.setChild(child);
            nodeLine.setParent(parent);
            child.getLinesConnected().add(nodeLine);
            parent.getLinesConnected().add(nodeLine);
            mark += (nodeLine.bytes()-8);
            //Minus 8 because the other bytes have already been added
        }
    }
    
    /**
     * Creates a flowchart from data obtained from an InputStream.
     * This function closes the InputStream.
     * @param stream the stream with the flowchart data, opened.
     * @param window the window using the flowchart
     * @return the loaded flowchart.
     */
    public static final Flowchart readFlowchartData(InputStream stream, FlowchartWindow window) {
        Flowchart chart = new Flowchart(false);
        byte[] data = null;
        try {
            byte[] bSize = new byte[4];
            stream.read(bSize);
            int fileSize = BinaryUtil.bytesToInt(bSize, 0);
            data = new byte[fileSize];
            int actualRead;
            if ((actualRead = stream.read(data)) != data.length) {
                System.err.println("The file was expected to be " + data.length
                 + " bytes long, but it was actually " + actualRead + " bytes long");
            }
            stream.close();
        } catch(IOException e) {
            System.err.println("Could not read flowchart from input stream: " + e);
        }
        if (data != null) {
            chart.passStyleManager(window.getStyleManager());
            chart.fromBinary(data, 0, window);
            return chart;
        } else {
            System.err.println("Unable to read flowchart, using the default flowchart instead.");
            return new Flowchart(true);
        }
    }
    
}
