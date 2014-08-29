package edu.clarkson.cs.mbg.tool.univdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.clientlib.ipinfo.IPInfoContextSet;
import edu.clarkson.cs.clientlib.ipinfo.model.IPInfo;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.lang.proc.ProcessRunner;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.UniversityDao;
import edu.clarkson.cs.mbg.map.model.University;
import edu.clarkson.cs.mbg.topology.IPUtils;

/**
 * Query IP address for the collected university websites, and report possible
 * errors that require manual involvement.
 * 
 * @author harper
 * 
 */
public class WashUniversityData {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();
		new IPInfoContextSet().apply();

		UniversityDao univDao = BeanContext.get().get("universityDao");
		IPInfoAccess ipAccess = BeanContext.get().get("ipinfoAccess");

		for (University univ : univDao.all()) {
			if (!StringUtils.isEmpty(univ.getRemark()))
				continue;

			String website = univ.getWebsite();
			List<String> ips = new ArrayList<String>();
			String mark = ipaddressWash(website, ips);
			if (!StringUtils.isEmpty(mark)) {
				univ.setRemark(mark);
				univDao.save(univ);
			} else {
				String ip = ips.get(0);
				if (!IPUtils.samePrefix(univ.getIpAddress(), ip, 16)) {
					// Maybe incorrect ip, use the new one
					univ.setIpAddress(ip);
					IPInfo info = ipAccess.getInfo(ip);
					univ.setLatitude(info.getLatitude());
					univ.setLongitude(info.getLongitude());
				}
				univ.setRemark("pass");
				univDao.save(univ);
			}
		}
	}

	static Pattern addressPattern = Pattern
			.compile(".+ has address ([0-9\\.]+)");

	static Pattern aliasPattern = Pattern
			.compile("([\\w0-9\\.]+) is an alias for ([\\w0-9\\-\\.]+)\\.");

	protected static String ipaddressWash(String website, List<String> ipresult)
			throws InterruptedException, IOException {
		ProcessRunner pr = new ProcessRunner("host", website);
		int result = pr.runAndWait();
		if (result != 0) {
			return "error executing host";
		}
		List<String> outputs = pr.getOutput();
		for (String output : outputs) {
			Matcher matcher = addressPattern.matcher(output);
			if (matcher.matches()) {
				ipresult.add(matcher.group(1));
				continue;
			}
			matcher = aliasPattern.matcher(output);
			if (matcher.matches()) {
				// Check whether they have the same suffix
				String[] addrs1 = matcher.group(1).split("\\.");
				String[] addrs2 = matcher.group(2).split("\\.");

				if (!(addrs1[addrs1.length - 1]
						.equals(addrs2[addrs2.length - 1]) && addrs1[addrs1.length - 2]
						.equals(addrs2[addrs2.length - 2]))) {
					if (!addrs2[addrs2.length - 1].equals("edu"))
						return "alias " + matcher.group(2);
				}
			}
		}
		// Check whether all the ips are in the same subnet. University have to
		// at least have a 16 prefix the same
		for (int i = 0; i < ipresult.size(); i++) {
			for (int j = i + 1; j < ipresult.size(); j++) {
				if (!IPUtils.samePrefix(ipresult.get(i), ipresult.get(j), 16))
					return "ip from different subnet " + join(ipresult);
			}
		}
		return null;
	}

	public static String join(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			sb.append(string).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
