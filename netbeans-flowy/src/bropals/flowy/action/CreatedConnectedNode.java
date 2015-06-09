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
package bropals.flowy.action;

import bropals.flowy.FlowchartWindow;

/**
 * The action that occurs when you make a new node by dragging it from an 
 *  existing node and creating a connection with it.
 * @author Kevin
 */
public class CreatedConnectedNode extends Action {

    @Override
    public void undo(FlowchartWindow instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}