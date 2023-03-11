package com.caam.mrs.api.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;

public interface ReportService {
	byte[] generatePDFReport(String inputFileName, Map<String, Object> params);
	
	byte[] generatePDFReport(String inputFileName, Map<String, Object> params, Connection dataSource);
	
	@SuppressWarnings("rawtypes")
	public byte[] generatePDFReportCollection(String inputFileName, Map<String, Object> params, List dataList);
	
	public ResponseEntity<InputStreamResource> generateReportPDF(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv);
	
	public ResponseEntity<InputStreamResource> generateReportCSV(String name, String sheetName, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv);
	
	public ResponseEntity<InputStreamResource> generateReportXLS(String name, String sheetName, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv);

	public ResponseEntity<InputStreamResource> generateReportPDFNoData(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv);

	public ResponseEntity<InputStreamResource> generateReportPDFNoDatasource(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv);
	
	Object getFilePath(String file);
	
}
