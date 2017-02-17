package royalties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import royalties.dto.AllPayments;
import royalties.dto.PaymentForAllStudios;
import royalties.dto.PaymentForStudio;
import royalties.requests.NoParams;
import royalties.requests.*;

import java.util.List;

@RestController
public class RoyaltiesController {
    @Autowired
    private ViewingHandler viewingHandler;

    @Autowired
    private ResetHandler resetHandler;

    @Autowired
    private PaymentByStudioHandler paymentByStudioHandler;

    @Autowired
    private AllPaymentsHandler allPaymentsHandler;

    private NoParams noParams; // Placeholder for requests with no parameters

    @RequestMapping(value = "/royaltymanager/reset", method = RequestMethod.POST)
    public ResponseEntity<?> reset() {
        return new ResponseEntity<>(resetHandler.manageRequest(noParams));
    }

    @RequestMapping(value = "/royaltymanager/viewing", method = RequestMethod.POST)
    public ResponseEntity<?> viewing(@RequestBody ViewingRequest request) {
        return new ResponseEntity<>(viewingHandler.manageRequest(request));
    }

    @RequestMapping(value = "/royaltymanager/payments/{owner}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentForStudio> paymentsByStudio(@PathVariable("owner") String owner) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            ResponseEntity<PaymentForStudio> response = new ResponseEntity<>(paymentByStudioHandler.manageRequest(owner), responseHeaders, HttpStatus.OK);
            return response;
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/royaltymanager/payments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllPayments> paymentsAllByStudio() {
        ResponseEntity<AllPayments> response = new ResponseEntity<>(allPaymentsHandler.manageRequest(noParams), HttpStatus.OK);
        return response;
    }
}
