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

package org.datagear.management.domain;

import java.util.Date;
import java.util.List;

import org.apache.hc.client5.http.classic.HttpClient;
import org.datagear.analysis.DataSetField;
import org.datagear.analysis.support.HttpDataSet;
import org.springframework.beans.BeanUtils;

/**
 * {@linkplain HttpDataSet}实体。
 * 
 * @author datagear@163.com
 *
 */
public class HttpDataSetEntity extends HttpDataSet implements DataSetEntity, CloneableEntity
{
	private static final long serialVersionUID = 1L;

	/** 创建用户 */
	private User createUser;

	/** 创建时间 */
	private Date createTime = null;

	/** 权限 */
	private int dataPermission = PERMISSION_NOT_LOADED;

	private AnalysisProject analysisProject = null;

	public HttpDataSetEntity()
	{
		super();
	}

	public HttpDataSetEntity(String id, String name, HttpClient httpClient, String uri, User createUser)
	{
		super(id, name, httpClient, uri);
		this.createUser = createUser;
	}

	public HttpDataSetEntity(String id, String name, List<DataSetField> fields, HttpClient httpClient,
			String uri, User createUser)
	{
		super(id, name, fields, httpClient, uri);
		this.createUser = createUser;
	}

	@Override
	public String getDataSetType()
	{
		return DataSetEntity.DATA_SET_TYPE_Http;
	}

	@Override
	public void setDataSetType(String dataSetType)
	{
		// XXX 什么也不做，不采用抛出异常的方式，便于统一底层SQL查询语句
		// throw new UnsupportedOperationException();
	}

	@Override
	public User getCreateUser()
	{
		return createUser;
	}

	@Override
	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}

	@Override
	public Date getCreateTime()
	{
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	@Override
	public int getDataPermission()
	{
		return dataPermission;
	}

	@Override
	public void setDataPermission(int dataPermission)
	{
		this.dataPermission = dataPermission;
	}

	@Override
	public AnalysisProject getAnalysisProject()
	{
		return analysisProject;
	}

	@Override
	public void setAnalysisProject(AnalysisProject analysisProject)
	{
		this.analysisProject = analysisProject;
	}

	@Override
	public HttpDataSetEntity clone()
	{
		HttpDataSetEntity entity = new HttpDataSetEntity();
		BeanUtils.copyProperties(this, entity);

		return entity;
	}
}
