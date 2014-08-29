package edu.clarkson.cs.mbg.pingmeasure;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeService;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.CityDao;
import edu.clarkson.cs.mbg.map.model.City;

public class ProbeMapping {

	static int[] CITY = { 9233, 17417, 18395, 14589, 3562, 987, 4987 };

	static Map<Integer, Integer> map = new HashMap<Integer, Integer>();

	static {
		map.put(10518, 0);
		map.put(801, 0);
		map.put(10301, 0);
		map.put(12680, 0);
		map.put(13202, 0);
		map.put(12578, 0);
		map.put(14657, 0);
		map.put(14606, 0);
		map.put(15038, 0);
		map.put(14754, 1);
		map.put(13419, 1);
		map.put(10334, 1);
		map.put(14729, 1);
		map.put(16470, 1);
		map.put(17468, 1);
		map.put(12609, 1);
		map.put(14233, 1);
		map.put(12853, 1);
		map.put(1194, 1);
		map.put(10790, 1);
		map.put(17382, 1);
		map.put(3925, 2);
		map.put(3932, 2);
		map.put(12070, 3);
		map.put(13833, 3);
		map.put(12144, 3);
		map.put(4894, 3);
		map.put(15932, 3);
		map.put(10099, 3);
		map.put(10697, 3);
		map.put(13717, 3);
		map.put(3660, 3);
		map.put(13834, 3);
		map.put(3644, 3);
		map.put(13639, 3);
		map.put(10338, 4);
		map.put(10581, 4);
		map.put(10517, 4);
		map.put(11025, 4);
		map.put(10579, 4);
		map.put(13426, 4);
		map.put(10540, 5);
		map.put(11837, 5);
		map.put(16909, 5);
		map.put(2858, 5);
		map.put(17322, 5);
		map.put(11857, 5);
		map.put(14796, 5);
		map.put(10478, 5);
		map.put(10542, 5);
		map.put(12452, 5);
		map.put(16053, 5);
		map.put(12688, 5);
		map.put(10544, 5);
		map.put(12650, 5);
		map.put(12330, 6);
		map.put(12318, 6);
		map.put(3740, 6);
		map.put(10578, 6);
	}

	public static List<Integer> candidate(int source) {
		return null;
	}

	public static String fieldName(int source) {
		return MessageFormat.format("probe0{0}",
				String.valueOf(map.get(source) + 1));
	}

	public static void main(String[] args) {
		new MBGContextSet().apply();

		ProbeService probeService = BeanContext.get().get("probeService");
		CityDao cityDao = BeanContext.get().get("cityDao");

		BigDecimal range = new BigDecimal("0.5");

		int counter = 0;
		for (int id : CITY) {

			City city = cityDao.find(id);
			List<Probe> probesInCity = probeService.findByRange(city
					.getLatitude().subtract(range),
					city.getLatitude().add(range), city.getLongitude()
							.subtract(range), city.getLongitude().add(range));

			for (Probe p : probesInCity) {
				System.out.println(MessageFormat.format("map.put({0},{1});",
						String.valueOf(p.getId()), String.valueOf(counter)));
			}
			counter++;
		}
	}
}
