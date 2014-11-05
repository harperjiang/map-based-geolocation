package edu.clarkson.cs.mbg.tool.citydata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.common.proc.ProcessRunner;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.CityWebsiteDao;
import edu.clarkson.cs.mbg.map.model.CityWebsite;

public class QueryCityIpAddress {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();

		CityWebsiteDao cwDao = BeanContext.get().get("cityWebsiteDao");
		IPInfoAccess ipAccess = BeanContext.get().get("ipinfoAccess");

		for (CityWebsite cw : cwDao.all()) {
			if (StringUtils.isEmpty(cw.getIpAddress())) {
				List<String> ips = new ArrayList<String>();
				String result = ipaddressWash(cw.getWebsite(), ips);
				if (!StringUtils.isEmpty(result)) {
					cw.setRemark(result);
				}
				for (String ip : ips) {
					cw.getIpAddresses().add(ip);
					ipAccess.getInfo(ip);
				}
				cwDao.save(cw);
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
		}
		return null;
	}

}
