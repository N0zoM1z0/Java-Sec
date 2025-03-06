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

package org.datagear.persistence;

/**
 * 分页数据。
 * 
 * @author datagear@163.com
 *
 * @param <T>
 */
public class PagingData<T> extends org.datagear.util.query.PagingData<T>
{
	public PagingData()
	{
		super();
	}

	public PagingData(int page, long total)
	{
		super(page, total);
	}

	public PagingData(int page, long total, int pageSize)
	{
		super(page, total, pageSize);
	}
}
