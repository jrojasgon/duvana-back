package com.inopek.rest.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inopek.bean.view.SinkBeanView;
import com.inopek.beans.ClientBean;
import com.inopek.beans.SinkBean;
import com.inopek.beans.UserBean;
import com.inopek.enums.ProfileEnum;
import com.inopek.services.ClientService;
import com.inopek.services.SinkService;
import com.inopek.services.UserService;

@RestController
public class SinkController {

	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final Logger LOGGER = LoggerFactory.getLogger(SinkController.class);

	private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Autowired
	private SinkService sinkService;
	@Autowired
	private UserService userService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(value = "/save/{userImi}/{checkReferenceExits}/{updateAll}/{stepBefore}")
	@ResponseBody
	public Long save(@PathVariable("userImi") String userImi,
			@PathVariable("checkReferenceExits") String checkReferenceExits,
			@PathVariable("updateAll") String updateAll, @PathVariable("stepBefore") String stepBefore,
			@RequestBody SinkBean sink) {
		// check if reference exists
		if (Boolean.valueOf(checkReferenceExits)
				&& sinkService.checkReferenceExists(sink, Boolean.valueOf(stepBefore))) {
			return 0L;
		}
		UserBean user = getSavedUser(userImi);
		SinkBean createdSink;
		if (Boolean.valueOf(updateAll)) {
			createdSink = sinkService.update(sink, user);
		} else {
			createdSink = sinkService.prepareAndSave(sink, user);
		}
		return createdSink != null ? createdSink.getId() : null;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = { "/search/{startDate}/{endDate}/{clientName}/{reference}",
			"/search/{startDate}/{endDate}/{clientName}/" })
	@ResponseBody
	public SinkBean[] search(@PathVariable("startDate") @DateTimeFormat(pattern = DD_MM_YYYY) Date startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = DD_MM_YYYY) Date endDate,
			@PathVariable("clientName") String clientName,
			@PathVariable(name = "reference", required = false) String reference) {
		// check if reference exists
		if (startDate == null) {
			return null;
		}

		if (endDate == null) {
			endDate = new DateTime().plusDays(1).withTimeAtStartOfDay().toDate();
		}
		ArrayList<SinkBean> results = sinkService.findAllSinksByDateAnClientAndReference(startDate, endDate, clientName,
				reference);
		if (CollectionUtils.isEmpty(results)) {
			return null;
		}
		return results.toArray(new SinkBean[results.size()]);
	}

	@RequestMapping(value = "/searchPairReferenceClient/{clientName}/{reference}/{stepBefore}")
	@ResponseBody
	public Boolean searchPairReferenceClient(@PathVariable("clientName") String clientName,
			@PathVariable("reference") String reference, @PathVariable("stepBefore") String stepBefore) {
		// check if pair reference-client exists
		return sinkService.findByReferenceAndClientAndStep(reference, clientName, Boolean.valueOf(stepBefore));
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Boolean delete(@RequestBody SinkBean sink) {
		if (sink == null) {
			return false;
		}
		return sinkService.deleteSink(sink);
	}

	@RequestMapping(value = "/saveFromFile/{userImi}/{profile}", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Boolean> saveFromFile(@PathVariable("userImi") String userImi,
			@PathVariable("profile") String profile, @RequestBody Set<SinkBean> sinks) {
		UserBean user = getSavedUser(userImi);
		if (user == null) {
			LOGGER.warn("Imi is not in database : " + userImi);
		}
		return sinkService.prepareAndSave(sinks, user, ProfileEnum.getProfileEnum(profile));
	}

	private UserBean getSavedUser(String userImi) {
		return userService.findByImeiNumber(userImi);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/clients")
	@ResponseBody
	public List<String> getClients() {
		List<String> names = new ArrayList<>();
		ArrayList<ClientBean> clients = clientService.findAll();
		clients.forEach(client -> names.add(client.getName()));
		return names;
	}

	@RequestMapping(value = "/address")
	@ResponseBody
	public ArrayList<ClientBean> getAddress() {
		return clientService.findAll();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/search/test")
	@ResponseBody
	public List<SinkBeanView> search(@RequestParam(value = "reference", required = false) String referenceParam,
			@RequestParam(value = "clientName", required = false) String clientNameParam,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = YYYY_MM_DD) Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = YYYY_MM_DD) Date to) {

		DateTime today = new DateTime().withTimeAtStartOfDay();
		DateTime startDate = from == null ? today : new DateTime(from).withTimeAtStartOfDay();
		DateTime endDate = to == null ? today : new DateTime(to).withTimeAtStartOfDay();

		String clientName = Optional.ofNullable(clientNameParam).orElse(StringUtils.EMPTY);
		String reference = Optional.ofNullable(referenceParam).orElse(StringUtils.EMPTY);

		ArrayList<SinkBean> sinks = sinkService.findAllSinksByDateAnClientAndReference(startDate.toDate(),
				endDate.toDate(), clientName, reference);
		List<SinkBeanView> sinkViewBeans = new ArrayList<>();
		sinks.forEach(sinkBean -> sinkViewBeans.add(mapper.map(sinkBean, SinkBeanView.class)));
		return sinkViewBeans;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/duvana/downloadExcel", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void downloadExcel(@RequestParam(value = "ids") List<String> ids, HttpServletResponse response) {

		DateTime today = new DateTime().withTimeAtStartOfDay();

		DateTime startDate = today;
		DateTime endDate = today;

		SinkBean sinkBeanView = new SinkBean();
		sinkBeanView.setReference("kaka");

		SinkBean sinkBeanView2 = new SinkBean();
		sinkBeanView2.setReference("kaka2");

		// return a view which will be resolved by an excel view resolver
		Map<String, Object> map = new HashMap<>();
		map.put("sinks", Arrays.asList(sinkBeanView, sinkBeanView2));
		File initialFile = new File("C:\\Users\\rojas\\Desktop\\Comptes-Serveurs.xlsx");
		InputStream is;
		try {
			is = new FileInputStream(initialFile);
			response.setContentType(XLSX_CONTENT_TYPE);
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"Comptes-Serveurs.xlsx\"");
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
