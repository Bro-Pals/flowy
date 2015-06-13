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
package bropals.flowy.action.style;

import bropals.flowy.FlowchartWindow;
import bropals.flowy.data.Selectable;
import bropals.flowy.style.LineStyle;
import java.util.ArrayList;

/**
 *
 * @author Kevin
 */
public class EditedNodeLineStyle extends EditedStyle {
    
    ArrayList<LineStyle> styles;
    
    public EditedNodeLineStyle(ArrayList<Selectable> changedThings, ArrayList<LineStyle> oldStyles) {
        super(changedThings);
        styles = oldStyles;
    }
    
    @Override
    public void undo(FlowchartWindow instance) {
        System.out.println("Undoing applyin node line styles");
        for (int i=0; i<styles.size(); i++) {
            getEditedNodeLines().get(i).setStyle(styles.get(i));
        }
    }
    
}
