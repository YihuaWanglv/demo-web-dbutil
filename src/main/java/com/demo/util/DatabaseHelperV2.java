package com.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库操作助手类
 * 
 * @author iyihua
 *
 */
public class DatabaseHelperV2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelperV2.class);

	private static final String DRIVER;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;

	private static final QueryRunner QUERY_RUNNER = new QueryRunner();

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
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("execute sql failure", e);
		}

		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.error("execute sql failure", e);
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
	public static <T> List<T> queryEntityList(Class<T> entityClass, Connection conn, String sql, Object... params) {
		List<T> entityList = null;
		try {
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass), params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("query entity list failure", e);
		} finally {
			closeConnection(conn);
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
	public static <T> List<T> queryEntityList(Class<T> entityClass, Connection conn, String sql) {
		List<T> entityList = null;
		try {
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(entityClass));
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("query entity list failure", e);
		} finally {
			closeConnection(conn);
		}
		return entityList;
	}
}
