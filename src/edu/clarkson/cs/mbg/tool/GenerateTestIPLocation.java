package edu.clarkson.cs.mbg.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoAccess;
import edu.clarkson.cs.clientlib.ipinfo.IPInfoContextSet;
import edu.clarkson.cs.clientlib.ipinfo.model.IPInfo;
import edu.clarkson.cs.clientlib.lang.BeanContext;

public class GenerateTestIPLocation {

	public static void main(String[] args) throws Exception {
		new IPInfoContextSet().apply();

		IPInfoAccess ipinfoAccess = BeanContext.get().get("ipinfoAccess");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/university_ip.tsv")));
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"data/university_ip_loc.tsv"));

		String line = null;
		boolean header = true;
		while ((line = br.readLine()) != null) {
			if (header) {
				header = false;
				pw.println(line);
				continue;
			}
			String[] parts = line.split("\t");

			IPInfo info = ipinfoAccess.getInfo(parts[2]);
			pw.println(MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}",
					parts[0], parts[1], parts[2], info.getLatitude(),
					info.getLongitude()));

		}
		br.close();
		pw.close();
	}
}
