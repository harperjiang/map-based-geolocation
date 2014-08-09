package edu.clarkson.cs.mbg.tool.univdata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.clarkson.cs.clientlib.lang.proc.OutputHandler;
import edu.clarkson.cs.clientlib.lang.proc.ProcessRunner;

public class QueryWebsiteIP {

	static final Pattern alias = Pattern
			.compile("([A-Za-z0-9\\.]+) is an alias for ([A-Za-z0-9\\.]+)");

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("data/all_university_website.tsv")));

		final Map<String, Integer> aliasCounter = new HashMap<String, Integer>();
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\t");

			ProcessRunner runner = new ProcessRunner("host", parts[1]);
			runner.setHandler(new OutputHandler() {
				@Override
				public void output(String input) {
					Matcher matcher = alias.matcher(input);
					if (matcher.matches()) {
						String alias = matcher.group(2);
						if (aliasCounter.containsKey(alias)) {
							aliasCounter.put(alias, aliasCounter.get(alias) + 1);
						} else {
							aliasCounter.put(alias, 1);
						}
					}
				}
			});
			runner.runAndWait();
		}

		br.close();

		for (Entry<String, Integer> aliasEntry : aliasCounter.entrySet()) {
			if (aliasEntry.getValue() > 2) {
				System.out.println(aliasEntry.getKey());
			}
		}

	}
}
