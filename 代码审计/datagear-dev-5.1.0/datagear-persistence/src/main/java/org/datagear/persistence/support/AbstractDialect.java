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

package org.datagear.persistence.support;

import java.sql.Types;

import org.datagear.meta.Column;
import org.datagear.meta.SearchableType;
import org.datagear.meta.Table;
import org.datagear.persistence.Dialect;
import org.datagear.persistence.Order;
import org.datagear.persistence.Query;
import org.datagear.util.JdbcUtil;
import org.datagear.util.NumberParser;
import org.datagear.util.Sql;

/**
 * 抽象{@linkplain Dialect}。
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractDialect extends PersistenceSupport implements Dialect
{
	/** 标识引用符 */
	private String identifierQuote;

	/** 作为关键字查询的列数 */
	private int keywordQueryColumnCount = Dialect.DEFAULT_KEYWORD_QUERY_COLUMN_COUNT;

	private NumberParser numberParser = new NumberParser();

	public AbstractDialect()
	{
		super();
	}

	public AbstractDialect(String identifierQuote)
	{
		super();
		this.identifierQuote = identifierQuote;
	}

	@Override
	public String getIdentifierQuote()
	{
		return identifierQuote;
	}

	public void setIdentifierQuote(String identifierQuote)
	{
		this.identifierQuote = identifierQuote;
	}

	@Override
	public int getKeywordQueryColumnCount()
	{
		return keywordQueryColumnCount;
	}

	public void setKeywordQueryColumnCount(int keywordQueryColumnCount)
	{
		this.keywordQueryColumnCount = keywordQueryColumnCount;
	}

	public NumberParser getNumberParser()
	{
		return numberParser;
	}

	public void setNumberParser(NumberParser numberParser)
	{
		this.numberParser = numberParser;
	}

	@Override
	public String quote(String name)
	{
		return JdbcUtil.quote(name, this.identifierQuote);
	}

	@Override
	public boolean isQuoted(String name)
	{
		return (name.length() > this.identifierQuote.length() * 2 && name.startsWith(this.identifierQuote)
				&& name.endsWith(this.identifierQuote));
	}

	@Override
	public String unquote(String name)
	{
		if (!isQuoted(name))
			return name;

		int iqLen = this.identifierQuote.length();
		return name.substring(iqLen, name.length() - iqLen);
	}

	@Override
	public Sql toKeywordQueryCondition(Table table, Query query, boolean parameterized)
	{
		Sql sql = Sql.valueOf();

		if (!query.hasKeyword())
			return sql;

		String joinOpt = (query.isNotLike() ? " AND " : " OR ");
		String likeOpt = (query.isNotLike() ? " NOT LIKE " : " LIKE ");
		String equalOpt = (query.isNotLike() ? " != " : " = ");

		Column[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++)
		{
			if (i >= this.keywordQueryColumnCount)
				break;

			Column column = columns[i];
			SearchableType searchableType = column.getSearchableType();
			String myOperator = null;
			Object myKeyword = null;

			if (SearchableType.NO.equals(searchableType))
				;
			else if (SearchableType.ONLY_LIKE.equals(searchableType) || SearchableType.ALL.equals(searchableType))
			{
				myOperator = likeOpt;
				myKeyword = wrapLikeKeyword(query.getKeyword());
			}
			else
			{
				Number number = parseToNumber(query.getKeyword(), column.getType());

				if (number != null)
				{
					myOperator = equalOpt;
					myKeyword = number;
				}
			}

			if (myOperator != null && myKeyword != null)
			{
				if (!sql.isEmpty())
					sql.sql(joinOpt);

				if (parameterized)
					sql.sql(quote(column.getName()) + myOperator + "?", createSqlParamValue(column, myKeyword));
				else
					sql.sql(quote(column.getName()) + myOperator
							+ (myKeyword instanceof Number ? query.getKeyword() : "'" + myKeyword + "'"));
			}
		}

		return sql;
	}

	@Override
	public Sql toOrderSql(Sql query, Order[] orders)
	{
		Sql orderSql = toOrderSql(orders);

		if (Sql.isEmpty(orderSql))
			return query;

		return Sql.valueOf().sql(query).sql(" ORDER BY ").sql(orderSql);
	}

	/**
	 * 转换为排序SQL。
	 * 
	 * @param orders
	 * @return 返回{@code null}表示无排序SQL
	 */
	protected Sql toOrderSql(Order... orders)
	{
		if (orders == null || orders.length == 0)
			return null;

		Sql orderSql = Sql.valueOf().delimit(", ");

		for (int i = 0; i < orders.length; i++)
		{
			Order order = orders[i];

			orderSql.sqld(quote(order.getName()) + " " + (order.isAsc() ? Order.ASC : Order.DESC));
		}

		return orderSql;
	}

	/**
	 * SQL是否为空。
	 * 
	 * @param sql
	 * @return
	 */
	protected boolean isEmptySql(Sql sql)
	{
		return Sql.isEmpty(sql);
	}

	/**
	 * 包裹Like关键字。
	 * 
	 * @param keyword
	 * @return
	 */
	protected String wrapLikeKeyword(String keyword)
	{
		if (keyword == null || keyword.isEmpty())
			return keyword;

		char first = keyword.charAt(0), last = keyword.charAt(keyword.length() - 1);

		if (first != '%' && first != '_' && last != '%' && last != '_')
			return "%" + keyword + "%";

		return keyword;
	}

	/**
	 * 将字符串转换为指定SQL类型的数值。
	 * <p>
	 * 如果{@code str}不合法、或者{@code sqlType}不是数值类型，将返回{@code null}。
	 * </p>
	 * 
	 * @param sqlType
	 * @return
	 */
	protected Number parseToNumber(String str, int sqlType)
	{
		Number value = null;

		try
		{
			switch (sqlType)
			{
				case Types.TINYINT:
				{
					value = this.numberParser.parseByteIfExact(str);
					break;
				}

				case Types.SMALLINT:
				{
					value = this.numberParser.parseShortIfExact(str);
					break;
				}

				case Types.INTEGER:
				{
					value = this.numberParser.parseIntIfExact(str);
					break;
				}

				case Types.BIGINT:
				{
					value = this.numberParser.parseLongIfExact(str);
					break;
				}

				case Types.REAL:
				case Types.FLOAT:
				{
					value = this.numberParser.parseFloatIfExact(str);
					break;
				}

				case Types.DOUBLE:
				{
					value = this.numberParser.parseDoubleIfExact(str);
					break;
				}

				case Types.NUMERIC:
				case Types.DECIMAL:
				{
					value = this.numberParser.parseBigDecimal(str);
					break;
				}

				default:
				{
					value = null;
				}
			}
		}
		catch (Throwable t)
		{
		}

		return value;
	}
}
