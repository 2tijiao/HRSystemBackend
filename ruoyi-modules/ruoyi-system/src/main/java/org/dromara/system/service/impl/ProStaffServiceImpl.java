package org.dromara.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;

import org.dromara.system.domain.*;
import org.dromara.system.domain.bo.ProStaffBo;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.ProStaffVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.*;
import org.dromara.system.service.IProStaffService;
import org.dromara.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工档案Service业务层处理
 *
 * @author Lion Li
 * @date 2025-11-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ProStaffServiceImpl implements IProStaffService {

    private final ProStaffMapper baseMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysPostMapper sysPostMapper;
    private final ProStaffEmnumberMapper proStaffEmnumberMapper;
    private final ISysUserService sysUserService;
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper userRoleMapper;


    /**
     * 查询员工档案
     *
     * @param proStaffId 主键
     * @return 员工档案
     */
    @Override
    public ProStaff queryById(Long proStaffId){
        return baseMapper.selectById(proStaffId);
    }

    /**
     * 分页查询员工档案列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工档案分页列表
     */
    @Override
    public TableDataInfo<ProStaffVo> queryPageList(ProStaffBo bo, PageQuery pageQuery) {
        // 构建查询条件
        LambdaQueryWrapper<ProStaff> lqw = buildQueryWrapper(bo);
        // 执行分页查询
        Page<ProStaffVo> result = baseMapper.selectPageStaff(pageQuery.build(), lqw);
        List<ProStaffVo> records = result.getRecords();
        // 2. 如果结果为空，直接返回
        if (CollUtil.isEmpty(records)) {
            return TableDataInfo.build(result);
        }
        // 3. 提取所有的 DeptId 和 PostId (利用 Java Stream 流)
        Set<Long> deptIds = records.stream()
            .map(ProStaffVo::getDeptId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        Set<Long> postIds = records.stream()
            .map(ProStaffVo::getPostId)
            .filter(Objects::nonNull) // 如果PostId是String类型，用 StringUtils::isNotBlank
            .collect(Collectors.toSet());
        // 4. 批量查询数据库获取 Map 映射
        Map<Long, String> deptMap = Map.of();
        if (CollUtil.isNotEmpty(deptIds)) {
            // 查询部门表，转换成 Map<id, name>
            List<SysDept> depts = sysDeptMapper.selectByIds(deptIds);
            if (CollUtil.isNotEmpty(depts)) {
                deptMap = depts.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
            }
        }
        Map<Long, String> postMap = Map.of();
        if (CollUtil.isNotEmpty(postIds)) {
            // 查询岗位表，转换成 Map<id, name>
            List<SysPost> posts = sysPostMapper.selectByIds(postIds);
            if (CollUtil.isNotEmpty(posts)) {
                postMap = posts.stream().collect(Collectors.toMap(SysPost::getPostId, SysPost::getPostName));
            }
        }
        // 5. 遍历结果集进行赋值
        // 为了在 lambda 表达式中使用，重新赋值给 final 变量
        final Map<Long, String> finalDeptMap = deptMap;
        final Map<Long, String> finalPostMap = postMap;
        records.forEach(vo -> {
            // 填充部门名称
            if (vo.getDeptId() != null) {
                vo.setDeptName(finalDeptMap.get(vo.getDeptId()));
            }
            // 填充岗位名称
            if (vo.getPostId() != null) {
                vo.setPostName(finalPostMap.get(vo.getPostId()));
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工档案列表
     *
     * @param bo 查询条件
     * @return 员工档案列表
     */
    @Override
    public List<ProStaffVo> queryList(ProStaffBo bo) {
        LambdaQueryWrapper<ProStaff> lqw = buildQueryWrapper(bo);
        List<ProStaffVo> records = baseMapper.selectVoList(lqw);
        // 2. 如果结果为空，直接返回
        if (CollUtil.isEmpty(records)) {
            return records;
        }
        // 3. 提取所有的 DeptId 和 PostId (利用 Java Stream 流)
        Set<Long> deptIds = records.stream()
            .map(ProStaffVo::getDeptId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        Set<Long> postIds = records.stream()
            .map(ProStaffVo::getPostId)
            .filter(Objects::nonNull) // 如果PostId是String类型，用 StringUtils::isNotBlank
            .collect(Collectors.toSet());
        // 4. 批量查询数据库获取 Map 映射
        Map<Long, String> deptMap = Map.of();
        if (CollUtil.isNotEmpty(deptIds)) {
            // 查询部门表，转换成 Map<id, name>
            List<SysDept> depts = sysDeptMapper.selectByIds(deptIds);
            if (CollUtil.isNotEmpty(depts)) {
                deptMap = depts.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
            }
        }
        Map<Long, String> postMap = Map.of();
        if (CollUtil.isNotEmpty(postIds)) {
            // 查询岗位表，转换成 Map<id, name>
            List<SysPost> posts = sysPostMapper.selectByIds(postIds);
            if (CollUtil.isNotEmpty(posts)) {
                postMap = posts.stream().collect(Collectors.toMap(SysPost::getPostId, SysPost::getPostName));
            }
        }
        // 5. 遍历结果集进行赋值
        // 为了在 lambda 表达式中使用，重新赋值给 final 变量
        final Map<Long, String> finalDeptMap = deptMap;
        final Map<Long, String> finalPostMap = postMap;
        records.forEach(vo -> {
            // 填充部门名称
            if (vo.getDeptId() != null) {
                vo.setDeptName(finalDeptMap.get(vo.getDeptId()));
            }
            // 填充岗位名称
            if (vo.getPostId() != null) {
                vo.setPostName(finalPostMap.get(vo.getPostId()));
            }
        });
        return records;
    }

    private LambdaQueryWrapper<ProStaff> buildQueryWrapper(ProStaffBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProStaff> lqw = Wrappers.lambdaQuery();
        // 先按 is_frozen 排序：0（未冻结）在上面，1（冻结）在下面
        lqw.orderByAsc(ProStaff::getIsFrozen);
        lqw.orderByDesc(ProStaff::getIsSupervisor);
        // 再按创建时间从旧到新排序（升序）
        lqw.orderByDesc(ProStaff::getUpdateTime);
        lqw.orderByDesc(ProStaff::getCreateTime);
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProStaff::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getEnglishName()), ProStaff::getEnglishName, bo.getEnglishName());
        lqw.eq(StringUtils.isNotBlank(bo.getIdCardNumber()), ProStaff::getIdCardNumber, bo.getIdCardNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getEmployeeNumber()), ProStaff::getEmployeeNumber, bo.getEmployeeNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPhonenumber()), ProStaff::getPhonenumber, bo.getPhonenumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPhonenumber1()), ProStaff::getPhonenumber1, bo.getPhonenumber1());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), ProStaff::getStatus, bo.getStatus());
        lqw.eq(bo.getDeptId() !=null, ProStaff::getDeptId, bo.getDeptId());
        lqw.eq(bo.getBasicSalary() != null, ProStaff::getBasicSalary, bo.getBasicSalary());
        lqw.eq(bo.getActualSalary() != null, ProStaff::getActualSalary, bo.getActualSalary());
        lqw.eq(bo.getSalaryCost() != null, ProStaff::getSalaryCost, bo.getSalaryCost());
        lqw.eq(StringUtils.isNotBlank(bo.getIsMainPost()), ProStaff::getIsMainPost, bo.getIsMainPost());
        lqw.eq(bo.getPostId()!=null, ProStaff::getPostId, bo.getPostId());
        lqw.eq(bo.getHireDate() != null, ProStaff::getHireDate, bo.getHireDate());
        lqw.eq(bo.getDimissionDate() != null, ProStaff::getDimissionDate, bo.getDimissionDate());
        lqw.eq(StringUtils.isNotBlank(bo.getPosition()), ProStaff::getPosition, bo.getPosition());
        lqw.eq(StringUtils.isNotBlank(bo.getIsSupervisor()), ProStaff::getIsSupervisor, bo.getIsSupervisor());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), ProStaff::getEmail, bo.getEmail());
        lqw.eq(StringUtils.isNotBlank(bo.getExtensionNumber()), ProStaff::getExtensionNumber, bo.getExtensionNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getOfficeLocation()), ProStaff::getOfficeLocation, bo.getOfficeLocation());
        lqw.eq(StringUtils.isNotBlank(bo.getIsFrozen()), ProStaff::getIsFrozen, bo.getIsFrozen());
        return lqw;
    }



    /**
     * 根据身份证号码和部门id确定某一条档案
     * @param idCardNumber 身份证号
     * @param deptId 部门id
     * @return 获取到的档案信息
     */
    public ProStaff queryByPhoneNumAndDeptId(
        @Size(min = 0, max = 18, message = "身份证长度不能超过{max}个字符") String idCardNumber,
        Long deptId,
        String phonenumber) {
        ProStaff result = null;
        // 1. 优先逻辑：尝试使用 手机号码 + 部门ID 进行匹配
        if (StringUtils.isNotBlank(phonenumber)) {
            LambdaQueryWrapper<ProStaff> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(ProStaff::getPhonenumber, phonenumber)
                .eq(ProStaff::getDeptId, deptId)
                .eq(ProStaff::getIsFrozen, "0") // 仅查询未冻结的
                .orderByDesc(ProStaff::getUpdateTime)
                .orderByDesc(ProStaff::getCreateTime)
                .last("LIMIT 1");

            result = baseMapper.selectOne(phoneWrapper);
        }
        // 2. 降级逻辑：如果手机号未查到记录（result为null），且身份证号不为空，则尝试使用 身份证号 + 部门ID 匹配
        if (result == null && StringUtils.isNotBlank(idCardNumber)) {
            LambdaQueryWrapper<ProStaff> idCardWrapper = new LambdaQueryWrapper<>();
            idCardWrapper.eq(ProStaff::getIdCardNumber, idCardNumber)
                .eq(ProStaff::getDeptId, deptId)
                .eq(ProStaff::getIsFrozen, "0") // 仅查询未冻结的
                .orderByDesc(ProStaff::getUpdateTime)
                .orderByDesc(ProStaff::getCreateTime)
                .last("LIMIT 1");
            result = baseMapper.selectOne(idCardWrapper);
        }
        return result;
    }



    /**
     * 修改员工档案
     *
     * @param bo 员工档案
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateByBo(ProStaffBo bo) {
        ProStaff proStaff = BeanUtil.copyProperties(bo, ProStaff.class);
        try {
            // 获取原始记录
            ProStaff originalStaff = baseMapper.selectById(bo.getProStaffId());
            if (originalStaff == null) {
                return R.fail("未找到对应的员工记录");
            }
            // 判断员工号是否发生变化
            boolean employeeNumberChanged = !Objects.equals(originalStaff.getEmployeeNumber(), bo.getEmployeeNumber());
            // 2、如果修改了员工号，需要检查唯一性并更新映射表
            if (employeeNumberChanged && StringUtils.isNotBlank(bo.getEmployeeNumber())) {
                // 检查新员工号在pro_staff_emnumber中是否存在
                LambdaQueryWrapper<ProStaffEmnumber> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ProStaffEmnumber::getEmployeeNumber, bo.getEmployeeNumber());
                ProStaffEmnumber existingEmnumber = proStaffEmnumberMapper.selectOne(queryWrapper);
                // 如果存在且不是当前员工的记录，返回错误
                if (existingEmnumber != null) {
                    return R.fail(2001, "该员工号已存在");
                }
                // 更新pro_staff_emnumber表中身份证号和员工号对应的记录
                LambdaQueryWrapper<ProStaffEmnumber> emnumberQueryWrapper = new LambdaQueryWrapper<>();
                emnumberQueryWrapper.eq(ProStaffEmnumber::getIdCardNumber, bo.getIdCardNumber());
                ProStaffEmnumber existingIdCardRecord = proStaffEmnumberMapper.selectOne(emnumberQueryWrapper);
                if (existingIdCardRecord != null) {
                    // 更新已存在的记录
                    existingIdCardRecord.setEmployeeNumber(bo.getEmployeeNumber());
                    proStaffEmnumberMapper.updateById(existingIdCardRecord);
                } else {
                    // 插入新记录
                    ProStaffEmnumber newEmnumber = new ProStaffEmnumber();
                    newEmnumber.setIdCardNumber(bo.getIdCardNumber());
                    newEmnumber.setEmployeeNumber(bo.getEmployeeNumber());
                    proStaffEmnumberMapper.insert(newEmnumber);
                }
            }
            boolean deptIdChanged = !Objects.equals(originalStaff.getDeptId(), bo.getDeptId());
            if (deptIdChanged && bo.getDeptId()!=null) {
                SysDept dept = sysDeptMapper.selectById(bo.getDeptId());
                if (dept != null) {
                     // 假设部门对象中有getDeptNumber方法
                    // 生成loginNumber，这里假设loginNumber的生成规则，您可以根据实际需求调整
                   proStaff.setDeptNumber(dept.getDeptNumber());
                   proStaff.setDeptLevel(sysDeptMapper.getLevelById(proStaff.getDeptId()));
                } else {
                    return R.fail("未找到对应的部门信息");
                }
            }
            // 3、更新pro_staff表
            proStaff.setLoginNumber(proStaff.getDeptNumber()+proStaff.getEmployeeNumber());
            int update = baseMapper.updateById(proStaff);
            //4、更新用户表
            SysUser sysUser = BeanUtil.copyProperties(proStaff, SysUser.class);
            sysUser.setUserName(proStaff.getLoginNumber());
            sysUser.setUserId(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, bo.getLoginNumber())).getUserId());
            sysUserMapper.updateById(sysUser);
            if(proStaff.getIsSupervisor().equals("1"))userRoleMapper.update(new LambdaUpdateWrapper<SysUserRole>()
                .set(SysUserRole::getRoleId,3L)
                .eq(SysUserRole::getUserId, sysUser.getUserId()));
            if (update > 0) {
                return R.ok();
            } else {
                return R.fail("员工档案更新失败");
            }
        } catch (Exception e) {
            log.error("更新员工档案失败：", e);
            return R.fail("员工档案更新失败：" + e.getMessage());
        }
    }


    /**
     * 校验并批量删除员工档案信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    // 假设你需要先注入用户表的Mapper
// @Autowired
// private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        try {
            // 1. 根据ids查询ProStaff列表
            List<ProStaff> proStaffList = baseMapper.selectList(
                new LambdaQueryWrapper<ProStaff>()
                    .in(ProStaff::getProStaffId, ids)
            );

            if (proStaffList.isEmpty()) {
                return false;
            }
            // ==================== 处理身份证号逻辑 (原有的) ====================
            // 2. 提取所有的身份证号码
            List<String> idCardNumbers = proStaffList.stream()
                .map(ProStaff::getIdCardNumber)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
            // 3. 批量查询这些身份证号是否在其他记录中使用
            if (!idCardNumbers.isEmpty()) {
                // 查询所有使用这些身份证号的记录（包括要删除的和保留的）
                List<ProStaff> allRecordsWithSameIdCard = baseMapper.selectList(
                    new LambdaQueryWrapper<ProStaff>()
                        .in(ProStaff::getIdCardNumber, idCardNumbers)
                        .select(ProStaff::getIdCardNumber, ProStaff::getProStaffId)
                );
                // 按身份证号分组，统计每个身份证号对应的记录ID
                Map<String, List<Long>> idCardToStaffIds = allRecordsWithSameIdCard.stream()
                    .collect(Collectors.groupingBy(
                        ProStaff::getIdCardNumber,
                        Collectors.mapping(ProStaff::getProStaffId, Collectors.toList())
                    ));
                // 找出需要删除的身份证号（即所有使用该身份证号的记录都在要删除的ids中）
                List<String> idCardNumbersToDelete = idCardToStaffIds.entrySet().stream()
                    .filter(entry -> ids.containsAll(entry.getValue())) // 关键判断：所有关联ID都在删除列表中
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
                // 4. 删除prostaffemnumber表中对应的行
                if (!idCardNumbersToDelete.isEmpty()) {
                    int deletedCount = proStaffEmnumberMapper.delete(
                        new LambdaQueryWrapper<ProStaffEmnumber>()
                            .in(ProStaffEmnumber::getIdCardNumber, idCardNumbersToDelete)
                    );
                    log.info("删除员工编码表记录 {} 条，身份证号: {}", deletedCount, idCardNumbersToDelete);
                }
            }
            // ==================== 新增：处理 LoginNumber (用户账号) 逻辑 ====================
            // A. 提取所有的 LoginNumber
            List<String> loginNumbers = proStaffList.stream()
                .map(ProStaff::getLoginNumber) // 假设ProStaff类中有 getLoginNumber 方法
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
            // B. 批量查询这些账号是否在其他档案记录中使用
            if (!loginNumbers.isEmpty()) {
                // B1. 查询所有使用这些LoginNumber的档案记录（只查LoginNumber和ID即可）
                List<ProStaff> allRecordsWithSameLoginNum = baseMapper.selectList(
                    new LambdaQueryWrapper<ProStaff>()
                        .in(ProStaff::getLoginNumber, loginNumbers)
                        .select(ProStaff::getLoginNumber, ProStaff::getProStaffId)
                );
                // B2. 按 LoginNumber 分组，统计每个账号对应的档案ID列表
                Map<String, List<Long>> loginNumToStaffIds = allRecordsWithSameLoginNum.stream()
                    .collect(Collectors.groupingBy(
                        ProStaff::getLoginNumber,
                        Collectors.mapping(ProStaff::getProStaffId, Collectors.toList())
                    ));
                // B3. 找出完全被删除的 LoginNumber
                // 如果一个 LoginNumber 对应的所有 ProStaffId 都在本次删除的 ids 集合中，说明该账号不再被任何档案使用
                List<String> usersToDelete = loginNumToStaffIds.entrySet().stream()
                    .filter(entry -> ids.containsAll(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
                // C. 删除用户表 (SysUser) 中对应的行
                if (!usersToDelete.isEmpty()) {
                    // 注意：请根据你实际的User实体类和Mapper名称修改下面的代码
                    int userDeletedCount = sysUserMapper.delete(
                        new LambdaQueryWrapper<SysUser>()
                            .in(SysUser::getUserName, usersToDelete) // 假设用户表账号字段为 UserName
                    );
                    log.info("关联删除系统用户记录 {} 条，账号: {}", userDeletedCount, usersToDelete);
                }
            }
            // ==================== 新增结束 ====================
            // 5. 删除ProStaff记录
            int mainDeletedCount = baseMapper.deleteByIds(ids);
            log.info("删除员工档案记录 {} 条", mainDeletedCount);
            return mainDeletedCount > 0;

        } catch (Exception e) {
            log.error("删除员工档案失败，ids: {}", ids, e);
            throw new ServiceException("删除员工档案失败");
        }
    }


    /**
     * 根据参数查询对应的档案记录
     * @param params 传入的查询参数
     * @return 返回查到的记录
     */
    @Override
    public R<List<ProStaffVo>> searchList(String params) {
        //1、当传入的参数为空或者没有传入参数的时候，直接返回所有记录
        if(params==null||StrUtil.isBlankIfStr(params)){
            return R.ok(this.queryList(new ProStaffBo()));
        }
        //2、当传入的参数不为空时，根据参数获取对应的记录
        List<ProStaffVo> list=baseMapper.selectListByParam(params);
        if(list.isEmpty())return R.fail("没有查找到任何记录，请更换搜索关键词");
        return R.ok(list);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean freezeByBo(ProStaffBo bo) {
        int update = baseMapper.update(
            null,
            new LambdaUpdateWrapper<ProStaff>()
                .eq(ProStaff::getProStaffId, bo.getProStaffId())
                .set(ProStaff::getIsFrozen, 1)
                .set(ProStaff::getUpdateTime, new Date())
                .set(ProStaff::getUpdateBy, LoginHelper.getUserId())
        );
        sysUserMapper.update(new LambdaUpdateWrapper<SysUser>()
            .set(SysUser::getStatus, "1")
            .eq(SysUser::getUserName, bo.getLoginNumber())
        );
        if(update>0)return true;
        else return false;
    }

    /**
     * 将员工档案解除冻结
     *
     * @param bo 传入的需要解冻的员工档案
     * @return 返回解除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> unfreezeByBo(ProStaffBo bo) {
        //1、查询该员工最新档案记录
        ProStaff proStaff = queryByPhoneNumAndDeptId(bo.getIdCardNumber(), bo.getDeptId(), bo.getPhonenumber());
        //2、如果查询到记录相同且非冻结，就提示存在相同记录，无法解除冻结
        if(proStaff!=null&&proStaff.getIsFrozen().equals("0")){
            return R.fail(2001,"该员工档案已存在，无法解除冻结");
        }
        //3、其他情况，直接解除冻结
        int update = baseMapper.update(
            null,
            new LambdaUpdateWrapper<ProStaff>()
                .eq(ProStaff::getProStaffId, bo.getProStaffId())
                .set(ProStaff::getIsFrozen, "0")
        );
        sysUserMapper.update(new LambdaUpdateWrapper<SysUser>()
            .set(SysUser::getStatus, "0")
            .eq(SysUser::getUserName, bo.getLoginNumber())
        );
        if(update>0)return R.ok();
        else return R.fail("解除冻结失败");
    }

    /**
     * 保存员工档案
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> saveByBo(ProStaffBo bo) {
        ProStaff staff=BeanUtil.copyProperties(bo, ProStaff.class);
        //1、通过身份证和部门获取员工
        ProStaff proStaff=queryByPhoneNumAndDeptId(bo.getIdCardNumber(),bo.getDeptId(), bo.getPhonenumber());
        //2、用户存在
        if(proStaff!=null){
            //2.1、员工原记录未冻结
            if(proStaff.getIsFrozen().equals("0")){
                //2.1.1、直接更新员工档案记录
                int update=baseMapper.updateById(staff);
                if(update>0)return R.ok();
                else return R.fail("该记录已保存，保存员工档案失败");
            }
        }
        //3、用户不存在，或者用户被冻结，直接插入
        getNumber(staff);
        SysUserBo user=BeanUtil.copyProperties(staff, SysUserBo.class);
        user.setUserName(staff.getLoginNumber());
        //user.setPassword(BCrypt.hashpw("123456"));
        user.setPassword("123456");
        if(bo.getIsSupervisor().equals("0"))user.setRoleIds(new Long[]{4L});
        if(bo.getIsSupervisor().equals("1"))user.setRoleIds(new Long[]{3L});
        user.setNickName(staff.getName());
        sysUserService.insertUser(user);
        int insert = baseMapper.insert(staff);
        if(insert>0)return R.ok();
        else return R.fail("保存员工档案失败");
    }

    /**
     * 获取员工账号，存在则赋值，不存在则新建
     *
     */
    private void getNumber(ProStaff proStaff) {
        //3.1、查询员工号码表是否存在该员工，有则直接使用
        ProStaffEmnumber proStaffEmnumber = proStaffEmnumberMapper.selectOne(new LambdaQueryWrapper<ProStaffEmnumber>()
            .eq(ProStaffEmnumber::getIdCardNumber, proStaff.getIdCardNumber())
        );
        if(proStaffEmnumber!=null){
            proStaff.setEmployeeNumber(proStaffEmnumber.getEmployeeNumber());}
        //3.2、没有，则新建员工号码，并插入员工号码表
        else{
            String emNumber=createNum();
            proStaff.setEmployeeNumber(emNumber);
            proStaffEmnumberMapper.insert(BeanUtil.copyProperties(proStaff,ProStaffEmnumber.class));
        }
        //4.1、设置员工部门号
        proStaff.setDeptNumber(sysDeptMapper.selectNumById(proStaff.getDeptId()));
        //5.1、设置员工登录号
        proStaff.setLoginNumber(proStaff.getDeptNumber()+proStaff.getEmployeeNumber());
        //6.3、设置员工部门等级
        proStaff.setDeptLevel(sysDeptMapper.getLevelById(proStaff.getDeptId()));
    }

    //给新创建的档案创建员工号
    private String createNum() {
        int maxRetry = 100;
        Random random = new Random();
        Set<String> generated = new HashSet<>();
        for (int i = 0; i < maxRetry; i++) {
            // 批量生成多个候选号码
            for (int j = 0; j < 10; j++) {
                int randomNum = random.nextInt(10000);
                String candidate = String.format("%04d", randomNum);
                generated.add(candidate);
            }
            // 批量查询已存在的号码
            List<ProStaffEmnumber> existing = proStaffEmnumberMapper.selectList(
                new LambdaQueryWrapper<ProStaffEmnumber>()
                    .in(ProStaffEmnumber::getEmployeeNumber, generated));
            // 构建已存在的号码集合
            Set<String> existingNums = existing.stream()
                .map(ProStaffEmnumber::getEmployeeNumber)
                .collect(Collectors.toSet());
            // 找出可用的号码
            for (String candidate : generated) {
                if (!existingNums.contains(candidate)) {
                    return candidate;
                }
            }
            generated.clear(); // 清空集合，准备下一轮生成
        }
        throw new RuntimeException("生成唯一员工号失败，请稍后重试");
    }

    /**
     * 将员工档案归档
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> archiveByBo(ProStaffBo bo) {
        ProStaff proStaff = baseMapper.selectOne(new LambdaQueryWrapper<ProStaff>().eq(ProStaff::getProStaffId, bo.getProStaffId()));
        int insert=0;
        if(proStaff==null){
            R<Void> result = saveByBo(bo);
            if(result.getCode()==200)insert=1;
        }
        else{
            proStaff.setProStaffId(null);
            proStaff.setIsFrozen("1");
            insert=baseMapper.insert(proStaff);
        }
        return insert>0?R.ok():R.fail("归档失败");
    }

    // 需要在类中注入用户表的Mapper
// @Autowired
// private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> resign(ProStaffBo bo) {
        try {
            // 1. 构造查询条件
            LambdaQueryWrapper<ProStaff> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProStaff::getIdCardNumber, bo.getIdCardNumber());
            // ==================== 新增逻辑开始 ====================
            // A. 在更新前，先查出这些记录对应的 LoginNumber
            List<ProStaff> staffList = baseMapper.selectList(
                new LambdaQueryWrapper<ProStaff>()
                    .eq(ProStaff::getIdCardNumber, bo.getIdCardNumber())
                    .select(ProStaff::getLoginNumber) // 只需查LoginNumber字段
            );
            // B. 提取非空的 LoginNumber 列表
            List<String> loginNumbers = staffList.stream()
                .map(ProStaff::getLoginNumber)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
            // ==================== 新增逻辑结束（准备阶段） ====================
            // 2. 原有逻辑：更新 ProStaff 表
            ProStaff updateStaff = new ProStaff();
            updateStaff.setDimissionDate(bo.getDimissionDate()); // 设置离职日期
            updateStaff.setIsFrozen("1"); // 冻结状态
            updateStaff.setStatus("1"); // 离职状态
            int updateCount = baseMapper.update(updateStaff, queryWrapper);
            if (updateCount > 0) {
                // ==================== 新增逻辑开始 ====================
                // C. 如果存在关联的 LoginNumber，更新 User 表状态
                if (!loginNumbers.isEmpty()) {
                    // 创建用于更新的 User 实体 (假设实体类为 SysUser)
                    SysUser updateUser = new SysUser();
                    updateUser.setStatus("1"); // 设置状态为1
                    // 执行批量更新
                    sysUserMapper.update(updateUser,
                        new LambdaQueryWrapper<SysUser>()
                            .in(SysUser::getUserName, loginNumbers) // 匹配 UserName 在列表中的记录
                    );
                    log.info("员工离职，同步冻结系统账号: {}", loginNumbers);
                }
                // ==================== 新增逻辑结束 ====================
                return R.ok("员工离职设置成功");
            } else {
                return R.fail("未找到对应的员工记录");
            }
        } catch (Exception e) {
            log.error("设置员工离职失败：", e);
            throw new ServiceException("设置员工离职失败：" + e.getMessage()); // 建议抛出异常以触发事务回滚
        }
    }


    // 记得注入 SysUserMapper
// @Autowired
// private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> rescindResign(ProStaffBo bo) {
        try {
            ProStaff updateStaff = BeanUtil.copyProperties(bo, ProStaff.class);
            updateStaff.setDimissionDate(null); // 清空离职日期
            updateStaff.setStatus("0"); // 设置在职状态为在职（"1"表示在职）
            // 假设这是你原有的解冻逻辑
            R<Void> unfreeze = unfreezeByBo(bo);
            if (unfreeze.getCode() != 200) {
                return R.fail("该员工档案已存在，无法解除该员工冻结状态");
            }
            int updateCount = baseMapper.updateById(updateStaff);
            if (updateCount > 0) {
                // ==================== 新增逻辑开始 ====================
                // 1. 确保获取到该档案的 LoginNumber (从数据库查最稳妥，防止bo中没有传LoginNumber)
                ProStaff currentStaff = baseMapper.selectOne(
                    new LambdaQueryWrapper<ProStaff>()
                        .eq(ProStaff::getProStaffId, updateStaff.getProStaffId())
                        .select(ProStaff::getLoginNumber) // 只查询需要的字段
                );
                // 2. 如果存在 LoginNumber，则去更新用户表
                if (currentStaff != null && StringUtils.isNotBlank(currentStaff.getLoginNumber())) {
                    SysUser userUpdate = new SysUser();
                    userUpdate.setStatus("0"); // 设置状态为 0 (正常/启用)
                    sysUserMapper.update(userUpdate,
                        new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getUserName, currentStaff.getLoginNumber())
                    );
                    log.info("恢复职位成功，已将账号 {} 的状态恢复为0", currentStaff.getLoginNumber());
                }
                // ==================== 新增逻辑结束 ====================
                return R.ok("恢复职位成功");
            } else {
                return R.fail("未找到对应的员工记录");
            }
        } catch (Exception e) {
            log.error("恢复职位失败：", e);
            throw new ServiceException("恢复职位失败：" + e.getMessage()); // 建议抛出异常以触发事务回滚
        }
    }


    @Override
    public Long getDeptIdByDeptNumber(String deptNumber) {
        Long deptId=sysDeptMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getDeptNumber, deptNumber)).getDeptId();
        System.out.println(deptId);
        return deptId;
    }

    @Override
    public Long getPostIdByDeptIdAndPostName(Long deptId, String postName) {
        SysPost sysPost = sysPostMapper.selectOne(new LambdaQueryWrapper<SysPost>()
            .eq(SysPost::getDeptId, deptId)
            .eq(SysPost::getPostName, postName)
        );
        if(sysPost==null){return null;}
        return sysPost.getPostId();
    }

    @Override
    public ProStaff queryByIdCardNum(String idCardNumber) {
        LambdaQueryWrapper<ProStaff> queryWrapper = new LambdaQueryWrapper<ProStaff>()
            // 【核心补充】使用 select 指定需要查询的字段（对应图中红线部分）
            .select(
                ProStaff::getIdCardNumber,
                ProStaff::getName,            // name
                ProStaff::getEnglishName,     // english_name
                ProStaff::getPhonenumber,     // phonenumber
                ProStaff::getPhonenumber1,    // phonenumber1
                ProStaff::getEmail,           // email
                ProStaff::getExtensionNumber, // extension_number
                ProStaff::getSex,             // sex
                // 建议：通常建议同时查出主键ID，方便后续业务操作，不需要可删除
                ProStaff::getProStaffId,
                ProStaff::getOfficeLocation
            )
            //原本的查询条件
            .eq(ProStaff::getIdCardNumber, idCardNumber)
            .eq(ProStaff::getIsFrozen, "0")
            // 排序逻辑
            .orderByDesc(ProStaff::getUpdateTime)
            .orderByDesc(ProStaff::getCreateTime)
            // 只取第一条
            .last("LIMIT 1");
        return baseMapper.selectOne(queryWrapper);
    }
}
