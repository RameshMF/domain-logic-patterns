package revenue.recognition.domain.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
@Table(name="products")
public class Product {

	@Transient
	private ProductId productId;
	
	private String name;
	private String type;

	@Transient
	private RecognitionStrategy recognitionStrategy;

	public Product(String name, String type) {
		this.name = name;
		this.type = type;
		initRecognitionStrategy();
	}

	public ProductId getProductId() {
		return productId != null ? productId : (this.productId = new ProductId(getId()));
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	protected void initRecognitionStrategy() {
		if ("WORDPROCESSOR".equals(type)) {
			recognitionStrategy = new CompleteRecognitionStrategy();
		} else if ("SPREADSHEET".equals(type)) {
			recognitionStrategy = new ThreeWayRecognitionStrategy(60, 90);
		} else if ("DATABASE".equals(type)) {
			recognitionStrategy = new ThreeWayRecognitionStrategy(30, 60);
		} else {
			throw new IllegalArgumentException(
					"Unsupported product type [" + type + "]");
		}
	}

	public void calculateRevenueRecognition(Contract contract) {
		if (! this.equals(contract.getProduct())) {
			throw new IllegalArgumentException("Contract is not for this product");
		}
		recognitionStrategy.calculateRevenueRecognitions(contract);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	protected Long getId() {
		return id;
	}
	
	@Access(AccessType.PROPERTY)
	protected void setId(Long id) {
		this.id = id;
		this.productId = new ProductId(id);
	}

	@PostLoad
	protected void onPostLoad() {
		initRecognitionStrategy();
	}

	protected Product() { /* as needed by JPA/ORM */ }

}
