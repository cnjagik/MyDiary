package ke.co.coansinternational.mydiary.data;

public class GetAllData {
    private String NDate, NTitle, NDetails;
    private int id;


    public GetAllData() {
    }

    public GetAllData(String NDate, String NTitle, String NDetails, int id) {
        this.NDate = NDate;
        this.NTitle = NTitle;
        this.NDetails = NDetails;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNDate() {
        return NDate;
    }


    public void setNDate(String NDate) {
        this.NDate = NDate;
    }

    public String getNTitle() {
        return NTitle;
    }

    public void setNTitle(String NTitle) {
        this.NTitle = NTitle;
    }

    public String getNDetails() {
        return NDetails;
    }

    public void setNDetails(String NDetails) {
        this.NDetails = NDetails;
    }

}
