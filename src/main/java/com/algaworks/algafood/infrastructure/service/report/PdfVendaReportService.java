package com.algaworks.algafood.infrastructure.service.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PdfVendaReportService implements VendaReportService {

	@Autowired
	private VendaQueryService vendaQueryService;
	
	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(
					"/relatorios/vendas-diarias.jasper");
			
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			List<VendaDiaria> vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
		
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		}
	}

}
