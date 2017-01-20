package vn.edu.nuce.datn.bean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(eager=true)
@ApplicationScoped
public class TimerBean {

    private ScheduledExecutorService scheduler; 

    @PostConstruct
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DailyUpdateGraduationPeriod(), 0, 1, TimeUnit.DAYS);
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdownNow();
    }

}