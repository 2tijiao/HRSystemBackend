package org.dromara.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.domain.dto.DeptDTO;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.DeptService;
import org.dromara.common.core.utils.*;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.dromara.common.redis.utils.CacheUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.SysDept;
import org.dromara.system.domain.SysRole;
import org.dromara.system.domain.SysUser;
import org.dromara.system.domain.bo.SysDeptBo;
import org.dromara.system.domain.vo.SysDeptVo;
import org.dromara.system.mapper.ProStaffMapper;
import org.dromara.system.mapper.SysDeptMapper;
import org.dromara.system.mapper.SysRoleMapper;
import org.dromara.system.mapper.SysUserMapper;
import org.dromara.system.service.ISysDeptService;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 部门管理 服务实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysDeptServiceImpl implements ISysDeptService, DeptService {

    private final SysDeptMapper baseMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserMapper userMapper;
    private final ProStaffMapper proStaffMapper;

    /**
     * 分页查询部门管理数据
     *
     * @param dept      部门信息
     * @param pageQuery 分页对象
     * @return 部门信息集合
     */
    @Override
    public TableDataInfo<SysDeptVo> selectPageDeptList(SysDeptBo dept, PageQuery pageQuery) {
        Page<SysDeptVo> page = baseMapper.selectPageDeptList(pageQuery.build(), buildQueryWrapper(dept));
        return TableDataInfo.build(page);
    }

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDeptVo> selectDeptList(SysDeptBo dept) {
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(dept);
        return baseMapper.selectDeptList(lqw);
    }

    /**
     * 查询部门树结构信息
     *
     * @param bo 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<Tree<Long>> selectDeptTreeList(SysDeptBo bo) {
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
        List<SysDeptVo> depts = baseMapper.selectDeptList(lqw);
        return buildDeptTreeSelect(depts);
    }

    private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDeptBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysDept::getDelFlag, SystemConstants.NORMAL);
        lqw.eq(ObjectUtil.isNotNull(bo.getDeptId()), SysDept::getDeptId, bo.getDeptId());
        lqw.eq(ObjectUtil.isNotEmpty(bo.getDeptNumber()), SysDept::getDeptNumber, bo.getDeptNumber());
        lqw.eq(ObjectUtil.isNotNull(bo.getParentId()), SysDept::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getDeptName()), SysDept::getDeptName, bo.getDeptName());
        lqw.like(StringUtils.isNotBlank(bo.getDeptCategory()), SysDept::getDeptCategory, bo.getDeptCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysDept::getStatus, bo.getStatus());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            SysDept::getCreateTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByAsc(SysDept::getAncestors);
        lqw.orderByAsc(SysDept::getParentId);
        lqw.orderByAsc(SysDept::getOrderNum);
        lqw.orderByAsc(SysDept::getDeptId);
        if (ObjectUtil.isNotNull(bo.getBelongDeptId())) {
            //部门树搜索
            lqw.and(x -> {
                List<Long> deptIds = baseMapper.selectDeptAndChildById(bo.getBelongDeptId());
                x.in(SysDept::getDeptId, deptIds);
            });
        }
        return lqw;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<Long>> buildDeptTreeSelect(List<SysDeptVo> depts) {
        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.buildMultiRoot(
            depts,
            SysDeptVo::getDeptId,
            SysDeptVo::getParentId,
            (node, treeNode) -> treeNode
                .setId(node.getDeptId())
                .setParentId(node.getParentId())
                .setName(node.getDeptName())
                .setWeight(node.getOrderNum())
                .putExtra("disabled", SystemConstants.DISABLE.equals(node.getStatus()))
        );
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        return baseMapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_DEPT, key = "#deptId")
    @Override
    public SysDeptVo selectDeptById(Long deptId) {
        SysDeptVo dept = baseMapper.selectVoById(deptId);
        if (ObjectUtil.isNull(dept)) {
            return null;
        }
        SysDeptVo parentDept = baseMapper.selectVoOne(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtils.notNullGetter(parentDept, SysDeptVo::getDeptName));
        return dept;
    }

    @Override
    public List<SysDeptVo> selectDeptByIds(List<Long> deptIds) {
        return baseMapper.selectDeptList(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptId, SysDept::getDeptName, SysDept::getLeader)
            .eq(SysDept::getStatus, SystemConstants.NORMAL)
            .in(CollUtil.isNotEmpty(deptIds), SysDept::getDeptId, deptIds));
    }

    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    @Override
    public String selectDeptNameByIds(String deptIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(deptIds, Convert::toLong)) {
            SysDeptVo vo = SpringUtils.getAopProxy(this).selectDeptById(id);
            if (ObjectUtil.isNotNull(vo)) {
                list.add(vo.getDeptName());
            }
        }
        return StringUtils.joinComma(list);
    }

    /**
     * 根据部门ID查询部门负责人
     *
     * @param deptId 部门ID，用于指定需要查询的部门
     * @return 返回该部门的负责人ID
     */
    @Override
    public Long selectDeptLeaderById(Long deptId) {
        SysDeptVo vo = SpringUtils.getAopProxy(this).selectDeptById(deptId);
        return vo.getLeader();
    }

    /**
     * 查询部门
     *
     * @return 部门列表
     */
    @Override
    public List<DeptDTO> selectDeptsByList() {
        List<SysDeptVo> list = baseMapper.selectDeptList(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptId, SysDept::getDeptName, SysDept::getParentId)
            .eq(SysDept::getStatus, SystemConstants.NORMAL));
        return BeanUtil.copyToList(list, DeptDTO.class);
    }

    /**
     * 根据ID查询所有子部门数（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public long selectNormalChildrenDeptById(Long deptId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getStatus, SystemConstants.NORMAL)
            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return baseMapper.exists(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getParentId, deptId));
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return userMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getDeptId, deptId));
    }

    /**
     * 查询部门是否存在员工
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistStaff(Long deptId) {
        return proStaffMapper.exists(new LambdaQueryWrapper<ProStaff>()
            .eq(ProStaff::getDeptId, deptId)
            .eq(ProStaff::getIsFrozen,"0"));
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public boolean checkDeptNameUnique(SysDeptBo dept) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptName, dept.getDeptName())
            .eq(SysDept::getParentId, dept.getParentId())
            .ne(ObjectUtil.isNotNull(dept.getDeptId()), SysDept::getDeptId, dept.getDeptId()));
        return !exist;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return;
        }
        if (LoginHelper.isSuperAdmin()) {
            return;
        }
        if (baseMapper.countDeptById(deptId) == 0) {
            throw new ServiceException("没有权限访问部门数据！");
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    @CacheEvict(cacheNames = CacheNames.SYS_DEPT_AND_CHILD, allEntries = true)
    @Override
    public int insertDept(SysDeptBo bo) {
        SysDept info = baseMapper.selectById(bo.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!SystemConstants.NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        dept.setAncestors(info.getAncestors() + StringUtils.SEPARATOR + dept.getParentId());
        // --- 开始：新增的代码逻辑，如果部门号为空，就生成部门号 ---
        if(bo.getDeptNumber()==null)dept.setDeptNumber(getDeptNumber());
        // --- 结束：新增的代码逻辑 ---
        return baseMapper.insert(dept);
    }

    /**生成部门号**/
    @NotNull
    private String getDeptNumber() {
        String deptCode;
        boolean unique = false;
        // 建议增加一个防死循环的计数器，防止所有号码都被占满后程序卡死
        int retryCount = 0;
        do {
            // 1. 随机生成一个小于100的数 (0-99)
            // 如果项目里没有 Hutool，可以用 new Random().nextInt(100)
            int randomNum = cn.hutool.core.util.RandomUtil.randomInt(100);
            // 2. 补够四位字符串 (前补0)
            deptCode = String.format("%04d", randomNum);
            // 3. 校验是否存在 (false表示存在，true表示不存在/可用)
            // 这里直接复用了 MyBatis-Plus 的查询能力
            unique = checkDeptCodeUnique(deptCode);
            retryCount++;
            // 安全保护：如果只有100个数(0000-0099)，很容易占满。防止死循环。
            if (retryCount > 200) {
                throw new ServiceException("生成部门编号失败，尝试次数过多，请检查号段资源");
            }
        } while (!unique); // 如果 unique 为 false (即存在)，则继续循环重新生成
        return deptCode;
    }

    @Caching(evict = {
        @CacheEvict(cacheNames = CacheNames.SYS_DEPT, key = "#bo.deptId"),
        @CacheEvict(cacheNames = CacheNames.SYS_DEPT_AND_CHILD, allEntries = true)
    })
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDept(SysDeptBo bo) {
        SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        SysDept oldDept = baseMapper.selectById(dept.getDeptId());
        if (ObjectUtil.isNull(oldDept)) {
            throw new ServiceException("部门不存在，无法修改");
        }
        // --- 开始：新增的代码逻辑 ---
        // 1. 判断部门号是否发生了改变 (假设实体中的字段名为 deptCode)
        // 使用 StringUtils.equals 可以防止空指针，同时比较内容
        if (!StringUtils.equals(dept.getDeptNumber(), oldDept.getDeptNumber())) {
            // 2. 如果发生了改变，则查询新设置的部门号在数据库中是否存在
            long count = baseMapper.selectCount(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeptNumber, dept.getDeptNumber()));

            // 3. 如果存在 (>0)，直接抛出异常，中断事务回滚
            if (count > 0) {
                throw new ServiceException("部门编号已存在，请重新输入");
            }
        }
        // --- 结束：新增的代码逻辑 ---
        if (!oldDept.getParentId().equals(dept.getParentId())) {
            // 如果是新父部门 则校验是否具有新父部门权限 避免越权
            this.checkDeptDataScope(dept.getParentId());
            SysDept newParentDept = baseMapper.selectById(dept.getParentId());
            if (ObjectUtil.isNotNull(newParentDept)) {
                String newAncestors = newParentDept.getAncestors() + StringUtils.SEPARATOR + newParentDept.getDeptId();
                String oldAncestors = oldDept.getAncestors();
                dept.setAncestors(newAncestors);
                updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
            }
        } else {
            dept.setAncestors(oldDept.getAncestors());
        }
        int result = baseMapper.updateById(dept);
        // 如果部门状态为启用，且部门祖级列表不为空，且部门祖级列表不等于根部门祖级列表（如果部门祖级列表不等于根部门祖级列表，则说明存在上级部门）
        if (SystemConstants.NORMAL.equals(dept.getStatus())
            && StringUtils.isNotEmpty(dept.getAncestors())
            && !StringUtils.equals(SystemConstants.ROOT_DEPT_ANCESTORS, dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 辅助方法：检查部门号是否唯一
     * @param deptCode 部门四位编号
     * @return true=不存在(可用)，false=已存在(不可用)
     */
    private boolean checkDeptCodeUnique(String deptCode) {
        // 使用 MyBatis-Plus 查询是否存在该 deptCode
        long count = baseMapper.selectCount(
            new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeptNumber, deptCode) // 假设你的实体字段叫 DeptCode
        );
        // count == 0 说明不存在，返回 true
        return count == 0;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        baseMapper.update(null, new LambdaUpdateWrapper<SysDept>()
            .set(SysDept::getStatus, SystemConstants.NORMAL)
            .in(SysDept::getDeptId, Arrays.asList(deptIds)));
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<SysDept> list = new ArrayList<>();
        for (SysDept child : children) {
            SysDept dept = new SysDept();
            dept.setDeptId(child.getDeptId());
            dept.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(dept);
        }
        if (CollUtil.isNotEmpty(list)) {
            if (baseMapper.updateBatchById(list)) {
                list.forEach(dept -> CacheUtils.evict(CacheNames.SYS_DEPT, dept.getDeptId()));
            }
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Caching(evict = {
        @CacheEvict(cacheNames = CacheNames.SYS_DEPT, key = "#deptId"),
        @CacheEvict(cacheNames = CacheNames.SYS_DEPT_AND_CHILD, key = "#deptId")
    })
    @Override
    public int deleteDeptById(Long deptId) {
        return baseMapper.deleteById(deptId);
    }

    /**
     * 将部门档案归档
     * @param dept
     * @return
     */
    @Override
    public R<Void> archive(SysDeptBo dept) {
        SysDept sysDept = baseMapper.selectOne(new LambdaQueryWrapper<SysDept>().eq(SysDept::getDeptId, dept.getDeptId()));
        int insert=0;
        if(sysDept==null){
           if(insertDept(dept)>0)insert=1;
        }
        else{
            dept.setDeptId(null);
            dept.setStatus(SystemConstants.DISABLE);
            dept.setCreateTime(new Date());
            insert=insertDept(dept);
        }
        return insert>0?R.ok():R.fail("归档失败");
    }


    /**
     * 根据部门 ID 列表查询部门名称映射关系
     *
     * @param deptIds 部门 ID 列表
     * @return Map，其中 key 为部门 ID，value 为对应的部门名称
     */
    @Override
    public Map<Long, String> selectDeptNamesByIds(List<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }
        List<SysDept> list = baseMapper.selectList(
            new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId, SysDept::getDeptName)
                .in(SysDept::getDeptId, deptIds)
        );
        return StreamUtils.toMap(list, SysDept::getDeptId, SysDept::getDeptName);
    }

}
