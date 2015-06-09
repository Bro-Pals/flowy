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
import java.util.ArrayList;

/**
 * Used to manage the history of what the user has done. Used to implement the undo feature.
 * @author Kevin
 */
public class HistoryManager {
    
    /**
     * A list of actions. The list can't be longer than HISTORY_LIMIT
     */
    private ArrayList<Action> actions;
    /**
     * The limit to how many Action objects can be stored in the actions array.
     */
    private final int HISTORY_LIMIT = 10;
    /**
     * The FlowchartWindow this HistoryManager is keeping track of the history of.
     */
    private FlowchartWindow instance;
    
    /**
     * Creates a new HistoryManager for the given FlowchartWindow.
     * @param window THe window that this object will track history for.
     */
    public HistoryManager(FlowchartWindow window) {
        instance = window;
    }
    
    /**
     * Undo the last action added to the history. This will remove the action 
     * from the history.
     */
    public void undoLastAction() {
        int last = actions.size() - 1;
        actions.get(last).undo(instance);
        actions.remove(last);
    }
    
    /**
     * Add an action to the history. If there are too many actions 
     * saved in the history, the oldest one is removed.
     * @param a The action to add
     */
    public void addToHistory(Action a) {
        actions.add(a);
        if (actions.size() > HISTORY_LIMIT) {
            actions.remove(0);
        }
    }
}