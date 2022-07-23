package fr.challenge.wedoogift.web.rest;

import fr.challenge.wedoogift.web.rest.dto.DistributeDepositDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/init.sql")
public class CompaniesControllerTestIntegration {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @ParameterizedTest
    @CsvSource(value = {"2, 3, 100, OK", "2, 4, 100, NOT_FOUND", "4, 3, 100, NOT_FOUND", "2, 3, 10000, FORBIDDEN", "2, 3, 0, FORBIDDEN", "2, 3, -1, FORBIDDEN"})
    public void testIntegration_gift(long idCompany, long idUser, int amount, HttpStatus statusAttendu) {
        DistributeDepositDto dto = new DistributeDepositDto();
        dto.setIdCompany(idCompany);
        dto.setIdUser(idUser);
        dto.setAmount(amount);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/wedoogiftback/api/companies/distributegift", dto, String.class);

        assertEquals(statusAttendu, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource(value = {"2, 3, 100, OK", "2, 4, 100, NOT_FOUND", "4, 3, 100, NOT_FOUND", "2, 3, 10000, FORBIDDEN", "2, 3, 0, FORBIDDEN", "2, 3, -1, FORBIDDEN"})
    public void testIntegration_meal(long idCompany, long idUser, int amount, HttpStatus statusAttendu) {
        DistributeDepositDto dto = new DistributeDepositDto();
        dto.setIdCompany(idCompany);
        dto.setIdUser(idUser);
        dto.setAmount(amount);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/wedoogiftback/api/companies/distributemeal", dto, String.class);

        assertEquals(statusAttendu, response.getStatusCode());
    }

    @Test
    public void testIntegration_badUrl() {
        DistributeDepositDto dto = new DistributeDepositDto();
        dto.setIdCompany(2L);
        dto.setIdUser(3L);
        dto.setAmount(100);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/wedoogiftback/api/companies/distribute", dto, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @ParameterizedTest
    @CsvSource(value = {"1, gift", "2, gift", "3, gift",
            "1, meal", "2, meal", "3, meal"})
    public void testIntegration_badDto(int numeroParametreNull, String distributeType) {
        DistributeDepositDto dto = new DistributeDepositDto();
        if (numeroParametreNull != 1) dto.setIdCompany(2L);
        if (numeroParametreNull != 2) dto.setIdUser(3L);
        if (numeroParametreNull != 3) dto.setAmount(100);

        ResponseEntity<String> response;
        if (distributeType.equals("gift"))
            response = restTemplate.postForEntity("http://localhost:" + port + "/wedoogiftback/api/companies/distributegift", dto, String.class);
        else
            response = restTemplate.postForEntity("http://localhost:" + port + "/wedoogiftback/api/companies/distributemeal", dto, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
