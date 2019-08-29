package nil.ed.onlineexam.common;

/**
 * builder for normal response
 * @param <T> data type
 */
public class NormalResponseBuilder<T> extends ResponseBuilder<T> {
    @Override
    public Response build() {
        Response response = new Response();
        response.setData(data);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
