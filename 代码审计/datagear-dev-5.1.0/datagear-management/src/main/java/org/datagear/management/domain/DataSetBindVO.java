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

import java.io.Serializable;

import org.datagear.analysis.DataSetBind;
import org.datagear.analysis.DataSet;
import org.datagear.analysis.DataSetResult;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@linkplain DataSetBind}值对象。
 * 
 * @author datagear@163.com
 *
 */
public class DataSetBindVO extends DataSetBind implements CloneableEntity, Serializable
{
	private static final long serialVersionUID = 1L;

	public DataSetBindVO()
	{
		super();
	}

	public DataSetBindVO(DataSet dataSet)
	{
		super(dataSet);
	}

	/**
	 * 设置{@linkplain SummaryDataSetEntity}，仅用于保存操作。
	 * 
	 * @param dataSetEntity
	 */
	public void setSummaryDataSetEntity(SummaryDataSetEntity dataSetEntity)
	{
		super.setDataSet(dataSetEntity);
	}

	@JsonIgnore
	@Override
	public DataSetResult getResult()
	{
		return super.getResult();
	}

	@Override
	public DataSetBindVO clone()
	{
		DataSetBindVO entity = new DataSetBindVO();
		BeanUtils.copyProperties(this, entity);

		return entity;
	}
}
