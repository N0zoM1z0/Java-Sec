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

package org.datagear.webapp.config;

import org.datagear.util.IOUtil;
import org.datagear.web.config.ApplicationPropertiesConfigSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 属性配置。
 * 
 * @author datagear@163.com
 */
@Configuration
@PropertySource(value = ApplicationPropertiesConfigSupport.PROPERTY_SOURCE_PATH, encoding = IOUtil.CHARSET_UTF_8)
public class ApplicationPropertiesConfig extends ApplicationPropertiesConfigSupport
{
	public ApplicationPropertiesConfig()
	{
		super();
	}
}
