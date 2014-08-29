package edu.clarkson.cs.mbg.pingmeasure;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.model.PingAnchor;

public class RefreshIPInfoOnPing {

	public static void main(String[] args) {
		new MBGContextSet().apply();

		IPInfoAccess ipinfoAccess = BeanContext.get().get("ipinfoAccess");
		PingAnchorDao ppDao = BeanContext.get().get("pingAnchorDao");

		for (PingAnchor pp : ppDao.all()) {
			ipinfoAccess.getInfo(pp.getIpAddress());
		}
	}

}
