package ca.uqtr.fitbit.dto;


import lombok.Data;

@Data
public class Response {

    private Object object;
    private Error error;

    public Response(Object object, Error error) {
        this.object = object;
        this.error = error;
    }
}
