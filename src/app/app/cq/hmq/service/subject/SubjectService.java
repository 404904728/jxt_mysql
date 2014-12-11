package app.cq.hmq.service.subject;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.subject.SubjectInfo;
import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.service.BaseService;

@Service
public class SubjectService extends BaseService {

	/**
	 * 查询科目
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JqGridData<SubjectInfo> findAll(JqPageModel model) {
		JqGridData<SubjectInfo> jq = new JqGridData<SubjectInfo>();
		PageList<SubjectInfo> subjectInfos = null;
		if (null == model.getSearchKey() || "".equals(model.getSearchKey())) {
			subjectInfos = page(model, SubjectInfo.class);
		} else {
			subjectInfos = page(model, "from " + SubjectInfo.class.getName()
					+ " r where r.name like '%" + model.getSearchKey() + "%'");
		}
		jq.setPage(subjectInfos.getPageNo());
		jq.setRecords(subjectInfos.getTotalCount());
		jq.setRows(subjectInfos.getList());
		jq.setTotal(subjectInfos.getPageCount());
		return jq;
	}

	/**
	 * 根据id查找科目
	 * 
	 * @param id
	 * @return
	 */
	public SubjectInfo findById(Long id) {
		// TODO Auto-generated method stub
		return dao.findOne(SubjectInfo.class, "id", id);
	}

	@Transactional
	public int saveOrUpdate(SubjectInfo subject) {
		// TODO Auto-generated method stub
		if (subject.getId() == null) {
			SubjectInfo subjectInfo = dao.findOne(SubjectInfo.class, "name",
					subject.getName());
			if (subjectInfo != null) {
				return 0;
			}
			subject.setCreateDate(new Date());
			subject.setCreatePerson(currentUserId());
			dao.insert(subject);
			return 1;
		} else {
			SubjectInfo dbSubject = dao.findOne(SubjectInfo.class, "id",
					subject.getId());
			dbSubject.setDesc(subject.getDesc());
			dbSubject.setName(subject.getName());
			dao.update(dbSubject);
			return 1;
		}
	}

	@Transactional
	public void del(Long id) {
		// TODO Auto-generated method stub
		dao.delete(SubjectInfo.class, id);
	}
}
