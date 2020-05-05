package ua.nure.cherkashyn.hotel.web.service.entity;

public class BookingServiceEntity {

    private String title;

    private  String message;

    private String js;

    private boolean  error;

   public BookingServiceEntity() {
       this.js = "$('#Modal').modal();";
   }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
