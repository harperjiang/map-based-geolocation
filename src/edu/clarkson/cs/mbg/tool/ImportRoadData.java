package edu.clarkson.cs.mbg.tool;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import edu.clarkson.cs.mbg.common.Environment;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.mbg.map.model.Waypoint;

public class ImportRoadData {

	public static void main(String[] args) throws Exception {
		FileInputStream inputStream = new FileInputStream(
				"/home/harper/ResearchData/map-based-geolocation/road/data.xml");
		XMLInputFactory f = XMLInputFactory.newInstance();
		XMLStreamReader r = f.createXMLStreamReader(inputStream);

		StateMachine sm = new StateMachine();

		List<Section> buffer = new ArrayList<Section>();
		int threshold = 5000;

		EntityManager em = Environment.getEnvironment().getEntityManager();

		while (r.hasNext()) {
			if (r.isStartElement()) {
				String name = r.getName().getLocalPart();
				if ("Record".equals(name)) {
					sm.reset();
				}
				if ("Value".equals(name)) {
					switch (sm.fieldIndex) {
					case 0: // OBJECT_ID
						break;
					case 1: // Shape
						break;
					case 2: // PRIME_NAME
						sm.road.setName(r.getElementText());
						break;
					case 3: // SECONDARY
						sm.road.setName2(r.getElementText());
						break;
					case 4: // PREFIX_DIR
						sm.road.setPrefixDir(r.getElementText());
						break;
					case 5: // PRE_TYPE
						sm.road.setPrefixType(r.getElementText());
						break;
					case 6: // NAME
						sm.road.setPrefixNum(r.getElementText());
						break;
					case 7: // STREET_TYP
						sm.road.setStreetType(r.getElementText());
						break;
					case 8: // SUFFIX_DIR
						sm.road.setSuffixDir(r.getElementText());
						break;
					case 9: // INTERSTATE
						break;
					case 10:// US_ROUTE
						break;
					case 11:// ST_ROUTE
						break;
					case 12:// QUALIFIER
						sm.road.setQualifier(r.getElementText());
						break;
					case 13:// TRANS_TYPE
						sm.road.setTransType(r.getElementText());
						break;
					case 14:// FCODE
						sm.road.setFcode(r.getElementText());
						break;
					case 15:// STATE_FIPS
						break;
					case 16:// STATE
						sm.road.setState(r.getElementText());
						break;
					case 17:// MILES
						sm.road.setMiles(new BigDecimal(r.getElementText()));
						break;
					case 18:// KILOMETERS
						sm.road.setKilometers(new BigDecimal(r.getElementText()));
						break;
					case 19:// Shape_Length
						sm.road.setShapeLength(new BigDecimal(r
								.getElementText()));
						break;
					default:
						break;
					}

					sm.nextField();
				}
				if ("XMax".equals(name) && sm.road != null) {
					sm.road.setLongMax(new BigDecimal(r.getElementText()));
				}
				if ("YMax".equals(name) && sm.road != null) {
					sm.road.setLatMax(new BigDecimal(r.getElementText()));
				}
				if ("XMin".equals(name) && sm.road != null) {
					sm.road.setLongMin(new BigDecimal(r.getElementText()));
				}
				if ("YMin".equals(name) && sm.road != null) {
					sm.road.setLatMin(new BigDecimal(r.getElementText()));
				}
				if ("Point".equals(name)) {
					sm.waypoint = new Waypoint();
				}
				if ("X".equals(name)) {
					BigDecimal xval = new BigDecimal(r.getElementText());
					sm.waypoint.setPointX(xval);
				}
				if ("Y".equals(name)) {
					BigDecimal yval = new BigDecimal(r.getElementText());
					sm.waypoint.setPointY(yval);
				}
			}
			if (r.isEndElement()) {
				String name = r.getName().getLocalPart();
				if ("Record".equals(name)) {
					// Save current road
					buffer.add(sm.road);

					if (buffer.size() >= threshold) {
						EntityTransaction t = em.getTransaction();
						t.begin();
						for (Section newr : buffer)
							em.persist(newr);
						t.commit();

						buffer.clear();
					}
				}
				if ("Point".equals(name)) {
					sm.road.addWaypoint(sm.waypoint);
				}
			}
			r.next();
		}

		r.close();

		// Save remaining data
		EntityTransaction t = em.getTransaction();
		t.begin();
		for (Section newr : buffer)
			em.persist(newr);
		t.commit();

	}

	public static class StateMachine {

		int fieldIndex;

		Section road;

		Waypoint waypoint;

		void reset() {
			road = new Section();
			fieldIndex = 0;
		}

		void nextField() {
			fieldIndex++;
		}
	}

}
