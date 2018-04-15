package revenue.recognition.infrastructure.persistence.gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class ProductTableDataGateway extends AbstractTableDataGateway {

	public ProductTableDataGateway(DataSource dataSource) {
		super(dataSource);
	}

	private static final String SELECT_PRODUCT_SQL =
			"SELECT * FROM products WHERE id = ?";

	public ResultSet findOne(long productId) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						SELECT_PRODUCT_SQL);
			) {
			ps.setLong(1, productId);
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

	private static final String INSERT_PRODUCT_SQL = 
			"INSERT INTO products (name, type) VALUES (?, ?)";

	public long insert(String name, String type) {
		try (
				Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(
						INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS);
			) {
			ps.setString(1, name);
			ps.setString(2, type);
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
