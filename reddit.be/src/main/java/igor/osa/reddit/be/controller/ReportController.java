package igor.osa.reddit.be.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import igor.osa.reddit.be.dto.ReportDTO;
import igor.osa.reddit.be.model.Report;
import igor.osa.reddit.be.service.ReportService;

@RestController
@RequestMapping(value = "/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@PostMapping
	public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO reportDTO){
		Report report = reportService.create(reportDTO);
		if (report == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<ReportDTO>(reportService.convertToDTO(report), HttpStatus.OK);
		}
	}
	
	@GetMapping
	public ResponseEntity<List<ReportDTO>> getAllByModerator(@RequestParam(value="user") String userName) {
		List<Report> reports = reportService.getAllByModerator(userName);
		if (reports == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<List<ReportDTO>>(reportService.convertListToDTO(reports), HttpStatus.OK);
		}
	}
}