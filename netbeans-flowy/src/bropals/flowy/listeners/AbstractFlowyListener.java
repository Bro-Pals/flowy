/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bropals.flowy.listeners;

import bropals.flowy.FlowchartWindow;

/**
 *
 * @author Jonathon
 */
public abstract class AbstractFlowyListener {
    
    private FlowchartWindow flowchartWindow;
    
    public AbstractFlowyListener(FlowchartWindow window) {
        flowchartWindow = window;
    }

    public FlowchartWindow getFlowchartWindow() {
        return flowchartWindow;
    }
    
    
}