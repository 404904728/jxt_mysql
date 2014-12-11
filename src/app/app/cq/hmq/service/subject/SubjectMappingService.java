package app.cq.hmq.service.subject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.subject.SubjectInfo;
import app.cq.hmq.pojo.subject.SubjectMapping;

import common.cq.hmq.pojo.sys.Org;

import core.cq.hmq.service.BaseService;

/**
 * 科目 部门 老师映射关系
 * 
 * @author cqmonster
 * 
 */
@Service
public class SubjectMappingService extends BaseService {

	/**
	 * 根据班级ID取映射关系
	 * 
	 * @return
	 */
	public List<SubjectMapping> findSubjectMapingByOrg(Long orgId) {
		return dao.find(SubjectMapping.class, "org.id", orgId);
	}

	/**
	 * 根据班级查询 班级下的老师
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findTeacherByOrg(Long orgId) {
		return dao
				.getHelperDao()
				.find("select t.name_f from teacherInfo_t t,subjectMapping_t s where  t.id_f=s.teacher_f  and s.org_f=?",
						orgId);
	}

	/**
	 * 根据老师ID取映射关系
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SubjectMapping> findSubjectMapingByTeacherId(Long tId) {
		List<SubjectMapping> result = new ArrayList<SubjectMapping>();
		List<Object> list = dao
				.find("select distinct org.id, org.name,subjectInfo.id,subjectInfo.name from SubjectMapping where teacher = ?",
						tId);
		if (null != list && list.size() > 0) {
			Object[] os = null;
			for (Object o : list) {
				os = (Object[]) o;
				SubjectMapping sb = new SubjectMapping();
				Org org = new Org();
				org.setId((Long) os[0]);
				org.setName((String) os[1]);
				sb.setOrg(org);
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.setId((Long) os[2]);
				subjectInfo.setName((String) os[3]);
				sb.setSubjectInfo(subjectInfo);
				result.add(sb);
			}
		}
		return result;
	}
	
	/**
	 * 根据教师id 得到教师所授课班级
	 */
	@SuppressWarnings("unchecked")
	public List<SubjectMapping> findClassByTeacherId(Long tId) {
		List<SubjectMapping> result = new ArrayList<SubjectMapping>();
		List<Object> list = dao.getHelperDao().find(
		"select distinct o.id_f,o.name_f from org_t o where o.id_f  in (select sm.org_f from subjectmapping_t sm where sm.teacher_f = ?)"+
		"union select o.id_f,o.name_f from org_t o where instr(o.mleader_f,?) > 0 and o.type_f = 3"
		,tId,","+tId+",");
		if (null != list && list.size() > 0) {
			Object[] os = null;
			for (Object o : list) {
				os = (Object[]) o;
				SubjectMapping sb = new SubjectMapping();
				Org org = new Org();
				org.setId(Long.parseLong(String.valueOf(os[0])));
				org.setName((String) os[1]);
				sb.setOrg(org);
				result.add(sb);
			}
		}
		return result;
	}
	
	

	@SuppressWarnings("unchecked")
	public List<Object[]> findClassByTeacherIdObject(Long tId) {
		return dao
				.find("select distinct org.id, org.name from SubjectMapping where teacher = ?",
						tId);
	}

	/**
	 * 查找所有科目
	 * 
	 * @return
	 */
	public List<SubjectInfo> findSubjectAll() {
		return dao.find(SubjectInfo.class);
	}

	/**
	 * 绑定 教师 班级 科目 关系
	 * 
	 * @param subjectClass
	 *            [12,2] [班级id，科目id]
	 * @param tId
	 *            老师id
	 */
	@Transactional
	public void bindSubject(String[] subjectClass, Long tId) {
		dao.excute("delete SubjectMapping where teacher = ? ", tId);
		if(null != subjectClass){
			for (int i = 0; i < subjectClass.length; i++) {
				String[] subject = subjectClass[i].split(",");
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.setId(Long.parseLong(subject[1]));
				Org org = new Org();
				org.setId(Long.parseLong(subject[0]));
				SubjectMapping sm = new SubjectMapping();
				sm.setOrg(org);
				sm.setSubjectInfo(subjectInfo);
				sm.setTeacher(tId);
				dao.insert(sm);
			}
		}

	}
	
	public void findSubjectsByOrgId(Long orgId){
	 List<SubjectMapping> list = dao.find("from SubjectMapping, where org.id = ?", orgId);
	 if(null != list && list.size() > 0){
		 for (SubjectMapping subjectMapping : list) {
			//subjectMapping.getSubjectInfo().getName();
		}
	 }
	}

}
