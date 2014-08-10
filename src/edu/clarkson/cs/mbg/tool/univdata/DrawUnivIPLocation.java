package edu.clarkson.cs.mbg.tool.univdata;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import edu.clarkson.cs.mbg.tool.drawrange.DrawPoint;
import edu.clarkson.cs.mbg.tool.drawrange.DrawRangeFrame;

/**
 * This tool draw the known university location on map
 * @author harper
 *
 */
public class DrawUnivIPLocation {

	public static void main(String[] args) throws Exception {
		DrawRangeFrame frame = new DrawRangeFrame();
		frame.setVisible(false);

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/all_university_ip_loc.tsv")));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\t");
			frame.getDrPanel()
					.getItems()
					.add(new DrawPoint(new Point(new BigDecimal(parts[4])
							.intValue(), new BigDecimal(parts[3]).intValue())));
		}

		br.close();

		frame.setVisible(true);
	}
}
