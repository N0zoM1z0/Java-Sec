package com.publiccms.logic.service.log;

import java.io.Serializable;

// Generated 2015-7-3 16:15:25 by com.publiccms.common.generator.SourceGenerator

import java.util.Date;
import java.util.List;

import com.publiccms.entities.log.LogTask;
import com.publiccms.logic.dao.log.LogTaskDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * LogTaskService
 * 
 */
@Service
@Transactional
public class LogTaskService extends BaseService<LogTask> {

    /**
     * @param siteId
     * @param taskId
     * @param startBegintime
     * @param endBegintime
     * @param success
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer taskId, Date startBegintime, Date endBegintime, Boolean success,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, taskId, startBegintime, endBegintime, success, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param endBegintime
     * @return
     */
    @Transactional(readOnly = true)
    public List<LogTask> getNotEndList(Short siteId, Date endBegintime) {
        return dao.getNotEndList(siteId, endBegintime);
    }

    /**
     * @param siteId
     * @param begintime
     * @return
     */
    public int delete(Short siteId, Date begintime) {
        return dao.delete(siteId, begintime);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (LogTask entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Resource
    private LogTaskDao dao;

}