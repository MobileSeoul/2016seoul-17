package com.seoul.culture.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CSVFile {
	InputStream inputStream;

	public CSVFile(InputStream inputStream){
		this.inputStream = inputStream;
	}

	public ArrayList<Center> read() throws UnsupportedEncodingException{
		ArrayList<Center> resultList = new ArrayList<Center>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "euc-kr"));
		try {
			String csvLine;
			while ((csvLine = reader.readLine()) != null) {
				String[] row = csvLine.split(",#,");
				for (int i = 0; i < row.length; i++) {
					row[i] = row[i].replace("\"", "");
				}
				resultList.add(new Center(row[0], row[1], row[2], row[3], row[4], row[5]));
			}
		}
		catch (IOException ex) {
			throw new RuntimeException("Error in reading CSV file: "+ex);
		}
		finally {
			try {
				inputStream.close();
			}
			catch (IOException e) {
				throw new RuntimeException("Error while closing input stream: "+e);
			}
		}
		return resultList;
	}
}