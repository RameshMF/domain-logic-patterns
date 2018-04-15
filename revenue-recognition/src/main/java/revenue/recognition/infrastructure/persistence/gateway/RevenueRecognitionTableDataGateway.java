package revenue.recognition.infrastructure.persistence.gateway;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RevenueRecognitionTableDataGateway extends AbstractTableDataGateway {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public RevenueRecognitionTableDataGateway(DataSource dataSource) {
		super(dataSource);
	}

	private static final String SELECT_RECOGNITIONS_AMOUNT_SQL =
			"SELECT amount "
			+ "FROM revenueRecognitions "
			+ "WHERE contract_id = ? AND recognizedOn <= ?";

	public ResultSet findByContract(
			long contractId, LocalDate asOf) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						SELECT_RECOGNITIONS_AMOUNT_SQL);
			) {
			ps.setLong(1, contractId);
			ps.setDate(2, java.sql.Date.valueOf(asOf));
			try (ResultSet rs = ps.executeQuery()) {
				RowSetFactory factory = RowSetProvider.newFactory();
				CachedRowSet crs = factory.createCachedRowSet();
				crs.populate(rs);
				return crs;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static final String INSERT_RECOGNITION_SQL =
			"INSERT INTO revenueRecognitions ("
			+ "contract_id, amount, recognizedOn) "
			+ "VALUES (?, ?, ?)";

	public void insert(
			long contractId, BigDecimal amount, LocalDate recognizedOn) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						INSERT_RECOGNITION_SQL);
			) {
			ps.setLong(1, contractId);
			ps.setBigDecimal(2, amount);
			ps.setDate(3, java.sql.Date.valueOf(recognizedOn));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(INSERT_RECOGNITION_SQL
						.replace("?", "{}"),
						contractId, amount, recognizedOn);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
