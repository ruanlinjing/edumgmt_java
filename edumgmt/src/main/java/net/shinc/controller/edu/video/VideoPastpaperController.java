package net.shinc.controller.edu.video;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import net.shinc.common.AbstractBaseController;
import net.shinc.common.ErrorMessage;
import net.shinc.common.IRestMessage;
import net.shinc.common.ShincUtil;
import net.shinc.formbean.edu.video.VideoPastpaperQueryBean;
import net.shinc.orm.mybatis.bean.edu.VideoDetail;
import net.shinc.orm.mybatis.bean.edu.VideoPastpaper;
import net.shinc.orm.mybatis.bean.edu.VideoPic;
import net.shinc.service.common.QNService;
import net.shinc.service.edu.video.VideoBaseService;
import net.shinc.service.edu.video.VideoDetailService;
import net.shinc.service.edu.video.VideoPastpaperService;
import net.shinc.service.edu.video.VideoPicService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * @ClassName: VideoPastpaperController
 * @Description: 真题、模拟题具体信息控制层
 * @author hushichong
 * @date 2015年8月4日 下午4:08:02
 */
@Controller
@RequestMapping(value = "/videoPastpaper")
public class VideoPastpaperController extends AbstractBaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${page.count}")
	private String limit;

	@Autowired
	private VideoPastpaperService videoPastpaperService;
	
	@Autowired
	private VideoDetailService videoDetailService;
	
	@Autowired
	private VideoBaseService videoBaseService;
	
	@Autowired
	private VideoPicService videoPicService;
	
	@Autowired
	private QNService qnservice;
	
	//视频和截图所在空间域名
	@Value("${qiniu.eduonline.domain}")
	private String domain;
	
	//视频和截图所在空间名称
	@Value("${qiniu.eduonline.bucketName}")
	private String bucketName;
	
	@Value("${qiniu.eduonline.deadline}")
	private String expires;
	
	/**
	* @Title: getVideoPastpaperAndRelevantInfoList
	* @Description: 查询视频相关信息列表
	* @param videoPastpaper
	* @param page
	* @param pageSize
	* @return  IRestMessage
	 */
	@RequestMapping(value = "/getVideoPastpaperAndRelevantInfoList")
	@ResponseBody
	public IRestMessage getVideoPastpaperAndRelevantInfoList(VideoPastpaperQueryBean videoPastpaper,
			@RequestParam("page") Integer page,
			@RequestParam("pageSize") Integer pageSize) {
		IRestMessage msg = getRestMessage();
		try {
			
			if(page == null || page < 1) {
				page = 1;
			}
			if(pageSize == null || pageSize < 1) {
				pageSize = Integer.parseInt(limit);
			}
			PageBounds pb = new PageBounds(page,pageSize);
			List<Map> list = videoPastpaperService.getVideoPastpaperAndRelevantInfoList(videoPastpaper,pb);
			
			if (null != list && list.size() > 0 && list.get(0) != null) {
				msg.setCode(ErrorMessage.SUCCESS.getCode());
				msg.setResult(list);
				msg.setPageInfo(((PageList)list).getPaginator());
			} else {
				msg.setCode(ErrorMessage.RESULT_EMPTY.getCode());
			}
		} catch (Exception e) {
			logger.error("真题模拟题及相关信息列表查询失败==>" + ExceptionUtils.getStackTrace(e));
		}
		return msg;
	}

	/**
	* @Title: addVideoPastpaperAndRelevantInfo
	* @Description: 新增视频相关信息
	* @param videoPastpaper
	* @param bindingResult
	* @param locale
	* @return  IRestMessage
	 */
	@RequestMapping(value = "/addVideoPastpaperAndRelevantInfo")
	@ResponseBody
	public IRestMessage addVideoPastpaperAndRelevantInfo( @Valid VideoPastpaper videoPastpaper, BindingResult bindingResult, Locale locale) {

		IRestMessage iRestMessage = getRestMessage();
		if (bindingResult.hasErrors()) {
			iRestMessage.setCode(ErrorMessage.ERROR_PARAM_ERROR.getCode());
			iRestMessage.setDetail(ShincUtil.getErrorFields(bindingResult));
			return iRestMessage;
		}
		try {
			videoPastpaperService.insertVideoPastpaper(videoPastpaper);
			iRestMessage.setCode(ErrorMessage.SUCCESS.getCode());
			
			Integer vbid = videoPastpaper.getVideoBaseId();
			
			videoBaseService.generateQRCodeAndUpload(vbid);
			iRestMessage.setResult(vbid);
		} catch (Exception e) {
			logger.error("添加真题/模拟题视频详细信息失败==>" + ExceptionUtils.getStackTrace(e));
			iRestMessage.setCode(ErrorMessage.ADD_FAILED.getCode());
		}
		return iRestMessage;
	}


	@RequestMapping(value = "/getVideoPastpaperAndRelevantInfo")
	@ResponseBody
	public IRestMessage getVideoPastpaperAndRelevantInfo(VideoPastpaperQueryBean videoPastpaperQuery) {
		IRestMessage iRestMessage = getRestMessage();
		try {

			List<Map> list = videoPastpaperService.getVideoPastpaperAndRelevantInfoList(videoPastpaperQuery,new PageBounds());
			if (list != null && list.size() > 0 && list.get(0) != null) {
				iRestMessage.setCode(ErrorMessage.SUCCESS.getCode());
				iRestMessage.setResult(list.get(0));
			} else {
				iRestMessage.setCode(ErrorMessage.RESULT_EMPTY.getCode());
			}
		} catch (Exception e) {
			logger.error("获得真题/模拟题视频详细信息失败==>" + ExceptionUtils.getStackTrace(e));
		}
		return iRestMessage;
	}
	/**
	* @Title: updateVideoPastpaperAndRelevantInfo
	* @Description: 更新视频相关信息
	* @param videoPastpaper
	* @param bindingResult
	* @param locale
	* @return  IRestMessage
	 */
	@RequestMapping(value = "/updateVideoPastpaperAndRelevantInfo")
	@ResponseBody
	public IRestMessage updateVideoPastpaperAndRelevantInfo(@Valid VideoPastpaper videoPastpaper, BindingResult bindingResult, Locale locale) {
		IRestMessage iRestMessage = getRestMessage();
		if (bindingResult.hasErrors()) {
			iRestMessage.setDetail(ShincUtil.getErrorFields(bindingResult));
			return iRestMessage;
		}	
		try {
			videoPastpaperService.updateVideoPastpaper(videoPastpaper);
			iRestMessage.setCode(ErrorMessage.SUCCESS.getCode());
			
			//result放入视频和截图信息
			Map<String,Object> resultMap = new HashMap<String,Object>();
			Integer videoBaseId = videoPastpaper.getVideoBase().getId();
			List<VideoDetail> list = videoDetailService.getVideoDetailListByVideoBaseId(videoBaseId);
			List<VideoPic> pic = videoPicService.selectPicByVideoBaseId(videoBaseId, domain, Long.parseLong(expires));
			resultMap.put("video", list);
			resultMap.put("pic", pic);
			resultMap.put("videoBaseId", videoBaseId);
			iRestMessage.setResult(resultMap);
			
		} catch (Exception e) {
			logger.error("更新真题/模拟题视频失败==>" + ExceptionUtils.getStackTrace(e));
		}
		return iRestMessage;
	}
	
}
