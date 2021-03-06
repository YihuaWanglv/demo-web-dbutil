package com.demo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库操作助手类
 * 
 * @author iyihua
 *
 */
public class DatabaseHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

	private static final String DRIVER;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;

	private static final QueryRunner QUERY_RUNNER = new QueryRunner();
	private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

	static {
		Properties conf = PropsUtil.loadProps("config.properties");
		DRIVER = conf.getProperty("jdbc.driver");
		URL = conf.getProperty("jdbc.url");
		USERNAME = conf.getProperty("jdbc.username");
		PASSWORD = conf.getProperty("jdbc.password");

		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			LOGGER.error("can not load jdbc driver", e);
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = CONNECTION_HOLDER.get();
		if (null == conn) {
			try {
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.error("execute sql failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection() {
		Connection conn = CONNECTION_HOLDER.get();
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.error("close connection failure", e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}

	/**
	 * 查询实体列表
	 * 
	 * @param entityClass
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
		List<T> entityList = null;
		try {
			Connection conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("query entity list failure", e);
			throw new RuntimeException(e);
		} finally {
			closeConnection();
		}
		return entityList;
	}

	/**
	 * 查询实体列表
	 * 
	 * @param entityClass
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql) {
		List<T> entityList = null;
		try {
			Connection conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass));
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("query entity list failure", e);
			throw new RuntimeException(e);
		} finally {
			closeConnection();
		}
		return entityList;
	}

	/**
	 * 查询实体
	 * 
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
		T entity;
		Connection conn = getConnection();
		try {
			entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<>(entityClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("query entity failure", e);
			throw new RuntimeException(e);
		} finally {
			closeConnection();
		}
		return entity;
	}

	/**
	 * 执行查询语句
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
		List<Map<String, Object>> result;
		Connection conn = getConnection();
		try {
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("execute query failure", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 执行更新语句(包括update, insert, delete)
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object... params) {
		int rows = 0;
		Connection conn = getConnection();
		try {
			rows = QUERY_RUNNER.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("execute update failure", e);
			throw new RuntimeException(e);
		} finally {
			closeConnection();
		}
		return rows;
	}

	/**
	 * 新增实体
	 * 
	 * @param enrityClass
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean insertEntity(Class<T> enrityClass, Map<String, Object> fieldMap) {
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not insert entity:fieldMap is empty");
			return false;
		}
		String sql = "insert into " + getTableName(enrityClass);
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
		values.replace(values.lastIndexOf(", "), values.length(), ")");
		sql += columns + " values " + values;
		System.err.println(sql);
		Object[] params = fieldMap.values().toArray();
		return executeUpdate(sql, params) == 1;
	}

	/**
	 * 更新实体
	 * 
	 * @param enrityClass
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	public static <T> boolean updateEntity(Class<T> enrityClass, long id, Map<String, Object> fieldMap) {
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not insert entity:fieldMap is empty");
			return false;
		}
		String sql = "update " + getTableName(enrityClass) + " set ";
		StringBuilder columns = new StringBuilder();
		for (String fieldName : fieldMap.keySet()) {
			columns.append(fieldName).append("=?, ");
		}
		sql += columns.substring(0, columns.lastIndexOf(", ")) + "where id=?";

		List<Object> paramList = new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params = paramList.toArray();
		return executeUpdate(sql, params) == 1;
	}

	/**
	 * 删除实体
	 * 
	 * @param enrityClass
	 * @param id
	 * @return
	 */
	public static <T> boolean deleteEntity(Class<T> enrityClass, long id) {
		String sql = "delete from " + getTableName(enrityClass) + " where id=?";
		return executeUpdate(sql, id) == 1;
	}

	private static String getTableName(Class<?> enrityClass) {
		return enrityClass.getSimpleName();
	}

	/**
	 * 执行sql文件
	 * 
	 * @param filePath
	 */
	public static void executeSqlFile(String filePath) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			String sql;
			while ((sql = reader.readLine()) != null) {
				executeUpdate(sql);
			}
		} catch (Exception e) {
			LOGGER.error("execute sql file failure", e);
			throw new RuntimeException(e);
		}

	}
}
