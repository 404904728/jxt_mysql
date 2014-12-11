/**
 * Limit
 *
 */
package app.cq.hmq.service.cjmanage;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cq.hmq.pojo.cjmanage.Score;
import app.cq.hmq.pojo.cjmanage.ScoreContentMapping;
import core.cq.hmq.service.BaseService;

/**
 * @author Administrator
 *
 */
@Service
public class ScoreImportThread extends BaseService implements Callable<Integer> {
	private String dankezcj; 
	private int start;
	private int end;
	private HSSFSheet hssfSheet;
	private String title;
	private  Map<Integer, String> cMap;
	private int arriveTotalScoreCount;
	private String date;
	private Long curUserID;
	private Map<Integer, String> sMap;
	private Integer scoreType;
	

	@Override
	@Transactional
	public Integer call() throws Exception {
		StringBuffer tempContent = new StringBuffer();
		/** 该文件中在数据库中已经存在的数据班级id */
		Set<String> classExistSet = new HashSet<String>();
		/** 该文件中在数据库中不存在的数据班级id */
		Set<String> classNOExistSet = new HashSet<String>();
		 int effectiveStu = 0;
		 String tempContentFlag = null;
		 String tempTitle = null;
			 for (int j = start;j <= end; j++) {
					HSSFRow dataRow = hssfSheet.getRow(j);
					if (dataRow == null) {
						continue;
					}
					Score sc = new Score();
					sc.setTitle(title);
					HSSFCell dataCell = null;
					Method setMethod = null;
					String colName = null;
					String contentName = null;
					String val = null;
					
					for (int c = 0; c < dataRow.getLastCellNum(); c++) {
						dataCell = dataRow.getCell(c);
						if (c == 0) {
							dataCell.setCellType(Cell.CELL_TYPE_STRING);
							sc.setSno(getValue(dataCell));
							continue;
						}else if (c == 1) {
							sc.setsName(getValue(dataCell));
							continue;
						}else if (c == 2) {
							dataCell.setCellType(Cell.CELL_TYPE_STRING);
							sc.setsClass(getValue(dataCell));
							tempTitle = title + sc.getsClass()+ sc.getsName();
							continue;
						}

						/** 判断标题、班级、姓名是否已经在数据库中存在  true 存在 false 不存在*/
						if(classExistSet.contains(sc.getsClass())){
							break;
						}else{
							/** 查询某个班级的某个学生在某次考试内容中是否已经存在 */
							if(!classNOExistSet.contains(sc.getsClass()) &&
									judgeScoreExistByTitleandClass(sc.getTitle(),sc.getsClass(),sc.getsName())){
								classExistSet.add(sc.getsClass());
								break;
							}else{
								classNOExistSet.add(sc.getsClass());
							}
						}

						if (3 <= c && c < arriveTotalScoreCount) {
								contentName = cMap.get(c);
								colName = sMap.get(c);
								if (null != contentName && dankezcj.equals(contentName.trim())) {
									    try {
											setMethod = sc.getClass()
												.getDeclaredMethod("set" + colName,Float.class);
											val = getValue(dataCell);
											if (val.matches("([1-9]+[0-9]*|0)(\\.[\\d]{1})?")) {
												setMethod.invoke(sc, Float.parseFloat(val));
											} else {
												setMethod.invoke(sc, -1f);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									
									if(!tempContentFlag.equals(colName)){
										ScoreContentMapping scm = new ScoreContentMapping();
										scm.setTitle(tempTitle);
										/** 当遇到某些科目直接是总成绩开始时，需保存前一科目的考试内容 */
										scm.setColName(sMap.get(c-1));
										scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString() + "}");
										dao.insert(scm);
										tempContentFlag = colName;
										tempContent = new StringBuffer();
									}
								} else {
									if (null == tempContentFlag
											|| tempContentFlag.equals(colName)) {
										tempContent.append("\"" + contentName+ "\":\"" + getValue(dataCell)+ "\",");
										tempContentFlag = colName;
									} else {
										ScoreContentMapping scm = new ScoreContentMapping();
										scm.setTitle(tempTitle);
										scm.setColName(sMap.get(c-1));
										scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString() + "}");
										dao.insert(scm);
										tempContentFlag = colName;
										tempContent = new StringBuffer();
										tempContent.append("\"" + contentName+ "\":\"" + getValue(dataCell)	+ "\",");
									}
								}
							}else{
								/** 总分 */
								if (c == arriveTotalScoreCount) {
									String t = getValue(dataCell);
									if (null == t || t.matches("\\s*")) {
										sc.setTotalScore(0f);
									} else {
										float ts = Float.parseFloat(t);
										if(ts > 0){
											effectiveStu++;
										}
										sc.setTotalScore(Float.parseFloat(t));
									}

									/** 当设置总分的时候，需要把之前的科目内容保存 */
									ScoreContentMapping scm = new ScoreContentMapping();
									scm.setTitle(title + sc.getsClass() + sc.getsName());
									scm.setColName(sMap.get(c-1));
									scm.setContentData("{"+ tempContent.deleteCharAt(tempContent.length() - 1).toString()+ "}");
									dao.insert(scm);
									tempContentFlag = null;
									tempContent = new StringBuffer();
									continue;
								}else if (c == 1 + arriveTotalScoreCount) {/** 班排 */
									String t = getValue(dataCell);
									if (null == t || t.matches("\\s*")) {
									} else {
										sc.setClassOrder(Short.parseShort(t.replace(".0", "")));
									}
									continue;
								}else if (c == 2 + arriveTotalScoreCount) {/** 年排 */
									String t = getValue(dataCell);
									if (null == t || t.matches("\\s*")) {
									} else {
										sc.setGradeOrder(Short.parseShort(t.replace(".0", "")));
									}
									continue;
								}else if (c == 3 + arriveTotalScoreCount) { 			/**班级涨幅 */
									dataCell.setCellType(Cell.CELL_TYPE_STRING);
									sc.setClassAsc(getValue(dataCell));
									continue;
								}else if (c == 4 + arriveTotalScoreCount) {/**年排涨幅 */
									dataCell.setCellType(Cell.CELL_TYPE_STRING);
									sc.setGradeAsc(getValue(dataCell));
									continue;
								}
							}
					}
					if(classExistSet.contains(sc.getsClass())){
						continue;
					}
					sc.setImportDate(date);
					sc.setImportUser(curUserID);
					sc.setScoreType(scoreType);
					dao.insert(sc);
				}
			return effectiveStu;
	}
	
	
	public boolean judgeScoreExistByTitleandClass(String ti,String cc,String na){
		List o = dao.getHelperDao().find("select count(1) from score_t s where  s.sname_f = ? and s.sclass_f = ? and s.title_f = ?",
				na,cc,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}
	
	public boolean judgeScoreTitleExistByImporter(String ti,Long uid){
		List o = dao.getHelperDao().find("select count(1) from score_t s where s.importUser_f = ? and s.title_f = ?",
				uid,ti);
		if(null != o && o.size() > 0 && Integer.parseInt((String.valueOf(o.get(0)))) > 0){
			return true;
		}
		return false;
	}
	
	public String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public HSSFSheet getHssfSheet() {
		return hssfSheet;
	}

	public void setHssfSheet(HSSFSheet hssfSheet) {
		this.hssfSheet = hssfSheet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<Integer, String> getcMap() {
		return cMap;
	}

	public void setcMap(Map<Integer, String> cMap) {
		this.cMap = cMap;
	}

	public int getArriveTotalScoreCount() {
		return arriveTotalScoreCount;
	}

	public void setArriveTotalScoreCount(int arriveTotalScoreCount) {
		this.arriveTotalScoreCount = arriveTotalScoreCount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getCurUserID() {
		return curUserID;
	}

	public void setCurUserID(Long curUserID) {
		this.curUserID = curUserID;
	}

	public Map<Integer, String> getsMap() {
		return sMap;
	}

	public void setsMap(Map<Integer, String> sMap) {
		this.sMap = sMap;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}
	
	public void setDankezcj(String dankezcj) {
		this.dankezcj = dankezcj;
	}

	public String getDankezcj() {
		return dankezcj;
	}
	
	/*ExecutorService pool;
	try {
		int total = hssfSheet.getLastRowNum();
		int onetime = 200;
		int b = total/onetime;
		int c = total%onetime;
		
		int times = b;
		if(c != 0){
			times += 1;
		}
		 pool = Executors.newFixedThreadPool(times); 
		 ScoreImportThread[] es = new ScoreImportThread[times];
		 Future[] fs = new Future[times];
		 for (int i = 0; i < es.length; i++) {
			es[i] = new ScoreImportThread();
			if(i ==0){
				es[i].setStart(num);	
			}else{
				es[i].setStart(i*onetime);	
			}
			if((i+1)*onetime > total){
				es[i].setEnd(i*onetime+c);
			}else{
				es[i].setEnd((i+1)*onetime -1);
			}
			es[i].setArriveTotalScoreCount(arriveTotalScoreCount);
			es[i].setcMap(cMap);
			es[i].setCurUserID(cur.getId());
			es[i].setDate(date);
			es[i].setHssfSheet(hssfSheet);
			es[i].setScoreType(scoreType);
			es[i].setsMap(sMap);
			es[i].setTitle(title);
			es[i].setDankezcj(DANKEZCJ);
			fs[i] = pool.submit(es[i]);
		}
		for (Future future : fs) {
		Integer t = (Integer) future.get();
		System.out.println(t);
		 effectiveStu +=t;
		}
	} catch (Exception e) {
		dao.getHelperDao().excute("delete from score_t sc where sc.title_f = '" +title+"' and sc.importuser_f = " + cur.getId());
		dao.getHelperDao().excute("delete from scorecontentmapping_t scm where scm.title_f like '"+title+"%'");
		dao.getHelperDao().excute("commit");
		throw e;
	} 
	 pool.shutdown(); */
}
