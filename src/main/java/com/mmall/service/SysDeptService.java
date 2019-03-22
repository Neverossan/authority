package com.mmall.service;

import com.mmall.dao.SysDeptMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.util.BeanValidator;
import com.mmall.util.LevelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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
        SysDept dept = SysDept.builder().name(param.getName()).seq(param.getSeq())
                .id(param.getId()).remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        dept.setOperateIp("127.0.0.1");//TODO
        dept.setOperateTime(new Date());
        dept.setOperator("system");//TODO
        sysDeptMapper.insertSelective(dept);
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId){
        //TODO
        return true;
    }

    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }else {
            return dept.getLevel();
        }
    }
}
