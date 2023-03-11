package com.caam.mrs.api;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * 
 * @author Suhaimi AK
 *
 */
@Data
@Validated
@ConfigurationProperties(prefix = "com.caam.mrs.api.service.jasper")
public class ApplicationProperties {
	/**
	 * The base path where reports will be stored after compilation
	 */
	@NotNull
	private Resource storageLocation;
	/**
	 * The location of JasperReports source files
	 */
	@NotNull
	private String reportLocation;
	/**
	 * The jndi connection of JasperReports source files
	 */
	@NotNull
	private Resource reportJndi;
}