package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 *
 * contentQuoteList 内容引用列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>quoteId</code>:引用内容id
 * </ul>
 * <p>
 * 返回结果:
 * <ul>
 * <li><code>list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentQuoteList contentId=1 pageSize=10&gt;&lt;#list list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentQuoteList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/contentQuoteList?contentId=1', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsContentQuoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        Long quoteId = handler.getLong("quoteId");
        if (null != quoteId) {
            List<CmsContent> list = service.getListByQuoteId(site.getId(), quoteId);
            handler.put("list", list).render();
        }
    }

    @Resource
    private CmsContentService service;
}