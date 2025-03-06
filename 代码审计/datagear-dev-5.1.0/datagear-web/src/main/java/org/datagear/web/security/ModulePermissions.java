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

package org.datagear.web.security;

import java.io.Serializable;

/**
 * 模块权限集。
 * 
 * @author datagear@163.com
 *
 */
public class ModulePermissions implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ModulePermission dtbsSourcePermission;

	private ModulePermission analysisProjectPermission;

	private ModulePermission dataSetPermission;

	private ModulePermission chartPermission;

	private ModulePermission dashboardPermission;

	public ModulePermissions()
	{
		super();
	}

	public ModulePermissions(ModulePermission dtbsSourcePermission, ModulePermission analysisProjectPermission,
			ModulePermission dataSetPermission, ModulePermission chartPermission, ModulePermission dashboardPermission)
	{
		super();
		this.dtbsSourcePermission = dtbsSourcePermission;
		this.analysisProjectPermission = analysisProjectPermission;
		this.dataSetPermission = dataSetPermission;
		this.chartPermission = chartPermission;
		this.dashboardPermission = dashboardPermission;
	}

	public ModulePermission getDtbsSourcePermission()
	{
		return dtbsSourcePermission;
	}

	public void setDtbsSourcePermission(ModulePermission dtbsSourcePermission)
	{
		this.dtbsSourcePermission = dtbsSourcePermission;
	}

	public ModulePermission getAnalysisProjectPermission()
	{
		return analysisProjectPermission;
	}

	public void setAnalysisProjectPermission(ModulePermission analysisProjectPermission)
	{
		this.analysisProjectPermission = analysisProjectPermission;
	}

	public ModulePermission getDataSetPermission()
	{
		return dataSetPermission;
	}

	public void setDataSetPermission(ModulePermission dataSetPermission)
	{
		this.dataSetPermission = dataSetPermission;
	}

	public ModulePermission getChartPermission()
	{
		return chartPermission;
	}

	public void setChartPermission(ModulePermission chartPermission)
	{
		this.chartPermission = chartPermission;
	}

	public ModulePermission getDashboardPermission()
	{
		return dashboardPermission;
	}

	public void setDashboardPermission(ModulePermission dashboardPermission)
	{
		this.dashboardPermission = dashboardPermission;
	}
}
