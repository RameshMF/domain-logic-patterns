package revenue.recognition.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
@Table(name="contracts")
public class Contract {

	public static final CurrencyUnit CURRENCY = Monetary.getCurrency("USD");

	@Transient
	private ContractId contractId;

	@ManyToOne
	private Product product;

	@Transient
	private MonetaryAmount revenue;

	private LocalDate dateSigned;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="contract_id", updatable=false)
	private List<RevenueRecognition> revenueRecognitions;

	public Contract(Product product, MonetaryAmount revenue, LocalDate dateSigned) {
		this.product = product;
		this.revenue = revenue;
		this.dateSigned = dateSigned;
		this.revenueRecognitions = new LinkedList<>();
	}

	public ContractId getContractId() {
		return contractId != null ? contractId : (this.contractId = new ContractId(getId()));
	}

	public Product getProduct() {
		return product;
	}

	public MonetaryAmount getRevenue() {
		return revenue;
	}

	public LocalDate getDateSigned() {
		return dateSigned;
	}
	
	public void calculateRevenueRecognitions() {
		product.calculateRevenueRecognition(this);
	}

	public void addRevenueRecognition(MonetaryAmount monetaryAmount, LocalDate dateRecognized) {
		revenueRecognitions.add(
				new RevenueRecognition(this, dateRecognized, monetaryAmount));
	}

	public List<RevenueRecognition> getRevenueRecognitions() {
		return Collections.unmodifiableList(revenueRecognitions);
	}

	@Transient
	private MonetaryAmountFactory<?> amountFactory = Monetary.getDefaultAmountFactory();

	public MonetaryAmount recognizedRevenue(LocalDate asOf) {
		MonetaryAmount result =
				amountFactory.setNumber(0L).setCurrency(CURRENCY).create();
		for (RevenueRecognition revenueRecognition : revenueRecognitions) {
			if (revenueRecognition.isRecognizableBy(asOf)) {
				result = result.add(revenueRecognition.getAmount());
			}
		}
		return result;
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
		this.contractId = new ContractId(id);
	}

	@Transient
	private String currencyCode = CURRENCY.getCurrencyCode();

	@Column(name="revenue", precision=10, scale=2)
	private BigDecimal revenue_;

	@PrePersist
	protected void onPrePersist() {
		currencyCode = revenue.getCurrency().getCurrencyCode();
		revenue_ = revenue.getNumber()
				.numberValue(BigDecimal.class)
				.setScale(revenue.getCurrency().getDefaultFractionDigits(),
						RoundingMode.HALF_EVEN);
	}

	@PostLoad
	protected void onPostLoad() {
		this.revenue =
				Monetary.getDefaultAmountFactory()
					.setNumber(revenue_)
					.setCurrency(currencyCode)
					.create();
	}

	protected Contract() { /* as needed by JPA/ORM */ }

}
