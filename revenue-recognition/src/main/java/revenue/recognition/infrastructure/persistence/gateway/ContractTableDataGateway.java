package revenue.recognition.infrastructure.persistence.gateway;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class ContractTableDataGateway extends AbstractTableDataGateway {

	public ContractTableDataGateway(DataSource dataSource) {
		super(dataSource);
	}

	private static final String SELECT_CONTRACT_SQL =
		      "SELECT c.id AS id, c.product_id AS product_id, c.revenue AS revenue, c.dateSigned AS dateSigned, p.type AS type "
		      + "FROM contracts c, products p "
		      + "WHERE c.id = ? AND c.product_id = p.id";

	public ResultSet findOne(long contractId) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						SELECT_CONTRACT_SQL);
			) {
			ps.setLong(1, contractId);
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

	private static final String INSERT_CONTRACT_SQL =
			"INSERT INTO contracts (product_id, revenue, dateSigned) VALUES (?, ?, ?)";

	public long insert(
			long productId, BigDecimal contractPrice, LocalDate dateSigned) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						INSERT_CONTRACT_SQL, Statement.RETURN_GENERATED_KEYS);
			) {
			ps.setLong(1, productId);
			ps.setBigDecimal(2, contractPrice);
			ps.setDate(3, java.sql.Date.valueOf(dateSigned));
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
