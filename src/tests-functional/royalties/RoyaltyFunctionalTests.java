package royalties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests to verify that the service is doing what it supposed to do from the client point of view.
 * Sends actual requests to the client.
 * <p>
 * To run these tests, first start the royalty manager service and then run this.
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class RoyaltyFunctionalTests {
    private static final Logger log = LoggerFactory.getLogger(RoyaltyFunctionalTests.class);

    private String uriRoot = "http://localhost:8080/royaltymanager/";
    private RestTemplate restTemplate = new RestTemplate();

    private String buildURI(String endpoint) {
        return uriRoot + endpoint;
    }

    private HttpEntity<String> buildRequestOject(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String data = params.entrySet().stream().map(e -> String.format("\"%s\": \"%s\"", e.getKey(), e.getValue())).collect(Collectors.joining(","));
        HttpEntity<String> request = new HttpEntity<>(String.format("{%s}", data), headers);
        return request;
    }

    private ResponseEntity sendViewing(String episodeId) {
        Map<String, String> params = new HashMap<>();
        params.put("episode", episodeId);
        params.put("customer", "yyy");
        return restTemplate.postForEntity(buildURI("viewing"), buildRequestOject(params), Object.class);
    }

    @Before
    public void resetDb() throws Exception {
        restTemplate.postForEntity(buildURI("reset"), null, Object.class);
    }

    @Test
    public void addViewingRecordWithBadEpisode() {
        try {
            ResponseEntity response = sendViewing("abd");
            log.info(response.toString());
            fail("Expected exception was not thrown");
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void addViewingRecordWithGoodEpisode() {
        ResponseEntity response = sendViewing("89eb6371df374163859c5d69ae0fc561");
        log.info(response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void getPaymentWithBadStudio() {
        try {
            ResponseEntity response =restTemplate.getForEntity(buildURI("payments/badId"), Object.class);
            log.info(response.toString());
            fail("Expected exception was not thrown");
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    private String paymentByStudioResponse(String owner, String royalty, String views) {
        return String.format("{\"rightsowner\":\"%s\",\"royalty\":%s,\"viewings\":%s}", owner, royalty, views);
    }

    @Test
    public void getPaymentByStudioWIthNoViews() {
        // request views with no viewing records
        ResponseEntity response = restTemplate.getForEntity(buildURI("payments/8d713a092ebf4844840cb90d0c4a2030"), String.class);
        log.info(response.toString());
        log.info(response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paymentByStudioResponse("Sky UK", "0.00", "0"));
    }

    @Test
    public void getPaymentByStudioWithSomeViews() {
        // Add viewing records for episodes for more than one studio
        sendViewing("89eb6371df374163859c5d69ae0fc561");
        sendViewing("78a7efb2bb36491996ff562f118d5a3d");
        sendViewing("5dce6bf9a7c54103bee9f52fadd2bafe");
        ResponseEntity response = restTemplate.getForEntity(buildURI("payments/8d713a092ebf4844840cb90d0c4a2030"), String.class);
        log.info(response.toString());
        log.info(response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(paymentByStudioResponse("Sky UK", "29.34", "2"));
    }

    // Yes, I know, 'payments' shouldn't be there, but I haven't been able to get rid of it.
    private String paymentJson = "{\"payments\":[" +
            "{\"rightsowner\":\"665115721c6f44e49be3bd3e26606026\",\"name\":\"HBO\",\"royalty\":%s,\"viewings\":%s}," +
            "{\"rightsowner\":\"8d713a092ebf4844840cb90d0c4a2030\",\"name\":\"Sky UK\",\"royalty\":%s,\"viewings\":%s}," +
            "{\"rightsowner\":\"75aee18236484501b209aa36f95c7e0f\",\"name\":\"Showtime\",\"royalty\":%s,\"viewings\":%s}," +
            "{\"rightsowner\":\"49924ec6ec6c4efca4aa8b0779c89406\",\"name\":\"Fox\",\"royalty\":%s,\"viewings\":%s}" +
            "]}";

    private String allPaymentResponse(ValuePair<String, String> hbo, ValuePair<String, String> sky, ValuePair<String, String> showtime, ValuePair<String, String> fox) {
        return  String.format(paymentJson, hbo.getValue1(), hbo.getValue2(), sky.getValue1(), sky.getValue2(), showtime.getValue1(), showtime.getValue2(), fox.getValue1(), fox.getValue2());
    }

    @Test
    public void getAllPaymentsWithNoViews() {
        ValuePair<String, String> expectedHbo = new ValuePair<>("0", "0");
        ValuePair<String, String> expectedSky = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedShowtime = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedFox = new ValuePair<>("0.00", "0");

        ResponseEntity response = restTemplate.getForEntity(buildURI("payments"), String.class);
        log.info(response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(allPaymentResponse(expectedHbo, expectedSky, expectedShowtime, expectedFox));
    }

    @Test
    public void getAllPaymentsWithOneViewing() {
        sendViewing("89eb6371df374163859c5d69ae0fc561");
        ValuePair<String, String> expectedHbo = new ValuePair<>("0", "0");
        ValuePair<String, String> expectedSky = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedShowtime = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedFox = new ValuePair<>("17.34", "1");

        ResponseEntity response = restTemplate.getForEntity(buildURI("payments"), String.class);
        log.info(response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(allPaymentResponse(expectedHbo, expectedSky, expectedShowtime, expectedFox));
    }

    @Test
    public void getAllPaymentsWithSeveralViewings() {
        sendViewing("5dce6bf9a7c54103bee9f52fadd2bafe"); // Sky
        sendViewing("89eb6371df374163859c5d69ae0fc561"); // Fox
        sendViewing("5646cdd4ac874431bf40e52237d54bea"); // Showtime
        sendViewing("5dce6bf9a7c54103bee9f52fadd2bafe"); // Sky
        sendViewing("78a7efb2bb36491996ff562f118d5a3d"); // Sky
        sendViewing("1731355b2309475bb436ae938c93c801"); // Showtime

        ValuePair<String, String> expectedHbo = new ValuePair<>("0", "0");
        ValuePair<String, String> expectedSky = new ValuePair<>("44.01", "3");
        ValuePair<String, String> expectedShowtime = new ValuePair<>("26.90", "2");
        ValuePair<String, String> expectedFox = new ValuePair<>("17.34", "1");

        ResponseEntity response = restTemplate.getForEntity(buildURI("payments"), String.class);
        log.info(response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(allPaymentResponse(expectedHbo, expectedSky, expectedShowtime, expectedFox));
    }

    @Test
    public void resetResetsUsageRecords() {
        // Let's test that we actually do clear things.
        sendViewing("5dce6bf9a7c54103bee9f52fadd2bafe"); // Sky
        sendViewing("89eb6371df374163859c5d69ae0fc561"); // Fox
        sendViewing("5646cdd4ac874431bf40e52237d54bea"); // Showtime
        sendViewing("5dce6bf9a7c54103bee9f52fadd2bafe"); // Sky
        sendViewing("78a7efb2bb36491996ff562f118d5a3d"); // Sky
        sendViewing("1731355b2309475bb436ae938c93c801"); // Showtime

        ResponseEntity response = restTemplate.postForEntity(buildURI("reset"), null, Object.class);
        log.info(response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        ValuePair<String, String> expectedHbo = new ValuePair<>("0", "0");
        ValuePair<String, String> expectedSky = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedShowtime = new ValuePair<>("0.00", "0");
        ValuePair<String, String> expectedFox = new ValuePair<>("0.00", "0");

        ResponseEntity payments = restTemplate.getForEntity(buildURI("payments"), String.class);
        log.info(payments.toString());
        assertThat(payments.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(payments.getBody()).isEqualTo(allPaymentResponse(expectedHbo, expectedSky, expectedShowtime, expectedFox));
    }
}