package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWExposedFieldDefinition;
import filenet.vw.api.VWQueueDefinition;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWSystemConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QueueDefinitionHelper {
	private static Map<String, VWExposedFieldDefinition> nameToSystemFields;

	static {
		try {
			nameToSystemFields = new HashMap<String, VWExposedFieldDefinition>();
			VWExposedFieldDefinition[] fields = VWQueueDefinition.optionalSystemFields();
			for (int i = 0; i < fields.length; ++i) {
				VWExposedFieldDefinition feild = fields[i];
				nameToSystemFields.put(feild.getName(), feild);
			}
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
	}

	public static void createWorkQueueQueue(VWSession vwSession, String queueName) {
		try {
			VWSystemConfiguration sysConfig = vwSession.fetchSystemConfiguration();

			VWQueueDefinition queueDef = sysConfig.createQueueDefinition(queueName, 1);
			String[] fieldNames = { "F_Overdue", "F_Subject", "F_StepName", "F_TimeOut" };
			VWExposedFieldDefinition[] systemFields = toOptionalSystemFields(fieldNames);
			queueDef.createFieldDefinitions(systemFields);
			sysConfig.commit();
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
	}

	private static VWExposedFieldDefinition[] toOptionalSystemFields(String[] fieldNames) throws VWException {
		return toOptionalSystemFields(Arrays.asList(fieldNames));
	}

	private static VWExposedFieldDefinition[] toOptionalSystemFields(Collection<String> fieldNames) throws VWException {
		List<VWExposedFieldDefinition> systemFields = new ArrayList<VWExposedFieldDefinition>();
		for (Iterator<String> it = fieldNames.iterator(); it.hasNext();) {
			String fieldName = (String) it.next();
			VWExposedFieldDefinition field = (VWExposedFieldDefinition) nameToSystemFields.get(fieldName);
			if (field != null)
				systemFields.add(field);
			else {
				throw new IllegalArgumentException("'" + fieldName + "' is not an optional system field");
			}
		}
		return ((VWExposedFieldDefinition[]) systemFields.toArray(new VWExposedFieldDefinition[systemFields.size()]));
	}
}