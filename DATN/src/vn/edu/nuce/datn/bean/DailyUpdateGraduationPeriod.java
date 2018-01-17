package vn.edu.nuce.datn.bean;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vn.edu.nuce.datn.dao.GraduationScoreDAO;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.util.ResourceBundleUtil;

public class DailyUpdateGraduationPeriod implements Runnable {
	
	@Override
    public void run() {
		LocalDate now = LocalDate.now();
		String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		GraduationScoreDAO graduationScoreDAO = new GraduationScoreDAO();
		List<GraduationScore> lstGraduationScore = graduationScoreDAO.getLstCheckGraduationScore(dateStr);
		List<GraduationScore> lstUpdate = new ArrayList<>();
		for(GraduationScore item: lstGraduationScore){
//			File file = new File(ResourceBundleUtil.getString("local.path.document") + item.getFileName());
			File file = new File(ResourceBundleUtil.getString("server.path.document") + item.getFileName());
			if (!file.exists()) {
				item.setStatus(false);
				lstUpdate.add(item);
			}
		}
		if (lstUpdate.size() >= 0) {
			graduationScoreDAO.saveGS(lstUpdate);
		}
    }
}
