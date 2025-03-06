/*
 * Copyright 2018-present datagear.tech
 *
 * This file is part of DataGear.
 *
 * DataGear is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * DataGear is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with DataGear.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package org.datagear.analysis;

import java.io.Reader;
import java.io.Writer;

/**
 * 模板看板渲染上下文。
 * <p>
 * 如果设置了{@linkplain #setTemplateReader(Reader)}，应作为模板输入流首选项。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class TplDashboardRenderContext extends DefaultRenderContext
{
	private static final long serialVersionUID = 1L;

	public static final long TEMPLATE_LAST_MODIFIED_NONE = -1;

	/**模板名*/
	private String template;

	/**模版上次修改时间*/
	private long templateLastModified = TEMPLATE_LAST_MODIFIED_NONE;

	/**模版输入流*/
	private transient Reader templateReader = null;

	/**输出流*/
	private transient Writer writer;
	
	public TplDashboardRenderContext()
	{
		super();
	}

	public TplDashboardRenderContext(String template, Writer writer)
	{
		super();
		this.template = template;
		this.writer = writer;
	}

	public TplDashboardRenderContext(String template, long templateLastModified, Writer writer)
	{
		super();
		this.template = template;
		this.templateLastModified = templateLastModified;
		this.writer = writer;
	}

	public TplDashboardRenderContext(String template, Reader templateReader, Writer writer)
	{
		super();
		this.template = template;
		this.templateReader = templateReader;
		this.writer = writer;
	}

	public TplDashboardRenderContext(String template, Reader templateReader, long templateLastModified, Writer writer)
	{
		super();
		this.template = template;
		this.templateReader = templateReader;
		this.templateLastModified = templateLastModified;
		this.writer = writer;
	}

	public TplDashboardRenderContext(TplDashboardRenderContext renderContext)
	{
		super(renderContext);
		this.template = renderContext.getTemplate();
		this.templateLastModified = renderContext.getTemplateLastModified();
		this.templateReader = renderContext.getTemplateReader();
		this.writer = renderContext.getWriter();
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}
	
	public boolean hasTemplateLastModified()
	{
		return this.templateLastModified > 0;
	}

	public long getTemplateLastModified()
	{
		return templateLastModified;
	}

	public void setTemplateLastModified(long templateLastModified)
	{
		this.templateLastModified = templateLastModified;
	}
	
	public boolean hasTemplateReader()
	{
		return this.templateReader != null;
	}

	public Reader getTemplateReader()
	{
		return templateReader;
	}

	public void setTemplateReader(Reader templateReader)
	{
		this.templateReader = templateReader;
	}

	public Writer getWriter()
	{
		return writer;
	}

	public void setWriter(Writer writer)
	{
		this.writer = writer;
	}
}
