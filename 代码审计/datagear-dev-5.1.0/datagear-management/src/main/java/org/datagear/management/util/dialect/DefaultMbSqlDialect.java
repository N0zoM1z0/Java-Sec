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

package org.datagear.management.util.dialect;

/**
 * 默认{@linkplain MbSqlDialect}。
 * <p>
 * 此方言不支持分页查询（{@linkplain #supportsPaging()}返回{@code false}）。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class DefaultMbSqlDialect extends MbSqlDialect
{
	public DefaultMbSqlDialect()
	{
		super();
	}

	public DefaultMbSqlDialect(String identifierQuote)
	{
		super(identifierQuote);
	}

	@Override
	public boolean supportsPaging()
	{
		return false;
	}

	@Override
	public String pagingSqlHead(int index, int fetchSize)
	{
		return null;
	}

	@Override
	public String pagingSqlFoot(int index, int fetchSize)
	{
		return null;
	}
}
