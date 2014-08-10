package edu.clarkson.cs.mbg;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.clarkson.cs.clientlib.common.json.BeanDeserializer;
import edu.clarkson.cs.clientlib.common.json.BeanSerializer;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.lang.ContextSet;
import edu.clarkson.cs.clientlib.ripeatlas.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeAccess;
import edu.clarkson.cs.clientlib.ripeatlas.RipeAtlasEnvironment;
import edu.clarkson.cs.clientlib.ripeatlas.json.MeasurementDeserializer;
import edu.clarkson.cs.clientlib.ripeatlas.json.MeasurementResultDeserializer;
import edu.clarkson.cs.clientlib.ripeatlas.json.TracerouteDeserializer;
import edu.clarkson.cs.clientlib.ripeatlas.model.Measurement;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteTarget;
import edu.clarkson.cs.mbg.map.dao.JpaRoadDao;
import edu.clarkson.cs.mbg.topology.GraphService;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;
import edu.clarkson.cs.mbg.tracedata.dao.JpaTraceDataDao;

public class MBGContextSet implements ContextSet {

	@Override
	public void apply() {

		EntityManager em = Persistence.createEntityManagerFactory("mbg")
				.createEntityManager();

		JpaRoadDao roadDao = new JpaRoadDao();
		roadDao.setEntityManager(em);
		BeanContext.get().put("roadDao", roadDao);

		JpaTraceDataDao traceDataDao = new JpaTraceDataDao();
		traceDataDao.setEntityManager(em);
		BeanContext.get().put("traceDataDao", traceDataDao);

		RipeAtlasEnvironment env = new RipeAtlasEnvironment();
		// Create HTTP Client
		env.setHttpClient(HttpClients.createDefault());

		// Create JSON Parser
		GsonBuilder builder = new GsonBuilder();

		// Serializer/Deserializers for RipeAtlas
		builder.registerTypeAdapter(MeasurementResult.class,
				new MeasurementResultDeserializer());
		builder.registerTypeAdapter(TracerouteOutput.class,
				new TracerouteDeserializer());
		builder.registerTypeAdapter(Measurement.class,
				new MeasurementDeserializer());
		builder.registerTypeAdapter(Probe.class, new BeanDeserializer<Probe>());

		BeanSerializer<MeasurementCreate> mcbs = new BeanSerializer<MeasurementCreate>();
		builder.registerTypeAdapter(MeasurementCreate.class, mcbs);
		BeanSerializer<TracerouteTarget> ttbs = new BeanSerializer<TracerouteTarget>();
		builder.registerTypeAdapter(TracerouteTarget.class, ttbs);
		BeanSerializer<ProbeSpec> psbs = new BeanSerializer<ProbeSpec>();
		builder.registerTypeAdapter(ProbeSpec.class, psbs);

		Gson gson = builder.create();
		env.setGson(gson);

		mcbs.setGson(gson);
		ttbs.setGson(gson);
		psbs.setGson(gson);

		env.setReader(new JsonParser());

		// Beans
		MeasurementAccess ms = new MeasurementAccess();
		ms.setEnv(env);
		BeanContext.get().put("measurementService", ms);
		ProbeAccess ps = new ProbeAccess();
		ps.setEnv(env);
		BeanContext.get().put("probeService", ps);

		TraceDataService traceDataService = new TraceDataService();
		traceDataService.setMeasurementAccess(ms);
		traceDataService.setProbeAccess(ps);

		BeanContext.get().put("traceDataService", traceDataService);
		
		BeanContext.get().put("graphService", new GraphService());
	}

}
