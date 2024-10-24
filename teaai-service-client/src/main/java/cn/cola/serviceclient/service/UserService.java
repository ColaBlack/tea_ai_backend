package cn.cola.serviceclient.service;

import cn.cola.common.common.ErrorCode;
import cn.cola.common.common.exception.BusinessException;
import cn.cola.common.constant.CommonConstant;
import cn.cola.common.constant.UserConstant;
import cn.cola.common.utils.SqlUtils;
import cn.cola.model.enums.UserRoleEnum;
import cn.cola.model.po.User;
import cn.cola.model.user.UserQueryRequest;
import cn.cola.model.vo.LoginUserVO;
import cn.cola.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 用户服务
 *
 * @author ColaBlack
 */
@FeignClient(name = "teaai-user", url = "/api/user")
public interface UserService {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    @GetMapping("/register")
    long userRegister(@RequestParam(value = "userAccount") String userAccount,
                      @RequestParam(value = "userPassword") String userPassword,
                      @RequestParam(value = "checkPassword") String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    @GetMapping("/login")
    LoginUserVO userLogin(@RequestParam(value = "userAccount") String userAccount,
                          @RequestParam(value = "userPassword") String userPassword);

    @GetMapping("/inner/get/id")
    User getById(@RequestParam(value = "id") Long id);

    @PutMapping("/inner/save")
    boolean save(@RequestBody User user);

    @DeleteMapping("/inner/remove/id")
    boolean removeById(@RequestParam(value = "id") Long id);

    @PutMapping("/inner/update/id")
    boolean updateById(@RequestBody User user);

    @GetMapping("/inner/page/queryWrapper")
    Page<User> page(@RequestParam(value = "page") Page<User> page,
                    @RequestParam(value = "queryWrapper") QueryWrapper<User> queryWrapper);


    /**
     * 获取当前登录用户
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     */
    default boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    /**
     * 是否为管理员
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     */
    default boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取脱敏的已登录用户信息
     */
    default LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取脱敏的用户信息
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取查询条件
     */
    default QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "user_profile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @PostMapping("/inner/list/ids")
    List<User> listByIds(@RequestBody Set<Long> ids);
}
