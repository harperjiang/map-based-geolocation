package edu.clarkson.cs.mbg.map.tool.drawmap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JPanel;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class DrawRoadPanel extends JPanel {

	private static BigDecimal factor = new BigDecimal(100000);

	List<GeoPoint> drawPoints;

	GeoPoint min;

	GeoPoint max;

	private boolean processed = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2188675489303787697L;

	public void setDrawPoints(List<GeoPoint> drawPoints) {
		this.drawPoints = drawPoints;
	}

	public void setDrawBounds(GeoPoint min, GeoPoint max) {
		this.min = min;
		this.max = max;
	}

	protected void processData() {
		if (!processed) {
			for (GeoPoint point : drawPoints) {
				point.latitude = point.latitude.subtract(min.latitude)
						.multiply(factor);
				point.longitude = point.longitude.subtract(min.longitude)
						.multiply(factor);
			}

			max.latitude = max.latitude.subtract(min.latitude).multiply(factor);
			max.longitude = max.longitude.subtract(min.longitude).multiply(
					factor);

			min.latitude = BigDecimal.ZERO;
			min.longitude = BigDecimal.ZERO;
		}
		processed = true;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2.5f));

		processData();

		// Convert to upside-down
		g2d.setTransform(new AffineTransform(1, 0, 0, -1, 500 - max.longitude
				.intValue() / 2, 500 + max.latitude.intValue() / 2));

		GeoPoint previous = null;
		for (GeoPoint point : drawPoints) {
			if (previous != null) {
				g2d.drawLine(previous.longitude.intValue(),
						previous.latitude.intValue(),
						point.longitude.intValue(), point.latitude.intValue());
			}
			previous = point;
		}
	}
}
