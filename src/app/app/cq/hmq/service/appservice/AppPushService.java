package app.cq.hmq.service.appservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.cq.hmq.service.subject.SubjectMappingService;
import core.cq.hmq.service.BaseService;

@Service
public class AppPushService extends BaseService {

	/**
	 * 根据班主任id查找学生数据
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findStudentByMleaderId(Long tId) {
		return dao
				.getHelperDao()
				.find("select s.id_f,s.name_f from studentInfo_t s,Org_t o where s.org_f=o.id_f and instr(o.mLeader_f,?) > 0",
						","+tId+",");
	}

	@Autowired
	private SubjectMappingService mappingService;

	/**
	 * 根据老师ID查找所教班级
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findClass(Long tId) {
		return dao
				.find("select distinct org.id, org.name from SubjectMapping where teacher = ?",
						tId);

	}

	/**
	 * 根据班级Id查找学生
	 * 
	 * @param classId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findStudentByClassId(Long classId) {
		return dao.find(" select id,name from studentInfo where status = 0 and org.id=?",
				classId);
	}

	/**
	 * 根据任课老师ID与班级ID查找所教班级的学生
	 * 
	 * @param tId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findStudentByTeacherId(Long tId, Long classId) {
		return dao
				.getHelperDao()
				.find("select s.id_f,s.name_f from studentInfo_t s,Org_t o where s.org_f=o.id_f and instr(o.mLeader_f,?) > 0",
						","+tId+",");
	}

}
