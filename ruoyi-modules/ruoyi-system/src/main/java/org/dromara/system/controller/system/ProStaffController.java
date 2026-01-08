package org.dromara.system.controller.system;

import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import org.dromara.common.ratelimiter.annotation.RateLimiter;
import org.dromara.common.web.core.BaseController;

import org.dromara.system.domain.ProStaff;
import org.dromara.system.domain.bo.ProStaffBo;
import org.dromara.system.domain.vo.ProStaffVo;
import org.dromara.system.listener.ProStaffImportListener;
import org.dromara.system.service.IProStaffService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工档案
 *
 * @author Lion Li
 * @date 2025-11-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/profile/staff")
public class ProStaffController extends BaseController {

    private final IProStaffService proStaffService;

    /**
     * 查询员工档案列表
     */
    //@SaCheckPermission("profile:staff:list")
    @GetMapping("/list")
    public TableDataInfo<ProStaffVo> list(ProStaffBo bo, PageQuery pageQuery) {
        return proStaffService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工档案列表
     */
    //@SaCheckPermission("profile:staff:export")
    @Log(title = "员工档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProStaffBo bo, HttpServletResponse response) {
        // 【核心修改】：强制只导出状态为 "0" (正常/未冻结) 的数据
        // 这样生成的 Excel 文件里根本就不会有冻结的人
        bo.setIsFrozen("0");
        List<ProStaffVo> list = proStaffService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工档案", ProStaffVo.class, response);
    }

    /**
     * 获取导入模板
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "员工档案", ProStaffVo.class, response);
    }

    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Log(title = "员工档案", businessType = BusinessType.IMPORT)
    //@SaCheckPermission("system:user:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        ExcelResult<ProStaffVo> result = ExcelUtil.importExcel(file.getInputStream(), ProStaffVo.class, new ProStaffImportListener(updateSupport));
        return R.ok(result.getAnalysis());
    }

    /**
     * 获取员工档案详细信息
     *
     * @param proStaffId 主键
     */
    //@SaCheckPermission("profile:staff:query")
    @GetMapping("/{proStaffId}")
    public R<ProStaff> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long proStaffId) {
        return R.ok(proStaffService.queryById(proStaffId));
    }

    /**
     * 根据身份证号和部门id获取员工信息
     */
    @PostMapping("/getInfo")
    @RateLimiter(key="ProStaff:info")
    public R<ProStaffVo> getInfoByIdNumberAndDeptId(@RequestBody ProStaffBo bo) {
        if(bo.getDeptId()!=null){
            return R.ok(
                BeanUtil.copyProperties(
                    proStaffService.queryByPhoneNumAndDeptId(bo.getIdCardNumber(), bo.getDeptId(), bo.getPhonenumber()),ProStaffVo.class
                )
            );
        }
        else return R.ok(
            BeanUtil.copyProperties(
                proStaffService.queryByIdCardNum(bo.getIdCardNumber()),ProStaffVo.class
            )
        );

    }

    /**
     * 保存员工档案
     */
    //@SaCheckPermission("profile:staff:add")
    @Log(title = "员工档案", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> save(@RequestBody ProStaffBo bo) {
        return proStaffService.saveByBo(bo);
    }

    /**
     * 将员工档案归档
     * @param bo
     * @return
     */
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/archive")
    public R<Void> archive(@RequestBody ProStaffBo bo){
        return proStaffService.archiveByBo(bo);
    }

    /**
     * 修改员工档案
     */
    //@SaCheckPermission("profile:staff:edit")
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProStaffBo bo) {
        return proStaffService.updateByBo(bo);
    }

    /**
     * 删除员工档案
     *
     * @param proStaffIds 主键串
     */
    //@SaCheckPermission("profile:staff:remove")
    @Log(title = "员工档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{proStaffIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] proStaffIds) {
        return toAjax(proStaffService.deleteWithValidByIds(List.of(proStaffIds), true));
    }

    /**
     * 冻结员工档案
     *
     * @param bo 传入的员工档案
     */
    @PostMapping("/freeze")
    @Log(title = "员工档案",businessType = BusinessType.UPDATE)
    public R<Void> freeze(@RequestBody ProStaffBo bo) {
        return toAjax(proStaffService.freezeByBo(bo));
    }

    /**
     * 将已存在的员工档案解除冻结
     *
     * @param bo 传入的员工档案
     */
    @PostMapping("/unfreeze")
    @Log(title="员工档案",businessType = BusinessType.UPDATE)
    public R<Void> unfreeze(@RequestBody ProStaffBo bo) {
        return proStaffService.unfreezeByBo(bo);
    }

    /**
     * 离职操作
     */
    @PostMapping("/resign")
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    public R<Void> resign(@RequestBody ProStaffBo bo) {
        return proStaffService.resign(bo);
    }

    /**
     * 恢复某员工职位
     * @param bo
     * @return
     */
    @PostMapping("/rescind/resign")
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    public R<Void> rescindResign(@RequestBody ProStaffBo bo) {
        return proStaffService.rescindResign(bo);
    }

}
