package com.ofsoft.cms.front.template.freemarker;

import java.util.HashMap;
import java.util.Map;

import com.ofsoft.cms.front.template.directive.AdDirective;
import com.ofsoft.cms.front.template.directive.AnnounceDirective;
import com.ofsoft.cms.front.template.directive.AnnounceListDirective;
import com.ofsoft.cms.front.template.directive.BbsListDirective;
import com.ofsoft.cms.front.template.directive.ColumnDirective;
import com.ofsoft.cms.front.template.directive.CommentListDirective;
import com.ofsoft.cms.front.template.directive.ContentDirective;
import com.ofsoft.cms.front.template.directive.ContentListDirective;
import com.ofsoft.cms.front.template.directive.PageDirective;
import com.ofsoft.cms.front.template.directive.SystemDirective;
import com.ofsoft.cms.front.template.directive.TopicDirective;
import com.ofsoft.cms.front.template.directive.likeDirective;

/**
 * Created by OF on 2018/5/13.
 */
public class FreemarkerUtile {
    
    public static Map<String, TagBase> initTemplate(){
        Map<String, TagBase> data = new HashMap();
        
        data.put("like",new likeDirective());
        data.put("column",new ColumnDirective());
        data.put("content",new ContentDirective());
        data.put("content_list",new ContentListDirective());
        data.put("ad",new AdDirective());
        data.put("announce_list",new AnnounceListDirective());
        data.put("announce",new AnnounceDirective());
        data.put("page",new PageDirective());
        data.put("topic",new TopicDirective());
        data.put("system",new SystemDirective());
        data.put("bbs",new BbsListDirective());
        data.put("comment",new CommentListDirective());
        return  data;
    }
}
