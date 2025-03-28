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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * IO工具类。
 * 
 * @author datagear@163.com
 *
 */
public class IOUtil
{
	public static final String CHARSET_UTF_8 = "UTF-8";

	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

	public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	private IOUtil()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * 从输入流读字符串。
	 * 
	 * @param in
	 * @param encoding
	 * @param closeIn
	 * @return
	 * @throws IOException
	 */
	public static String readString(InputStream in, String encoding, boolean closeIn) throws IOException
	{
		Reader reader = getReader(in, encoding);

		return readString(reader, closeIn);
	}

	/**
	 * 从输入流读字符串。
	 * 
	 * @param in
	 * @param closeIn
	 * @return
	 * @throws IOException
	 */
	public static String readString(Reader in, boolean closeIn) throws IOException
	{
		StringWriter out = new StringWriter();

		try
		{
			write(in, out);
		}
		finally
		{
			if (closeIn)
				close(in);

			close(out);
		}

		return out.toString();
	}

	/**
	 * 从输入流读字节数组。
	 * 
	 * @param in
	 * @param closeIn
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream in, boolean closeIn) throws IOException
	{
		ByteArrayOutputStream out = null;

		try
		{
			out = new ByteArrayOutputStream();

			write(in, out);
		}
		finally
		{
			if (closeIn)
				close(in);

			close(out);
		}

		return out.toByteArray();
	}

	/**
	 * 读取字符输入流，并写入字符输出流。
	 * 
	 * @param reader
	 * @param writer
	 * @throws IOException
	 */
	public static void write(Reader reader, Writer writer) throws IOException
	{
		char[] cache = new char[512];
		int readLen = -1;

		while ((readLen = reader.read(cache)) > -1)
			writer.write(cache, 0, readLen);
	}

	/**
	 * 读取输入流，并写入输出流。
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void write(InputStream in, OutputStream out) throws IOException
	{
		byte[] cache = new byte[1024];
		int readLen = -1;

		while ((readLen = in.read(cache)) > -1)
			out.write(cache, 0, readLen);
	}

	/**
	 * 将文件写入输出流。
	 * 
	 * @param file
	 * @param out
	 * @throws IOException
	 */
	public static void write(File file, OutputStream out) throws IOException
	{
		InputStream in = null;

		try
		{
			in = getInputStream(file);

			write(in, out);
		}
		finally
		{
			close(in);
		}
	}

	/**
	 * 将输入流写入文件。
	 * 
	 * @param in
	 * @param file
	 * @throws IOException
	 */
	public static void write(InputStream in, File file) throws IOException
	{
		OutputStream out = null;

		try
		{
			out = getOutputStream(file);

			write(in, out);
		}
		finally
		{
			close(out);
		}
	}

	/**
	 * 将文件写入ZIP输出流。
	 * 
	 * @param out
	 * @param file
	 * @param zipEntryName
	 *            文件的ZIP条目名，可以为{@code null}或者空字符串。
	 * @throws IOException
	 */
	public static void writeFileToZipOutputStream(ZipOutputStream out, File file, String zipEntryName)
			throws IOException
	{
		if (!file.exists())
			return;

		boolean isDirectory = file.isDirectory();
		boolean isZipEntryNameEmpty = (zipEntryName == null || zipEntryName.isEmpty());

		if (isDirectory)
		{
			if (isZipEntryNameEmpty)
				zipEntryName = "";
			else if (!zipEntryName.endsWith("/"))
				zipEntryName = zipEntryName + "/";
		}
		else if (isZipEntryNameEmpty)
		{
			zipEntryName = file.getName();
			isZipEntryNameEmpty = false;
		}

		if (!isZipEntryNameEmpty)
		{
			ZipEntry zipEntry = new ZipEntry(zipEntryName);
			out.putNextEntry(zipEntry);
		}

		if (!isDirectory)
		{
			InputStream fileIn = null;

			try
			{
				fileIn = new FileInputStream(file);
				write(fileIn, out);
			}
			finally
			{
				close(fileIn);
			}
		}

		if (!isZipEntryNameEmpty)
			out.closeEntry();

		if (isDirectory)
		{
			File[] children = file.listFiles();

			for (File child : children)
			{
				String myName = zipEntryName + child.getName();
				writeFileToZipOutputStream(out, child, myName);
			}
		}
	}

	/**
	 * 获取输入流的{@linkplain ByteArrayInputStream}输入流。
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static ByteArrayInputStream getByteArrayInputStream(InputStream in) throws IOException
	{
		byte[] bytes = getBytes(in);

		return new ByteArrayInputStream(bytes);
	}

	/**
	 * 获取输入流的数据字节数组。
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream in) throws IOException
	{
		return getBytes(in, false);
	}

	/**
	 * 获取输入流的数据字节数组。
	 * 
	 * @param in
	 * @param closeIn
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream in, boolean closeIn) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try
		{
			write(in, out);
			return out.toByteArray();
		}
		finally
		{
			if (closeIn)
				close(in);

			close(out);
		}
	}

	/**
	 * 获取输入流。
	 * 
	 * @param file
	 * @param encoding
	 *            允许为{@code null}
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedReader getReader(File file, String encoding)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		return getReader(getInputStream(file), encoding);
	}

	/**
	 * 获取输入流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedReader getReader(File file) throws FileNotFoundException, UnsupportedEncodingException
	{
		return getReader(getInputStream(file), (String) null);
	}

	/**
	 * 获取输入流。
	 * 
	 * @param in
	 * @param charset
	 *            允许为{@code null}
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedReader getReader(InputStream in, String charset) throws UnsupportedEncodingException
	{
		if (StringUtil.isEmpty(charset))
			return new BufferedReader(new InputStreamReader(in));
		else
			return new BufferedReader(new InputStreamReader(in, charset));
	}

	/**
	 * 获取输入流。
	 * 
	 * @param in
	 * @param charset
	 *            允许为{@code null}
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) throws UnsupportedEncodingException
	{
		if (charset == null)
			return new BufferedReader(new InputStreamReader(in));
		else
			return new BufferedReader(new InputStreamReader(in, charset));
	}

	/**
	 * 获取输入流。
	 * 
	 * @param s
	 * @return
	 */
	public static StringReader getReader(String s)
	{
		return new StringReader(s);
	}

	/**
	 * 获取{@linkplain BufferedReader}。
	 * 
	 * @param reader
	 * @return
	 */
	public static BufferedReader getBufferedReader(Reader reader)
	{
		return (reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader));
	}

	/**
	 * 获取输出流。
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedWriter getWriter(File file, String encoding)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		return getWriter(getOutputStream(file), encoding);
	}

	/**
	 * 获取输出流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedWriter getWriter(File file) throws FileNotFoundException, UnsupportedEncodingException
	{
		return getWriter(getOutputStream(file), Charset.defaultCharset().name());
	}

	/**
	 * 获取输出流。
	 * 
	 * @param out
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedWriter getWriter(OutputStream out, String encoding) throws UnsupportedEncodingException
	{
		return new BufferedWriter(new OutputStreamWriter(out, encoding));
	}

	/**
	 * 获取输出流。
	 * 
	 * @param out
	 * @param charset
	 *            允许为{@code null}
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static BufferedWriter getWriter(OutputStream out, Charset charset) throws UnsupportedEncodingException
	{
		if (charset == null)
			return new BufferedWriter(new OutputStreamWriter(out));
		else
			return new BufferedWriter(new OutputStreamWriter(out, charset));
	}

	/**
	 * 获取{@linkplain BufferedWriter}。
	 * 
	 * @param writer
	 * @return
	 */
	public static BufferedWriter getBufferedWriter(Writer writer)
	{
		return (writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer));
	}

	/**
	 * 获取文件输入流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(File file) throws FileNotFoundException
	{
		return new FileInputStream(file);
	}

	/**
	 * 获取文件输入流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(String file) throws FileNotFoundException
	{
		return new FileInputStream(FileUtil.getFile(file));
	}

	/**
	 * 获取{@linkplain BufferedInputStream}。
	 * 
	 * @param in
	 * @return
	 */
	public static BufferedInputStream getBufferedInputStream(InputStream in)
	{
		return (in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in));
	}

	/**
	 * 获取文件输出流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static OutputStream getOutputStream(File file) throws FileNotFoundException
	{
		return new FileOutputStream(file);
	}

	/**
	 * 获取文件输出流。
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static OutputStream getOutputStream(String file) throws FileNotFoundException
	{
		return new FileOutputStream(FileUtil.getFile(file));
	}

	/**
	 * 获取{@linkplain BufferedOutputStream}。
	 * 
	 * @param out
	 * @return
	 */
	public static BufferedOutputStream getBufferedOutputStream(OutputStream out)
	{
		return (out instanceof BufferedOutputStream ? (BufferedOutputStream) out : new BufferedOutputStream(out));
	}

	/**
	 * 获取ZIP输入流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param in
	 * @return
	 */
	public static ZipInputStream getZipInputStream(InputStream in)
	{
		return getZipInputStream(in, CHARSET_UTF_8);
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param in
	 * @param charset 允许为{@code null}、{@code ""}
	 * @return
	 */
	public static ZipInputStream getZipInputStream(InputStream in, String charset)
	{
		if (StringUtil.isEmpty(charset))
			charset = CHARSET_UTF_8;

		return getZipInputStream(in, Charset.forName(charset));
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param in
	 * @param charset 允许为{@code null}
	 * @return
	 */
	public static ZipInputStream getZipInputStream(InputStream in, Charset charset)
	{
		if (charset == null)
			charset = Charset.forName(CHARSET_UTF_8);

		ZipInputStream zin = new ZipInputStream(in, charset);

		return zin;
	}

	/**
	 * 获取ZIP输入流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(File file) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file));
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}、{@code ""}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(File file, String charset) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file), charset);
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(File file, Charset charset) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file), charset);
	}

	/**
	 * 获取ZIP输入流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(String file) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file));
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}、{@code ""}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(String file, String charset) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file), charset);
	}

	/**
	 * 获取ZIP输入流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipInputStream getZipInputStream(String file, Charset charset) throws FileNotFoundException
	{
		return getZipInputStream(getInputStream(file), charset);
	}

	/**
	 * 获取ZIP输出流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param out
	 * @return
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out)
	{
		return getZipOutputStream(out, CHARSET_UTF_8);
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param out
	 * @param charset 允许为{@code null}、{@code ""}
	 * @return
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out, String charset)
	{
		if (StringUtil.isEmpty(charset))
			charset = CHARSET_UTF_8;

		return getZipOutputStream(out, Charset.forName(charset));
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param out
	 * @param charset 允许为{@code null}
	 * @return
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out, Charset charset)
	{
		if (charset == null)
			charset = Charset.forName(CHARSET_UTF_8);

		return new ZipOutputStream(out, charset);
	}

	/**
	 * 获取ZIP输出流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(File file) throws FileNotFoundException
	{
		return getZipOutputStream(getOutputStream(file));
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}、{@code ""}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(File file, String charset) throws FileNotFoundException
	{
		return getZipOutputStream(getOutputStream(file), charset);
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(File file, Charset charset) throws FileNotFoundException
	{
		return getZipOutputStream(getOutputStream(file), charset);
	}

	/**
	 * 获取ZIP输出流。
	 * <p>
	 * ZIP内的文件名将使用{@code UTF-8}编码。
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(String file) throws FileNotFoundException
	{
		ZipOutputStream out = new ZipOutputStream(getOutputStream(file));

		return out;
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}、{@code ""}
	 * @par
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(String file, String charset) throws FileNotFoundException
	{
		return getZipOutputStream(getOutputStream(file), charset);
	}

	/**
	 * 获取ZIP输出流。
	 * 
	 * @param file
	 * @param charset 允许为{@code null}
	 * @par
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ZipOutputStream getZipOutputStream(String file, Charset charset) throws FileNotFoundException
	{
		return getZipOutputStream(getOutputStream(file), charset);
	}

	/**
	 * 解压ZIP输入流至指定文件夹。
	 * 
	 * @param zipInputStream
	 * @param directory
	 * @throws IOException
	 */
	public static void unzip(ZipInputStream zipInputStream, File directory) throws IOException
	{
		doUnzip(zipInputStream, directory);
	}

	/**
	 * 解压ZIP输入流至指定文件夹。
	 * @param zipInputStream
	 * @param directory
	 * @throws IOException
	 * @throws MalformedZipException
	 */
	public static void unzipCheckMalformed(ZipInputStream zipInputStream, File directory) throws IOException, MalformedZipException
	{
		try
		{
			doUnzip(zipInputStream, directory);
		}
		catch(IllegalArgumentException e)
		{
			if (MalformedZipException.isMalformedZipException(e))
				throw new MalformedZipException(e);
			else
				throw e;
		}
	}
	
	/**
	 * 解压ZIP输入流至指定文件夹。
	 * @param zipInputStream
	 * @param directory
	 * @throws IOException
	 */
	protected static void doUnzip(ZipInputStream zipInputStream, File directory) throws IOException
	{
		if (!directory.exists())
			directory.mkdirs();

		ZipEntry zipEntry = null;

		while ((zipEntry = zipInputStream.getNextEntry()) != null)
		{
			File my = FileUtil.getFile(directory, zipEntry.getName());

			if (zipEntry.isDirectory())
			{
				if (!my.exists())
					my.mkdirs();
			}
			else
			{
				File parent = my.getParentFile();

				if (parent != null && !parent.exists())
					parent.mkdirs();

				OutputStream out = getOutputStream(my);

				try
				{
					write(zipInputStream, out);
				}
				finally
				{
					close(out);
				}
			}

			zipInputStream.closeEntry();
		}
	}

	/**
	 * 拷贝文件至目录内。
	 * 
	 * @param from
	 *            文件、目录
	 * @param to
	 *            目录，不存在时会自动创建
	 * @return 目标文件
	 * @throws IOException
	 */
	public static File copyInto(File from, File to) throws IOException
	{
		if (!to.exists())
			to.mkdirs();

		if (!to.isDirectory())
			throw new IllegalArgumentException("[" + to + "] must be directory");

		if (from.isDirectory())
			to = FileUtil.getDirectory(to, from.getName());
		else
			to = FileUtil.getFile(to, from.getName());

		copy(from, to, null);

		return to;
	}

	/**
	 * 拷贝文件。
	 * 
	 * @param from
	 *            文件、目录
	 * @param to
	 *            文件、目录，不存在时会自动创建
	 * @throws IOException
	 */
	public static File copy(File from, File to) throws IOException
	{
		return copy(from, to, null);
	}

	/**
	 * 拷贝文件。
	 * 
	 * @param from
	 *            文件、目录
	 * @param to
	 *            文件、目录，不存在时会自动创建
	 * @param filter
	 *            允许为{@code null}，仅拷贝接受的文件
	 * @return 目标文件，{@code null}表示没有拷贝任何文件
	 * @throws IOException
	 */
	public static File copy(File from, File to, CopyFileFilter filter) throws IOException
	{
		if (filter != null && !filter.accept(from, to))
			return null;

		if (from.isDirectory())
		{
			if (!to.exists())
				to.mkdirs();

			if (!to.isDirectory())
				throw new IllegalArgumentException("[" + to + "] must be directory");

			File[] children = from.listFiles();

			if (children != null)
			{
				for (File child : children)
					copy(child, FileUtil.getFile(to, child.getName()), filter);
			}

			return to;
		}
		else
		{
			// 如果目标时目录，应先删除
			if (to.exists() && to.isDirectory())
				FileUtil.deleteFile(to);

			OutputStream out = null;

			try
			{
				out = IOUtil.getOutputStream(to);
				IOUtil.write(from, out);
			}
			finally
			{
				IOUtil.close(out);
			}

			return to;
		}
	}

	/**
	 * 关闭{@linkplain AutoCloseable}。
	 * <p>
	 * 此方法不会抛出任何{@linkplain Throwable}。
	 * </p>
	 * 
	 * @param closeable
	 */
	public static void close(AutoCloseable closeable)
	{
		if (closeable == null)
			return;

		try
		{
			closeable.close();
		}
		catch (Throwable t)
		{
		}
	}

	/**
	 * 刷新{@linkplain Flushable}。
	 * 
	 * @param flushable
	 */
	public static void flush(Flushable flushable)
	{
		if (flushable == null)
			return;

		try
		{
			flushable.flush();
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * 如果是{@linkplain AutoCloseable}，则将其关闭。
	 * <p>
	 * 此方法不会抛出异常。
	 * </p>
	 * 
	 * @param list
	 */
	public static void closeIf(Collection<?> list)
	{
		if (list == null)
			return;

		for (Object o : list)
			closeIf(o);
	}

	/**
	 * 如果是{@linkplain AutoCloseable}，则将其关闭。
	 * <p>
	 * 此方法不会抛出异常。
	 * </p>
	 * 
	 * @param objs
	 */
	public static void closeIf(Object[] objs)
	{
		if (objs == null)
			return;

		for (Object o : objs)
			closeIf(o);
	}

	/**
	 * 如果是{@linkplain AutoCloseable}，则将其关闭。
	 * <p>
	 * 此方法不会抛出异常。
	 * </p>
	 * 
	 * @param o
	 * @return
	 */
	public static boolean closeIf(Object o)
	{
		if (o instanceof AutoCloseable)
		{
			close((AutoCloseable) o);
			return true;
		}

		return false;
	}
}
