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
import edu.clarkson.cs.mbg.pingmeasure.dao.PingProbeDao;
import edu.clarkson.cs.mbg.pingmeasure.model.PingProbe;

public class LoadPingProbeMeasureResult {

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();

		EntityManager em = BeanContext.get().get("entityManager");
		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		PingProbeDao ppDao = BeanContext.get().get("pingProbeDao");

		PropertyDescriptor[] pds = PropertyUtils
				.getPropertyDescriptors(PingProbe.class);
		Map<String, PropertyDescriptor> pmap = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor pd : pds) {
			pmap.put(pd.getName(), pd);
		}

		List<PingProbe> buffer = new ArrayList<PingProbe>();

		for (int i = 1724578; i <= 1726439; i++) {
			MeasurementGetResponse mmtr = ma.get(i).execute();
			if (mmtr.getError() != null) {
				continue;
			}
			Measurement mt = mmtr.getResult();
			if (!mt.getDescription().startsWith("PingProbe_Univ")) {
				continue;
			}
			MeasurementResultResponse resp = ma.result(i).execute();
			if (resp.getError() != null)
				// This measurement is not ours
				continue;
			List<MeasurementResult> mrs = resp.getResult();
			PingProbe pp = new PingProbe();
			for (MeasurementResult mr : mrs) {
				if (StringUtils.isEmpty(pp.getIpAddress())) {
					pp.setIpAddress(mr.getDstAddr());
				} else {
					Validate.isTrue(pp.getIpAddress().equals(mr.getDstAddr()),
							"invalid measurement:" + i);
				}
				String fieldName = ProbeMapping.fieldName(mr.getProbeId());
				pmap.get(fieldName).getWriteMethod().invoke(pp, mr.getAvg());
			}
			if (ppDao.findByIp(pp.getIpAddress()).isEmpty())
				// Not exist yet
				buffer.add(pp);

			if (buffer.size() >= 20) {
				em.getTransaction().begin();
				for (PingProbe tpa : buffer)
					em.persist(tpa);
				em.getTransaction().commit();
				buffer.clear();
			}
		}

		em.getTransaction().begin();
		for (PingProbe tpa : buffer)
			em.persist(tpa);
		em.getTransaction().commit();
		buffer.clear();
	}
}
