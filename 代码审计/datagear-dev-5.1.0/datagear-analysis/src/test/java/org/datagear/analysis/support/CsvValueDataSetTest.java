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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagear.analysis.DataSetParam;
import org.datagear.analysis.DataSetField;
import org.datagear.analysis.DataSetQuery;
import org.datagear.analysis.DataSetResult;
import org.junit.Test;

/**
 * {@linkplain CsvValueDataSet}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class CsvValueDataSetTest
{
	@Test
	public void getResultTest_hasParam()
	{
		List<DataSetField> fields = new ArrayList<>();
		fields.add(new DataSetField("name", DataSetField.DataType.STRING));
		fields.add(new DataSetField("value", DataSetField.DataType.NUMBER));
		fields.add(new DataSetField("size", DataSetField.DataType.NUMBER));

		List<DataSetParam> params = new ArrayList<>();
		params.add(new DataSetParam("size", DataSetParam.DataType.NUMBER, true));

		CsvValueDataSet dataSet = new CsvValueDataSet("a", "a", fields, "name, value, size \n aaa, 11, ${size}");
		dataSet.setParams(params);
		dataSet.setNameRow(1);

		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("size", 12);

		DataSetResult result = dataSet.getResult(DataSetQuery.valueOf(paramValues));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getData();

		{
			assertEquals(1, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(11, ((Number) row.get("value")).intValue());
				assertEquals(12, ((Number) row.get("size")).intValue());
			}
		}
	}

	@Test
	public void getResultTest_hasParam_convertFieldValue()
	{
		List<DataSetField> fields = new ArrayList<>();
		fields.add(new DataSetField("name", DataSetField.DataType.STRING));
		fields.add(new DataSetField("value", DataSetField.DataType.NUMBER));
		fields.add(new DataSetField("size", DataSetField.DataType.STRING));

		List<DataSetParam> params = new ArrayList<>();
		params.add(new DataSetParam("size", DataSetParam.DataType.NUMBER, true));

		CsvValueDataSet dataSet = new CsvValueDataSet("a", "a", fields, "name, value, size \n aaa, 11, ${size}");
		dataSet.setParams(params);
		dataSet.setNameRow(1);

		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("size", 12);

		DataSetResult result = dataSet.getResult(DataSetQuery.valueOf(paramValues));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getData();

		{
			assertEquals(1, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(11, ((Number) row.get("value")).intValue());
				assertEquals("12", row.get("size"));
			}
		}
	}

	@Test
	public void resolveTest_hasParam()
	{
		String name = "aa---\"---";
		int value = 11;
		int size = 12;
		String nameEscape = "aa---\"\"---";

		List<DataSetParam> params = new ArrayList<>();
		params.add(new DataSetParam("name", DataSetParam.DataType.STRING, true));
		params.add(new DataSetParam("size", DataSetParam.DataType.NUMBER, true));

		CsvValueDataSet dataSet = new CsvValueDataSet("a", "a",
				"name, value, size" //
						+ "\n" //
						+ "\"${name}\", " + value + ", ${size}");
		dataSet.setParams(params);
		dataSet.setNameRow(1);

		Map<String, Object> paramValues = new HashMap<>();
		paramValues.put("name", name);
		paramValues.put("size", size);

		TemplateResolvedDataSetResult result = dataSet.resolve(DataSetQuery.valueOf(paramValues));
		List<DataSetField> fields = result.getFields();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getResult().getData();

		assertEquals("name, value, size" //
				+ "\n" //
				+ "\"" + nameEscape + "\"" + ", " + value + ", " + size, result.getTemplateResult());

		{
			assertEquals(3, fields.size());

			{
				DataSetField field = fields.get(0);
				assertEquals("name", field.getName());
				assertEquals(DataSetField.DataType.STRING, field.getType());
			}

			{
				DataSetField field = fields.get(1);
				assertEquals("value", field.getName());
				assertEquals(DataSetField.DataType.NUMBER, field.getType());
			}

			{
				DataSetField field = fields.get(2);
				assertEquals("size", field.getName());
				assertEquals(DataSetField.DataType.NUMBER, field.getType());
			}
		}

		{
			assertEquals(1, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals(name, row.get("name"));
				assertEquals(value, ((Number) row.get("value")).intValue());
				assertEquals(size, ((Number) row.get("size")).intValue());
			}
		}
	}
}
