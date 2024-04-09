package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Notice;
import com.gs.commons.service.NoticeService;
import com.gs.commons.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_notice】的数据库操作Service实现
* @createDate 2024-04-09 11:59:46
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




