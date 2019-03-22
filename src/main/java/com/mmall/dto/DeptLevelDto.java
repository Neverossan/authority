package com.mmall.dto;



import com.google.common.collect.Lists;
import com.mmall.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;


/**
 * @Auther: HP
 * @Date: 2019/3/22 16:21
 * @Description:
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept){
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(dept,deptLevelDto);
        return deptLevelDto;
    }


}
