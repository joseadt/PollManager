package es.udc.jadt.arbitrium.model.factory;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.model.entities.polltype.ConfigurationParameters;
import es.udc.jadt.arbitrium.model.util.polltype.PollType;

public final class ConfigurationParametersFactory {

	private static Map<PollType, ConfigurationParameters> instances = new EnumMap<>(PollType.class);

	private ConfigurationParametersFactory() {

	}

	public static ConfigurationParameters getConfigutacionParameters(PollType pollType) {
		ConfigurationParameters result = instances.get(pollType);

		if(result == null) {
			result = createConfiguration(pollType);
			instances.put(pollType, result);
		}

		return result;

	}

	private static ConfigurationParameters createConfiguration(PollType pollType) {

		switch (pollType) {
		case POLL:
			return basicPollConfiguration();
		case PROPOSAL:
			return proposalConfiguration();
		default:
			return new ConfigurationParameters();

		}
	}

	private static ConfigurationParameters basicPollConfiguration() {
		ConfigurationParameters config = new ConfigurationParameters();

		config.setIsMultiSelection(Boolean.FALSE);
		config.setIsUserDefinedOptions(Boolean.TRUE);
		config.setMaxOptions(Integer.valueOf(10));

		return config;
	}

	private static ConfigurationParameters proposalConfiguration() {
		ConfigurationParameters config = new ConfigurationParameters();

		config.setIsMultiSelection(Boolean.FALSE);
		config.setIsUserDefinedOptions(Boolean.FALSE);
		config.setDefaultOptions(Arrays.asList(new PollOption(null, "Yes"), new PollOption(null, "No")));
		config.setMaxOptions(Integer.valueOf(2));

		return config;
	}



}
