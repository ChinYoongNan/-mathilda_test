package com.caam.mrs.api.service.jasper;

import java.io.IOException;
import java.io.InputStream;

public interface ReportStorageService {

	void init();

	void deleteAll();

	boolean reportFileExists(String file) throws IOException;

	InputStream loadReportFile(String file);

	String loadJndi();
	
	String getPathFile(String file);
}
