import com.google.common.math.Quantiles;
import com.google.common.primitives.Longs;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Data
public class TicketIndex {
    private final HashSet<Ticket> tickets;
    private HashMap<String, Long> ticketsMap;

    public TicketIndex() {
        tickets = new HashSet<>(0);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public double differenceBetweenAveragePriceAndMedian (
            double averagePrice,
            double medianPrice) {
        return averagePrice >= medianPrice? averagePrice - medianPrice:
                medianPrice - averagePrice;
    }

    public double getAveragePrice() {
        long[] array = Longs.toArray(getPrices());

        return LongStream.of(array).average().getAsDouble();
    }

    public double getMedianPrice() {
        return Quantiles.median().compute(getPrices());
    }

    public HashMap<String, String> minFlightTimeMap() {
        HashMap<String, String> map = new HashMap<>(0);
        ticketsMap = minFlightTimeMapInSeconds();
        for (Map.Entry<String, Long> item : ticketsMap.entrySet()) {
            map.put(item.getKey(), timeToString(item.getValue()));
        }
        return map;
    }

    private List<Long> getPrices() {
        return tickets
                .stream()
                .map(Ticket::getPrice)
                .collect(Collectors.toList());
    }

    private String timeToString(long secs) {
        long hour = secs / 3600,
                min = secs / 60 % 60,
                sec = secs / 1 % 60;
        return String.format("%02dh %02dm %02ds", hour, min, sec);
    }

    private HashMap<String, Long> minFlightTimeMapInSeconds() {
        ticketsMap = new HashMap<>(0);
        for (Ticket ticket : tickets) {
            if (!ticketsMap.containsKey(ticket.getCarrier())) {
                ticketsMap.put(ticket.getCarrier(), ticket.getFlightTimeInSeconds());
            }

            if (ticketsMap.containsKey(ticket.getCarrier()) &
                    ticketsMap.get(ticket.getCarrier()) > ticket.getFlightTimeInSeconds()) {
                ticketsMap.put(ticket.getCarrier(), ticket.getFlightTimeInSeconds());
            }
        }

        return ticketsMap;
    }
}
