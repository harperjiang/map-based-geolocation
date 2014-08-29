package edu.clarkson.cs.mbg.tool.citydata;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.gsearch.GSearchAccess;
import edu.clarkson.cs.clientlib.gsearch.GSearchContextSet;
import edu.clarkson.cs.clientlib.gsearch.model.SearchResponse;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.CityWebsiteDao;
import edu.clarkson.cs.mbg.map.model.CityWebsite;

public class QueryCityWebsite {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();
		new GSearchContextSet().apply();
		GSearchAccess access = BeanContext.get().get("gsearchAccess");

		CityWebsiteDao cityWebsiteDao = BeanContext.get().get("cityWebsiteDao");

		for (CityWebsite cw : cityWebsiteDao.all()) {
			// Second round
			// if (!StringUtils.isEmpty(cw.getIpAddress()))
			// continue;
			// First round
			if (!StringUtils.isEmpty(cw.getWebsite()))
				continue;
			String query = MessageFormat.format("City of {0},{1}", cw.getCity()
					.getName(), StateNameUtils.getStateName(cw.getCity()
					.getState().toUpperCase()));
			System.out.println("Querying " + query);
			SearchResponse resp = access.search(query).execute().getResult();
			cw.setWebsite(resp.getResults().get(0).getVisibleUrl());
			cityWebsiteDao.save(cw);
		}
	}
}
