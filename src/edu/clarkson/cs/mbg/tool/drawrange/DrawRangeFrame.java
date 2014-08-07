package edu.clarkson.cs.mbg.tool.drawrange;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class DrawRangeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440957940941695242L;

	public DrawRangeFrame() {
		super();
		setTitle("Draw Range");
		setSize(1000, 1000);

		setLayout(new BorderLayout());

		add(new DrawRangePanel(), BorderLayout.CENTER);

		setVisible(true);
	}
}
