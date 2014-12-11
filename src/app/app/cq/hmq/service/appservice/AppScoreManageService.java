/**
 * Limit
 *
 */
package app.cq.hmq.service.appservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.StringUtil;

/**
 * @author Administrator
 *
 */
@Service(value = "appScoreManageService")
public class AppScoreManageService extends BaseService {
	
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-22 下午3:19:59
	 *@version 1.0
	 * @param lk 
	 *@Description 查询具体某个班级的所有科目信息
	 *
	 *@param lk
	 *@param ut
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryClassSubject(String lk, String classId) {
		Map<String, Object> subMap = null;
		List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
		if(!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)){
			if(null == classId || "".equals(classId)){
				return backList;
			}
			List subList = dao.getHelperDao().find("select sc.colname_f, sub.name_f from scoresubjectmapping_t sc , subjectinfo_t sub, subjectmapping_t adssad where sc.subject_f = sub.id_f and sub.id_f = adssad.subjectinfo_f and adssad.org_f = ?", Long.parseLong(classId));
			if(null != subList){
				for (int i = 0; i < subList.size(); i++) {
					Object[] object = (Object[]) subList.get(i);
					subMap = new HashMap<String, Object>();
					subMap.put("id", object[0]);
					subMap.put("name", object[1]);
					backList.add(subMap);
				}
				return backList;
			}
		}
		return null;
	}

	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-3-22 下午3:19:59
	 *@version 1.0
	 *@Description 获取具体某个班级中某一科目所有学生的信息
	 *
	 *@param lk
	 * @param title 
	 *@param ut
	 *@return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryStudentScore(String lk,
			String classID, String subId, String title) throws JsonParseException, JsonMappingException, IOException {
		if(!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(null == classID || "".equals(classID)){
				resultMap.put("flg", "0");
				return resultMap;
			}
			if(null == subId || "".equals(subId)){
				resultMap.put("flg", "0");
				return resultMap;
			}
			if(null == title || "".equals(title)){
				resultMap.put("flg", "0");
				return resultMap;
			}
			String grandClass = getClassValue(classID).getCode();
			String dateSql = "";
			if("now".equals(title)){
				dateSql = "and s.importdate_f = (select max(st.importdate_f) from score_t st where st.sclass_f = '"+ grandClass +"')";
			}else{
				dateSql =" and s.title_f = '"+ title +"'";
			}
			//s.title_f like '%"+ ksnr +"%'
			String sor = "order by s."+ subId +"_f desc ";
			String quertSql = "select r.title_f,  r.stuname, " +
					"scm.contentdata_f, r.total from " +
					"(select s.sname_f,s.title_f,s.sclass_f," +
					"s.sname_f as stuname, case s."+ subId +"_f when -1 then '缺考' " +
					"else to_char(s."+ subId +"_f) end as total from score_t s where " +
					" s.sclass_f = '"+ grandClass +"' " + dateSql + sor +") r left join scorecontentmapping_t scm " +
					"on scm.title_f = r.title_f || r.sclass_f ||  r.stuname and scm.colname_f = '"+ subId +"'";
			List scoreList = dao.getHelperDao().find(quertSql);
			if(CollectionUtils.isEmpty(scoreList)){
				return resultMap;
			}
			Object[] object = (Object[]) scoreList.get(0);
			if(object.length == 0){
				resultMap.put("flg", "0");
				return resultMap;
			}
			if(null != object[0] && !"".equals(object[0].toString())){
				String kaoshiTitle = object[0].toString();
				resultMap.put("title", kaoshiTitle);
			}
			List<String> flgList = new ArrayList<String>();
			List<String> contentList = new ArrayList<String>();
			if(null != object[1] && !"".equals(object[1].toString())){
				flgList.add("姓名");
			}
			if(null != object[2] && !"".equals(object[2].toString())){
				String content = object[2].toString();
				String[] replace = content.replace("{", "").replace("}", "").replace("\"", "").replace(".0", "").split(",");
				for (int i = 0; i < replace.length; i++) {
					flgList.add(replace[i].split(":")[0]);
					contentList.add(replace[i].split(":")[0]);
				}
			}
			if(null != object[3] && !"".equals(object[3].toString())){
				flgList.add("总分");
			}
			resultMap.put("content", flgList);
			ObjectMapper o = new ObjectMapper();
			List<Object> scrList = null;
			List<List<Object>> returnList = new ArrayList<List<Object>>();
			for (int i = 0; i < scoreList.size(); i++) {
				Object[] sco = (Object[]) scoreList.get(i);
				scrList = new ArrayList<Object>();
				scrList.add(sco[1].toString());
				if(null != sco[2] && !"".equals(sco[2].toString())){
					Map<String,Object> readValue = o.readValue(sco[2].toString().replace(".0", ""), Map.class);
					for (int j = 0; j < contentList.size(); j++) {
						scrList.add(readValue.get(contentList.get(j)));
					}
				}
				if(null != sco[3] && !"".equals(sco[3].toString())){
					scrList.add(sco[3].toString());
				}
				returnList.add(scrList);
			}
			resultMap.put("body", returnList);
			try {
				resultMap.put("body", returnList);
			} catch (Exception e) {
				if(contentList.size() != 0){
					contentList.clear();
					contentList = null;
				}
				if(scrList.size() != 0){
					scrList.clear();
					scrList = null;
				}
			}
			resultMap.put("flg", "1");
			return resultMap;
		}
		return null;
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-1 上午9:47:30
	 *@version 1.0
	 *@Description 查询一个班级近期考试中标题
	 *
	 *@param lk
	 *@param classID
	 *@param subId
	 *@return
	 *
	 *
	 */
	public List<Map<String, Object>> queryStudentScoreTitle(String lk, String classID) {
		List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
		if(!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)){
			Map<String, Object> map = null;
			if(null == classID || "".equals(classID)){
				return backList;
			}
			Org org = getClassValue(classID);
			String startTitle = String.valueOf(dao.getHelperDao().find("select case t.parent_f when 5 then '高' when 5 then '初' else '小' end sta" +
					" from org_t t where t.id_f = (select o.parent_f from org_t o where o.id_f = ?) and t.type_f = 2", Long.parseLong(classID)).get(0));
			String queryCountTitle = "with t as (select sc.title_f, count(1) as total from score_t sc where sc.totalscore_f != 0" +
					" and sc.title_f like '"+org.getName().substring(0,1)+"%' and sc.sclass_f = '"+org.getCode()+"' group by sc.title_f,sc.importdate_f" +
					" order by sc.importdate_f desc) select title_f,total from t  where rownum < 7"; 
			List titleList = dao.getHelperDao().find(queryCountTitle);
			if(titleList.size() == 0){
				return backList;
			}
			for (int i = 0; i < titleList.size(); i++) {
				Object[] object = (Object[]) titleList.get(i);
				map = new HashMap<String, Object>();
				map.put("title", object[0].toString());
				map.put("count", object[1].toString());
				backList.add(map);
			}
			return backList;
		}
		return null;
	}
	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-1 上午9:50:35
	 *@version 1.0
	 *@Description 班级名称转换
	 *
	 *@param classId
	 *@return
	 *
	 *
	 */
	@SuppressWarnings("unused")
	private Org getClassValue(String classID){
		Org org = (common.cq.hmq.pojo.sys.Org) dao.findOne("from Org o where o.id = ?", Long.parseLong(classID));
//		String name = org.getParent().getParent().getName();
//		String grand = "";
//		String classValue = "";
		String grandClass = "";
		if(null != org){
			return org;
		}else{
			return new Org();
		}
//		
//		//2011级3班
//		if(org.getName().length() == 8){
//			grand = org.getName().substring(2, 4);
//			classValue = org.getName().substring(5, 7);
//		}else if(org.getName().length() == 7){
//			grand = org.getName().substring(2, 4);
//			classValue = org.getName().substring(5, 6);
//		}
//		if("小学".equals(name)){
//			int i = Integer.parseInt(grand) + 6;
//			if(org.getName().length() == 8){
//				grandClass = i + classValue;
//			}else if(org.getName().length() == 7){
//				grandClass = i + "0" + classValue;
//			}
//		}else if("初中".equals(name)){
//			int i = Integer.parseInt(grand) + 3;
//			if(org.getName().length() == 8){
//				grandClass = i + classValue;
//			}else if(org.getName().length() == 7){
//				grandClass = i + "0" + classValue;
//			}
//		}else if("高中".equals(name)){
//			int i = Integer.parseInt(grand) + 3;
//			if(org.getName().length() == 8){
//				grandClass = i + classValue;
//			}else if(org.getName().length() == 7){
//				grandClass = i + "0" + classValue;
//			}
//		}
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-4-3 下午6:07:23
	 *@version 1.0
	 *@Description 家长登录后查询自己学生的总成绩
	 *
	 *@param lk
	 *@param stuId
	 *@param StuName
	 *@return
	 *
	 *
	 */
	public Map<String, Object> queryStudentScoreTitle(String lk, Long stuId,
			String stuName) {
//		stuName = "李嘉豪";
		Map<String, Object> scoreMap = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)){
			
		if(null == stuId){
			scoreMap.put("flg", 0);
			return scoreMap;
		}if(null == stuName || "".equals(stuName)){
			scoreMap.put("flg", 0);
			return scoreMap;
		}
		String orgSql = "select org.id_f,org.code_f from org_t org  where org.id_f = (select stu.org_f from studentinfo_t stu where stu.id_f = "+ stuId +")";
		List<Object[]> orgList = dao.getHelperDao().find(orgSql);
		if(null == orgList || orgList.size() == 0){
			scoreMap.put("flg", 0);
			return scoreMap;
		}
		String classValue = (String) orgList.get(0)[1];
		scoreMap.put("classId", orgList.get(0)[0]);
		String scoreSql = "select classasc_f,classorder_f,gradeasc_f,gradeorder_f," +
				"totalscore_f,title_f,sclass_f,bzrcomments_f from score_t where id_f = ( select max(id_f) from score_t where " +
				" sclass_f = '"+ classValue +"' and sname_f = '"+ stuName +"' and publishstatus_f = 1)";
		List<Object[]> scoreList = dao.getHelperDao().find(scoreSql);
		if(null == scoreList || scoreList.size() == 0){
			scoreMap.put("flg", 0);
			return scoreMap;
		}
		scoreMap.put("classValue", classValue);
		Object[] sco = null;
		for (int i = 0; i < scoreList.size(); i++) {
			sco = scoreList.get(i);
			if(null != sco[0] && !"".equals(sco[0].toString())){
				scoreMap.put("classAsc", sco[0].toString().replace(".0", ""));
			}else{
				scoreMap.put("classAsc", "");
			}
			if(null != sco[1] && !"".equals(sco[1].toString())){
				scoreMap.put("classOrder", sco[1].toString().replace(".0", ""));
			}else{
				scoreMap.put("classOrder", "");
			}
			if(null != sco[2] && !"".equals(sco[2].toString())){
				scoreMap.put("grandAsc", sco[2].toString().replace(".0", ""));
			}else{
				scoreMap.put("grandAsc", "");
			}
			if(null != sco[3] && !"".equals(sco[3].toString())){
				scoreMap.put("grandOrder", sco[3].toString().replace(".0", ""));
			}else{
				scoreMap.put("grandOrder", "");
			}
			if(null != sco[4] && !"".equals(sco[4].toString())){
				scoreMap.put("totalScore", sco[4].toString().replace(".0", ""));
			}else{
				scoreMap.put("totalScore", "");
			}
			if(null != sco[5] && !"".equals(sco[5].toString())){
				scoreMap.put("title", sco[5].toString());
			}else{
				scoreMap.put("title", "");
			}
			if(null != sco[6] && !"".equals(sco[6].toString())){
				scoreMap.put("class", sco[6].toString());
			}else{
				scoreMap.put("class", "");
			}
			if(null != sco[7] && !"".equals(sco[7].toString())){
				scoreMap.put("bzrcomments", sco[7].toString());
			}else{
				scoreMap.put("bzrcomments", "");
			}
			
		}
			scoreMap.put("flg", 1);
			return scoreMap;
		}
		scoreMap.put("flg", 0);
		return scoreMap;
	}


	
	/**
	 * 
	 *@title
	 *@author Limit
	 *@date 2014-5-26 下午2:50:16
	 *@version 1.0
	 *@Description 家长登录后查询自己学生的各科成绩
	 *
	 *@param lk
	 *@param classId
	 *@param classValue
	 *@param stuName
	 *@return
	 *
	 *
	 */
	public List<Map<String, Object>> queryOneStudentScore(String lk, Long classId,
			String classValue, String stuName) {
//		stuName = "周阳";
		if(!StringUtil.isEmpty(lk) && User.LKEY.equals(lk)){
			if(null == classId){
				return null;
			}
			if(StringUtil.isEmpty(classValue)){
				return null;
			}
			if(StringUtil.isEmpty(stuName)){
				return null;
			}
			String otherSql = "select (select pping.colname_f from scoresubjectmapping_t pping " +
					"where pping.subject_f = pp.subjectinfo_f) as otherName, " +
					"(select info.name_f from subjectinfo_t info where info.id_f = pp.subjectinfo_f) " +
					"as subname, (select concat(ttea.name_f,',',ttea.telephone_f,',',ttea.id_f) from teacherinfo_t ttea " +
					"where ttea.id_f = pp.teacher_f) as tea from subjectmapping_t pp " +
					"where pp.org_f =" + classId;
			List otherList = dao.getHelperDao().find(otherSql);
			if(null ==  otherList || otherList.size() == 0){
				return null;
			}
			//存放科目的list
			List<String> subList = new ArrayList<String>();
			//存放科目对应汉字科目的Map
			Map<String,Object> subMappingMap = new HashMap<String,Object>();
			//存放科目对应教师的map
			Map<String,Object> sunTeaMap = new HashMap<String,Object>();
			/**拼接score表 的单科分列名 */
			String sub = "";
			
			
			/** 分数内容隐射表的别名和已查询出的结果集为硬代码 scm 和 r */
			String tempStr="decode(scm.colname_f,";
			
			/** 例子 Score4	物理	唐先峰,18980971315,1125 */
			for (int i = 0; i < otherList.size(); i++) {
				Object[] object = (Object[]) otherList.get(i);
				if(!StringUtil.isEmpty(object[0])){
					subList.add(object[0].toString());
					sub += object[0].toString()+ "_f "+object[0].toString()+", ";
					tempStr += "'"+object[0].toString()+"',r."+object[0].toString()+",";
					/*if((i+1) == otherList.size()){
						sub += "'" + object[0].toString() + "'";
					}else{
						sub += "'" + object[0].toString() + "',";
					}*/
				}
				if(!StringUtil.isEmpty(object[0]) && !StringUtil.isEmpty(object[1]) && !StringUtil.isEmpty(object[2])){
					subMappingMap.put(object[0].toString(), object[1].toString());
					sunTeaMap.put(object[0].toString(), object[2].toString());
				}
			}
			
			tempStr += "'-1')";
			String scoreSql = "select scm.contentdata_f, " +
					" scm.colname_f," +tempStr+
					" from (select " +sub+
					" s.title_f from score_t s " +
					"where s.sclass_f = '"+ classValue +"' and s.sname_f = '"+ stuName +"' " +
					"and s.importdate_f = (select max(st.importdate_f) " +
					"from score_t st where st.sclass_f = '"+ classValue +"' and st.publishstatus_f = 1 and st.sname_f = '" + stuName + "') ) r " +
					"left join scorecontentmapping_t scm on " +
					"scm.title_f = r.title_f || '"+classValue+"' || '"+stuName+"'";
					/** 暂时不用 In 班级所开设的科目 */
					//" and scm.colname_f in("+ sub +")";
			List<Object[]> scoreList = dao.getHelperDao().find(scoreSql);
			if(scoreList.size() == 0){
				return null;
			}
			Object[] obj = null;
			Map<String, Object> resMap = null;
			String scoreContent=null;
			List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < scoreList.size(); i++) {
				obj = scoreList.get(i);
				for (int j = 0; j < subList.size(); j++) {
					if(subList.get(j).equals(obj[1].toString())){
						resMap = new HashMap<String, Object>();
						scoreContent = String.valueOf( obj[0]);
						resMap.put("sub", subMappingMap.get(subList.get(j)));
						String tea = (String) sunTeaMap.get(subList.get(j));
						String[] split = tea.split(",");
						resMap.put("teaName", split[0]);
						resMap.put("teaNO", split[1]);
						resMap.put("teaId", split[2]);
						if(!"-1".equals(String.valueOf(obj[2])) ){
							resMap.put("coutent",scoreContent.replace("}",",\"总分\":"+obj[2]+"}"));
						}else{
							resMap.put("coutent",scoreContent);
						}
						resList.add(resMap);
						break;
					}
				}
			}
			try {
				return resList;
			} finally{
				if(subList.size() != 0){
					subList.clear();
					subList = null;
				}
				if(subMappingMap.size() != 0){
					subMappingMap.clear();
					subMappingMap = null;
				}
				if(sunTeaMap.size() != 0){
					sunTeaMap.clear();
					sunTeaMap = null;
				}
			}
		}
		return null;
	}
}
