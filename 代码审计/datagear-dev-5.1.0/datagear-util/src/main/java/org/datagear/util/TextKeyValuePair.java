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

package org.datagear.util;

/**
 * 文本{@linkplain KeyValuePair}。
 * 
 * @author datagear@163.com
 *
 * @param <V>
 */
public class TextKeyValuePair<V> implements KeyValuePair<String, V>
{
	private String key;

	private V value;

	public TextKeyValuePair()
	{
		super();
	}

	public TextKeyValuePair(String key, V value)
	{
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@Override
	public V getValue()
	{
		return value;
	}

	public void setValue(V value)
	{
		this.value = value;
	}
}
