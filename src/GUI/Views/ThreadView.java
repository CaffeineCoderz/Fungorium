package GUI.Views;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import GUI.GameObjectEvent;
import fungus.FungusThread;
import fungus.FungusBody;
import tektonTypes.Tekton;

public class ThreadView extends AbstractGameObjectView {
    private static final int THREAD_WIDTH = 3;
    private FungusThread thread;
    private Map<String, GameObjectView> allViews;
    
    public ThreadView(String objectName, Point position, FungusThread thread, 
            Map<String, GameObjectView> allViews) {
        super(objectName, position);
        this.thread = thread;
        this.allViews = allViews;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        try {
            if (thread.isBridge()) {
                drawBridgeThread(g2d);
            } else if (thread.getNext() != null) {
                drawConnectedThread(g2d);
            } else {
                drawRegularThread(g2d);
            }
            
            // Draw thread name
            g2d.setColor(Color.WHITE);
            g2d.drawString(objectName, position.x, position.y);
        } catch (Exception e) {
            System.err.println("Error drawing thread: " + objectName);
        }
    }
    
    private void drawBridgeThread(Graphics2D g2d) {
        List<Tekton> tektons = thread.getTektons();
        if (tektons.size() >= 2) {
            Tekton firstTekton = tektons.get(0);
            Tekton secondTekton = tektons.get(1);
            
            TektonView firstTektonView = findTektonView(firstTekton);
            TektonView secondTektonView = findTektonView(secondTekton);
            
            if (firstTektonView != null && secondTektonView != null) {
                Point firstControlPoint = findClosestCardinalPoint(firstTektonView, 
                    secondTektonView.getPosition());
                Point secondControlPoint = findClosestCardinalPoint(secondTektonView, 
                    firstTektonView.getPosition());
                
                if (firstControlPoint != null && secondControlPoint != null) {
                    drawThreadLine(g2d, firstControlPoint, secondControlPoint);
                }
            }else {
                
            }
        }
    }
    
    private void drawConnectedThread(Graphics2D g2d) {
        FungusThread nextThread = thread.getNext();
        if (nextThread != null) {
            ThreadView nextThreadView = findThreadView(nextThread);
            if (nextThreadView != null) {
                Point nextThreadPos = nextThreadView.getPosition();
                drawThreadLine(g2d, position, nextThreadPos);
            }
        }
    }
    
    private void drawRegularThread(Graphics2D g2d) {
        Tekton tekton = thread.getTektons().isEmpty() ? null : thread.getTektons().get(0);
        if (tekton != null) {
            TektonView tektonView = findTektonView(tekton);
            if (tektonView != null) {
                Point controlPoint = tektonView.getCardinalPoint("n");
                
                if (thread.getMyBody() != null) {
                    BodyView bodyView = findBodyView(thread.getMyBody());
                    if (bodyView != null && controlPoint != null) {
                        drawThreadLine(g2d, controlPoint, bodyView.getPosition());
                        return;
                    }
                }
                
                // Fallback: Draw between cardinal points if no body
                Point anotherControlPoint = tektonView.getCardinalPoint("s");
                if (controlPoint != null && anotherControlPoint != null) {
                    drawThreadLine(g2d, controlPoint, anotherControlPoint);
                }
            }
        }
    }
    
    private void drawThreadLine(Graphics2D g2d, Point start, Point end) {
        g2d.setColor(new Color(150, 75, 0));
        g2d.setStroke(new BasicStroke(THREAD_WIDTH));
        g2d.draw(new Line2D.Double(start.x, start.y, end.x, end.y));
    }
    
    private TektonView findTektonView(Tekton tekton) {
        for (GameObjectView view : allViews.values()) {
            if (view instanceof TektonView && ((TektonView)view).getTekton() == tekton) {
                return (TektonView)view;
            }
        }
        return null;
    }
    
    private ThreadView findThreadView(FungusThread thread) {
        for (GameObjectView view : allViews.values()) {
            if (view instanceof ThreadView && ((ThreadView)view).getThread() == thread) {
                return (ThreadView)view;
            }
        }
        return null;
    }
    
    private BodyView findBodyView(FungusBody body) {
        for (GameObjectView view : allViews.values()) {
            if (view instanceof BodyView && ((BodyView)view).getBody() == body) {
                return (BodyView)view;
            }
        }
        return null;
    }
    
    private Point findClosestCardinalPoint(TektonView tektonView, Point targetPoint) {
        List<Point> cardinalPoints = (List<Point>) tektonView.getCardinalPoints();
        if (cardinalPoints == null || cardinalPoints.isEmpty()) return null;
        
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Point cardinalPoint : cardinalPoints) {
            double distance = cardinalPoint.distance(targetPoint);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = cardinalPoint;
            }
        }
        
        return closestPoint;
    }
    
    public FungusThread getThread() {
        return thread;
    }
    
    @Override
    public void update(GameObjectEvent event) {
        //! Handle thread state changes if needed
    }
}