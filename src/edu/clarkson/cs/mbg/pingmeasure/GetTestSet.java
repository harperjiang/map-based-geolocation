package edu.clarkson.cs.mbg.pingmeasure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GetTestSet {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("train/ping_result")));
		PrintWriter pw = new PrintWriter(new FileOutputStream(
				"train/local_testset"));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] result = line.split(",");
			Double latitude = Double.valueOf(result[1]);
			Double longitude = Double.valueOf(result[2]);

			if (latitude >= 37.94 && latitude <= 39.35 && longitude >= -77.82
					&& longitude <= -76.51) {
				pw.println(line);
			}
		}

		br.close();
		pw.close();
	}

}
