package net.shinc.service.common.impl;

import java.util.Date;
import java.util.List;

import net.shinc.common.ShincUtil;
import net.shinc.orm.mybatis.bean.common.AdminUser;
import net.shinc.orm.mybatis.bean.common.AdminUserHasAuthGroup;
import net.shinc.orm.mybatis.bean.common.Authority;
import net.shinc.orm.mybatis.bean.common.AuthorityGroup;
import net.shinc.orm.mybatis.bean.common.Company;
import net.shinc.orm.mybatis.mappers.common.AdminUserHasAuthGroupMapper;
import net.shinc.orm.mybatis.mappers.common.AdminUserMapper;
import net.shinc.orm.mybatis.mappers.common.CompanyMapper;
import net.shinc.service.common.AdminUserService;
import net.shinc.service.common.MenuService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

/**
 * @ClassName AdminUserServiceImpl 
 * @Description 后台管理用户接口实现类
 * @author guoshijie 
 * @date 2015年7月14日 上午11:53:24
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserMapper adminUserMapper;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private AdminUserHasAuthGroupMapper adminUserHasAuthGroupMapper;
	
	@Autowired
	private MenuService menuService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static String pattern = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public PageList<AdminUser> getAdminUserList(PageBounds pageBounds,Company company) {
		if(null != company) {
			List<AdminUser> list = adminUserMapper.getAdminUserList(company,pageBounds);
			PageList<AdminUser> pageList = (PageList<AdminUser>)list;
			return pageList;
		}
		return null;
	}

	@Override
	public Integer getAdminUserListCount() {
		return adminUserMapper.getAdminUserListCount();
	}

	@Override
	public Integer addAdminUser(AdminUser adminUser) {
		if (null != adminUser) {
			try {
				adminUser.setCreateTime(ShincUtil.formatDate(new Date(), pattern));
				adminUser.setUpdateTime(ShincUtil.formatDate(new Date(), pattern));
				return adminUserMapper.insert(adminUser);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return 0;
	}

	@Override
	public Integer deleteAdminUser(AdminUser adminUser) {
		if(null != adminUser){
			//删除权限  待完成
			return adminUserMapper.deleteByPrimaryKey(adminUser.getId());
		}
		return 0;
	}

	@Override
	public Integer updateAdminUser(AdminUser adminUser) {
		if(null != adminUser){
			adminUser.setUpdateTime(ShincUtil.formatDate(new Date(), pattern));
			return adminUserMapper.updateByPrimaryKeySelective(adminUser);
		}
		return 0;
	}

	@Override
	public AdminUser getAdminUserById(AdminUser adminUser) {
		if(null != adminUser ) {
			AdminUser admin = adminUserMapper.getAdminUserById(adminUser);
			if(null != admin) {
//				adminUser.setMenuMap(menuService.getMenu(admin));
			}
			return admin;
		}
		return null;
	}

	@Override
	public PageList<AdminUser> getAdminUserByCompany(Company company, PageBounds pageBounds) {
		List<AdminUser> list = adminUserMapper.getAdminUserByCompany(company.getId(), pageBounds);
		PageList<AdminUser> pageList = (PageList<AdminUser>)list;
		return pageList;
	}

	@Override
	public AdminUser getAdminUserByNickName(String nickname) {
		if(null != nickname) {
			AdminUser admin = adminUserMapper.getAdminUserByNickName(nickname);
			if(null != admin){
				admin.setMenuMap(menuService.getMenu(admin));
			}
			return admin;
		}
		return null;
	}

	@Override
	public Integer addAuthGroupForUser(AdminUser adminUser, AuthorityGroup authGroup) {
		if (null != adminUser && null != authGroup) {
			return adminUserHasAuthGroupMapper.addAdminUserHasAuthGroup(new AdminUserHasAuthGroup(adminUser, authGroup));
		}
		return 0;
	}

	@Override
	public List<AuthorityGroup> getAuthGroup(AdminUser adminUser) {
		if(null != adminUser){
			List<AuthorityGroup> list = adminUserHasAuthGroupMapper.getAuthGroup(adminUser);
			return list;
		}
		return null;
	}
	
	@Override
	public List<Authority> getAuthList(AdminUser adminUser) {
		if (null != adminUser) {
			List<AuthorityGroup> authGroup = getAuthGroup(adminUser);
			if (null != authGroup) {
				//待完成
			}
		}
		return null;
	}
	
}
