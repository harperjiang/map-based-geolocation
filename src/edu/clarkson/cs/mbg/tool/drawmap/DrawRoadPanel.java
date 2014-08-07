package edu.clarkson.cs.mbg.tool.drawmap;

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

	private BigDecimal scale;

	List<GeoPoint> drawPoints;

	GeoPoint min;

	GeoPoint max;

	private boolean processed = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2188675489303787697L;

	public void setDrawPoints(List<GeoPoint> drawPoints, GeoPoint min,
			GeoPoint max) {
		this.drawPoints = drawPoints;
		this.min = min;
		this.max = max;
		processData();
	}

	protected void processData() {
		for (GeoPoint point : drawPoints) {
			point.latitude = point.latitude.subtract(min.latitude).multiply(
					scale);
			point.longitude = point.longitude.subtract(min.longitude).multiply(
					scale);
		}

		max.latitude = max.latitude.subtract(min.latitude).multiply(scale);
		max.longitude = max.longitude.subtract(min.longitude).multiply(scale);

		min.latitude = BigDecimal.ZERO;
		min.longitude = BigDecimal.ZERO;

		processed = true;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!processed) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2.5f));

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

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

}
