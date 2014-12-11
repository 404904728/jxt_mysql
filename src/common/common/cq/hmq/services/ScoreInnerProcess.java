package common.cq.hmq.services;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import app.cq.hmq.pojo.cjmanage.Score;
import app.cq.hmq.pojo.cjmanage.ScoreContentMapping;
import core.cq.hmq.dao.Dao;

public class ScoreInnerProcess implements Callable<Integer>{

	private Dao dao;
	private String date;
	private String title;
	private List<Map<String, Object>> dataList;
	
	public ScoreInnerProcess(Dao dao, String date, String title,
			List<Map<String, Object>> dataList) {
		super();
		this.setDao(dao);
		this.setDate(date);
		this.setTitle(title);
		this.setDataList(dataList);
	}

	@Override
	public Integer call() throws Exception {
		Map<String,Object> map = null;
		/** 拼接内容映射表中的关联字符 */
		String tempTitle = null;
		/** 该数据中在数据库中已经存在的数据班级id */
		Set<String> classExistSet = new HashSet<String>();
		/** 该数据中在数据库中不存在的数据班级id */
		Set<String> classNOExistSet = new HashSet<String>();
		/** 存放对应科目的 映射列名 */
		Map<String, String> subjectMap = new HashMap<String, String>();
		for (int i = 0; i < dataList.size(); i++) {
			tempTitle = "";
			map = dataList.get(i);
			Score sc = new Score();
			sc.setTitle(title);
			sc.setsClass((String)map.get("class"));
			/** 判断标题与班级是否已经在数据库中存在  true 存在 false 不存在*/
			if(classExistSet.contains(sc.getsClass())){
				continue;
			}else{
				/** 查询某个班级在某次考试内容中是否已经存在 */
				if(!classNOExistSet.contains(sc.getsClass())){
					if(judgeScoreExistByTitleandClass(dao,sc.getTitle(),sc.getsClass())){
						classExistSet.add(sc.getsClass());
						continue;
					}else{
						classNOExistSet.add(sc.getsClass());	
					}
				}
			}
			sc.setSno((String)map.get("no"));
			sc.setsName((String)map.get("name"));
			tempTitle = title + sc.getsClass()+ sc.getsName();
			List<Map<String, Object>> subjectList = (List<Map<String, Object>>) map.get("subject");
			if(null != subjectList && subjectList.size() > 0){
				List<String> colNameList = null;
				Method setMethod = null;
				for (Map subMap : subjectList) {
					/** 取出每一科的科目名称 */
					String subjectName = (String) subMap.get("name");
					if(i == 0){
						if(null == subjectMap.get(subjectName)){
							colNameList  =  dao.getHelperDao().find("select ssm.colname_f from scoresubjectmapping_t ssm," +
									"subjectinfo_t si where si.id_f = ssm.subject_f and si.name_f = ?", subjectName);
						if (null == colNameList || colNameList.size() < 1) {
							/** 此处有可能是在成绩科目映射表中未配置关系 */
							continue;
						}
							subjectMap.put(subjectName, colNameList.get(0));
						}
					}
					
					String score = String.valueOf(subMap.get("score"));
				    setMethod = sc.getClass().getDeclaredMethod("set" + subjectMap.get(subjectName),Float.class);
					if (score.matches("([1-9]+[0-9]*|0)(\\.[\\d]{1})?")) {
						setMethod.invoke(sc, Float.parseFloat(score));
					} else {
						setMethod.invoke(sc, -1f);
					}
					ScoreContentMapping scm = new ScoreContentMapping();
					scm.setTitle(tempTitle);
					scm.setColName(subjectMap.get(subjectName));
					scm.setContentData((String)subMap.get("content"));
					dao.insert(scm);
				}
			}
			
			
			String totalScore =  String.valueOf(map.get("totalscore"));
			if (null == totalScore || totalScore.matches("\\s*")) {
				sc.setTotalScore(0f);
			} else {
				sc.setTotalScore(Float.parseFloat(totalScore));
			}
			
			String clasSorder =  String.valueOf(map.get("classorder"));
			if (null != clasSorder && !clasSorder.matches("\\s*")) {
				sc.setClassOrder(Short.parseShort(clasSorder));
			}
			
			String gradeOrder =  String.valueOf(map.get("gradeorder"));
			if (null != gradeOrder && !gradeOrder.matches("\\s*")) {
				sc.setGradeOrder(Short.parseShort(gradeOrder));
			}
			
			sc.setClassAsc(String.valueOf(map.get("classasc")));
			sc.setGradeAsc(String.valueOf(map.get("gradeasc")));
			
			sc.setScoreType(2);
			sc.setImportDate(date);
			sc.setImportUser(1L);
			dao.insert(sc);
		}
		return 121121;
	}
	
	private boolean judgeScoreExistByTitleandClass(Dao dao,String ti,String cc){
		List o = dao.getHelperDao().find("select count(1) from score_t s where s.sclass_f = ? and s.title_f = ?",
				cc,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Dao getDao() {
		return dao;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
	
}
