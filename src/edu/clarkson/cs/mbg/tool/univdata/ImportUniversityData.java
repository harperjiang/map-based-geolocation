package edu.clarkson.cs.mbg.tool.univdata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityTransaction;

import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.JpaUniversityDao;
import edu.clarkson.cs.mbg.map.model.University;

public class ImportUniversityData {

	static final Pattern pattern = Pattern
			.compile("([^\\(]+)\\(([A-Z/\\s]+)\\)([^\\)]*)");

	public static void main(String[] args) throws Exception {
		new MBGContextSet().apply();

		JpaUniversityDao univDao = BeanContext.get().get("universityDao");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/all_university_ip_loc.tsv")));
		String line = null;

		boolean error = false;
		List<University> univs = new ArrayList<University>();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\t");
			Matcher matcher = pattern.matcher(parts[0]);
			University univ = new University();
			if (matcher.matches()) {
				String univName = MessageFormat.format("{0} {1}", matcher
						.group(1).trim(), matcher.group(3).trim());
				univ.setName(univName);

				String state = matcher.group(2).trim();
				if (state.length() == 2)
					univ.setState(state);
			} else {
				univ.setName(parts[0]);
			}
			univ.setWebsite(parts[1]);
			univ.setIpAddress(parts[2]);
			univ.setLatitude(new BigDecimal(parts[3]));
			univ.setLongitude(new BigDecimal(parts[4]));

			univs.add(univ);
		}

		br.close();
		if (error)
			System.exit(1);

		EntityTransaction tran = univDao.getEntityManager().getTransaction();
		tran.begin();
		for (University univ : univs)
			univDao.getEntityManager().persist(univ);
		tran.commit();
	}
}
