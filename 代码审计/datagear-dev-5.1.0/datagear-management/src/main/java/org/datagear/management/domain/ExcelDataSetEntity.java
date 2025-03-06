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

import java.io.File;
import java.util.Date;
import java.util.List;

import org.datagear.analysis.DataSetField;
import org.datagear.analysis.DataSetQuery;
import org.datagear.analysis.support.AbstractExcelFileDataSet;
import org.datagear.analysis.support.ExcelDirectoryFileDataSet;
import org.datagear.analysis.support.FileResolvedInfo;
import org.springframework.beans.BeanUtils;

/**
 * {@linkplain ExcelDirectoryFileDataSet}实体。
 * 
 * @author datagear@163.com
 *
 */
public class ExcelDataSetEntity extends AbstractExcelFileDataSet implements DirectoryFileDataSetEntity, CloneableEntity
{
	private static final long serialVersionUID = 1L;

	/** 文件源类型 */
	private String fileSourceType;

	/** 上传文件所在的目录 */
	private File directory = null;

	/** 上传文件名 */
	private String fileName = "";

	/** 展示名 */
	private String displayName = "";

	/** 服务器端文件源 */
	private FileSource fileSource = null;

	/** 服务器端文件的文件名（相对于{@linkplain #getFileSource()}） */
	private String dataSetResFileName = "";

	/** 创建用户 */
	private User createUser;

	/** 创建时间 */
	private Date createTime = null;

	/** 权限 */
	private int dataPermission = PERMISSION_NOT_LOADED;

	private AnalysisProject analysisProject = null;

	public ExcelDataSetEntity()
	{
		super();
	}

	public ExcelDataSetEntity(String id, String name, List<DataSetField> fields, File directory, String fileName,
			String displayName, User createUser)
	{
		super(id, name, fields);
		this.fileSourceType = FILE_SOURCE_TYPE_UPLOAD;
		this.directory = directory;
		this.fileName = fileName;
		this.displayName = displayName;
		this.createUser = createUser;
	}

	public ExcelDataSetEntity(String id, String name, List<DataSetField> fields,
			FileSource fileSource, String dataSetResFileName, User createUser)
	{
		super(id, name, fields);
		this.fileSourceType = FILE_SOURCE_TYPE_SERVER;
		this.fileSource = fileSource;
		this.dataSetResFileName = dataSetResFileName;
		this.createUser = createUser;
	}

	@Override
	public String getFileSourceType()
	{
		return fileSourceType;
	}

	@Override
	public void setFileSourceType(String fileSourceType)
	{
		this.fileSourceType = fileSourceType;
	}

	@Override
	public File getDirectory()
	{
		return directory;
	}

	@Override
	public void setDirectory(File directory)
	{
		this.directory = directory;
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

	@Override
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public String getDisplayName()
	{
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public FileSource getFileSource()
	{
		return fileSource;
	}

	@Override
	public void setFileSource(FileSource fileSource)
	{
		this.fileSource = fileSource;
	}

	@Override
	public String getDataSetResFileName()
	{
		return dataSetResFileName;
	}

	@Override
	public void setDataSetResFileName(String dataSetResFileName)
	{
		this.dataSetResFileName = dataSetResFileName;
	}

	@Override
	public String getDataSetType()
	{
		return DataSetEntity.DATA_SET_TYPE_Excel;
	}

	@Override
	public void setDataSetType(String dataSetType)
	{
		// XXX 什么也不做，不采用抛出异常的方式，便于统一底层SQL查询语句
		// throw new UnsupportedOperationException();
	}

	@Override
	public User getCreateUser()
	{
		return createUser;
	}

	@Override
	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}

	@Override
	public Date getCreateTime()
	{
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	@Override
	public int getDataPermission()
	{
		return dataPermission;
	}

	@Override
	public void setDataPermission(int dataPermission)
	{
		this.dataPermission = dataPermission;
	}

	@Override
	public AnalysisProject getAnalysisProject()
	{
		return analysisProject;
	}

	@Override
	public void setAnalysisProject(AnalysisProject analysisProject)
	{
		this.analysisProject = analysisProject;
	}

	@Override
	public String resolveTemplateFileName(String fileName, DataSetQuery query)
	{
		return resolveTemplatePlain(fileName, query);
	}

	@Override
	protected FileResolvedInfo getExcelFile(DataSetQuery query) throws Throwable
	{
		return getFileForDataSetQuery(query);
	}

	@Override
	public ExcelDataSetEntity clone()
	{
		ExcelDataSetEntity entity = new ExcelDataSetEntity();
		BeanUtils.copyProperties(this, entity);

		return entity;
	}
}
