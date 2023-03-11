package com.caam.mrs.api.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.caam.mrs.api.exception.EntityNotFoundException;
import com.caam.mrs.api.payload.ApiResponse;
import com.caam.mrs.api.service.jasper.ReportStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


@Slf4j
@Component
@RequiredArgsConstructor
public class JasperReportService implements ReportService {
	private final ReportStorageService reportStorageService;
	
	@Autowired
	DataSource dataSource;
	
	@Override
	public byte[] generatePDFReport(String inputFileName, Map<String, Object> params) {
		try {
			return generatePDFReport(inputFileName, params, dataSource.getConnection());
		}
		catch (SQLException sqlex) {
			sqlex.printStackTrace();
			log.error("Encountered error during report generation", sqlex);
			return null;
		}
	}
	
	@Override
	public byte[] generatePDFReport(String inputFileName, Map<String, Object> params, Connection dataSource) {
		
		byte[] bytes = null;
		JasperReport jasperReport = null;
		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
			// Check if a compiled report exists
			if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
				jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					dataSource);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		}
		catch (JRException | IOException e) {
			e.printStackTrace();
			log.error("Encountered error when loading jasper file", e);
		}
		finally {
			try {
				dataSource.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Fail to close the dataSource report connection", e);
			}
		}

		return bytes;
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public byte[] generatePDFReportCollection(String inputFileName, Map<String, Object> params, List dataList) {
		byte[] bytes = null;
		
		JasperReport jasperReport = null;
		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
			InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
			log.info("{} loaded. Compiling report", jrxml);
			jasperReport = JasperCompileManager.compileReport(jrxml);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					dataSource);
			bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		}
		catch (JRException | IOException e) {
			e.printStackTrace();
			log.error("Encountered error when loading jasper file", e);
		}
				
		return bytes;
	}
	
	public ResponseEntity<InputStreamResource> generateReportPDF(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv) {
	    FileInputStream st = null;
	    Connection cc = null;
	    JasperReport jasperReport = null;
	    
	    try {
	        cc = dataSource.getConnection();
	        
	        if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
	        	log.info(">>jasper report found");
	        	jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
	        boolean noRecordFound = false;
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, cc);
	        try {
		        if(((net.sf.jasperreports.engine.fill.JRTemplatePrintText)jasperPrint.getPages().get(0).getElements().get(0)).getFullText().equals("Sorry!. No record found with your request.")){
		        	noRecordFound = true;
		        }	        	
	        }catch(Exception ex) {
	        	
	        }
	        if(noRecordFound) {
	        	return new ResponseEntity(new ApiResponse(true, "Sorry!. No record found with your request."),
		                HttpStatus.BAD_REQUEST);
	        }
	        JRPdfExporter exporter = new JRPdfExporter();
	        SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(c);
	        exporter.exportReport();
	 
	        st = new FileInputStream(name);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
	        responseHeaders.setContentDispositionFormData("attachment", name);
	        responseHeaders.setContentLength(st.available());
	        return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
//	        fv.cleanup();
	        sfv.cleanup();
	        if (cc != null)
	            try {
	                cc.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	    }
	    return null;
	}
	public ResponseEntity<InputStreamResource> generateReportCSV(String name, String sheetName, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv) {
	    FileInputStream st = null;
	    Connection cc = null;
	    JasperReport jasperReport = null;
	    
	    try {
	        cc = dataSource.getConnection();
	        
	        if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
	        	log.info(">>jasper report found");
	        	jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
	        boolean noRecordFound = false;
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, cc);
	        try {
		        if(((net.sf.jasperreports.engine.fill.JRTemplatePrintText)jasperPrint.getPages().get(0).getElements().get(0)).getFullText().equals("Sorry!. No record found with your request.")){
		        	noRecordFound = true;
		        }	        	
	        }catch(Exception ex) {
	        	
	        }
	        if(noRecordFound) {
	        	return new ResponseEntity(new ApiResponse(true, "Sorry!. No record found with your request."),
		                HttpStatus.BAD_REQUEST);
	        }

	        JRCsvExporter exporter = new JRCsvExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput((new SimpleWriterExporterOutput(new File(name))));
	        exporter.exportReport();
	        
	        st = new FileInputStream(name);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.parseMediaType("text/csv"));
	        responseHeaders.setContentDispositionFormData("attachment", name);
	        responseHeaders.setContentLength(st.available());
	        return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
//	        fv.cleanup();
	        sfv.cleanup();
	        if (cc != null)
	            try {
	                cc.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	    }
	    return null;
	}
	
	
	public ResponseEntity<InputStreamResource> generateReportXLS(String name, String sheetName, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv) {
	    FileInputStream st = null;
	    Connection cc = null;
	    JasperReport jasperReport = null;
	    
	    try {
	        cc = dataSource.getConnection();
	        
	        if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
	        	log.info(">>jasper report found");
	        	jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
	        
	        boolean noRecordFound = false;
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, cc);
	        try {
		        if(((net.sf.jasperreports.engine.fill.JRTemplatePrintText)jasperPrint.getPages().get(0).getElements().get(0)).getFullText().equals("Sorry!. No record found with your request.")){
		        	noRecordFound = true;
		        }	        	
	        }catch(Exception ex) {
	        	
	        }
	        if(noRecordFound) {
	        	return new ResponseEntity(new ApiResponse(true, "Sorry!. No record found with your request."),
		                HttpStatus.BAD_REQUEST);
	        }
	        
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(name));
	        SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
	        reportConfig.setSheetNames(new String[] { "" + sheetName });
	        reportConfig.setWhitePageBackground(true);
	        reportConfig.setDetectCellType(true);
	        reportConfig.setFontSizeFixEnabled(true);
	        exporter.setConfiguration(reportConfig);
	        exporter.exportReport();
	        
	        st = new FileInputStream(name);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
	        responseHeaders.setContentDispositionFormData("attachment", name);
	        responseHeaders.setContentLength(st.available());
	        return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
//	        fv.cleanup();
	        sfv.cleanup();
	        if (cc != null)
	            try {
	                cc.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	    }
	    return null;
	}
	
	public ResponseEntity<InputStreamResource> generateReportPDFNoDatasource(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv) {                                           
        FileInputStream st = null;
	    JasperReport jasperReport = null;
	    
	    try {
	        
	        if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
	        	log.info(">>jasper report found");
	        	jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
	        
	        JasperPrint p = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
	        JRPdfExporter exporter = new JRPdfExporter();
	        SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
	        exporter.setExporterInput(new SimpleExporterInput(p));
	        exporter.setExporterOutput(c);
	        exporter.exportReport();
	 
	        st = new FileInputStream(name);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
	        responseHeaders.setContentDispositionFormData("attachment", name);
	        responseHeaders.setContentLength(st.available());
	        return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
//	        fv.cleanup();
	        sfv.cleanup();
	    }
	    return null;
    }
	
	
	public ResponseEntity<InputStreamResource> generateReportPDFNoData(String name, String inputFileName, Map<String, Object> params, JRSwapFileVirtualizer sfv) {
	    FileInputStream st = null;
	    JasperReport jasperReport = null;
	    
	    try {
	        
	        if (reportStorageService.reportFileExists(inputFileName + ".jasper")) {
	        	log.info(">>jasper report found");
	        	jasperReport = (JasperReport) JRLoader
						.loadObject(reportStorageService.loadReportFile(inputFileName + ".jasper"));
			}
			// Compile report from source and save
			else {
				InputStream jrxml = reportStorageService.loadReportFile(inputFileName + ".jrxml");
				log.info("{} loaded. Compiling report", jrxml);
				jasperReport = JasperCompileManager.compileReport(jrxml);

			}
	        
	        JasperPrint p = JasperFillManager.fillReport(jasperReport, params);
	        JRPdfExporter exporter = new JRPdfExporter();
	        SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
	        exporter.setExporterInput(new SimpleExporterInput(p));
	        exporter.setExporterOutput(c);
	        exporter.exportReport();
	 
	        st = new FileInputStream(name);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
	        responseHeaders.setContentDispositionFormData("attachment", name);
	        responseHeaders.setContentLength(st.available());
	        return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
//	        fv.cleanup();
	        sfv.cleanup();
	    }
	    return null;
	}
	
	public String getFilePath(String file) {
		return reportStorageService.getPathFile(file);
	}
}
