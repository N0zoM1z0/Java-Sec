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

package org.datagear.web.controller;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datagear.management.domain.DtbsSource;
import org.datagear.management.domain.User;
import org.datagear.meta.Column;
import org.datagear.meta.SimpleTable;
import org.datagear.meta.Table;
import org.datagear.meta.TableUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SQL编辑器控制器。
 * 
 * @author datagear@163.com
 *
 */
@Controller
@RequestMapping("/dtbsSourceSqlEditor")
public class DtbsSourceSqlEditorController extends AbstractDtbsSourceConnTableController
{
	public DtbsSourceSqlEditorController()
	{
		super();
	}

	@RequestMapping(value = "/{dtbsSourceId}/findTableNames", produces = CONTENT_TYPE_JSON)
	@ResponseBody
	public List<String> findTableNames(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model springModel, @PathVariable("dtbsSourceId") String dtbsSourceId,
			@RequestParam(value = "keyword", required = false) String keyword) throws Throwable
	{
		final User user = getCurrentUser();

		List<SimpleTable> tables = new ReturnDtbsSourceConnExecutor<List<SimpleTable>>(request, response, springModel,
				dtbsSourceId, true)
		{
			@Override
			protected List<SimpleTable> execute(HttpServletRequest request, HttpServletResponse response,
					org.springframework.ui.Model springModel, DtbsSource dtbsSource) throws Throwable
			{
				checkReadTableDataPermission(dtbsSource, user);

				Connection cn = getConnection();
				List<SimpleTable> tables = getDbMetaResolver().getDataTables(cn);

				return tables;
			}

		}.execute();

		List<SimpleTable> keywordTables = TableUtil.findTable(tables, keyword);
		List<String> tableNames = TableUtil.namesOf(keywordTables, true);

		return tableNames;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{dtbsSourceId}/findColumns", produces = CONTENT_TYPE_JSON)
	@ResponseBody
	public List<Column> findColumns(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model springModel, @PathVariable("dtbsSourceId") String dtbsSourceId,
			@RequestParam("table") final String table,
			@RequestParam(value = "keyword", required = false) String keyword) throws Throwable
	{
		final User user = getCurrentUser();

		Table tableObj = null;

		try
		{
			tableObj = new ReturnDtbsSourceConnTableExecutor<Table>(request, response, springModel, dtbsSourceId, table,
					true)
			{
				@Override
				protected Table execute(HttpServletRequest request, HttpServletResponse response,
						org.springframework.ui.Model springModel, DtbsSource dtbsSource, Table table) throws Exception
				{
					checkReadTableDataPermission(dtbsSource, user);

					return table;
				}
			}.execute();
		}
		catch (Throwable t)
		{
			// 避免出现TableNotFoundException导致界面出现错误提示
		}

		if (tableObj == null)
			return Collections.EMPTY_LIST;

		Column[] columns = tableObj.getColumns();
		
		// 这里无需排序，使用列定义顺序而非名称排序顺序更合适
		List<Column> keywordColumns = TableUtil.findColumn(columns, keyword);
		
		return keywordColumns;
	}
}
