package support;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * https://github.com/alibaba/druid/issues/592
 * <pre>
 * Druid通过实现Hibernate接口ConnectionProvider提供对hibernate的支持：
 * public class DruidConnectionProvider implements ConnectionProvider, Configurable,Stoppable {
 * 在最新版中Druid1.0.6中，继承的是org.hibernate.service.jdbc.connections.spi.ConnectionProvider,
 * Hibernate4.3的ConnectionProvider接口声明已经被修改为：
 * org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
 * </pre>
 * <p>
 * 在hibernate.properties中不使用Druid的ConnectionProvider，而使用重新定义的ConnectionProvider：
 * <pre><i>
 * 	#Druid::注意这里配置的ConnectionProvider！！！ 第一次，配置错误了导致直接加不成功的异常！！
 * 	#hibernate.connection.provider_class=com.alibaba.druid.support.hibernate.DruidConnectionProvider
 * </i></pre>
 *
 * @author ya_feng_li@163.com
 * @date 2015年2月12日
 */
public class DruidConnectionProvider implements ConnectionProvider, Configurable, Stoppable {
  private static final long serialVersionUID = 1026193803901107651L;

  private DruidDataSource dataSource;

  public DruidConnectionProvider() {
    dataSource = new DruidDataSource();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean isUnwrappableAs(Class unwrapType) {
    return dataSource.isWrapperFor(unwrapType);
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return dataSource.unwrap(unwrapType);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void closeConnection(Connection conn) throws SQLException {
    conn.close();
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void configure(Map configurationValues) {
    try {
      DruidDataSourceFactory.config(dataSource, configurationValues);
    } catch (SQLException e) {
      throw new IllegalArgumentException("config error", e);
    }
  }

  @Override
  public void stop() {
    dataSource.close();
  }
}
