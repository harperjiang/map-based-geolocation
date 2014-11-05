package edu.clarkson.cs.mbg.tool.univdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.clientlib.ipinfo.IPInfoContextSet;
import edu.clarkson.cs.clientlib.ipinfo.model.IPInfo;
import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.UniversityDao;
import edu.clarkson.cs.mbg.map.model.University;

/**
 * Deal with the problems encountered in previous step
 * 
 * @author harper
 * 
 */
public class WashUniversityData2 {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();
		new IPInfoContextSet().apply();

		UniversityDao univDao = BeanContext.get().get("universityDao");
		IPInfoAccess ipAccess = BeanContext.get().get("ipinfoAccess");

		for (University univ : univDao.all()) {
			if (univ.getRemark().startsWith("pass"))
				continue;

			String remark = univ.getRemark();
			if (remark.startsWith("ip from different subnet")) {
				String[] ips = remark.replace("ip from different subnet", "")
						.trim().split(",");

				Set<String> org = new HashSet<String>();
				for (String ip : ips) {
					IPInfo info = ipAccess.getInfo(ip);
					org.add(info.getOrganization());
				}

				StringBuilder sb = new StringBuilder();
				for (String str : org) {
					sb.append(str).append(",");
				}
				univ.setRemark("from orgs " + sb.toString());
				univDao.save(univ);
			}
			if (remark.startsWith("from orgs")) {
				List<String> ips = new ArrayList<String>();
				WashUniversityData.ipaddressWash(univ.getWebsite(), ips);
				for (String ip : ips) {
					IPInfo info = ipAccess.getInfo(ip);
					if (info.getOrganization().contains("University")
							|| info.getOrganization().contains("College")) {
						univ.setIpAddress(ip);
						univ.setLatitude(info.getLatitude());
						univ.setLongitude(info.getLongitude());
						univ.setRemark("pass");
						univDao.save(univ);
					}
				}
			}
			if (remark.startsWith("alias")) {
				List<String> ips = new ArrayList<String>();
				WashUniversityData.ipaddressWash(univ.getWebsite(), ips);
				for (String ip : ips) {
					IPInfo info = ipAccess.getInfo(ip);
					if (info.getOrganization().contains("University")
							|| info.getOrganization().contains("College")) {
						univ.setIpAddress(ip);
						univ.setLatitude(info.getLatitude());
						univ.setLongitude(info.getLongitude());
						univ.setRemark("pass " + info.getOrganization());
						univDao.save(univ);
					}
				}
			}
		}
	}

}
