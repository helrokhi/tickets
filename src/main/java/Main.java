import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class Main {
    private static final String DATA_FILE = "src/main/resources/ticket-office.json";
    private static TicketIndex ticketIndex;

    public static void main(String[] args) {
        createTicketIndex();

        HashMap<String, String> map = ticketIndex
                .minFlightTimeMap();
        log.info("Минимальное время полета между городами " +
                "Владивосток и Тель-Авив для каждого авиаперевозчика {}", map );

        double averagePrice = ticketIndex.getAveragePrice();
        double medianPrice = ticketIndex.getMedianPrice();
        double difference = ticketIndex
                .differenceBetweenAveragePriceAndMedian(averagePrice, medianPrice);

        log.info("Разницу между средней ценой ({}" +
                        ") и медианой для полета ({}" +
                        ") между городами Владивосток и Тель-Авив равна {}",
                averagePrice, medianPrice, difference);
    }

    private static void createTicketIndex() {
        ticketIndex = new TicketIndex();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

            JSONArray ticketArray = (JSONArray) jsonData.get("tickets");
            parseTickets(ticketArray);

        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
    }

    private static void parseTickets(JSONArray ticketsArray) {
        ticketsArray.forEach(object -> {
            JSONObject ticketJsonObject = (JSONObject) object;
            Ticket ticket = new Ticket(
                    (String) ticketJsonObject.get("origin"),
                    (String) ticketJsonObject.get("origin_name"),
                    (String) ticketJsonObject.get("destination"),
                    (String) ticketJsonObject.get("destination_name"),
                    (String) ticketJsonObject.get("departure_date"),
                    (String) ticketJsonObject.get("departure_time"),
                    (String) ticketJsonObject.get("arrival_date"),
                    (String) ticketJsonObject.get("arrival_time"),
                    (String) ticketJsonObject.get("carrier"),
                    ((Long) ticketJsonObject.get("stops")).intValue(),
                    (Long) ticketJsonObject.get("price")
            );
            ticketIndex.addTicket(ticket);
        });
    }

    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
        return builder.toString();
    }
}
