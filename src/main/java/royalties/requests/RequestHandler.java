package royalties.requests;

/**
 * Interface implemented by all request handlers
 */
public interface RequestHandler<R, A> {
    A manageRequest(R request);
}
