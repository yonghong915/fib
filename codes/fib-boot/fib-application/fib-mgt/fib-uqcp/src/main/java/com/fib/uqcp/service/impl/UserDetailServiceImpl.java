package com.fib.uqcp.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.uqcp.entity.Role;
import com.fib.uqcp.entity.SysUser;
import com.fib.uqcp.entity.UserRole;
import com.fib.uqcp.mapper.SysUserMapper;
import com.fib.uqcp.mapper.UserRoleMapper;
import com.fib.uqcp.mapper.RoleMapper;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	SysUserMapper sysUserMapper;
	@Autowired
	UserRoleMapper userRoleMapper;
	private final RoleMapper roleMapper = null;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * @Author HJJ
	 * @Date 2022-12-27 9:43
	 * @Params String username
	 * @Return User
	 * @Description SpringSecurity方法，验证并获取用户权限信息，并将User数据写入Redis缓存
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String cacheUser = stringRedisTemplate.opsForValue().get("username::" + username);
		// 如果有缓存，直接返回，如果没有缓存，否则需要查询数据库并写入Redis缓存
//		if (!Objects.isNull(cacheUser)) {
//			return JSON.parseObject(cacheUser, UserDetails.class);
//		} else {
			SysUser userEntity = getUserEntityByUsername(username);
			User user = new User(username, userEntity.getPassword(), getAuthoritiesByUserId(userEntity.getUserId()));
//			String strUser = JSONObject.toJSONString(user);
//			System.out.println(username + "查询了数据库并写入Redis缓存：" + strUser);
//			// 将User对象转换成String并写入缓存
//			stringRedisTemplate.opsForValue().set("username::" + username, strUser, 60, TimeUnit.SECONDS);
			return user;
	//	}
	}

	private List<GrantedAuthority> getAuthoritiesByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Author HJJ
	 * @Date 2022-12-27 9:42
	 * @Params
	 * @Return
	 * @Description 查询数据库，获取用户信息
	 */
	public SysUser getUserEntityByUsername(String username) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("userName", username);
		SysUser userEntity = sysUserMapper.selectOne(queryWrapper);
		if (Objects.isNull(userEntity)) {
			System.out.println("没有此用户:" + username);
			throw new UsernameNotFoundException("没有此用户");
		}
		return userEntity;
	}

	/**
	 * @Author HJJ
	 * @Date 2022-12-27 9:55
	 * @Params
	 * @Return
	 * @Description 查询数据库，获取用户权限信息
	 */
	public List<GrantedAuthority> getAuthoritiesByUserId(int userId) {
		StringBuilder roles = new StringBuilder();
		QueryWrapper<UserRole> queryWrapper1 = new QueryWrapper<>();
		queryWrapper1.eq("userId", userId);
		List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper1);
		for (int i = 0; i < userRoles.size(); i++) {
			UserRole userRole = userRoles.get(i);
			String roleId = userRole.getRoleId();
			QueryWrapper<Role> queryWrapper2 = new QueryWrapper<>();
			queryWrapper2.eq("roleId", roleId);
			Role role = roleMapper.selectOne(queryWrapper2);
			String roleName = role.getRoleName();
			if (Objects.equals(userRoles.size() - 1, i)) {
				roles.append(roleName);
				break;
			}
			roles.append(roleName).append(",");
		}
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles.toString());
	}
}