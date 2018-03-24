package com.inopek.converters;

import org.dozer.CustomConverter;

import com.inopek.enums.SinkDiameterEnum;
import com.inopek.enums.SinkPlumbOptionEnum;
import com.inopek.enums.SinkStatusEnum;
import com.inopek.enums.SinkTypeEnum;

public class EnumCustomConverter implements CustomConverter {

	@Override
	public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {

		if (sourceFieldValue == null) {
			return null;
		}
		if (SinkTypeEnum.class.isAssignableFrom(destinationClass) && Long.class.isAssignableFrom(sourceClass)) {
			return SinkTypeEnum.getSinkTypeEnumById((Long) sourceFieldValue);
		} else if (SinkStatusEnum.class.isAssignableFrom(destinationClass)
				&& Long.class.isAssignableFrom(sourceClass)) {
			return SinkStatusEnum.getSinkStatusEnumById((Long) sourceFieldValue);
		} else if (SinkDiameterEnum.class.isAssignableFrom(destinationClass)
				&& Long.class.isAssignableFrom(sourceClass)) {
			return SinkDiameterEnum.getSinkDiameterEnumById((Long) sourceFieldValue);
		} else if (SinkPlumbOptionEnum.class.isAssignableFrom(destinationClass)
				&& Long.class.isAssignableFrom(sourceClass)) {
			return SinkPlumbOptionEnum.getSinkPlumbEnumById((Long) sourceFieldValue);
		} else if (Enum.class.isAssignableFrom(sourceClass) && String.class.equals(destinationClass)) {
			return ((Enum<?>) sourceFieldValue).name();
		}
		throw new IllegalArgumentException(
				"This CustomConverter can only convert Strings and Enums!\nArguments passed in were: '"
						+ destinationClass + "' and '" + sourceClass + "'");
	}

}
