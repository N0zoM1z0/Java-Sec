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

import org.datagear.analysis.DataSet;
import org.datagear.analysis.DataSetException;
import org.datagear.analysis.DataSetField;
import org.datagear.analysis.DataSetQuery;
import org.datagear.util.FileUtil;

/**
 * 目录内JSON文件{@linkplain DataSet}。
 * <p>
 * 注意：此类不支持<code>Freemarker</code>模板语言。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class JsonDirectoryFileDataSet extends AbstractJsonFileDataSet
{
	private static final long serialVersionUID = 1L;

	/** JSON文件所在的目录 */
	private File directory;

	/** JSON文件名 */
	private String fileName;

	public JsonDirectoryFileDataSet()
	{
		super();
	}

	public JsonDirectoryFileDataSet(String id, String name, File directory, String fileName)
	{
		super(id, name);
		this.directory = directory;
		this.fileName = fileName;
	}

	public JsonDirectoryFileDataSet(String id, String name, List<DataSetField> fields, File directory,
			String fileName)
	{
		super(id, name, fields);
		this.directory = directory;
		this.fileName = fileName;
	}

	public File getDirectory()
	{
		return directory;
	}

	public void setDirectory(File directory)
	{
		this.directory = directory;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	protected FileResolvedInfo getJsonFile(DataSetQuery query) throws DataSetException
	{
		File jsonFile = FileUtil.getFile(this.directory, this.fileName);
		return new FileResolvedInfo(jsonFile, this.fileName);
	}
}
