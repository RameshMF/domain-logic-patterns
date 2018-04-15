package revenue.recognition.infrastructure.persistence.gateway;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractTableDataGateway {

	private final DataSource dataSource;

	public AbstractTableDataGateway(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
