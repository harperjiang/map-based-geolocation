package edu.clarkson.cs.mbg.tool.drawrange;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DrawRangePanel extends JPanel implements MouseMotionListener,
		MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4444845135010371161L;

	public DrawRangePanel() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);

		items = new ArrayList<DrawItem>();
	}

	// 20 pixels for 1
	protected static final int SCALE = 20;

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (null == center) {
			center = new Point(getSize().width / 2, getSize().height / 2);
		}

		// Draw x,y axis
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(center.x, 0, center.x, getSize().height);
		g2d.drawLine(0, center.y, getSize().width, center.y);

		g2d.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER, 10.0f, new float[] { 4f, 4f }, 0.0f));
		g2d.setColor(Color.GRAY);

		// Draw the dashes
		boolean leftTrue = true, rightTrue = true;
		int leftx = center.x - SCALE, rightx = center.x + SCALE;
		while (leftTrue || rightTrue) {
			g2d.drawLine(leftx, 0, leftx, getSize().height);
			g2d.drawLine(rightx, 0, rightx, getSize().height);
			leftx -= SCALE;
			rightx += SCALE;
			leftTrue = leftx >= 0;
			rightTrue = rightx <= getSize().width;
		}

		boolean topTrue = true, bottomTrue = true;
		int topy = center.y - SCALE, bottomy = center.y + SCALE;
		while (topTrue || bottomTrue) {
			g2d.drawLine(0, topy, getSize().width, topy);
			g2d.drawLine(0, bottomy, getSize().width, bottomy);
			topy -= SCALE;
			bottomy += SCALE;
			topTrue = topy >= 0;
			bottomTrue = bottomy <= getSize().height;
		}

		// Draw the points
		// g2d.translate(center.x, center.y);
		g2d.setTransform(new AffineTransform(1, 0, 0, -1, center.x, center.y));
		for (DrawItem item : items) {
			Graphics2D copy = (Graphics2D) g2d.create();
			item.draw(copy);
			copy.dispose();
		}
	}

	private List<DrawItem> items;

	public List<DrawItem> getItems() {
		return items;
	}

	public void setItems(List<DrawItem> items) {
		this.items = items;
	}

	// Axis infos

	private Point center;

	private Point dragPoint = null;

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragPoint != null) {
			center.x = center.x + e.getX() - dragPoint.x;
			center.y = center.y + e.getY() - dragPoint.y;
			dragPoint = e.getPoint();
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragPoint = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragPoint = null;
	}

	// These are methods not in use
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// private
}
