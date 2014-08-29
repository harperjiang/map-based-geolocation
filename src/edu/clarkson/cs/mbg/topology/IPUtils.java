package edu.clarkson.cs.mbg.topology;

import org.apache.commons.lang3.Validate;

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

	public static boolean samePrefix(String ip1, String ip2, int prefix) {
		return prefix(ip1, prefix) == prefix(ip2, prefix);
	}

	protected static long prefix(String ip, int prefix) {
		String[] parts = ip.split("\\.");
		Validate.isTrue(parts.length == 4);
		long val = 0;
		for (int i = 0; i < parts.length; i++) {
			val += Long.valueOf(parts[3 - i]) << 8 * i;
		}
		return val >> (32 - prefix);
	}
}
