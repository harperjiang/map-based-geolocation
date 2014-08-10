package edu.clarkson.cs.mbg.tool.drawrange;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawPoint implements DrawItem {

	private Point point;

	public DrawPoint(Point point) {
		this.point = point;
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		g2d.fillOval(point.x * DrawRangePanel.SCALE - POINT_RADIUS, point.y
				* DrawRangePanel.SCALE - POINT_RADIUS, 2 * POINT_RADIUS,
				2 * POINT_RADIUS);
	}

	static final int POINT_RADIUS = 4;
}
