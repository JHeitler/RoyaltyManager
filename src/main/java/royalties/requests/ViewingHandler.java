package royalties.requests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import royalties.dal.RoyaltyDao;
import royalties.dal.RoyaltyDaoJsonFileImpl;

/**
 * Manages the viewing request
 *
 * /royaltymanager/viewing
 *
 *  Add the new details to the list of views via the DAO.
 *  Views are used to calculate the royalties due to a studio.
 */
public class ViewingHandler implements RequestHandler<ViewingRequest, HttpStatus> {
    private static final Logger log = LoggerFactory.getLogger(ViewingHandler.class);

    @Autowired
    RoyaltyDao dao;

    @Override
    public HttpStatus manageRequest(ViewingRequest request) {
        log.info("Viewing Handler request starting - " + request.toString());

        // Verify that the episode is a real one.
        if (dao.getEpisode(request.getEpisode()).isPresent()) {
            dao.addViewing(request.getEpisode(), request.getCustomer());
            return HttpStatus.ACCEPTED;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
