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

package org.datagear.dataexchange;

import java.sql.Connection;

import org.datagear.util.QueryResultSet;

/**
 * 查询。
 * 
 * @author datagear@163.com
 *
 */
public interface Query
{
	/**
	 * 执行查询并返回结果。
	 * 
	 * @param cn
	 * @return
	 * @throws Throwable
	 */
	QueryResultSet execute(Connection cn) throws Throwable;
}
