package io.sam_java.COVID19.Tracker.services;

import io.sam_java.COVID19.Tracker.models.locationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<locationStats> allStatsList = new ArrayList<>();

    public List<locationStats> getAllStatsList() {
        return allStatsList;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<locationStats> currentStatsList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader in = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            locationStats locationStats = new locationStats();

            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            int currentCases = Integer.parseInt(record.get(record.size() - 1));
            int prevCases = Integer.parseInt(record.get(record.size() - 2));
            locationStats.setLatest_total_cases(currentCases);
            locationStats.setDiff_total_cases(currentCases - prevCases);
            currentStatsList.add(locationStats);
        }
        allStatsList = currentStatsList;

    }

}
