package edu.clarkson.cs.mbg.tool.univdata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import edu.clarkson.cs.clientlib.gsearch.GSearchAccess;
import edu.clarkson.cs.clientlib.gsearch.GSearchContextSet;
import edu.clarkson.cs.clientlib.gsearch.api.GSearchResponse;
import edu.clarkson.cs.clientlib.gsearch.model.SearchResult;
import edu.clarkson.cs.common.BeanContext;

public class QueryUniversityWebsite {

	public static void main(String[] args) throws Exception {
		new GSearchContextSet().apply();
		GSearchAccess access = BeanContext.get().get("gsearchAccess");

		// Get existed data
		Set<String> existed = new HashSet<String>();
		BufferedReader ebr = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/all_university_website.tsv")));
		String eline = null;
		while ((eline = ebr.readLine()) != null) {
			existed.add(eline.split("\t")[0]);
		}
		ebr.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/all_university.tsv")));
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"data/all_university_website.tsv", true));

		// Random rand = new Random(System.currentTimeMillis());

		String line = null;
		String prefix = null;
		while ((line = br.readLine()) != null) {
			if (!line.startsWith("        ")) {
				prefix = line;
			} else {
				line = prefix + " " + line.trim();
			}
			line = line.trim();
			if (existed.contains(line))
				continue;
			// Thread.sleep(5000 + rand.nextInt(1000));
			GSearchResponse resp = access.search(line).execute();
			if (resp.getError() == null) {
				SearchResult res = resp.getResult().getResults().get(0);
				pw.println(MessageFormat.format("{0}\t{1}", line,
						res.getVisibleUrl()));
			} else {
				System.err.println(MessageFormat.format("Error:{0}-{1}", resp
						.getError().getCode(), resp.getError().getMessage()));
				break;
			}

		}

		br.close();
		pw.close();
	}
}
