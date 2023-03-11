package com.caam.mrs.api.service.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import org.springframework.util.FileSystemUtils;

import com.caam.mrs.api.ApplicationProperties;
import com.caam.mrs.api.exception.StorageException;
import com.caam.mrs.api.exception.StorageFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileSystemStorageService implements ReportStorageService {
	private final Path rootLocation;
	
	private final ApplicationProperties properties;

	public FileSystemStorageService(ApplicationProperties properties) throws IOException {
		this.properties = properties;
		this.rootLocation = Paths.get(properties.getStorageLocation().getURL().getPath()); 
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void deleteAll() {
		try {
			FileSystemUtils.deleteRecursively(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not delete files and folders", e);
		}
	}

	@Override
	public boolean reportFileExists(String file) throws IOException {
		
		ClassPathResource cpr = new ClassPathResource(properties.getReportLocation() + "/" + file);
		if (cpr.exists())
			return true;
		else
			return false;
	}

	@Override
	public InputStream loadReportFile(String file) {
		try {
			//Report template in classpath/reports
			ClassPathResource cpr = new ClassPathResource(properties.getReportLocation() + "/" + file);
			return cpr.getInputStream();
			
		} catch (IOException e) {
			log.error("Error while trying to get file prefix", e);
			throw new StorageFileNotFoundException("Could not load file", e);
		}
	}

	@Override
	public String loadJndi() {
		return properties.getReportJndi().toString();
	}
	
	@Override
	public String getPathFile(String file) {
		ClassPathResource cpr = new ClassPathResource(properties.getReportLocation() + "/" + file);
		
		return cpr.getPath();
	}

}