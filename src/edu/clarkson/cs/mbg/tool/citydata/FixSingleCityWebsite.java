package edu.clarkson.cs.mbg.tool.citydata;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.gsearch.GSearchAccess;
import edu.clarkson.cs.clientlib.gsearch.GSearchContextSet;
import edu.clarkson.cs.clientlib.gsearch.model.SearchResponse;
import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.CityWebsiteDao;
import edu.clarkson.cs.mbg.map.model.CityWebsite;

public class FixSingleCityWebsite {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();
		new GSearchContextSet().apply();
		GSearchAccess access = BeanContext.get().get("gsearchAccess");
		IPInfoAccess ipAccess = BeanContext.get().get("ipinfoAccess");

		CityWebsiteDao cityWebsiteDao = BeanContext.get().get("cityWebsiteDao");

		CityWebsite cw = cityWebsiteDao.find(3325);

		String query = MessageFormat.format("City of {0},{1}", cw.getCity()
				.getName(), StateNameUtils.getStateName(cw.getCity().getState()
				.toUpperCase()));
		System.out.println("Querying " + query);
		SearchResponse resp = access.search(query).execute().getResult();
		cw.setWebsite(resp.getResults().get(0).getVisibleUrl());

		List<String> ips = new ArrayList<String>();
		String result = QueryCityIpAddress.ipaddressWash(cw.getWebsite(), ips);
		if (!StringUtils.isEmpty(result)) {
			cw.setRemark(result);
		}
		cw.getIpAddresses().clear();
		cw.setIpAddress(null);
		for (String ip : ips) {
			cw.getIpAddresses().add(ip);
			ipAccess.getInfo(ip);
		}

		cityWebsiteDao.save(cw);
	}
}
