package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import renders.View;

public class EventManager implements KeyListener, MouseListener, MouseMotionListener {

    private View currentView;

    public EventManager(View currentView) {
        this.currentView = currentView;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        currentView.handleKeyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentView.handleKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentView.handleKeyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentView.handleMouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentView.handleMousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentView.handleMouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        currentView.handleMouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        currentView.handleMouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentView.handleMouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentView.handleMouseMoved(e);
    }
}
