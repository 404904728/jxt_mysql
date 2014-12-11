package app.cq.hmq.service.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import app.cq.hmq.pojo.notice.SmsState;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;

import core.cq.hmq.dao.PageList;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.tools.StringUtil;

/**
 * Created by IntelliJ IDEA. User: cqmonster Date: 14-8-26 Time: 下午4:57 To
 * change this template use File | Settings | File Templates.
 */
@Service
public class SmsManageService extends BaseService {

    @SuppressWarnings("unchecked")
	public JqGridData<SmsState> findAll(JqPageModel model) {
        JqGridData<SmsState> jq = new JqGridData<SmsState>();
        String hql = "from " + SmsState.class.getName();
        if (!StringUtil.isEmpty(model.getSearchKey())) {
            hql += " where  date like '%" + model.getSearchKey()
                    + "%' or name like'%" + model.getSearchKey()
                    + "%' or  recipient='" + model.getSearchKey() + "'";
        }
        hql += " order by date desc";
        PageList<SmsState> smsStatePageList = page(model, hql);
        jq.setPage(smsStatePageList.getPageNo());
        jq.setRecords(smsStatePageList.getTotalCount());
        jq.setRows(smsStatePageList.getList());
        jq.setTotal(smsStatePageList.getPageCount());
        return jq;
    }

    public JqGridData<SmsState> findByNoticeId(JqPageModel model, Long id) {
        // List<Object[]> list =
        // dao.getHelperDao().find("select ss.id_f,ss.smsid_f,ss.recipient_f,ss.sendtime_f,ss.marktime_f,ss.mark_f,ss.report_f from smsstate_t ss where ss.smsid_f =(select s.smsid_f from smsstatecontent_t s where s.noticeId_f="
        // + id + ")");

        JqGridData<SmsState> jq = new JqGridData<SmsState>();
        String hql = "from "
                + SmsState.class.getName()
                + " s  where s.smsId in(select smsId from SmsStateContent where noticeId="
                + id + " )";
        // List<SmsStateContent> listContent = dao.find(SmsStateContent.class,
        // "noticeId", id);
        // if (listContent.size() == 0) {
        // return new JqGridData<SmsState>();
        // }
        // String smsids="";
        // for(SmsStateContent smss:listContent){
        // smsids+=smss.getSmsId()+",";
        // }
        PageList<SmsState> smsStatePageList = page(model, hql); // ,smsids.substring(0,
        // smsids.lastIndexOf(","))
        jq.setPage(smsStatePageList.getPageNo());
        jq.setRecords(smsStatePageList.getTotalCount());
        jq.setRows(smsStatePageList.getList());
        jq.setTotal(smsStatePageList.getPageCount());
        return jq;
    }

    /**
     * 总数统计
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public JqGridData<Map<String, String>> countTotal(JqPageModel model, String sqls, String sqlPush, String pushtable) {
        String sql = " select  min(ss.date_f),max(ss.date_f), " +
                "        count(ss.id_f) as total, " +
                "        sum(decode(sign((length(stc.content_f)-1))*(sign(length(stc.content_f)-57)-1),0,0,1)) one, " +
                "        sum(decode((sign((length(stc.content_f)-58))+1)*(sign(length(stc.content_f)-116)-1),0,0,-1,0,1)) two, " +
                "        sum(decode(sign((length(stc.content_f)-117)+1),0,0,-1,0,1)) three " +
                "  from smsstate_t ss,smsstatecontent_t stc " +
                " where stc.smsid_f=ss.smsid_f  " + sqls +
                " group by TO_CHAR(ss.date_f, 'IW') " +
                " order by TO_CHAR(ss.date_f, 'IW') desc";
        String sqlCount = " select * from (" + sql + ")";
        JqGridData<Map<String, String>> jq = new JqGridData<Map<String, String>>();
        PageList<Object[]> list = dao.getHelperDao().pageByTotal(
                model.getPage(), model.getRows() - 1, sql, sqlCount); //需要减1来存放统计记录
        jq.setPage(list.getPageNo());
        jq.setRecords(list.getTotalCount());
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        int counttotal = 0;
        int billingtotal = 0;
        int pushtotal = 0;
        for (Object[] objs : list.getList()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("start", objs[0].toString());
            map.put("end", objs[1].toString());
            map.put("count", objs[2].toString());
            int one = Integer.parseInt(String.valueOf(objs[3]));
            int two = Integer.parseInt(String.valueOf(objs[4]));
            int three = Integer.parseInt(String.valueOf(objs[5]));
            map.put("billing", (one + (two * 2) + three * 3) + "");
            if (StringUtil.isEmpty(pushtable)) {
                map.put("push", "");
            } else {
                List<Object> listObjs = dao.getHelperDao().find(
                        " select count(*)"
                                + "    from " + pushtable + " ns, notice_t t"
                                + "   where ns.notice_f = t.id_f"
                                + "   " + sqlPush + "  and  to_char(t.date_f, 'yyyy-MM-dd') >'"
                                + objs[0].toString() + "'"
                                + "     and to_char(t.date_f, 'yyyy-MM-dd') < '"
                                + objs[1].toString() + "'"
                                + "     and t.shortMsg_f = 0");
                map.put("push", listObjs.get(0).toString());
                pushtotal = pushtotal + Integer.parseInt(listObjs.get(0).toString());
            }
            mapList.add(map);
            counttotal = counttotal + Integer.parseInt(objs[2].toString());
            billingtotal = billingtotal + (one + (two * 2) + three * 3);
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("start", "当前页总计");
        map.put("count", String.valueOf(counttotal));
        map.put("billing", String.valueOf(billingtotal));
        if (StringUtil.isEmpty(pushtable)) {
            map.put("push", "");
        } else {
            map.put("push", String.valueOf(pushtotal));
        }
        mapList.add(map);
        jq.setRows(mapList);
        jq.setTotal(list.getPageCount());
        return jq;
    }

    public JqGridData<Map<String, String>> count(JqPageModel model, Long oid) {
        if (oid == null) {
            return countTotal(model, "", "", "Noticereceivestu_t");
        }
        if (oid == 0) {//查询老师
            return countTotal(model, "  and ss.recipient_f in(select tea.telePhone_f from teacherinfo_t tea)",
                    " and ns.teacherinfo_f in(select tea.telePhone_f from teacherinfo_t tea)", "NoticeReceiveTea_t");
        } else if (oid == -1) {//查询其他
            return countTotal(model, " and (stc.noticeid_f is null  and stc.scoreid_f is null)", null, null);
        }
        Org org = dao.findOne(Org.class, "id", oid);
        if (org.getType() == 0) {
            return countTotal(model, "", "", "Noticereceivestu_t");
        } else if (org.getType() == 1) {//阶段
            return countTotal(model, "and ss.recipient_f in (select stu.no_f from studentinfo_t stu where stu.org_f in( select id_f from org_t oo where oo.parent_f in(select o.id_f from org_t o where o.parent_f=" + oid + ")))",
                    "and ns.studentInfo_f in (select stu.id_f from studentinfo_t stu where stu.org_f in( select id_f from org_t oo where oo.parent_f in(select o.id_f from org_t o where o.parent_f=" + oid + ")))", "Noticereceivestu_t");
        } else if (org.getType() == 2) {//年级
            return countTotal(model, "and ss.recipient_f in (select stu.no_f from studentinfo_t stu where stu.org_f in(select o.id_f from org_t o where o.parent_f=" + oid + "))",
                    "and ns.studentInfo_f in (select stu.id_f from studentinfo_t stu where stu.org_f in(select o.id_f from org_t o where o.parent_f=" + oid + "))", "Noticereceivestu_t");
        } else if (org.getType() == 3) {//班级
            return countTotal(model, "and ss.recipient_f in (select s.no_f from studentinfo_t s where s.org_f=" + oid + ")",
                    "and ns.studentInfo_f in (select stu.id_f from studentinfo_t stu where stu.org_f=" + oid + ")", "Noticereceivestu_t");
        }
        return null;
    }
}
