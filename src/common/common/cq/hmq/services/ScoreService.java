package common.cq.hmq.services;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.TransactionException;

import app.cq.hmq.pojo.cjmanage.Score;
import app.cq.hmq.pojo.cjmanage.ScoreContentMapping;
import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.util.tools.DateUtil;


/**
 * 
 * @author Administrator
 *
 */
public class ScoreService{
	
	public String getMessage(String userCode,int a){
		 return "are you ready? " + userCode +" " +a;
	}
	
	
	/**
	 * import student month score
	 * @param json
	 * @param scoreType
     * 	 String aa = "{\"title\": \"2014届初中九年级2014年2月入学考试\",\"datalist\": [{\"no\": \"214040070168\",\"name\": \"肖菥\",\"class\": \"1404\",\"subject\": [{\"name\": \"语文\",\"score\": 126.5,\"content\": \"A卷成绩:84.5,B卷成绩:42,年排:6,年排涨幅:21\"}," +
	   		"{\"name\": \"语文\",\"score\": 126.5,\"content\": \"A卷成绩:84.5,B卷成绩:42,年排:6,年排涨幅:21\"}," +
	   		"{\"name\": \"数学\",\"score\": 126.5,\"content\": \"A卷成绩:84.5,B卷成绩:42,年排:6,年排涨幅:21\"}," +
	   		"{\"name\": \"英语\",\"score\": 126.5,\"content\": \"A卷成绩:84.5,B卷成绩:42,年排:6,年排涨幅:21\"}," +
	   		"{\"name\": \"物理\",\"score\": 126.5,\"content\": \"A卷成绩:84.5,B卷成绩:42,年排:6,年排涨幅:21\"}]," +
	   		"\"totalscore\": 888,\"classorder\": 8,\"gradeorder\": 88,\"gradeasc\": 6,\"classasc\": -6}]}";
	 *
	 * @return  -1 事务初始化错误  -2 json 转换错误     -3 json传入参数错误或属性不足 -4 该标题已经存在  -5 事务提交失败回滚 -6 科目不存在或者科目对应的映射列未配置
	 */
	public int importScore(String json,int scoreType){
		if(null != json && !json.toString().matches("\\s*")){
			if(2 == scoreType){
				SessionFactory sessionFactory = (SessionFactory) BeanUtil.getBean("sessionFactory");
				if(null == sessionFactory){
					return -1;
				}
				Session session = sessionFactory.openSession();   
				if(null == session){
					return -1;
				}
				Transaction transaction = session.beginTransaction();
				if(null == transaction){
					return -1;
				}
				transaction.setTimeout(600);
				/** 导入成绩的日期 */
				String date = DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN);
				ObjectMapper om = new ObjectMapper();
				try {
					Map<String,Object> resultMap =  om.readValue(json, Map.class);
					if(null == resultMap || resultMap.size() < 1){
						return -3;
					}
					/** 考试标题 */
					String title = String.valueOf(resultMap.get("title"));
					/** 默认导入人为1 */
					if(judgeScoreTitleExistByImporter(session,title,1L)){
						return -4;
					}
					
					List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("datalist");
					List<Map<String, Object>> subjectList = null;
					if(null != dataList && dataList.size() > 0){
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
									if(judgeScoreExistByTitleandClass(session,sc.getTitle(),sc.getsClass())){
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
							subjectList = (List<Map<String, Object>>) map.get("subject");
							if(null != subjectList && subjectList.size() > 0){
								List<String> colNameList = null;
								Method setMethod = null;
								for (Map subMap : subjectList) {
									/** 取出每一科的科目名称 */
									String subjectName = (String) subMap.get("name");
									if(i == 0){
										if(null == subjectMap.get(subjectName)){
											colNameList  = session.createSQLQuery("select ssm.colname_f from scoresubjectmapping_t ssm," +
													"subjectinfo_t si where si.id_f = ssm.subject_f and si.name_f = '"+subjectName+"'").list();
										if (null == colNameList || colNameList.size() < 1) {
											/** 此处有可能是在成绩科目映射表中未配置关系 */
											System.out.println(subjectName+"不存在或者科目对应的映射列未配置");
											return -6;
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
									/** 确保有这个SEQ */
									scm.setId(getIdBySeq(session,"SCORECONTENTMAPPING_SEQ"));
									session.save(scm);
									if(i % 80 == 0){
										session.flush();
										session.clear();
									}
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
							
							sc.setScoreType(scoreType);
							sc.setImportDate(date);
							sc.setImportUser(1L);
							/** 确保有这个SEQ */
							sc.setId(getIdBySeq(session,"SCORE_SEQ"));
							session.save(sc);
						}
						transaction.commit();
						return 1;
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
					return -2;
				} catch (JsonMappingException e) {
					e.printStackTrace();
					return -2;
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}catch (TransactionException e) {
					transaction.rollback();
					return -5;
				}catch (Exception e) {
					e.printStackTrace();
					return -2;
				}finally{
					session.close();
				}
			}
		}
		return -2;
	}
	
	/*public int importScore(String json,int scoreType){
		if(null != json && !json.toString().matches("\\s*")){
			if(2 == scoreType){
				HibernateTransactionManager transactionManager = (HibernateTransactionManager) BeanUtil.getBean("transactionManager");
				Dao dao = (Dao) BeanUtil.getBean("dao");
				if(null == transactionManager || null == dao){
					return -1;
				}
				DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(
						DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
				if(null == transactionDefinition){
					return -1;
				}
				transactionDefinition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
				transactionDefinition.setTimeout(600);
				TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
				*//** 导入成绩的日期 *//*
				String date = DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN);
				ObjectMapper om = new ObjectMapper();
				try {
					Map<String,Object> resultMap =  om.readValue(json, Map.class);
					if(null == resultMap || resultMap.size() < 1){
						return -3;
					}
					*//** 考试标题 *//*
					String title = String.valueOf(resultMap.get("title"));
					*//** 默认导入人为1 *//*
					if(judgeScoreTitleExistByImporter(dao,title,1L)){
						return -4;
					}
					
					List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("datalist");
					List<Map<String, Object>> subjectList = null;
					if(null != dataList && dataList.size() > 0){
						Map<String,Object> map = null;
						*//** 拼接内容映射表中的关联字符 *//*
						String tempTitle = null;
						*//** 该数据中在数据库中已经存在的数据班级id *//*
						Set<String> classExistSet = new HashSet<String>();
						*//** 该数据中在数据库中不存在的数据班级id *//*
						Set<String> classNOExistSet = new HashSet<String>();
						*//** 存放对应科目的 映射列名 *//*
						Map<String, String> subjectMap = new HashMap<String, String>();
						
						for (int i = 0; i < dataList.size(); i++) {
							tempTitle = "";
							map = dataList.get(i);
							Score sc = new Score();
							sc.setTitle(title);
							sc.setsClass((String)map.get("class"));
							*//** 判断标题与班级是否已经在数据库中存在  true 存在 false 不存在*//*
							if(classExistSet.contains(sc.getsClass())){
								continue;
							}else{
								*//** 查询某个班级在某次考试内容中是否已经存在 *//*
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
							subjectList = (List<Map<String, Object>>) map.get("subject");
							if(null != subjectList && subjectList.size() > 0){
								List<String> colNameList = null;
								Method setMethod = null;
								for (Map subMap : subjectList) {
									*//** 取出每一科的科目名称 *//*
									String subjectName = (String) subMap.get("name");
									if(i == 0){
										if(null == subjectMap.get(subjectName)){
											colNameList  =  dao.getHelperDao().find("select ssm.colname_f from scoresubjectmapping_t ssm," +
													"subjectinfo_t si where si.id_f = ssm.subject_f and si.name_f = ?", subjectName);
										if (null == colNameList || colNameList.size() < 1) {
											*//** 此处有可能是在成绩科目映射表中未配置关系 *//*
											System.out.println(subjectName+"不存在或者科目对应的映射列未配置");
											return -6;
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
							
							sc.setScoreType(scoreType);
							sc.setImportDate(date);
							sc.setImportUser(1L);
							dao.insert(sc);
						}
						
						transactionManager.commit(status);
						return 1;
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
					return -2;
				} catch (JsonMappingException e) {
					e.printStackTrace();
					return -2;
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}catch (TransactionException e) {
					transactionManager.rollback(status);
					return -5;
				}catch (Exception e) {
					e.printStackTrace();
					return -2;
				}
			}
		}
		return -2;
	}*/

	/*public int importScore(String json,int scoreType){
		if(null != json && !json.toString().matches("\\s*")){
			if(2 == scoreType){
				HibernateTransactionManager transactionManager = (HibernateTransactionManager) BeanUtil.getBean("transactionManager");
				Dao dao = (Dao) BeanUtil.getBean("dao");
				if(null == transactionManager || null == dao){
					return -1;
				}
				DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(
						DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
				if(null == transactionDefinition){
					return -1;
				}
				transactionDefinition.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
				transactionDefinition.setTimeout(600);
				TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
				*//** 导入成绩的日期 *//*
				String date = DateUtil.format(new Date(), DateUtil.DATETIME_PATTERN);
				ObjectMapper om = new ObjectMapper();
				try {
					Map<String,Object> resultMap =  om.readValue(json, Map.class);
					if(null == resultMap || resultMap.size() < 1){
						return -3;
					}
					*//** 考试标题 *//*
					String title = String.valueOf(resultMap.get("title"));
					*//** 默认导入人为1 *//*
					if(judgeScoreTitleExistByImporter(dao,title,1L)){
						return -4;
					}
					
					List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("datalist");
					if(null != dataList && dataList.size() > 0){
						int length = dataList.size();
						int times = 4;
						int count = length/times;
						int mod = length%times;
						ExecutorService exec = Executors.newFixedThreadPool(times); 
						ArrayList<Future<Integer>> futures = new ArrayList<Future<Integer>>();
						try {			
								if(count == 0){
									ScoreInnerProcess process = new ScoreInnerProcess(dao, date, title,
											dataList.subList(0,mod));
									futures.add(exec.submit(process));
								}else{
									for(int i=0;i<times;i++){
										if(i == times -1){
											ScoreInnerProcess process = new ScoreInnerProcess(dao, date, title,dataList.subList(i*count,
													(((i+1)*count + mod))));
											futures.add(exec.submit(process));
										}else{
											ScoreInnerProcess process = new ScoreInnerProcess(dao, date, title,dataList.subList(i*count, (i+1)*count));
											futures.add(exec.submit(process));
										}
									}
								}
							
							for (Future<Integer> future : futures) {
									System.out.println(future.get());  
					        }
							exec.shutdown();
							transactionManager.commit(status);
							return 1;
						} catch (Exception e) {
							e.printStackTrace();
							transactionManager.rollback(status);
							exec.shutdown();
							return -5;
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
					return -2;
				} catch (JsonMappingException e) {
					e.printStackTrace();
					return -2;
				} catch (IOException e) {
					e.printStackTrace();
					return -2;
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return -2;
	}*/
	
	private boolean judgeScoreTitleExistByImporter(Session session,String ti,Long uid){
		Query query = session.createSQLQuery("select count(1) from score_t s where s.importUser_f = ? and s.title_f = ?");
		query.setParameter(0, uid);
		query.setParameter(1, ti);
		List o = query.list();
		if(null != o && o.size() > 0 && Integer.parseInt(String.valueOf(o.get(0))) > 0){
			return true;
		}
		return false;
	}
	
	private boolean judgeScoreExistByTitleandClass(Session session,String ti,String cc){
		Query query = session.createSQLQuery("select count(1) from score_t s where s.sclass_f = ? and s.title_f = ?");
		query.setParameter(0, cc);
		query.setParameter(1, ti);
		List o = query.list();
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}
	
	private Long getIdBySeq(Session session, String seqName) {
		final Number id = (Number) session.createSQLQuery("select " + seqName
					+ ".nextval from dual").list().get(0);
		return id.longValue();
	}
	
/*	private boolean judgeScoreTitleExistByImporter(Dao dao,String ti,Long uid){
		List o = dao.getHelperDao().find("select count(1) from score_t s where s.importUser_f = ? and s.title_f = ?",
				uid,ti);
		if(null != o && o.size() > 0 && Integer.parseInt(String.valueOf(o.get(0))) > 0){
			return true;
		}
		return false;
	}
	
	private boolean judgeScoreExistByTitleandClass(Dao dao,String ti,String cc){
		List o = dao.getHelperDao().find("select count(1) from score_t s where s.sclass_f = ? and s.title_f = ?",
				cc,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}*/
}
