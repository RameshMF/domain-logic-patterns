package revenue.recognition.domain.model;

public class CompleteRecognitionStrategy implements RecognitionStrategy {

	@Override
	public void calculateRevenueRecognitions(Contract contract) {
		contract.addRevenueRecognition(
				contract.getRevenue(), contract.getDateSigned());
	}

}
