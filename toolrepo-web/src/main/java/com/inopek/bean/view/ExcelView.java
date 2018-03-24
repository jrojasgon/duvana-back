package com.inopek.bean.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.inopek.beans.SinkBean;
import com.inopek.enums.SinkDiameterEnum;
import com.inopek.enums.SinkPlumbOptionEnum;
import com.inopek.enums.SinkStatusEnum;
import com.inopek.enums.SinkTypeEnum;


public class ExcelView extends AbstractXlsxView {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"duvana-sumideros.xlsx\"");
		@SuppressWarnings("unchecked")
		List<SinkBean> sinks = (List<SinkBean>) model.get("sinks");
		
		// Set Header Font
		HSSFFont headerFont = (HSSFFont) workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(headerFont);
		
		Sheet dataSheet = workbook.createSheet(sinks.get(0).getClient().getName());
		Sheet imagesSheet = workbook.createSheet("Fotos");
		
		createDataSheetHeader(headerStyle, dataSheet);
		
		mergeCells(dataSheet);
		
		createImageSheetHeader(headerStyle, imagesSheet);
		
		for (int colNum = 0; colNum < 11; colNum++) {
			dataSheet.autoSizeColumn(colNum);
		}
		
		CreationHelper helper = workbook.getCreationHelper();
		
		Drawing drawing = imagesSheet.createDrawingPatriarch();
		
		int widthUnits = 20 * 256;
		imagesSheet.setColumnWidth(1, widthUnits);
		imagesSheet.setColumnWidth(2, widthUnits);
		
		int rowCount = 2;
		
		for (SinkBean sink : sinks) {
			
			int imageBeforeIdx = -1;
			int imageAfterIdx = -1;
			
			// TODO image for report
//			if (sink.getImageBefore() != null) {
//				byte[] imageBytes = sink.getImageBefore().getBytes(1, Long.valueOf(sink.getImageBefore().length()).intValue());
//				imageBeforeIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
//			}
//			
//			if (sink.getImageAfter() != null) {
//				byte[] imageBytes = sink.getImageAfter().getBytes(1, Long.valueOf(sink.getImageAfter().length()).intValue());
//				imageAfterIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
//			}
			
			int rownum = rowCount++;
			Row row = dataSheet.createRow(rownum);
			Row rowImage = imagesSheet.createRow(rownum);
			rowImage.createCell(0).setCellValue(sink.getReference() != null ? sink.getReference() : StringUtils.EMPTY);
			populateDataSheet(sink, row);
			
			if (imageBeforeIdx > 0) {
				createImage(helper, drawing, imageBeforeIdx, rownum, 1, rowImage);
			}
			
			if (imageAfterIdx > 0) {
				createImage(helper, drawing, imageAfterIdx, rownum, 2, rowImage);
			}
		}
		
	}

	private void populateDataSheet(SinkBean sink, Row row) {
		row.createCell(0).setCellValue(sink.getReference() != null ? sink.getReference() : StringUtils.EMPTY);
		row.createCell(1).setCellValue(
				sink.getAddress() != null && sink.getAddress().getStreet() != null ? sink.getAddress().getStreet() : StringUtils.EMPTY);
		row.createCell(2).setCellValue(
				sink.getSinkTypeId() != null ? SinkTypeEnum.getSinkTypeEnumById(sink.getSinkTypeId()).getLabel() : StringUtils.EMPTY);
		row.createCell(3).setCellValue(sink.getLength() != null ? String.valueOf(sink.getLength()) : StringUtils.EMPTY);
		if (sink.getSinkStatusId() != null) {
			SinkStatusEnum statusEnum = SinkStatusEnum.getSinkStatusEnumById(sink.getSinkStatusId());
			if (SinkStatusEnum.GOOD.equals(statusEnum)) {
				row.createCell(4).setCellValue("X");
			} else if (SinkStatusEnum.MODERATE.equals(statusEnum)) {
				row.createCell(5).setCellValue("X");
			} else if (SinkStatusEnum.BAD.equals(statusEnum)) {
				row.createCell(6).setCellValue("X");
			}
		}
		row.createCell(7).setCellValue(
				sink.getPipeLineDiameterId() != null ? SinkDiameterEnum.getSinkDiameterEnumById(sink.getPipeLineDiameterId()).getLabel()
						: StringUtils.EMPTY);
		row.createCell(8).setCellValue(
				sink.getPlumbOptionId() != null ? SinkPlumbOptionEnum.getSinkPlumbEnumById(sink.getPlumbOptionId()).getLabel() : StringUtils.EMPTY);
		row.createCell(9).setCellValue(sink.getPipeLineLength() != null ? String.valueOf(sink.getPipeLineLength()) : StringUtils.EMPTY);
		row.createCell(10).setCellValue(sink.getObservations() != null ? sink.getObservations() : StringUtils.EMPTY);
	}

	private void createImageSheetHeader(CellStyle headerStyle, Sheet imagesSheet) {
		Row headerImages = imagesSheet.createRow(0);
		Cell cellImageReference = headerImages.createCell(0);
		cellImageReference.setCellStyle(headerStyle);
		cellImageReference.setCellValue("REFERENCIA");
		
		Cell cellImageBefore = headerImages.createCell(1);
		cellImageBefore.setCellStyle(headerStyle);
		cellImageBefore.setCellValue("ANTES");
		
		Cell cellImageAfter = headerImages.createCell(2);
		cellImageAfter.setCellStyle(headerStyle);
		cellImageAfter.setCellValue("DESPUES");
	}

	private void createDataSheetHeader(CellStyle headerStyle, Sheet dataSheet) {
		Row mainHeader = dataSheet.createRow(0);
		Cell cellReference = mainHeader.createCell(0);
		cellReference.setCellStyle(headerStyle);
		cellReference.setCellValue("REFERENCIA");
		Cell cellAddress = mainHeader.createCell(1);
		cellAddress.setCellStyle(headerStyle);
		cellAddress.setCellValue("DIRECCION");
		Cell cellType = mainHeader.createCell(2);
		cellType.setCellStyle(headerStyle);
		cellType.setCellValue("TIPO DE SUMIDERO");
		Cell cellLenght = mainHeader.createCell(3);
		cellLenght.setCellStyle(headerStyle);
		cellLenght.setCellValue("LONGITUD (ML)");
		Cell cellState = mainHeader.createCell(4);
		cellState.setCellStyle(headerStyle);
		cellState.setCellValue("ESTADO");
		cellReference.setCellStyle(headerStyle);
		Cell cellPlumb = mainHeader.createCell(7);
		cellPlumb.setCellStyle(headerStyle);
		cellPlumb.setCellValue("TUBERIA DE DESCARGA");
		Cell cellObs = mainHeader.createCell(9);
		cellObs.setCellStyle(headerStyle);
		cellObs.setCellValue("OBSERVACIONES");
		
		Row header = dataSheet.createRow(1);
		header.createCell(4).setCellValue("BUENO");
		header.createCell(5).setCellValue("REGULAR");
		header.createCell(6).setCellValue("MALO");
		header.createCell(7).setCellValue("DIAMETRO");
		header.createCell(8).setCellValue("LONGITUD");
	}

	private void mergeCells(Sheet dataSheet) {
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
	}
	
	private void createImage(CreationHelper helper, Drawing drawing, int index, int rownum, int colnum, Row rowImage) {
		
		ClientAnchor anchor = createAnchor(helper, rownum, colnum);
		
		Cell cell = rowImage.createCell(1);
		short heightUnits = 60 * 20;
		cell.getRow().setHeight(heightUnits);
		drawing.createPicture(anchor, index);
	}
	
	private ClientAnchor createAnchor(CreationHelper helper, int rownum, int colnum) {
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(colnum);
		anchor.setRow1(rownum);
		anchor.setCol2(colnum + 1);
		anchor.setRow2(rownum + 1);
		return anchor;
	}
	
}
