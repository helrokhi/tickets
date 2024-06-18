import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class Ticket {
    private String origin;
    private String originName;
    private String destination;
    private String destinationName;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private String carrier;
    private int stops;
    private long price;

    public long getFlightTimeInSeconds() {
        return getTimestampInSeconds(arrivalDate, arrivalTime, "Asia/Tel_Aviv") -
                getTimestampInSeconds(departureDate, departureTime, "Asia/Vladivostok");
    }

    private long getTimestampInSeconds(String date, String time, String zone) {
        DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("dd.MM.yy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate localDate = LocalDate.parse(date, dataFormat);
        LocalTime localTime = LocalTime.parse(time, timeFormat);
        ZoneId zoneId = ZoneId.of(zone);
        ZoneOffset zoneOffSet = ZoneOffset.of(String.valueOf(zoneId.getRules().getOffset(Instant.now())));

        return localTime.toEpochSecond(localDate, zoneOffSet);
    }
}
