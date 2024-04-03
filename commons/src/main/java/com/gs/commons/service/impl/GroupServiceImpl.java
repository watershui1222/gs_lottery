package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Group;
import com.gs.commons.service.GroupService;
import com.gs.commons.mapper.GroupMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_group】的数据库操作Service实现
* @createDate 2024-04-03 17:29:52
*/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
    implements GroupService{

}




