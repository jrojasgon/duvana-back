package com.inopek.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.inopek.beans.SinkBean;
import com.inopek.enums.SinkDiameterEnum;
import com.inopek.enums.SinkPlumbOptionEnum;
import com.inopek.enums.SinkStatusEnum;
import com.inopek.enums.SinkTypeEnum;
import com.inopek.services.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {
	
	private static final String RESIZED_PREFIX = "-resized";
	private static final String IMAGE_EXTENSION = ".png";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelServiceImpl.class);

	@Override
	public SXSSFWorkbook createWorkbook(List<SinkBean> sinks) {

		SXSSFWorkbook workbook = new SXSSFWorkbook(10); // keep 10 rows in
														// memory, exceeding
														// rows will be flushed
														// to disk
		Sheet dataSheet = workbook.createSheet(sinks.get(0).getClient().getName());
		Sheet imagesSheet = workbook.createSheet("Fotos");

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(headerFont);

		createDataSheetHeader(headerStyle, dataSheet);
		mergeCells(dataSheet);
		createImageSheetHeader(headerStyle, imagesSheet);

		CreationHelper helper = workbook.getCreationHelper();
		Drawing<?> drawing = imagesSheet.createDrawingPatriarch();

		sinks.forEach(sink -> {
			int rownum = dataSheet.getLastRowNum() + 1;
			Row row = dataSheet.createRow(rownum);
			Row rowImage = imagesSheet.createRow(rownum);
			rowImage.createCell(0).setCellValue(sink.getReference() != null ? sink.getReference() : StringUtils.EMPTY);
			populateDataSheet(sink, row);

			int imageBeforeIdx = getImageIndex(sink.getImagePathBeforeClean(), workbook);
			int imageAfterIdx = getImageIndex(sink.getImagePathAfterClean(), workbook);
			
			if (imageBeforeIdx > -1) {
				createImage(helper, drawing, imageBeforeIdx, rownum, 1, rowImage);
			}

			if (imageAfterIdx > -1) {
				createImage(helper, drawing, imageAfterIdx, rownum, 2, rowImage);
			}
		});

		return workbook;
	}

	private int getImageIndex(String imagePath, SXSSFWorkbook workbook) {
		int index = -1;
		if (imagePath != null) {
			try {
				String resizedImagePath = FilenameUtils.removeExtension(imagePath) + RESIZED_PREFIX + IMAGE_EXTENSION;
				File file = new File(resizedImagePath);
				LOGGER.info("Read image in :" + resizedImagePath);
				if (file.exists()) {
					LOGGER.info("file exists");
					InputStream imageInputStream = new FileInputStream(file);
					index = workbook.addPicture(IOUtils.toByteArray(imageInputStream), Workbook.PICTURE_TYPE_PNG);
					LOGGER.info("index :" + index );
					imageInputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error("Error opening " + imagePath, e);
			}
		}
		return index;
	}

	private void createImage(CreationHelper helper, Drawing<?> drawing, int index, int rownum, int colnum, Row rowImage) {

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

	private void populateDataSheet(SinkBean sink, Row row) {
		row.createCell(0).setCellValue(sink.getReference());
		row.createCell(1).setCellValue(sink.getAddress() != null && sink.getAddress().getStreet() != null
				? sink.getAddress().getStreet() : StringUtils.EMPTY);
		row.createCell(2).setCellValue(sink.getSinkTypeId() != null
				? SinkTypeEnum.getSinkTypeEnumById(sink.getSinkTypeId()).getLabel() : StringUtils.EMPTY);
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
		row.createCell(7)
				.setCellValue(sink.getPipeLineDiameterId() != null
						? SinkDiameterEnum.getSinkDiameterEnumById(sink.getPipeLineDiameterId()).getLabel()
						: StringUtils.EMPTY);
		row.createCell(8).setCellValue(sink.getPlumbOptionId() != null
				? SinkPlumbOptionEnum.getSinkPlumbEnumById(sink.getPlumbOptionId()).getLabel() : StringUtils.EMPTY);
		row.createCell(9).setCellValue(
				sink.getPipeLineLength() != null ? String.valueOf(sink.getPipeLineLength()) : StringUtils.EMPTY);
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

	private void mergeCells(Sheet dataSheet) {
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
		dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
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

}
