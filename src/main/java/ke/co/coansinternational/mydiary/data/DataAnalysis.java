package ke.co.coansinternational.mydiary.data;

public class DataAnalysis {
    private String names, clientidno, phonenumber, occupation;

    public DataAnalysis(String names, String clientidno, String phonenumber, String occupation) {
        this.names = names;
        this.clientidno = clientidno;
        this.phonenumber = phonenumber;
        this.occupation = occupation;
    }

    public DataAnalysis() {
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getClientidno() {
        return clientidno;
    }

    public void setClientidno(String clientidno) {
        this.clientidno = clientidno;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}

