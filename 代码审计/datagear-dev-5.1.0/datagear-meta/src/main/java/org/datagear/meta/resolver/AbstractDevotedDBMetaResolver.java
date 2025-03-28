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

package org.datagear.meta.resolver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datagear.meta.Column;
import org.datagear.meta.DataType;
import org.datagear.meta.Database;
import org.datagear.meta.ImportKey;
import org.datagear.meta.PrimaryKey;
import org.datagear.meta.SearchableType;
import org.datagear.meta.SimpleTable;
import org.datagear.meta.Table;
import org.datagear.meta.TableType;
import org.datagear.meta.UniqueKey;
import org.datagear.util.JDBCCompatiblity;
import org.datagear.util.JdbcSupport;
import org.datagear.util.JdbcUtil;
import org.datagear.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象{@linkplain DevotedDBMetaResolver}。
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractDevotedDBMetaResolver extends JdbcSupport implements DevotedDBMetaResolver
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDevotedDBMetaResolver.class);

	protected static final String[] DEFAULT_TABLE_TYPES = { TableType.TABLE, TableType.VIEW, TableType.SYSTEM_TABLE,
			TableType.GLOBAL_TEMPORARY, TableType.LOCAL_TEMPORARY, TableType.ALIAS, TableType.SYNONYM };

	protected static final String[] EMPTY_STRING_ARRAY = new String[0];

	private TableTypeResolver tableTypeResolver = new DefaultTableTypeResolver();

	private DbMetaSupport dbMetaSupport = new DbMetaSupport();

	public AbstractDevotedDBMetaResolver()
	{
		super();
	}

	public TableTypeResolver getTableTypeResolver()
	{
		return tableTypeResolver;
	}

	public void setTableTypeResolver(TableTypeResolver tableTypeResolver)
	{
		this.tableTypeResolver = tableTypeResolver;
	}

	public DbMetaSupport getDbMetaSupport()
	{
		return dbMetaSupport;
	}

	public void setDbMetaSupport(DbMetaSupport dbMetaSupport)
	{
		this.dbMetaSupport = dbMetaSupport;
	}

	@Override
	public Database getDatabase(Connection cn) throws DBMetaResolverException
	{
		try
		{
			DatabaseMetaData metaData = cn.getMetaData();

			Database databaseInfo = new Database();

			databaseInfo.setCatalog(getCatalog(cn));
			databaseInfo.setSchema(getSchema(cn, metaData));

			databaseInfo.setUrl(metaData.getURL());
			databaseInfo.setUser(metaData.getUserName());
			databaseInfo.setProductName(metaData.getDatabaseProductName());
			databaseInfo.setProductVersion(metaData.getDatabaseProductVersion());
			databaseInfo.setDriverName(metaData.getDriverName());
			databaseInfo.setDriverVersion(metaData.getDriverVersion());

			return databaseInfo;
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
	}

	@Override
	public List<SimpleTable> getTables(Connection cn) throws DBMetaResolverException
	{
		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		return getSimpleTables(cn, metaData, catalog, schema, null);
	}

	@Override
	public List<SimpleTable> getDataTables(Connection cn) throws DBMetaResolverException
	{
		List<SimpleTable> tables = getTables(cn);

		List<SimpleTable> re = new ArrayList<>(tables.size());

		List<Boolean> dts = isDataTables(cn, tables);
		for (int i = 0, len = tables.size(); i < len; i++)
		{
			if (Boolean.TRUE.equals(dts.get(i)))
			{
				re.add(tables.get(i));
			}
		}

		return re;
	}

	@Override
	public List<SimpleTable> getEntityTables(Connection cn) throws DBMetaResolverException
	{
		List<SimpleTable> tables = getTables(cn);

		List<SimpleTable> re = new ArrayList<>(tables.size());

		List<Boolean> dts = isEntityTables(cn, tables);
		for (int i = 0, len = tables.size(); i < len; i++)
		{
			if (Boolean.TRUE.equals(dts.get(i)))
			{
				re.add(tables.get(i));
			}
		}

		return re;
	}

	@Override
	public SimpleTable getRandomDataTable(Connection cn) throws DBMetaResolverException
	{
		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		return getRandomDataTable(cn, metaData, catalog, schema);
	}

	@Override
	public String[] getTableTypes(Connection cn) throws DBMetaResolverException
	{
		return this.tableTypeResolver.getTableTypes(cn);
	}

	@Override
	public boolean isDataTable(Connection cn, SimpleTable table) throws DBMetaResolverException
	{
		return this.tableTypeResolver.isDataTable(cn, table);
	}

	@Override
	public boolean[] isDataTables(Connection cn, SimpleTable[] tables) throws DBMetaResolverException
	{
		return this.tableTypeResolver.isDataTables(cn, tables);
	}

	@Override
	public List<Boolean> isDataTables(Connection cn, List<? extends SimpleTable> tables) throws DBMetaResolverException
	{
		return this.tableTypeResolver.isDataTables(cn, tables);
	}

	@Override
	public boolean isEntityTable(Connection cn, SimpleTable table) throws DBMetaResolverException
	{
		return this.tableTypeResolver.isEntityTable(cn, table);
	}

	@Override
	public boolean[] isEntityTables(Connection cn, SimpleTable[] tables) throws DBMetaResolverException
	{
		return this.tableTypeResolver.isEntityTables(cn, tables);
	}

	@Override
	public List<Boolean> isEntityTables(Connection cn, List<? extends SimpleTable> tables)
			throws DBMetaResolverException
	{
		return this.tableTypeResolver.isEntityTables(cn, tables);
	}

	@Override
	public String getExactTableName(Connection cn, String tableName) throws DBMetaResolverException
	{
		String[] re = getExactTableNames(cn, new String[] { tableName });
		return re[0];
	}

	@Override
	public String[] getExactTableNames(Connection cn, String[] tableNames) throws DBMetaResolverException
	{
		String[] re = new String[tableNames.length];

		Arrays.fill(re, null);

		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		ResultSet rs = null;
		String[] tableTypes = getTableTypes(cn);

		try
		{
			rs = getTableResulSet(cn, metaData, catalog, schema, null, tableTypes);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			while (rs.next())
			{
				String name = mrs.getString("TABLE_NAME", null);

				if (name != null)
				{
					int okCount = 0;

					for (int i = 0; i < tableNames.length; i++)
					{
						if (re[i] != null && re[i].equals(tableNames[i]))
							continue;

						if (name.equals(tableNames[i]))
						{
							re[i] = name;
							okCount++;
						}
						else if (re[i] == null && name.equalsIgnoreCase(tableNames[i]))
						{
							re[i] = name;
						}
					}

					if (okCount == tableNames.length)
						break;
				}
			}
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}

		return re;
	}

	@Override
	public Table getTable(Connection cn, String tableName) throws TableNotFoundException, DBMetaResolverException
	{
		@JDBCCompatiblity("如果cn为readonly，某些驱动程序的DatabaseMetaData.isReadOnly()也将为true（比如：Postgresql JDBC 42.2.5），"
				+ "这会导致解析Table.readonly不正确，因此这里设为false，以保证解析正确")
		boolean readonly = JdbcUtil.isReadonlyIfSupports(cn, true);
		if (readonly)
			JdbcUtil.setReadonlyIfSupports(cn, false);

		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		return getTable(cn, metaData, catalog, schema, tableName);
	}

	@Override
	public Column[] getColumns(Connection cn, String tableName) throws DBMetaResolverException
	{
		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		return getColumns(cn, metaData, catalog, schema, tableName, null);
	}

	@Override
	public Column getRandomColumn(Connection cn, String tableName) throws DBMetaResolverException
	{
		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		Column[] columns = getColumns(cn, metaData, catalog, schema, tableName, 1);

		return (columns == null || columns.length < 1 ? null : columns[0]);
	}

	@Override
	public Column[] getColumns(Connection cn, ResultSetMetaData resultSetMetaData) throws DBMetaResolverException
	{
		try
		{
			List<Column> columns = getColumnsByResultSetMetaData(cn, resultSetMetaData);
			return columns.toArray(new Column[columns.size()]);
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
	}

	@Override
	public PrimaryKey getPrimaryKey(Connection cn, String tableName) throws DBMetaResolverException
	{
		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		return getPrimaryKey(cn, metaData, catalog, schema, tableName);
	}

	@Override
	public List<DataType> getDataTypes(Connection cn) throws DBMetaResolverException
	{
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		return getDataTypes(cn, metaData);
	}

	@Override
	public List<String[]> getImportTables(Connection cn, String... tableNames)
	{
		List<String[]> importTabless = new ArrayList<>(tableNames.length);

		String catalog = getCatalog(cn);
		DatabaseMetaData metaData = getDatabaseMetaData(cn);
		String schema = getSchema(cn, metaData);

		for (int i = 0; i < tableNames.length; i++)
		{
			String[] importTables = null;

			if (StringUtil.isEmpty(tableNames[i]))
				importTables = EMPTY_STRING_ARRAY;
			else
			{
				// 处理重复表
				for (int k = 0; i < i; k++)
				{
					if (tableNames[k].equals(tableNames[i]))
					{
						importTables = importTabless.get(k);
						break;
					}
				}

				if (importTables == null)
				{
					ImportKey[] importKeys = getImportKeys(cn, metaData, catalog, schema, tableNames[i]);

					if (importKeys == null || importKeys.length == 0)
						importTables = EMPTY_STRING_ARRAY;
					else
					{
						List<String> importedTableList = new ArrayList<>(2);

						for (int j = 0; j < importKeys.length; j++)
						{
							String primaryTable = importKeys[j].getPrimaryTableName();

							if (!importedTableList.contains(primaryTable))
								importedTableList.add(primaryTable);
						}

						importTables = new String[importedTableList.size()];
						importedTableList.toArray(importTables);
					}
				}
			}

			importTabless.add(importTables);
		}

		return importTabless;
	}

	/**
	 * @param cn
	 * @param metaData
	 * @param schema
	 * @param tableNamePattern
	 *            允许为{@code null}
	 * @return
	 * @throws DBMetaResolverException
	 */
	protected List<SimpleTable> getSimpleTables(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableNamePattern) throws DBMetaResolverException
	{
		ResultSet rs = null;

		String[] tableTypes = getTableTypes(cn);

		try
		{
			rs = getTableResulSet(cn, metaData, catalog, schema, tableNamePattern, tableTypes);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			List<SimpleTable> simpleTables = new ArrayList<>();

			while (rs.next())
			{
				SimpleTable simpleTable = readSimpleTable(cn, metaData, catalog, schema, mrs);

				if (simpleTable != null)
				{
					simpleTable = postProcessSimpleTable(cn, metaData, schema, simpleTable);
					simpleTables.add(simpleTable);
				}
			}

			return simpleTables;
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}
	}

	/**
	 * @return 返回{@code null}表示未读取到
	 */
	protected SimpleTable readSimpleTable(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			MetaResultSet rs)
	{
		try
		{
			String name = rs.getString("TABLE_NAME", null);
			String type = TableType.toTableType(rs.getString("TABLE_TYPE", ""));

			if (StringUtil.isEmpty(name) || StringUtil.isEmpty(type))
			{
				LOGGER.warn("invalid table : name={}, type={}", name, type);
				return null;
			}

			SimpleTable simpleTable = new SimpleTable(name, type);
			simpleTable.setComment(rs.getString("REMARKS", ""));

			return simpleTable;
		}
		catch (SQLException e)
		{
			return null;
		}
	}

	protected SimpleTable postProcessSimpleTable(Connection cn, DatabaseMetaData metaData, String schema,
			SimpleTable simpleTable) throws SQLException
	{
		return simpleTable;
	}

	protected List<DataType> getDataTypes(Connection cn, DatabaseMetaData metaData) throws DBMetaResolverException
	{
		List<DataType> dataTypes = new ArrayList<>();

		ResultSet rs = null;

		try
		{
			rs = getDataTypeResultSet(cn, metaData);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			while (rs.next())
			{
				DataType dataType = readDataType(mrs);

				if (dataType != null)
					dataTypes.add(dataType);
			}

			return dataTypes;
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}
	}

	protected Table getTable(Connection cn, DatabaseMetaData metaData, String catalog, String schema, String tableName)
			throws TableNotFoundException, DBMetaResolverException
	{
		boolean readonly = resolveTableReadonly(cn);

		List<SimpleTable> simpleTables = getSimpleTables(cn, metaData, catalog, schema, tableName);

		if (simpleTables == null || simpleTables.isEmpty())
			throw new TableNotFoundException(tableName);

		SimpleTable simpleTable = simpleTables.get(0);
		tableName = simpleTable.getName();

		Table table = new Table();
		table.setName(tableName);
		table.setType(simpleTable.getType());
		table.setComment(simpleTable.getComment());
		table.setColumns(getColumns(cn, metaData, catalog, schema, tableName, null));
		table.setPrimaryKey(getPrimaryKey(cn, metaData, catalog, schema, tableName));
		table.setUniqueKeys(getUniqueKeys(cn, metaData, catalog, schema, tableName));
		table.setImportKeys(getImportKeys(cn, metaData, catalog, schema, tableName));
		table.setReadonly(readonly);

		table = postProcessTable(cn, metaData, schema, table);

		return table;
	}

	protected boolean resolveTableReadonly(Connection cn)
	{
		@JDBCCompatiblity("如果cn为readonly，某些驱动程序的DatabaseMetaData.isReadOnly()也将为true（比如：Postgresql JDBC 42.2.5），"
				+ "这会导致解析Table.readonly不正确，因此这里尝试设为false，来测试数据库是否为只读")
		boolean cnReadonly = JdbcUtil.isReadonlyIfSupports(cn, true);
		if (cnReadonly)
			JdbcUtil.setReadonlyIfSupports(cn, false);

		try
		{
			// 数据库是否只读
			boolean dbReadonly = cn.getMetaData().isReadOnly();
			return dbReadonly;
		}
		catch (SQLException e)
		{
			return false;
		}
	}

	protected Table postProcessTable(Connection cn, DatabaseMetaData metaData, String schema, Table table)
	{
		return table;
	}

	/**
	 * 
	 * @param cn
	 * @param databaseMetaData
	 * @param catalog
	 * @param schema
	 * @param tableNamePattern
	 *            为{@code null}或空则查询全部
	 * @param tableTypes
	 *            为{@code null}查询所有类型
	 * @return
	 * @throws SQLException
	 */
	protected ResultSet getTableResulSet(Connection cn, DatabaseMetaData databaseMetaData, String catalog,
			String schema, String tableNamePattern, String[] tableTypes) throws SQLException
	{
		if (tableNamePattern == null || tableNamePattern.isEmpty())
			tableNamePattern = "%";

		return databaseMetaData.getTables(catalog, schema, tableNamePattern, tableTypes);
	}

	/**
	 * 
	 * @param cn
	 * @param metaData
	 * @param schema
	 * @param tableName
	 * @param count
	 *            为{@code null}获取全部，否则获取指定数目
	 * @return
	 * @throws DBMetaResolverException
	 */
	protected Column[] getColumns(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableName, Integer count) throws DBMetaResolverException
	{
		ResultSet rs = null;

		try
		{
			rs = getColumnResulSet(cn, metaData, catalog, schema, tableName);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			List<Column> columns = new ArrayList<>();
			
			boolean hasColumnInMeta = false;
			
			while (rs.next())
			{
				if(hasColumnInMeta == false)
					hasColumnInMeta = true;
				
				Column column = readColumn(cn, metaData, schema, tableName, mrs);
				column = postProcessColumn(cn, metaData, schema, tableName, column);
				addValidColumn(columns, column);

				if (count != null && columns.size() == count)
					break;
			}
			
			if(!hasColumnInMeta)
			{
				@JDBCCompatiblity("某些类型的表（比如Oracle的同义词），不能通过DatabaseMetaData的getColumns获取列信息，所以，这里采用查询结果集的方式再次读取列信息")
				List<Column> columnsByQuery = getColumnsByQuery(cn, metaData, catalog, schema, tableName);
				
				for(int i=0,len=columnsByQuery.size(); i<len; i++)
				{
					Column column = postProcessColumn(cn, metaData, schema, tableName, columnsByQuery.get(i));
					addValidColumn(columns, column);

					if (count != null && columns.size() == count)
						break;
				}
			}
			
			sortColumns(columns);

			return columns.toArray(new Column[columns.size()]);
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}
	}
	
	protected List<Column> getColumnsByQuery(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableName) throws SQLException
	{
		String iq = getIdentifierQuote(cn);
		String sql = "SELECT * FROM " + iq + tableName + iq;
		
		Statement st = null;
		ResultSet rs = null;
		
		try
		{
			st = cn.createStatement();
			st.setFetchSize(1);
			rs = st.executeQuery(sql);
			
			return getColumnsByResultSetMetaData(cn, rs.getMetaData());
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
			JdbcUtil.closeStatement(st);
		}
	}

	protected List<Column> getColumnsByResultSetMetaData(Connection cn, ResultSetMetaData resultSetMetaData) throws SQLException
	{
		int columnCount = resultSetMetaData.getColumnCount();

		List<Column> columns = new ArrayList<Column>(columnCount);

		for (int i = 1; i <= columnCount; i++)
		{
			Column column = new Column();

			String columnName = getColumnName(resultSetMetaData, i);

			column.setName(columnName);
			column.setType(resultSetMetaData.getColumnType(i));
			column.setTypeName(resultSetMetaData.getColumnTypeName(i));
			column.setSize(resultSetMetaData.getPrecision(i));
			column.setDecimalDigits(resultSetMetaData.getScale(i));
			column.setNullable(DatabaseMetaData.columnNoNulls != resultSetMetaData.isNullable(i));
			column.setAutoincrement(resultSetMetaData.isAutoIncrement(i));

			resolveSortable(column);
			resolveSearchableType(column);

			resolveDefaultValue(column);

			columns.add(column);
		}

		return columns;
	}

	/**
	 * @return 返回{@code null}表示未读取到
	 */
	protected Column readColumn(Connection cn, DatabaseMetaData metaData, String schema, String tableName,
			MetaResultSet rs)
	{
		try
		{
			String name = rs.getString("COLUMN_NAME", null);
			Integer type = rs.getInt("DATA_TYPE", null);

			if (StringUtil.isEmpty(name) || type == null)
			{
				LOGGER.warn("invalid column : name={}, type={}", name, type);
				return null;
			}

			Column column = new Column(name, type);

			column.setTypeName(rs.getString("TYPE_NAME", ""));
			column.setSize(rs.getInt("COLUMN_SIZE", 0));
			column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS", 0));
			column.setNullable(
					DatabaseMetaData.columnNoNulls != rs.getInt("NULLABLE", DatabaseMetaData.columnNullable));
			column.setComment(rs.getString("REMARKS", ""));
			column.setDefaultValue(rs.getString("COLUMN_DEF", null));
			column.setAutoincrement("yes".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT", "no")));
			column.setPosition(rs.getInt("ORDINAL_POSITION", 1));

			resolveSortable(cn, metaData, schema, tableName, column);
			resolveSearchableType(cn, metaData, schema, tableName, column);

			resolveDefaultValue(column);

			return column;
		}
		catch (SQLException e)
		{
			return null;
		}
	}

	protected void resolveDefaultValue(Column column)
	{
		if (!column.hasDefaultValue())
			return;

		String value = column.getDefaultValue();
		int len = value.length();

		// 移除开头和结尾的引号
		if (len >= 2 && ((value.charAt(0) == '\'' && value.charAt(len - 1) == '\'')
				|| (value.charAt(0) == '"' && value.charAt(len - 1) == '"')))
		{
			value = (len == 2 ? "" : value.substring(1, value.length() - 1));
			column.setDefaultValue(value);
		}

		if (StringUtil.isEmpty(value))
			return;

	}

	@JDBCCompatiblity("很多驱动程序的值为SearchableType.ALL但实际并不支持LIKE语法（比如：PostgreSQL JDBC 42.2.5），"
			+ "这里为了兼容，不采用数据库级的SearchableType逻辑")
	protected void resolveSearchableType(Connection cn, DatabaseMetaData metaData, String schema, String tableName,
			Column column)
	{
		resolveSearchableType(column);
	}

	protected void resolveSearchableType(Column column)
	{
		SearchableType searchableType = null;

		int sqlType = column.getType();

		if (Types.CHAR == sqlType || Types.VARCHAR == sqlType || Types.NCHAR == sqlType || Types.NVARCHAR == sqlType)
			searchableType = SearchableType.ALL;

		column.setSearchableType(searchableType);
	}

	@JDBCCompatiblity("某些驱动程序对有些类型不支持排序（比如Oracle对于BLOB类型）")
	protected void resolveSortable(Connection cn, DatabaseMetaData metaData, String schema, String tableName,
			Column column)
	{
		resolveSortable(column);
	}

	protected void resolveSortable(Column column)
	{
		int sqlType = column.getType();

		boolean sortable = (Types.BIGINT == sqlType || Types.BIT == sqlType || Types.BOOLEAN == sqlType
				|| Types.CHAR == sqlType || Types.DATE == sqlType || Types.DECIMAL == sqlType || Types.DOUBLE == sqlType
				|| Types.FLOAT == sqlType || Types.INTEGER == sqlType || Types.NCHAR == sqlType
				|| Types.NUMERIC == sqlType || Types.NVARCHAR == sqlType || Types.REAL == sqlType
				|| Types.SMALLINT == sqlType || Types.TIME == sqlType || Types.TIMESTAMP == sqlType
				|| Types.TINYINT == sqlType || Types.VARCHAR == sqlType);

		column.setSortable(sortable);
	}

	protected Column postProcessColumn(Connection cn, DatabaseMetaData metaData, String schema, String tableName,
			Column column) throws SQLException
	{
		return column;
	}

	protected ResultSet getColumnResulSet(Connection cn, DatabaseMetaData databaseMetaData, String catalog,
			String schema, String tableName) throws SQLException
	{
		return databaseMetaData.getColumns(catalog, schema, tableName, "%");
	}

	/**
	 * 
	 * @param cn
	 * @param metaData
	 * @param schema
	 * @param tableName
	 * @return 返回{@code null}表示无主键。
	 * @throws DBMetaResolverException
	 */
	protected PrimaryKey getPrimaryKey(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableName) throws DBMetaResolverException
	{
		PrimaryKey primaryKey = null;

		ResultSet rs = null;
		try
		{
			rs = getPrimaryKeyResulSet(cn, metaData, catalog, schema, tableName);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			List<String> columnNames = new ArrayList<>();
			String keyName = null;

			while (rs.next())
			{
				String columnName = mrs.getString("COLUMN_NAME", null);

				if (StringUtil.isEmpty(keyName))
					keyName = mrs.getString("PK_NAME", "");

				addValidName(columnNames, columnName);
			}

			if (!columnNames.isEmpty())
			{
				primaryKey = new PrimaryKey(columnNames.toArray(new String[columnNames.size()]));
				primaryKey.setKeyName(keyName);
			}

			return primaryKey;
		}
		catch (SQLException e)
		{
			LOGGER.warn("return null primary key object for exception", e);

			@JDBCCompatiblity("当tableName是视图时，某些驱动（比如Oracle）可能会抛出SQLSyntaxErrorException异常")
			PrimaryKey nullPrimaryKey = null;
			return nullPrimaryKey;
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}
	}

	/**
	 * @param cn
	 * @param metaData
	 * @param schema
	 * @param tableName
	 * @return 返回{@code null}表示无唯一键
	 * @throws DBMetaResolverException
	 */
	protected UniqueKey[] getUniqueKeys(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableName) throws DBMetaResolverException
	{
		UniqueKey[] uniqueKeys = null;

		ResultSet rs = null;

		List<String> keyNames = new ArrayList<>();
		List<List<String>> keyColumnNamess = new ArrayList<>();

		try
		{
			rs = getUniqueKeyResulSet(cn, metaData, catalog, schema, tableName);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			while (rs.next())
			{
				@JDBCCompatiblity("某些驱动程序INDEX_NAME列可能为nul，但COLUMN_NAME不为null，此行应是有效的，"
						+ "而某些驱动程序会返回INDEX_NAME和COLUMN_NAME都为null的无效行，所以，这里统一先把它们整理出来，下面再筛选过滤")
				String keyName = mrs.getString("INDEX_NAME", "");
				String columnName = mrs.getString("COLUMN_NAME", null);

				int myIndex = keyNames.indexOf(keyName);
				List<String> keyColumnNames = null;

				if (myIndex < 0)
				{
					keyNames.add(keyName);
					keyColumnNames = new ArrayList<>();
					keyColumnNamess.add(keyColumnNames);
				}
				else
					keyColumnNames = keyColumnNamess.get(myIndex);

				addValidName(keyColumnNames, columnName);
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("return null unique key object for exception", e);

			@JDBCCompatiblity("当tableName是视图时，某些驱动（比如Oracle）可能会抛出SQLSyntaxErrorException异常")
			UniqueKey[] nullUniqueKeys = null;
			return nullUniqueKeys;
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}

		if (!keyNames.isEmpty())
		{
			List<UniqueKey> uks = new ArrayList<>();

			for (int i = 0; i < keyNames.size(); i++)
			{
				List<String> keyColumnNames = keyColumnNamess.get(i);

				// 忽略无效的
				if (keyColumnNames.isEmpty())
					continue;

				UniqueKey uk = new UniqueKey(keyColumnNames.toArray(new String[keyColumnNames.size()]));
				uk.setKeyName(keyNames.get(i));

				uks.add(uk);
			}

			if (!uks.isEmpty())
				uniqueKeys = uks.toArray(new UniqueKey[uks.size()]);
		}

		return uniqueKeys;
	}

	protected ImportKey[] getImportKeys(Connection cn, DatabaseMetaData metaData, String catalog, String schema,
			String tableName) throws DBMetaResolverException
	{
		ImportKey[] importKeys = null;

		ResultSet rs = null;

		List<String> keyNames = new ArrayList<>();
		List<List<String>> columnNamess = new ArrayList<>();
		List<String> primaryTableNames = new ArrayList<>();
		List<List<String>> primaryColumnNamess = new ArrayList<>();

		try
		{
			rs = getImportKeyResulSet(cn, metaData, catalog, schema, tableName);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			while (rs.next())
			{
				String keyName = mrs.getString("FK_NAME", "");

				String columnName = mrs.getString("FKCOLUMN_NAME", null);
				String primaryColumnName = mrs.getString("PKCOLUMN_NAME", null);

				int myIndex = keyNames.indexOf(keyName);
				List<String> columnNames = null;
				List<String> primaryColumnNames = null;

				if (myIndex < 0)
				{
					keyNames.add(keyName);
					primaryTableNames.add(mrs.getString("PKTABLE_NAME", ""));

					columnNames = new ArrayList<>();
					columnNamess.add(columnNames);

					primaryColumnNames = new ArrayList<>();
					primaryColumnNamess.add(primaryColumnNames);
				}
				else
				{
					columnNames = columnNamess.get(myIndex);
					primaryColumnNames = primaryColumnNamess.get(myIndex);
				}

				addValidName(columnNames, columnName);
				addValidName(primaryColumnNames, primaryColumnName);
			}
		}
		catch (SQLException e)
		{
			LOGGER.warn("return null import key object for exception", e);

			@JDBCCompatiblity("当tableName是视图时，某些驱动（比如Oracle）可能会抛出SQLSyntaxErrorException异常")
			ImportKey[] nullImportKeys = null;
			return nullImportKeys;
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}

		if (!keyNames.isEmpty())
		{
			importKeys = new ImportKey[keyNames.size()];

			for (int i = 0; i < importKeys.length; i++)
			{
				ImportKey importKey = new ImportKey();

				List<String> columnNames = columnNamess.get(i);
				List<String> primaryColumnNames = primaryColumnNamess.get(i);

				importKey.setColumnNames(columnNames.toArray(new String[columnNames.size()]));
				importKey.setPrimaryTableName(primaryTableNames.get(i));
				importKey.setPrimaryColumnNames(primaryColumnNames.toArray(new String[primaryColumnNames.size()]));
				importKey.setKeyName(keyNames.get(i));

				importKeys[i] = importKey;
			}
		}

		return importKeys;
	}

	/**
	 * 添加非空、且不重名的名称元素。
	 * 
	 * @param names
	 * @param name
	 * @return
	 */
	@JDBCCompatiblity("避免某些驱动程序的结果集出现非法或者重复项")
	protected boolean addValidName(List<String> names, String name)
	{
		if (StringUtil.isEmpty(name) || names.indexOf(name) > -1)
			return false;

		names.add(name);
		return true;
	}

	/**
	 * 添加非空、且不重名的列元素。
	 * 
	 * @param columns
	 * @param column
	 * @return
	 */
	@JDBCCompatiblity("避免某些驱动程序的结果集出现非法或者重复项")
	protected boolean addValidColumn(List<Column> columns, Column column)
	{
		if (column == null || containsColumn(columns, column.getName()))
			return false;

		columns.add(column);
		return true;
	}

	protected boolean containsColumn(List<Column> columns, String name)
	{
		for (Column column : columns)
		{
			if (column.getName().equals(name))
				return true;
		}

		return false;
	}

	protected void sortColumns(List<Column> columns)
	{
		Collections.sort(columns, COLUMN_SORT_COMPARATOR);
	}

	protected DataType readDataType(MetaResultSet rs)
	{
		try
		{
			String name = rs.getString("TYPE_NAME", null);
			Integer type = rs.getInt("DATA_TYPE", null);

			if (StringUtil.isEmpty(name) || type == null)
			{
				LOGGER.warn("invalid data type : name={}, type={}", name, type);
				return null;
			}

			DataType column = new DataType(name, type);

			return column;
		}
		catch (SQLException e)
		{
			return null;
		}
	}

	protected ResultSet getPrimaryKeyResulSet(Connection cn, DatabaseMetaData databaseMetaData, String catalog,
			String schema, String tableName) throws SQLException
	{
		return databaseMetaData.getPrimaryKeys(catalog, schema, tableName);
	}

	protected ResultSet getUniqueKeyResulSet(Connection cn, DatabaseMetaData databaseMetaData, String catalog,
			String schema, String tableName) throws SQLException
	{
		return databaseMetaData.getIndexInfo(catalog, schema, tableName, true, false);
	}

	protected ResultSet getImportKeyResulSet(Connection cn, DatabaseMetaData databaseMetaData, String catalog,
			String schema, String tableName) throws SQLException
	{
		return databaseMetaData.getImportedKeys(catalog, schema, tableName);
	}

	protected ResultSet getDataTypeResultSet(Connection cn, DatabaseMetaData databaseMetaData) throws SQLException
	{
		return databaseMetaData.getTypeInfo();
	}

	protected SimpleTable getRandomDataTable(Connection cn, DatabaseMetaData metaData, String catalog, String schema)
			throws DBMetaResolverException
	{
		SimpleTable simpleTable = null;

		String[] tableTypes = getTableTypes(cn);

		ResultSet rs = null;

		try
		{
			rs = getTableResulSet(cn, metaData, catalog, schema, null, tableTypes);
			MetaResultSet mrs = MetaResultSet.valueOf(rs);

			while (rs.next())
			{
				simpleTable = readSimpleTable(cn, metaData, catalog, schema, mrs);

				if (simpleTable != null && isDataTable(cn, simpleTable))
					return simpleTable;
			}

			return null;
		}
		catch (SQLException e)
		{
			throw new DBMetaResolverException(e);
		}
		finally
		{
			JdbcUtil.closeResultSet(rs);
		}
	}

	protected DatabaseMetaData getDatabaseMetaData(Connection cn) throws DBMetaResolverException
	{
		return this.dbMetaSupport.getDatabaseMetaData(cn);
	}

	/**
	 * 获取当前连接的catalog。
	 * 
	 * @param cn
	 * @return
	 * @throws SQLException
	 */
	protected String getCatalog(Connection cn) throws DBMetaResolverException
	{
		return this.dbMetaSupport.getCatalog(cn);
	}

	/**
	 * 获取当前连接的schema。
	 * 
	 * @param cn
	 * @param databaseMetaData
	 * @return
	 * @throws DBMetaResolverException
	 */
	protected String getSchema(Connection cn, DatabaseMetaData databaseMetaData) throws DBMetaResolverException
	{
		return this.dbMetaSupport.getSchema(cn, databaseMetaData);
	}

	/**
	 * 获取标识符引用符。
	 * 
	 * @param cn
	 * @return
	 */
	protected String getIdentifierQuote(Connection cn)
	{
		return this.dbMetaSupport.getIdentifierQuote(cn);
	}
	
	protected static final Comparator<Column> COLUMN_SORT_COMPARATOR = new Comparator<Column>()
	{
		@Override
		public int compare(Column o1, Column o2)
		{
			Integer p1 = o1.getPosition();
			return p1.compareTo(o2.getPosition());
		}
	};

	/**
	 * 元信息结果集。
	 * 
	 * @author datagear@163.com
	 *
	 */
	@JDBCCompatiblity("某些驱动程序的元信息结果集并不符合JDBC规范，比如字段名缺失、不匹配等等，所以这里特别封装此类")
	protected static class MetaResultSet extends JdbcSupport
	{
		private ResultSet resultSet;
		private Map<String, Integer> _nameColumnIndexMap = new HashMap<>();

		public MetaResultSet(ResultSet resultSet)
		{
			super();
			this.resultSet = resultSet;
		}

		/**
		 * 获取列值。
		 * 
		 * @param name
		 * @param defaultValue
		 * @return
		 * @throws SQLException
		 */
		public String getString(String name, String defaultValue) throws SQLException
		{
			int columnIndex = getColumnIndexAndCache(this.resultSet, name);

			if (columnIndex < 1)
				return defaultValue;

			String value = this.resultSet.getString(columnIndex);

			return (value == null || this.resultSet.wasNull() ? defaultValue : value);
		}

		/**
		 * 获取列值。
		 * 
		 * @param name
		 * @param defaultValue
		 * @return
		 * @throws SQLException
		 */
		public Integer getInt(String name, Integer defaultValue) throws SQLException
		{
			int columnIndex = getColumnIndexAndCache(this.resultSet, name);

			if (columnIndex < 1)
				return defaultValue;

			int value = this.resultSet.getInt(columnIndex);

			return (this.resultSet.wasNull() ? defaultValue : value);
		}

		protected int getColumnIndexAndCache(ResultSet rs, String name) throws SQLException
		{
			Integer columnIndex = this._nameColumnIndexMap.get(name);
			if (columnIndex == null)
			{
				columnIndex = getColumnIndex(rs, name);
				this._nameColumnIndexMap.put(name, columnIndex);
			}

			return columnIndex;
		}

		protected int getColumnIndex(ResultSet rs, String name) throws SQLException
		{
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();

			for (int i = 1; i <= columnCount; i++)
			{
				@JDBCCompatiblity("这里列名忽略大小写比较，避免不规范的驱动程序")
				String columnName = getColumnName(meta, i);
				if (columnName.equalsIgnoreCase(name))
					return i;
			}

			return -1;
		}

		public static MetaResultSet valueOf(ResultSet rs)
		{
			return new MetaResultSet(rs);
		}
	}
}
