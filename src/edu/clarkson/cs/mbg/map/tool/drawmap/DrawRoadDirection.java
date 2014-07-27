package edu.clarkson.cs.mbg.map.tool.drawmap;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.mbg.map.model.Waypoint;

public class DrawRoadDirection {

	private Section roadToDraw;

	public DrawRoadDirection() {

	}

	public void draw() {
		JFrame frame = new JFrame();
		frame.setTitle("Draw Road Direction");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(800, 600);

		frame.getContentPane().setLayout(new BorderLayout());

		DrawRoadPanel drawPanel = new DrawRoadPanel();
		drawPanel.setDrawPoints(points());
		drawPanel.setDrawBounds(new GeoPoint(roadToDraw.getLatMin(),
				roadToDraw.getLongMin()), new GeoPoint(roadToDraw.getLatMax(),
				roadToDraw.getLongMax()));
		frame.getContentPane().add(drawPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	protected List<GeoPoint> points() {
		if (null == getRoadToDraw()) {
			return null;
		}
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		for (Waypoint wp : getRoadToDraw().getWaypoints()) {
			points.add(new GeoPoint(wp.getPointY(), wp.getPointX()));
		}

		return points;
	}

	public Section getRoadToDraw() {
		return roadToDraw;
	}

	public void setRoadToDraw(Section roadToDraw) {
		this.roadToDraw = roadToDraw;
	}

	public static void main(String[] args) {
		new MBGContextSet().apply();

		RoadDao roadDao = BeanContext.get().get("roadDao");
		Section road = roadDao.find(221118);

		DrawRoadDirection dr = new DrawRoadDirection();
		dr.setRoadToDraw(road);
		dr.draw();
	}
}
