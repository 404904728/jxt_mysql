package app.cq.hmq.controller.sms;

import app.cq.hmq.pojo.notice.SmsState;
import app.cq.hmq.service.sms.SmsManageService;
import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: HJ
 * Date: 14-8-26
 * Time: 下午4:18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/smsManage")
public class SmsManageController {

    @RequestMapping(params = "page")
    public ModelAndView page() {
        ModelAndView modelAndView = new ModelAndView("/app/system/smspage");
        return modelAndView;
    }

    /**
     * 短信统计页面跳转
     *
     * @return
     */
    @RequestMapping(params = "countpage")
    public ModelAndView countpage() {
        //ModelAndView modelAndView = new ModelAndView("/app/system/counttotalpage");
        ModelAndView modelAndView = new ModelAndView("/app/system/countpage");
        return modelAndView;
    }

    @RequestMapping(params = "count", method = RequestMethod.POST)
    @ResponseBody
    public JqGridData<Map<String, String>> count(JqPageModel model, Long oid) {
        return smsManageService.count(model, oid);
        //return smsManageService.countTotal(model);
    }


    @RequestMapping(params = "showdetail")
    public ModelAndView showdetail(Long id) {
        ModelAndView modelAndView = new ModelAndView("/app/system/smsdetailpage");
        modelAndView.addObject("snId", id);
        return modelAndView;
    }


    /**
     * 查询数据
     *
     * @param model
     * @return
     */
    @RequestMapping(params = "findDataDetail", method = RequestMethod.POST)
    @ResponseBody
    public JqGridData<SmsState> findDataDetail(JqPageModel model, Long id) {
        return smsManageService.findByNoticeId(model, id);
    }


    @Autowired
    private SmsManageService smsManageService;

    /**
     * 查询数据
     *
     * @param model
     * @return
     */
    @RequestMapping(params = "findData", method = RequestMethod.POST)
    @ResponseBody
    public JqGridData<SmsState> findData(JqPageModel model) {
        return smsManageService.findAll(model);
    }
}
