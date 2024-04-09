package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Activity;
import com.gs.commons.service.ActivityService;
import com.gs.commons.mapper.ActivityMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_activity】的数据库操作Service实现
* @createDate 2024-04-09 12:27:12
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService{

}




