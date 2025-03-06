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

import java.io.File;
import java.util.List;

import org.datagear.analysis.DataSetException;
import org.datagear.analysis.DataSetField;
import org.datagear.analysis.DataSetQuery;

/**
 * 简单Excel数据集。
 * <p>
 * 注意：此类不支持<code>Freemarker</code>模板语言。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class SimpleExcelDataSet extends AbstractExcelFileDataSet
{
	private static final long serialVersionUID = 1L;

	/** Excel文件 */
	private File file;

	public SimpleExcelDataSet()
	{
		super();
	}

	public SimpleExcelDataSet(String id, String name, File file)
	{
		super(id, name);
		this.file = file;
	}

	public SimpleExcelDataSet(String id, String name, List<DataSetField> fields, File file)
	{
		super(id, name, fields);
		this.file = file;
	}

	@Override
	protected FileResolvedInfo getExcelFile(DataSetQuery query) throws DataSetException
	{
		return new FileResolvedInfo(this.file);
	}
}
