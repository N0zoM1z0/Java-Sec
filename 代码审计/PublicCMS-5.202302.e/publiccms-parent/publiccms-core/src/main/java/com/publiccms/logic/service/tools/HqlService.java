package com.publiccms.logic.service.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.hibernate.search.backend.lucene.LuceneBackend;
import org.hibernate.search.engine.backend.Backend;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.dao.tools.HqlDao;

/**
 *
 * HqlService
 * 
 */
@Service
@Transactional
public class HqlService extends BaseService<Object> {

    /**
     * @param hql
     * @return
     */
    public int update(String hql) {
        return dao.update(hql);
    }

    /**
     * @param hql
     * @return
     */
    public int delete(String hql) {
        return dao.delete(hql);
    }
    /**
     * @param <R> 
     * @param hql
     * @param parameters
     * @param pageIndex
     * @param pageSize
     * @param resultClass 
     * @return
     */
    @Transactional(readOnly = true)
    public <R> PageHandler getPage(String hql, Map<String, Object> parameters, Integer pageIndex, Integer pageSize,
            Class<R> resultClass) {
        return dao.getPage(hql, parameters, pageIndex, pageSize,resultClass);
    }
    /**
     * @param hql
     * @param parameters
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String hql, Map<String, Object> parameters, Integer pageIndex, Integer pageSize) {
        return dao.getPage(hql, parameters, pageIndex, pageSize);
    }

    /**
     * @param text
     * @return
     */
    public Set<String> getToken(String text) {
        Set<String> list = new LinkedHashSet<>();
        if (CommonUtils.notEmpty(text)) {
            Backend backend = dao.getSearchBackend();
            if (backend instanceof LuceneBackend) {
                Analyzer analyzer = backend.unwrap(LuceneBackend.class).analyzer("cms").get();
                try (StringReader stringReader = new StringReader(text);
                        TokenStream tokenStream = analyzer.tokenStream(CommonConstants.BLANK, stringReader)) {
                    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                    tokenStream.reset();
                    while (tokenStream.incrementToken()) {
                        list.add(charTermAttribute.toString());
                    }
                    tokenStream.end();
                    return list;
                } catch (IOException e) {
                    return list;
                }
            }
        }
        return list;
    }

    /**
     * @return results page
     */
    public CompletionStage<?> reCreateIndex() {
        return dao.reCreateIndex();
    }

    /**
     * 
     */
    public void clear() {
        dao.clear();
    }

    @Resource
    private HqlDao dao;

}
