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

package org.datagear.analysis.support;

/**
 * 数据集源文件未找到异常。
 * 
 * @author datagear@163.com
 *
 */
public class DataSetSourceFileNotFoundException extends DataSetSourceParseException
{
	private static final long serialVersionUID = 1L;

	public DataSetSourceFileNotFoundException()
	{
		super();
	}

	public DataSetSourceFileNotFoundException(String message)
	{
		super(message);
	}

	public DataSetSourceFileNotFoundException(Throwable cause)
	{
		super(cause);
	}

	public DataSetSourceFileNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataSetSourceFileNotFoundException(Throwable cause, String source)
	{
		super(cause, source);
	}

	public DataSetSourceFileNotFoundException(String message, String source)
	{
		super(message, source);
	}
}
