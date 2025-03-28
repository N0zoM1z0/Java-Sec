package com.publiccms.views.method.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getHtml 获取远程页面文本
 * <p>
 * 参数列表
 * <ol>
 * <li><code>url</code>:
 * <li>参数,<code>map</code>类型或<code>string</code>类型,为空时以get方式请求,不为空时以post方式请求
 * <li>请求头,<code>map</code>类型
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>html</code>:页面文本
 * </ul>
 * 使用示例
 * <p>
 * ${getHtml('https://www.publiccms.com/')}
 * <p>
 * ${getHtml('https://www.publiccms.com/',"body")}
 * <p>
 * ${getHtml('https://www.publiccms.com/',{"parameters1":"value1","parameters2":"value2"},{"headers1":"value1","headers2":"value2"})}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getHtml?appToken=接口访问授权Token&amp;parameters=https://www.publiccms.com/', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetHtmlMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        Map<?, ?> parameters = getMap(1, arguments);
        String body = getString(1, arguments);
        Map<?, ?> headers = getMap(2, arguments);
        String html = null;
        if (CommonUtils.notEmpty(url)) {
            try (CloseableHttpClient httpclient = HttpClients.custom()
                    .setDefaultRequestConfig(CommonConstants.defaultRequestConfig).build()) {
                HttpUriRequest request;
                if (null != parameters || CommonUtils.notEmpty(body)) {
                    HttpPost httppost = new HttpPost(url);
                    if (null != parameters) {
                        List<NameValuePair> nvps = new ArrayList<>();
                        Iterator<?> it = parameters.keySet().iterator();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            Object value = parameters.get(key);
                            nvps.add(new BasicNameValuePair(key, null == value ? null : value.toString()));
                        }
                        httppost.setEntity(new UrlEncodedFormEntity(nvps, CommonConstants.DEFAULT_CHARSET));
                    } else {
                        httppost.setEntity(new StringEntity(body, CommonConstants.DEFAULT_CHARSET));
                    }
                    request = httppost;
                } else {
                    request = new HttpGet(url);
                }
                if (null != headers) {
                    for (Entry<?, ?> entry : headers.entrySet()) {
                        request.addHeader((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                try (CloseableHttpResponse response = httpclient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (null != entity) {
                        html = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                        EntityUtils.consume(entity);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
            return html;
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}
