package com.mmall.service;

import com.google.common.base.Preconditions;
import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Auther: HP
 * @Date: 2019/3/22 15:05
 * @Description:
 */
@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    public void save(DeptParam param){
        //验证参数
        BeanValidator.check(param);
        //判断同级是否存在相同的部门
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        //添加部门
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        dept.setOperateIp("127.0.0.1");//TODO
        dept.setOperateTime(new Date());
        dept.setOperator("system");//TODO
        sysDeptMapper.insertSelective(dept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId){
        //TODO
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }else {
            return dept.getLevel();
        }
    }

    public void update(DeptParam param){
        //验证参数
        BeanValidator.check(param);
        //判断同级是否存在相同的部门
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        if (checkExist(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperateIp("127.0.0.1");//TODO
        after.setOperateTime(new Date());
        after.setOperator("system-update");//TODO
        updateWithChild(before,after);
    }

    @Transactional
    private void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }
}
