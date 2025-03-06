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

import java.io.Serializable;

/**
 * 文本导出设置项。
 * 
 * @author datagear@163.com
 *
 */
public class TextDataExportOption implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 列值非法时设置为null */
	private boolean nullForIllegalColumnValue = false;

	public TextDataExportOption()
	{
		super();
	}

	public TextDataExportOption(boolean nullForIllegalColumnValue)
	{
		super();
		this.nullForIllegalColumnValue = nullForIllegalColumnValue;
	}

	public boolean isNullForIllegalColumnValue()
	{
		return nullForIllegalColumnValue;
	}

	public void setNullForIllegalColumnValue(boolean nullForIllegalColumnValue)
	{
		this.nullForIllegalColumnValue = nullForIllegalColumnValue;
	}
}
