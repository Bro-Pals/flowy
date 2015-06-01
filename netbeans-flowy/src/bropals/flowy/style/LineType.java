/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bropals.flowy.style;

import bropals.flowy.Camera;
import bropals.flowy.data.Node;
import bropals.flowy.data.NodeLine;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Kevin
 */
public enum LineType {
    SOLID, DASHED, DOTTED;
    
    /**
     * Renders this line tpe, changing the color of the graphics to match the style.
     * @param n The node line to render
     * @param camera The camera in the flowchart
     * @param g The graphics to draw it to
     * @return An array of two points of integers, representing the start and end point of
     *      the line in canvas units. 
     */
    public Point[] renderLine(NodeLine n, Camera camera, Graphics g) {
        Node par = n.getParent();
        Node chi = n.getChild();

        Point.Float p1 = new Point.Float((par.getX() + ((par.getWidth() / camera.getZoom())/2)), 
                par.getY() + ((par.getHeight() / camera.getZoom())/2));
        Point.Float p2 = new Point.Float(chi.getX() + ((chi.getWidth() / camera.getZoom())/2), 
                chi.getY() + ((chi.getHeight() / camera.getZoom())/2));

        Point int_p1 = camera.convertWorldToCanvas(p1);
        Point int_p2 = camera.convertWorldToCanvas(p2);
        
        switch(this) {
            case SOLID:
                g.setColor(n.getStyle().getLineColor());
                g.drawLine((int)int_p1.getX(),(int)int_p1.getY(), 
                        (int)int_p2.getX(), (int)int_p2.getY());
                break;
        }
        
        return new Point[]{int_p1, int_p2};
    }
}
