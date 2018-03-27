package com.inopek.services;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.inopek.beans.SinkBean;

public interface ExcelService {

	/**
	 * 
	 * @param sinks
	 * @return
	 */
	SXSSFWorkbook createWorkbook(List<SinkBean> sinks);
}
