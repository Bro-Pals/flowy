/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bropals.flowy.listeners;

import bropals.flowy.FlowchartWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The listener for the export chart to image button.
 * @author Jonathon
 */
public class ExportChartToImageListener extends AbstractFlowyListener implements ActionListener {

    public ExportChartToImageListener(FlowchartWindow window) {
        super(window);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
}
