package edu.clarkson.cs.mbg.tracedata;

import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;

public class CompareMeasureResult {

	public static void main(String[] args) {
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");
		
		
		
		System.out.println(tdService.comparePing(1719137, 1719138));
		System.out.println(tdService.comparePing(1719138, 1719139));
		System.out.println(tdService.comparePing(1719137, 1719139));

	}
}
