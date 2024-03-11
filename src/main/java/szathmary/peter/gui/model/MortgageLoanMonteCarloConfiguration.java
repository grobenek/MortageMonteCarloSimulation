package szathmary.peter.gui.model;

public record MortgageLoanMonteCarloConfiguration(
    long numberOfReplications,
    long sampleSize,
    long numberOfReplicationsToCut,
    long principalLoan) {}
