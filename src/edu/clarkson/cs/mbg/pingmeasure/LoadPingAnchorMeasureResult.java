package edu.clarkson.cs.mbg.pingmeasure;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.api.measurement.MeasurementGetResponse;
import edu.clarkson.cs.clientlib.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.clientlib.ripeatlas.model.Measurement;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.model.PingAnchor;

public class LoadPingAnchorMeasureResult {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();

		EntityManager em = BeanContext.get().get("entityManager");
		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		PingAnchorDao paDao = BeanContext.get().get("pingAnchorDao");

		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(PingAnchor.class);
		Map<String, PropertyDescriptor> pmap = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor pd : pds) {
			pmap.put(pd.getName(), pd);
		}

		List<PingAnchor> buffer = new ArrayList<PingAnchor>();

		for (int i = 1723154; i <= 1723189; i++) {
			MeasurementGetResponse mmtr = ma.get(i).execute();
			if (mmtr.getError() != null) {
				continue;
			}
			Measurement mt = mmtr.getResult();
			if (!mt.getDescription().startsWith("PingProbe_{}")) {
				continue;
			}
			MeasurementResultResponse resp = ma.result(i).execute();
			if (resp.getError() != null)
				// This measurement is not ours
				continue;
			List<MeasurementResult> mrs = resp.getResult();
			PingAnchor pa = new PingAnchor();
			for (MeasurementResult mr : mrs) {
				if (StringUtils.isEmpty(pa.getIpAddress())) {
					pa.setIpAddress(mr.getDstAddr());
				} else {
					Validate.isTrue(pa.getIpAddress().equals(mr.getDstAddr()),
							"invalid measurement:" + i);
				}
				String fieldName = "anchor" + mr.getProbeId();
				pmap.get(fieldName).getWriteMethod().invoke(pa, mr.getAvg());
			}
			if (paDao.findByIp(pa.getIpAddress()).isEmpty())
				// Not exist yet
				buffer.add(pa);

			if (buffer.size() >= 20) {
				em.getTransaction().begin();
				for (PingAnchor tpa : buffer)
					em.persist(tpa);
				em.getTransaction().commit();
				buffer.clear();
			}
		}

		em.getTransaction().begin();
		for (PingAnchor tpa : buffer)
			em.persist(tpa);
		em.getTransaction().commit();
		buffer.clear();
	}
}
