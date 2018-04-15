package revenue.recognition.table.module;

import java.sql.ResultSet;
import java.sql.SQLException;

import revenue.recognition.infrastructure.persistence.gateway.ProductTableDataGateway;

public class ProductTableModule {

	private final ProductTableDataGateway productGateway;

	public ProductTableModule(ProductTableDataGateway productGateway) {
		this.productGateway = productGateway;
	}

	public String getProductType(long productId) {
		try (ResultSet product = productGateway.findOne(productId)) {
			product.next();
			// TODO Handle if next() returns false
			return product.getString("type");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public long insert(String name, String type) {
		return productGateway.insert(name, type);
	}

}
