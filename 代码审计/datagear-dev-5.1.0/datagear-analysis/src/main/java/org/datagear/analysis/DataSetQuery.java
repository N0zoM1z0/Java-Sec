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
import java.util.HashMap;
import java.util.Map;

import org.datagear.util.Global;

/**
 * 数据集查询。
 * <p>
 * 此类用于从{@linkplain DataSet}中查询{@linkplain DataSetResult}。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class DataSetQuery implements ResultDataFormatAware, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 内置参数名前缀。
	 * <p>
	 * 所有内置参数名都应以此作为前缀，避免名称冲突。
	 * </p>
	 * <p>
	 * 注意：谨慎重构此常量值，因为它可能已被用于系统已创建的数据集中，重构它将导致这些数据集执行出错。
	 * </p>
	 */
	public static final String BUILTIN_PARAM_PREFIX = Global.NAME_SHORT_UCUS;

	/** 参数值映射表 */
	private Map<String, Object> paramValues = new HashMap<String, Object>();

	/**结果数据格式*/
	private ResultDataFormat resultDataFormat = null;

	/** 结果数据最大返回数目 */
	private int resultFetchSize = -1;

	public DataSetQuery()
	{
		super();
	}

	public DataSetQuery(DataSetQuery query)
	{
		super();
		setParamValues(query.getParamValues());
		this.resultDataFormat = query.resultDataFormat;
		this.resultFetchSize = query.resultFetchSize;
	}

	public Map<String, ?> getParamValues()
	{
		return paramValues;
	}

	/**
	 * 设置参数值映射表。
	 * <p>
	 * 将{@code paramValues}全部放入{@linkplain #getParamValues()}。
	 * </p>
	 * <p>
	 * 参数值映射表的关键字是{@linkplain DataSet#getParam(String)}中的{@linkplain DataSetParam#getName()}，应是符合{@linkplain DataSet#isReady(DataSetQuery)}校验的，
	 * 参数值映射表并不要求与{@linkplain #getParams()}一一对应，通常是包含相同、或者更多的项。
	 * </p>
	 * 
	 * @param paramValues
	 */
	public void setParamValues(Map<String, ?> paramValues)
	{
		if (paramValues == null)
			return;

		this.paramValues.putAll(paramValues);
	}
	
	@Override
	public ResultDataFormat getResultDataFormat()
	{
		return resultDataFormat;
	}

	/**
	 * 设置结果数据格式。
	 * <p>
	 * 当希望自定义{@linkplain DataSet#getResult(Map)}的{@linkplain DataSetResult#getData()}数据格式时，可以设置此项。
	 * </p>
	 * 
	 * @param dataFormat
	 */
	@Override
	public void setResultDataFormat(ResultDataFormat resultDataFormat)
	{
		this.resultDataFormat = resultDataFormat;
	}
	
	/**
	 * 获取结果数据最大返回数目。
	 * 
	 * @return {@code <0} 表示不限定数目
	 */
	public int getResultFetchSize()
	{
		return resultFetchSize;
	}

	public void setResultFetchSize(int resultFetchSize)
	{
		this.resultFetchSize = resultFetchSize;
	}

	/**
	 * 设置参数。
	 * 
	 * @param name
	 * @param value
	 */
	public void setParamValue(String name, Object value)
	{
		this.paramValues.put(name, value);
	}

	/**
	 * 获取参数。
	 * 
	 * @param <T>
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParamValue(String name)
	{
		return (T) this.paramValues.get(name);
	}

	/**
	 * 删除参数。
	 * 
	 * @param <T>
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T removeParamValue(String name)
	{
		return (T) this.paramValues.remove(name);
	}

	/**
	 * 浅复制此对象。
	 * 
	 * @return
	 */
	public DataSetQuery copy()
	{
		return new DataSetQuery(this);
	}

	/**
	 * 构建{@linkplain DataSetQuery}。
	 * 
	 * @return
	 */
	public static DataSetQuery valueOf()
	{
		return new DataSetQuery();
	}
	
	/**
	 * 构建{@linkplain DataSetQuery}。
	 * 
	 * @param paramValues
	 * @return
	 */
	public static DataSetQuery valueOf(Map<String, ?> paramValues)
	{
		DataSetQuery query = new DataSetQuery();
		query.setParamValues(paramValues);
		
		return query;
	}
	
	/**
	 * 构建{@linkplain DataSetQuery}。
	 * 
	 * @param paramValues
	 * @param resultDataFormat
	 * @return
	 */
	public static DataSetQuery valueOf(Map<String, ?> paramValues, ResultDataFormat resultDataFormat)
	{
		DataSetQuery query = valueOf(paramValues);
		query.setResultDataFormat(resultDataFormat);
		
		return query;
	}
	
	/**
	 * 构建{@linkplain DataSetQuery}。
	 * 
	 * @param paramValues
	 * @param resultDataFormat
	 * @param resultFetchSize
	 * @return
	 */
	public static DataSetQuery valueOf(Map<String, ?> paramValues, ResultDataFormat resultDataFormat, int resultFetchSize)
	{
		DataSetQuery query = valueOf(paramValues, resultDataFormat);
		query.setResultFetchSize(resultFetchSize);
		
		return query;
	}
	
	/**
	 * 构建{@linkplain DataSetQuery}。
	 * 
	 * @param query
	 * @return
	 */
	public static DataSetQuery valueOf(DataSetQuery query)
	{
		return new DataSetQuery(query);
	}

	/**
	 * 拷贝。
	 * 
	 * @param query 允许为{@code null}
	 * @return 如果{@code query}为{@code null}，将返回{@code new DataSetQuery()}。
	 */
	public static DataSetQuery copy(DataSetQuery query)
	{
		if(query == null)
			return DataSetQuery.valueOf();
		
		return query.copy();
	}
}
