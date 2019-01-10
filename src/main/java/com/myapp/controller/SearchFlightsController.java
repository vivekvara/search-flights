package com.myapp.controller;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingDouble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.vo.ProviderVO;

@RestController
@RequestMapping("/searchFlights")
public class SearchFlightsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchFlightsController.class);
	private static final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("M/dd/yyyy H:mm:ss");

	@GetMapping("{origin}/{destination}")
	public String searchFlights(@PathVariable String origin, @PathVariable String destination) {
		LOGGER.info("seachFlights.start {}, {}", origin, destination);
		// curl http://localhost:8088/searchFlights/MIA/ORD
		String results = fetchFlights(origin, destination);
		if (null == results || results.isEmpty()) {
			results = "No flights Found for " + origin + " --> " + destination;
		}

		LOGGER.info("seachFlights.end {}", results);
		return results;

	}
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@GetMapping("/checkdate/{date}")
	public String checkDateBinder(@PathVariable Date date) {
		System.out.println("***********************");
		System.out.println(date);
		System.out.println("***********************");
		return "sucess";
	}

	private String fetchFlights(String origin, String destination) {
		File fileName = null;
		try {
			fileName = ResourceUtils.getFile("classpath:provider1.csv");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String results = null;
		try (Stream<String> stream = Files.lines(fileName.toPath())) {
			results = stream.skip(1).map(splitter).filter(
					p -> origin.equalsIgnoreCase(p.getOrigin()) && destination.equalsIgnoreCase(p.getDestination()))
					.distinct()
					.sorted(comparingDouble(ProviderVO::getPrice)
							.thenComparing(comparing(ProviderVO::getDepartureTime)))
					.map(formatter).collect(Collectors.joining("\n"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		LOGGER.info("Final String: {}", results);
		return results;
	}

	Function<String, ProviderVO> splitter = new Function<String, ProviderVO>() {
		public ProviderVO apply(String s) {
			String[] arg = s.split(",");
			return new ProviderVO(arg[0], LocalDateTime.parse(arg[1], DATETIMEFORMATTER),
					arg[2], LocalDateTime.parse(arg[3], DATETIMEFORMATTER),
					Double.parseDouble(arg[4].substring(1)));
		}
	};

	Function<ProviderVO, String> formatter = new Function<ProviderVO, String>() {
		public String apply(ProviderVO vo) {
			return vo.formatted();
		}
	};

}
