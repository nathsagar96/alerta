package dev.sagar.alerta.alerting.service;

import dev.sagar.alerta.alerting.dto.nasa.NasaNeoResponse;
import dev.sagar.alerta.alerting.dto.nasa.NearEarthObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class NasaNeoService {

  private final RestTemplate restTemplate;
  private final String nasaApiUrl;
  private final String nasaApiKey;
  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public NasaNeoService(
      RestTemplate restTemplate,
      @Value("${nasa.api.url}") String nasaApiUrl,
      @Value("${nasa.api.key}") String nasaApiKey) {
    this.restTemplate = restTemplate;
    this.nasaApiUrl = nasaApiUrl;
    this.nasaApiKey = nasaApiKey;
  }

  public List<NearEarthObject> fetchNeoData(LocalDate startDate, LocalDate endDate) {
    String formattedStartDate = startDate.format(dateFormatter);
    String formattedEndDate = endDate.format(dateFormatter);

    String url =
        UriComponentsBuilder.fromUriString(nasaApiUrl)
            .queryParam("start_date", formattedStartDate)
            .queryParam("end_date", formattedEndDate)
            .queryParam("api_key", nasaApiKey)
            .toUriString();

    log.info(
        "Fetching NEO data from NASA API for date range: {} to {}",
        formattedStartDate,
        formattedEndDate);

    try {
      NasaNeoResponse response = restTemplate.getForObject(url, NasaNeoResponse.class);
      if (response == null || response.getNearEarthObjects() == null) {
        log.error("Received null response from NASA API");
        return Collections.emptyList();
      }

      return flattenNeoData(response.getNearEarthObjects());
    } catch (RestClientException e) {
      log.error("Error fetching NEO data from NASA API", e);
      return Collections.emptyList();
    }
  }

  private List<NearEarthObject> flattenNeoData(
      Map<String, List<NearEarthObject>> neosGroupedByDate) {
    return neosGroupedByDate.values().stream().flatMap(List::stream).collect(Collectors.toList());
  }
}
