package revenue.recognition.interfaces.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractRevenueRecognitionServiceFacadeTests {

	private final CurrencyUnit USD = Monetary.getCurrency("USD");

	protected RevenueRecognitionServiceFacade service;

	private long wordProcessorProductId;
	private long spreadsheetProductId;
	private long databaseProductId;

	@Before
	public void setUp() throws Exception {
		service = createRevenueRecognitionService();
		assertNotNull(service);
		wordProcessorProductId =
				service.insertProductInformation(
						"Microsoft Word Special Edition", "WORDPROCESSOR");
		spreadsheetProductId =
				service.insertProductInformation(
						"Microsoft Excel Anniversary Edition", "SPREADSHEET");
		databaseProductId =
				service.insertProductInformation(
						"Oracle Database 2000x", "DATABASE");
	}

	protected abstract RevenueRecognitionServiceFacade createRevenueRecognitionService();

	@After
	public void tearDown() throws Exception {
	}

	protected MonetaryAmount dollars(Number number) {
		return Monetary.getDefaultAmountFactory()
			.setNumber(number)
			.setCurrency(USD)
			.create();
	}

	protected String formatAmount(MonetaryAmount monetaryAmount) {
		MonetaryAmountFormat amountFormat =
				MonetaryFormats.getAmountFormat(
						AmountFormatQueryBuilder
							.of(Locale.US)
							.set("pattern", "#0.00")
							.build());
		return amountFormat.format(monetaryAmount);
	}

	@Test
	public void testRevenueRecognizedForWordProcessor() {
		LocalDate dateSigned = LocalDate.of(2015, Month.AUGUST, 21);
		MonetaryAmount contractPrice = dollars(2000);
		long contractId = service.insertContractInformation(
				wordProcessorProductId, contractPrice, dateSigned);
		service.calculateRevenueRecognitions(contractId);

		MonetaryAmount recognizedRevenue;
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned);
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("2000.00", formatAmount(recognizedRevenue));
	}

	@Test
	public void testRevenueRecognizedForDatabase() {
		LocalDate dateSigned = LocalDate.of(2015, Month.AUGUST, 21);
		MonetaryAmount contractPrice = dollars(5000);
		long contractId = service.insertContractInformation(
				databaseProductId, contractPrice, dateSigned);
		service.calculateRevenueRecognitions(contractId);

		MonetaryAmount recognizedRevenue;

		// 1/3 today
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned);
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("1666.67", formatAmount(recognizedRevenue));

		// 1/3 in 30 days
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned.plusDays(30));
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("3333.34", formatAmount(recognizedRevenue));

		// 1/3 in 60 days
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned.plusDays(60));
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("5000.00", formatAmount(recognizedRevenue));
	}

	@Test
	public void testRevenueRecognizedForSpreadsheet() {
		LocalDate dateSigned = LocalDate.of(2015, Month.AUGUST, 21);
		MonetaryAmount contractPrice = dollars(1000);
		long contractId = service.insertContractInformation(
				spreadsheetProductId, contractPrice, dateSigned);
		service.calculateRevenueRecognitions(contractId);

		MonetaryAmount recognizedRevenue;

		// 1/3 today
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned);
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("333.33", formatAmount(recognizedRevenue));

		// 1/3 in 60 days
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned.plusDays(60));
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("666.66", formatAmount(recognizedRevenue));

		// 1/3 in 90 days
		recognizedRevenue = service.recognizedRevenue(contractId, dateSigned.plusDays(90));
		assertEquals(USD, recognizedRevenue.getCurrency());
		assertEquals("1000.00", formatAmount(recognizedRevenue));
	}

}
