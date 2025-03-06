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

import java.io.Serializable;
import java.util.Map;

/**
 * 数据集结果。
 * 
 * @author datagear@163.com
 *
 */
public class DataSetResult implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 结果数据对象 */
	private Object data;

	public DataSetResult()
	{
		super();
	}

	/**
	 * 创建实例。
	 * 
	 * @param data
	 *            应符合{@linkplain #getData()}规范
	 */
	public DataSetResult(Object data)
	{
		super();
		this.data = data;
	}

	/**
	 * 获取数据。
	 * <p>
	 * 数据应是普通JavaBean、 {@linkplain Map}对象，或者是它们的数组、集合。
	 * </p>
	 * 
	 * @return 数据，为{@code null}表示无数据
	 */
	public Object getData()
	{
		return this.data;
	}

	/**
	 * 设置数据。
	 * 
	 * @param data
	 *            应符合{@linkplain #getData()}规范
	 */
	public void setData(Object data)
	{
		this.data = data;
	}
}
