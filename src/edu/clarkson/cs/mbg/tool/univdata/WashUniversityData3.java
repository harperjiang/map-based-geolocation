package edu.clarkson.cs.mbg.tool.univdata;

import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.clientlib.ipinfo.IPInfoContextSet;
import edu.clarkson.cs.clientlib.ipinfo.model.IPInfo;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.UniversityDao;
import edu.clarkson.cs.mbg.map.model.University;

/**
 * Check data that has different IP and state info
 * 
 * @author harper
 * 
 */
public class WashUniversityData3 {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();
		new IPInfoContextSet().apply();

		UniversityDao univDao = BeanContext.get().get("universityDao");
		IPInfoAccess ipAccess = BeanContext.get().get("ipinfoAccess");

		for (University univ : univDao.all()) {
			IPInfo info = ipAccess.getInfo(univ.getIpAddress());
			if (!StringUtils.isEmpty(univ.getState())) {
				if (!univ.getState().equals(info.getRegionCode())) {
					System.out.println(univ.getWebsite());
				}
			}

		}
	}

}
