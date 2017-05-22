package vn.edu.nuce.datn.thread;

import java.util.concurrent.Callable;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;

public class ImportStudentThread implements Callable<Boolean> {
	
	private GraduationPeriod graduationPeriod;
	private Student student;
	private GraduationPeriodDAO graduationPeriodDAO;
	
	
	public ImportStudentThread(GraduationPeriod graduationPeriod, Student student,
			GraduationPeriodDAO graduationPeriodDAO) {
		super();
		this.graduationPeriod = graduationPeriod;
		this.student = student;
		this.graduationPeriodDAO = graduationPeriodDAO;
	}


	@Override
	public Boolean call() throws Exception {
		return graduationPeriodDAO.saveGraPeriodAndSTSerial(graduationPeriod, student);
	}

}
