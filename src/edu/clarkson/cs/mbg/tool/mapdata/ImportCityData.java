package edu.clarkson.cs.mbg.tool.mapdata;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import edu.clarkson.cs.mbg.common.Environment;
import edu.clarkson.cs.mbg.map.model.City;
import edu.clarkson.cs.mbg.map.model.Waypoint;

public class ImportCityData {

	public static void main(String[] args) throws Exception {
		FileInputStream inputStream = new FileInputStream(
				"/home/harper/ResearchData/map-based-geolocation/city/data.xml");
		XMLInputFactory f = XMLInputFactory.newInstance();
		XMLStreamReader r = f.createXMLStreamReader(inputStream);

		StateMachine sm = new StateMachine();

		List<City> buffer = new ArrayList<City>();
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
					case 2: // GNIS_ID
						break;
					case 3: // ANSICODE
						break;
					case 4: // FEATURE
						sm.city.setFeature(r.getElementText());
						break;
					case 5: // FEATURE2
						sm.city.setFeature2(r.getElementText());
						break;
					case 6: // NAME
						sm.city.setName(r.getElementText());
						break;
					case 7: // POP_2010
						sm.city.setPopulation(new BigDecimal(r.getElementText()));
						break;
					case 8: // COUNTY
						sm.city.setCounty(r.getElementText());
						break;
					case 9: // COUNTYFIPS
						break;
					case 10:// STATE
						sm.city.setState(r.getElementText());
						break;
					case 11:// STATE_FIPS
						break;
					case 12:// LATITUDE
						sm.city.setLatitude(new BigDecimal(r.getElementText()));
						break;
					case 13:// LONGITUDE
						sm.city.setLongitude(new BigDecimal(r.getElementText()));
						break;
					case 14:// PopPlLat
						break;
					case 15:// PopPlLong
						break;
					case 16:// ELEV_IN_M
						break;
					case 17:// ELEV_IN_FT
						break;
					default:
						break;
					}

					sm.nextField();
				}
				if ("X".equals(name)) {
					// BigDecimal xval = new BigDecimal(r.getElementText());
					// sm.city.setRefX(xval);
				}
				if ("Y".equals(name)) {
					// BigDecimal yval = new BigDecimal(r.getElementText());
					// sm.city.setRefY(yval);
				}
			}
			if (r.isEndElement()) {
				String name = r.getName().getLocalPart();
				if ("Record".equals(name)) {
					// Save current city
					buffer.add(sm.city);

					if (buffer.size() >= threshold) {
						EntityTransaction t = em.getTransaction();
						t.begin();
						for (City newr : buffer)
							em.persist(newr);
						t.commit();

						buffer.clear();
					}
				}
			}
			r.next();
		}

		r.close();

		// Save remaining data
		EntityTransaction t = em.getTransaction();
		t.begin();
		for (City newr : buffer)
			em.persist(newr);
		t.commit();

	}

	public static class StateMachine {

		int fieldIndex;

		City city;

		Waypoint waypoint;

		void reset() {
			city = new City();
			fieldIndex = 0;
		}

		void nextField() {
			fieldIndex++;
		}
	}

}
