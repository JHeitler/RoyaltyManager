package royalties.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import royalties.dal.RoyaltyDao;

/**
 * Handles the reset request
 *
 * /royaltymanager/reset
 *
 * This request will clear all the viewing data from the datastore.
 */
public class ResetHandler implements RequestHandler<NoParams, HttpStatus> {
    @Autowired
    RoyaltyDao dao;

    @Override
    public HttpStatus manageRequest(NoParams request) {
        dao.resetViewings();
        return HttpStatus.ACCEPTED;
    }
}
