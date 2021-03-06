package net.shinc.controller.edu.business;

import java.util.List;

import javax.validation.Valid;

import net.shinc.common.AbstractBaseController;
import net.shinc.common.ErrorMessage;
import net.shinc.common.IRestMessage;
import net.shinc.common.ShincUtil;
import net.shinc.orm.mybatis.bean.edu.Book;
import net.shinc.orm.mybatis.bean.edu.Parter;
import net.shinc.service.edu.business.BookService;
import net.shinc.service.edu.business.ParterService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: ParterController
 * @Description: 书商控制类
 * @author hushichong
 * @date 2015年9月16日 下午9:53:23
 */
@Controller
@RequestMapping(value = "/parter")
public class ParterController extends AbstractBaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ParterService parterService;
	@Autowired
	private BookService bookService;

	/**
	 * @Title: getParterList
	 * @Description: 得到书商列表，含书目数量，视频数量
	 * @param parter
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/getParterList")
	@ResponseBody
	public IRestMessage getParterList(Parter parter) {
		IRestMessage msg = getRestMessage();
		try {
			List<Parter> list = parterService.getParterList(parter);
			if (null != list && list.size() > 0) {
				msg.setCode(ErrorMessage.SUCCESS.getCode());
				msg.setResult(list);
			} else {
				msg.setCode(ErrorMessage.RESULT_EMPTY.getCode());
			}
		} catch (Exception e) {
			logger.error("书商列表查询失败==>" + ExceptionUtils.getStackTrace(e));
			msg.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return msg;
	}

	/**
	 * @Title: getParterBookList
	 * @Description: 得到书商_书列表
	 * @param parter
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/getParterBookList")
	@ResponseBody
	public IRestMessage getParterBookList(Parter parter) {
		IRestMessage msg = getRestMessage();
		try {
			Book book = new Book();
			book.setParterId(parter.getId());
			List list = bookService.getBookList(book);
			if (null != list && list.size() > 0) {
				msg.setCode(ErrorMessage.SUCCESS.getCode());
				msg.setResult(list);
			} else {
				msg.setCode(ErrorMessage.RESULT_EMPTY.getCode());
			}
		} catch (Exception e) {
			logger.error("书商列表查询失败==>" + ExceptionUtils.getStackTrace(e));
			msg.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return msg;
	}

	/**
	 * @Title: addParter
	 * @Description: 添加书商
	 * @param parter
	 * @param bindingResult
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/addParter")
	@ResponseBody
	public IRestMessage addParter(@Valid Parter parter, BindingResult bindingResult) {
		IRestMessage iRestMessage = getRestMessage();
		if (bindingResult.hasErrors()) {
			iRestMessage.setDetail(ShincUtil.getErrorFields(bindingResult));
			return iRestMessage;
		}
		try {
			parterService.addParter(parter);
			iRestMessage.setCode(ErrorMessage.ADD_SUCCESS.getCode());
			iRestMessage.setMessage("添加成功");
		} catch(DuplicateKeyException ee) {
			iRestMessage.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
			iRestMessage.setMessage("该书商已存在");
		} 
		catch (Exception e) {
			logger.error("添加书商失败==>" + ExceptionUtils.getStackTrace(e));
			iRestMessage.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return iRestMessage;
	}

	/**
	 * @Title: getParterInfo
	 * @Description: 得到书商信息
	 * @param id
	 * @param bindingResult
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/getParterInfo")
	@ResponseBody
	public IRestMessage getParterInfo(@RequestParam(value = "id") Integer id) {
		IRestMessage iRestMessage = getRestMessage();
		try {
			Parter parter = parterService.getParterById(id);
			iRestMessage.setCode(ErrorMessage.SUCCESS.getCode());
			iRestMessage.setResult(parter);
		} catch (Exception e) {
			logger.error("获得书商详细信息失败==>" + ExceptionUtils.getStackTrace(e));
			iRestMessage.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return iRestMessage;
	}

	/**
	 * @Title: updateParter
	 * @Description: 更新书商
	 * @param parter
	 * @param bindingResult
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/updateParter")
	@ResponseBody
	public IRestMessage updateParter(@Valid Parter parter, BindingResult bindingResult) {
		IRestMessage iRestMessage = getRestMessage();
		if (bindingResult.hasErrors()) {
			iRestMessage.setDetail(ShincUtil.getErrorFields(bindingResult));
			return iRestMessage;
		}
		try {
			parterService.updateParter(parter);
			iRestMessage.setCode(ErrorMessage.UPDATE_SUCCESS.getCode());
			iRestMessage.setMessage("修改成功");
		} catch (Exception e) {
			logger.error("更新书商失败==>" + ExceptionUtils.getStackTrace(e));
			iRestMessage.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return iRestMessage;
	}

	/**
	 * @Title: deleteParter
	 * @Description: 删除书商，有书则不支持删除
	 * @param id
	 * @param bindingResult
	 * @return IRestMessage
	 */
	@RequestMapping(value = "/deleteParter")
	@ResponseBody
	public IRestMessage deleteParter(@RequestParam(value = "id") Integer id) {
		IRestMessage iRestMessage = getRestMessage();
		try {
			Parter parter = new Parter();
			parter.setId(id);

			if (parterService.isHasBook(parter)) {
				iRestMessage.setCode(ErrorMessage.DELETE_FAILED.getCode());
				iRestMessage.setMessage("该书商下已有书存在暂不支持删除");
				return iRestMessage;
			}
			parterService.deleteParterById(parter.getId());
			iRestMessage.setCode(ErrorMessage.SUCCESS.getCode());
			iRestMessage.setMessage("删除成功");
		} catch (Exception e) {
			logger.error("删除书商失败==>" + ExceptionUtils.getStackTrace(e));
			iRestMessage.setCode(ErrorMessage.ERROR_DEFAULT.getCode());
		}
		return iRestMessage;
	}
}
