package vn.edu.nuce.datn.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class ReportUtils {

	public static byte[] generateReportPDF(List<?> dataSource, Map parameters, JasperReport jasperReport)
			throws JRException, NamingException, SQLException, IOException {
		byte[] bytes = null;
		if(dataSource.size() != 0){
			JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);
			bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, beanDataSource);
		}

		return bytes;
	}
	
	public static byte[] generateReportPDF(Map parameters, JasperReport jasperReport)
			throws JRException, NamingException, SQLException, IOException {
		byte[] bytes = null;
		bytes = JasperRunManager.runReportToPdf(jasperReport, parameters);
		return bytes;
	}

	public static byte[] generateReportExcel(Map parameters, JasperReport jasperReport,
			List<?> dataSource) throws JRException, NamingException, SQLException, IOException {
		byte[] excel = null;
		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanDataSource);
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		excel = xlsReport.toByteArray();

		return excel;
	}

	public static JasperReport getCompiledFile(String jasperPath, String jrxmlPath) throws JRException {

		File reportFile = new File(jasperPath);
		// If compiled file is not found, then compile XML template
		if (!reportFile.exists()) {
			JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
		}
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(reportFile.getPath()));
		} catch (FileNotFoundException e) {
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
		
		return jasperReport;
	}

	
	
}
