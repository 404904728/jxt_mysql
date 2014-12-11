package core.cq.hmq.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.cq.hmq.annotation.Sequence;
import core.cq.hmq.dao.HelperDao;
import core.cq.hmq.pojo.sys.HbMySq;
import core.cq.hmq.util.tools.StringUtil;

@Service(value = "initDataService")
public class InitDataService extends BaseService {

	private static Class[] voClasses;

	public static void setVoClasses(Class[] voClasses) {
		InitDataService.voClasses = voClasses;
	}

	private List<String> initFiles;

	public void setInitFiles(List<String> initFiles) {
		this.initFiles = initFiles;
	}

	public List<String> getInitFiles() {
		return initFiles;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void createSequence() {
		if (voClasses == null) {
			return;
		}
		final HelperDao helperDao = dao.getHelperDao();
		if (dao.getHelperDao().isSequence()) {
			// 数据库支持sequence，
			for (@SuppressWarnings("rawtypes") Class clazz : voClasses) {
			    Sequence sequence = (Sequence) clazz
						.getAnnotation(Sequence.class);
				if (sequence == null) {
					sequence=(Sequence)HbMySq.class.getAnnotation(Sequence.class);
				}
				String seqName = StringUtil.getSeqName(sequence, clazz);
				final String existSeq = "select sequence_name from user_sequences where sequence_name='"
						+ seqName.toUpperCase() + "'";
				if (helperDao.find(existSeq).isEmpty()) {
					// 检查该类的sequence没有，就开始创建
					helperDao.excute("create sequence " + seqName
							+ " minvalue " + sequence.initialValue()
							+ " maxvalue 999999999999999999999999999"
							+ " start with " + sequence.initialValue()
							+ " increment by " + sequence.allocationSize()
							+ " cache " + sequence.cache());
				}
			}
		} else {
			if (dao.findOne(HbMySq.class) == null) {
				HbMySq hbMySq = new HbMySq();
				hbMySq.setId(0l);
				hbMySq.setVal(10000000l);// 主键的初始值设置很大:10000000，对于MySql
				dao.insert(hbMySq);
			}
		}
		voClasses = null;
	}
}
