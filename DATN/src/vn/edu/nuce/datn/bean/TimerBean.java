package vn.edu.nuce.datn.bean;

import java.io.File;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import vn.edu.nuce.datn.dao.GraduationScoreDAO;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.util.ResourceBundleUtil;

@ManagedBean(eager = true)
@ApplicationScoped
public class TimerBean {

	private ScheduledExecutorService scheduler;
	 

	@PostConstruct
	public void init() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new DailyUpdateGraduationPeriod(), 0, 1, TimeUnit.HOURS);
	}

	@PreDestroy
	public void destroy() {
		scheduler.shutdownNow();
	}

}