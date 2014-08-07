package edu.clarkson.cs.mbg.tool.drawrange;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class DrawRangeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440957940941695242L;

	private DrawRangePanel drPanel;

	public DrawRangeFrame() {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Draw Range");
		setSize(1000, 1000);

		setLayout(new BorderLayout());

		drPanel = new DrawRangePanel();
		add(drPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	public DrawRangePanel getDrPanel() {
		return drPanel;
	}

	public static void main(String[] args) {
		new DrawRangeFrame();
	}
}
