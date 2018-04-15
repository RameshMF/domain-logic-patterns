package revenue.recognition.table.module;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractTableModule {

	protected final DataSource dataSource;

	public AbstractTableModule(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	protected Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

}
