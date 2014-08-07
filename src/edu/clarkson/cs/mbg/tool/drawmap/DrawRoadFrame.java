package edu.clarkson.cs.mbg.tool.drawmap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.mbg.map.model.Waypoint;

public class DrawRoadFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6230211170879056588L;

	private DrawRoadPanel roadPanel;

	private JTextField idField;

	private JTextField scaleField;

	private JLabel infoLabel;

	public DrawRoadFrame() {
		super();
		setTitle("Draw Road Direction");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 1100);

		getContentPane().setLayout(new BorderLayout());

		roadPanel = new DrawRoadPanel();
		getContentPane().add(roadPanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		controlPanel.add(new JLabel("Road ID:"));

		idField = new JTextField();
		idField.setPreferredSize(new Dimension(100, 25));
		controlPanel.add(idField);

		controlPanel.add(new JLabel("Scale:"));

		scaleField = new JTextField();
		scaleField.setPreferredSize(new Dimension(100, 25));
		controlPanel.add(scaleField);

		JButton drawButton = new JButton("Draw");
		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				final int id = Integer.valueOf(idField.getText());
				int scale = Integer.valueOf(scaleField.getText());

				roadPanel.setScale(new BigDecimal(scale));

				new Thread() {
					public void run() {
						final Section road = roadDao.find(id);
						if (road != null) {
							final List<GeoPoint> points = points(road);

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									infoLabel.setText(MessageFormat.format(
											"{0}/{1} in {2}", road.getName(),
											road.getName2(), road.getState()));

									roadPanel.setDrawPoints(
											points,
											new GeoPoint(road.getLatMin(), road
													.getLongMin()),
											new GeoPoint(road.getLatMax(), road
													.getLongMax()));
									roadPanel.repaint();
								}
							});
						}
					}
				}.start();
			}
		});
		controlPanel.add(drawButton);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);

		infoLabel = new JLabel();
		getContentPane().add(infoLabel, BorderLayout.NORTH);
		setVisible(true);
	}

	protected List<GeoPoint> points(Section road) {
		if (null == road) {
			return null;
		}
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		for (Waypoint wp : road.getWaypoints()) {
			points.add(new GeoPoint(wp.getPointY(), wp.getPointX()));
		}

		return points;
	}

	private RoadDao roadDao;

	public RoadDao getRoadDao() {
		return roadDao;
	}

	public void setRoadDao(RoadDao roadDao) {
		this.roadDao = roadDao;
	}

	public static void main(String[] args) {
		new MBGContextSet().apply();

		RoadDao roadDao = BeanContext.get().get("roadDao");

		DrawRoadFrame dr = new DrawRoadFrame();
		dr.setRoadDao(roadDao);
	}
}
