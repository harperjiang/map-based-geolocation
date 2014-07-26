package edu.clarkson.cs.mbg.tracedata;

public class IPUtils {
	
	public static boolean isPrivateIp(String from) {
		if (from.startsWith("192.168."))
			return true;
		if (from.startsWith("10."))
			return true;
		if (from.startsWith("172.")) {
			String[] parts = from.split("\\.");
			Integer part2 = Integer.parseInt(parts[1]);
			if (part2 >= 16 && part2 <= 31)
				return true;
		}
		return false;
	}
}
