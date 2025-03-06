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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.datagear.analysis.DataSetField;
import org.datagear.analysis.DataSetQuery;
import org.datagear.analysis.DataSetResult;
import org.datagear.analysis.ResolvedDataSetResult;
import org.datagear.util.FileUtil;
import org.junit.Test;

/**
 * {@linkplain ExcelDirectoryFileDataSet}单元测试用例。
 * 
 * @author datagear@163.com
 *
 */
public class ExcelDirectoryFileDataSetTest
{
	private static final File DIRECTORY = FileUtil.getFile("src/test/resources/org/datagear/analysis/support/");

	@Test
	public void getResultTest_xlsx()
	{
		List<DataSetField> fields = new ArrayList<>();
		fields.add(new DataSetField("name", DataSetField.DataType.STRING));
		fields.add(new DataSetField("value", DataSetField.DataType.NUMBER));
		fields.add(new DataSetField("size", DataSetField.DataType.NUMBER));
		fields.add(new DataSetField("date", DataSetField.DataType.DATE));

		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", fields, DIRECTORY,
				"ExcelDirectoryFileDataSetTest-0.xlsx");
		dataSet.setNameRow(1);

		DataSetResult result = dataSet.getResult(DataSetQuery.valueOf());
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getData();

		{
			assertEquals(3, data.size());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(15, ((Number) row.get("value")).intValue());
				assertEquals(16, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-01", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(25, ((Number) row.get("value")).intValue());
				assertEquals(26, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-02", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(35, ((Number) row.get("value")).intValue());
				assertEquals(36, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-03", dateFormat.format((Date) row.get("date")));
			}
		}
	}

	@Test
	public void getResultTest_xlsx_convertFieldValue()
	{
		List<DataSetField> fields = new ArrayList<>();
		fields.add(new DataSetField("name", DataSetField.DataType.STRING));
		fields.add(new DataSetField("value", DataSetField.DataType.NUMBER));
		fields.add(new DataSetField("size", DataSetField.DataType.STRING));
		fields.add(new DataSetField("date", DataSetField.DataType.STRING));

		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", fields, DIRECTORY,
				"ExcelDirectoryFileDataSetTest-0.xlsx");
		dataSet.setNameRow(1);

		DataSetResult result = dataSet.getResult(DataSetQuery.valueOf());
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getData();

		{
			assertEquals(3, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(15, ((Number) row.get("value")).intValue());
				assertEquals("16", row.get("size"));
				assertEquals("2020-08-01", row.get("date"));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(25, ((Number) row.get("value")).intValue());
				assertEquals("26", row.get("size"));
				assertEquals("2020-08-02", row.get("date"));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(35, ((Number) row.get("value")).intValue());
				assertEquals("36", row.get("size"));
				assertEquals("2020-08-03", row.get("date"));
			}
		}
	}

	@Test
	public void resolveTest_xlsx()
	{
		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", DIRECTORY,
				"ExcelDirectoryFileDataSetTest-0.xlsx");
		dataSet.setNameRow(1);

		ResolvedDataSetResult resolvedResult = dataSet.resolve(DataSetQuery.valueOf());

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) resolvedResult.getResult().getData();
		List<DataSetField> fields = resolvedResult.getFields();

		{
			assertEquals(4, fields.size());

			{
				DataSetField field = fields.get(0);
				assertEquals("name", field.getName());
				assertEquals(DataSetField.DataType.STRING, field.getType());
			}

			{
				DataSetField field = fields.get(1);
				assertEquals("value", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(2);
				assertEquals("size", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(3);
				assertEquals("date", field.getName());
				assertEquals(DataSetField.DataType.DATE, field.getType());
			}
		}

		{
			assertEquals(3, data.size());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(15, ((Number) row.get("value")).intValue());
				assertEquals(16, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-01", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(25, ((Number) row.get("value")).intValue());
				assertEquals(26, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-02", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(35, ((Number) row.get("value")).intValue());
				assertEquals(36, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-03", dateFormat.format((Date) row.get("date")));
			}
		}
	}

	@Test
	public void resolveTest_xls()
	{
		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", DIRECTORY,
				"ExcelDirectoryFileDataSetTest-1.xls");
		dataSet.setNameRow(1);

		ResolvedDataSetResult resolvedResult = dataSet.resolve(DataSetQuery.valueOf());

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) resolvedResult.getResult().getData();
		List<DataSetField> fields = resolvedResult.getFields();

		{
			assertEquals(4, fields.size());

			{
				DataSetField field = fields.get(0);
				assertEquals("name", field.getName());
				assertEquals(DataSetField.DataType.STRING, field.getType());
			}

			{
				DataSetField field = fields.get(1);
				assertEquals("value", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(2);
				assertEquals("size", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(3);
				assertEquals("date", field.getName());
				assertEquals(DataSetField.DataType.DATE, field.getType());
			}
		}

		{
			assertEquals(3, data.size());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(15, ((Number) row.get("value")).intValue());
				assertEquals(16, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-01", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(25, ((Number) row.get("value")).intValue());
				assertEquals(26, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-02", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(35, ((Number) row.get("value")).intValue());
				assertEquals(36, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-03", dateFormat.format((Date) row.get("date")));
			}
		}
	}

	@Test
	public void resolveTest_dataRowColumnExp()
	{
		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", DIRECTORY,
				"ExcelDirectoryFileDataSetTest-0.xlsx");
		dataSet.setNameRow(1);
		dataSet.setDataRowExp("2,3-");
		dataSet.setDataColumnExp("A,C-");

		ResolvedDataSetResult resolvedResult = dataSet.resolve(DataSetQuery.valueOf());

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) resolvedResult.getResult().getData();
		List<DataSetField> fields = resolvedResult.getFields();

		{
			assertEquals(3, fields.size());

			{
				DataSetField field = fields.get(0);
				assertEquals("name", field.getName());
				assertEquals(DataSetField.DataType.STRING, field.getType());
			}

			{
				DataSetField field = fields.get(1);
				assertEquals("size", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(2);
				assertEquals("date", field.getName());
				assertEquals(DataSetField.DataType.DATE, field.getType());
			}
		}

		{
			assertEquals(3, data.size());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(16, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-01", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(26, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-02", dateFormat.format((Date) row.get("date")));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(36, ((Number) row.get("size")).intValue());
				assertEquals("2020-08-03", dateFormat.format((Date) row.get("date")));
			}
		}
	}

	@Test
	public void resolveTest_containsEmptyCell()
	{
		ExcelDirectoryFileDataSet dataSet = new ExcelDirectoryFileDataSet("a", "a", DIRECTORY,
				"ExcelDirectoryFileDataSetTest-2.xls");
		dataSet.setNameRow(1);

		ResolvedDataSetResult resolvedResult = dataSet.resolve(DataSetQuery.valueOf());

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) resolvedResult.getResult().getData();
		List<DataSetField> fields = resolvedResult.getFields();

		{
			assertEquals(3, fields.size());

			{
				DataSetField field = fields.get(0);
				assertEquals("a", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(1);
				assertEquals("b", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}

			{
				DataSetField field = fields.get(2);
				assertEquals("c", field.getName());
				assertEquals(DataSetField.DataType.DECIMAL, field.getType());
			}
		}

		{
			assertEquals(3, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals(1, ((Number) row.get("a")).intValue());
				assertEquals(2, ((Number) row.get("b")).intValue());
				assertEquals(null, row.get("c"));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals(3, ((Number) row.get("a")).intValue());
				assertEquals(null, row.get("b"));
				assertEquals(4, ((Number) row.get("c")).intValue());
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals(5, ((Number) row.get("a")).intValue());
				assertEquals(6, ((Number) row.get("b")).intValue());
				assertEquals(7, ((Number) row.get("c")).intValue());
			}
		}
	}
}
