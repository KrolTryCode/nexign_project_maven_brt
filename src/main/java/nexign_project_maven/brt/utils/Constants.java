package nexign_project_maven.brt.utils;

import java.math.BigDecimal;

public class Constants {
    public static final String GROUP_ID = "auth-service";

    public static final String CDR_TOPIC = "cdrTopic";
    public static final String AUTH_RECORDS_TOPIC = "authRecordsTopic";
    public static final String PAYMENT_TOPIC = "payment";

    public static final String TARIFF_DATA_TOPIC = "tariffDataTopic";
    public static final String SUBSCRIBER_DATA_TOPIC = "subscriberDataTopic";

    public static final String MONTH_EVENT_TOPIC = "monthEventTopic";

    public static final BigDecimal DEFAULT_MONEY = BigDecimal.valueOf(100);
    public static final int RANGE_PAY = 300;
    public static final int NUMBER_CHANGE_TARIFF_DOWN = 1;
    public static final int NUMBER_CHANGE_TARIFF_UPPER = 3;

}
