package edu.clarkson.cs.mbg.pingmeasure;

import java.awt.Point;

import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.model.PingAnchor;
import edu.clarkson.cs.mbg.tool.drawrange.DrawPoint;
import edu.clarkson.cs.mbg.tool.drawrange.DrawRangeFrame;

public class DrawPingMeasure {

	public static void main(String[] args) {
		new MBGContextSet().apply();
		PingAnchorDao paDao = BeanContext.get().get("pingAnchorDao");

		DrawRangeFrame frame = new DrawRangeFrame();
		frame.setVisible(false);
		for (PingAnchor pa : paDao.all()) {
			frame.getDrPanel()
					.getItems()
					.add(new DrawPoint(new Point(pa.getLongitude().intValue(),
							pa.getLatitude().intValue())));
		}
		frame.setVisible(true);
		frame.repaint();
	}

}
